package com.dzs.projectframe.base.Bean;

import com.dzs.projectframe.Cfg;

import java.io.Serializable;
import java.util.HashMap;

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
    private long saveDate; // 缓存数据的时间
    private long shelfLife; // 有效期,System.currentTimeMillis()
    private Cfg.NetResultType netResultType;//请求结果
    private String resultString;// 数据(json字符串格式)
    private HashMap<String, Object> resultMap;//数据(map格式)
    private boolean hasCach = false;//是否有缓存数据
    private Object extendData;//扩展数据

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

    public String getResultString() {
        return resultString;
    }

    public void setResultString(String resultString) {
        this.resultString = resultString;
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

    public Cfg.NetResultType getNetResultType() {
        return netResultType;
    }

    public void setNetResultType(Cfg.NetResultType netResultType) {
        this.netResultType = netResultType;
    }

    public HashMap<String, Object> getResultMap() {
        return resultMap;
    }

    public void setResultMap(HashMap<String, Object> resultMap) {
        this.resultMap = resultMap;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public boolean isHasCach() {
        return hasCach;
    }

    public void setHasCach(boolean hasCach) {
        this.hasCach = hasCach;
    }

    public Object getExtendData() {
        return extendData;
    }

    public void setExtendData(Object extendData) {
        this.extendData = extendData;
    }
}
