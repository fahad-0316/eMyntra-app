package com.example.emyntra;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

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
    private RelativeLayout adContainer;
    private ImageView btnCloseAd;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // --- NEW: Request Notification Permission (Android 13+) ---
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }

        // --- IMPROVED TOKEN LOGGING ---
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        System.out.println("!!! FCM ERROR: Fetching FCM registration token failed: " + task.getException());
                        return;
                    }
                    // Get new FCM registration token
                    String token = task.getResult();

                    // Print it clearly in the system output (easier to find)
                    System.out.println("!!! FCM TOKEN !!! " + token);
                    Log.d("FCM_TOKEN", token);

                    // Optional: Show a toast just so you know it worked
                    // Toast.makeText(MainActivity.this, "Token Generated (See Logcat)", Toast.LENGTH_SHORT).show();
                });

        // 1. Initialize AdMob SDK
        MobileAds.initialize(this, initializationStatus -> {});

        // 2. Setup Banner Ad & Close Button logic
        mAdView = findViewById(R.id.adView);
        adContainer = findViewById(R.id.ad_container);
        btnCloseAd = findViewById(R.id.btn_close_ad);

        AdRequest adRequest = new AdRequest.Builder().build();
        if (mAdView != null) {
            mAdView.loadAd(adRequest);
        }

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

        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }
    }

    private void loadInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
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

    public void openDrawer() {
        if (drawerLayout != null) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    public void setBottomNavigationVisibility(boolean isVisible) {
        int visibility = isVisible ? View.VISIBLE : View.GONE;
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(visibility);
        }
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

        if (adContainer != null) {
            adContainer.setVisibility(View.VISIBLE);
        }

        if (id == R.id.nav_support) {
            startActivity(new Intent(this, SupportChatActivity.class));
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        } else if (id == R.id.nav_logout) {
            logoutUser();
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }

        if (id == R.id.nav_home) {
            loadFragment(new HomeFragment());
            return true;
        } else if (id == R.id.nav_under999) {
            loadFragment(new Under999Fragment());
            return true;
        } else if (id == R.id.nav_luxury) {
            loadFragment(new LuxuryFragment());
            return true;
        } else if (id == R.id.nav_bag) {
            if (mInterstitialAd != null) {
                mInterstitialAd.show(MainActivity.this);
                mInterstitialAd = null;
                loadInterstitialAd();
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