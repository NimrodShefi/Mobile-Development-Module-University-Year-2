package com.example.toolbarssession;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); // to get this to work, the styles.xml file has to be changed from DarkActionBar to NoActionBar

        // get fragment manager
        // start fragment transaction
        // replace the container with the fragment
        FragmentManager fragmentManager = getSupportFragmentManager(); // this uses the androidx version of fragment manager
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, new MyFragmentToolbar())
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.settings:
                Toast.makeText(this, "Settings was selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_doSomethingElse:
                Toast.makeText(this, "Do something else was selected", Toast.LENGTH_SHORT).show();
                return true;
                // usually it is break; instead of return, but this method wants to return true at the end
        }

        return super.onOptionsItemSelected(item);
    }
}
