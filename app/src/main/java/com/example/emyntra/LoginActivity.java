package com.example.emyntra;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
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

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmailLogin, etPasswordLogin;
    private Button btnLogin;
    private TextView tvSignup, tvForgotPassword; // Added tvForgotPassword
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mAuth = FirebaseAuth.getInstance();

        // Note: The ID in XML is etUsernameLogin but we refer to it as Email here
        etEmailLogin = findViewById(R.id.etUsernameLogin);
        etPasswordLogin = findViewById(R.id.etPasswordLogin);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignup = findViewById(R.id.tvSignup);
        tvForgotPassword = findViewById(R.id.tvForgotPassword); // Initialize the Forgot Password link

        // Ensure hint is explicit
        // Removed setHint to avoid double hint issue

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndLogin();
            }
        });

        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

        // Forgot Password Click Listener
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });
    }

    private void validateAndLogin() {
        String email = etEmailLogin.getText().toString().trim();
        String password = etPasswordLogin.getText().toString().trim();

        // 1. Check for Empty Fields
        if (TextUtils.isEmpty(email)) {
            showErrorToast("Please enter your Email");
            showButtonErrorState(btnLogin);
            return;
        }

        if (TextUtils.isEmpty(password)) {
            showErrorToast("Please enter your Password");
            showButtonErrorState(btnLogin);
            return;
        }

        // 2. Firebase Login
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Login Success
                            showButtonSuccessState(btnLogin);

                            // Navigate to Main Activity after 1 second
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    // Clear history so 'Back' doesn't return to Login
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            }, 1000);

                        } else {
                            // Login Failed
                            String errorMsg = task.getException() != null ? task.getException().getMessage() : "Authentication Failed";
                            showErrorToast("Login Failed: " + errorMsg);
                            showButtonErrorState(btnLogin);
                        }
                    }
                });
    }

    // --- Helper Methods for UI Feedback ---

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