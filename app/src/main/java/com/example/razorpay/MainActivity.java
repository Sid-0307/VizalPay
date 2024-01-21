package com.example.razorpay;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        EditText phoneEditText =findViewById(R.id.phoneNumber);
        EditText passwordEditText =findViewById(R.id.password);
        Button insert = (Button) findViewById(R.id.loginButton);


        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phone = phoneEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                Pattern ptrn = Pattern.compile("[1-9][0-9]{9}");
                Matcher match = ptrn.matcher(phone);
                if ((phone.length()==10) && match.find() && match.group().equals(phone)){
                    UsersDB db = new
                            UsersDB(getApplicationContext());
                    Cursor record=db.retrieve_users(phone);
                    if(record.getCount()!=1) {
                        Toast.makeText(MainActivity.this,"Welcome to VizalPay", Toast.LENGTH_SHORT).show();
                        Intent j = new Intent(MainActivity.this,
                                SetUPIActivity.class);
                        j.putExtra("phone",phone);
                        j.putExtra("password",password);
                        startActivity(j);
                    }
                    else{
                        if(record.getCount()==1){
                            while(record.moveToNext()){
                                String p1=record.getString(1);
                                if(p1.equals(password)){
                                    Toast.makeText(MainActivity.this,"Good to see you back", Toast.LENGTH_SHORT).show();
                                    Intent j = new Intent(MainActivity.this,
                                            HomeActivity.class);
                                    j.putExtra("phone",phone);
                                    startActivity(j);
                                }
                                else{
                                    Toast.makeText(MainActivity.this,"Credentials Invalid", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                }

                else{
                    Toast.makeText(MainActivity.this,"Invalid Number", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}