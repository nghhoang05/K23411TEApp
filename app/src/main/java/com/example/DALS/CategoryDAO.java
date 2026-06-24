package com.example.DALS;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.models.Category;

import java.util.ArrayList;

public class CategoryDAO {
    public static final String DATABASE_NAME = "K23411TEDSales.sqlite";
    public static final String TABLE_NAME = "Category";

    public static SQLiteDatabase database = null;

    public static ArrayList<Category> getCategories(Context context) {
        ArrayList<Category> categories = new ArrayList<>();
        try {
            database = context.openOrCreateDatabase(DATABASE_NAME, context.MODE_PRIVATE, null);
            
            // Debug: List all tables
            Cursor c = database.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name NOT LIKE 'android_%'", null);
            android.util.Log.d("DATABASE_DEBUG", "Found tables:");
            while (c.moveToNext()) {
                android.util.Log.d("DATABASE_DEBUG", "Table: " + c.getString(0));
            }
            c.close();

            // Check if Category table exists
            Cursor checkTable = database.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='" + TABLE_NAME + "'", null);
            boolean exists = checkTable.getCount() > 0;
            checkTable.close();

            if (!exists) {
                android.util.Log.e("DATABASE_DEBUG", "Table " + TABLE_NAME + " DOES NOT EXIST!");
                return categories;
            }

            Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME, null);
            while(cursor.moveToNext()){
                String cateId = cursor.getString(0);
                String cateName = cursor.getString(1);
                String description = cursor.getString(2);
                Category c1 = new Category(cateId, cateName, description);
                categories.add(c1);
            }
            cursor.close();
        } catch (Exception e) {
            android.util.Log.e("DATABASE_DEBUG", "Error query categories: " + e.getMessage());
            e.printStackTrace();
        }
        return categories;
    }
    public static long saveNewCategory(Context context, Category category)
    {
        long result=-1;
        database=context.openOrCreateDatabase(DATABASE_NAME, context.MODE_PRIVATE, null);

        ContentValues values=new ContentValues();
        values.put("CategoryId",category.getCategoryId());
        values.put("CategoryName",category.getCategoryName());
        values.put("Dcription",category.getDescription());
        result=database.insert(TABLE_NAME,null,values);
        return result;
    }

    public static long removeNewCategory(Context context, Category category)
    {
        long result=-1;
        database=context.openOrCreateDatabase(DATABASE_NAME, context.MODE_PRIVATE, null);

        result=database.delete(TABLE_NAME,"CategoryId=?", new String[]{category.getCategoryId()});
        return result;
    }

}
