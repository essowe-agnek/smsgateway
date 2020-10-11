package com.agnekdev.smsgateway;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView mContent,mSenderNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContent=findViewById(R.id.content);
        mSenderNumber=findViewById(R.id.sender);

        Intent intent=getIntent();
        String msg=intent.getStringExtra("msg");
        String sender=intent.getStringExtra("sender");

        mContent.setText(msg);
        mSenderNumber.setText(sender);
    }

}
