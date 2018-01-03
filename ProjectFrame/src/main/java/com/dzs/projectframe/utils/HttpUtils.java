package com.dzs.projectframe.utils;


import com.dzs.projectframe.Cfg;
import com.dzs.projectframe.base.Bean.LibEntity;
import com.dzs.projectframe.base.Bean.Upload;
import com.dzs.projectframe.base.ProjectContext;

import org.json.JSONException;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;


/**
 * http工具类
 *
 * @author DZS dzsdevelop@163.com
 * @version V2.0
 * @date 2015-12-23 上午9:55:16
 */
public class HttpUtils {

    private static final String UTF_8 = "UTF-8";
    private final static int TIMEOUT_CONNECTION = Cfg.TIMEOUT_CONNECTION;
    private final static int TIMEOUT_READ = Cfg.TIMEOUT_READ;
    private final static int RETRY_TIME = Cfg.RETRY_TIME;
    private static String BOUNDARY = UUID.randomUUID().toString();
    private static String twoHyphens = "--";
    private static String lineEnd = System.getProperty("line.separator");
    private static String HTTPS = "https";
    private static String GET = "GET";
    private static String POST = "POST";

    public static HttpURLConnection getHttpUrlConnect(String urlString, String method) throws IOException {
        HttpURLConnection connection;
        URL url = new URL(urlString);
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        setConnect(connection, method);
        connection.connect();
        return connection;
    }

    public static HttpsURLConnection getHttpsUrlConnect(String urlString, String method, InputStream inputStream) throws Exception {
        HttpsURLConnection connection;
        URL url = new URL(urlString);
        if (inputStream == null) initSSLAll();
        connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        setConnect(connection, method);
        connection.connect();
        if (inputStream != null) initSSL(connection, inputStream);
        return connection;
    }

    public static void setConnect(URLConnection connection, String method) throws IOException {
        connection.setDoInput(true);
        connection.setDoOutput(method.equals(POST));
        connection.setUseCaches(false);
        connection.setConnectTimeout(TIMEOUT_CONNECTION);
        connection.setReadTimeout(TIMEOUT_READ);
        connection.setRequestProperty("Accept-Charset", UTF_8);
        connection.setRequestProperty("User-Agent", Cfg.getUserAgent().toString());
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Content-Type", "multipart/form-data" + "; boundary=" + BOUNDARY);
    }

    /**
     * 获取缓存数据
     *
     * @param url     URL地址（key）
     * @param isForce 为true时候无论是否过期都会返回数据
     * @return LibEntity
     */
    public static LibEntity getCatch(String url, boolean isForce) {
        LibEntity cacheLibEntity = DiskLruCacheHelpUtils.getInstance().getCatch(url);
        return cacheLibEntity == null ? null : isForce ? cacheLibEntity : (cacheLibEntity.isExpired() ? null : cacheLibEntity);
    }

