package com.example.emyntra;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    private EditText etEmail, etUsername, etPassword;
    private Button btnSignup;
    private TextView tvLogin;

    private SharedPreferences sharedPreferences;
    public static final String PREFS_NAME = "eMyntraPrefs";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";

    // Flags to track if current inputs are valid
    private boolean isEmailValid = false;
    private boolean isUsernameValid = false;
    private boolean isPasswordValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Initialize views
        etEmail = findViewById(R.id.etEmail);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnSignup = findViewById(R.id.btnSignup);
        tvLogin = findViewById(R.id.tvLogin);

        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // --- Setup Real-time Validation Listeners ---
        setupValidationListeners();

        // Signup button click listener
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSignup();
            }
        });

        // Login text click listener
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupValidationListeners() {
        // 1. Email Listener
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString().trim();
                // Rule: Must contain @ and .
                if (!input.isEmpty() && input.contains("@") && input.contains(".")) {
                    isEmailValid = true;
                    setValidationIcon(etEmail, true);
                } else {
                    isEmailValid = false;
                    // Only show error icon if user has typed something
                    if (input.length() > 0) setValidationIcon(etEmail, false);
                    else clearValidationIcon(etEmail);
                }
            }
        });

        // 2. Username Listener
        etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString().trim();
                // Rule: Min 4, Max 25 chars
                if (input.length() >= 4 && input.length() <= 25) {
                    isUsernameValid = true;
                    setValidationIcon(etUsername, true);
                } else {
                    isUsernameValid = false;
                    if (input.length() > 0) setValidationIcon(etUsername, false);
                    else clearValidationIcon(etUsername);
                }
            }
        });

        // 3. Password Listener
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString().trim();
                // Rule: Min 4, Max 16 chars AND at least one special character
                // Regex for special char: [^a-zA-Z0-9] means anything NOT a letter or number
                boolean hasSpecialChar = Pattern.compile("[^a-zA-Z0-9]").matcher(input).find();

                if (input.length() >= 4 && input.length() <= 16 && hasSpecialChar) {
                    isPasswordValid = true;
                    setValidationIcon(etPassword, true);
                } else {
                    isPasswordValid = false;
                    if (input.length() > 0) setValidationIcon(etPassword, false);
                    else clearValidationIcon(etPassword);
                }
            }
        });
    }

    /**
     * Helper to set the icon on the right side of the EditText
     */
    private void setValidationIcon(EditText editText, boolean isValid) {
        if (isValid) {
            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_circle, 0);
        } else {
            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_error_circle, 0);
        }
    }

    private void clearValidationIcon(EditText editText) {
        editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
    }

    private void attemptSignup() {
        String email = etEmail.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // 1. Check for Empty Fields First
        if (email.isEmpty()) {
            showErrorToast("Please enter your Email");
            showButtonErrorState(btnSignup);
            return;
        }

        if (username.isEmpty()) {
            showErrorToast("Please enter a Username");
            showButtonErrorState(btnSignup);
            return;
        }

        if (password.isEmpty()) {
            showErrorToast("Please enter a Password");
            showButtonErrorState(btnSignup);
            return;
        }

        // 2. Check Specific Validation Rules (if not empty but still invalid)
        if (!isEmailValid) {
            showErrorToast("Invalid Email: Must include '@' and '.'");
            showButtonErrorState(btnSignup);
            return;
        }

        if (!isUsernameValid) {
            showErrorToast("Invalid Username: Must be 4-25 characters");
            showButtonErrorState(btnSignup);
            return;
        }

        if (!isPasswordValid) {
            showErrorToast("Invalid Password: 4-16 chars & 1 special char (e.g. @, #, $)");
            showButtonErrorState(btnSignup);
            return;
        }

        // All validations passed
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_PASSWORD, password);
        editor.apply();

        showButtonSuccessState(btnSignup);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1000);
    }

    private void showButtonErrorState(final Button button) {
        final Drawable originalBackground = ContextCompat.getDrawable(this, R.drawable.button_background);
        button.setBackgroundResource(R.drawable.button_background_error);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                button.setBackground(originalBackground);
            }
        }, 2000);
    }

    private void showButtonSuccessState(final Button button) {
        button.setBackgroundResource(R.drawable.button_background_success);
    }

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
}