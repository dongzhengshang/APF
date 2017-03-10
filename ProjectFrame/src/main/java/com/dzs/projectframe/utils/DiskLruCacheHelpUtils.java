package com.dzs.projectframe.utils;

import com.dzs.projectframe.base.Bean.LibEntity;
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
 * @author DZS dzsdevelop@163.com
 * @version V1.0
 * @date 2015-12-31 下午3:47:54
 */
public class DiskLruCacheHelpUtils implements Cache<LibEntity> {
    private static DiskLruCacheHelpUtils diskLruCacheHelpUtils;
    private static int CACHE_SIZE = 20 * 1024 * 1024;// 默认缓存大小为20M
    private static String CACHE_PATH = FileUtils.getAppFile(ProjectContext.appContext, "Cache");
    private DiskLruCache mDiskLruCache;
    private final Object mDiskCacheLock = new Object();
    private boolean mDiskCacheStarting = true;
    private int DISK_CACHE_INDEX = 0;

    private DiskLruCacheHelpUtils() {
        initDiskCache(CACHE_PATH, CACHE_SIZE);
    }

    public static DiskLruCacheHelpUtils getInstanse() {
        if (diskLruCacheHelpUtils == null) {
            synchronized (DiskLruCacheHelpUtils.class) {
                if (diskLruCacheHelpUtils == null) {
                    diskLruCacheHelpUtils = new DiskLruCacheHelpUtils(CACHE_PATH, CACHE_SIZE);
                }
            }
        }
        return diskLruCacheHelpUtils;
    }

    public DiskLruCacheHelpUtils(String cachePath, int cacheSize) {
        initDiskCache(cachePath, cacheSize);
    }

    private void initDiskCache(String cachePath, int cacheSize) {
        if (StringUtils.isEmail(cachePath) || cacheSize == 0) {
            LogUtils.info("DiskCache-路径为空或者缓存大小为0");
            return;
        } else {
            CACHE_PATH = cachePath;
            CACHE_SIZE = cacheSize;
        }
        synchronized (mDiskCacheLock) {
            if (mDiskLruCache == null || mDiskLruCache.isClosed()) {
                File file = new File(cachePath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                try {
                    mDiskLruCache = DiskLruCache.open(new File(cachePath), ProjectContext.appContext
                            .getApplicationVersion(), 1, cacheSize);
                    LogUtils.info("Disk cache initialized");
                } catch (IOException e) {
                    LogUtils.error("initDiskCache - " + e);
                }
            }
            mDiskCacheStarting = false;
            mDiskCacheLock.notifyAll();
        }
    }

    @Override
    public void putCatch(String key, LibEntity data, boolean isCover) {
        if (StringUtils.isEmpty(key) || data == null) {
            return;
        }
        synchronized (mDiskCacheLock) {
            if (mDiskLruCache != null) {
                final String cachekey = EncryptionUtils.MD5encode(key);
                OutputStream out = null;
                ObjectOutputStream objectOutputStream = null;
                DiskLruCache.Snapshot snapshot;
                try {
                    snapshot = mDiskLruCache.get(cachekey);
                    if (snapshot == null) {
                        put(cachekey, out, objectOutputStream, data);
                    } else {
                        if (isCover) {
                            put(cachekey, out, objectOutputStream, data);
                        } else {
                            snapshot.getInputStream(DISK_CACHE_INDEX).close();
                        }
                    }
                } catch (IOException e) {
                    LogUtils.exception(e);
                } finally {
                    FileUtils.closeIO(objectOutputStream, out);
                }
            }
        }

    }

    private void put(String cachekey, OutputStream out, ObjectOutputStream objectOutputStream, LibEntity data)
            throws IOException {
        final DiskLruCache.Editor editor = mDiskLruCache.edit(cachekey);
        if (editor != null) {
            out = editor.newOutputStream(DISK_CACHE_INDEX);
            objectOutputStream = new ObjectOutputStream(out);
            objectOutputStream.writeObject(data);
            objectOutputStream.flush();
            editor.commit();
        }
    }

    @Override
    public LibEntity getCatch(String key) {
        LibEntity libEntity = null;
        final String cacheKey = EncryptionUtils.MD5encode(key);
        synchronized (mDiskCacheLock) {
            while (mDiskCacheStarting) {
                try {
                    mDiskCacheLock.wait();
                } catch (InterruptedException e) {
                    LogUtils.exception(e);
                }
            }
            if (mDiskLruCache != null) {
                InputStream inputStream = null;
                try {
                    final DiskLruCache.Snapshot snapshot = mDiskLruCache.get(cacheKey);
                    if (snapshot != null) {
                        inputStream = snapshot.getInputStream(DISK_CACHE_INDEX);
                        if (inputStream != null) {
                            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                            libEntity = (LibEntity) objectInputStream.readObject();
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    LogUtils.error("getBitmapFromDiskCache - " + e);
                } finally {
                    FileUtils.closeIO(inputStream);
                }
            }
        }
        return libEntity;
    }

    @Override
    public void evictAll() {
        synchronized (mDiskCacheLock) {
            mDiskCacheStarting = true;
            if (mDiskLruCache != null && !mDiskLruCache.isClosed()) {
                try {
                    mDiskLruCache.delete();
                    LogUtils.info("Disk cache cleared");
                } catch (IOException e) {
                    LogUtils.error("clearCache - " + e);
                }
                mDiskLruCache = null;
                initDiskCache(CACHE_PATH, CACHE_SIZE);
            }
        }
    }

    @Override
    public void remove(String key) {
        synchronized (mDiskCacheLock) {
            if (mDiskLruCache != null && !StringUtils.isEmpty(key)) {
                try {
                    mDiskLruCache.remove(key);
                    LogUtils.info("Disk cache remove");
                } catch (IOException e) {
                    LogUtils.exception(e);
                }
            }
        }
    }

    public void flush() {
        synchronized (mDiskCacheLock) {
            if (mDiskLruCache != null) {
                try {
                    mDiskLruCache.flush();
                    LogUtils.info("Disk cache flushed");
                } catch (IOException e) {
                    LogUtils.exception(e);
                }
            }
        }
    }

    public void close() {
        synchronized (mDiskCacheLock) {
            if (mDiskLruCache != null) {
                try {
                    if (!mDiskLruCache.isClosed()) {
                        mDiskLruCache.close();
                        mDiskLruCache = null;
                        LogUtils.info("Disk cache closed");
                    }
                } catch (IOException e) {
                    LogUtils.error("close - " + e);
                }
            }
        }
    }

}
