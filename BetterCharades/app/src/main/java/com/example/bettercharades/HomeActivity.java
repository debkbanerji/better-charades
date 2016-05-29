package com.example.bettercharades;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.FileOutputStream;


public class HomeActivity extends AppCompatActivity {

    Button playButton;
    Button downloadButton;
    Button myCategoriesButton;
    Button helpButton;
    Button settingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        playButton = (Button) findViewById(R.id.playButton);
        downloadButton = (Button) findViewById(R.id.downloadButton);
        helpButton = (Button) findViewById(R.id.helpButton);
        settingsButton = (Button) findViewById(R.id.settingsButton);
        myCategoriesButton = (Button) findViewById(R.id.myCategoriesButton);


        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(HomeActivity.this, ChooseCategoryActivity.class);
                startActivity(playIntent);
            }
        });

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent downloadIntent = new Intent(HomeActivity.this, DownloadCategoryActivity.class);
                startActivity(downloadIntent);
            }
        });

        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent helpIntent = new Intent(HomeActivity.this, HelpActivity.class);
                startActivity(helpIntent);
            }
        });
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
            }
        });

        myCategoriesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myCategoriesIntent = new Intent(HomeActivity.this, MyCategories.class);
                startActivity(myCategoriesIntent);
            }
        });
//                generateTestCategory();
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
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_choose, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

}
