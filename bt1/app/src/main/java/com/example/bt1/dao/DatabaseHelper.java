package com.example.bt1.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Version
    public static final int DATABASE_VERSION = 1;

    // Database Name
    public static final String DATABASE_NAME = "bt1";

    // Table Name
    public static final String TABLE_USERS = "users";

    // User Table Columns names
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_FULLNAME = "fullname";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_GENDER = "gender";
    public static final String COLUMN_DOB = "dob";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USERNAME + " TEXT,"
                + COLUMN_PASSWORD + " TEXT,"
                + COLUMN_FULLNAME + " TEXT,"
                + COLUMN_EMAIL + " TEXT,"
                + COLUMN_GENDER + " TEXT,"
                + COLUMN_DOB + " INTEGER" + ")";
        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

        // Create tables again
        onCreate(db);
    }
}
