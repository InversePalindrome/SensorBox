/*
Copyright (c) 2019 Inverse Palindrome
SensorBox - AccelerometerActivity.java
https://inversepalindrome.com/
*/


package com.inversepalindrome.sensorbox;

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

    private TextView xAccelerationText;
    private TextView yAccelerationText;
    private TextView zAccelerationText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);

        xAccelerationText = findViewById(R.id.xAccelerationValueText);
        yAccelerationText = findViewById(R.id.yAccelerationValueText);
        zAccelerationText = findViewById(R.id.zAccelerationValueText);
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

        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event){
        final float xAcceleration = event.values[0];
        final float yAcceleration = event.values[1];
        final float zAcceleration = event.values[2];

        xAccelerationText.setText(String.format(Locale.US, "%.1f", xAcceleration));
        yAccelerationText.setText(String.format(Locale.US, "%.1f", yAcceleration));
        zAccelerationText.setText(String.format(Locale.US, "%.1f", zAcceleration));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }
}
