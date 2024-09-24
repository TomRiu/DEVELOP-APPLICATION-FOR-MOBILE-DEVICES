package com.example.bt1.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.bt1.model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserDAO {

    private DatabaseHelper databaseHelper;

    public UserDAO(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    // CRUD operations

    // Create new user
    public long createUser(User user) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_USERNAME, user.getUsername());
        values.put(DatabaseHelper.COLUMN_PASSWORD, user.getPassword());
        values.put(DatabaseHelper.COLUMN_FULLNAME, user.getFullname());
        values.put(DatabaseHelper.COLUMN_EMAIL, user.getEmail());
        values.put(DatabaseHelper.COLUMN_GENDER, user.getGender());
        values.put(DatabaseHelper.COLUMN_DOB, user.getDob().getTime());

        long id = db.insert(DatabaseHelper.TABLE_USERS, null, values);
        db.close();
        return id;
    }

    // Get user
    public User getUser(long id) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_USERS, new String[]{DatabaseHelper.COLUMN_ID,
                        DatabaseHelper.COLUMN_USERNAME, DatabaseHelper.COLUMN_PASSWORD, DatabaseHelper.COLUMN_FULLNAME, DatabaseHelper.COLUMN_EMAIL, DatabaseHelper.COLUMN_GENDER, DatabaseHelper.COLUMN_DOB}, DatabaseHelper.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        User user = new User(
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                new Date(cursor.getLong(6)));
        cursor.close();
        db.close();
        return user;
    }

    // Get All Users
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_USERS;

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(0)));
                user.setUsername(cursor.getString(1));
                user.setPassword(cursor.getString(2));
                user.setFullname(cursor.getString(3));
                user.setEmail(cursor.getString(4));
                user.setGender(cursor.getString(5));
                user.setDob(new Date(cursor.getLong(6)));
                userList.add(user);
            } while (cursor.moveToNext());
        }

        db.close();
        return userList;
    }

    // Update user
    public int updateUser(User user) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_USERNAME, user.getUsername());
        values.put(DatabaseHelper.COLUMN_PASSWORD, user.getPassword());
        values.put(DatabaseHelper.COLUMN_FULLNAME, user.getFullname());
        values.put(DatabaseHelper.COLUMN_EMAIL, user.getEmail());
        values.put(DatabaseHelper.COLUMN_GENDER, user.getGender());
        values.put(DatabaseHelper.COLUMN_DOB, user.getDob().getTime());

        // updating row
        return db.update(DatabaseHelper.TABLE_USERS, values, DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
    }

    // Delete user
    public int deleteUser(User user) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_USERS, DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
        return 1;
    }

    public User findUserByUsername(String username) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_USERS, new String[]{
                DatabaseHelper.COLUMN_ID,
                DatabaseHelper.COLUMN_USERNAME,
                DatabaseHelper.COLUMN_PASSWORD,
                DatabaseHelper.COLUMN_FULLNAME,
                DatabaseHelper.COLUMN_EMAIL,
                DatabaseHelper.COLUMN_GENDER,
                DatabaseHelper.COLUMN_DOB
        }, DatabaseHelper.COLUMN_USERNAME + "=?", new String[]{username}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            User user = new User(
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    new java.util.Date(cursor.getLong(6))
            );
            user.setId(cursor.getInt(0));
            cursor.close();
            db.close();
            return user;
        }
        return null;
    }

    public List<User> findAllUsersByUsername(String query) {
        List<User> users = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_USERS, new String[]{
                DatabaseHelper.COLUMN_ID,
                DatabaseHelper.COLUMN_USERNAME,
                DatabaseHelper.COLUMN_PASSWORD,
                DatabaseHelper.COLUMN_FULLNAME,
                DatabaseHelper.COLUMN_EMAIL,
                DatabaseHelper.COLUMN_GENDER,
                DatabaseHelper.COLUMN_DOB
        }, DatabaseHelper.COLUMN_USERNAME + " LIKE ?", new String[]{"%"+query+"%"}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                User user = new User(
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        new java.util.Date(cursor.getLong(6))
                );
                user.setId(cursor.getInt(0));
                users.add(user);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return users;
    }

    public List<User> findAllUsersByEmail(String query) {
        List<User> users = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_USERS, new String[]{
                DatabaseHelper.COLUMN_ID,
                DatabaseHelper.COLUMN_USERNAME,
                DatabaseHelper.COLUMN_PASSWORD,
                DatabaseHelper.COLUMN_FULLNAME,
                DatabaseHelper.COLUMN_EMAIL,
                DatabaseHelper.COLUMN_GENDER,
                DatabaseHelper.COLUMN_DOB
        }, DatabaseHelper.COLUMN_EMAIL + " LIKE ?", new String[]{"%"+query.toLowerCase()+"%"}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                User user = new User(
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        new java.util.Date(cursor.getLong(6))
                );
                user.setId(cursor.getInt(0));
                users.add(user);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return users;
    }
}
