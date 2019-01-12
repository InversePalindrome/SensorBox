/*
Copyright (c) 2019 Inverse Palindrome
SensorBox - EnvironmentActivity.java
https://inversepalindrome.com/
*/


package com.inversepalindrome.sensorbox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;
import android.widget.TextView;

import java.util.Locale;


public class EnvironmentActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;

    private TextView temperatureText;
    private TextView humidityText;
    private TextView pressureText;
    private TextView lightText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_environment);

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);

        temperatureText = findViewById(R.id.temperatureValueText);
        humidityText = findViewById(R.id.ambientHumidityValueText);
        pressureText = findViewById(R.id.ambientPressureValueText);
        lightText = findViewById(R.id.lightValueText);
    }

    @Override
    protected void onResume() {
        super.onResume();

        final Sensor temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);

        if(temperatureSensor != null){
            sensorManager.registerListener(this, temperatureSensor, SensorManager.SENSOR_DELAY_FASTEST);
        }
        else{
            Toast.makeText(this, "No temperature sensor found!", Toast.LENGTH_LONG).show();
        }

        final Sensor humiditySensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);

        if(humiditySensor != null){
            sensorManager.registerListener(this, humiditySensor, SensorManager.SENSOR_DELAY_FASTEST);
        }
        else{
            Toast.makeText(this, "No humidity sensor found!", Toast.LENGTH_LONG).show();
        }

        final Sensor pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);

        if(pressureSensor != null){
            sensorManager.registerListener(this, pressureSensor, SensorManager.SENSOR_DELAY_FASTEST);
        }
        else{
            Toast.makeText(this, "No pressure sensor found!", Toast.LENGTH_LONG).show();
        }

        final Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        if(lightSensor != null){
            sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_FASTEST);
        }
        else{
            Toast.makeText(this, "No light sensor found!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent){
       if(sensorEvent.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE){
           final float ambientTemperature = sensorEvent.values[0];

           temperatureText.setText(String.format(Locale.US, "%.1f", ambientTemperature));
       }
       else if(sensorEvent.sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY){
           final float ambientHumidity = sensorEvent.values[0];

           humidityText.setText(String.format(Locale.US, "%.1f", ambientHumidity));
       }
       else if(sensorEvent.sensor.getType() == Sensor.TYPE_PRESSURE){
           final float ambientPressure = sensorEvent.values[0];

           pressureText.setText(String.format(Locale.US, "%.1f", ambientPressure));
       }
       else if(sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT){
           final float ambientLight = sensorEvent.values[0];

           lightText.setText(String.format(Locale.US, "%.1f", ambientLight));
       }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) { }
}
