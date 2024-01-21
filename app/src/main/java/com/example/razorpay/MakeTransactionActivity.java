package com.example.razorpay;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.style.BulletSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;


public class MakeTransactionActivity extends AppCompatActivity {
    private AutoCompleteTextView autoCompleteTextView;
    private ArrayList<String> allData = new ArrayList<String>();
    private TextView username;
    private EditText amountEditText,pinEditText;
    private Button payButton;
    private static final String CHANNEL_ID = "channel_id";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_transaction);


        String phone=getIntent().getStringExtra("phone");
        UpiDB db = new
                UpiDB(getApplicationContext());
        Cursor record= db.retrieve_all_records();
        while(record.moveToNext()){
            if(!record.getString(0).equals(phone))
                allData.add(record.getString(0));
        }

        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        username=findViewById(R.id.name);
        amountEditText=findViewById(R.id.amount);
        pinEditText=findViewById(R.id.pin);
        payButton=(Button)findViewById(R.id.payButton);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, allData);
        autoCompleteTextView.setThreshold(2);
        autoCompleteTextView.setAdapter(adapter);


        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String rec = (String) parent.getItemAtPosition(position);
                UpiDB db = new
                        UpiDB(getApplicationContext());
                Cursor record= db.retrieve_record(rec);
                while (record.moveToNext()) {
                    username.setText(record.getString(1));
                    }
            }
        });


        autoCompleteTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String rec = autoCompleteTextView.getText().toString();
                    UpiDB db = new
                            UpiDB(getApplicationContext());
                    Cursor record= db.retrieve_record(rec);
                    if(record.getCount()==0) {
                        username.setText("Invalid User");
                        payButton.setEnabled(false);
                        Toast.makeText(MakeTransactionActivity.this, "PayButton is disabled", Toast.LENGTH_SHORT).show();
                    }
                    else if(rec.equals(phone)){
                        username.setText("No self transaction");
                        payButton.setEnabled(false);
                        Toast.makeText(MakeTransactionActivity.this, "PayButton is disabled", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        while (record.moveToNext()) {
                            username.setText(record.getString(1));
                        }
                        payButton.setEnabled(true);
                    }
            }
        };
        });

        payButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                Integer amount=Integer.valueOf(amountEditText.getText().toString());
                String receiver=autoCompleteTextView.getText().toString();
                String pin=pinEditText.getText().toString();

                UpiDB db = new
                        UpiDB(getApplicationContext());
                Cursor record=db.retrieve_record(phone);
                while(record.moveToNext()){
                    if(record.getString(2).equals(pin) && Integer.valueOf(record.getString(3))-amount>0){
                        db.update_upi(phone,-amount);
                        db.update_upi(receiver,amount);

                        TransactionDB db1 = new
                                TransactionDB(getApplicationContext());
                        db1.insert_transaction(phone, receiver, String.valueOf(amount));

                        Toast.makeText(MakeTransactionActivity.this,"Payment Successful", Toast.LENGTH_SHORT).show();
                        showNotification("Debit INR "+amount+".00 from your account");
                        Intent i=new Intent(MakeTransactionActivity.this,HomeActivity.class);
                        i.putExtra("phone",phone);
                        startActivity(i);
                    }
                    else if(!record.getString(2).equals(pin)){
                        Toast.makeText(MakeTransactionActivity.this,"Invalid Pin", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(MakeTransactionActivity.this,"Avalo kaasu illa kumaru", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }

    public void showNotification(String message){
        String chanelID="Vizal";
        NotificationCompat.Builder builder=new NotificationCompat.Builder(getApplicationContext(),chanelID);
        builder.setSmallIcon(R.drawable.payment_notification)
                .setContentTitle("Payment Successful")
                .setContentText(message)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "My Notification Channel";
            String description = "Channel description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(chanelID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, builder.build());
    }


}