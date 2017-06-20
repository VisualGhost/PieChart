package com.piechart.piechart;


import android.graphics.Bitmap;
import android.graphics.Canvas;

public interface ViewController {

    void onSize(int w, int h);

    void draw(Canvas canvas, float sweepAngle);

    void slide(Canvas canvas, float sweepAngle, int progress);

    void reset();

    void setBackground(Bitmap background);

    void setMaxSegment1Angle(float angle);

    void setMaxSegment2Angle(float angle);

    void setMaxSegment3Angle(float angle);
}
