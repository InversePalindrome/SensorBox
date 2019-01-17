/*
Copyright (c) 2019 Inverse Palindrome
SensorBox - MagnetActivity.java
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


public class MagnetActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;

    private Thread thread;

    private LineChart magnetismChart;

    private TextView xGeomagneticFieldText;
    private TextView yGeomagneticFieldText;
    private TextView zGeomagneticFieldText;

    private final static float MAX_POINTS_DISPLAYED = 150;
    private final static float CHART_RANGE = 10;

    private boolean plotData = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magnet);

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);

        magnetismChart = findViewById(R.id.magnetism_chart);
        magnetismChart.getDescription().setEnabled(true);
        magnetismChart.setTouchEnabled(false);
        magnetismChart.setDragEnabled(false);
        magnetismChart.setScaleEnabled(true);
        magnetismChart.setDrawGridBackground(false);
        magnetismChart.setPinchZoom(false);
        magnetismChart.setBackgroundColor(Color.WHITE);
        magnetismChart.getDescription().setText("Geomagnetic Field Data");

        LineData emptyData = new LineData();
        emptyData.setValueTextColor(Color.BLACK);

        magnetismChart.setData(emptyData);

        Legend legend = magnetismChart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextColor(Color.BLACK);
        legend.setEnabled(true);

        XAxis xAxis = magnetismChart.getXAxis();
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawGridLines(true);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setEnabled(true);

        YAxis leftAxis = magnetismChart.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMaximum(CHART_RANGE);
        leftAxis.setAxisMinimum(0.f);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = magnetismChart.getAxisRight();
        rightAxis.setEnabled(false);

        magnetismChart.getXAxis().setDrawGridLines(false);
        magnetismChart.setDrawBorders(false);

        xGeomagneticFieldText = findViewById(R.id.xGeomagneticFieldValueText);
        yGeomagneticFieldText = findViewById(R.id.yGeomagneticFieldValueText);
        zGeomagneticFieldText = findViewById(R.id.zGeomagneticFieldValueText);

        startPlot();
    }

    @Override
    protected void onResume(){
        super.onResume();

        final Sensor magnetSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        if(magnetSensor != null){
            sensorManager.registerListener(this, magnetSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        else{
            Toast.makeText(this, "No magnetic field sensor found!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause(){
        super.onPause();

        sensorManager.unregisterListener(this);
    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent){
        final float xGeomagneticField = sensorEvent.values[0];
        final float yGeomagneticField = sensorEvent.values[1];
        final float zGeomagneticField = sensorEvent.values[2];

        updateTextData(xGeomagneticField, yGeomagneticField, zGeomagneticField);

        if(plotData){
            addDataEntry(xGeomagneticField, yGeomagneticField, zGeomagneticField);

            plotData = false;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) { }

    private void updateTextData(final float xGeomagneticField, final float yGeomagneticField, final float zGeomagneticField){
        xGeomagneticFieldText.setText(String.format(Locale.US, "%.1f", xGeomagneticField));
        yGeomagneticFieldText.setText(String.format(Locale.US, "%.1f", yGeomagneticField));
        zGeomagneticFieldText.setText(String.format(Locale.US, "%.1f", zGeomagneticField));
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

    private void addDataEntry(final float xGeomagneticField, final float yGeomagneticField, final float zGeomagneticField){
        LineData data = magnetismChart.getData();

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

            data.addEntry(new Entry(xData.getEntryCount(), xGeomagneticField + CHART_RANGE / 2.f), 0);
            data.addEntry(new Entry(yData.getEntryCount(), yGeomagneticField + CHART_RANGE / 2.f), 1);
            data.addEntry(new Entry(zData.getEntryCount(), zGeomagneticField + CHART_RANGE / 2.f), 2);

            data.notifyDataChanged();

            magnetismChart.notifyDataSetChanged();
            magnetismChart.setVisibleXRangeMaximum(MAX_POINTS_DISPLAYED);
            magnetismChart.moveViewToX(data.getEntryCount());
        }
    }

    private ILineDataSet createXDataSet(){
        LineDataSet set = new LineDataSet(null, "X Geomagnetic Field");
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
        LineDataSet set = new LineDataSet(null, "Y Geomagnetic Field");
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
        LineDataSet set = new LineDataSet(null, "Z Geomagnetic Field");
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
