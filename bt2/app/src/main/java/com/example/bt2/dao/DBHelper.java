package com.example.bt2.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "expense_tracker.db";
    private static final int DATABASE_VERSION = 3;

    // Table names
    public static final String TABLE_INOUT = "inOut";
    public static final String TABLE_CATEGORY = "category";
    public static final String TABLE_CATEGORY_INOUT = "categoryInOut";
    public static final String TABLE_TRANSACTIONS = "transactions";

    // Common column names
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";

    // inOut table columns
    // (only id and name, which are common columns)

    // category table columns
    public static final String COLUMN_PARENT_ID = "idParent";
    public static final String COLUMN_ICON = "icon";
    public static final String COLUMN_NOTE = "note";

    // categoryInOut table columns
    public static final String COLUMN_CATEGORY_ID = "idCategory";
    public static final String COLUMN_INOUT_ID = "idInOut";

    // transaction table columns
    public static final String COLUMN_CATEGORY_INOUT_ID = "idCategoryInOut";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_DATE = "date";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create inOut table
        String CREATE_INOUT_TABLE = "CREATE TABLE " + TABLE_INOUT + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_NAME + " TEXT" + ")";
        db.execSQL(CREATE_INOUT_TABLE);

        // Create category table
        String CREATE_CATEGORY_TABLE = "CREATE TABLE " + TABLE_CATEGORY + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_ICON + " TEXT,"
                + COLUMN_PARENT_ID + " INTEGER,"
                + COLUMN_NOTE + " TEXT" + ")";
        db.execSQL(CREATE_CATEGORY_TABLE);

        // Create categoryInOut table
        String CREATE_CATEGORY_INOUT_TABLE = "CREATE TABLE " + TABLE_CATEGORY_INOUT + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_CATEGORY_ID + " INTEGER,"
                + COLUMN_INOUT_ID + " INTEGER" + ")";
        db.execSQL(CREATE_CATEGORY_INOUT_TABLE);

        // Create transaction table
        String CREATE_TRANSACTIONS_TABLE = "CREATE TABLE " + TABLE_TRANSACTIONS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_CATEGORY_INOUT_ID + " INTEGER,"
                + COLUMN_AMOUNT + " REAL,"
                + COLUMN_DATE + " TEXT,"
                + COLUMN_NOTE + " TEXT" + ")";
        db.execSQL(CREATE_TRANSACTIONS_TABLE);

        // Insert initial data
        insertInitialData(db);
    }

    private void insertInitialData(SQLiteDatabase db) {
        // Insert InOut types
        db.execSQL("INSERT INTO " + TABLE_INOUT + " (" + COLUMN_NAME + ") VALUES ('In'), ('Out')");

        // Insert In categories with icon
        db.execSQL("INSERT INTO " + TABLE_CATEGORY + " (" + COLUMN_NAME + ", " + COLUMN_ICON + ") VALUES ('Salary', '@drawable/home'), ('Part-time', '@drawable/date'), ('Scholarship', '@drawable/shopping_bag'), ('ParentGive', '@drawable/home'), ('Present', '@drawable/date')");

        // Insert Out categories with icon
        db.execSQL("INSERT INTO " + TABLE_CATEGORY + " (" + COLUMN_NAME + ", " + COLUMN_ICON + ") VALUES ('Tuition', '@drawable/shopping_bag'), ('Daily Pay', '@drawable/home'), ('Transport Pay', '@drawable/date'), ('Present Pay', '@drawable/shopping_bag')");
        db.execSQL("INSERT INTO " + TABLE_CATEGORY + " (" + COLUMN_NAME + ", " + COLUMN_ICON + ", " + COLUMN_PARENT_ID + ") VALUES ('Home Pay', '@drawable/home', 7), ('Electric Pay', '@drawable/date', 7), ('Water Pay', '@drawable/shopping_bag', 7), ('Telephone Pay', '@drawable/home', 7), ('Eat Pay', '@drawable/date', 7)");

        // Link categories to InOut types
        for (int i = 1; i <= 5; i++) {
            db.execSQL("INSERT INTO " + TABLE_CATEGORY_INOUT + " (" + COLUMN_CATEGORY_ID + ", " + COLUMN_INOUT_ID + ") VALUES (" + i + ", 1)");
        }
        for (int i = 6; i <= 13; i++) {
            db.execSQL("INSERT INTO " + TABLE_CATEGORY_INOUT + " (" + COLUMN_CATEGORY_ID + ", " + COLUMN_INOUT_ID + ") VALUES (" + i + ", 2)");
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY_INOUT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INOUT);

        // Create tables again
        onCreate(db);
    }
}
