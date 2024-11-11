package com.habibi.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "barang.db";
    private static final String TABLE_USERS = "users";
    private static final String TABLE_BARANG = "barang";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
db.execSQL("CREATE TABLE "+TABLE_USERS+"(id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT)");
db.execSQL("CREATE TABLE "+TABLE_BARANG+"(id INTEGER PRIMARY KEY AUTOINCREMENT, KodeBarang TEXT, NamaBarang TEXT, Merk TEXT, HargaBarang DECIMAL, JumlahStok INTEGER)");

    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BARANG);
        onCreate(db);
    }
    public void addDefaultUser() {
        // Ensure this is called after the database has been created
        insertUser("testUser", "testPass"); // Change these values as needed
    }
    public boolean checkUserExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{"username"}, "username = ?",
                new String[]{username}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }
    // Insert user
    public boolean insertUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        long result = db.insert(TABLE_USERS, null, contentValues);
        return result != -1;
    }
    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USERS, null);
    }
    // Check login credentials
    public boolean checkLogin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE username=? AND password=?", new String[]{username, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        // Log the credentials being checked
        Log.d("DatabaseHelper", "Checking login for: " + username + ", " + password + ", exists: " + exists);
        return exists;
    }
    // CRUD for students
    public boolean insertBarang(String KodeBarang, String NamaBarang, String Merk, Integer HargaBarang, Integer JumlahStok) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("KodeBarang", KodeBarang);
        contentValues.put("NamaBarang", NamaBarang);
        contentValues.put("Merk", Merk);
        contentValues.put("HargaBarang", HargaBarang);
        contentValues.put("JumlahStok", JumlahStok);
        long result = db.insert(TABLE_BARANG, null, contentValues);
        return result != -1;
    }
    public Cursor getAllBarang() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT id AS _id, KodeBarang, NamaBarang, Merk, HargaBarang, JumlahStok FROM " + TABLE_BARANG, null);
    }
    public boolean updateBarang(int id, String KodeBarang, String NamaBarang, String Merk, Integer HargaBarang, Integer JumlahStok) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("KodeBarang", KodeBarang);
        contentValues.put("NamaBarang", NamaBarang);
        contentValues.put("Merk", Merk);
        contentValues.put("HargaBarang", HargaBarang);
        contentValues.put("JumlahStok", JumlahStok);
        long result = db.update(TABLE_BARANG, contentValues, "id=?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    public Integer deleteBarang(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_BARANG, "id=?", new String[]{String.valueOf(id)});
    }
}
