package com.health.myapplication;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Random;

public class ForgotActivity extends AppCompatActivity {

    private enum ForgotPasswordStep {
        ENTER_PHONE,
        VERIFY_CODE,
        SET_NEW_PASSWORD,
        PASSWORD_RESET_SUCCESS
    }

    private ForgotPasswordStep currentStep;
    private TextInputLayout phoneTextInputLayout;
    private TextInputLayout newPasswordTextInputLayout;
    private TextInputLayout confirmPasswordTextInputLayout;
    private String verificationCode;
    private int userId; // Added to store the user ID
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        currentStep = ForgotPasswordStep.ENTER_PHONE;
        phoneTextInputLayout = findViewById(R.id.phoneTextInputLayout);
        databaseHelper = new DatabaseHelper(this);

        findViewById(R.id.sendVerificationCodeButton).setOnClickListener(v -> handleButtonClick());

        phoneTextInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void handleButtonClick() {
        switch (currentStep) {
            case ENTER_PHONE:
                sendVerificationCode();
                break;
            case VERIFY_CODE:
                verifyCode();
                break;
            case SET_NEW_PASSWORD:
                resetPassword();
                break;
            case PASSWORD_RESET_SUCCESS:
                finish();
                break;
        }
    }

    private void sendVerificationCode() {
        String phoneNumber = phoneTextInputLayout.getEditText().getText().toString().trim();
        if (isValidPhoneNumber(phoneNumber)) {

            userId = getUserIdFromPhoneNumber(phoneNumber);
            if (userId != -1) {
                verificationCode = generateRandomCode();
                sendVerificationCodeToUser(phoneNumber, verificationCode);
                switchToNextStep();
            } else {
                Toast.makeText(this, "Phone number not associated with any user", Toast.LENGTH_SHORT).show();
            }
        } else {
            phoneTextInputLayout.setError("Enter a valid phone number");
        }
    }

    private void verifyCode() {
        String enteredCode = phoneTextInputLayout.getEditText().getText().toString().trim();
        if (enteredCode.equals(verificationCode)) {
            switchToNextStep();
        } else {
            phoneTextInputLayout.setError("Invalid verification code");
        }
    }

    private void resetPassword() {
        newPasswordTextInputLayout = findViewById(R.id.newPasswordTextInputLayout);
        confirmPasswordTextInputLayout = findViewById(R.id.confirmPasswordTextInputLayout);

        String newPassword = newPasswordTextInputLayout.getEditText().getText().toString().trim();
        String confirmPassword = confirmPasswordTextInputLayout.getEditText().getText().toString().trim();

        if (isValidPassword(newPassword) && newPassword.equals(confirmPassword)) {
            confirmPasswordTextInputLayout.setError(null);
        } else {
            confirmPasswordTextInputLayout.setError("Passwords do not match or are not valid");
        }

        boolean passwordUpdated = databaseHelper.updateUserPassword(userId, newPassword);
        if (passwordUpdated) {
            Toast.makeText(this, "Password reset successful", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to update password", Toast.LENGTH_SHORT).show();

    }

}
    private void switchToNextStep() {
        switch (currentStep) {
            case ENTER_PHONE:
                currentStep = ForgotPasswordStep.VERIFY_CODE;
                phoneTextInputLayout.getEditText().getText().clear();
                phoneTextInputLayout.setHint("Verification Code");
                break;
            case VERIFY_CODE:
                currentStep = ForgotPasswordStep.SET_NEW_PASSWORD;
                setContentView(R.layout.activity_reset);
                newPasswordTextInputLayout = findViewById(R.id.newPasswordTextInputLayout);
                confirmPasswordTextInputLayout = findViewById(R.id.confirmPasswordTextInputLayout);
                findViewById(R.id.resetPasswordButton).setOnClickListener(v -> handleButtonClick());
                break;
            case SET_NEW_PASSWORD:
                setContentView(R.layout.activity_reset);
                newPasswordTextInputLayout = findViewById(R.id.newPasswordTextInputLayout);
                confirmPasswordTextInputLayout = findViewById(R.id.confirmPasswordTextInputLayout);
                findViewById(R.id.resetPasswordButton).setOnClickListener(v -> handleButtonClick());

                break;
        }
    }

    private String generateRandomCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    private void sendVerificationCodeToUser(String phoneNumber, String verificationCode) {
        try {
            if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
                android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNumber, null, "Your verification code: " + verificationCode, null, null);
                Toast.makeText(this, "Verification code sent successfully", Toast.LENGTH_SHORT).show();
                }
        } catch (Exception e) {
            Toast.makeText(this, "Failed to send verification code", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    private boolean isValidPhoneNumber(String phoneNumber) {
        return !TextUtils.isEmpty(phoneNumber);
    }

    private boolean isValidPassword(String password) {
        if (password.length() < 8) {
            return false;
        }

        boolean containsUpperCase = false;
        boolean containsLowerCase = false;
        boolean containsDigit = false;
        boolean containsSpecialChar = false;

        for (char ch : password.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                containsUpperCase = true;
            } else if (Character.isLowerCase(ch)) {
                containsLowerCase = true;
            } else if (Character.isDigit(ch)) {
                containsDigit = true;
            } else {
                containsSpecialChar = true;
            }
        }

        return containsUpperCase && containsLowerCase && containsDigit && containsSpecialChar;
    }

    private int getUserIdFromPhoneNumber(String phoneNumber) {
        return databaseHelper.getUserIdFromPhoneNumber(phoneNumber);
    }
}
