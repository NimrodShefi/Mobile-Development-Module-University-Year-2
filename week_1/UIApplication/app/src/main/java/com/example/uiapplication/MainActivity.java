package com.example.uiapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    /*
    There is no main method here, but onCreate can be considered the same thing, generally speaking
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        Get a reference to a part of the UI, such as a specific button
        When the button is tapped on, show a message to the user
         */
        AppCompatButton button_a = findViewById(R.id.button_a);
        button_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // code that will run when button is clicked on

                Toast toast = Toast.makeText(getApplicationContext(), R.string.button_a_clicked_toast, Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        /*
        get button_b, which will get the application to swap to MainActivity2
         */
        AppCompatButton button_b = findViewById(R.id.button_b);
        button_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create Intent message to tell Android to open MainActivity2

                Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d("ACTIVITYEVENT", "onPause() was called");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("ACTIVITYEVENT", "onResume() was called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d("ACTIVITYEVENT", "onDestroy() was called");
    }
}
