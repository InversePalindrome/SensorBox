/*
Copyright (c) 2019 Inverse Palindrome
SensorBox - GyroscopeActivity.java
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
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;


public class GyroscopeActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;

    private Thread thread;

    private LineChart gyroscopeChart;

    private TextView xAngularSpeedText;
    private TextView yAngularSpeedText;
    private TextView zAngularSpeedText;

    private final static float MAX_POINTS_DISPLAYED = 150;
    private final static float CHART_RANGE = 15;

    private boolean plotData = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gyroscope);

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);

        gyroscopeChart = findViewById(R.id.gyroscope_chart);
        gyroscopeChart.getDescription().setEnabled(true);
        gyroscopeChart.setTouchEnabled(false);
        gyroscopeChart.setDragEnabled(false);
        gyroscopeChart.setScaleEnabled(true);
        gyroscopeChart.setDrawGridBackground(false);
        gyroscopeChart.setPinchZoom(false);
        gyroscopeChart.setBackgroundColor(Color.WHITE);
        gyroscopeChart.getDescription().setText("Gyroscope Data");

        LineData emptyData = new LineData();
        emptyData.setValueTextColor(Color.BLACK);

        gyroscopeChart.setData(emptyData);

        Legend legend = gyroscopeChart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextColor(Color.BLACK);
        legend.setEnabled(true);

        XAxis xAxis = gyroscopeChart.getXAxis();
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawGridLines(true);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setEnabled(true);

        YAxis leftAxis = gyroscopeChart.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMaximum(CHART_RANGE);
        leftAxis.setAxisMinimum(0.f);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = gyroscopeChart.getAxisRight();
        rightAxis.setEnabled(false);

        gyroscopeChart.getXAxis().setDrawGridLines(false);
        gyroscopeChart.setDrawBorders(false);

        xAngularSpeedText = findViewById(R.id.xAngularSpeedValueText);
        yAngularSpeedText = findViewById(R.id.yAngularSpeedValueText);
        zAngularSpeedText = findViewById(R.id.zAngularSpeedValueText);

        startPlot();
    }

    @Override
    protected void onResume(){
        super.onResume();

        Sensor gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        if(gyroscopeSensor != null){
            sensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_GAME);
        }
        else{
            Toast.makeText(this, "No gyroscope sensor found!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause(){
        super.onPause();

        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent){
        final float xAngularSpeed = sensorEvent.values[0];
        final float yAngularSpeed = sensorEvent.values[1];
        final float zAngularSpeed = sensorEvent.values[2];

        updateTextData(xAngularSpeed, yAngularSpeed, zAngularSpeed);

        if(plotData){
            addDataEntry(xAngularSpeed, yAngularSpeed, zAngularSpeed);

            plotData = false;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) { }

    private void updateTextData(final float xAngularSpeed, final float yAngularSpeed, final float zAngularSpeed){
        xAngularSpeedText.setText(String.format(Locale.US, "%.1f", xAngularSpeed));
        yAngularSpeedText.setText(String.format(Locale.US, "%.1f", yAngularSpeed));
        zAngularSpeedText.setText(String.format(Locale.US, "%.1f", zAngularSpeed));
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

    private void addDataEntry(final float xAngularSpeed, final float yAngularSpeed, final float zAngularSpeed){
        LineData data = gyroscopeChart.getData();

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

            data.addEntry(new Entry(xData.getEntryCount(), xAngularSpeed + CHART_RANGE / 2.f), 0);
            data.addEntry(new Entry(yData.getEntryCount(), yAngularSpeed + CHART_RANGE / 2.f), 1);
            data.addEntry(new Entry(zData.getEntryCount(), zAngularSpeed + CHART_RANGE / 2.f), 2);

            data.notifyDataChanged();

            gyroscopeChart.notifyDataSetChanged();
            gyroscopeChart.setVisibleXRangeMaximum(MAX_POINTS_DISPLAYED);
            gyroscopeChart.moveViewToX(data.getEntryCount());
        }
    }

    private ILineDataSet createXDataSet(){
        LineDataSet set = new LineDataSet(null, "X Angular Speed");
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
        LineDataSet set = new LineDataSet(null, "Y Angular Speed");
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
        LineDataSet set = new LineDataSet(null, "Z Angular Speed");
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
