/*
Copyright (c) 2019 Inverse Palindrome
SensorBox - MainActivity.java
https://inversepalindrome.com/
*/


package com.inversepalindrome.sensorbox;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.content.Intent;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button accelerometerButton = findViewById(R.id.accelerometerButton);
        accelerometerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent accelerometerIntent = new Intent(MainActivity.this, AccelerometerActivity.class);
                startActivity(accelerometerIntent);
            }
        });

        final Button compassButton = findViewById(R.id.compassButton);
        compassButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent compassIntent = new Intent(MainActivity.this, CompassActivity.class);
                startActivity(compassIntent);
            }
        });

        final Button gyroscopeButton = findViewById(R.id.gyroscopeButton);
        gyroscopeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent gyroscopeIntent = new Intent(MainActivity.this, GyroscopeActivity.class);
                startActivity(gyroscopeIntent);
            }
        });

        final Button environmentButton = findViewById(R.id.environmentButton);
        environmentButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent environmentIntent = new Intent(MainActivity.this, EnvironmentActivity.class);
                startActivity(environmentIntent);
            }
        });

        final Button magnetButton = findViewById(R.id.magnetButton);
        magnetButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent magnetIntent = new Intent(MainActivity.this, MagnetActivity.class);
                startActivity(magnetIntent);
            }
        });

        final Button soundButton = findViewById(R.id.soundButton);
        soundButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent soundIntent = new Intent(MainActivity.this, SoundActivity.class);
                startActivity(soundIntent);
            }
        });
    }
}
