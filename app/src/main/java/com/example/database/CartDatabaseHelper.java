package com.example.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.fb.CartItem;

import java.util.ArrayList;
import java.util.List;

/**
 * SQLite helper quản lý giỏ hàng local.
 * Giỏ hàng tồn tại trên thiết bị, không cần internet.
 */
public class CartDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME    = "cart.db";
    private static final int    DB_VERSION = 1;

    private static final String TABLE        = "cart";
    private static final String COL_ID       = "id";
    private static final String COL_PROD_ID  = "productId";
    private static final String COL_PROD_NAME= "productName";
    private static final String COL_PRICE    = "price";
    private static final String COL_QTY      = "quantity";
    private static final String COL_IMG      = "imageUrl";

    private static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE + " (" +
            COL_ID       + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_PROD_ID  + " TEXT UNIQUE, " +
            COL_PROD_NAME+ " TEXT, " +
            COL_PRICE    + " REAL, " +
            COL_QTY      + " INTEGER, " +
            COL_IMG      + " TEXT)";

    public CartDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override public void onCreate(SQLiteDatabase db)                          { db.execSQL(CREATE_TABLE); }
    @Override public void onUpgrade(SQLiteDatabase db, int o, int n)          { db.execSQL("DROP TABLE IF EXISTS " + TABLE); onCreate(db); }

    // ────────────────────────────────────────────────────
    //  WRITE
    // ────────────────────────────────────────────────────

    /**
     * Thêm sản phẩm vào giỏ. Nếu đã có thì cộng thêm số lượng.
     */
    public void addOrUpdateItem(CartItem item) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.query(TABLE,
                new String[]{COL_QTY},
                COL_PROD_ID + "=?",
                new String[]{item.getProductId()},
                null, null, null);

        if (c != null && c.moveToFirst()) {
            int cur = c.getInt(c.getColumnIndexOrThrow(COL_QTY));
            c.close();
            ContentValues cv = new ContentValues();
            cv.put(COL_QTY, cur + item.getQuantity());
            db.update(TABLE, cv, COL_PROD_ID + "=?", new String[]{item.getProductId()});
        } else {
            if (c != null) c.close();
            ContentValues cv = new ContentValues();
            cv.put(COL_PROD_ID,   item.getProductId());
            cv.put(COL_PROD_NAME, item.getProductName());
            cv.put(COL_PRICE,     item.getPrice());
            cv.put(COL_QTY,       item.getQuantity());
            cv.put(COL_IMG,       item.getImageUrl() != null ? item.getImageUrl() : "");
            db.insert(TABLE, null, cv);
        }
        db.close();
    }

    /**
     * Cập nhật số lượng. Nếu quantity <= 0 thì xóa khỏi giỏ.
     */
    public void updateQuantity(String productId, int quantity) {
        SQLiteDatabase db = getWritableDatabase();
        if (quantity <= 0) {
            db.delete(TABLE, COL_PROD_ID + "=?", new String[]{productId});
        } else {
            ContentValues cv = new ContentValues();
            cv.put(COL_QTY, quantity);
            db.update(TABLE, cv, COL_PROD_ID + "=?", new String[]{productId});
        }
        db.close();
    }

    /** Xóa 1 sản phẩm khỏi giỏ */
    public void removeItem(String productId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE, COL_PROD_ID + "=?", new String[]{productId});
        db.close();
    }

    /** Xóa toàn bộ giỏ hàng */
    public void clearCart() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE);
        db.close();
    }

    // ────────────────────────────────────────────────────
    //  READ
    // ────────────────────────────────────────────────────

    /** Lấy tất cả sản phẩm trong giỏ */
    public List<CartItem> getAllItems() {
        List<CartItem> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE, null, null, null, null, null, COL_ID + " ASC");
        if (c != null) {
            while (c.moveToNext()) {
                CartItem item = new CartItem();
                item.setId(c.getInt(c.getColumnIndexOrThrow(COL_ID)));
                item.setProductId(c.getString(c.getColumnIndexOrThrow(COL_PROD_ID)));
                item.setProductName(c.getString(c.getColumnIndexOrThrow(COL_PROD_NAME)));
                item.setPrice(c.getDouble(c.getColumnIndexOrThrow(COL_PRICE)));
                item.setQuantity(c.getInt(c.getColumnIndexOrThrow(COL_QTY)));
                item.setImageUrl(c.getString(c.getColumnIndexOrThrow(COL_IMG)));
                list.add(item);
            }
            c.close();
        }
        db.close();
        return list;
    }

    /** Tổng tiền giỏ hàng */
    public double getTotal() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT SUM(" + COL_PRICE + "*" + COL_QTY + ") FROM " + TABLE, null);
        double total = 0;
        if (c != null && c.moveToFirst()) { total = c.getDouble(0); c.close(); }
        db.close();
        return total;
    }

    /** Tổng số lượng sản phẩm (để hiện badge trên icon giỏ hàng) */
    public int getTotalQuantity() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT SUM(" + COL_QTY + ") FROM " + TABLE, null);
        int qty = 0;
        if (c != null && c.moveToFirst()) { qty = c.getInt(0); c.close(); }
        db.close();
        return qty;
    }

    /** Số dòng (số loại sản phẩm) trong giỏ */
    public int getDistinctCount() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM " + TABLE, null);
        int cnt = 0;
        if (c != null && c.moveToFirst()) { cnt = c.getInt(0); c.close(); }
        db.close();
        return cnt;
    }
}
