package com.example.razorpay;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SetUPIActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText pinEditText;
    private EditText confirmPinEditText;
    private Button setUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_signup);

        usernameEditText = findViewById(R.id.username);
        pinEditText = findViewById(R.id.pin);
        confirmPinEditText = findViewById(R.id.confirmPin);
        setUpButton = findViewById(R.id.setUpButton);

        setUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phone=getIntent().getStringExtra("phone");
                String password=getIntent().getStringExtra("password");
                String username = usernameEditText.getText().toString();
                String pin = pinEditText.getText().toString();
                String confirmPin = confirmPinEditText.getText().toString();

                if (pin.equals(confirmPin) && pin.length()==4 && !username.isEmpty()) {
                    UsersDB db1 = new
                            UsersDB(getApplicationContext());
                    db1.insert_users(phone, password);

                    UpiDB db = new
                            UpiDB(getApplicationContext());
                    db.insert_upi(phone,username,pin);

                    Toast.makeText(SetUPIActivity.this,"Set Up Successful", Toast.LENGTH_SHORT).show();
                    Intent k = new Intent(SetUPIActivity.this,
                            HomeActivity.class);
                    k.putExtra("phone",phone);
                    startActivity(k);
                }
                else if(pin.length()!=4){
                    Toast.makeText(SetUPIActivity.this,"Enter 4 digit pin", Toast.LENGTH_SHORT).show();
                }
                else if(username.isEmpty()){
                    Toast.makeText(SetUPIActivity.this,"Unga amma appa unnaku peru vekalaya?", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(SetUPIActivity.this,"Pin does not match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
