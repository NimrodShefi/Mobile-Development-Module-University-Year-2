package com.example.intentssession;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent intent = getIntent(); // this grabs the object that was sent from MainActivity
        String receivedText = intent.getStringExtra(Intent.EXTRA_TEXT);
//        intent.getExtras().keySet(); // this method will produce all of the keys sent across which can be sued to retrieve all of the data without worrying about having the wrong data type, like on line 17

        if (receivedText != null){
            Toast.makeText(this, receivedText, Toast.LENGTH_SHORT).show();
        }
    }
}
