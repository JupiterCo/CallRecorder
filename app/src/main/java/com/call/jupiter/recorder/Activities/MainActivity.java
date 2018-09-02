package com.call.jupiter.recorder.Activities;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.SearchView;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.call.jupiter.recorder.Fragments.HomepageFragment;
import com.call.jupiter.recorder.Fragments.RecordsFragment;
import com.call.jupiter.recorder.Helper.AppUtility;
import com.call.jupiter.recorder.Helper.Utility;
import com.call.jupiter.recorder.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BillingProcessor.IBillingHandler {
    public static String RECORDFRAGMENTTAG = "Records";
    public static String HOMEPAGEFRAGMENTTAG = "Homepage";
    public static String TAG = "Kontrol";
    MenuItem item_search, item_remove_ads;
    boolean isMenuItemChangeReady = false, isReadyToPurchase = false;
    BillingProcessor bp;
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

        bp = new BillingProcessor(this, getString(R.string.google_play_license_key), this);

        goToFragment(new HomepageFragment(), HOMEPAGEFRAGMENTTAG);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_homepage) {
            goToFragment(new HomepageFragment(), HOMEPAGEFRAGMENTTAG);
            //invalidateOptionsMenu();
        } else if (id == R.id.nav_records) {
            goToFragment(new RecordsFragment(), RECORDFRAGMENTTAG);
        }else if(id == R.id.nav_email){
            goToMail();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        item_search = menu.findItem(R.id.action_search);
        item_remove_ads = menu.findItem(R.id.action_remove_ads);

        SearchView searchView = (SearchView) item_search.getActionView();
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

        item_remove_ads.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (isReadyToPurchase){
                    bp.purchase(MainActivity.this, getString(R.string.remove_ads_product_id));
                }
                return true;
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
        menu.findItem(R.id.action_remove_ads).setVisible(true);

        return super.onPrepareOptionsMenu(menu);
    }

    public void goToFragment(android.support.v4.app.Fragment fragment, String TAG){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.FLHome, fragment, TAG).addToBackStack(null).commit();

        if(isMenuItemChangeReady){
            if(TAG.equals(RECORDFRAGMENTTAG)){
                item_search.setVisible(true);
                item_remove_ads.setVisible(false);
            }else{
                item_search.setVisible(false);
                item_remove_ads.setVisible(true);
            }
        }
    }

    private void goToMail(){
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + getString(R.string.email)));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "About Call Recorder: ");

        startActivity(Intent.createChooser(emailIntent, "Chooser Title"));
    }

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
        Log.d(TAG, "onProductPurchased: ");
    }

    @Override
    public void onPurchaseHistoryRestored() {
        Log.d(TAG, "onPurchaseHistoryRestored: ");
    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {
        Log.d(TAG, "onBillingError: " + String.valueOf(errorCode));
    }

    @Override
    public void onBillingInitialized() {
        isReadyToPurchase = true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
