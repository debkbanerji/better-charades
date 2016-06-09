package com.example.bettercharades;

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
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyCategories extends AppCompatActivity {

    private ListView categoryListView;
    private DatabaseReference mRootReef;
    private DatabaseReference mCategories;
    private DatabaseReference mCategoryList;
    private Set dataBaseCategories;
    private List<String> categoryList;
    private ArrayAdapter<String> adapter;
    private Button createButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_categories);

        categoryListView = (ListView) findViewById(R.id.myCategoriesList);
        dataBaseCategories = new HashSet();

        createButton = (Button) findViewById(R.id.createButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createIntent = new Intent(MyCategories.this, CreateCategoryActivity.class);
                startActivity(createIntent);
            }
        });


        String[] fileList = fileList();
        categoryList = new LinkedList<>();

        String rgx = "(.+).category.txt";
        for (int i = 0; i < fileList.length; i++) {
            Matcher m = Pattern.compile(rgx).matcher(fileList[i]);
            if (m.find()) {
                categoryList.add(m.group(1));
            }
        }

//        if (categoryList.isEmpty()) {
//            categoryList.add("No categories available\nCreate or download categories to start playing");
//        }

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1, categoryList);

        categoryListView = (ListView) findViewById(R.id.myCategoriesList);
        categoryListView.setAdapter(adapter);

        registerForContextMenu(categoryListView);

        categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                parent.showContextMenuForChild(view);
//                String categoryString = (String) parent.getItemAtPosition(position);
//                uploadCategory(categoryString);
            }
        });


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


        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        int itemID = info.position;
        menu.setHeaderTitle(categoryList.get(itemID));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.playCategoryItem:
                playCategory(categoryList.get(info.position));
                adapter.notifyDataSetChanged();
                break;
            case R.id.uploadCategoryItem:
                uploadCategory(categoryList.get(info.position));
                adapter.notifyDataSetChanged();
                break;
            case R.id.deleteCategoryItem:
                deleteCategory(categoryList.get(info.position));
                adapter.notifyDataSetChanged();
                break;
            case R.id.editCategoryItem:
                editCategory(categoryList.get(info.position));
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
        Collections.sort(result);
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
//            //return to home screen
//            Intent intent = new Intent(MyCategories.this, HomeActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
        }
    }

    public void deleteCategory(String categoryString) {

        Toast.makeText(getApplicationContext(), "Deleting \"" + categoryString + "\""
                , Toast.LENGTH_SHORT).show();
        File dir = getFilesDir();
        File file = new File(dir, categoryString + ".category.txt");
        boolean deleted = file.delete();
        deleted = categoryList.remove(categoryString);
//        Log.e("File deleted", Boolean.toString(deleted));
//        Intent intent = new Intent(MyCategories.this, HomeActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
    }

    public void editCategory(String categoryString) {
        Intent editIntent = new Intent(MyCategories.this, EditCategoryActivity.class);
        editIntent.putExtra("CATEGORY_NAME", categoryString);
        startActivity(editIntent);
    }

    public void playCategory(String categoryString) {
        boolean soundOn;
        int timeIndex;
        int time;
        boolean invertTilt;
        try {
            FileInputStream fis = openFileInput("settings.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

            timeIndex = Integer.parseInt(reader.readLine());
            soundOn = Boolean.parseBoolean(reader.readLine());
            invertTilt = Boolean.parseBoolean(reader.readLine());

        } catch (Exception e) {
//            Log.e("Settings error", e.getMessage());
            timeIndex = 0;
            soundOn = true;
            invertTilt = false;
        }

        if (timeIndex == 0) {
            time = 30100;
        } else if (timeIndex == 1) {
            time = 60100;
        } else if (timeIndex == 2) {
            time = 90100;
        } else {
            time = 120100;
        }

        Intent intent = new Intent(MyCategories.this, PlayActivity.class);
        intent.putExtra("CATEGORY", categoryString);
        intent.putExtra("SOUND_ON", soundOn);
        intent.putExtra("TIME", time);
        if (invertTilt) {
            intent.putExtra("TILT_FACTOR", -1);

        } else {
            intent.putExtra("TILT_FACTOR", 1);
        }
        startActivity(intent);
    }
}
