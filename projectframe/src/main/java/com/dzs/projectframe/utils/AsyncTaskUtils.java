package com.dzs.projectframe.utils;

import android.os.AsyncTask;

import com.dzs.projectframe.Conif;
import com.dzs.projectframe.base.Bean.LibEntity;
import com.dzs.projectframe.base.Bean.Upload;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;


public class AsyncTaskUtils extends AsyncTask<Object, Integer, LibEntity> {

    private String taskId;
    private HttpUtils.HttpType httpType;
    private boolean reflsh;//强制刷新
    private boolean saveCache;//存储缓存
    private ArrayList<OnNetReturnListener> dataReturnListeners = new ArrayList<OnNetReturnListener>();

    public AsyncTaskUtils(String taskId, HttpUtils.HttpType httpType, boolean saveCache, boolean reflsh, OnNetReturnListener... dataReturnListener) {
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

    public void addDataReturnListener(OnNetReturnListener... dataReturnListener) {
        Collections.addAll(dataReturnListeners, dataReturnListener);
    }

    public void removeDataReturnListener(OnNetReturnListener dataReturnListener) {
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
        String cacheKey = "";
        switch (httpType) {
            case Get:
                try {
                    String url = StringUtils.mapToUrl(params[0].toString(), (Map<String, Object>) params[1]);
                    cacheKey = StringUtils.mapToCachUrl(params[0].toString(), (Map<String, Object>) params[1], (String[]) params[2]);
                    libEntity = HttpUtils.httpURLConnect_Get(url, saveCache, reflsh, cacheKey);
                } catch (UnsupportedEncodingException e) {
                    LogUtils.exception(e);
                }
                break;
            case Json:
                try {
                    cacheKey = StringUtils.mapToCachUrl(params[0].toString(), (Map<String, Object>) params[1], (String[]) params[2]);
                    libEntity = HttpUtils.httpURLConnect_Post(params[0].toString(), (Map<String, Object>) params[1], (Upload[]) params[3], saveCache, reflsh, cacheKey, HttpUtils.HttpType.Json);
                } catch (UnsupportedEncodingException e) {
                    LogUtils.exception(e);
                }
                break;
            case Form:
                try {
                    cacheKey = StringUtils.mapToCachUrl(params[0].toString(), (Map<String, Object>) params[1], (String[]) params[2]);
                    libEntity = HttpUtils.httpURLConnect_Post(params[0].toString(), (Map<String, Object>) params[1], (Upload[]) params[3], saveCache, reflsh, cacheKey, HttpUtils.HttpType.Form);
                } catch (UnsupportedEncodingException e) {
                    LogUtils.exception(e);
                }
                break;
        }
        return libEntity;
    }

    @Override
    protected void onPostExecute(LibEntity result) {
        super.onPostExecute(result);
        for (OnNetReturnListener on : dataReturnListeners) {
            result.setTaskId(taskId);
            on.onDateReturn(result);
        }
    }

    public interface OnNetReturnListener {
        void onDateReturn(LibEntity libEntity);
    }
}
