/*
Copyright (c) 2019 Inverse Palindrome
SensorBox - SoundActivity.java
https://inversepalindrome.com/
*/


package com.inversepalindrome.sensorbox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.media.MediaRecorder;

import java.io.IOException;
import java.util.Locale;


public class SoundActivity extends AppCompatActivity {
    private MediaRecorder mediaRecorder;

    private TextView soundAmplitudeText;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound);

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile("/dev/null");

        try {
            mediaRecorder.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaRecorder.start();

        soundAmplitudeText = findViewById(R.id.soundAmplitudeValueText);

        final Handler handler = new Handler();
        handler.post(new Runnable(){
            @Override
            public void run() {
                final double maxAmplitude = mediaRecorder.getMaxAmplitude();
                final double referenceAmplitude = 0.00001;
                final double amplitude = 20 * Math.log10(maxAmplitude / referenceAmplitude);

                soundAmplitudeText.setText(String.format(Locale.US, "%.1f", amplitude));

                handler.postDelayed(this,500);
            }
        });
    }
}
