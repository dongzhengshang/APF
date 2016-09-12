package com.dzs.projectframe.base.Bean;

import java.io.Serializable;

/**
 * 数据Bean基类
 *
 * @author DZS dzsdevelop@163.com
 * @version V1.0
 * @date 2015-12-23 上午9:47:02
 */
public class LibEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private String baseId;
    private String cachKey;// 缓存键值
    private byte[] data;// 数据
    private long saveDate; // 缓存数据的时间
    private long shelfLife; // 有效期,System.currentTimeMillis()
    private String ErrorCode;//异常码

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

    public String getBaseId() {
        return baseId;
    }

    public void setBaseId(String baseId) {
        this.baseId = baseId;
    }

    public String getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(String errorCode) {
        ErrorCode = errorCode;
    }

}
