package com.health.myapplication;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {

    private static final String PREFERENCES_NAME = "RememberMePrefs";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_REMEMBER_ME = "rememberMe";

    private final SharedPreferences sharedPreferences;

    public SharedPreferencesHelper(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public void saveRememberMeCredentials(String email, boolean rememberMe) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_EMAIL, email);
        editor.putBoolean(KEY_REMEMBER_ME, rememberMe);
        editor.apply();
    }

    public void clearRememberMeCredentials() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public void logout(Context context, Class<?> loginActivityClass) {
        clearRememberMeCredentials();
        Intent intent = new Intent(context, loginActivityClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

}
