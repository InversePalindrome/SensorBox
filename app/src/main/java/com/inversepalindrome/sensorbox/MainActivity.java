/*
* Copyright (c) 2019 Inverse Palindrome
* SensorBox - MainActivity.java
* https://inversepalindrome.com/
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

        final Button compassButton = findViewById(R.id.compassButton);
        compassButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent compassIntent = new Intent(MainActivity.this, CompassActivity.class);
                startActivity(compassIntent);
            }
        });

        final Button accelerometerButton = findViewById(R.id.accelerometerButton);
        accelerometerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent accelerometerIntent = new Intent(MainActivity.this, AccelerometerActivity.class);
                startActivity(accelerometerIntent);
            }
        });
    }
}
