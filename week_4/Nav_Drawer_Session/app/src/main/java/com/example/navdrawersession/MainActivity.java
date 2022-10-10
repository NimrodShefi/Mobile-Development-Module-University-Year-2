package com.example.navdrawersession;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // grab the toolbar and set it to be the activity's app bar (action bar)
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // grab the drawer layout
        // add burger icon
        // set up event for opening and closing
        drawerLayout = findViewById(R.id.nav_drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.nav_Drawer_open,
                R.string.nav_drawer_closed
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_panel);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().performIdentifierAction(R.id.menu_nav_a, 0);

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){ // if nav bar is open:
            drawerLayout.closeDrawer(GravityCompat.START); // close it
        } else {
            super.onBackPressed(); // continue like supposed to with the method when the back arrow is pressed
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_nav_a) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new FragmentA())
                    .commit();
            Toast.makeText(this, "Fragment A was clicked on", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.menu_nav_b) {
            Toast.makeText(this, "Fragment B was clicked on", Toast.LENGTH_SHORT).show();
        }
        drawerLayout.closeDrawer(GravityCompat.START); // automatically closes the nav bar after being used

        return false;
    }
}
