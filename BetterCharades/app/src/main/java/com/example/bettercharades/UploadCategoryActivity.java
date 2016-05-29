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
        return result;
    }

    public void generateTestCategory() {


        //test code
        //use this to create categories easily for testing
        try {
            String title = "Dhamakedar Music";
            String result = "A Cruel Angel's Thesis\n" +
                    "A Man for All Seasons\n" +
                    "Afterlife\n" +
                    "Ain't No Mountain High Enough\n" +
                    "All I Can Do\n" +
                    "American Pie\n" +
                    "Auld Lang Syne\n" +
                    "Be OK\n" +
                    "Be Prepared \n" +
                    "Bleeding Love\n" +
                    "Blowin' in the Wind\n" +
                    "Bohemian Rhapsody\n" +
                    "Bonfire Heart\n" +
                    "Bottle It Up\n" +
                    "Brave\n" +
                    "Breathe Again\n" +
                    "Bridge over Troubled Water\n" +
                    "Budapest\n" +
                    "Cecilia\n" +
                    "Chances\n" +
                    "Colours Of The Wind\n" +
                    "Come and Get Your Love\n" +
                    "Come Home\n" +
                    "Could've Been Watching You\n" +
                    "Crystals\n" +
                    "Do You Want to Build a Snowman\n" +
                    "Don't Stop Believin'\n" +
                    "Drops of Jupiter\n" +
                    "Everybody\n" +
                    "Everybody's Got Somebody But Me\n" +
                    "Everything\n" +
                    "Eye Of the Tiger\n" +
                    "Few Days Down\n" +
                    "Five Hundred Miles\n" +
                    "Fix You\n" +
                    "Gimme Something Good\n" +
                    "Girls Chase Boys\n" +
                    "Gonna Get Over You\n" +
                    "Greatest Change\n" +
                    "Hallelujah\n" +
                    "Haven't Met You Yet\n" +
                    "Hello\n" +
                    "Here Comes the Sun\n" +
                    "Hey Jude\n" +
                    "Hey, Soul Sister\n" +
                    "Home\n" +
                    "Homeward Bound\n" +
                    "Hooked On A Feeling\n" +
                    "Hotel California\n" +
                    "How To Save A Life\n" +
                    "Human\n" +
                    "I Choose You\n" +
                    "I Don't Want to Change You\n" +
                    "I See Fire\n" +
                    "I Want You Back\n" +
                    "I Will Be\n" +
                    "I Won't Give Up\n" +
                    "I'll Make a Man Out of You\n" +
                    "I'm Gonna Be (500 Miles)\n" +
                    "I'm Ready\n" +
                    "I'm Yours\n" +
                    "Imagine\n" +
                    "In Other Words\n" +
                    "Into The West \n" +
                    "Intro\n" +
                    "Invincible\n" +
                    "It's a Beautiful Day\n" +
                    "It's Just You\n" +
                    "Just Say Yes\n" +
                    "Just the Way You Are\n" +
                    "Keep Singing\n" +
                    "King of Anything\n" +
                    "La Vie en rose\n" +
                    "Last Christmas\n" +
                    "Leaving on a Jet Plane\n" +
                    "Let Her Go\n" +
                    "Let It Be\n" +
                    "Let It Go\n" +
                    "Life's Too Short\n" +
                    "Light in the Dark \n" +
                    "Listen to the Man\n" +
                    "Little Talks\n" +
                    "Livin' La Vida Loca\n" +
                    "Livin' On A Prayer\n" +
                    "Locked Up\n" +
                    "Looking Forward to Looking Back\n" +
                    "Love Me Like the World Is Ending\n" +
                    "Love Song\n" +
                    "Marry You\n" +
                    "Merrimack River\n" +
                    "Never Gonna Give You Up\n" +
                    "New Soul\n" +
                    "Nineteen You and Me\n" +
                    "No Surprise\n" +
                    "One Night Town\n" +
                    "Open Arms\n" +
                    "Over The Rainbow\n" +
                    "Parachute\n" +
                    "Paradise Awaits\n" +
                    "Photograph\n" +
                    "Pocket Philosopher\n" +
                    "Pompeii\n" +
                    "Rolling in the Deep\n" +
                    "Say Something\n" +
                    "Say The Word\n" +
                    "Secrets\n" +
                    "Separate Ways (Worlds Apart)\n" +
                    "September\n" +
                    "Service and Sacrifice\n" +
                    "Set Fire To The Rain\n" +
                    "Shake It Out\n" +
                    "She Used To Be Mine\n" +
                    "She Will Be Loved\n" +
                    "Skyfall\n" +
                    "Slice\n" +
                    "Slow\n" +
                    "Somebody That I Used To Know\n" +
                    "Someone Like You\n" +
                    "Something That I Want \n" +
                    "Somewhere Only We Know\n" +
                    "Son Of Man \n" +
                    "Stand by Me\n" +
                    "Stay With Me\n" +
                    "Stop And Stare\n" +
                    "Strangers Like Me\n" +
                    "Sugar\n" +
                    "Sway\n" +
                    "Sweet Serendipity\n" +
                    "Swept Away\n" +
                    "Tell Her About It\n" +
                    "Tenerife Sea\n" +
                    "Tha Mo Ghaol Air ird A' Chuain\n" +
                    "That's How You Know\n" +
                    "The Circle of Life\n" +
                    "The End\n" +
                    "The Heady Feeling of Freedom\n" +
                    "The Last Goodbye\n" +
                    "The Longest Time\n" +
                    "The Parting Glass\n" +
                    "The Scientist\n" +
                    "The Show\n" +
                    "The Sound of Silence\n" +
                    "The Strawberry Blonde\n" +
                    "The Tides of Destiny\n" +
                    "The Way I Am\n" +
                    "The Whole Of The Moon\n" +
                    "The Wind\n" +
                    "Thinking Out Loud\n" +
                    "Time\n" +
                    "Time in a Bottle\n" +
                    "Time Machine\n" +
                    "Time Travel\n" +
                    "To Love Somebody\n" +
                    "Touch The Sky\n" +
                    "Uncharted\n" +
                    "Uptown Girl\n" +
                    "VCR\n" +
                    "Viva La Vida\n" +
                    "Waiting for an Invitation\n" +
                    "Wanted Dead Or Alive\n" +
                    "We Didn't Start the Fire\n" +
                    "What Kind of Pokemon Are You\n" +
                    "When I Was Your Man\n" +
                    "When Will My Life Begin\n" +
                    "Why Can't We Be Friends\n" +
                    "Winter Song\n" +
                    "Won't Go Home Without You\n" +
                    "Writing's On The Wall\n" +
                    "Yellow\n" +
                    "Yesterday\n" +
                    "You and I\n" +
                    "You Found Me\n" +
                    "You Know My Name\n" +
                    "You'll Be In My Heart\n";
            FileOutputStream fos = openFileOutput(title + ".category.txt", Context.MODE_PRIVATE);
            fos.write(result.getBytes());
            fos.close();
        } catch (Exception e) {
            Log.e("File I/O error", e.getMessage());
        }
    }
}
