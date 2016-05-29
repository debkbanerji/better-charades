package com.example.bettercharades;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;

public class AddItemActivity extends AppCompatActivity {

    TextView headingText;
    EditText itemText;
    Button addButton;
    Button finishButton;
    FileOutputStream fos;
    boolean hasAdded;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        headingText = (TextView) findViewById(R.id.addItemHeading);
        Intent intent = getIntent();

        title = intent.getStringExtra("CATEGORY_NAME");
        headingText.setText("Creating category \"" + title + "\"");

        hasAdded = false;


        itemText = (EditText) findViewById(R.id.itemText);

        addButton = (Button) findViewById(R.id.addButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.e("Add hasadded: ", Boolean.toString(hasAdded));
                addtoCategoory();
            }
        });

        finishButton = (Button) findViewById(R.id.finishCreationButton);

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.e("hasadded: ", Boolean.toString(hasAdded));
                addtoCategoory();

                if (hasAdded) {
                    try {
                        fos.close();
                    } catch (Exception e) {
                        Log.e("File IO Error", e.getMessage());
                    }
                }

                Toast.makeText(getApplicationContext(), "Created Category \"" + title + "\""
                        , Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(AddItemActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void addtoCategoory() {
        String item = (itemText.getText().toString());

        if (!hasAdded && item.length() > 0) {
            try {
                fos = openFileOutput(title + ".category.txt", Context.MODE_PRIVATE);
                hasAdded = true;
            } catch (Exception e) {
                Log.e("File IO Error", e.getMessage());
            }
        }

        if (item.length() > 0) {
            itemText.setText("");
            try {
                fos.write((item + "\n").getBytes());
            } catch (Exception e) {
                Log.e("File IO Error", e.getMessage());
            }
        }
    }

}


