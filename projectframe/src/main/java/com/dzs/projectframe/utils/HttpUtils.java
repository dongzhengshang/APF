package com.dzs.projectframe.utils;


import com.dzs.projectframe.Conif;
import com.dzs.projectframe.base.Bean.LibEntity;
import com.dzs.projectframe.base.Bean.Upload;
import com.dzs.projectframe.base.ProjectContext;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


/**
 * http工具类
 *
 * @author DZS dzsdevelop@163.com
 * @version V2.0
 * @date 2015-12-23 上午9:55:16
 */
public class HttpUtils {

    public static final String UTF_8 = "UTF-8";
    private final static int TIMEOUT_CONNECTION = Conif.TIMEOUT_CONNECTION;
    private final static int TIMEOUT_READ = Conif.TIMEOUT_READ;
    private final static int RETRY_TIME = Conif.RETRY_TIME;
    private static String BOUNDARY = UUID.randomUUID().toString();
    private static String twoHyphens = "--";
    private static String lineEnd = System.getProperty("line.separator");

    public static HttpURLConnection getHttpUrlConnect(String urlString, String method) throws IOException {
        HttpURLConnection connection = null;
        URL url = new URL(urlString);
        connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(method.equals("POST"));
        connection.setUseCaches(false);
        connection.setRequestMethod(method);
        connection.setConnectTimeout(TIMEOUT_CONNECTION);
        connection.setReadTimeout(TIMEOUT_READ);
        connection.setRequestProperty("Accept-Charset", UTF_8);
        connection.setRequestProperty("User-Agent", Conif.getUserAgent().toString());
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Content-Type", "multipart/form-data" + "; boundary=" + BOUNDARY);
        connection.connect();
        return connection;
    }

    /**
     * 获取缓存数据
     *
     * @param url     URL地址（key）
     * @param isForce 为true时候无论是否过期都会返回数据
     * @return LibEntity
     */
    public static LibEntity getCatch(String url, boolean isForce) {
        LibEntity cacheLibEntity = DiskLruCacheHelpUtils.getInstanse().getCatch(url);
        return cacheLibEntity == null ? null : isForce ? cacheLibEntity : (cacheLibEntity.isExpired() ? null : cacheLibEntity);
    }

