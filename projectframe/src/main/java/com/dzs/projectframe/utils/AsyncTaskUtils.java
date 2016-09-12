package com.dzs.projectframe.utils;

import android.os.AsyncTask;

import com.dzs.projectframe.Conif;
import com.dzs.projectframe.base.Bean.LibEntity;
import com.dzs.projectframe.base.Bean.Upload;
import com.dzs.projectframe.interf.OnDataReturnListener;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map;


public class AsyncTaskUtils extends AsyncTask<Object, Integer, Map<String, Object>> {

    private String taskId;
    private int type;
    private boolean reflsh;//强制刷新
    private boolean saveCache;//存储缓存
    public static final int HTTP_GET = 0;
    public static final int HTTP_POST = 1;
    public static final int HTTP_POST_UPDATEIMAGE = 2;
    public static final int HTTP_POST_FORMS = 3;
    private ArrayList<OnDataReturnListener> dataReturnListeners = new ArrayList<OnDataReturnListener>();

    public AsyncTaskUtils(String taskId, int type, boolean saveCache, boolean reflsh, OnDataReturnListener... dataReturnListener) {
        this.taskId = taskId;
        this.type = type;
        this.reflsh = reflsh;
        this.saveCache = saveCache;
        for (OnDataReturnListener onDataReturnListener : dataReturnListener) {
            dataReturnListeners.add(onDataReturnListener);
        }
    }

    public AsyncTaskUtils(String taskId, int type, boolean saveCache, boolean reflsh) {
        this.taskId = taskId;
        this.type = type;
        this.reflsh = reflsh;
        this.saveCache = saveCache;
    }

    public void addDataReturnListener(OnDataReturnListener... dataReturnListener) {
        for (OnDataReturnListener onDataReturnListener : dataReturnListener) {
            dataReturnListeners.add(onDataReturnListener);
        }
    }

    public void removeDataReturnListener(OnDataReturnListener dataReturnListener) {
        dataReturnListeners.remove(dataReturnListener);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Map<String, Object> doInBackground(Object... params) {
        LibEntity libEntity = null;
        Map<String, Object> data = null;
        String result = "";
        String cacheKey = "";
        switch (type) {
            case HTTP_GET:
                try {
                    String url = StringUtils.mapToUrl(params[0].toString(),  params[1]);
                    cacheKey = StringUtils.mapToCachUrl(params[0].toString(), params[1]);
                    libEntity = HttpUtils.httpurlconnGet(url, saveCache, reflsh, cacheKey);
                } catch (UnsupportedEncodingException e) {
                    LogUtils.exception(e);
                }
                break;
            case HTTP_POST:
                try {
                    cacheKey = StringUtils.mapToCachUrl(params[0].toString(), (Map<String, Object>) params[1]);
                    libEntity = HttpUtils.HttpUrlConn_postforms(params[0].toString(),
                            (Map<String, Object>) params[1], saveCache, reflsh, cacheKey);
                } catch (UnsupportedEncodingException e) {
                    LogUtils.exception(e);
                }
                break;
            case HTTP_POST_FORMS:
                try {
                    cacheKey = StringUtils.mapToCachUrl(params[0].toString(), (Map<String, Object>) params[1]);
                    libEntity = HttpUtils.HttpUrlConn_postforms(params[0].toString(),
                            (Map<String, Object>) params[1], saveCache, reflsh, cacheKey);
                } catch (UnsupportedEncodingException e) {
                    LogUtils.exception(e);
                }
                break;
            case HTTP_POST_UPDATEIMAGE:
                String url = params[0].toString();
                Map<String, Object> map = (Map<String, Object>) params[1];
                Upload[] images = (Upload[]) params[2];
                result = HttpUtils.HttpUrlConn_Upload(url, map, images);
                break;
        }
        if (libEntity != null && libEntity.getData() != null) {
            result = new String(libEntity.getData());
            try {
                data = JsonUtils.getMap(result);
                if (libEntity.isError() && libEntity.getErrorMessage() != null) {
                    data.putAll(ResultUtils.addMessageMap(libEntity.getErrorCode(), libEntity.getErrorMessage()));
                }
            } catch (Exception e) {
                LogUtils.exception(e);
                data = ResultUtils.getErrorMap(Conif.NET_STATUS_FAIL, "数据解析失败");
            }
        } else if (libEntity != null) {
            if (libEntity.isError()) {
                data = ResultUtils.getErrorMap(Conif.NETWORK_NOTCONNECT_ERRORCode, libEntity.getErrorMessage());
            }
        } else {
            if (StringUtils.isEmpty(result)) {
                data = ResultUtils.getErrorMap(Conif.NET_STATUS_FAIL, "网络异常");
            } else {
                try {
                    data = JsonUtils.getMap(result);
                } catch (Exception e) {
                    LogUtils.exception(e);
                    data = ResultUtils.getErrorMap(Conif.NET_STATUS_FAIL, "数据解析失败");
                }
            }
        }
        return data;
    }

    @Override
    protected void onPostExecute(Map<String, Object> result) {
        super.onPostExecute(result);
        for (OnDataReturnListener on : dataReturnListeners) {
            on.onDateReturn(taskId, result);
        }
    }

}
