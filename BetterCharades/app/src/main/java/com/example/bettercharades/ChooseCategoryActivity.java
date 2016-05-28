package com.example.bettercharades;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChooseCategoryActivity extends AppCompatActivity {

    public Spinner timePicker;
    public int time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_category);

        String[] fileList = fileList();
        List<String> categoryList = new LinkedList<>();

        String rgx = "(.+).category.txt";
        for (int i = 0; i < fileList.length; i++) {
            Matcher m = Pattern.compile(rgx).matcher(fileList[i]);
            if (m.find()) {
                categoryList.add(m.group(1));
            }
        }

        if (categoryList.isEmpty()) {
            TextView headingText = (TextView) findViewById(R.id.chooseCategoryHeading);
            headingText.setText("No categories available\nCreate or download categories to start playing");
        }

        String[] categories = (String[]) categoryList.toArray(new String[0]);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1, categories);

        ListView lv = (ListView) findViewById(R.id.categoryList);
        lv.setAdapter(adapter);

        time = 60100;
        timePicker = (Spinner) findViewById(R.id.timePicker);

        final List<String> times = new ArrayList<String>();
        times.add("30s");
        times.add("60s");
        times.add("90s");
        times.add("120s");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, times);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timePicker.setAdapter(dataAdapter);
        timePicker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    time = 30100;
                } else if (position == 1) {
                    time = 60100;
                } else if (position == 2) {
                    time = 90100;
                } else {
                    time = 120100;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent intent = new Intent(ChooseCategoryActivity.this, PlayActivity.class);
                String categoryString = (String) parent.getItemAtPosition(position);
                intent.putExtra("CATEGORY", categoryString);
                intent.putExtra("TIME", time);
                startActivity(intent);
            }
        });
    }
}
