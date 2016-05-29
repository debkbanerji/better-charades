package com.example.bettercharades;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyCategories extends AppCompatActivity {

    ListView categoryListView;
    private DatabaseReference mRootReef;
    private DatabaseReference mCategories;
    private DatabaseReference mCategoryList;
    private Set dataBaseCategories;
    private List<String> categoryList;
    private ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_categories);

        categoryListView = (ListView) findViewById(R.id.myCategoriesList);
        dataBaseCategories = new HashSet();

        String[] fileList = fileList();
        ;
        categoryList = new LinkedList<>();

        String rgx = "(.+).category.txt";
        for (int i = 0; i < fileList.length; i++) {
            Matcher m = Pattern.compile(rgx).matcher(fileList[i]);
            if (m.find()) {
                categoryList.add(m.group(1));
            }
        }

        if (categoryList.isEmpty()) {
            TextView headingText = (TextView) findViewById(R.id.textView9);
            headingText.setText("No categories available\nCreate or download categories to start playing");
        }

        String[] categories = (String[]) categoryList.toArray(new String[0]);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1, categories);

        categoryListView = (ListView) findViewById(R.id.myCategoriesList);
        categoryListView.setAdapter(adapter);

        categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                String categoryString = (String) parent.getItemAtPosition(position);
                uploadCategory(categoryString);
            }
        });

        registerForContextMenu(categoryListView);

        mRootReef = FirebaseDatabase.getInstance().getReference();
        mCategories = mRootReef.child("categories");
        mCategoryList = mRootReef.child("categoryList");

        mCategoryList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map categoryListMap = (Map) dataSnapshot.getValue();
                if (categoryListMap == null) {
                    dataBaseCategories = new HashSet<String>();
                } else {
                    dataBaseCategories = categoryListMap.keySet();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
//                Toast.makeText(getApplicationContext(), "Network error: check your connection"
//                        , Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_my_category, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.uploadCategoryItem:
                uploadCategory(categoryList.get(info.position));
                adapter.notifyDataSetChanged();
        }

        return super.onContextItemSelected(item);
    }

    public List<String> itemList(String title) {

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

    public void uploadCategory(String categoryString) {
        if (dataBaseCategories.contains(categoryString)) {
            Toast.makeText(getApplicationContext(), "\"" + categoryString + "\" already in database"
                    , Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Starting upload: \"" + categoryString + "\""
                    , Toast.LENGTH_SHORT).show();
            List<String> itemList = itemList(categoryString);
            mCategoryList.child(categoryString).setValue(itemList.size());
            mCategories.child(categoryString).setValue(itemList);
            //return to home screen
            Intent intent = new Intent(MyCategories.this, ChooseActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }
}
