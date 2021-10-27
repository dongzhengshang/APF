package com.dzs.projectframe.interf;

/**
 * 缓存接口
 *
 * @param <T>
 * @author DZS dzsk@outlook.com
 * @version 1.0
 * @date 2015-11-3 上午11:54:55
 */
public interface Cache<T> {
    void putCatch(String key, T t);

    T getCatch(String key);

    void evictAll();

    void remove(String key);
}
