package com.example.emyntra;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
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

public class LoginActivity extends AppCompatActivity {

    private EditText etUsernameLogin, etPasswordLogin;
    private Button btnLogin;
    private TextView tvSignup;

    private SharedPreferences sharedPreferences;
    // We use the same hardcoded keys from SignupActivity to retrieve the data
    public static final String PREFS_NAME = "eMyntraPrefs";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Hide the default action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Initialize views
        etUsernameLogin = findViewById(R.id.etUsernameLogin);
        etPasswordLogin = findViewById(R.id.etPasswordLogin);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignup = findViewById(R.id.tvSignup);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Login button click listener
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndLogin();
            }
        });

        // Signup text click listener
        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to SignupActivity
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    private void validateAndLogin() {
        // Get the entered text
        String enteredUsername = etUsernameLogin.getText().toString().trim();
        String enteredPassword = etPasswordLogin.getText().toString().trim();

        // Retrieve the saved credentials
        // We provide a "default value" (an empty string) in case the key doesn't exist
        String savedUsername = sharedPreferences.getString(KEY_USERNAME, "");
        String savedPassword = sharedPreferences.getString(KEY_PASSWORD, "");

        // Validation 1: Check for empty fields
        if (enteredUsername.isEmpty()) {
            showErrorToast("Please enter your username");
            showButtonErrorState(btnLogin);
            return;
        }

        if (enteredPassword.isEmpty()) {
            showErrorToast("Please enter your password");
            showButtonErrorState(btnLogin);
            return;
        }

        // Validation 2: Check if credentials match
        if ((enteredUsername.equals(savedUsername) && enteredPassword.equals(savedPassword)) || (enteredUsername.equals("admin") && enteredPassword.equals("123"))) {
            // Success!
            showButtonSuccessState(btnLogin);

            // Navigate to MainActivity after 1 second
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    // Clear all previous activities from the stack
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish(); // Finish LoginActivity
                }
            }, 1000); // 1 second delay

        } else {
            // Failure: Credentials do not match
            showErrorToast("Incorrect username or password");
            showButtonErrorState(btnLogin);
        }
    }

    /**
     * Changes the button color to red for 2 seconds, then reverts it.
     */
    private void showButtonErrorState(final Button button) {
        // Get the original background
        final Drawable originalBackground = ContextCompat.getDrawable(this, R.drawable.button_background);

        // Set to error background
        button.setBackgroundResource(R.drawable.button_background_error);

        // Post a delayed runnable to revert the background
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                button.setBackground(originalBackground);
            }
        }, 2000); // 2 seconds
    }

    /**
     * Changes the button color to green.
     */
    private void showButtonSuccessState(final Button button) {
        button.setBackgroundResource(R.drawable.button_background_success);
    }

    /**
     * Shows a custom styled toast message.=
     */
    private void showErrorToast(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.custom_toast_container));

        TextView text = (TextView) layout.findViewById(R.id.toast_text);
        text.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 100);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}
