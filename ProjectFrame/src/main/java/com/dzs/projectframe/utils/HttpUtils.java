package com.dzs.projectframe.utils;


import android.text.TextUtils;

import com.dzs.projectframe.bean.NetEntity;
import com.dzs.projectframe.bean.NetResultType;
import com.dzs.projectframe.bean.Upload;
import com.dzs.projectframe.base.ProjectContext;

import org.json.JSONException;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
	private static final String HTTPS = "https";
	private static final String BOUNDARY = UUID.randomUUID().toString();
	private static final String twoHyphens = "--";
	private static final String lineEnd = System.getProperty("line.separator");
	
	/**
	 * 请求方式
	 */
	public enum HttpRequestMethod {
		GET("GET"), POST("POST"), PUT("PUT"), DELETE("DELETE");
		private String name;
		
		HttpRequestMethod(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
	}
	
	/**
	 * 请求方法
	 */
	public enum RequestType {
		JSON, FORM, IMAGE;
	}
	
	/**
	 * 获取HttpUrlConnect
	 *
	 * @param urlString url
	 * @return HttpURLConnection
	 */
	private static HttpURLConnection getHttpUrlConnect(String urlString, NetEntity netEntity) throws IOException {
		HttpURLConnection connection;
		URL url = new URL(urlString);
		connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod(netEntity.getRequestMethod().getName());
		setConnect(connection, netEntity);
		connection.connect();
		return connection;
	}
	
	/**
	 * 获取HttpsUrlConnect
	 *
	 * @param urlString url
	 * @return HttpURLConnection
	 */
	private static HttpsURLConnection getHttpsUrlConnect(String urlString, NetEntity netEntity) throws Exception {
		HttpsURLConnection connection;
		URL url = new URL(urlString);
		if (netEntity.getSslStream() == null) initSSLAll();
		connection = (HttpsURLConnection) url.openConnection();
		connection.setRequestMethod(netEntity.getRequestMethod().getName());
		setConnect(connection, netEntity);
		connection.connect();
		if (netEntity.getSslStream() != null) initSSL(connection, netEntity.getSslStream());
		return connection;
	}
	
	/**
	 * 设置 URLConnection
	 *
	 * @param connection URLConnection
	 */
	private static void setConnect(URLConnection connection, NetEntity netEntity) {
		connection.setDoInput(true);
		connection.setDoOutput(netEntity.getRequestMethod() != HttpRequestMethod.GET);
		connection.setUseCaches(false);
		connection.setConnectTimeout(netEntity.getTIMEOUT_CONNECTION());
		connection.setReadTimeout(netEntity.getTIMEOUT_READ());
		connection.setRequestProperty("User-Agent", netEntity.getUserAgent().toString());
		connection.setRequestProperty("Accept-Charset", StandardCharsets.UTF_8.toString());
		connection.setRequestProperty("Connection", "Keep-Alive");
		connection.setRequestProperty("Accept", "application/json");
		@SuppressWarnings("unchecked")
		Map<String, Object> head = netEntity.getRequestHead();
		if (head != null && !head.isEmpty()) {
			for (Map.Entry<String, Object> entry : head.entrySet()) {
				connection.setRequestProperty(entry.getKey(), entry.getValue() + "");
			}
		}
		connection.setRequestProperty("Content-Type", "multipart/form-data" + "; boundary=" + BOUNDARY);
	}
	
	/**
	 * 获取缓存数据
	 *
	 * @param netEntity 数据BEAN
	 * @param isForce   为true时候无论是否过期都会返回数据
	 * @return NetEntity
	 */
	public static NetEntity getCatch(NetEntity netEntity, boolean isForce) {
		if (netEntity == null || TextUtils.isEmpty(netEntity.getCacheKey())) {
			return null;
		}
		NetEntity cacheNetEntity = DiskLruCacheHelpUtils.getInstance().getCatch(netEntity.getCacheKey());
		return cacheNetEntity == null ? null : isForce ? cacheNetEntity : (cacheNetEntity.isExpired() ? null : cacheNetEntity);
	}
	
	
	/**
	 * 请求数据
	 *
	 * @return NetEntity
	 */
	public static NetEntity getData(NetEntity netEntity) {
		//网络未连接时候，读取缓存文件
		if (!SystemUtils.checkNetConttent(ProjectContext.appContext)) {
			NetEntity tempCache = getCatch(netEntity, true);
			netEntity = tempCache != null ? tempCache : new NetEntity();
			netEntity.setCacheData(tempCache != null);
			netEntity.setNetResultType(NetResultType.NET_NOT_CONNECT);
			return netEntity;
		}
		//默认设置为网络连接失败
		netEntity.setNetResultType(NetResultType.NET_CONNECT_FAIL);
		String url = netEntity.getUrl();
		HttpRequestMethod method = netEntity.getRequestMethod();
		@SuppressWarnings("unchecked")
		Map<String, Object> params = netEntity.getRequestParameter();
		int accessNum = 0;
		InputStream is = null;
		HttpURLConnection connection = null;
		HttpsURLConnection httpsURLConnection = null;
		DataOutputStream dataOutputStream = null;
		
		boolean isHttps = url.startsWith(HTTPS);
		//进行多次访问网络
		do {
			try {
				String tempURL = mapToCatchUrl(url, params);
				if (method == HttpRequestMethod.GET) {
					url = tempURL;
				}
				connection = isHttps ? null : getHttpUrlConnect(url, netEntity);
				httpsURLConnection = isHttps ? getHttpsUrlConnect(url, netEntity) : null;
				if (method != HttpRequestMethod.GET) {
					dataOutputStream = new DataOutputStream(isHttps ? httpsURLConnection.getOutputStream() : connection.getOutputStream());
					if (netEntity.getRequestType() == RequestType.FORM) {
						addFormField(params.entrySet(), dataOutputStream);
					} else if (netEntity.getRequestType() == RequestType.JSON) {
						addJsonField(params, dataOutputStream);
					}
					// 数据结束标志
					dataOutputStream.writeBytes(twoHyphens + BOUNDARY + twoHyphens + lineEnd);
					dataOutputStream.flush();
				}
				int statueCode = isHttps ? httpsURLConnection.getResponseCode() : connection.getResponseCode();
				if (statueCode != HttpURLConnection.HTTP_OK) {
					accessNum++;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						LogUtils.exception(e1);
					}
					continue;
				}
				is = isHttps ? httpsURLConnection.getInputStream() : connection.getInputStream();
				String resultString = FileUtils.input2String(is);
				netEntity.setResultString(resultString);
				netEntity.setResultMap(JsonUtils.jsonToMap(resultString));
				netEntity.setNetResultType(NetResultType.NET_CONNECT_SUCCESS);
				if (TextUtils.isEmpty(netEntity.getCacheKey()) && !netEntity.isExpired()) {
					DiskLruCacheHelpUtils.getInstance().putCatch(netEntity.getCacheKey(), netEntity);
				}
				LogUtils.info("Network-URL(POST_FORMS)地址：" + mapToCatchUrl(url, params) + "\n返回状态值: " + statueCode + "\n返回值：" + resultString);
				break;
			} catch (JSONException e) {
				netEntity.setNetResultType(NetResultType.NET_PARSE_FAIL);
				break;
			} catch (Exception e) {
				LogUtils.exception(e);
				accessNum++;
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
				FileUtils.closeIO(dataOutputStream, is);
			}
		} while (accessNum < netEntity.getACCESS_NUM());
		if (netEntity.getNetResultType() == NetResultType.NET_CONNECT_FAIL) {
			NetEntity tempCache = getCatch(netEntity, true);
			netEntity = tempCache != null ? tempCache : new NetEntity();
			netEntity.setCacheData(tempCache != null);
			netEntity.setNetResultType(NetResultType.NET_CONNECT_FAIL);
		}
		return netEntity;
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
				sb.append(param.getValue()).append(lineEnd);
			}
			output.write(new String(sb.toString().getBytes(), StandardCharsets.UTF_8).getBytes());
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
		sb.append(JsonUtils.mapToJsonStr(params)).append(lineEnd);
		output.write(new String(sb.toString().getBytes(), StandardCharsets.UTF_8).getBytes());
	}
	
	public static String mapToCatchUrl(String url, Map<String, Object> parameters, String... variableKey) throws UnsupportedEncodingException {
		StringBuilder getUrl = new StringBuilder(url);
		if (parameters != null && !parameters.isEmpty()) {
			getUrl.append("?");
			for (Map.Entry<String, Object> param : parameters.entrySet()) {
				if (variableKey != null) {
					for (String key : variableKey) {
						if (param.getKey().equals(key)) break;
					}
				}
				getUrl.append(param.getKey()).append("=").append(URLEncoder.encode(param.getValue() == null ? "" : param.getValue().toString(), StandardCharsets.UTF_8.name()));
				getUrl.append("&");
			}
			getUrl.deleteCharAt(getUrl.length() - 1);
		}
		return getUrl.toString();
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
