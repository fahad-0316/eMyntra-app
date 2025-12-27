package com.example.emyntra;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Calendar;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private Toolbar toolbar;
    private LinearLayout llAddDate;
    private EditText etSearch;
    private ImageView ivSearchButton;
    private TextView tvTabAll, tvTabMen, tvTabWomen, tvTabKids;

    private SharedPreferences sharedPreferences;
    public static final String PREFS_NAME = "eMyntraPrefs";
    public static final String KEY_SELECTED_DATE = "selected_date";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Initialize features
        setupToolbar(view);
        setupAddDateButton(view);
        setupSearchBar(view);
        setupTabs(view);

        // Default Load "All" Tab
        loadChildFragment(new AllFragment());
        selectTab(tvTabAll);
    }

    private void setupToolbar(View view) {
        toolbar = view.findViewById(R.id.home_toolbar);
        // Setup Hamburger Icon
        toolbar.setNavigationIcon(R.drawable.ic_menu); // Ideally a menu icon, but reusing what you have or using standard
        // Note: Android default hamburger icon resource is '@android:drawable/ic_menu_sort_by_size' or similar,
        // but often 'app:navigationIcon' needs a drawable.
        // Let's use a standard workaround if you don't have a specific icon, or the camera one you used before.
        // For professional look, we assume you might have an ic_menu. If not, this uses standard camera/menu logic.
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);

        toolbar.setNavigationOnClickListener(v -> {
            // Call method in MainActivity to open drawer
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).openDrawer();
            }
        });
    }

    private void setupAddDateButton(View view) {
        llAddDate = view.findViewById(R.id.ll_add_date);
        llAddDate.setOnClickListener(v -> showDatePickerDialog());
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                (view, year1, month1, dayOfMonth) -> {
                    String selectedDate = String.format(Locale.getDefault(), "%02d-%02d-%d", dayOfMonth, month1 + 1, year1);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(KEY_SELECTED_DATE, selectedDate);
                    editor.apply();
                    Intent intent = new Intent(getActivity(), ShowDateActivity.class);
                    startActivity(intent);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void setupSearchBar(View view) {
        etSearch = view.findViewById(R.id.et_search);
        ivSearchButton = view.findViewById(R.id.iv_search_button);

        ivSearchButton.setOnClickListener(v -> performSearch());

        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch();
                return true;
            }
            return false;
        });
    }

    private void performSearch() {
        String query = etSearch.getText().toString().trim();
        if (query.isEmpty()) {
            Toast.makeText(getContext(), "Please enter something to search", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            intent.putExtra("SEARCH_QUERY", query);
            startActivity(intent);
        }
    }

    private void setupTabs(View view) {
        tvTabAll = view.findViewById(R.id.tv_tab_all);
        tvTabMen = view.findViewById(R.id.tv_tab_men);
        tvTabWomen = view.findViewById(R.id.tv_tab_women);
        tvTabKids = view.findViewById(R.id.tv_tab_kids);

        tvTabAll.setOnClickListener(v -> { loadChildFragment(new AllFragment()); selectTab(tvTabAll); });
        tvTabMen.setOnClickListener(v -> { loadChildFragment(new MenFragment()); selectTab(tvTabMen); });
        tvTabWomen.setOnClickListener(v -> { loadChildFragment(new WomenFragment()); selectTab(tvTabWomen); });
        tvTabKids.setOnClickListener(v -> { loadChildFragment(new KidsFragment()); selectTab(tvTabKids); });
    }

    // Loads fragment into the Home Fragment's internal container
    private void loadChildFragment(Fragment fragment) {
        // Use getChildFragmentManager() for fragments inside fragments
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.home_content_container, fragment);
        ft.commit();
    }

    private void selectTab(TextView selectedTab) {
        int defaultColor = ContextCompat.getColor(requireContext(), R.color.colorTextPrimary);
        tvTabAll.setTextColor(defaultColor);
        tvTabMen.setTextColor(defaultColor);
        tvTabWomen.setTextColor(defaultColor);
        tvTabKids.setTextColor(defaultColor);
        selectedTab.setTextColor(Color.parseColor("#E0007A"));
    }
}