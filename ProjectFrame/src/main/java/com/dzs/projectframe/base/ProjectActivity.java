package com.dzs.projectframe.base;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewbinding.ViewBinding;

import com.dzs.projectframe.Cfg;
import com.dzs.projectframe.R;
import com.dzs.projectframe.adapter.ViewHolder;
import com.dzs.projectframe.bean.NetEntity;
import com.dzs.projectframe.broadcast.Receiver;
import com.dzs.projectframe.utils.ActivityUtils;
import com.dzs.projectframe.utils.AsyncTaskUtils;
import com.dzs.projectframe.utils.DiskLruCacheHelpUtils;
import com.dzs.projectframe.utils.FileUtils;
import com.dzs.projectframe.utils.LogUtils;
import com.dzs.projectframe.utils.SharedPreferUtils;
import com.dzs.projectframe.utils.SystemUtils;

import java.io.File;

/**
 * @author DZS dzsdevelop@163.com
 * @version V1.0
 * @date 2016/8/19.
 */
public abstract class ProjectActivity extends FragmentActivity implements View.OnClickListener, AsyncTaskUtils.OnNetReturnListener, Receiver.OnBroadcastReceiverListener {
    public ViewHolder viewUtils;
    protected Resources resources;
    protected SharedPreferUtils sharedPreferUtils;
    protected PowerManager.WakeLock wakeLock;

    protected abstract ViewBinding setContentByViewBinding();

    protected abstract void initView();

    protected abstract void initData();

    protected View setContentByView() {
        return null;
    }
    protected  int setContentById(){
        return 0;
    }


    /*设置View之前做的一些操作*/
    protected void setContentViewBefore() {
    }

    /*设置View之后做的一些操作*/
    protected void setContentViewAfter() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUtils.getInstanse().addActivity(this);
        ProjectContext.appContext.addReceiver(this);
        resources = ProjectContext.resources;
        sharedPreferUtils = ProjectContext.sharedPreferUtils;
        setTextAbout();
        setContentViewBefore();
        int layoutId = setContentById();
        View view = setContentByView();
        ViewBinding viewBinding = setContentByViewBinding();
        if (layoutId <= 0 && view == null && viewBinding == null){
            throw new NullPointerException("layout can not be null.");
        }
        viewUtils = viewBinding!=null?ViewHolder.get(this, viewBinding.getRoot()):layoutId > 0 ? ViewHolder.get(this, layoutId) : ViewHolder.get(this, view);
        setContentView(viewUtils.getView());
        setContentViewAfter();
        initView();
        initData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        DiskLruCacheHelpUtils.getInstance().flush();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ProjectContext.appContext.removeReceiver(this);
        ActivityUtils.getInstanse().finishActivity(this);
    }

    @Override
    public void onDateReceiver(NetEntity netEntity) {

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


    //设置全屏
    protected void setFullScream() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    //--------------------------权限管理-需要重写onRequestPermissionsResult----------------------------
    //判断是否授权,批量处理
    public boolean isPermissionGranted(int questCode, String... permArray) {
        return SystemUtils.isPermissionGranted(this, questCode, permArray);
    }

    //------------------相机操作相关------------------------
    protected static final int SYS_INTENT_REQUEST = 0XFF01;//系统相册
    protected static final int CAMERA_INTENT_REQUEST = 0XFF02;//调用系统相机
    protected static final int IMAGE_CUT = 0XFF03;//裁剪
    protected File tempPhotoImageFile = FileUtils.createNFAtAPPRoot("TempImage", "tempPhoto.jpeg");
    protected Uri photoUri = tempPhotoImageFile == null ? null : Uri.fromFile(tempPhotoImageFile);
    protected String photoPath = tempPhotoImageFile == null ? "" : tempPhotoImageFile.getAbsolutePath();
    protected File tempCropImageFile = FileUtils.createNFAtAPPRoot("TempImage", "tempCrop.jpeg");
    private Uri cropUri = tempCropImageFile == null ? null : Uri.fromFile(tempCropImageFile);
    protected String cropPath = tempCropImageFile == null ? "" : tempCropImageFile.getAbsolutePath();

    //调用系统相册
    public void systemPhoto() {
        systemPhoto(null);
    }

    public void systemPhoto(Fragment fragment) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        if (fragment != null) {
            fragment.startActivityForResult(intent, SYS_INTENT_REQUEST);
            return;
        }
        startActivityForResult(intent, SYS_INTENT_REQUEST);
    }

    //调用系统相机
    public void cameraPhoto(Cfg.OperationResult result) {
        cameraPhoto(null, result);
    }

    //调用系统相机，fragment调用
    public void cameraPhoto(Fragment fragment, Cfg.OperationResult result) {
        try {
            if (!FileUtils.checkSDCard()) {
                result.onResult(Cfg.OperationResultType.FAIL.setMessage(ProjectContext.appContext.getString(R.string.SDError)));
                return;
            }
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            if (fragment != null) {
                fragment.startActivityForResult(intent, CAMERA_INTENT_REQUEST);
                return;
            }
            startActivityForResult(intent, CAMERA_INTENT_REQUEST);
        } catch (Exception e) {
            result.onResult(Cfg.OperationResultType.FAIL.setMessage(ProjectContext.appContext.getString(R.string.OpenCameraError)));
            LogUtils.exception(e);
        }
    }

    //图片剪切
    public void cropImageUri(Uri uri, int aspectX, int aspectY, int outputX, int outputY, int requestCode) {
        cropImageUri(null, uri, aspectX, aspectY, outputX, outputY, requestCode);
    }

    //调用系统相机，fragment调用
    public void cropImageUri(Fragment fragment, Uri uri, int aspectX, int aspectY, int outputX, int outputY, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);//黑边
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropUri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        if (fragment != null) {
            fragment.startActivityForResult(intent, requestCode);
            return;
        }
        startActivityForResult(intent, requestCode);
    }
}
