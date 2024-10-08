package com.example.bt2.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.bt2.model.Category;
import com.example.bt2.model.CategoryInOut;

import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {
    private SQLiteDatabase db;

    public CategoryDAO(SQLiteDatabase db) {
        this.db = db;
    }

    public List<CategoryInOut> searchByInOut(boolean isIn) {
        List<CategoryInOut> categories = new ArrayList<>();
        String query = "SELECT c.*, ci." + DBHelper.COLUMN_INOUT_ID + " FROM " + DBHelper.TABLE_CATEGORY + " c " +
                "JOIN " + DBHelper.TABLE_CATEGORY_INOUT + " ci ON c." + DBHelper.COLUMN_ID + " = ci." + DBHelper.COLUMN_CATEGORY_ID + " " +
                "WHERE ci." + DBHelper.COLUMN_INOUT_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(isIn ? 1 : 2)});

        if (cursor.moveToFirst()) {
            do {
                CategoryInOut categoryInOut = cursorToCategoryInOut(cursor);
                categories.add(categoryInOut);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return categories;
    }

    @SuppressLint("Range")
    private CategoryInOut cursorToCategoryInOut(Cursor cursor) {
        Category category = new Category();
        category.setId(cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_ID)));
        category.setName(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_NAME)));
        category.setIcon(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ICON))); // Thêm dòng này
        category.setNote(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_NOTE)));

        int parentId = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_PARENT_ID));
        if (parentId != 0) {
            Category parent = getCategoryById(parentId);
            category.setParent(parent);
        }

        CategoryInOut categoryInOut = new CategoryInOut();
        categoryInOut.setId(cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_ID)));
        categoryInOut.setIdInOut(cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_INOUT_ID)));
        categoryInOut.setCategory(category);

        return categoryInOut;
    }


    @SuppressLint("Range")
    private Category getCategoryById(int id) {
        String query = "SELECT * FROM " + DBHelper.TABLE_CATEGORY + " WHERE " + DBHelper.COLUMN_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});

        Category category = null;
        if (cursor.moveToFirst()) {
            category = new Category();
            category.setId(cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_ID)));
            category.setName(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_NAME)));
            category.setNote(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_NOTE)));
        }
        cursor.close();
        return category;
    }

    public long addCategory(Category category) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_NAME, category.getName());
        values.put(DBHelper.COLUMN_ICON, category.getIcon());
        values.put(DBHelper.COLUMN_NOTE, category.getNote());
        if (category.getParent() != null) {
            values.put(DBHelper.COLUMN_PARENT_ID, category.getParent().getId());
        } else {
            values.put(DBHelper.COLUMN_PARENT_ID, 0); // Nếu không có parent
        }

        return db.insert(DBHelper.TABLE_CATEGORY, null, values);
    }

    public boolean addCategoryInOut(CategoryInOut categoryInOut) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_CATEGORY_ID, categoryInOut.getCategory().getId());
        values.put(DBHelper.COLUMN_INOUT_ID, categoryInOut.getIdInOut());

        long result = db.insert(DBHelper.TABLE_CATEGORY_INOUT, null, values);
        return result != -1; // Trả về true nếu thêm thành công
    }


}