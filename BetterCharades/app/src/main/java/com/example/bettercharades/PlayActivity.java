package com.example.bettercharades;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    RelativeLayout background;
    String title;
    MediaPlayer correct;
    MediaPlayer incorrect;
    Button finishGame;
    int tiltFactor;
    int oldTiltFactor;
    List<ResultPair> results;
    ListView resultView;
    List<String> questions;
    float inversionFactor;
    int correctItems;
    int totalItems;
    int currentItem;
    int time;
    boolean gameOver;
    boolean gameHasBegun;

    @Override
    public void onSensorChanged(SensorEvent event) {
        float tiltThreshold = 7.0f;
        tiltFactor = 0;
        float rawValue = event.values[2] * inversionFactor;
        if (rawValue < -tiltThreshold) {
            tiltFactor = -1;
        } else if (rawValue > tiltThreshold) {
            tiltFactor = 1;
        }

        if (gameHasBegun && !gameOver && tiltFactor != oldTiltFactor) { //If the game is ongoing;
            if (tiltFactor == 0) {
                background.setBackgroundColor(ContextCompat.getColor(PlayActivity.this, R.color.colorPrimary));
                currentItem++;
                infoText.setText(questions.get(currentItem % questions.size()));
            } else if (tiltFactor < 0) {
                background.setBackgroundColor(ContextCompat.getColor(PlayActivity.this, R.color.colorSuccess));
                totalItems++;
                correctItems++;
                if (getIntent().getBooleanExtra("SOUND_ON", true)) {
                    correct = MediaPlayer.create(PlayActivity.this, R.raw.correct);
                    correct.start();
                    correct.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        public void onCompletion(MediaPlayer mp) {
                            mp.release();

                        }


                    });
                }
                results.add(new ResultPair(questions.get(currentItem % questions.size()), true));
                infoText.setText("Correct!");
            } else {
                background.setBackgroundColor(ContextCompat.getColor(PlayActivity.this, R.color.colorFailure));
                totalItems++;
                if (getIntent().getBooleanExtra("SOUND_ON", true)) {
                    incorrect = MediaPlayer.create(PlayActivity.this, R.raw.incorrect);
                    incorrect.start();
                    incorrect.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        public void onCompletion(MediaPlayer mp) {
                            mp.release();

                        }


                    });
                }
                results.add(new ResultPair(questions.get(currentItem % questions.size()), false));
                infoText.setText("Pass");
            }
        }
        oldTiltFactor = tiltFactor;
//        infoText.setText(Float.toString(rawValue));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        final Intent intent = getIntent();


        try {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        } catch (Exception e) {
        }


        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
//        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
//        mGyroscope = SensorManager.getOrientation()
        mSensorManager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_GAME);
        infoText = (TextView) findViewById(R.id.infoText);
        timeText = (TextView) findViewById(R.id.timeText);
        background = (RelativeLayout) findViewById(R.id.playBackground);
        resultView = (ListView) findViewById(R.id.resultView);
        finishGame = (Button) findViewById(R.id.finishGameButton);
        gameOver = false;
        totalItems = 0;
        correctItems = 0;
        currentItem = 0;
        results = new ArrayList<>();


        title = intent.getStringExtra("CATEGORY");
        time = intent.getIntExtra("TIME", 30100);
        inversionFactor = intent.getIntExtra("TILT_FACTOR", 1);

        questions = questionList();

        new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {
                timeText.setText("Game starts in " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                //game is over
                gameHasBegun = true;
                infoText.setText(questions.get(currentItem % questions.size()));
                new CountDownTimer(time, 1000) {

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
        }.start();
    }

    private Boolean exit = false;

    @Override
    public void onBackPressed() {
        if (exit) {
            android.os.Process.killProcess(android.os.Process.myPid());
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

    }

    public void finishGame() {
        timeText.setText("You got " + correctItems + " out of " + totalItems + " correct");
        timeText.setTextColor(Color.DKGRAY);
        timeText.setTextSize(35);
        background.setBackgroundColor(ContextCompat.getColor(PlayActivity.this, R.color.colorBackground));
        infoText.setVisibility(View.INVISIBLE);
        resultView.setVisibility(View.VISIBLE);
        finishGame.setVisibility(View.VISIBLE);
        ResultListAdapter resultListAdapter = new ResultListAdapter(
                PlayActivity.this, R.layout.result_item, results);
        resultView.setAdapter(resultListAdapter);


        finishGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.os.Process.killProcess(android.os.Process.myPid());
                Intent intent = new Intent(PlayActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
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
        return result;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //do nothing
    }
}
