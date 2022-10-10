package com.example.intentssession;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        1. grab the 'Open' button
        2. Set an onClick listener
        3. Build an intent message object
        4. Open MainActivity2
         */

        AppCompatButton openActivityButton = findViewById(R.id.open_same_app);
        openActivityButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), MainActivity2.class);

                startActivity(intent);
            }
        }); // alternatively use openActivityButton.setOnClickListener(this) and have the activity handle the event


        AppCompatButton openBrowserButton = findViewById(R.id.open_browser);
        openBrowserButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                // Instruct the intent to open a browser
                intent.setAction(Intent.ACTION_VIEW);
                // Give it a url (web page) to open
                intent.setData(Uri.parse("https://www.cardiff.ac.uk"));
                startActivity(intent);
            }
        });

        AppCompatButton shareButton = findViewById(R.id.share);
        shareButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                // Instruct the intent that we want to send some data somewhere
                intent.setAction(Intent.ACTION_SEND);
                // add the data we want to send
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "Some text to send somewhere");
                intent.putExtra(Intent.EXTRA_EMAIL, "test@test.com");

                startActivity(intent);

                // to see how you make the app receive data look at AndroidManifest.xml
            }
        });
    }
}
