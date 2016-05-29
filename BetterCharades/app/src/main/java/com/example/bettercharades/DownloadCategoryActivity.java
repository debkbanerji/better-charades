package com.example.bettercharades;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DownloadCategoryActivity extends AppCompatActivity {

    DatabaseReference mRootReef;
    TextView uploadHelpText;
    Set<String> dataBaseCategories;
    ListView lv;
    DatabaseReference mCategoryListRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_category);

        uploadHelpText = (TextView) findViewById(R.id.downloadInfoText);
        lv = (ListView) findViewById(R.id.downloadCategoryList);

        String[] connectingText = new String[]{"Connecting to database..."};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1, connectingText);
        lv.setAdapter(adapter);

        mRootReef = FirebaseDatabase.getInstance().getReference();
        mCategoryListRef = mRootReef.child("categoryList");
        mCategoryListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> dataMap = (Map<String, String>) dataSnapshot.getValue();
                dataBaseCategories = (dataMap.keySet());
                String[] categories = (String[]) dataBaseCategories.toArray(new String[0]);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(),
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1, categories);
                lv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //do nothing
            }
        });


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                String categoryString = (String) parent.getItemAtPosition(position);
                if (!categoryString.equals("Connecting to database...")) {
                    downloadCategory(categoryString);
                    //return to home screen
                    Intent intent = new Intent(DownloadCategoryActivity.this, ChooseActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });
    }

    public void downloadCategory(String title) {
        DatabaseReference categoryReference = mRootReef.child("categories").child(title);
        final String category = title;

        categoryReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> items = (List<String>) dataSnapshot.getValue();
                Log.e("Found items", items.toString());
                Toast.makeText(getApplicationContext(), "Starting download: \"" + category + "\""
                        , Toast.LENGTH_SHORT).show();
                try {
                    FileOutputStream fos = openFileOutput(category + ".category.txt", Context.MODE_PRIVATE);
                    for (String item : items) {
                        fos.write((item + "\n").getBytes());
                    }
                    fos.close();
                    Toast.makeText(getApplicationContext(), "Finished downloading \"" + category + "\""
                            , Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    //do nothing
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
