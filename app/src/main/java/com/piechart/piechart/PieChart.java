package com.piechart.piechart;

public interface PieChart {

    void setViewController(ViewController viewController);

    void setObservableHolder(ObservableHolder observableHolder);

    void startAnimation();

    void slide(float sweepAngle, int progress);

}
