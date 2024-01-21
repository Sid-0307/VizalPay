package com.example.razorpay;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.database.Cursor;
public class UpiDB extends SQLiteOpenHelper {
    public UpiDB(@Nullable Context context) {
        super(context, "test1.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE upi (" +
                "phone Text PRIMARY KEY, " +
                "username Text,"+
                "password Text," +
                "balance Real)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS upi");
    }

    public Boolean insert_upi(String phone,String username,String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content_value = new ContentValues();
        content_value.put("phone", phone);
        content_value.put("username", username);
        content_value.put("password", password);
        content_value.put("balance", 0.00);
        System.out.println("Content value" + content_value);

        Long result = db.insert("upi", null, content_value);
        System.out.println("Result is" + result);
        if(result == -1) return false;
        return true;
    }


    //Updation
    public Boolean update_upi(String phone, Integer amount) {
        Integer balance=amount;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content_value = new ContentValues();
        Cursor record = db.rawQuery("SELECT * FROM upi WHERE phone = ?",
                new
                        String []{phone});
        if(record.getCount() > 0)
        {
            while(record.moveToNext()){
                balance+=Integer.valueOf(record.getString(3));
                System.out.println("Balance is "+balance);
            }
            System.out.println("Final Balance is "+balance);
            content_value.put("balance", balance);
            int result = db.update("upi", content_value, "phone = ?", new
                    String []{phone});
            if(result == -1) return false;
            else return true;
        }
        return false;
    }


    public Cursor retrieve_record(String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor record = db.rawQuery("SELECT * FROM upi WHERE phone = ?", new String []{phone});
        return record;
    }


    public Cursor retrieve_all_records() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor records = db.rawQuery("SELECT * FROM upi", null);
        return records;
    }
}

