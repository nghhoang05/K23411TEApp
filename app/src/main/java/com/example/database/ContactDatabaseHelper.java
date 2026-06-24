package com.example.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.models.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * SQLite helper quản lý bảng contacts dùng làm local cache khi offline.
 * Khi có internet: Firebase → insertOrReplace() → cache lại.
 * Khi offline: getAllContacts() → hiển thị dữ liệu đã cache.
 */
public class ContactDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME    = "contacts_cache.db";
    private static final int    DB_VERSION = 1;

    private static final String TABLE      = "contacts";
    private static final String COL_KEY    = "key";
    private static final String COL_NAME   = "name";
    private static final String COL_EMAIL  = "email";
    private static final String COL_PHONE  = "phone";

    private static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE + " (" +
            COL_KEY   + " TEXT PRIMARY KEY, " +
            COL_NAME  + " TEXT, " +
            COL_EMAIL + " TEXT, " +
            COL_PHONE + " TEXT)";

    public ContactDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    /**
     * Thêm mới hoặc cập nhật một contact (INSERT OR REPLACE).
     * Được gọi sau mỗi lần onDataChange() từ Firebase.
     */
    public void insertOrReplace(Contact contact) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_KEY,   contact.getKey());
        cv.put(COL_NAME,  contact.getName());
        cv.put(COL_EMAIL, contact.getEmail());
        cv.put(COL_PHONE, contact.getPhone());
        db.insertWithOnConflict(TABLE, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    /**
     * Lấy toàn bộ contacts từ local cache.
     * Dùng khi offline.
     */
    public List<Contact> getAllContacts() {
        List<Contact> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE, null, null, null, null, null, COL_NAME + " ASC");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String key   = cursor.getString(cursor.getColumnIndexOrThrow(COL_KEY));
                String name  = cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(COL_EMAIL));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow(COL_PHONE));
                list.add(new Contact(key, name, email, phone));
            }
            cursor.close();
        }
        db.close();
        return list;
    }

    /**
     * Xóa toàn bộ dữ liệu trong bảng.
     * Gọi trước khi sync lại từ Firebase để tránh dữ liệu thừa.
     */
    public void clearAll() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE);
        db.close();
    }

    /** Kiểm tra có dữ liệu cache chưa */
    public boolean hasData() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE, null);
        boolean hasData = false;
        if (cursor != null && cursor.moveToFirst()) {
            hasData = cursor.getInt(0) > 0;
            cursor.close();
        }
        db.close();
        return hasData;
    }
}
