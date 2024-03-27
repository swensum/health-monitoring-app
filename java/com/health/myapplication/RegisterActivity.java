package com.health.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText fullNameEditText, mobileEditText, addressEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    private Spinner sexSpinner, divisionSpinner;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fullNameEditText = findViewById(R.id.input_fullName);
        mobileEditText = findViewById(R.id.inputMobile);
        addressEditText = findViewById(R.id.inputAddress);
        emailEditText = findViewById(R.id.input_userEmail);
        passwordEditText = findViewById(R.id.input_password);
        confirmPasswordEditText = findViewById(R.id.input_password_confirm);

        sexSpinner = findViewById(R.id.inputSex);
        divisionSpinner = findViewById(R.id.inputDivision);

        Button registerButton = findViewById(R.id.button_register);
        databaseHelper = new DatabaseHelper(this);

        registerButton.setOnClickListener(v -> {
            if (validateInput()) {
                User user = new User(
                        fullNameEditText.getText().toString().trim(),
                        mobileEditText.getText().toString().trim(),
                        addressEditText.getText().toString().trim(),
                        emailEditText.getText().toString().trim(),
                        passwordEditText.getText().toString().trim(),
                        sexSpinner.getSelectedItem().toString(),
                        divisionSpinner.getSelectedItem().toString(),
                        null
                );


                long id = databaseHelper.insertUser(user);

                if (id != -1) {
                    Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                    navigateToLogin();
                } else {
                    Toast.makeText(RegisterActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validateInput() {
        boolean isValid = true;

        // Validate other fields as needed
        if (fullNameEditText.getText().toString().trim().isEmpty()) {
            fullNameEditText.setError("Enter your full name");
            isValid = false;
        } else {
            fullNameEditText.setError(null);
        }
        String mobileNumber = mobileEditText.getText().toString().trim();
        if (mobileNumber.isEmpty()) {
            mobileEditText.setError("Enter your contact number");
            isValid = false;
        } else if (!isValidMobileNumber(mobileNumber)) {
            mobileEditText.setError("Invalid contact number");
            isValid = false;
        } else {
            mobileEditText.setError(null);
        }

        // Validate address field
        if (addressEditText.getText().toString().trim().isEmpty()) {
            addressEditText.setError("Enter your address");
            isValid = false;
        } else {
            addressEditText.setError(null);
        }

        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Enter a valid email address");
            isValid = false;
        } else {
            emailEditText.setError(null);
        }

        if (password.isEmpty() || password.length() < 6) {
            passwordEditText.setError("Password must be at least 6 characters");
            isValid = false;
        } else {
            passwordEditText.setError(null);
        }

        if (!confirmPassword.equals(password)) {
            confirmPasswordEditText.setError("Passwords do not match");
            isValid = false;
        } else if (confirmPassword.isEmpty()) {
            confirmPasswordEditText.setError("Confirm your password");
            isValid = false;
        } else {
            confirmPasswordEditText.setError(null);
        }

        return isValid;
    }
    private boolean isValidMobileNumber(String mobileNumber) {

        return mobileNumber.matches("\\d{1,10}");
    }
    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}

