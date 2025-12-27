package com.example.emyntra;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

// Firebase Imports
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnSignup;
    private TextView tvLogin;

    private FirebaseAuth mAuth;

    // Flags to track validation status
    private boolean isEmailValid = false;
    private boolean isPasswordValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize Views
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnSignup = findViewById(R.id.btnSignup);
        tvLogin = findViewById(R.id.tvLogin);

        // --- 1. SETUP REAL-TIME VALIDATIONS ---
        setupValidationListeners();

        // --- 2. SETUP FIREBASE SIGNUP ---
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSignup();
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Login Activity
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupValidationListeners() {
        // Email Validation Listener
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString().trim();
                // Check for valid email format (contains @ and .)
                if (!input.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(input).matches()) {
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

        // Password Validation Listener
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString().trim();
                // Rule: Min 4, Max 16 chars AND at least one special character
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
        final String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // 1. Check Empty first
        if (email.isEmpty()) {
            showErrorToast("Please enter Email");
            showButtonErrorState(btnSignup);
            return;
        }
        if (password.isEmpty()) {
            showErrorToast("Please enter Password");
            showButtonErrorState(btnSignup);
            return;
        }

        // 2. Check Validity Flags (Real-time checks)
        if (!isEmailValid) {
            showErrorToast("Invalid Email format");
            showButtonErrorState(btnSignup);
            return;
        }
        if (!isPasswordValid) {
            showErrorToast("Password too weak (add symbol, 4-16 chars)");
            showButtonErrorState(btnSignup);
            return;
        }

        // 3. Create User in Firebase
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Signup Success
                            showButtonSuccessState(btnSignup);

                            // Delay navigation to show success green button
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // Navigate to Login Activity to force fresh login
                                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish(); // Close SignupActivity
                                }
                            }, 1000);

                        } else {
                            // Signup Failure (e.g., email already in use)
                            showErrorToast("Signup Failed: " + task.getException().getMessage());
                            showButtonErrorState(btnSignup);
                        }
                    }
                });
    }

    // Helper Methods for UI
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
        View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.custom_toast_container));
        TextView text = layout.findViewById(R.id.toast_text);
        text.setText(message);
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 100);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}