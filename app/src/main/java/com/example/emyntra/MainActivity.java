package com.example.emyntra;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

// AdMob Imports
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        BottomNavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;
    private SharedPreferences sharedPreferences;
    public static final String PREFS_NAME = "eMyntraPrefs";

    // Ad Variables
    private AdView mAdView;
    private RelativeLayout adContainer; // Container for Ad + Close Button
    private ImageView btnCloseAd;       // The X button
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Initialize AdMob SDK
        MobileAds.initialize(this, initializationStatus -> {});

        // 2. Setup Banner Ad & Close Button logic
        mAdView = findViewById(R.id.adView);
        adContainer = findViewById(R.id.ad_container);
        btnCloseAd = findViewById(R.id.btn_close_ad);

        // Load Banner Ad
        AdRequest adRequest = new AdRequest.Builder().build();
        if (mAdView != null) {
            mAdView.loadAd(adRequest);
        }

        // Handle the "X" button to hide the banner
        if (btnCloseAd != null) {
            btnCloseAd.setOnClickListener(v -> {
                if (adContainer != null) {
                    adContainer.setVisibility(View.GONE);
                }
            });
        }

        // 3. Pre-load Interstitial Ad
        loadInterstitialAd();

        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        navigationView.setNavigationItemSelectedListener(this);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        // Load Default Home Fragment
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }
    }

    /**
     * Loads the Interstitial Ad with the Test ID
     */
    private void loadInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        // Use Test Unit ID for Interstitial
        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        mInterstitialAd = null;
                    }
                });
    }

    /**
     * PUBLIC method used by HomeFragment to open the drawer
     */
    public void openDrawer() {
        if (drawerLayout != null) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    /**
     * Helper to show/hide bottom nav and banner.
     * Often used by fragments like AI Chat to get full-screen space.
     */
    public void setBottomNavigationVisibility(boolean isVisible) {
        int visibility = isVisible ? View.VISIBLE : View.GONE;
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(visibility);
        }
        // Also hide/show the ad container based on context
        if (adContainer != null) {
            adContainer.setVisibility(visibility);
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        // RE-SHOW THE AD CONTAINER whenever a new tab is selected
        if (adContainer != null) {
            adContainer.setVisibility(View.VISIBLE);
        }

        // --- Handle Side Drawer Menu Clicks ---
        if (id == R.id.nav_support) {
            // NEW: Open Support Chat
            startActivity(new Intent(this, SupportChatActivity.class));
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        } else if (id == R.id.nav_logout) {
            logoutUser();
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }

        // --- Handle Bottom Navigation Bar Clicks ---
        if (id == R.id.nav_home) {
            loadFragment(new HomeFragment());
            return true;
        } else if (id == R.id.nav_under999) {
            loadFragment(new Under999Fragment()); // QR Scanner Page
            return true;
        } else if (id == R.id.nav_luxury) {
            loadFragment(new LuxuryFragment()); // AI Chat Page
            return true;
        } else if (id == R.id.nav_bag) {
            // Show Interstitial Ad before loading the Bag
            if (mInterstitialAd != null) {
                mInterstitialAd.show(MainActivity.this);
                mInterstitialAd = null;
                loadInterstitialAd(); // Load next ad for future use
            }
            loadFragment(new BagFragment());
            return true;
        }

        return false;
    }

    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}