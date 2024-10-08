package com.example.bt2.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.bt2.model.Category;
import com.example.bt2.model.CategoryInOut;
import com.example.bt2.model.InOut;
import com.example.bt2.model.Transaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TransactionDAO {
    private SQLiteDatabase db;
    private SimpleDateFormat dateFormat;

    public TransactionDAO(SQLiteDatabase db) {
        this.db = db;
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    }

    public boolean add(Transaction t) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_NAME, t.getName());
        values.put(DBHelper.COLUMN_CATEGORY_INOUT_ID, t.getCategory().getId());
        values.put(DBHelper.COLUMN_AMOUNT, t.getAmount());
        values.put(DBHelper.COLUMN_DATE, dateFormat.format(t.getDay()));
        values.put(DBHelper.COLUMN_NOTE, t.getNote());

        long insertId = db.insert(DBHelper.TABLE_TRANSACTIONS, null, values);
        return insertId != -1;
    }

    public boolean edit(Transaction t) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_NAME, t.getName());
        values.put(DBHelper.COLUMN_CATEGORY_INOUT_ID, t.getCategory().getId());
        values.put(DBHelper.COLUMN_AMOUNT, t.getAmount());
        values.put(DBHelper.COLUMN_DATE, dateFormat.format(t.getDay()));
        values.put(DBHelper.COLUMN_NOTE, t.getNote());

        int rowsAffected = db.update(DBHelper.TABLE_TRANSACTIONS, values,
                DBHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(t.getId())});
        return rowsAffected > 0;
    }

    public boolean delete(int id) {
        int rowsAffected = db.delete(DBHelper.TABLE_TRANSACTIONS,
                DBHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        return rowsAffected > 0;
    }

    public List<Transaction> search(Date day) {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT t.*, c.name as category_name, i.name as inout_name, i.id as inout_id " +
                "FROM " + DBHelper.TABLE_TRANSACTIONS + " t " +
                "JOIN " + DBHelper.TABLE_CATEGORY_INOUT + " ci ON t." + DBHelper.COLUMN_CATEGORY_INOUT_ID + " = ci.id " +
                "JOIN " + DBHelper.TABLE_CATEGORY + " c ON ci." + DBHelper.COLUMN_CATEGORY_ID + " = c.id " +
                "JOIN " + DBHelper.TABLE_INOUT + " i ON ci." + DBHelper.COLUMN_INOUT_ID + " = i.id " +
                "WHERE t." + DBHelper.COLUMN_DATE + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{dateFormat.format(day)});

        if (cursor.moveToFirst()) {
            do {
                Transaction transaction = cursorToTransaction(cursor);
                transactions.add(transaction);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return transactions;
    }

    @SuppressLint("Range")
    private Transaction cursorToTransaction(Cursor cursor) {
        Transaction transaction = new Transaction();
        transaction.setId(cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_ID)));
        transaction.setName(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_NAME)));
        transaction.setAmount(cursor.getDouble(cursor.getColumnIndex(DBHelper.COLUMN_AMOUNT)));
        transaction.setNote(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_NOTE)));

        try {
            transaction.setDay(dateFormat.parse(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DATE))));
        } catch (Exception e) {
            e.printStackTrace();
        }

        CategoryInOut categoryInOut = new CategoryInOut();
        categoryInOut.setId(cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_CATEGORY_INOUT_ID)));

        Category category = new Category();
        category.setName(cursor.getString(cursor.getColumnIndex("category_name")));
        category.setIcon(cursor.getString(cursor.getColumnIndex("icon"))); // Thêm dòng này
        categoryInOut.setCategory(category);

        InOut inOut = new InOut();
        inOut.setId(cursor.getInt(cursor.getColumnIndex("inout_id")));
        inOut.setName(cursor.getString(cursor.getColumnIndex("inout_name")));
        categoryInOut.setIdInOut(inOut.getId());

        transaction.setCategory(categoryInOut);

        return transaction;
    }


    public double getTotalIncome(Date date) {
        return getTotal(date, 1); // Assuming 1 is for income
    }

    public double getTotalExpense(Date date) {
        return getTotal(date, 2); // Assuming 2 is for expense
    }

    @SuppressLint("Range")
    private double getTotal(Date date, int inOutId) {
        String query = "SELECT SUM(t." + DBHelper.COLUMN_AMOUNT + ") as total " +
                "FROM " + DBHelper.TABLE_TRANSACTIONS + " t " +
                "JOIN " + DBHelper.TABLE_CATEGORY_INOUT + " ci ON t." + DBHelper.COLUMN_CATEGORY_INOUT_ID + " = ci.id " +
                "WHERE t." + DBHelper.COLUMN_DATE + " = ? AND ci." + DBHelper.COLUMN_INOUT_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{dateFormat.format(date), String.valueOf(inOutId)});

        double total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(cursor.getColumnIndex("total"));
        }
        cursor.close();
        return total;
    }

    public int getIncomeCount(Date date) {
        return getCount(date, 1); // Giả sử 1 là ID cho thu nhập
    }

    public int getExpenseCount(Date date) {
        return getCount(date, 2); // Giả sử 2 là ID cho chi tiêu
    }

    @SuppressLint("Range")
    private int getCount(Date date, int inOutId) {
        String query = "SELECT COUNT(*) as count " +
                "FROM " + DBHelper.TABLE_TRANSACTIONS + " t " +
                "JOIN " + DBHelper.TABLE_CATEGORY_INOUT + " ci ON t." + DBHelper.COLUMN_CATEGORY_INOUT_ID + " = ci.id " +
                "WHERE t." + DBHelper.COLUMN_DATE + " = ? AND ci." + DBHelper.COLUMN_INOUT_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{dateFormat.format(date), String.valueOf(inOutId)});

        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(cursor.getColumnIndex("count"));
        }
        cursor.close();
        return count;
    }
}