    /**
     * get请求
     *
     * @param url         url地址
     * @param inputStream 证书
     * @param saveCache   是否进行缓存
     * @param reflsh      是否强制刷新（为true后不读取缓存）
     * @return libentity
     */
    public static LibEntity httpURLConnect_Get(String url, InputStream inputStream, boolean saveCache, boolean reflsh, String cachkey) {
        int time = 0;
        LibEntity libEntity = null;
        InputStream is = null;
        HttpURLConnection connection = null;
        HttpsURLConnection httpsURLConnection = null;
        if (!SystemUtils.checkNetConttent(ProjectContext.appContext)) {
            LibEntity tempCache = getCatch(cachkey, true);
            libEntity = tempCache != null ? tempCache : new LibEntity();
            libEntity.setHasCache(tempCache != null);
            libEntity.setNetResultType(Cfg.NetResultType.NET_NOT_CONNECT);
            return libEntity;
        }
        // 如果没有开启强制刷新,先读取缓存
        if (!reflsh && saveCache) {
            libEntity = getCatch(cachkey, false);
            if (libEntity != null) {
                libEntity.setNetResultType(Cfg.NetResultType.NET_CONNECT_SUCCESS);
                return libEntity;
            }
        }
        boolean isHttps = url.startsWith(HTTPS);
        //进行三次访问网络
        do {
            try {
                connection = isHttps ? null : getHttpUrlConnect(url, GET);
                httpsURLConnection = isHttps ? getHttpsUrlConnect(url, GET, inputStream) : null;
                int statueCode = isHttps ? httpsURLConnection.getResponseCode() : connection.getResponseCode();
                if (statueCode != HttpURLConnection.HTTP_OK) {
                    time++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        LogUtils.exception(e1);
                    }
                    continue;
                }
                is = isHttps ? httpsURLConnection.getInputStream() : connection.getInputStream();
                libEntity = new LibEntity();
                String resultString = FileUtils.input2String(is);
                LogUtils.info("Network-URL(GET)地址：" + url + "\n返回状态值" + statueCode + "\n返回值：" + resultString);
                libEntity.setResultString(resultString);
                libEntity.setResultMap(JsonUtils.getMap(resultString));
                libEntity.setCacheKey(cachkey);
                libEntity.setShelfLife(System.currentTimeMillis() + Cfg.getCacheTime());
                libEntity.setNetResultType(Cfg.NetResultType.NET_CONNECT_SUCCESS);
                if (saveCache) DiskLruCacheHelpUtils.getInstance().putCatch(cachkey, libEntity);
                break;
            } catch (JSONException e) {
                LogUtils.exception(e);
                libEntity = new LibEntity();
                libEntity.setNetResultType(Cfg.NetResultType.NET_PARSE_FAIL);
                break;
            } catch (Exception e) {
                LogUtils.exception(e);
                time++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    LogUtils.exception(e1);
                }
            } finally {
                if (connection != null) {
                    connection.disconnect();
                    connection = null;
                }
                if (httpsURLConnection != null) {
                    httpsURLConnection.disconnect();
                    httpsURLConnection = null;
                }
                FileUtils.closeIO(is);
            }
        } while (time < RETRY_TIME);
        if (libEntity == null) {
            LibEntity tempCache = getCatch(cachkey, true);
            libEntity = tempCache == null ? new LibEntity() : tempCache;
            libEntity.setHasCache(tempCache != null);
            libEntity.setNetResultType(Cfg.NetResultType.NET_CONNECT_FAIL);
        }
        return libEntity;
    }

    /**
     * post请求，以表单形式提交
     *
     * @param url       网络地址
     * @param params    参数
     * @param saveCache 是否进行缓存
     * @param reflsh    是否进行强制刷新
     * @return LibEntity
     */
    public static LibEntity httpURLConnect_Post(String url, InputStream inputStream, Map<String, Object> params, Upload[] files, boolean saveCache, boolean reflsh, String cachkey, HttpType httpType) {
        int time = 0;
        LibEntity libEntity = null;
        InputStream is = null;
        HttpURLConnection connection = null;
        HttpsURLConnection httpsURLConnection = null;
        if (!SystemUtils.checkNetConttent(ProjectContext.appContext)) {
            LibEntity tempCache = getCatch(cachkey, true);
            libEntity = tempCache != null ? tempCache : new LibEntity();
            libEntity.setHasCache(tempCache != null);
            libEntity.setNetResultType(Cfg.NetResultType.NET_NOT_CONNECT);
            return libEntity;
        }
        // 如果没有开启强制刷新,先读取缓存
        if (!reflsh) {
            libEntity = getCatch(cachkey, false);
            if (libEntity != null) {
                libEntity.setNetResultType(Cfg.NetResultType.NET_CONNECT_SUCCESS);
                return libEntity;
            }
        }
        boolean isHttps = url.startsWith(HTTPS);
        //进行三次访问网络
        do {
            try {
                LogUtils.info("Network-URL(POST_FORMS)：" + StringUtils.mapToUrl(url, params));
                connection = isHttps ? null : getHttpUrlConnect(url, POST);
                httpsURLConnection = isHttps ? getHttpsUrlConnect(url, POST, inputStream) : null;
                DataOutputStream dataOutputStream = new DataOutputStream(isHttps ? httpsURLConnection.getOutputStream() : connection.getOutputStream());
                switch (httpType) {
                    case Json:
                        addJsonField(params, dataOutputStream);
                        break;
                    case Form:
                        addFormField(params.entrySet(), dataOutputStream);
                        break;
                    default:
                        break;
                }
                if (files != null) addImageContent(files, dataOutputStream);
                // 数据结束标志
                dataOutputStream.writeBytes(twoHyphens + BOUNDARY + twoHyphens + lineEnd);
                dataOutputStream.flush();
                int statueCode = isHttps ? httpsURLConnection.getResponseCode() : connection.getResponseCode();
                if (statueCode != HttpURLConnection.HTTP_OK) {
                    time++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        LogUtils.exception(e1);
                    }
                    continue;
                }
                is = isHttps ? httpsURLConnection.getInputStream() : connection.getInputStream();
                libEntity = new LibEntity();
                String resultString = FileUtils.input2String(is);
                LogUtils.info("Network-URL(POST_FORMS)地址：" + StringUtils.mapToUrl(url, params) + "\n返回状态值: " + statueCode + "\n返回值：" + resultString);
                libEntity.setResultString(resultString);
                libEntity.setResultMap(JsonUtils.getMap(resultString));
                libEntity.setCacheKey(cachkey);
                libEntity.setShelfLife(System.currentTimeMillis() + Cfg.getCacheTime());
                libEntity.setNetResultType(Cfg.NetResultType.NET_CONNECT_SUCCESS);
                if (saveCache) {
                    DiskLruCacheHelpUtils.getInstance().putCatch(cachkey, libEntity);
                }
                dataOutputStream.close();
                break;
            } catch (JSONException e) {
                LogUtils.exception(e);
                libEntity = new LibEntity();
                libEntity.setNetResultType(Cfg.NetResultType.NET_PARSE_FAIL);
                break;
            } catch (Exception e) {
                LogUtils.exception(e);
                time++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    LogUtils.exception(e1);
                }
            } finally {
                if (connection != null) {
                    connection.disconnect();
                    connection = null;
                }
                if (httpsURLConnection != null) {
                    httpsURLConnection.disconnect();
                    httpsURLConnection = null;
                }
                FileUtils.closeIO(is);
            }
        } while (time < RETRY_TIME);
        if (libEntity == null) {
            LibEntity tempCache = getCatch(cachkey, true);
            libEntity = tempCache == null ? new LibEntity() : tempCache;
            libEntity.setHasCache(tempCache != null);
            libEntity.setNetResultType(Cfg.NetResultType.NET_CONNECT_FAIL);
        }
        return libEntity;
    }


    /**
     * 添加图片/文件
     *
     * @param files  图片文件
     * @param output 数据流
     */
    private static void addImageContent(Upload[] files, DataOutputStream output) throws IOException {
        if (files != null) {
            for (Upload file : files) {
                StringBuilder split = new StringBuilder();
                split.append(twoHyphens).append(BOUNDARY).append(lineEnd);
                split.append("Content-Disposition:form-data; name=\"")
                        .append(file.getFormName()).append("\"; filename=\"")
                        .append(file.getFileName()).append("\"").append(lineEnd);
                split.append("Content-Type:").append(file.getContentType()).append(lineEnd);
                split.append(lineEnd);
                output.writeBytes(split.toString());
                output.write(file.getData(), 0, file.getData().length);
                output.writeBytes(lineEnd);
            }
        }
    }

    /**
     * 添加表单数据
     *
     * @param params 参数列表
     * @param output 输出流
     */
    private static void addFormField(Set<Map.Entry<String, Object>> params, DataOutputStream output) throws IOException {
        if (params != null) {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, Object> param : params) {
                sb.append(twoHyphens).append(BOUNDARY).append(lineEnd);
                sb.append("Content-Disposition: form-data; name=\"").append(param.getKey()).append("\"").append(lineEnd);
                sb.append(lineEnd);
                sb.append(param.getValue()).append(lineEnd);
            }
            output.write(new String(sb.toString().getBytes(), UTF_8).getBytes());
        }
    }

    /**
     * 添加json数据
     *
     * @param params 参数
     * @param output 输入流
     */
    private static void addJsonField(Map<String, Object> params, DataOutputStream output) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(twoHyphens).append(BOUNDARY).append(lineEnd);
        sb.append("Content-Disposition: form-data; name=\"params\"").append(lineEnd);
        sb.append(lineEnd);
        sb.append(JsonUtils.mapToJsonStr(params)).append(lineEnd);
        output.write(new String(sb.toString().getBytes(), UTF_8).getBytes());
    }

    public enum HttpType {
        Get, Json, Form, GET_NO_ENCODE
    }

    private static void initSSL(HttpsURLConnection httpsURLConnection, InputStream inputStream) throws Exception {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        Certificate ca = cf.generateCertificate(inputStream);
        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        keystore.load(null, null);
        keystore.setCertificateEntry("ca", ca);
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keystore);
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, tmf.getTrustManagers(), null);
        httpsURLConnection.setSSLSocketFactory(context.getSocketFactory());
    }

    private static void initSSLAll() throws Exception {
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }}, null);
        HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String arg0, SSLSession arg1) {
                return true;
            }
        });
    }
}
