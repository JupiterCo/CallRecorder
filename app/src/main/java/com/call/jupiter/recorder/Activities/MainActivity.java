package com.call.jupiter.recorder.Activities;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.SearchView;

import com.call.jupiter.recorder.Fragments.HomepageFragment;
import com.call.jupiter.recorder.Fragments.RecordsFragment;
import com.call.jupiter.recorder.Helper.AppUtility;
import com.call.jupiter.recorder.Helper.Utility;
import com.call.jupiter.recorder.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static String RECORDFRAGMENTTAG = "Records";
    public static String HOMEPAGEFRAGMENTTAG = "Homepage";
    MenuItem item;
    boolean isMenuItemChangeReady = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        goToFragment(new HomepageFragment(), HOMEPAGEFRAGMENTTAG);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_homepage) {
            goToFragment(new HomepageFragment(), HOMEPAGEFRAGMENTTAG);
            invalidateOptionsMenu();
        } else if (id == R.id.nav_records) {
            goToFragment(new RecordsFragment(), RECORDFRAGMENTTAG);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                sendSearchTermsToFragment(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                sendSearchTermsToFragment(newText);
                return false;
            }
        });

        isMenuItemChangeReady = true;

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppUtility.goToOtherPermission(this, this, true);
    }

    private void sendSearchTermsToFragment(String searchQuery){
        RecordsFragment fragment = (RecordsFragment) getSupportFragmentManager().findFragmentById(R.id.FLHome);
        fragment.loadRecordsWithSearch(searchQuery);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_search).setVisible(false);

        return super.onPrepareOptionsMenu(menu);
    }

    public void goToFragment(android.support.v4.app.Fragment fragment, String TAG){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.FLHome, fragment, TAG).addToBackStack(null).commit();

        if(isMenuItemChangeReady){
            if(TAG.equals(RECORDFRAGMENTTAG)){
                item.setVisible(true);
            }else{
                item.setVisible(false);
            }
        }
    }
}
