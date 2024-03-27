package com.health.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout emailTextInputLayout, passwordTextInputLayout;
    private EditText emailEditText, passwordEditText;
    private CheckBox rememberMeCheckBox;
    private SharedPreferencesHelper sharedPreferencesHelper;


    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailTextInputLayout = findViewById(R.id.emailTextInputLayout);
        passwordTextInputLayout = findViewById(R.id.passwordTextInputLayout);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        rememberMeCheckBox = findViewById(R.id.rememberMeCheckBox);
        sharedPreferencesHelper = new SharedPreferencesHelper(this);
        databaseHelper = new DatabaseHelper(this);

        TextView forgotPasswordLink = findViewById(R.id.forgotPasswordLink);
        forgotPasswordLink.setOnClickListener(v -> goToForgotPassword(v));

        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(v -> {
            if (validateInput()) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (isValidCredentials(email, password)) {
                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                    if (rememberMeCheckBox.isChecked()) {
                        sharedPreferencesHelper.saveRememberMeCredentials(email, true);
                    } else {
                        sharedPreferencesHelper.clearRememberMeCredentials();
                    }

                    int userId = databaseHelper.getUserIdByEmail(email);

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("USER_ID", userId);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            }
        });


        passwordTextInputLayout.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);

        passwordTextInputLayout.setEndIconOnClickListener(view -> togglePasswordVisibility());

    }




    private boolean validateInput() {
        boolean isValid = true;

        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailTextInputLayout.setError("Enter a valid email address");
            isValid = false;
        } else {
            emailTextInputLayout.setError(null);
        }

        if (password.isEmpty() || password.length() < 6) {
            passwordTextInputLayout.setError("Password must be at least 6 characters");
            isValid = false;
        } else {
            passwordTextInputLayout.setError(null);
        }

        return isValid;
    }

    private boolean isValidCredentials(String email, String password) {

        return databaseHelper.checkUserCredentials(email, password);
    }
    private void togglePasswordVisibility() {
        EditText passwordEditText = passwordTextInputLayout.getEditText();
        if (passwordEditText != null) {
            int selection = passwordEditText.getSelectionEnd();
            if (passwordEditText.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            passwordEditText.setSelection(selection);
        }
    }


    public void goToForgotPassword(View view) {
        Intent intent = new Intent(LoginActivity.this, ForgotActivity.class);
        startActivity(intent);
    }
    public void goToRegister(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}
