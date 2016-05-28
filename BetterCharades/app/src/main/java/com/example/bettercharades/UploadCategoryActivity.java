package com.example.bettercharades;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.FileOutputStream;

public class UploadCategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_category);





        //placeholder code
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
