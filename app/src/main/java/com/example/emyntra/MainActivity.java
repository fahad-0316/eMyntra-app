package com.example.emyntra;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;
import java.util.Locale;

// Implement both listener interfaces
public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        BottomNavigationView.OnNavigationItemSelectedListener {

    // --- Section 1: Drawer and Top Bar ---
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private LinearLayout llAddDate;

    // --- Section 2: Search Bar ---
    private EditText etSearch;
    private ImageView ivSearchButton;

    // --- Section 3: Fragment Tabs ---
    private LinearLayout llTabs; // The whole tab bar
    private TextView tvTabAll, tvTabMen, tvTabWomen, tvTabKids;

    // --- Section 5: Bottom Navigation ---
    private BottomNavigationView bottomNavigationView;

    // --- SharedPreferences (for Date and Logout) ---
    private SharedPreferences sharedPreferences;
    public static final String PREFS_NAME = "eMyntraPrefs";
    public static final String KEY_SELECTED_DATE = "selected_date";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Run setup methods for all sections
        setupToolbarAndDrawer();
        setupAddDateButton();
        setupSearchBar();
        setupTabs();
        setupBottomNavigation();

        // Load default "Home" state
        if (savedInstanceState == null) {
            loadFragment(new AllFragment());
            selectTab(tvTabAll);
            showProductTabs(true); // Show tabs on startup
        }
    }

    /**
     * SECTION 1: Initializes the Toolbar and Navigation Drawer
     */
    private void setupToolbarAndDrawer() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Remove default title to make space for "Add Date"
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        // Link Drawer and Toolbar
        drawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close
        );
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        // Set listener for drawer menu items
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);
    }

    /**
     * SECTION 1: Initializes the "Add Date" button
     */
    private void setupAddDateButton() {
        llAddDate = findViewById(R.id.ll_add_date);
        llAddDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }

    /**
     * SECTION 1: Shows the Date Picker dialog
     */
    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String selectedDate = String.format(Locale.getDefault(), "%02d-%02d-%d", dayOfMonth, month + 1, year);

                        // Save the date to SharedPreferences
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(KEY_SELECTED_DATE, selectedDate);
                        editor.apply();

                        // Navigate to the new screen
                        Intent intent = new Intent(MainActivity.this, ShowDateActivity.class);
                        startActivity(intent);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    /**
     * SECTION 2: Initializes the Search Bar listeners
     */
    private void setupSearchBar() {
        etSearch = findViewById(R.id.et_search);
        ivSearchButton = findViewById(R.id.iv_search_button);

        ivSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch();
            }
        });

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch();
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * SECTION 2: Checks search query and navigates or shows a toast
     */
    private void performSearch() {
        String query = etSearch.getText().toString().trim();

        if (query.isEmpty()) {
            showErrorToast("Please enter something to search");
        } else {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            intent.putExtra("SEARCH_QUERY", query);
            startActivity(intent);
        }
    }

    /**
     * SECTION 2: Shows a custom styled toast message.
     */
    private void showErrorToast(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.custom_toast_container));

        TextView text = (TextView) layout.findViewById(R.id.toast_text);
        text.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 100);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    /**
     * SECTION 3 & 4: Initializes the Tab click listeners and loads default fragment
     */
    private void setupTabs() {
        llTabs = findViewById(R.id.ll_tabs); // Get the whole LinearLayout
        tvTabAll = findViewById(R.id.tv_tab_all);
        tvTabMen = findViewById(R.id.tv_tab_men);
        tvTabWomen = findViewById(R.id.tv_tab_women);
        tvTabKids = findViewById(R.id.tv_tab_kids);

        tvTabAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new AllFragment());
                selectTab(tvTabAll);
            }
        });

        tvTabMen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new MenFragment());
                selectTab(tvTabMen);
            }
        });

        tvTabWomen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new WomenFragment());
                selectTab(tvTabWomen);
            }
        });

        tvTabKids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new KidsFragment());
                selectTab(tvTabKids);
            }
        });
    }

    /**
     * SECTION 4: Replaces the content of the fragment_container
     */
    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.commit();
    }

    /**
     * SECTION 3: Changes the text color of the selected tab
     */
    private void selectTab(TextView selectedTab) {
        int defaultColor = ContextCompat.getColor(this, R.color.colorTextPrimary);
        tvTabAll.setTextColor(defaultColor);
        tvTabMen.setTextColor(defaultColor);
        tvTabWomen.setTextColor(defaultColor);
        tvTabKids.setTextColor(defaultColor);

        // Use hardcoded color to avoid theme issues
        selectedTab.setTextColor(Color.parseColor("#E0007A"));
    }

    /**
     * SECTION 3: Shows or hides the product tab bar (All, Men, etc.)
     */
    private void showProductTabs(boolean show) {
        if (show) {
            llTabs.setVisibility(View.VISIBLE);
        } else {
            llTabs.setVisibility(View.GONE);
        }
    }

    /**
     * SECTION 5: Initializes the Bottom Navigation listener
     */
    private void setupBottomNavigation() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        // Cast 'this' to the specific listener to avoid ambiguity
        bottomNavigationView.setOnNavigationItemSelectedListener((BottomNavigationView.OnNavigationItemSelectedListener) this);
    }

    /**
     * Handles clicks for items in BOTH Navigation Views
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // --- Handle Top Navigation Drawer Clicks ---
        int drawerId = item.getItemId();
        if (drawerId == R.id.nav_my_orders) {
            Toast.makeText(this, "My Orders (coming soon!)", Toast.LENGTH_SHORT).show();
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        } else if (drawerId == R.id.nav_settings) {
            Toast.makeText(this, "Settings (coming soon!)", Toast.LENGTH_SHORT).show();
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        } else if (drawerId == R.id.nav_logout) {
            logoutUser();
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }

        // --- Handle Bottom Navigation Bar Clicks ---
        int bottomId = item.getItemId();
        if (bottomId == R.id.nav_home) {
            // Load the "All" fragment and show the tabs
            loadFragment(new AllFragment());
            selectTab(tvTabAll);
            showProductTabs(true);
            return true;
        } else if (bottomId == R.id.nav_under999) {
            // Load the "Under999" fragment and hide the tabs
            loadFragment(new Under999Fragment());
            showProductTabs(false);
            return true;
        } else if (bottomId == R.id.nav_luxury) {
            // Load the "Luxury" fragment and hide the tabs
            loadFragment(new LuxuryFragment());
            showProductTabs(false);
            return true;
        } else if (bottomId == R.id.nav_bag) {
            // Load the "Bag" fragment and hide the tabs
            loadFragment(new BagFragment());
            showProductTabs(false);
            return true;
        }

        return false;
    }

    /**
     * Logs out the user and returns to LoginActivity
     */
    private void logoutUser() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Clear all saved data
        editor.apply();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * Handles the Back button press. Closes the drawer if it's open.
     */
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}