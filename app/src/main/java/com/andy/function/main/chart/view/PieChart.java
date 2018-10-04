package com.andy.function.main.chart.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.andy.R;
import com.andy.function.main.chart.entity.PieContent;
import com.andy.utils.DPValueUtil;

/**
 * Created by Andy on 2018/9/10.
 * Modify time 2018/9/10
 */
public class PieChart extends View {

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

    private Paint mPaint;
    private Paint mTextPaint;
    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStrokeWidth(DPValueUtil.getInstance().dp2px(1));
        mTextPaint.setTextSize(DPValueUtil.getInstance().dp2px(16));
    }

    private PieContent[] mValues;
    private double mTotal = 0d;

    public void setData(PieContent... values) {
        mTotal = 0d;
        mValues = values;
        for (int i = 0; i < values.length; i++) {
            mTotal += values[i].getNum();
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
                height - getPaddingTop() - getPaddingBottom()) * 1.0f / 3);

        centerX = (getWidth() + getPaddingLeft() - getPaddingRight()) / 2;
        centerY = (getHeight() + getPaddingTop() - getPaddingBottom()) / 2;
    }

    RectF oval = null;

    private float centerX, centerY;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(centerX, centerY);
        if (oval == null) {
            oval = new RectF(-mRadius, -mRadius, mRadius, mRadius);
        }

        if (mValues != null && mValues.length != 0) {
            float currentAngle = 0.0f;

            for (int i = 0; i < mValues.length; i++) {
                PieContent content = mValues[i];
                float needDrawAngle = (float) (content.getNum() * 1f / mTotal) * 360;

                mPaint.setColor(getResources().getColor(mColor[i % 8]));
                canvas.drawArc(oval, currentAngle, needDrawAngle, true, mPaint);

                mTextPaint.setColor(getResources().getColor(mColor[i % 8]));
                drawLine(canvas, currentAngle, needDrawAngle, content.getContent());
                currentAngle = currentAngle + needDrawAngle;
            }

        }
    }

    private void drawLine(Canvas canvas, float start, float angles, String text) {
        float stopX, stopY;
        stopX = (float) ((mRadius + 40) * Math.cos((2 * start + angles) / 2 * Math.PI / 180));
        stopY = (float) ((mRadius + 40) * Math.sin((2 * start + angles) / 2 * Math.PI / 180));

        canvas.drawLine((float) ((mRadius - 20) * Math.cos((2 * start + angles) / 2 * Math.PI / 180)),
                (float) ((mRadius - 20) * Math.sin((2 * start + angles) / 2 * Math.PI / 180)),
                stopX, stopY, mTextPaint
        );


        //测量文字大小
        Rect rect = new Rect();
        mTextPaint.getTextBounds(text, 0, text.length(), rect);
        int w = rect.width();
        int h = rect.height();
        int offset = 20;//文字在横线的偏移量

        //画横线
        int dx;//判断横线是画在左边还是右边
        int endX;
        if (stopX > 0) {
            endX = (int) (stopX + w + (offset * 2));
        } else {
            endX = (int) (stopX - w - (offset * 2));
        }
        canvas.drawLine(stopX, stopY, endX, stopY, mTextPaint);
        dx = (int) (endX - stopX);

        //画文字 文字的Y坐标值的是文字底部的Y坐标
        canvas.drawText(text, 0, text.length(), dx > 0 ? stopX + offset : stopX - w - offset, stopY + h, mTextPaint);

        //测量百分比大小
        String percentage = angles / 3.60 + "";
        percentage = percentage.substring(0, percentage.length() > 4 ? 4 : percentage.length()) + "%";
        mTextPaint.getTextBounds(percentage, 0, percentage.length(), rect);
        w = rect.width();
        //画百分比
        canvas.drawText(percentage, 0, percentage.length(), dx > 0 ? stopX + offset : stopX - w - offset, stopY - 5, mTextPaint);

    }
}
