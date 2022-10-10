package com.example.fragmentssession;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class MySecondFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);

        // fragment logic goes here
        int numberOfItems = 10;
        ArrayList<String> data = new ArrayList<String>(numberOfItems);
        for (int i = 0; i < numberOfItems; i++) {
            data.add(String.format("List Item: %s", i));
        }

        if (getContext() == null) {
            return view;
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_list_item_1,
                data
        );

        final ListView listView = view.findViewById(R.id.myListView);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.changeFragment(
                        R.id.main_fragment_container,
                        ListItemFragment.newInstance(
                                String.valueOf(position),
                                String.valueOf(listView.getItemAtPosition(position))),
                        "List Item"
                );
            }
        });

        return view;
    }
}
