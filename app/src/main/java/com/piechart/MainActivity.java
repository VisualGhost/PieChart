package com.piechart;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SeekBar;

import com.piechart.piechart.ObservableHolder;
import com.piechart.piechart.PieChart;
import com.piechart.piechart.ViewControllerImpl;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private static final float SEGMENT_MAX = 270;
    private static final float SEGMENT_1 = 110;
    private static final float SEGMENT_2 = 190;

    private PieChart mPieChart;
    private SeekBar mSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPieChart = (PieChart) findViewById(R.id.pie_chart);
        Bitmap background = BitmapFactory.decodeResource(getResources(),
                R.drawable.piechart_background);
        mPieChart.setBackground(background);
        ObservableHolder.Range clockWiseRange = new ObservableHolder.Range(0, SEGMENT_MAX, 2000);
        ObservableHolder.Range counterClockWiseRange = new ObservableHolder.Range(SEGMENT_MAX, SEGMENT_2, 1500);
        mPieChart.setViewController(new ViewControllerImpl(background, SEGMENT_1, SEGMENT_2, SEGMENT_MAX));
        mPieChart.setObservableHolder(new ObservableHolder(clockWiseRange, counterClockWiseRange));
        mSeekBar = (SeekBar) findViewById(R.id.seek_bar);
        mSeekBar.setOnSeekBarChangeListener(this);
        animate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.animate) {
            animate();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void animate() {
        mPieChart.startAnimation();
        mSeekBar.setProgress(0);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        float sweepAngle = (progress / 100f) * (SEGMENT_MAX - SEGMENT_2);
        mPieChart.slide(sweepAngle, progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // empty
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // empty
    }
}
