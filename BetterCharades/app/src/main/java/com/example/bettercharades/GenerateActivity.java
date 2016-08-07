package com.example.bettercharades;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class GenerateActivity extends AppCompatActivity {

    Button generateCategoryButton;
    EditText generateUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate);

        generateCategoryButton = (Button) findViewById(R.id.generateCategoryButton);
        generateUrl = (EditText) findViewById(R.id.generateUrl);


        generateCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Generating Category...",
                        Toast.LENGTH_SHORT).show();
                String url = generateUrl.getText().toString();
//                url = "https://en.wikipedia.org/wiki/List_of_Pok%C3%A9mon";
                new generateCategory().execute(url);
            }
        });

    }

    private class generateCategory extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... urls) {
            String name = null;
            try {
                Document doc = Jsoup.connect(urls[0]).get();

                // Getting category name
                Elements heading = doc.getElementsByTag("h1");
                name = heading.get(0).text();
                Log.e("Category Name", name);
                assert (name != null);

                // Getting category items
                List<String> items = new LinkedList<>();

                Elements rows = doc.getElementsByTag("tr");
                for (Element row : rows) {
                    try {
                        String item = row.getElementsByTag("td").get(0).text();
                        Log.e("Item", row.toString());
                        item = item.replace("\n", "");

                        assert !item.equals("");
                        items.add(item);
                    } catch (Exception e) {
                        //do nothing
                    }

                }

                assert (!items.isEmpty());

                // Write to file
                try {
                    FileOutputStream fos = openFileOutput(name + ".category.txt", Context.MODE_PRIVATE);
                    for (String item : items) {
                        fos.write((item + "\n").getBytes());
                    }
                    fos.close();
                } catch (IOException e) {
                    //do nothing
                }

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Could not generate category from link",
                        Toast.LENGTH_SHORT).show();
                Log.e("JSOUP", "Could not generate category from link");
            }
            return name;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String name) {
            if (name != null) {
                Intent createIntent = new Intent(GenerateActivity.this, EditCategoryActivity.class);
                createIntent.putExtra("CATEGORY_NAME", name);
                startActivity(createIntent);
            }
        }
    }
}


