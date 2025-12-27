package com.example.emyntra;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ShowDateActivity extends AppCompatActivity {

    private TextView tvSelectedDate;
    private SharedPreferences sharedPreferences;

    // Use the same keys to retrieve data
    public static final String PREFS_NAME = "eMyntraPrefs";
    public static final String KEY_SELECTED_DATE = "selected_date";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_date);

        // Set a title for the activity
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Selected Date");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        tvSelectedDate = findViewById(R.id.tv_selected_date);
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Retrieve the date from SharedPreferences and display it
        String selectedDate = sharedPreferences.getString(KEY_SELECTED_DATE, "No date selected");
        tvSelectedDate.setText(selectedDate);
    }

    // Handle the back arrow in the toolbar
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}