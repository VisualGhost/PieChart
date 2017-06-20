package com.piechart.piechart;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.piechart.R;

public class ViewControllerImpl implements ViewController {

    private static final float START_ANGLE = 135;
    private static final float MIN_CIRCLE_FACTOR = 0.1f;
    private static final float MAX_CIRCLE_FACTOR = 0.3f;

    private float mRadius;
    private Bitmap mBitmap;
    private int height;
    private int width;
    private float marginBottom;

    private Paint mArchPaintSegment1;
    private Paint mArchPaintSegment2;
    private Paint mArchPaintSegment3;
    private Paint mArchPaintSegment4;
    private Paint mCenterCirclePaint;

    private RectF rectF;
    private RectF mBitmapRectF;

    private float mMaxSegmentAngle;
    private float mMaxSegment1Angle;
    private float mMaxSegment2Angle;
    private float mSweepAngle;
    private float mCurrentSweepAngle;
    private float radius;

    private boolean isMaxReached;

    public ViewControllerImpl(
            Context context,
            AttributeSet attrs) {
        initArchPaint1();
        initArchPaint2();
        initArchPaint3();
        initArchPaint4();
        initCenterCirclePaint();
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        if (attrs != null) {
            TypedArray array = null;
            try {
                array = context.obtainStyledAttributes(attrs, R.styleable.PieChart);
                mRadius = array.getDimension(R.styleable.PieChart_radius, 0f);
                radius = MIN_CIRCLE_FACTOR * mRadius;
                marginBottom = array.getDimension(R.styleable.PieChart_marginBottom, 0);
            } finally {
                if (array != null) {
                    array.recycle();
                }
            }
        }

    }

    private void initArchPaint1() {
        mArchPaintSegment1 = new Paint();
        mArchPaintSegment1.setStyle(Paint.Style.FILL);
        mArchPaintSegment1.setAntiAlias(true);
        //TODO use attr
        mArchPaintSegment1.setColor(Color.argb(100, 158, 180, 216));
    }

    private void initArchPaint2() {
        mArchPaintSegment2 = new Paint();
        mArchPaintSegment2.setStyle(Paint.Style.FILL);
        mArchPaintSegment2.setAntiAlias(true);
        //TODO use attr
        mArchPaintSegment2.setColor(Color.argb(100, 158, 198, 216));
    }

    private void initArchPaint3() {
        mArchPaintSegment3 = new Paint();
        mArchPaintSegment3.setStyle(Paint.Style.FILL);
        mArchPaintSegment3.setAntiAlias(true);
        //TODO use attr
        mArchPaintSegment3.setColor(Color.argb(100, 182, 216, 158));
    }

    private void initArchPaint4() {
        mArchPaintSegment4 = new Paint();
        mArchPaintSegment4.setStyle(Paint.Style.FILL);
        mArchPaintSegment4.setAntiAlias(true);
        //TODO use attr
        mArchPaintSegment4.setColor(Color.WHITE);
        mArchPaintSegment4.setAlpha(100);
    }

    private void initCenterCirclePaint() {
        mCenterCirclePaint = new Paint();
        mCenterCirclePaint.setStyle(Paint.Style.FILL);
        mCenterCirclePaint.setAntiAlias(true);
        mCenterCirclePaint.setColor(Color.WHITE);
    }

    @Override
    public void onSize(int w, int h) {
        mBitmapRectF = new RectF(0, 0, w, h);
        width = w;
        height = h;
        float left = w / 2 - mRadius / 2;
        float top = h / 2 - mRadius / 2;
        float right = mRadius + w / 2 - mRadius / 2;
        float bottom = mRadius + h / 2 - mRadius / 2;

        rectF = new RectF(left, top - marginBottom, right, bottom - marginBottom);
    }

    @Override
    public void draw(Canvas canvas, float sweepAngle) {
        mCurrentSweepAngle = sweepAngle;
        drawSegments(canvas, sweepAngle);
        float progress = getProgress(sweepAngle);
        drawCenterCircle(canvas, sweepAngle, progress);
    }

    private void drawSegments(Canvas canvas, float sweepAngle) {
        canvas.drawBitmap(mBitmap, null, mBitmapRectF, null);
        drawSegment4(canvas);
        drawSegment1(canvas, sweepAngle);
        if (mSweepAngle > 0) {
            drawSegment3(canvas);
        }
        if (Float.compare(sweepAngle, mMaxSegment1Angle) > 0) {
            drawSegment2(canvas, sweepAngle);
        }
    }

    private float getProgress(float sweepAngle) {
        return 100 / (mMaxSegmentAngle - mMaxSegment2Angle) * (sweepAngle - mMaxSegment2Angle);
    }

    @Override
    public void slide(Canvas canvas, float sweepAngle, int progress) {
        mSweepAngle = sweepAngle;
        drawSegments(canvas, mCurrentSweepAngle);
        drawCenterCircle(canvas, sweepAngle, progress);
    }

    private void drawSegment1(Canvas canvas, float sweepAngle) {
        canvas.drawArc(rectF, START_ANGLE, Math.min(sweepAngle, mMaxSegment1Angle), true, mArchPaintSegment1);
    }

    private void drawSegment2(Canvas canvas, float sweepAngle) {
        canvas.drawArc(rectF, START_ANGLE + Math.min(sweepAngle, mMaxSegment1Angle), sweepAngle - mMaxSegment1Angle, true, mArchPaintSegment2);
    }

    private void drawSegment3(Canvas canvas) {
        canvas.drawArc(rectF, START_ANGLE + mMaxSegment2Angle, mSweepAngle, true, mArchPaintSegment3);
    }

    private void drawSegment4(Canvas canvas) {
        canvas.drawArc(rectF, START_ANGLE, 2 * START_ANGLE, true, mArchPaintSegment4);
    }

    private void modifyCircleRadius(float progress) {
        radius = mRadius * (MIN_CIRCLE_FACTOR + MAX_CIRCLE_FACTOR * (1 - (progress / 100f)));
    }

    private void modifyCircleColor(float progress) {
        if (progress == 100) {
            mCenterCirclePaint.setColor(Color.WHITE);// TODO use attr
        } else {
            mCenterCirclePaint.setColor(Color.argb(255, 244, 140, 66));// TODO use attr
        }
    }

    private void drawCenterCircle(Canvas canvas, float sweepAngle, float progress) {
        if (!isMaxReached && Float.compare(sweepAngle, mMaxSegmentAngle) == 0) {
            isMaxReached = true;
        }
        if (isMaxReached) {
            modifyCircleColor(progress);
            modifyCircleRadius(progress);
        }
        canvas.drawCircle(width / 2, height / 2 - marginBottom, radius, mCenterCirclePaint);
    }

    @Override
    public void reset() {
        isMaxReached = false;
        radius = MIN_CIRCLE_FACTOR * mRadius;
        mCenterCirclePaint.setColor(Color.WHITE);// TODO use attr
    }

    @Override
    public void setBackground(Bitmap background) {
        mBitmap = background;
    }

    @Override
    public void setMaxSegment1Angle(float angle) {
        mMaxSegment1Angle = angle;
    }

    @Override
    public void setMaxSegment2Angle(float angle) {
        mMaxSegment2Angle = angle;
    }

    @Override
    public void setMaxSegment3Angle(float angle) {
        mMaxSegmentAngle = angle;
    }
}
