package com.example.fragmentsexercise;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

/* TODO(2)
Review the class below along with the Fragments slide deck to determine its components.
Take a look at the documentation for onCreateView at: https://developer.android.com/reference/androidx/fragment/app/Fragment#onCreateView(android.view.LayoutInflater,%20android.view.ViewGroup,%20android.os.Bundle)
and compare the differences between an activity's onCreate() method in terms of
parameters.

This method performs a similar function to the onCreateViewHolder method in the custom RecyclerView
view adapter in the Lists exercise.
*/
public class MainFragment extends Fragment {

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        // Perform any additional operations here

        return v;
    }

}
