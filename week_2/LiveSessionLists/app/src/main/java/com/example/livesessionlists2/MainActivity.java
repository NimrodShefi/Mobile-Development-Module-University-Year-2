package com.example.livesessionlists2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.example_list_view);
//        doSimpleExample();

        // grab the data to show
        ArrayList<Book> listOfBooks = new ArrayList<Book>(){{
            add(new Book("Some Title", "Some Author"));
            add(new Book("Some other title", "some other author"));
        }};

        // get the custom adapter
        MyCustomAdapter myCustomAdapter = new MyCustomAdapter(
                this,
                R.layout.my_custom_list_row2,
                listOfBooks
        );

        // hook up the custom adapter to ListView
        listView.setAdapter(myCustomAdapter);

        // create events for when clicking on one of the items in the lists
        listView.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(
                this,
                String.format("You tapped on %s: %s", String.valueOf(position), listView.getItemAtPosition(position)),
                Toast.LENGTH_SHORT
        ).show();
    }

    private void doSimpleExample(){
        // get some data
        int numberOfItems = 20;
        ArrayList<String> listOfThings = new ArrayList<>(numberOfItems);
        for (int i = 0; i < numberOfItems; i++) {
            listOfThings.add(String.format("List item %s", i));
        }

        // create an adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                R.layout.my_custom_list_row,
                R.id.row_text,
                listOfThings
        );

        // attach the adapter to the ListView
        listView.setAdapter(adapter);
    }
}