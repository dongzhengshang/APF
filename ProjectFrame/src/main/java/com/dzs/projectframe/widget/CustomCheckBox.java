package com.dzs.projectframe.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;

import com.dzs.projectframe.R;

/**
 * 自定义CheckBox
 *
 * @author DZS dzsdevelop@163.com
 * @version V1.0
 * @date 2017/3/13
 */
public class CustomCheckBox extends AppCompatCheckBox {
    /*左边宽高*/
    protected int drawableLeftWidth = 0;
    protected int drawableLeftHeight = 0;
    /*上边宽高*/
    protected int drawableTopWidth = 0;
    protected int drawableTopHeight = 0;
    /*右边宽高*/
    protected int drawableRightWidth = 0;
    protected int drawableRightHeight = 0;
    /*底部宽高*/
    protected int drawableBottomWidth = 0;
    protected int drawableBottomHeight = 0;

    public CustomCheckBox(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomCheckBox(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView);
        drawableLeftWidth = typedArray.getDimensionPixelOffset(R.styleable.CustomTextView_drawableLeftWidth, 0);
        drawableLeftHeight = typedArray.getDimensionPixelOffset(R.styleable.CustomTextView_drawableLeftHeight, 0);

        drawableTopWidth = typedArray.getDimensionPixelOffset(R.styleable.CustomTextView_drawableTopWidth, 0);
        drawableTopHeight = typedArray.getDimensionPixelOffset(R.styleable.CustomTextView_drawableTopHeight, 0);

        drawableRightWidth = typedArray.getDimensionPixelOffset(R.styleable.CustomTextView_drawableRightWidth, 0);
        drawableRightHeight = typedArray.getDimensionPixelOffset(R.styleable.CustomTextView_drawableRightHeight, 0);

        drawableBottomWidth = typedArray.getDimensionPixelOffset(R.styleable.CustomTextView_drawableBottomWidth, 0);
        drawableBottomHeight = typedArray.getDimensionPixelOffset(R.styleable.CustomTextView_drawableBottomHeight, 0);

        Drawable[] drawables = getCompoundDrawables();
        if (drawables[0] != null && drawableLeftWidth > 0 && drawableLeftHeight > 0) {
            drawables[0].setBounds(0, 0, drawableLeftWidth, drawableLeftHeight);
        }
        if (drawables[1] != null && drawableTopWidth > 0 && drawableTopHeight > 0) {
            drawables[1].setBounds(0, 0, drawableTopWidth, drawableTopHeight);
        }
        if (drawables[2] != null && drawableRightWidth > 0 && drawableRightHeight > 0) {
            drawables[2].setBounds(0, 0, drawableRightWidth, drawableRightHeight);
        }
        if (drawables[3] != null && drawableBottomWidth > 0 && drawableBottomHeight > 0) {
            drawables[3].setBounds(0, 0, drawableBottomWidth, drawableBottomHeight);
        }
        setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
        typedArray.recycle();
    }
}
