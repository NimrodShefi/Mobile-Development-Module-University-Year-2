package com.example.fragmentsexercise;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* TODO(5)
        Place the fragment, MainFragment, in the container element you created in TO-DO(4)

        Hint, to achieve this, create a new FragmentManager object by calling
        getSupportFragmentManager() (as we are using the support library's version of the Fragment
        as the class in MainFragment.java). Begin a new fragment transaction and add a new
        MainFragmentObject to the transaction before committing the transaction. Take a look at the
        lecture slides for further details on this process.
        */
    }
}