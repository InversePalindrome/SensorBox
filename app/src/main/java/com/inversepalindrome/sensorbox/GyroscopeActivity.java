/*
Copyright (c) 2019 Inverse Palindrome
SensorBox - GyroscopeActivity.java
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


public class GyroscopeActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;

    private TextView xAngularSpeedText;
    private TextView yAngularSpeedText;
    private TextView zAngularSpeedText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gyroscope);

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);

        xAngularSpeedText = findViewById(R.id.angularSpeedXValueText);
        yAngularSpeedText = findViewById(R.id.angularSpeedYValueText);
        zAngularSpeedText = findViewById(R.id.angularSpeedZValueText);
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
        final float angularSpeedX = sensorEvent.values[0];
        final float angularSpeedY = sensorEvent.values[1];
        final float angularSpeedZ = sensorEvent.values[2];

        xAngularSpeedText.setText(String.format(Locale.US, "%.1f", angularSpeedX));
        yAngularSpeedText.setText(String.format(Locale.US, "%.1f", angularSpeedY));
        zAngularSpeedText.setText(String.format(Locale.US, "%.1f", angularSpeedZ));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) { }
}
