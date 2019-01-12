/*
Copyright (c) 2019 Inverse Palindrome
SensorBox - MagnetActivity.java
https://inversepalindrome.com/
*/


package com.inversepalindrome.sensorbox;

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

    private TextView xGeomagneticFieldText;
    private TextView yGeomagneticFieldText;
    private TextView zGeomagneticFieldText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magnet);

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);

        xGeomagneticFieldText = findViewById(R.id.xGeomagneticFieldValueText);
        yGeomagneticFieldText = findViewById(R.id.yGeomagneticFieldValueText);
        zGeomagneticFieldText = findViewById(R.id.zGeomagneticFieldValueText);
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

        xGeomagneticFieldText.setText(String.format(Locale.US, "%.1f", xGeomagneticField));
        yGeomagneticFieldText.setText(String.format(Locale.US, "%.1f", yGeomagneticField));
        zGeomagneticFieldText.setText(String.format(Locale.US, "%.1f", zGeomagneticField));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) { }
}
