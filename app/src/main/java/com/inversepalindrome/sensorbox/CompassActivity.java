/*
Copyright (c) 2019 Inverse Palindrome
SensorBox - CompassActivity.java
https://inversepalindrome.com/
*/


package com.inversepalindrome.sensorbox;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.hardware.SensorEventListener;
import android.os.Bundle;

import java.util.Locale;


public class CompassActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;

    private ImageView compassImage;
    private TextView degreeText;

    private float currentDegree = 0.f;

    private static final long ANIMATION_DURATION = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);

        compassImage = findViewById(R.id.compassImage);
        degreeText = findViewById(R.id.degreeValueText);
    }

    @Override
    protected void onResume(){
        super.onResume();

        final Sensor orientationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

        if(orientationSensor != null) {
            sensorManager.registerListener(this, orientationSensor, sensorManager.SENSOR_DELAY_GAME);
        }
        else {
            Toast.makeText(this, "No orientation sensor found!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause(){
        super.onPause();

        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event){
        final float degree = Math.round(event.values[0]);

        degreeText.setText(String.format(Locale.US, "%.1f", degree));

        RotateAnimation rotateAnimation = new RotateAnimation(currentDegree, -degree, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        rotateAnimation.setDuration(ANIMATION_DURATION);
        rotateAnimation.setFillAfter(true);

        compassImage.startAnimation(rotateAnimation);

        currentDegree = -degree;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }
}
