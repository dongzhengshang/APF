package com.dzs.projectframe.base;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;

import com.dzs.projectframe.adapter.ViewHolder;
import com.dzs.projectframe.utils.AsyncTaskUtils;
import com.dzs.projectframe.utils.DiskLruCacheHelpUtils;
import com.dzs.projectframe.utils.FileUtils;
import com.dzs.projectframe.utils.LogUtils;
import com.dzs.projectframe.utils.SharedPreferUtils;
import com.dzs.projectframe.utils.StackUtils;
import com.dzs.projectframe.utils.TostUtils;

import java.io.File;

/**
 * @author DZS dzsdevelop@163.com
 * @version V1.0
 * @date 2016/8/19.
 */
public abstract class ProjectActivity extends FragmentActivity implements View.OnClickListener, AsyncTaskUtils.OnNetReturnListener {
    protected ViewHolder viewUtils;
    protected Resources resources;
    protected SharedPreferUtils sharedPreferUtils;

    protected abstract int setContent();

    protected abstract void initView();

    protected abstract void initAnimation();

    protected abstract void initData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StackUtils.getInstanse().addActivity(this);
        setTextAbout();
        viewUtils = ViewHolder.get(this, setContent());
        setContentView(viewUtils.getView());
        resources = ProjectContext.resources;
        sharedPreferUtils = ProjectContext.sharedPreferUtils;
        initView();
        initAnimation();
        initData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        DiskLruCacheHelpUtils.getInstanse().flush();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        StackUtils.getInstanse().finishActivity(this);
    }

    /**
     * 设置字体相关
     */
    private void setTextAbout() {
        Resources res = getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
    }

    /**
     * 跳转到某一Activity
     *
     * @param activity activity
     * @param isFinish 是否销毁当前界面
     */
    protected void Intent(Class activity, boolean isFinish) {
        Intent(activity, null, isFinish);
    }

    /**
     * 跳转到某一Activity
     *
     * @param activity activity
     * @param data     数据Bean,需要实现Serializable接口
     * @param isFinish 是否销毁当前界面
     */
    protected void Intent(Class activity, Class data, boolean isFinish) {
        Intent intent = new Intent(this, activity);
        if (data != null) intent.putExtra(activity.getName(), data);
        startActivity(intent);
        if (isFinish) finish();
    }

    /**
     * 设置全屏
     */
    protected void setFullScream() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    //------------------相机操作相关------------------------
    protected static final int SYS_INTENT_REQUEST = 0XFF01;//系统相册
    protected static final int CAMERA_INTENT_REQUEST = 0XFF02;//调用系统相机
    protected static final int IMAGE_CUT = 0XFF03;//裁剪
    protected Uri photoUri = Uri.fromFile(FileUtils.getSaveFile("TempImage", "tempPhoto.jpeg"));
    protected File file = FileUtils.getSaveFile("TempImage", "temp.jpeg");
    protected Uri imageUri = Uri.fromFile(file);
    protected String imagePath = file.getAbsolutePath();

    //调用系统相册
    protected void systemPhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(intent, SYS_INTENT_REQUEST);
    }

    //调用系统相机
    protected void cameraPhoto() {
        try {
            if (!FileUtils.checkSDcard()) {
                TostUtils.showOneToast("SD卡不可用!");
                return;
            }
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, CAMERA_INTENT_REQUEST);
        } catch (Exception e) {
            TostUtils.showOneToast("系统相机调取失败");
            LogUtils.exception(e);
        }
    }

    //图片剪切
    protected void cropImageUri(Uri uri, int aspectX, int aspectY, int outputX, int outputY, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, requestCode);

    }
}
