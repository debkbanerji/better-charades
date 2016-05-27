package com.example.bettercharades;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PlayActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mGyroscope;
    private TextView infoText;
    private int tiltFactor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);


        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);



        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        mSensorManager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_GAME);
        infoText = (TextView) findViewById(R.id.orientationText);

        Intent intent = getIntent();

        String title = intent.getStringExtra("CATEGORY");
        infoText.setText(title);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        double tiltThreshold = 0.2;
        tiltFactor = 0;
        if (Math.abs(event.values[0]) < tiltThreshold) {
            tiltFactor = -1;
        } else if (Math.abs(event.values[0]) > 1.0 - tiltThreshold) {
            tiltFactor = 1;
        }
//        infoText.setText(Integer.toString(tiltFactor));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //do nothing
    }
}
