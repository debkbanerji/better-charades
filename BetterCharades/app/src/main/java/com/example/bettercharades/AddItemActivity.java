package com.example.bettercharades;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;

public class AddItemActivity extends AppCompatActivity {

    TextView headingText;
    EditText itemText;
    Button addButton;
    Button finishButton;
    FileOutputStream fos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        headingText = (TextView) findViewById(R.id.addItemHeading);
        Intent intent = getIntent();

        String title = intent.getStringExtra("CATEGORY_NAME");
        headingText.setText("Creating category \"" + title + "\"");


        try {
//            fos = openFileOutput(title, Context.MODE_PRIVATE);
            fos = openFileOutput(title + ".category.txt", Context.MODE_PRIVATE);
//            fos.write("TEST".getBytes());
//            fos.close();
        } catch (Exception e) {
            Log.e("File IO Error", e.getMessage());
        }
//        Log.i("IO worked", "No errors writing to/creating file");

        itemText = (EditText) findViewById(R.id.itemText);

        addButton = (Button) findViewById(R.id.addButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item = (itemText.getText().toString() + "\n");
                if (item.length() > 0) {
                    try {
                        fos.write(item.getBytes());
                    } catch (Exception e) {
                        Log.e("File IO Error", e.getMessage());
                    }
                }
                itemText.setText("");
            }
        });

        finishButton = (Button) findViewById(R.id.finishCreationButton);

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item = (itemText.getText().toString() + "\n");
                if (item.length() > 0) {
                    try {
                        fos.write(item.getBytes());
                    } catch (Exception e) {
                        Log.e("File IO Error", e.getMessage());
                    }
                }
                try {
                    fos.close();
                } catch (Exception e) {
                    Log.e("File IO Error", e.getMessage());
                }
                Intent intent = new Intent(AddItemActivity.this, ChooseActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

}


