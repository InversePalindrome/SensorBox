/*
Copyright (c) 2019 Inverse Palindrome
SensorBox - SoundActivity.java
https://inversepalindrome.com/
*/


package com.inversepalindrome.sensorbox;

import com.cardiomood.android.controls.gauge.SpeedometerGauge;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.os.Build;
import android.os.Handler;
import android.content.pm.PackageManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.media.MediaRecorder;
import java.io.IOException;
import java.util.Locale;
import android.view.View;


public class SoundActivity extends AppCompatActivity {
    private MediaRecorder mediaRecorder;

    private SpeedometerGauge speedometer;

    private TextView soundAmplitudeText;
    private ImageView recordButton;

    private final int AUDIO_REQUEST_CODE = 200;
    private final int AUDIO_RECORDING_DELAY = 1000;
    private final int maxDecibelScale = 120;

    private final double REFERENCE_AMPLITUDE = 1.0;

    private boolean isRecording = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.RECORD_AUDIO,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, AUDIO_REQUEST_CODE);
        }

        speedometer = findViewById(R.id.speedometer);

        speedometer.setLabelConverter(new SpeedometerGauge.LabelConverter() {
            @Override
            public String getLabelFor(double progress, double maxProgress) {
                return String.format(Locale.US, "%.1f", progress);
            }
        });
        speedometer.setMaxSpeed(maxDecibelScale);
        speedometer.setMajorTickStep(20);
        speedometer.setMinorTicks(1);
        speedometer.setLabelTextSize(32);
        speedometer.addColoredRange(0, maxDecibelScale / 3, Color.GREEN);
        speedometer.addColoredRange(maxDecibelScale / 3, 2 * maxDecibelScale / 3, Color.YELLOW);
        speedometer.addColoredRange(2 * maxDecibelScale / 3, maxDecibelScale, Color.RED);

        soundAmplitudeText = findViewById(R.id.soundAmplitudeValueText);

        recordButton = findViewById(R.id.recordButton);
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRecording){
                    stopRecording();
                }
                else{
                    startRecording();
                }

                isRecording = !isRecording;
            }
        });

        startUpdate();
    }

    @Override
    public void onPause(){
        super.onPause();

        stopRecording();
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(this, "No audio recording permissions given!", Toast.LENGTH_LONG).show();
        }
    }

    private void startUpdate(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                final double amplitude = getAmplitude();

                speedometer.setSpeed(amplitude);
                soundAmplitudeText.setText(String.format(Locale.US, "%.1f", amplitude));

                handler.postDelayed(this, AUDIO_RECORDING_DELAY);
            }
        }, AUDIO_RECORDING_DELAY);
    }

    private void startRecording(){
        if(mediaRecorder == null)
        {
            mediaRecorder = new MediaRecorder();

            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setOutputFile("/dev/null");

            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        recordButton.setImageResource(R.drawable.stop_icon);
    }

    private void stopRecording(){
        if(mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        }

        recordButton.setImageResource(R.drawable.play_icon);
    }

    private double getAmplitude(){
        if(mediaRecorder != null){
            final double maxAmplitude = mediaRecorder.getMaxAmplitude();

            double amplitude = 20 * Math.log10(maxAmplitude / REFERENCE_AMPLITUDE);
            amplitude = amplitude < 0 ? 0 : amplitude;

            return amplitude;
        }
        else{
            return 0.0;
        }
    }
}
