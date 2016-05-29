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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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

public class UploadCategoryActivity extends AppCompatActivity {

    DatabaseReference mRootReef;
    TextView uploadHelpText;
    Set<String> dataBaseCategories;
    DatabaseReference mCategories;
    DatabaseReference mCategoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_category);

        dataBaseCategories = new HashSet<>(); //just in case
        String[] fileList = fileList();
        List<String> categoryList = new LinkedList<>();

        String rgx = "(.+).category.txt";
        for (int i = 0; i < fileList.length; i++) {
            Matcher m = Pattern.compile(rgx).matcher(fileList[i]);
            if (m.find()) {
                categoryList.add(m.group(1));
            }
        }

        uploadHelpText = (TextView) findViewById(R.id.uploadInfoText);

        if (categoryList.isEmpty()) {
            uploadHelpText.setText("No categories available\nCreate categories to upload or start playing");
        }

        String[] categories = (String[]) categoryList.toArray(new String[0]);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1, categories);

        ListView lv = (ListView) findViewById(R.id.uploadCategoryList);
        lv.setAdapter(adapter);

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


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                String categoryString = (String) parent.getItemAtPosition(position);

                uploadCategory(categoryString);
            }
        });
//        generateTestCategory();
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
            Intent intent = new Intent(UploadCategoryActivity.this, ChooseActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
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

    public void generateTestCategory() {


        //test code
        //use this to create categories easily for testing
        try {
            String title = "Disney Movies";
            String result = "Meet The Robinsons\n" +
                    "Bolt\n" +
                    "The Princess And The Frog\n" +
                    "Tangled\n" +
                    "The Pooh Winnie The Pooh\n" +
                    "Big Hero 6\n" +
                    "Frozen\n" +
                    "The Great Mouse Detective\n" +
                    "Oliver & Company\n" +
                    "The Little Mermaid\n" +
                    "The Rescuers Down Under\n" +
                    "Beauty And The Beast\n" +
                    "Aladdin\n" +
                    "The Lion King\n" +
                    "Pocahontas\n" +
                    "The Hunchback Of Notre Dame\n" +
                    "Hercules\n" +
                    "Mulan\n" +
                    "Tarzan\n" +
                    "Dinosaur\n" +
                    "The Emperor's New Groove\n" +
                    "Atlantis: The Lost Empire\n" +
                    "Lilo & Stitch\n" +
                    "Treasure Planet\n" +
                    "Brother Bear\n" +
                    "Home On The Range\n" +
                    "Chicken Little\n" +
                    "Snow White And The Seven Dwarfs\n" +
                    "Pinocchio\n" +
                    "Fantasia\n" +
                    "Dumbo\n" +
                    "Bambi\n" +
                    "Saludos Amigos\n" +
                    "The Three Caballeros\n" +
                    "Make Mine Music\n" +
                    "Fun And Fancy Free\n" +
                    "Melody Time\n" +
                    "The Adventures Of Ichabod And Mr. Toad\n" +
                    "Cinderella\n" +
                    "Wreck-It Ralph\n" +
                    "Alice In Wonderland\n" +
                    "Peter Pan\n" +
                    "Lady And The Tramp\n" +
                    "Sleeping Beauty\n" +
                    "One Hundred And One Dalmatians\n" +
                    "The Sword In The Stone\n" +
                    "The Jungle Book\n" +
                    "The Aristocats\n" +
                    "Robin Hood\n" +
                    "The Many Adventures Of Winnie The Pooh\n" +
                    "The Rescuers\n" +
                    "The Fox And The Hound\n" +
                    "The Black Cauldron\n" +
                    "Zootopia\n" +
                    "Gigantic";
            FileOutputStream fos = openFileOutput(title + ".category.txt", Context.MODE_PRIVATE);
            fos.write(result.getBytes());
            fos.close();
        } catch (Exception e) {
            Log.e("File I/O error", e.getMessage());
        }
    }
}
