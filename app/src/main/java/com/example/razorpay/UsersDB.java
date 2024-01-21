package com.example.razorpay;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.database.Cursor;
public class UsersDB extends SQLiteOpenHelper {
    public UsersDB(@Nullable Context context) {
        super(context, "test.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users (" +
                "phone Text PRIMARY KEY, " +
                "password Text)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS users");
    }

    public Boolean insert_users(String phone, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content_value = new ContentValues();
        content_value.put("phone", phone);
        content_value.put("password", password);
        System.out.println("Content value" + content_value);
        Long result = db.insert("users", null, content_value);
        System.out.println("Result is" + result);
        if(result == -1) return false;
        return true;
    }

    public Cursor retrieve_users(String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor record = db.rawQuery("SELECT * FROM users WHERE phone = ?",
                new
                        String []{phone});
        return record;
    }


    public Cursor retrieve_all_users() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor records = db.rawQuery("SELECT * FROM users", null);
        return records;
    }
}

