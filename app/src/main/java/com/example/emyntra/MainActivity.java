package com.example.emyntra;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
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

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        BottomNavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;
    private SharedPreferences sharedPreferences;
    public static final String PREFS_NAME = "eMyntraPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // 1. Initialize Structure Views
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // 2. Setup Drawer Listener
        navigationView.setNavigationItemSelectedListener(this);

        // 3. Setup Bottom Nav Listener
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        // 4. Load Default Home Fragment on Startup
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }
    }

    /**
     * PUBLIC method accessed by HomeFragment to open the side drawer
     */
    public void openDrawer() {
        if (drawerLayout != null) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    /**
     * Helper to swap fragments in the main container
     */
    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.commit();
    }

    /**
     * Handles clicks on the Side Drawer and Bottom Bar
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        // --- Handle Side Drawer Clicks ---
        if (id == R.id.nav_my_orders) {
            Toast.makeText(this, "My Orders (coming soon!)", Toast.LENGTH_SHORT).show();
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        } else if (id == R.id.nav_settings) {
            Toast.makeText(this, "Settings (coming soon!)", Toast.LENGTH_SHORT).show();
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
            loadFragment(new Under999Fragment()); // This is now the QR Scanner
            return true;
        } else if (id == R.id.nav_luxury) {
            loadFragment(new LuxuryFragment()); // This is now the AI Chat
            return true;
        } else if (id == R.id.nav_bag) {
            loadFragment(new BagFragment());
            return true;
        }

        return false;
    }

    /**
     * Signs out of Firebase and clears local data
     */
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
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}