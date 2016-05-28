package com.example.bettercharades;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class PlayActivity extends AppCompatActivity implements SensorEventListener {

    SensorManager mSensorManager;
    Sensor mGyroscope;
    TextView infoText;
    TextView timeText;
    TextView currentCategoryText;
    RelativeLayout background;
    String title;
    int tiltFactor;
    int oldTiltFactor;
    List<resultPair> results;
    List<String> questions;
    int correctItems;
    int totalItems;
    int currentItem;
    boolean gameOver;

    @Override
    public void onSensorChanged(SensorEvent event) {
        int tiltThreshold = 20;
        tiltFactor = 0;
        if (Math.abs(event.values[0] * 100) < tiltThreshold) {
            tiltFactor = -1;
        } else if (Math.abs(event.values[0] * 100) > (100 - tiltThreshold)) {
            tiltFactor = 1;
        }

        if (!gameOver && tiltFactor != oldTiltFactor) { //If the game is ongoing;
            if (tiltFactor == 0) {
                background.setBackgroundColor(ContextCompat.getColor(PlayActivity.this, R.color.colorPrimary));
                currentItem++;
                infoText.setText(questions.get(currentItem % questions.size()));
            } else if (tiltFactor < 0) {
                background.setBackgroundColor(ContextCompat.getColor(PlayActivity.this, R.color.colorSuccess));
                totalItems++;
                correctItems++;
                infoText.setText("Correct!");
            } else {
                background.setBackgroundColor(ContextCompat.getColor(PlayActivity.this, R.color.colorFailure));
                totalItems++;
                infoText.setText("Pass");
            }
        }
        oldTiltFactor = tiltFactor;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        try {
            View decorView = getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        } catch (Exception e) {
            //do nothing
        }


        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        mSensorManager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_GAME);
        infoText = (TextView) findViewById(R.id.infoText);
        timeText = (TextView) findViewById(R.id.timeText);
        currentCategoryText = (TextView) findViewById(R.id.currentCategoryText);
        background = (RelativeLayout) findViewById(R.id.playBackground);
        gameOver = false;
        totalItems = 0;
        correctItems = 0;
        currentItem = 0;
        results = new LinkedList<>();

        final Intent intent = getIntent();

        title = intent.getStringExtra("CATEGORY");
        currentCategoryText.setText(title);
        questions = questionList();
        infoText.setText(questions.get(currentItem));

        new CountDownTimer(30100, 1000) {

            public void onTick(long millisUntilFinished) {
                timeText.setText(millisUntilFinished / 1000 + " seconds");
            }

            public void onFinish() {
                //game is over
                gameOver = true;
                finishGame();
                timeText.setText("done!");
            }
        }.start();
    }


    public void finishGame() {
        //finish the game
    }

    public List<String> questionList() {

        List<String> result = new ArrayList<>();
        try {
            FileInputStream fis = openFileInput(title + ".category.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String line = reader.readLine();

            while (line != null && !line.equals("")) {
                result.add(line);
                line = reader.readLine();
            }

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            return result;
        }
        Collections.shuffle(result, new Random(System.currentTimeMillis()));
//        Log.e("Questions", result.toString());
//        Log.e("Questions", Integer.toString(result.size()));
        return result;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //do nothing
    }

    private class resultPair {
        String item;
        boolean isCorrect;

        resultPair(String item, boolean isCorrect) {
            this.item = item;
            this.isCorrect = isCorrect;
        }
    }
}
