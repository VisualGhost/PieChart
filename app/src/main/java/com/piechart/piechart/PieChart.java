package com.piechart.piechart;

import android.graphics.Bitmap;

public interface PieChart {

    void setViewController(ViewController viewController);

    void setObservableHolder(ObservableHolder observableHolder);

    void startAnimation();

    void slide(float sweepAngle, int progress);

    void setBackground(Bitmap bitmap);

}
