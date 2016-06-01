package com.example.bettercharades;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class EditCategoryActivity extends AppCompatActivity {

    TextView headingText;
    Button addButton;
    Button finishButton;
    ListView itemListView;
    ArrayAdapter adapter;
    String category;
    List<String> itemList;
    EditText itemText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);

        headingText = (TextView) findViewById(R.id.editCategoryHeading);
        addButton = (Button) findViewById(R.id.addButton);
        finishButton = (Button) findViewById(R.id.finishButton);
        itemListView = (ListView) findViewById(R.id.itemListView);
        itemText = (EditText) findViewById(R.id.itemText);

        Intent intent = getIntent();
        category = intent.getStringExtra("CATEGORY_NAME");
        itemList = new LinkedList<>();

        headingText.setText("Editing Category: " + category);

        try {
            FileInputStream fis = openFileInput(category + ".category.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String line = reader.readLine();

            while (line != null && !line.equals("")) {
                itemList.add(line);
                line = reader.readLine();
            }
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1, itemList);
        itemListView.setAdapter(adapter);
        registerForContextMenu(itemListView);

        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                parent.showContextMenuForChild(view);
            }
        });

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeToFile();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item = itemText.getText().toString();
                itemText.setText("");
                if (!itemList.contains(item)) {
                    itemList.add(item);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getApplicationContext(), "Category already contains \"" + item + "\""
                            , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_delete_item, menu);


        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        int itemID = info.position;
        menu.setHeaderTitle(itemList.get(itemID));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.deleteItem:
                itemList.remove(info.position);
                adapter.notifyDataSetChanged();
        }

        return super.onContextItemSelected(item);
    }

    public void writeToFile() {
        if (!itemList.isEmpty()) {
            try {
                FileOutputStream fos = openFileOutput(category + ".category.txt", Context.MODE_PRIVATE);
                for (String item : itemList) {
                    fos.write((item + "\n").getBytes());
                }
                fos.close();
                Toast.makeText(getApplicationContext(), "Updated Category \"" + category + "\""
                        , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditCategoryActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } catch (Exception e) {
                //do nothing
            }
        } else {
            Toast.makeText(getApplicationContext(), "Category cannot be empty"
                    , Toast.LENGTH_SHORT).show();
        }

    }
}
