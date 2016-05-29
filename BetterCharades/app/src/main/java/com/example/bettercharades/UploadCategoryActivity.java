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
import java.util.HashMap;
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
                }
                 else {
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
        });
//        generateTestCategory();
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
//        Log.e("Questions", result.toString());
//        Log.e("Questions", Integer.toString(result.size()));
        return result;
    }

    public void generateTestCategory() {


        //test code
        //use this to create categories easily for testing
        try {
            String title = "Taylor Swift Music";
            String result = "22\n" +
                    "A Perfectly Good Heart\n" +
                    "A Place In This World\n" +
                    "All Too Well\n" +
                    "All You Had To Do Was Stay\n" +
                    "Back to December\n" +
                    "Bad Blood\n" +
                    "Beautiful Eyes\n" +
                    "Begin Again\n" +
                    "Better Than Revenge\n" +
                    "Blank Space\n" +
                    "Breathe\n" +
                    "Change\n" +
                    "Christmas Must Be Something More\n" +
                    "Christmases When You Were Mine\n" +
                    "Clean\n" +
                    "Cold As You\n" +
                    "Come Back... Be Here\n" +
                    "Come in With the Rain\n" +
                    "Crazier\n" +
                    "Dear John\n" +
                    "Enchanted\n" +
                    "Eyes Open\n" +
                    "Fearless\n" +
                    "Fifteen\n" +
                    "Forever & Always\n" +
                    "Girl At Home\n" +
                    "Haunted\n" +
                    "Hey Stephen\n" +
                    "Holy Ground\n" +
                    "How You Get the Girl\n" +
                    "I Almost Do\n" +
                    "I Heart Question Mark\n" +
                    "I Knew You Were Trouble\n" +
                    "I Know Places\n" +
                    "I Wish You Would\n" +
                    "I'd Lie\n" +
                    "If This Was a Movie\n" +
                    "I'm Only Me When I'm with You\n" +
                    "Innocent\n" +
                    "Invisible\n" +
                    "Jump Then Fall\n" +
                    "Last Kiss\n" +
                    "Long Live\n" +
                    "Love Story\n" +
                    "Mary's Song (Oh my, my, my)\n" +
                    "Mean\n" +
                    "Mine\n" +
                    "Never Grow Up\n" +
                    "New Romantics\n" +
                    "Our Song\n" +
                    "Ours\n" +
                    "Out of the Woods\n" +
                    "Permanent Marker\n" +
                    "Picture to Burn\n" +
                    "Red\n" +
                    "Ronan\n" +
                    "Sad Beautiful Tragic\n" +
                    "Safe & Sound\n" +
                    "Shake It Off\n" +
                    "Should've Said No\n" +
                    "Sparks Fly\n" +
                    "Speak Now\n" +
                    "Starlight\n" +
                    "State of Grace\n" +
                    "Stay Beautiful\n" +
                    "Stay Stay Stay\n" +
                    "Style\n" +
                    "Superman\n" +
                    "Superstar\n" +
                    "Teardrops on My Guitar\n" +
                    "Tell Me Why\n" +
                    "The Best Day\n" +
                    "The Lucky One\n" +
                    "The Moment I Knew\n" +
                    "The Other Side Of The Door\n" +
                    "The Outside\n" +
                    "The Story of Us\n" +
                    "The Way I Loved You\n" +
                    "This Love\n" +
                    "Tied Together with a Smile\n" +
                    "Tim McGraw\n" +
                    "Today Was a Fairytale\n" +
                    "Treacherous\n" +
                    "Untouchable\n" +
                    "We Are Never Ever Getting Back Together\n" +
                    "Welcome To New York\n" +
                    "White Horse\n" +
                    "Wildest Dreams\n" +
                    "Wonderland\n" +
                    "You Are In Love\n" +
                    "You Belong With Me\n" +
                    "You're Not Sorry\n" +
                    "Everything Has Changed\n" +
                    "The Last Time";
            FileOutputStream fos = openFileOutput(title + ".category.txt", Context.MODE_PRIVATE);
            fos.write(result.getBytes());
            fos.close();
        } catch (Exception e) {
            Log.e("File I/O error", e.getMessage());
        }
    }
}
