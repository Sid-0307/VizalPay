package com.example.razorpay;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ScrollView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@RequiresApi(api = Build.VERSION_CODES.O)
public class HistoryActivity extends AppCompatActivity {

    private TableLayout tableLayout;
    final static DateTimeFormatter CUSTOM_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_history);

        String phone=getIntent().getStringExtra("phone");
        tableLayout = findViewById(R.id.tableLayout);

        TransactionDB db = new
                TransactionDB(getApplicationContext());
        Cursor record=db.retrieve_transaction(phone);

        while (record.moveToNext()) {

            if(record.getString(1).equals(phone)) {
                String formattedtime = LocalDateTime.parse(record.getString(0)).format(CUSTOM_FORMATTER);
                UpiDB db1 = new
                        UpiDB(getApplicationContext());
                Cursor record1= db1.retrieve_record(record.getString(2));

                String name="";
                while(record1.moveToNext()){
                    name=record1.getString(1);
                }
                addUserToTable(formattedtime,name, record.getString(2), record.getString(3),0);
            }

            else if(record.getString(2).equals(phone)){
                String formattedtime = LocalDateTime.parse(record.getString(0)).format(CUSTOM_FORMATTER);
                UpiDB db1 = new
                        UpiDB(getApplicationContext());
                Cursor record1= db1.retrieve_record(record.getString(1));
                String name="";
                while(record1.moveToNext()){
                    name=record1.getString(1);
                }
                addUserToTable(formattedtime, name,record.getString(1), record.getString(3),1);
            }
        }
    }

    private void addUserToTable(String date,String user, String phone,String amount,Integer val) {

        TableRow row = new TableRow(this);
        TableRow.LayoutParams params=new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT,2F
        );
        row.setPadding(16,16,16,16);
        params.setMargins(100,50,20,0);

        LinearLayout l=new LinearLayout(this);
        l.setOrientation(LinearLayout.VERTICAL);

        TextView dateTextView = new TextView(this);
        TextView userTextView = new TextView(this);
        TextView phoneTextView = new TextView(this);
        TextView amountTextView = new TextView(this);

        amountTextView.setLayoutParams(params);
        l.setLayoutParams(params);

        dateTextView.setText(date);
        dateTextView.setTextSize(10);

        userTextView.setText(user);
        userTextView.setTextSize(30);
        int color = Integer.parseInt("7711ff", 16)+0xFF000000;
        userTextView.setTextColor(color);

        phoneTextView.setText(phone);
        phoneTextView.setTextSize(20);

        amountTextView.setText(amount);
        amountTextView.setTextSize(30);
        amountTextView.setGravity(Gravity.CENTER);
        if(val.equals(0)){
            color = Integer.parseInt("ff0000", 16)+0xFF000000;
            amountTextView.setTextColor(color);
        }
        else{
            color = Integer.parseInt("00ff00", 16)+0xFF000000;
            amountTextView.setTextColor(color);
        }

        l.addView(userTextView);
        l.addView(phoneTextView);
        l.addView(dateTextView);

        row.addView(l);
        row.addView(amountTextView);

        tableLayout.addView(row);
    }
}