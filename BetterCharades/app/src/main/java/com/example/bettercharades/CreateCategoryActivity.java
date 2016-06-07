package com.example.bettercharades;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateCategoryActivity extends AppCompatActivity {

    EditText categoryName;
    Button createCategoryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_category);

        categoryName = (EditText) findViewById(R.id.categoryName);
        createCategoryButton = (Button) findViewById(R.id.enterNameButton);

        createCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!categoryName.getText().toString().equals("")) {
                    Intent createIntent = new Intent(CreateCategoryActivity.this, EditCategoryActivity.class);
                    createIntent.putExtra("CATEGORY_NAME", categoryName.getText().toString());
                    startActivity(createIntent);
                }
            }
        });
    }
}
