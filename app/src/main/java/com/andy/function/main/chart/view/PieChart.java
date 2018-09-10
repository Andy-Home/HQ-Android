package com.andy.function.main.chart.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.andy.R;

/**
 * Created by Andy on 2018/9/10.
 * Modify time 2018/9/10
 */
public class PieChart extends View {


    private Paint mPaint;

    public PieChart(Context context) {
        super(context);
        init();
    }

    public PieChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PieChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
    }

    private double[] mValues;
    private double mTotal = 0d;

    public void setData(double... values) {
        mValues = values;
        for (double value : mValues) {
            mTotal += value;
        }
        invalidate();
    }

    private static final int DEFAULT_RADIUS = 200;
    private int mRadius = DEFAULT_RADIUS; //外圆的半径

    private int[] mColor = {R.color.color11, R.color.color12, R.color.color13, R.color.color14,
            R.color.color15, R.color.color16, R.color.color17, R.color.color18};

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int wideSize = MeasureSpec.getSize(widthMeasureSpec);
        int wideMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width, height;
        if (wideMode == MeasureSpec.EXACTLY) { //精确值 或matchParent
            width = wideSize;
        } else {
            width = mRadius * 2 + getPaddingLeft() + getPaddingRight();
            if (wideMode == MeasureSpec.AT_MOST) {
                width = Math.min(width, wideSize);
            }

        }

        if (heightMode == MeasureSpec.EXACTLY) { //精确值 或matchParent
            height = heightSize;
        } else {
            height = mRadius * 2 + getPaddingTop() + getPaddingBottom();
            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(height, heightSize);
            }

        }
        setMeasuredDimension(width, height);
        mRadius = (int) (Math.min(width - getPaddingLeft() - getPaddingRight(),
                height - getPaddingTop() - getPaddingBottom()) * 1.0f / 2);
    }

    RectF oval = null;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate((getWidth() + getPaddingLeft() - getPaddingRight()) / 2, (getHeight() + getPaddingTop() - getPaddingBottom()) / 2);
        if (oval == null) {
            oval = new RectF(-mRadius, -mRadius, mRadius, mRadius);
        }

        if (mValues != null && mValues.length != 0) {
            int i = 0;
            float currentAngle = 0.0f;
            for (double value : mValues) {
                float needDrawAngle = (float) (value * 1f / mTotal);
                mPaint.setColor(mColor[i]);
                canvas.drawArc(oval, currentAngle, needDrawAngle - 1, true, mPaint);
                currentAngle = currentAngle + needDrawAngle;
                i++;
            }

        }
    }
}
