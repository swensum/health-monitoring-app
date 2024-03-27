package com.health.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "user_db";
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_FULL_NAME = "full_name";
    private static final String COLUMN_MOBILE = "mobile";
    private static final String COLUMN_ADDRESS = "address";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_SEX = "sex";
    private static final String COLUMN_DIVISION = "division";
    public static final String COLUMN_PROFILE_IMAGE_URI = "profile_image_uri";
    private static final String COLUMN_IS_VERIFIED = "is_verified";

    private static final String CREATE_TABLE_USERS =
            "CREATE TABLE " + TABLE_USERS + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_FULL_NAME + " TEXT," +
                    COLUMN_MOBILE + " TEXT," +
                    COLUMN_ADDRESS + " TEXT," +
                    COLUMN_EMAIL + " TEXT," +
                    COLUMN_PASSWORD + " TEXT," +
                    COLUMN_SEX + " TEXT," +
                    COLUMN_DIVISION + " TEXT," +
                    COLUMN_PROFILE_IMAGE_URI + " TEXT," +
                    COLUMN_IS_VERIFIED + " INTEGER DEFAULT 0" +
                    ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public long insertUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FULL_NAME, user.getFullName());
        values.put(COLUMN_MOBILE, user.getMobile());
        values.put(COLUMN_ADDRESS, user.getAddress());
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_PASSWORD, user.getPassword());
        values.put(COLUMN_SEX, user.getSex());
        values.put(COLUMN_DIVISION, user.getDivision());
        values.put(COLUMN_PROFILE_IMAGE_URI, user.getProfileImageUri());
        values.put(COLUMN_IS_VERIFIED, 0);
        long id = db.insert(TABLE_USERS, null, values);
        db.close();
        return id;
    }

    public User getUserById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_FULL_NAME, COLUMN_MOBILE, COLUMN_ADDRESS, COLUMN_EMAIL, COLUMN_PASSWORD, COLUMN_SEX, COLUMN_DIVISION, COLUMN_PROFILE_IMAGE_URI},
                COLUMN_ID + "=?",
                new String[]{String.valueOf(userId)},
                null, null, null, null);

        User user = null;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int fullNameIndex = cursor.getColumnIndex(COLUMN_FULL_NAME);
                int mobileIndex = cursor.getColumnIndex(COLUMN_MOBILE);
                int addressIndex = cursor.getColumnIndex(COLUMN_ADDRESS);
                int emailIndex = cursor.getColumnIndex(COLUMN_EMAIL);
                int passwordIndex = cursor.getColumnIndex(COLUMN_PASSWORD);
                int sexIndex = cursor.getColumnIndex(COLUMN_SEX);
                int divisionIndex = cursor.getColumnIndex(COLUMN_DIVISION);
                int profileImageUriIndex = cursor.getColumnIndex(COLUMN_PROFILE_IMAGE_URI);

                if (fullNameIndex >= 0 && mobileIndex >= 0 && addressIndex >= 0 && emailIndex >= 0 && passwordIndex >= 0 && sexIndex >= 0 && divisionIndex >= 0 && profileImageUriIndex >= 0) {
                    user = new User(
                            cursor.getString(fullNameIndex),
                            cursor.getString(mobileIndex),
                            cursor.getString(addressIndex),
                            cursor.getString(emailIndex),
                            cursor.getString(passwordIndex),
                            cursor.getString(sexIndex),
                            cursor.getString(divisionIndex),
                            cursor.getString(profileImageUriIndex)
                    );
                    Log.d("DatabaseHelper", "Profile Image URI for user ID " + userId + ": " + user.getProfileImageUri());
                }
            }
            cursor.close();
        }

        db.close();
        return user;
    }

    public boolean updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_FULL_NAME, user.getFullName());
        values.put(COLUMN_MOBILE, user.getMobile());
        values.put(COLUMN_ADDRESS, user.getAddress());
        values.put(COLUMN_SEX, user.getSex());
        values.put(COLUMN_DIVISION, user.getDivision());
        values.put(COLUMN_PROFILE_IMAGE_URI, user.getProfileImageUri());

        int rowsAffected = 0;

        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            rowsAffected = db.update(TABLE_USERS, values, COLUMN_EMAIL + " = ?",
                    new String[]{user.getEmail()});
        }

        if (rowsAffected > 0) {
            Log.i("DatabaseHelper", "Update successful: Rows affected = " + rowsAffected);
        } else {
            Log.e("DatabaseHelper", "Update failed: No rows affected or invalid user email");
        }

        Log.d("DatabaseHelper", "Values to be updated - " + values.toString());

        db.close();

        return rowsAffected > 0;
    }
    public boolean updateUserPassword(int userId, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PASSWORD, newPassword);

        int rowsAffected = db.update(TABLE_USERS, values, COLUMN_ID + "=?", new String[]{String.valueOf(userId)});
        db.close();

        return rowsAffected > 0;
    }

    public int getUserIdFromPhoneNumber(String phoneNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        int userId = -1;

        Cursor cursor = null;

        try {
            cursor = db.query(TABLE_USERS,
                    new String[]{COLUMN_ID},
                    COLUMN_MOBILE + "=?",
                    new String[]{phoneNumber},
                    null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                int userIdIndex = cursor.getColumnIndex(COLUMN_ID);

                if (userIdIndex >= 0) {
                    userId = cursor.getInt(userIdIndex);
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }

            db.close();
        }

        return userId;
    }
    public int getUserIdByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        int userId = -1;

        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_ID},
                COLUMN_EMAIL + "=?",
                new String[]{email},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int userIdIndex = cursor.getColumnIndex(COLUMN_ID);
            if (userIdIndex >= 0) {
                userId = cursor.getInt(userIdIndex);
            }
            cursor.close();
        }

        db.close();
        return userId;
    }

    public boolean checkUserCredentials(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        boolean isValid = false;

        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_ID},
                COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{email, password},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            isValid = true;
            cursor.close();
        }

        db.close();
        return isValid;
    }

}
