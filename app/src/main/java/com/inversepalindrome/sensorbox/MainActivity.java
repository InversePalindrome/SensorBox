/*
Copyright (c) 2019 Inverse Palindrome
SensorBox - MainActivity.java
https://inversepalindrome.com/
*/


package com.inversepalindrome.sensorbox;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.os.Bundle;
import android.content.Intent;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String[] labels = { "Accelerometer", "Compass", "Gyroscope", "Environment", "Magnet", "Sound"};
        final int[] imageIDs = { R.drawable.accelerometer_icon, R.drawable.compass_icon, R.drawable.gyroscope_icon
                , R.drawable.environment_icon, R.drawable.magnet_icon, R.drawable.volume_icon };

        MenuGridAdapter menuGridAdapter = new MenuGridAdapter(MainActivity.this, labels, imageIDs);

        GridView grid = findViewById(R.id.grid);
        grid.setAdapter(menuGridAdapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String currentItem = labels[i];

                switch(currentItem){
                    case "Accelerometer":
                        startActivity(new Intent(MainActivity.this, AccelerometerActivity.class));
                        break;
                    case "Compass":
                        startActivity(new Intent(MainActivity.this, CompassActivity.class));
                        break;
                    case "Gyroscope":
                        startActivity(new Intent(MainActivity.this, GyroscopeActivity.class));
                        break;
                    case "Environment":
                        startActivity(new Intent(MainActivity.this, EnvironmentActivity.class));
                        break;
                    case "Magnet":
                        startActivity(new Intent(MainActivity.this, MagnetActivity.class));
                        break;
                    case "Sound":
                        startActivity(new Intent(MainActivity.this, SoundActivity.class));
                        break;
                }
            }
        });
    }
}
