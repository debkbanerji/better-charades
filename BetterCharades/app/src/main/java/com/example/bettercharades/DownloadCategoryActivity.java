package com.example.bettercharades;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DownloadCategoryActivity extends AppCompatActivity {

    DatabaseReference mRootReef;
    TextView downloadHelpText;
    Set<String> dataBaseCategories;
    ListView lv;
    DatabaseReference mCategoryListRef;
    List<String> categories;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_category);

        downloadHelpText = (TextView) findViewById(R.id.downloadInfoText);
        lv = (ListView) findViewById(R.id.downloadCategoryList);

//        categories = new ArrayList<String>();
//        {"Connecting to database..."};
//        adapter = new ArrayAdapter<String>(getBaseContext(),
//                android.R.layout.simple_list_item_1,
//                android.R.id.text1, categories);
//        lv.setAdapter(adapter);

        mRootReef = FirebaseDatabase.getInstance().getReference();
        mCategoryListRef = mRootReef.child("categoryList");
        mCategoryListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> dataMap = (Map<String, String>) dataSnapshot.getValue();
                if (dataMap != null) {
                    dataBaseCategories = (dataMap.keySet());
                    categories = new ArrayList(dataBaseCategories);
                    adapter = new ArrayAdapter<String>(getBaseContext(),
                            android.R.layout.simple_list_item_1,
                            android.R.id.text1, categories);
                    lv.setAdapter(adapter);
                    registerForContextMenu(lv);
                }
                downloadHelpText.setText(R.string.download_prompt);
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
//                if (!categoryString.equals("Connecting to database...")) {
                downloadCategory(categoryString);
                //return to home screen
//                    Intent intent = new Intent(DownloadCategoryActivity.this, HomeActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);
//                }
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_delete_database_category, menu);


        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        int itemID = info.position;
        menu.setHeaderTitle(categories.get(itemID));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.downloadFromDatabaseCategory:
                downloadCategory(categories.get(info.position));
                break;
            case R.id.deleteDatabaseCategory:
                String category = categories.get(info.position);
                categories.remove(info.position);
                adapter.notifyDataSetChanged();
                Log.e("Category:", category);
                DatabaseReference categoryReferenceInList = mCategoryListRef.child(category);
                DatabaseReference categoryReference = mRootReef.child("categories").child(category);
                categoryReference.setValue(null);
                categoryReferenceInList.setValue(null);
                Toast.makeText(getApplicationContext(), "Deleted \"" + category + "\" from database"
                        , Toast.LENGTH_SHORT).show();

        }

        return super.onContextItemSelected(item);
    }

    public void downloadCategory(String title) {
        final String category = title;
//        Toast.makeText(getApplicationContext(), "Starting download: \"" + category + "\""
//                , Toast.LENGTH_SHORT).show();
        DatabaseReference categoryReference = mRootReef.child("categories").child(title);


        categoryReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> items = (List<String>) dataSnapshot.getValue();
                try {
                    FileOutputStream fos = openFileOutput(category + ".category.txt", Context.MODE_PRIVATE);
                    for (String item : items) {
                        fos.write((item + "\n").getBytes());
                    }
                    fos.close();
                    Toast.makeText(getApplicationContext(), "Downloading \"" + category + "\""
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
