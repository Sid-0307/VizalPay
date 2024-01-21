package com.example.razorpay;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Bundle;
import android.database.Cursor;

import java.time.LocalDateTime;
import java.time.LocalDateTime;
public class TransactionDB extends SQLiteOpenHelper {
    public TransactionDB(@Nullable Context context) {
        super(context, "test3.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE transactions (" +
                "date Text PRIMARY KEY, "+
                "sender Text , " +
                "receiver Text,"+
                "balance Text)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS transactions");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Boolean insert_transaction(String sender, String receiver, String balance) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content_value = new ContentValues();
        content_value.put("date", String.valueOf(LocalDateTime.now()));
        content_value.put("sender", sender);
        content_value.put("receiver", receiver);
        content_value.put("balance", balance);
        System.out.println("Content value" + content_value);

        Long result = db.insert("transactions", null, content_value);
        System.out.println("Result is" + result);
        if(result == -1) return false;
        return true;
    }


    public Cursor retrieve_transaction(String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor record = db.rawQuery("SELECT * FROM transactions WHERE sender = ? OR receiver=? ORDER BY date DESC", new String []{phone,phone});
        return record;
    }
    
}