    /**
     * get请求
     *
     * @param url       url地址
     * @param saveCache 是否进行缓存
     * @param reflsh    是否强制刷新（为true后不读取缓存）
     * @return libentity
     */
    public static LibEntity httpURLConnect_Get(String url, boolean saveCache, boolean reflsh, String cachkey) {
        int time = 0;
        LibEntity libEntity = null;
        InputStream is = null;
        HttpURLConnection connection = null;
        if (!SystemUtils.checkNetConttent(ProjectContext.appContext)) {
            libEntity = getCatch(cachkey, true);
            libEntity = libEntity == null ? new LibEntity() : libEntity;
            libEntity.setHttpResult(Conif.HttpResult.NetNotConnect);
            return libEntity;
        }
        // 如果没有开启强制刷新,先读取缓存
        if (!reflsh && getCatch(cachkey, false) != null) {
            return getCatch(cachkey, false);
        }
        //进行三次访问网络
        do {
            try {
                LogUtils.info("Network-URL(GET)：" + url);
                connection = getHttpUrlConnect(url, "GET");
                int statueCode = connection.getResponseCode();
                if (statueCode != HttpURLConnection.HTTP_OK) {
                    LogUtils.info("Network-URL(GET)返回状态值：" + statueCode);
                    time++;
                    if (time < RETRY_TIME) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e1) {
                            LogUtils.exception(e1);
                        }
                        continue;
                    }
                }
                is = connection.getInputStream();
                libEntity = new LibEntity();
                String resultString = FileUtils.input2String(is);
                libEntity.setMapData(JsonUtils.getMap(resultString));
                libEntity.setCachKey(cachkey);
                libEntity.setShelfLife(System.currentTimeMillis() + Conif.getCacheTime());
                libEntity.setHttpResult(Conif.HttpResult.Success);
                LogUtils.info("Network-URL(GET)返回值：" + resultString);
                if (saveCache)
                    DiskLruCacheHelpUtils.getInstanse().putCatch(cachkey, libEntity, true);
                break;
            } catch (JSONException e) {
                LogUtils.exception(e);
                libEntity.setHttpResult(Conif.HttpResult.ParseFaile);
                break;
            } catch (Exception e) {
                time++;
                if (time < RETRY_TIME) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        LogUtils.exception(e1);
                    }
                    continue;
                }
                LogUtils.exception(e);
            } finally {
                if (connection != null) {
                    connection.disconnect();
                    connection = null;
                }
                FileUtils.closeIO(is);
            }
        } while (time < RETRY_TIME);
        if (libEntity == null) {
            libEntity = getCatch(cachkey, true) == null ? new LibEntity() : libEntity;
            libEntity.setHttpResult(Conif.HttpResult.Faile);
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
    public static LibEntity httpURLConnect_Post(String url, Map<String, Object> params, Upload[] files, boolean saveCache, boolean reflsh, String cachkey, HttpType httpType) {
        int time = 0;
        LibEntity libEntity = null;
        InputStream is = null;
        HttpURLConnection connection = null;
        if (!SystemUtils.checkNetConttent(ProjectContext.appContext)) {
            libEntity = getCatch(cachkey, true);
            libEntity = libEntity == null ? new LibEntity() : libEntity;
            libEntity.setHttpResult(Conif.HttpResult.NetNotConnect);
            return libEntity;
        }
        // 如果没有开启强制刷新,先读取缓存
        if (!reflsh && getCatch(cachkey, false) != null) {
            return getCatch(cachkey, false);
        }
        //进行三次访问网络
        do {
            try {
                LogUtils.info("Network-URL(POST_FORMS)：" + url);
                LogUtils.info("Network-URL(POST_FORMS-GET地址)：" + StringUtils.mapToUrl(url, params));
                connection = getHttpUrlConnect(url, "POST");
                DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
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
                if (files != null) {
                    addImageContent(files, dataOutputStream);
                }
                // 数据结束标志
                dataOutputStream.writeBytes(twoHyphens + BOUNDARY + twoHyphens + lineEnd);
                dataOutputStream.flush();
                int statueCode = connection.getResponseCode();
                if (statueCode != HttpURLConnection.HTTP_OK) {
                    LogUtils.info("Network-URL(POST_FORMS)返回状态值：" + statueCode);
                    time++;
                    continue;
                }
                is = connection.getInputStream();
                libEntity = new LibEntity();
                String resultString = FileUtils.input2String(is);
                libEntity.setMapData(JsonUtils.getMap(resultString));
                libEntity.setCachKey(cachkey);
                libEntity.setShelfLife(System.currentTimeMillis() + Conif.getCacheTime());
                libEntity.setHttpResult(Conif.HttpResult.Success);
                LogUtils.info("Network-URL(POST_FORMS)返回值：" + resultString);
                if (saveCache) {
                    DiskLruCacheHelpUtils.getInstanse().putCatch(cachkey, libEntity, true);
                }
                dataOutputStream.close();
                break;
            } catch (JSONException e) {
                LogUtils.exception(e);
                libEntity.setHttpResult(Conif.HttpResult.ParseFaile);
                break;
            } catch (Exception e) {
                time++;
                if (time < RETRY_TIME) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        LogUtils.exception(e);
                    }
                    continue;
                }
                LogUtils.exception(e);
            } finally {
                if (connection != null) {
                    connection.disconnect();
                    connection = null;
                }
                FileUtils.closeIO(is);
            }
        } while (time < RETRY_TIME);
        if (libEntity == null) {
            libEntity = getCatch(cachkey, true) == null ? new LibEntity() : libEntity;
            libEntity.setHttpResult(Conif.HttpResult.Faile);
        }
        return libEntity;
    }


    /**
     * 添加图片/文件
     *
     * @param files
     * @param output
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
     * @param params
     * @param output
     */
    private static void addJsonField(Map<String, Object> params, DataOutputStream output) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(twoHyphens).append(BOUNDARY).append(lineEnd);
        sb.append("Content-Disposition: form-data; name=\"params\"").append(lineEnd);
        sb.append(lineEnd);
        JSONObject json = new JSONObject(params);
        sb.append(json.toString()).append(lineEnd);
        output.write(new String(sb.toString().getBytes(), UTF_8).getBytes());
    }

    public enum HttpType {
        Get, Json, Form
    }
}
