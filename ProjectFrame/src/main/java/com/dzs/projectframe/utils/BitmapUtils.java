package com.dzs.projectframe.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.dzs.projectframe.base.ProjectContext;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BitmapUtils {

    /**
     * 将图片写入文件
     *
     * @param bitmap   bitmap
     * @param filePath 文件绝对路径
     * @return boolean
     */
    public static boolean saveBitmap(Bitmap bitmap, String filePath) {
        if (bitmap == null || TextUtils.isEmpty(filePath)) return false;
        OutputStream out = null;
        try {
            out = new BufferedOutputStream(new FileOutputStream(filePath), 8 * 1024);
            return bitmap.compress(CompressFormat.PNG, 100, out);
        } catch (FileNotFoundException e) {
            LogUtils.exception(e);
        } finally {
            FileUtils.closeIO(out);
        }
        return false;
    }

    /**
     * 获取图片
     *
     * @param filePath 文件绝对路径
     * @return Bitmap
     */
    public static Bitmap getBitmapByPath(String filePath) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filePath);
            return BitmapFactory.decodeStream(fis);
        } catch (Exception e) {
            LogUtils.exception(e);
        } finally {
            FileUtils.closeIO(fis);
        }
        return null;
    }

    /**
     * 获取图片
     *
     * @param uri uri
     * @return Bitmap
     */
    public static Bitmap getBitmapByURI(Uri uri) {
        if (uri == null) return null;
        try {
            return MediaStore.Images.Media.getBitmap(ProjectContext.appContext.getContentResolver(), uri);
        } catch (IOException e) {
            LogUtils.exception(e);
            return null;
        }
    }

    /*根据指定比例压缩，从资源文件中获取图片*/
    @SuppressWarnings("deprecation")
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPurgeable = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        try {
            return BitmapFactory.decodeResource(res, resId, options);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    /*根据指定比例压缩，从流中获取图片*/
    @SuppressWarnings("deprecation")
    public static Bitmap decodeSampledBitmapFromStream(InputStream is, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPurgeable = true;
        BitmapFactory.decodeStream(is, null, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        try {
            return BitmapFactory.decodeStream(is, null, options);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*根据指定比例压缩，从文件中获取图片*/
    @SuppressWarnings("deprecation")
    public static Bitmap decodeSampledBitmapFromDescriptor(FileDescriptor fileDescriptor, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPurgeable = true;
        BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        try {
            return BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    /*根据指定压缩比例压缩，从数组中获取图片*/
    @SuppressWarnings("deprecation")
    public static Bitmap decodeSampledBitmapFromByteArray(byte[] data, int offset, int length, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPurgeable = true;
        BitmapFactory.decodeByteArray(data, offset, length, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(data, offset, length, options);
    }

    /*根据图片宽高计算压缩比例*/
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
            final float totalPixels = width * height;
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;
            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }

    /*图片质量压缩*/
    public static String compressAndGenImage(Bitmap image, String outPath, int maxSize) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        int options = 100;
        image.compress(CompressFormat.JPEG, options, os);
        while ((os.toByteArray().length / 1024) > maxSize && options > 0) {
            LogUtils.debug("当前大小：" + os.toByteArray().length / 1024);
            os.reset();
            options -= 10;
            image.compress(CompressFormat.JPEG, options, os);
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(outPath);
            fos.write(os.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            outPath = "";
        } finally {
            FileUtils.closeIO(fos);
        }
        return outPath;
    }
}
