package com.piechart.piechart;

import android.graphics.Bitmap;

public interface PieChart {

    void setObservableHolder(ObservableHolder observableHolder);

    void startAnimation();

    void slide(float sweepAngle, int progress);

    void setBackground(Bitmap bitmap);

    void setMaxSegment1Angle(float angle);

    void setMaxSegment2Angle(float angle);

    void setMaxSegment3Angle(float angle);

}
