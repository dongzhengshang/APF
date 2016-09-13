package com.dzs.projectframe.utils;

import android.os.AsyncTask;

import com.dzs.projectframe.Conif;
import com.dzs.projectframe.base.Bean.LibEntity;
import com.dzs.projectframe.base.Bean.Upload;
import com.dzs.projectframe.interf.OnDataReturnListener;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;


public class AsyncTaskUtils extends AsyncTask<Object, Integer, LibEntity> {

    private String taskId;
    private HttpUtils.HttpType httpType;
    private boolean reflsh;//强制刷新
    private boolean saveCache;//存储缓存
    private ArrayList<OnDataReturnListener> dataReturnListeners = new ArrayList<OnDataReturnListener>();

    public AsyncTaskUtils(String taskId, HttpUtils.HttpType httpType, boolean saveCache, boolean reflsh, OnDataReturnListener... dataReturnListener) {
        this.taskId = taskId;
        this.httpType = httpType;
        this.reflsh = reflsh;
        this.saveCache = saveCache;
        Collections.addAll(dataReturnListeners, dataReturnListener);
    }

    public AsyncTaskUtils(String taskId, HttpUtils.HttpType httpType, boolean saveCache, boolean reflsh) {
        this.taskId = taskId;
        this.httpType = httpType;
        this.reflsh = reflsh;
        this.saveCache = saveCache;
    }

    public void addDataReturnListener(OnDataReturnListener... dataReturnListener) {
        Collections.addAll(dataReturnListeners, dataReturnListener);
    }

    public void removeDataReturnListener(OnDataReturnListener dataReturnListener) {
        dataReturnListeners.remove(dataReturnListener);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    @SuppressWarnings ("unchecked")
    protected LibEntity doInBackground(Object... params) {
        LibEntity libEntity = null;
        Map<String, Object> data = null;
        String result = "";
        String cacheKey = "";
        switch (httpType) {
            case Get:
                String url = null;
                try {
                    url = StringUtils.mapToUrl(params[0].toString(), (Map<String, Object>) params[1]);
                    cacheKey = StringUtils.mapToCachUrl(params[0].toString(), (Map<String, Object>) params[1], (String[]) params[2]);
                    libEntity = HttpUtils.httpURLConnect_Get(url, saveCache, reflsh, cacheKey);
                } catch (UnsupportedEncodingException e) {
                    LogUtils.exception(e);
                }
                break;
            case Json:
                try {
                    cacheKey = StringUtils.mapToCachUrl(params[0].toString(), (Map<String, Object>) params[1], (String[]) params[2]);
                    libEntity = HttpUtils.httpURLConnect_Post(params[0].toString(), (Map<String, Object>) params[1], saveCache, reflsh, cacheKey, HttpUtils.HttpType.Json);
                } catch (UnsupportedEncodingException e) {
                    LogUtils.exception(e);
                }
                break;
            case Form:
                try {
                    cacheKey = StringUtils.mapToCachUrl(params[0].toString(), (Map<String, Object>) params[1], (String[]) params[2]);
                    libEntity = HttpUtils.httpURLConnect_Post(params[0].toString(), (Map<String, Object>) params[1], saveCache, reflsh, cacheKey, HttpUtils.HttpType.Form);
                } catch (UnsupportedEncodingException e) {
                    LogUtils.exception(e);
                }
                break;
            case Upload:
                result = HttpUtils.HttpUrlConn_Upload(params[0].toString(), (Map<String, Object>) params[1], (Upload[]) params[2]);
                break;
        }
        return libEntity;
    }

    @Override
    protected void onPostExecute(LibEntity result) {
        super.onPostExecute(result);
        for (OnDataReturnListener on : dataReturnListeners) {
            on.onDateReturn(taskId, result.getHttpResult(), result.);
        }
    }

    public interface OnDataReturnListener {
        void onDateReturn(String id, Conif.HttpResult httpResult, Map<String, Object> result);
    }
}
