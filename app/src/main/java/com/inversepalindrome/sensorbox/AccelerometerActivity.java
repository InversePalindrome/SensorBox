/*
Copyright (c) 2019 Inverse Palindrome
SensorBox - AccelerometerActivity.java
https://inversepalindrome.com/
*/


package com.inversepalindrome.sensorbox;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import android.graphics.Color;
import android.widget.TextView;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.Locale;


public class AccelerometerActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;

    private Thread thread;

    private LineChart accelerometerChart;

    private TextView xAccelerationText;
    private TextView yAccelerationText;
    private TextView zAccelerationText;

    private final static float MAX_POINTS_DISPLAYED = 150;

    private boolean plotData = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);

        accelerometerChart = findViewById(R.id.accelerometer_chart);
        accelerometerChart.getDescription().setEnabled(true);
        accelerometerChart.setTouchEnabled(false);
        accelerometerChart.setDragEnabled(false);
        accelerometerChart.setScaleEnabled(true);
        accelerometerChart.setDrawGridBackground(false);
        accelerometerChart.setPinchZoom(false);
        accelerometerChart.setBackgroundColor(Color.WHITE);
        accelerometerChart.getDescription().setText("Acceleration Data");

        LineData emptyData = new LineData();
        emptyData.setValueTextColor(Color.BLACK);

        accelerometerChart.setData(emptyData);

        Legend legend = accelerometerChart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextColor(Color.BLACK);
        legend.setEnabled(true);

        XAxis xAxis = accelerometerChart.getXAxis();
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawGridLines(true);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setEnabled(true);

        YAxis leftAxis = accelerometerChart.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMaximum(10.f);
        leftAxis.setAxisMinimum(0.f);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = accelerometerChart.getAxisRight();
        rightAxis.setEnabled(false);

        accelerometerChart.getXAxis().setDrawGridLines(false);
        accelerometerChart.setDrawBorders(false);

        xAccelerationText = findViewById(R.id.xAccelerationValueText);
        yAccelerationText = findViewById(R.id.yAccelerationValueText);
        zAccelerationText = findViewById(R.id.zAccelerationValueText);

        startPlot();
    }

    @Override
    protected void onResume(){
        super.onResume();

        final Sensor accelerationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if(accelerationSensor != null) {
            sensorManager.registerListener(this, accelerationSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        else {
            Toast.makeText(this, "No acceleration Sensor found!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause(){
        super.onPause();

        if (thread != null){
            thread.interrupt();
        }

        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event){
        final float xAcceleration = event.values[0];
        final float yAcceleration = event.values[1];
        final float zAcceleration = event.values[2];

        updateTextData(xAcceleration, yAcceleration, zAcceleration);

        if(plotData){
            addDataEntry(xAcceleration, yAcceleration, zAcceleration);

            plotData = false;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }

    private void updateTextData(final float xAcceleration, final float yAcceleration, final float zAcceleration){
        xAccelerationText.setText(String.format(Locale.US, "%.1f", xAcceleration));
        yAccelerationText.setText(String.format(Locale.US, "%.1f", yAcceleration));
        zAccelerationText.setText(String.format(Locale.US, "%.1f", zAcceleration));
    }

    private void startPlot(){
        if(thread != null){
           thread.interrupt();
        }

        thread = new Thread(new Runnable(){
            @Override
            public void run(){
                while(true) {
                    plotData = true;
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        thread.start();
    }

    private void addDataEntry(final float xAcceleration, final float yAcceleration, final float zAcceleration){
        LineData data = accelerometerChart.getData();

        if(data != null){
            ILineDataSet xData = data.getDataSetByIndex(0);
            ILineDataSet yData = data.getDataSetByIndex(1);
            ILineDataSet zData = data.getDataSetByIndex(2);

            if(xData == null){
                xData = createXDataSet();

                data.addDataSet(xData);
            }
            if(yData == null){
                yData = createYDataSet();

                data.addDataSet(yData);
            }
            if(zData == null){
                zData = createZDataSet();

                data.addDataSet(zData);
            }

            data.addEntry(new Entry(xData.getEntryCount(), xAcceleration + 5), 0);
            data.addEntry(new Entry(yData.getEntryCount(), yAcceleration + 5), 1);
            data.addEntry(new Entry(zData.getEntryCount(), zAcceleration + 5), 2);

            data.notifyDataChanged();

            accelerometerChart.notifyDataSetChanged();
            accelerometerChart.setVisibleXRangeMaximum(MAX_POINTS_DISPLAYED);
            accelerometerChart.moveViewToX(data.getEntryCount());
        }
    }

    private ILineDataSet createXDataSet(){
        LineDataSet set = new LineDataSet(null, "X Acceleration");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setLineWidth(3f);
        set.setColor(Color.BLUE);
        set.setHighlightEnabled(false);
        set.setDrawValues(false);
        set.setDrawCircles(false);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setCubicIntensity(0.2f);

        return set;
    }

    private ILineDataSet createYDataSet(){
        LineDataSet set = new LineDataSet(null, "Y Acceleration");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setLineWidth(3f);
        set.setColor(Color.RED);
        set.setHighlightEnabled(false);
        set.setDrawValues(false);
        set.setDrawCircles(false);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setCubicIntensity(0.2f);

        return set;
    }

    private ILineDataSet createZDataSet(){
        LineDataSet set = new LineDataSet(null, "Z Acceleration");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setLineWidth(3f);
        set.setColor(Color.GREEN);
        set.setHighlightEnabled(false);
        set.setDrawValues(false);
        set.setDrawCircles(false);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setCubicIntensity(0.2f);

        return set;
    }
}
