package com.dzs.projectframe.utils;

import android.app.Activity;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.dzs.projectframe.Cfg;
import com.dzs.projectframe.base.ProjectContext;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;

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
    private static String getSDRoot() throws NullPointerException {
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

    //获取当前AppSD卡根目录
    public static String getAppRoot() throws NullPointerException {
        return getSDRoot() + File.separator + Cfg.APP_ROOT;
    }

    //获取机身储存
    public static String getInternalFileDirectory() {
        String fileDirPath = null;
        File fileDir = ProjectContext.appContext.getFilesDir();
        if (fileDir != null) {
            fileDirPath = fileDir.getPath();
        }
        return fileDirPath;
    }

    /**
     * 获取当前APP文件夹下指定目录,没有则从新创建
     *
     * @param folderName 文件夹名称
     * @return
     * @throws NullPointerException SD卡不存在会抛出异常
     */
    public static File getAppSaveFolder(String folderName) throws NullPointerException {
        File file = new File(getAppRoot() + File.separator + folderName);
        if (!file.exists()) file.mkdirs();
        return file;
    }


    /**
     * 获取SD卡APP目录下指定文件夹的绝对路径
     *
     * @param folderName 文件夹名称
     * @return
     * @throws NullPointerException SD卡不存在会抛出异常
     */
    public static String getAppSavePath(String folderName) throws NullPointerException {
        return getAppSaveFolder(folderName).getAbsolutePath();
    }

    /**
     * 在SD卡APP目录指定文件夹下创建文件
     *
     * @param folderName 文件目录名称
     * @param fileName   文件名称
     * @return
     */
    public static File getSaveFile(String folderName, String fileName) {
        File file = new File(getAppSavePath(folderName) + File.separator + fileName);
        try {
            file.createNewFile();
        } catch (IOException e) {
            LogUtils.exception(e);
        }
        return file;
    }

    /**
     * 根据文件绝对路径获取文件名
     *
     * @param filePath 文件绝对路径
     * @return String
     */
    public static String getFileName(String filePath) {
        return StringUtils.isEmpty(filePath) ? "" : filePath.substring(filePath.lastIndexOf(File.separator) + 1);
    }

    /**
     * 根据文件绝对路径获取文件名，不含后缀名
     *
     * @param filePath 文件绝对路径
     * @return String
     */
    public static String getFileNameNoFormat(String filePath) {
        return StringUtils.isEmpty(filePath) ? "" : filePath.substring(filePath.lastIndexOf(File.separator) + 1, filePath.lastIndexOf("."));
    }

    /**
     * 获取当前文件的文件夹
     *
     * @param absoluteFilePath
     * @return
     */
    public static String getPathName(String absoluteFilePath) {
        return StringUtils.isEmpty(absoluteFilePath) ? "" : absoluteFilePath.substring(0, absoluteFilePath.lastIndexOf(File.separator));
    }

    /**
     * 保存文件
     *
     * @param is         输入流
     * @param stringPath 文件路径
     */
    public static boolean saveFile(InputStream is, String stringPath) {
        File file = new File(stringPath);
        if (!file.getParentFile().exists()) if (!file.getParentFile().mkdirs()) return false;
        OutputStream outputStream = null;
        try {
            if (!file.createNewFile()) return false;
            outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len = 0;
            while (-1 != (len = is.read(buffer))) {
                outputStream.write(buffer, 0, len);
            }
            outputStream.flush();
        } catch (IOException e) {
            LogUtils.exception(e);
            return false;
        } finally {
            closeIO(is, outputStream);
        }
        return true;
    }

    /**
     * 从文件中读取字符串
     *
     * @param filePath
     * @return String
     */
    public static String readFile(String filePath) {
        InputStream is = null;
        try {
            is = new FileInputStream(filePath);
        } catch (Exception e) {
            throw new RuntimeException(FileUtils.class.getName() + "readFile---->" + filePath + " not found");
        }
        return input2String(is);
    }

    /**
     * 从Asses中读取文本
     *
     * @param context
     * @param name
     * @return String
     */
    public static String readFileFromAssets(Context context, String name) {
        InputStream is = null;
        try {
            is = context.getResources().getAssets().open(name);
        } catch (Exception e) {
            throw new RuntimeException(FileUtils.class.getName() + ".readFileFromAssets---->" + name + " not found");
        }
        return input2String(is);
    }


    /**
     * 获取文件大小
     *
     * @param filePath
     * @return
     */
    public static long getFileSize(String filePath) {
        File file = new File(filePath);
        return file.exists() ? file.length() : 0;
    }

    /**
     * 转换文件大小，以B/KB/MB/GB形式输出
     *
     * @param size 文件大小
     * @return String
     */
    public static String formatFileSize(long size) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (size < 1024) {
            fileSizeString = df.format((double) size) + "B";
        } else if (size < 1048576) {
            fileSizeString = df.format((double) size / 1024) + "KB";
        } else if (size < 1073741824) {
            fileSizeString = df.format((double) size / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) size / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /**
     * 获取文件目录大小
     *
     * @param dir
     * @return
     */
    public static long getDirSize(File dir) {
        int size = 0;
        if (dir == null || !dir.isDirectory()) return 0;
        File[] files = dir.listFiles();
        for (File file2 : files) {
            size += file2.isDirectory() ? getDirSize(file2) : file2.length();
        }
        return size;
    }

    /**
     * 获取目录中文件个数
     *
     * @param dir
     * @return
     */
    public static long getFileCount(File dir) {
        long count = 0;
        File[] files = dir.listFiles();
        count = files.length;
        for (File file : files) {
            if (file.isDirectory()) {
                count = count + getFileCount(file);// 递归
                count--;
            }
        }
        return count;
    }

    /**
     * 删除目录或文件（删除目录时包括目录中的所有文件）
     *
     * @param dir 文件/或者目录
     * @return
     */
    public static boolean deleteDirectoryAllOrFile(File dir) {
        if (dir == null) return false;
        if (dir.isDirectory()) {
            String[] Name = dir.list();
            for (String newName : Name) {
                File file = new File(dir.getPath() + File.separator + newName);
                if (file.isDirectory()) {
                    deleteDirectoryAllOrFile(file);
                } else {
                    return dir.delete();
                }
            }
            return dir.delete();
        } else {
            return dir.delete();
        }
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
     * 文件转换为byte
     *
     * @param filepath
     * @return
     */
    public static byte[] file2byte(String filepath) throws FileNotFoundException {
        InputStream in;
        in = new FileInputStream(filepath);
        return input2byte(in);
    }

    /**
     * 输入流转byte
     *
     * @param inStream
     * @return
     */
    public static byte[] input2byte(InputStream inStream) {
        if (inStream == null) return null;
        byte[] in2b = null;
        BufferedInputStream in = new BufferedInputStream(inStream);
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        int rc;
        try {
            while ((rc = in.read()) != -1) {
                swapStream.write(rc);
            }
            in2b = swapStream.toByteArray();
        } catch (IOException e) {
            LogUtils.exception(e);
        } finally {
            closeIO(inStream, in, swapStream);
        }
        return in2b;
    }

    /**
     * 输入流转字符串
     *
     * @param is
     * @return
     */
    public static String input2String(InputStream is) {
        if (null == is) return null;
        StringBuilder resultSb = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(is));
            resultSb = new StringBuilder();
            String len;
            while (null != (len = br.readLine())) {
                resultSb.append(len);
            }
        } catch (IOException e) {
            LogUtils.exception(e);
        } finally {
            closeIO(is, br);
        }
        return resultSb.toString();
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
