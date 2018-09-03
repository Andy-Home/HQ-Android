package com.andy.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.andy.R;

/**
 * Created by Andy on 2018/9/3.
 * Modify time 2018/9/3
 */
public class TextWithDropView extends ConstraintLayout {

    public TextWithDropView(Context context) {
        super(context);
        initView(context);
    }

    public TextWithDropView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private TextView mText;
    private ImageView mImg;
    private ConstraintLayout mLayout;
    private void initView(Context context){
        LayoutInflater.from(context).inflate(R.layout.view_text_with_dropview, this, true);

        mLayout = findViewById(R.id.layout);
        mText = findViewById(R.id.text);
        mImg = findViewById(R.id.dropdown);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    public void setContentText(String str){
        mText.setText(str);
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, @Nullable Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        if(gainFocus){
            mLayout.setBackgroundResource(R.drawable.bg_rectangle_white_main_color_stroke);
        }else {
            mLayout.setBackgroundResource(R.drawable.bg_rectangle_white_black_stroke);
        }
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
    }
}
