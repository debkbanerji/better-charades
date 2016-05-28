package com.example.bettercharades;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PlayActivity extends AppCompatActivity implements SensorEventListener {

    SensorManager mSensorManager;
    Sensor mGyroscope;
    TextView infoText;
    TextView timeText;
    //    TextView currentCategoryText;
    RelativeLayout background;
    String title;
    MediaPlayer correct;
    MediaPlayer incorrect;
    int tiltFactor;
    int oldTiltFactor;
    List<ResultPair> results;
    GridView resultGrid;
    List<String> questions;
    int correctItems;
    int totalItems;
    int currentItem;
    int time;
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
                correct.start();
                results.add(new ResultPair(questions.get(currentItem % questions.size()), true));
                infoText.setText("Correct!");
            } else {
                background.setBackgroundColor(ContextCompat.getColor(PlayActivity.this, R.color.colorFailure));
                totalItems++;
                incorrect.start();
                results.add(new ResultPair(questions.get(currentItem % questions.size()), false));
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
//        currentCategoryText = (TextView) findViewById(R.id.currentCategoryText);
        background = (RelativeLayout) findViewById(R.id.playBackground);
        resultGrid = (GridView) findViewById(R.id.gridView);
        gameOver = false;
        totalItems = 0;
        correctItems = 0;
        currentItem = 0;
        results = new ArrayList<>();
        correct = MediaPlayer.create(PlayActivity.this, R.raw.correct);
        incorrect = MediaPlayer.create(PlayActivity.this, R.raw.incorrect);


        final Intent intent = getIntent();

        title = intent.getStringExtra("CATEGORY");
        time = intent.getIntExtra("TIME", 60100);
//        currentCategoryText.setText(title);
        questions = questionList();
        infoText.setText(questions.get(currentItem));

        new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {
                timeText.setText(millisUntilFinished / 1000 + " seconds");
            }

            public void onFinish() {
                //game is over
                gameOver = true;
                finishGame();
            }
        }.start();
    }


    public void finishGame() {
        timeText.setText("You got " + correctItems + " out of " + totalItems + " correct");
        timeText.setTextColor(Color.DKGRAY);
        timeText.setTextSize(35);
        background.setBackgroundColor(ContextCompat.getColor(PlayActivity.this, R.color.colorBackground));
        infoText.setVisibility(View.INVISIBLE);
        resultGrid.setVisibility(View.VISIBLE);
        ResultListAdapter resultListAdapter = new ResultListAdapter(
                PlayActivity.this, R.layout.result_item, results);
        resultGrid.setAdapter(resultListAdapter);

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


}
