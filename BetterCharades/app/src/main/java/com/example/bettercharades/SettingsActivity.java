package com.example.bettercharades;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    public Spinner timePicker;
    public int timeIndex;
    public boolean soundOn;
    public Switch soundSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        timePicker = (Spinner) findViewById(R.id.timePicker);

        try {
            FileInputStream fis = openFileInput("settings.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

            timeIndex = Integer.parseInt(reader.readLine());
            soundOn = Boolean.parseBoolean(reader.readLine());

        } catch (Exception e) {
//            Log.e("Settings error", e.getMessage());
            timeIndex = 0;
            soundOn = true;
            try {
                FileOutputStream fos = openFileOutput("settings.txt", Context.MODE_PRIVATE);
                fos.write((Integer.toString(timeIndex)+'\n').getBytes());
                fos.write(Boolean.toString(soundOn).getBytes());
                fos.close();

            } catch (Exception e2) {
                //do nothing;
            }
        }

        soundSwitch = (Switch) findViewById(R.id.soundSwitch);
        soundSwitch.setChecked(soundOn);
        soundSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                soundOn = isChecked;
                writeSettings();
            }
        });

        final List<String> times = new ArrayList<String>();
        times.add("30 seconds");
        times.add("60 seconds");
        times.add("90 seconds");
        times.add("120 seconds");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, times);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timePicker.setAdapter(dataAdapter);
        timePicker.setSelection(timeIndex);
        timePicker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                timeIndex = position;
//                if (position == 0) {
//                    time = 30100;
//                } else if (position == 1) {
//                    time = 60100;
//                } else if (position == 2) {
//                    time = 90100;
//                } else {
//                    time = 120100;
//                }
                writeSettings();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    public void writeSettings() {
        try {
//            Log.e("Time index", Integer.toString(timeIndex));
            FileOutputStream fos = openFileOutput("settings.txt", Context.MODE_PRIVATE);
            fos.write((Integer.toString(timeIndex)+'\n').getBytes());
            fos.write(Boolean.toString(soundOn).getBytes());
            fos.close();
        } catch (Exception e) {
            //do nothing;
        }
    }
}
