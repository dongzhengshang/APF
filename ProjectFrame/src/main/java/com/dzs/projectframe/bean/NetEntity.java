package com.dzs.projectframe.bean;

import android.text.TextUtils;

import com.dzs.projectframe.utils.HttpUtils;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Map;

/**
 * 网络实体类
 *
 * @author DZS dzsdevelop@163.com
 * @version V1.0
 * @date 2015-12-23 上午9:47:02
 */
public class NetEntity<T> implements Serializable {
	private static final long serialVersionUID = 1L;
	private int ACCESS_NUM = 3;//默认三次网络访问
	private int TIMEOUT_CONNECTION = 3000;//链接超时时间
	private int TIMEOUT_READ = 10000;//读取超时时间
	private HttpUtils.HttpRequestMethod requestMethod = HttpUtils.HttpRequestMethod.GET;//请求方式
	private HttpUtils.RequestType requestType = HttpUtils.RequestType.FORM;//传参方式
	
	private String taskId;//任务ID
	private String url;//网络URL
	private InputStream sslStream;//ssl证书
	private Map<String, Object> requestHead;//头部信息
	private Map<String, Object> requestParameter;//传参
	
	private NetResultType netResultType;//请求结果
	private String resultString;// 数据(json字符串格式)
	private Map<String, Object> resultMap;//数据(map格式)
	private T resultBean;//数据Bean
	private Object extendData;//扩展数据
	
	private long shelfLife; // 有效期,System.currentTimeMillis()
	private String cacheKey;//缓存KEY
	private boolean isCacheData;//标记位，用于标识是否为缓存数据
	private StringBuilder userAgent = new StringBuilder("DZSDevelop_Android");
	
	/**
	 * 判断是否过期
	 *
	 * @return boolean
	 */
	public boolean isExpired() {
		return this.shelfLife < System.currentTimeMillis();
	}
	
	/**
	 * 设置缓存
	 *
	 * @param cacheKey 缓存键值，不可重复
	 * @param second   缓存时间，单位秒
	 */
	public void setCacheTime(String cacheKey, int second) {
		this.cacheKey = cacheKey;
		this.shelfLife = second * 1000 + System.currentTimeMillis();
	}
	
	/**
	 * 获取缓存键值
	 */
	public String getCacheKey() {
		return cacheKey;
	}
	
	/**
	 * 判断是否存储
	 *
	 * @return boolean
	 */
	public boolean isSaveCache() {
		return !TextUtils.isEmpty(cacheKey) && !isExpired();
	}
	
	public StringBuilder getUserAgent() {
		return userAgent;
	}
	
	/**
	 * 添加userAgent
	 */
	public void addUserAgent(String userAgent) {
		this.userAgent.append(userAgent);
	}
	
	
	public int getACCESS_NUM() {
		return ACCESS_NUM;
	}
	
	public void setACCESS_NUM(int ACCESS_NUM) {
		this.ACCESS_NUM = ACCESS_NUM;
	}
	
	public int getTIMEOUT_CONNECTION() {
		return TIMEOUT_CONNECTION;
	}
	
	public void setTIMEOUT_CONNECTION(int TIMEOUT_CONNECTION) {
		this.TIMEOUT_CONNECTION = TIMEOUT_CONNECTION;
	}
	
	public int getTIMEOUT_READ() {
		return TIMEOUT_READ;
	}
	
	public void setTIMEOUT_READ(int TIMEOUT_READ) {
		this.TIMEOUT_READ = TIMEOUT_READ;
	}
	
	public String getTaskId() {
		return taskId;
	}
	
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public InputStream getSslStream() {
		return sslStream;
	}
	
	public void setSslStream(InputStream sslStream) {
		this.sslStream = sslStream;
	}
	
	public HttpUtils.HttpRequestMethod getRequestMethod() {
		return requestMethod;
	}
	
	public void setRequestMethod(HttpUtils.HttpRequestMethod requestMethod) {
		this.requestMethod = requestMethod;
	}
	
	public HttpUtils.RequestType getRequestType() {
		return requestType;
	}
	
	public void setRequestType(HttpUtils.RequestType requestType) {
		this.requestType = requestType;
	}
	
	public Map<String, Object> getRequestHead() {
		return requestHead;
	}
	
	public void setRequestHead(Map<String, Object> requestHead) {
		this.requestHead = requestHead;
	}
	
	public Map<String, Object> getRequestParameter() {
		return requestParameter;
	}
	
	public void setRequestParameter(Map<String, Object> requestParameter) {
		this.requestParameter = requestParameter;
	}
	
	public NetResultType getNetResultType() {
		return netResultType;
	}
	
	public void setNetResultType(NetResultType netResultType) {
		this.netResultType = netResultType;
	}
	
	public String getResultString() {
		return resultString;
	}
	
	public void setResultString(String resultString) {
		this.resultString = resultString;
	}
	
	public Map<String, Object> getResultMap() {
		return resultMap;
	}
	
	public void setResultMap(Map<String, Object> resultMap) {
		this.resultMap = resultMap;
	}
	
	public T getResultBean() {
		return resultBean;
	}
	
	public void setResultBean(T resultBean) {
		this.resultBean = resultBean;
	}
	
	public Object getExtendData() {
		return extendData;
	}
	
	public void setExtendData(Object extendData) {
		this.extendData = extendData;
	}
	
	public boolean isCacheData() {
		return isCacheData;
	}
	
	public void setCacheData(boolean cacheData) {
		isCacheData = cacheData;
	}
}
