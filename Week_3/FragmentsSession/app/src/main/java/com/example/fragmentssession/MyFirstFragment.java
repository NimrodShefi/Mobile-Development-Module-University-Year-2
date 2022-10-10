package com.example.fragmentssession;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class MyFirstFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);

        // fragment logic goes here
        /*
            Grab the button
            Set an OnClick listener
            Swap out the fragment
         */
        AppCompatButton button = view.findViewById(R.id.my_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                FragmentManager fragmentManager = getParentFragmentManager(); // this will give us access to the fragment above it
                fragmentManager.beginTransaction()
                        .replace(R.id.main_fragment_container, new MySecondFragment())
                        .addToBackStack("second fragment") // this allows to go back after clicking on the button using the back button of Android
                        .commit();
                 */
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.changeFragment(R.id.main_fragment_container, new MySecondFragment(), "second fragment"); // by using this instead of the commented out code above, we are able to change any fragment with what we want
            }
        });


        return view;
    }
}
