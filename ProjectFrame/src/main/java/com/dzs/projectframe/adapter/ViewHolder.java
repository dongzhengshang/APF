package com.dzs.projectframe.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dzs.projectframe.utils.SystemUtils;

import java.util.List;

/**
 * ViewHolder 帮助类
 *
 * @author DZS dzsdevelop@163.com
 * @version V1.0
 * @date 2016/6/8
 */
public class ViewHolder extends RecyclerView.ViewHolder {
    private final SparseArray<View> views;
    private final Context context;
    private int position;
    private View convertView;
    private int layoutId;
    private Object associatedObject;

    private ViewHolder(Context context, View itemView, int position) {
        super(itemView);
        this.context = context;
        this.position = position;
        this.views = new SparseArray<>();
        convertView = itemView;
        convertView.setTag(this);
    }

    public static ViewHolder get(Context context, int layout) {
        return new ViewHolder(context, View.inflate(context, layout, null), -1);
    }

    public static ViewHolder get(Context context, int layout, ViewGroup viewGroup) {
        return new ViewHolder(context, LayoutInflater.from(context).inflate(layout, viewGroup), -1);
    }

    public static ViewHolder get(Context context, View view) {
        return new ViewHolder(context, view, -1);
    }

    /**
     * This method is the only entry point to get a ViewHolder.
     *
     * @param context     The current context.
     * @param convertView The convertView arg passed to the getView() method.
     * @param parent      The parent arg passed to the getView() method.
     * @return A BaseAdapterHelper instance.
     */
    public static ViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId) {
        return get(context, convertView, parent, layoutId, -1);
    }

    /**
     * This method is package private and should only be used by QuickAdapter.
     */
    public static ViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId, int position) {
        if (convertView == null) {
            return new ViewHolder(context, LayoutInflater.from(context).inflate(layoutId, parent, false), position);
        }
        ViewHolder existingHelper = (ViewHolder) convertView.getTag();
        existingHelper.position = position;
        return existingHelper;
    }

    /**
     * This method allows you to retrieve a view and perform custom
     * operations on it, not covered by the ViewHolder.<br/>
     *
     * @param viewId The id of the view you want to retrieve.
     */
    public <T extends View> T getView(int viewId) {
        return retrieveView(viewId);
    }

    /**
     * Will set the text of a TextView.
     *
     * @param viewId The view id.
     * @param value  The text to put in the text view.
     * @return The ViewHolder for chaining.
     */
    public ViewHolder setText(int viewId, CharSequence value) {
        TextView view = retrieveView(viewId);
        view.setText(value);
        return this;
    }

    /**
     * Will get the text of a TextView.
     *
     * @param viewId The view id.
     * @return CharSequence.
     */
    public CharSequence getText(int viewId) {
        TextView view = retrieveView(viewId);
        return view.getText();
    }

    /**
     * Will set the image of an ImageView from a resource id.
     *
     * @param viewId     The view id.
     * @param imageResId The image resource id.
     * @return The ViewHolder for chaining.
     */
    public ViewHolder setImageResource(int viewId, int imageResId) {
        ImageView view = retrieveView(viewId);
        view.setImageResource(imageResId);
        return this;
    }

    /**
     * Will set background color of a view.
     *
     * @param viewId The view id.
     * @param color  A color, not a resource id.
     * @return The ViewHolder for chaining.
     */
    public ViewHolder setBackgroundColor(int viewId, int color) {
        View view = retrieveView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    /**
     * Will set background of a view.
     *
     * @param viewId        The view id.
     * @param backgroundRes A resource to use as a background.
     * @return The ViewHolder for chaining.
     */
    public ViewHolder setBackgroundRes(int viewId, int backgroundRes) {
        View view = retrieveView(viewId);
        view.setBackgroundResource(backgroundRes);
        return this;
    }

    /**
     * Will set text color of a TextView.
     *
     * @param viewId    The view id.
     * @param textColor The text color (not a resource id).
     * @return The ViewHolder for chaining.
     */
    public ViewHolder setTextColor(int viewId, int textColor) {
        TextView view = retrieveView(viewId);
        view.setTextColor(textColor);
        return this;
    }

    /**
     * Will set text color of a TextView.
     *
     * @param viewId       The view id.
     * @param textColorRes The text color resource id.
     * @return The ViewHolder for chaining.
     */
    public ViewHolder setTextColorRes(int viewId, int textColorRes) {
        TextView view = retrieveView(viewId);
        view.setTextColor(context.getResources().getColor(textColorRes));
        return this;
    }

    /**
     * Will set the image of an ImageView from a drawable.
     *
     * @param viewId   The view id.
     * @param drawable The image drawable.
     * @return The ViewHolder for chaining.
     */
    public ViewHolder setImageDrawable(int viewId, Drawable drawable) {
        ImageView view = retrieveView(viewId);
        view.setImageDrawable(drawable);
        return this;
    }

    /**
     * Will download an image from a URL and put it in an ImageView.<br/>
     * It uses Square's Glide library to download the image asynchronously and put the result into the ImageView.<br/>
     * Glide manages recycling of views in a ListView.<br/>
     *
     * @param viewId   The view id.
     * @param imageUrl The image URL.
     * @return The ViewHolder for chaining.
     */
    public ViewHolder setImageUrl(int viewId, String imageUrl) {
        ImageView view = retrieveView(viewId);
        Glide.with(context).load(imageUrl).into(view);
        return this;
    }


    /**
     * Add an action to set the image of an image view. Can be called multiple times.
     */
    public ViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView view = retrieveView(viewId);
        view.setImageBitmap(bitmap);
        return this;
    }

    /**
     * Add an action to set the alpha of a view. Can be called multiple times.
     * Alpha between 0-1.
     */
    public ViewHolder setAlpha(int viewId, float value) {
        retrieveView(viewId).setAlpha(value);
        return this;
    }

    /**
     * Set a view visibility to VISIBLE (true) or GONE (false).
     *
     * @param viewId  The view id.
     * @param visible True for VISIBLE, false for GONE.
     * @return The ViewHolder for chaining.
     */
    public ViewHolder setVisible(int viewId, boolean visible) {
        View view = retrieveView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    /**
     * Add links into a TextView.
     *
     * @param viewId The id of the TextView to linkify.
     * @return The ViewHolder for chaining.
     */
    public ViewHolder linkify(int viewId) {
        TextView view = retrieveView(viewId);
        Linkify.addLinks(view, Linkify.ALL);
        return this;
    }

    /**
     * Apply the typeface to the given viewId, and enable subpixel rendering.
     */
    public ViewHolder setTypeface(int viewId, Typeface typeface) {
        TextView view = retrieveView(viewId);
        view.setTypeface(typeface);
        view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        return this;
    }

    /**
     * Apply the typeface to all the given viewIds, and enable subpixel rendering.
     */
    public ViewHolder setTypeface(Typeface typeface, int... viewIds) {
        for (int viewId : viewIds) {
            TextView view = retrieveView(viewId);
            view.setTypeface(typeface);
            view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
        return this;
    }

    /**
     * Sets the progress of a ProgressBar.
     *
     * @param viewId   The view id.
     * @param progress The progress.
     * @return The ViewHolder for chaining.
     */
    public ViewHolder setProgress(int viewId, int progress) {
        ProgressBar view = retrieveView(viewId);
        view.setProgress(progress);
        return this;
    }

    /**
     * Sets the progress and max of a ProgressBar.
     *
     * @param viewId   The view id.
     * @param progress The progress.
     * @param max      The max value of a ProgressBar.
     * @return The ViewHolder for chaining.
     */
    public ViewHolder setProgress(int viewId, int progress, int max) {
        ProgressBar view = retrieveView(viewId);
        view.setMax(max);
        view.setProgress(progress);
        return this;
    }

    /**
     * Sets the range of a ProgressBar to 0...max.
     *
     * @param viewId The view id.
     * @param max    The max value of a ProgressBar.
     * @return The ViewHolder for chaining.
     */
    public ViewHolder setMax(int viewId, int max) {
        ProgressBar view = retrieveView(viewId);
        view.setMax(max);
        return this;
    }

    /**
     * Sets the rating (the number of stars filled) of a RatingBar.
     *
     * @param viewId The view id.
     * @param rating The rating.
     * @return The ViewHolder for chaining.
     */
    public ViewHolder setRating(int viewId, float rating) {
        RatingBar view = retrieveView(viewId);
        view.setRating(rating);
        return this;
    }

    /**
     * Sets the rating (the number of stars filled) and max of a RatingBar.
     *
     * @param viewId The view id.
     * @param rating The rating.
     * @param max    The range of the RatingBar to 0...max.
     * @return The ViewHolder for chaining.
     */
    public ViewHolder setRating(int viewId, float rating, int max) {
        RatingBar view = retrieveView(viewId);
        view.setMax(max);
        view.setRating(rating);
        return this;
    }

    /**
     * Sets the on click listener of the view.
     *
     * @param viewId   The view id.
     * @param listener The on click listener;
     * @return The ViewHolder for chaining.
     */
    public ViewHolder setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = retrieveView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    /**
     * Sets the on touch listener of the view.
     *
     * @param viewId   The view id.
     * @param listener The on touch listener;
     * @return The ViewHolder for chaining.
     */
    public ViewHolder setOnTouchListener(int viewId, View.OnTouchListener listener) {
        View view = retrieveView(viewId);
        view.setOnTouchListener(listener);
        return this;
    }

    /**
     * Sets the on long click listener of the view.
     *
     * @param viewId   The view id.
     * @param listener The on long click listener;
     * @return The ViewHolder for chaining.
     */
    public ViewHolder setOnLongClickListener(int viewId, View.OnLongClickListener listener) {
        View view = retrieveView(viewId);
        view.setOnLongClickListener(listener);
        return this;
    }

    /**
     * Sets the listview or gridview's item click listener of the view
     *
     * @param viewId   The view id.
     * @param listener The item on click listener;
     * @return The ViewHolder for chaining.
     */
    public ViewHolder setOnItemClickListener(int viewId, AdapterView.OnItemClickListener listener) {
        AdapterView view = retrieveView(viewId);
        view.setOnItemClickListener(listener);
        return this;
    }

    /**
     * Sets the listview or gridview's item long click listener of the view
     *
     * @param viewId   The view id.
     * @param listener The item long click listener;
     * @return The ViewHolder for chaining.
     */
    public ViewHolder setOnItemLongClickListener(int viewId, AdapterView.OnItemLongClickListener listener) {
        AdapterView view = retrieveView(viewId);
        view.setOnItemLongClickListener(listener);
        return this;
    }

    /**
     * Sets the listview or gridview's item selected click listener of the view
     *
     * @param viewId   The view id.
     * @param listener The item selected click listener;
     * @return The ViewHolder for chaining.
     */
    public ViewHolder setOnItemSelectedClickListener(int viewId, AdapterView.OnItemSelectedListener listener) {
        AdapterView view = retrieveView(viewId);
        view.setOnItemSelectedListener(listener);
        return this;
    }

    /**
     * Sets the on checked change listener of the view.
     *
     * @param viewId   The view id.
     * @param listener The checked change listener of compound button.
     * @return The ViewHolder for chaining.
     */
    public ViewHolder setOnCheckedChangeListener(int viewId, CompoundButton.OnCheckedChangeListener listener) {
        CompoundButton view = retrieveView(viewId);
        view.setOnCheckedChangeListener(listener);
        return this;
    }

    /**
     * Sets the tag of the view.
     *
     * @param viewId The view id.
     * @param tag    The tag;
     * @return The ViewHolder for chaining.
     */
    public ViewHolder setTag(int viewId, Object tag) {
        View view = retrieveView(viewId);
        view.setTag(tag);
        return this;
    }

    /**
     * Sets the tag of the view.
     *
     * @param viewId The view id.
     * @param key    The key of tag;
     * @param tag    The tag;
     * @return The ViewHolder for chaining.
     */
    public ViewHolder setTag(int viewId, int key, Object tag) {
        View view = retrieveView(viewId);
        view.setTag(key, tag);
        return this;
    }

    /**
     * Sets the checked status of a checkable.
     *
     * @param viewId  The view id.
     * @param checked The checked status;
     * @return The ViewHolder for chaining.
     */
    public ViewHolder setChecked(int viewId, boolean checked) {
        View view = retrieveView(viewId);
        // View unable cast to Checkable
        if (view instanceof CompoundButton) {
            ((CompoundButton) view).setChecked(checked);
        } else if (view instanceof CheckedTextView) {
            ((CheckedTextView) view).setChecked(checked);
        }
        return this;
    }

    /**
     * Sets the adapter of a adapter view.
     *
     * @param viewId  The view id.
     * @param adapter The adapter;
     * @return The ViewHolder for chaining.
     */
    public ViewHolder setAdapter(int viewId, Adapter adapter) {
        AdapterView view = retrieveView(viewId);
        view.setAdapter(adapter);
        return this;
    }

    /**
     * Sets the TextView drawable
     *
     * @param viewId      The view id.
     * @param leftResId   left image
     * @param topResId    top image
     * @param rightResId  right image
     * @param bottomResId bottom image
     */
    public void setTextDraw(int viewId, int leftResId, int topResId, int rightResId, int bottomResId) {
        TextView view = getView(viewId);
        Drawable left = null, top = null, right = null, botttom = null;
        if (leftResId > 0) {
            left = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? context.getDrawable(leftResId) : context.getResources().getDrawable(leftResId);
            if (left != null) left.setBounds(0, 0, left.getMinimumWidth(), left.getMinimumHeight());
        }
        if (topResId > 0) {
            top = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? context.getDrawable(topResId) : context.getResources().getDrawable(topResId);
            if (top != null) top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());
        }
        if (rightResId > 0) {
            right = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? context.getDrawable(leftResId) : context.getResources().getDrawable(leftResId);
            if (right != null) right.setBounds(0, 0, right.getMinimumWidth(), right.getMinimumHeight());
        }
        if (bottomResId > 0) {
            botttom = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? context.getDrawable(leftResId) : context.getResources().getDrawable(leftResId);
            if (botttom != null) botttom.setBounds(0, 0, botttom.getMinimumWidth(), botttom.getMinimumHeight());
        }

        view.setCompoundDrawables(left, top, right, botttom);
    }

    /**
     * Retrieve the convertView
     */
    public View getView() {
        return convertView;
    }

    /**
     * Retrieve the overall position of the data in the list.
     *
     * @throws IllegalArgumentException If the position hasn't been set at the construction of the this helper.
     */
    public int getCurrentPosition() {
        if (position == -1)
            throw new IllegalStateException("Use ViewHolder constructor " + "with position if you need to retrieve the position.");
        return position;
    }

    @SuppressWarnings("unchecked")
    protected <T extends View> T retrieveView(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = convertView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * Retrieves the last converted object on this view.
     */
    public Object getAssociatedObject() {
        return associatedObject;
    }

    /**
     * Should be called during convert
     */
    public void setAssociatedObject(Object associatedObject) {
        this.associatedObject = associatedObject;
    }

    /**
     * 更新position
     *
     * @param position position
     */
    public void updatePosition(int position) {
        this.position = position;
    }

    //====================以下是Activity中常用方法=================================
    private static long lastClickTime;

    /**
     * 判断按钮是不是连续点击
     *
     * @return boolean
     */
    public synchronized boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 强制隐藏软键盘
     *
     * @param view 当前页面任意一view
     */
    public void hideInput(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 跳转到某一Activity
     *
     * @param from        当前activity
     * @param to          需要跳转到的界面
     * @param data        数据Bean,需要实现Serializable接口
     * @param requestCode 请求值,小于0为 startActivity
     * @param isFinish    是否销毁当前界面
     */
    public void intent(Activity from, Class to, Class data, int requestCode, boolean isFinish) {
        Intent intent = new Intent(from, to);
        if (data != null) intent.putExtra(to.getName(), data);
        if (requestCode < 0) {
            from.startActivity(intent);
        } else {
            from.startActivityForResult(intent, requestCode);
        }
        if (isFinish) from.finish();
    }

    public void intent(Activity from, Class to, Class data, boolean isFinish) {
        intent(from, to, data, -1, isFinish);
    }

    public void intent(Activity from, Class to, boolean isFinish) {
        intent(from, to, null, -1, isFinish);
    }

    public void intent(Activity from, Class to) {
        intent(from, to, null, -1, false);
    }

    /**
     * 跳转到某一Activity
     *
     * @param fragment    当前Fragment
     * @param activity    需要跳转到的界面
     * @param data        数据Bean,需要实现Serializable接口
     * @param requestCode 请求值,小于0为startActivity
     * @param isFinish    是否销毁Activity
     */
    public void intent(Fragment fragment, Class activity, Class data, int requestCode, boolean isFinish) {
        Intent intent = new Intent(fragment.getActivity(), activity);
        if (data != null) intent.putExtra(activity.getName(), data);
        if (requestCode < 0) {
            fragment.startActivity(intent);
        } else {
            fragment.startActivityForResult(intent, requestCode);
        }
        if (isFinish) fragment.getActivity().finish();
    }

    public void intent(Fragment fragment, Class activity, Class data, boolean isFinish) {
        intent(fragment, activity, data, -1, isFinish);
    }

    public void intent(Fragment fragment, Class activity) {
        intent(fragment, activity, null, -1, false);
    }

    /**
     * 判断一个Scheme是否有效
     *
     * @param context 当前上下文
     * @param scheme  Scheme
     * @return boolean
     */
    public boolean schemeIsValid(@NonNull Context context, @NonNull String scheme) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(scheme));
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
        return !activities.isEmpty();
    }

    /**
     * 界面跳转使用 URL Scheme
     * <p>
     * 使用方式
     * <intent-filter>
     * <!--协议部分，随便设置-->
     * <data android:scheme="xl" android:host="goods" android:path="/goodsDetail" android:port="8888"/>
     * <!--下面这几行也必须得设置-->
     * <category android:name="android.intent.category.DEFAULT"/>
     * <action android:name="android.intent.action.VIEW"/>
     * <category android:name="android.intent.category.BROWSABLE"/>
     * </intent-filter>
     * <p>
     * 参数获取
     * Uri uri = getIntent().getData();
     * if (uri != null) {
     * // 完整的url信息
     * String url = uri.toString();
     * Log.e(TAG, "url: " + uri);
     * // scheme部分
     * String scheme = uri.getScheme();
     * Log.e(TAG, "scheme: " + scheme);
     * // host部分
     * String host = uri.getHost();
     * Log.e(TAG, "host: " + host);
     * //port部分
     * int port = uri.getPort();
     * Log.e(TAG, "host: " + port);
     * // 访问路劲
     * String path = uri.getPath();
     * Log.e(TAG, "path: " + path);
     * List<String> pathSegments = uri.getPathSegments();
     * // Query部分
     * String query = uri.getQuery();
     * Log.e(TAG, "query: " + query);
     * //获取指定参数值
     * String goodsId = uri.getQueryParameter("goodsId");
     * Log.e(TAG, "goodsId: " + goodsId);
     * }
     * <p>
     * H5调用
     * <a href="xl://goods:8888/goodsDetail?goodsId=10011002">打开商品详情</a>
     * </P>
     *
     * @param activity 当前Activity
     * @param scheme   Scheme
     */
    public void intent(Activity activity, String scheme) {
        activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(scheme)));
    }
}
