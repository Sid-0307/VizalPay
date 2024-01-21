package com.example.razorpay;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    private TextView usernameTextView,logout;
    private TextView balanceTextView;
    private Button addCreditButton,makeTransactionButton,viewTransactionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_home);

        usernameTextView=findViewById(R.id.username);
        balanceTextView=findViewById(R.id.balance);
        logout=findViewById(R.id.logout);
        addCreditButton=(Button)findViewById(R.id.addCreditsButton);
        makeTransactionButton=(Button) findViewById(R.id.makeTransactionButton);
        viewTransactionButton=(Button) findViewById(R.id.viewTransactionButton);

        String phone=getIntent().getStringExtra("phone");
        UpiDB db = new
                UpiDB(getApplicationContext());
        Cursor record=db.retrieve_record(phone);
        while(record.moveToNext()){
            usernameTextView.setText(record.getString(1));
            balanceTextView.setText(record.getString(3));
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent k = new Intent(HomeActivity.this,
                        MainActivity.class);
                Toast.makeText(HomeActivity.this, "Successfully Logged out", Toast.LENGTH_SHORT).show();
                k.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(k);
            }
        });
        addCreditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k = new Intent(HomeActivity.this,
                        AddCreditActivity.class);
                k.putExtra("phone",phone);
                startActivity(k);
            }
        });

        makeTransactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k = new Intent(HomeActivity.this,MakeTransactionActivity.class);
                k.putExtra("phone",phone);
                startActivity(k);
            }
        });

        viewTransactionButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                Intent k = new Intent(HomeActivity.this,HistoryActivity.class);
                k.putExtra("phone",phone);
                startActivity(k);
            }
        });
    }
}