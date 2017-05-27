package com.dzs.projectframe.utils;

import android.os.AsyncTask;

import com.dzs.projectframe.Cfg;
import com.dzs.projectframe.base.Bean.LibEntity;
import com.dzs.projectframe.base.Bean.Upload;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;


public class AsyncTaskUtils extends AsyncTask<Object, Integer, LibEntity> {

    private String taskId;
    private HttpUtils.HttpType httpType;
    private boolean reflsh;//强制刷新
    private boolean saveCache;//是否进行数据缓存
    private ArrayList<OnNetReturnListener> dataReturnListeners;

    public AsyncTaskUtils(String taskId, HttpUtils.HttpType httpType, boolean saveCache, boolean reflsh, OnNetReturnListener... dataReturnListener) {
        this.taskId = taskId;
        this.httpType = httpType;
        this.reflsh = reflsh;
        this.saveCache = saveCache;
        dataReturnListeners = new ArrayList<>();
        Collections.addAll(dataReturnListeners, dataReturnListener);
    }

    public AsyncTaskUtils(String taskId, HttpUtils.HttpType httpType, boolean saveCache, boolean reflsh) {
        this.taskId = taskId;
        this.httpType = httpType;
        this.reflsh = reflsh;
        this.saveCache = saveCache;
        dataReturnListeners = new ArrayList<>();
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
    @SuppressWarnings("unchecked")
    protected LibEntity doInBackground(Object... params) {
        LibEntity libEntity = null;
        String cacheKey;
        try {
            switch (httpType) {
                case Get:
                    String url = StringUtils.mapToUrl(params[0].toString(), (Map<String, Object>) params[2]);
                    cacheKey = saveCache ? StringUtils.mapToCatchUrl(params[0].toString(), (Map<String, Object>) params[2], (String[]) params[3]) : "";
                    libEntity = HttpUtils.httpURLConnect_Get(url, (InputStream) params[1], saveCache, reflsh, cacheKey);
                    break;
                case Json:
                    cacheKey = saveCache ? StringUtils.mapToCatchUrl(params[0].toString(), (Map<String, Object>) params[2], (String[]) params[3]) : "";
                    libEntity = HttpUtils.httpURLConnect_Post(params[0].toString(), (InputStream) params[1], (Map<String, Object>) params[2], (Upload[]) params[4],
                            saveCache, reflsh, cacheKey, HttpUtils.HttpType.Json);
                    break;
                case Form:
                    cacheKey = saveCache ? StringUtils.mapToCatchUrl(params[0].toString(), (Map<String, Object>) params[2], (String[]) params[3]) : "";
                    libEntity = HttpUtils.httpURLConnect_Post(params[0].toString(), (InputStream) params[1], (Map<String, Object>) params[2], (Upload[]) params[4],
                            saveCache, reflsh, cacheKey, HttpUtils.HttpType.Form);
                    break;
            }
        } catch (UnsupportedEncodingException e) {
            libEntity = new LibEntity();
            libEntity.setNetResultType(Cfg.NetResultType.NET_CONNECT_FAIL);
            LogUtils.error("参数转换错误");
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
