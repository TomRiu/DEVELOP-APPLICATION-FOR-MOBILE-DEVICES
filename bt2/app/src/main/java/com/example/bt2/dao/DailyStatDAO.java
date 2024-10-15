package com.example.bt2.dao;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.bt2.model.DailyStat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DailyStatDAO {
    private SQLiteDatabase db;

    public DailyStatDAO(SQLiteDatabase db) {
        this.db = db;
    }

    public DailyStat getDayStat(Date day) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String dateString = dateFormat.format(day);

        String query = "SELECT " +
                "SUM(CASE WHEN ci." + DBHelper.COLUMN_INOUT_ID + " = 1 THEN t." + DBHelper.COLUMN_AMOUNT + " ELSE 0 END) as income, " +
                "SUM(CASE WHEN ci." + DBHelper.COLUMN_INOUT_ID + " = 2 THEN t." + DBHelper.COLUMN_AMOUNT + " ELSE 0 END) as outcome " +
                "FROM " + DBHelper.TABLE_TRANSACTIONS + " t " +
                "JOIN " + DBHelper.TABLE_CATEGORY_INOUT + " ci ON t." + DBHelper.COLUMN_CATEGORY_INOUT_ID + " = ci.id " +
                "WHERE t." + DBHelper.COLUMN_DATE + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{dateString});

        DailyStat dailyStat = null;
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") double income = cursor.getDouble(cursor.getColumnIndex("income"));
            @SuppressLint("Range") double outcome = cursor.getDouble(cursor.getColumnIndex("outcome"));
            dailyStat = new DailyStat(day, income, outcome);
        }
        cursor.close();
        return dailyStat;
    }

    public List<DailyStat> getMonth(Date month) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(month);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date startDate = cal.getTime();
        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        Date endDate = cal.getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String startDateString = dateFormat.format(startDate);
        String endDateString = dateFormat.format(endDate);

        String query = "SELECT t." + DBHelper.COLUMN_DATE + ", " +
                "SUM(CASE WHEN ci." + DBHelper.COLUMN_INOUT_ID + " = 1 THEN t." + DBHelper.COLUMN_AMOUNT + " ELSE 0 END) as income, " +
                "SUM(CASE WHEN ci." + DBHelper.COLUMN_INOUT_ID + " = 2 THEN t." + DBHelper.COLUMN_AMOUNT + " ELSE 0 END) as outcome " +
                "FROM " + DBHelper.TABLE_TRANSACTIONS + " t " +
                "JOIN " + DBHelper.TABLE_CATEGORY_INOUT + " ci ON t." + DBHelper.COLUMN_CATEGORY_INOUT_ID + " = ci.id " +
                "WHERE t." + DBHelper.COLUMN_DATE + " BETWEEN ? AND ? " +
                "GROUP BY t." + DBHelper.COLUMN_DATE;

        Cursor cursor = db.rawQuery(query, new String[]{startDateString, endDateString});

        List<DailyStat> monthStats = new ArrayList<>();
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String dateString = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DATE));
            @SuppressLint("Range") double income = cursor.getDouble(cursor.getColumnIndex("income"));
            @SuppressLint("Range") double outcome = cursor.getDouble(cursor.getColumnIndex("outcome"));
            try {
                Date date = dateFormat.parse(dateString);
                monthStats.add(new DailyStat(date, income, outcome));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        cursor.close();
        return monthStats;
    }
}
