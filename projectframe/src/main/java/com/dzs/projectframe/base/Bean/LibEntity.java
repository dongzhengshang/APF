package com.dzs.projectframe.base.Bean;

import com.dzs.projectframe.Conif;

import java.io.Serializable;
import java.util.Map;

/**
 * 数据Bean基类
 *
 * @author DZS dzsdevelop@163.com
 * @version V1.0
 * @date 2015-12-23 上午9:47:02
 */
public class LibEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private String taskId;
    private String cachKey;// 缓存键值
    private byte[] data;// 数据
    private long saveDate; // 缓存数据的时间
    private long shelfLife; // 有效期,System.currentTimeMillis()
    private Conif.HttpResult httpResult;//请求结果
    private Map<String, Object> mapData;//数据(map格式)

    /**
     * 判断是否过期
     *
     * @return boolean
     */
    public boolean isExpired() {
        return this.shelfLife < System.currentTimeMillis();
    }

    public String getCachKey() {
        return cachKey;
    }

    public void setCachKey(String cachKey) {
        this.cachKey = cachKey;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public long getSaveDate() {
        return saveDate;
    }

    public void setSaveDate(long saveDate) {
        this.saveDate = saveDate;
    }

    public long getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(long shelfLife) {
        this.shelfLife = shelfLife;
    }

    public Conif.HttpResult getHttpResult() {
        return httpResult;
    }

    public void setHttpResult(Conif.HttpResult httpResult) {
        this.httpResult = httpResult;
    }

    public Map<String, Object> getMapData() {
        return mapData;
    }

    public void setMapData(Map<String, Object> mapData) {
        this.mapData = mapData;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
