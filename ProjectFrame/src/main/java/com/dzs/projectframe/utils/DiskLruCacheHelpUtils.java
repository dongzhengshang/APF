package com.dzs.projectframe.utils;

import android.text.TextUtils;

import com.dzs.projectframe.bean.NetEntity;
import com.dzs.projectframe.base.ProjectContext;
import com.dzs.projectframe.interf.Cache;
import com.dzs.projectframe.utils.lib.DiskLruCache;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;


/**
 * 缓存工具类
 *
 * @author DZS dzsk@outlook.com
 * @version V1.0
 * @date 2015-12-31 下午3:47:54
 */
public class DiskLruCacheHelpUtils implements Cache<NetEntity> {
    private static DiskLruCacheHelpUtils diskLruCacheHelpUtils;
    private static int CACHE_SIZE = 20 * 1024 * 1024;// 默认缓存大小为20M
    private static String CACHE_PATH = FileUtils.getAppFile(ProjectContext.appContext, "DataCache");
    private DiskLruCache mDiskLruCache;
    private int DISK_CACHE_INDEX = 0;

    private DiskLruCacheHelpUtils() {
        initDiskCache(CACHE_PATH, CACHE_SIZE);
    }

    public static DiskLruCacheHelpUtils getInstance() {
        if (diskLruCacheHelpUtils == null) {
            synchronized (DiskLruCacheHelpUtils.class) {
                if (diskLruCacheHelpUtils == null) {
                    diskLruCacheHelpUtils = new DiskLruCacheHelpUtils();
                }
            }
        }
        return diskLruCacheHelpUtils;
    }

    /*初始化缓存*/
    private void initDiskCache(String cachePath, int cacheSize) {
        if (TextUtils.isEmpty(cachePath) || cacheSize == 0) {
            LogUtils.info("DiskCache-路径为空或者缓存大小为0");
            return;
        }
        if (mDiskLruCache == null || mDiskLruCache.isClosed()) {
            File file = new File(cachePath);
            if (!file.exists()) file.mkdirs();
            try {
                mDiskLruCache = DiskLruCache.open(file, ProjectContext.appContext.getApplicationVersion(), 1, cacheSize);
                LogUtils.info("Disk cache initialized");
            } catch (IOException e) {
                LogUtils.error("initDiskCache - " + e);
            }
        }
    }

    @Override
    public void putCatch(String key, NetEntity data) {
        if (TextUtils.isEmpty(key) || data == null) return;
        if (mDiskLruCache != null && !mDiskLruCache.isClosed()) {
            String cacheKey = EncryptionUtils.MD5encode(key);
            OutputStream out = null;
            ObjectOutputStream objectOutputStream = null;
            try {
                DiskLruCache.Editor editor = mDiskLruCache.edit(cacheKey);
                if (editor != null) {
                    out = editor.newOutputStream(DISK_CACHE_INDEX);
                    objectOutputStream = new ObjectOutputStream(out);
                    objectOutputStream.writeObject(data);
                    objectOutputStream.flush();
                    editor.commit();
                }
            } catch (IOException e) {
                LogUtils.exception(e);
            } finally {
                FileUtils.closeIO(objectOutputStream, out);
            }
        }

    }

    @Override
    public NetEntity getCatch(String key) {
        if (mDiskLruCache != null && !mDiskLruCache.isClosed() && !TextUtils.isEmpty(key)) {
            String cacheKey = EncryptionUtils.MD5encode(key);
            InputStream inputStream = null;
            try {
                DiskLruCache.Snapshot snapshot = mDiskLruCache.get(cacheKey);
                if (snapshot != null) {
                    inputStream = snapshot.getInputStream(DISK_CACHE_INDEX);
                    if (inputStream != null) {
                        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                        return (NetEntity) objectInputStream.readObject();
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                LogUtils.error("getBitmapFromDiskCache - " + e);
            } finally {
                FileUtils.closeIO(inputStream);
            }
        }
        return null;
    }

    @Override
    public void evictAll() {
        if (mDiskLruCache != null && !mDiskLruCache.isClosed()) {
            try {
                mDiskLruCache.delete();
                LogUtils.info("Disk cache cleared");
            } catch (IOException e) {
                LogUtils.error("clearCache - " + e);
            }
        }
    }

    @Override
    public void remove(String key) {
        if (mDiskLruCache != null && !mDiskLruCache.isClosed() && !TextUtils.isEmpty(key)) {
            try {
                mDiskLruCache.remove(EncryptionUtils.MD5encode(key));
                LogUtils.info("Disk cache remove");
            } catch (IOException e) {
                LogUtils.exception(e);
            }
        }
    }

    /**
     * 刷新缓存
     */
    public void flush() {
        if (mDiskLruCache != null && !mDiskLruCache.isClosed()) {
            try {
                mDiskLruCache.flush();
                LogUtils.info("Disk cache flushed");
            } catch (IOException e) {
                LogUtils.exception(e);
            }
        }
    }

    /**
     * 关闭磁盘缓存
     */
    public void close() {
        if (mDiskLruCache != null && !mDiskLruCache.isClosed()) {
            try {
                mDiskLruCache.close();
                mDiskLruCache = null;
                LogUtils.info("Disk cache closed");
            } catch (IOException e) {
                LogUtils.error("close - " + e);
            }
        }
    }

}
