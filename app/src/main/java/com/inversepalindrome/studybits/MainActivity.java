/*
* Copyright (c) 2018 Inverse Palindrome
* StudyBits - MainActivity.java
* https://inversepalindrome.com/
*/


package com.inversepalindrome.studybits;

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

        final Button button = findViewById(R.id.CompassButton);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent compassIntent = new Intent(MainActivity.this, CompassActivity.class);
                startActivity(compassIntent);
            }
        });
    }
}
