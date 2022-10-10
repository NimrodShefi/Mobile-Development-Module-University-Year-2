package com.example.fragmentssession;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, new MyFirstFragment())
                .commit();

    }

    public void changeFragment(int id, Fragment fragment, String addToBackStack){
        FragmentManager fragmentManager = getSupportFragmentManager(); // this will give us access to the fragment above it
        fragmentManager.beginTransaction()
                .replace(id, fragment)
                .addToBackStack(addToBackStack) // this allows to go back after clicking on the button using the back button of Android
                .commit();
    }
}
