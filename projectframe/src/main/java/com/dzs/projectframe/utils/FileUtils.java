package com.dzs.projectframe.utils;

import android.app.Activity;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * 文件处理
 *
 * @author DZS dzsdevelop@163.com
 * @version V1.4
 * @date 2015-11-6 上午11:50:37
 */
public class FileUtils {

    // 检查SD卡是否存在
    public static boolean checkSDcard() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    // 获取SD卡根目录
    private static String getSDRoot() {
        if (!checkSDcard()) throw new NullPointerException("SD card does not exist.");
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    //获取缓存目录
    public static String getAppCache(Context context, String folderName) {
        return !checkSDcard() ? context.getCacheDir() + File.separator + folderName
                : context.getExternalCacheDir() + File.separator + folderName;
    }

    //获取缓文件目录
    public static String getAppFile(Context context, String folderName) {
        return !checkSDcard() ? context.getFilesDir() + File.separator + folderName
                : context.getExternalFilesDir(File.separator) + File.separator + folderName;
    }


    /**
     * 把uri转为File对象
     *
     * @param aty activity
     * @param uri uri
     * @return File
     */
    public static File uri2File(Activity aty, Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(aty, uri, projection, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return new File(cursor.getString(column_index));
    }

    /**
     * 复制文件
     *
     * @param from   File
     * @param toFile File
     */
    public static void copyFile(File from, File toFile) {
        if (null == from || !from.exists() || null == toFile) return;
        FileInputStream is = null;
        FileOutputStream os = null;
        try {
            is = new FileInputStream(from);
            if (!toFile.exists()) toFile.createNewFile();
            os = new FileOutputStream(toFile);
            copyFileFast(is, os);
        } catch (Exception e) {
            throw new RuntimeException(FileUtils.class.getClass().getName(), e);
        } finally {
            closeIO(is, os);
        }
    }

    /**
     * 快速复制文件[采用NIO]
     *
     * @param is 数据来源
     * @param os 数据目标
     * @throws IOException
     */
    public static void copyFileFast(FileInputStream is, FileOutputStream os) throws IOException {
        FileChannel in = is.getChannel();
        FileChannel out = os.getChannel();
        in.transferTo(0, in.size(), out);
    }

    /**
     * 关闭数据流
     *
     * @param closeables 数据流
     */
    public static void closeIO(Closeable... closeables) {
        if (null == closeables || closeables.length <= 0) return;
        for (Closeable cb : closeables) {
            try {
                if (null == cb) continue;
                cb.close();
            } catch (IOException e) {
                throw new RuntimeException(FileUtils.class.getClass().getName(), e);
            }
        }
    }
}
