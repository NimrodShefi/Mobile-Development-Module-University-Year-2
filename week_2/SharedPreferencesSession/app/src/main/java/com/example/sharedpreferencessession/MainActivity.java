package com.example.sharedpreferencessession;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("MY_DATASTORE", Context.MODE_PRIVATE); // this preference only works on this project, and can't be sued elsewhere
        doSharedPreferences();

        /*
            DO UI EXAMPLE
        */
        final AppCompatEditText textbox = findViewById(R.id.textbox);
//        Log.d("READ", String.valueOf(sharedPreferences.contains("textboxText")));
//        Log.d("READ", String.valueOf(sharedPreferences.getString("textboxText", "")));


        textbox.setText(sharedPreferences.getString("textboxText", ""));

        AppCompatButton submit = findViewById(R.id.submit_button); // the submit button works, but because of the function doSharedPreferences(), the data in sharedPreferences gets cleared every time
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textboxContents = textbox.getText().toString();

//                Log.d("SAVE", textboxContents);

                sharedPreferences.edit().putString("textboxText", textboxContents).apply();

//                Log.d("SAVE", String.valueOf(sharedPreferences.contains("textboxText")));
//                Log.d("SAVE", String.valueOf(sharedPreferences.getString("textboxText", "")));
            }
        });

    }

    private void doSharedPreferences() {
        /*
            ADDING DATA
        */

        SharedPreferences.Editor editor = sharedPreferences.edit(); // to edit data for sharedPreferences, the edit function is needed
        editor.putInt("some_number", 4);
        editor.putString("username", "user123");
        editor.putBoolean("first_time", false);


//        editor.commit(); // Consider using apply() instead; commit writes its data to persistent storage immediately, whereas apply will handle it in the background
        editor.apply(); // this may be better than commit if the data added is very large and you don't want the user to notice any slowness. However, commit may be needed if the data is needed very quickly

        /*
            REMOVING DATA
        */
        editor.remove("some_number"); // doesn't do anything without the apply/commit methods

        editor.putString("test", "tester"); // in Android, when apply/commit runs, the removal methods run before the put, so this test will be in the Log
        editor.clear(); // removes everything from sharedPreferences
        editor.apply();

        /*
            READING DATA
        */
        int i = sharedPreferences.getInt("some_number", -1); // the 2nd variable is the default number in case the value of the key given is not found
        Log.d("SP_INT_NUM", String.valueOf(i));

        // get everything in the SharedPreferences store
        Map<String, ?> map = sharedPreferences.getAll(); // ? is used because the type of Value is unknown, while the knowing that the Key is String
        for (Map.Entry<String, ?> e : map.entrySet()) {
            Log.d("sharedPreferences = " + e.getKey(), String.valueOf(e.getValue()));
        }
    }
}