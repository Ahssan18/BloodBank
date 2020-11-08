package com.ommi.bloodbank.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ommi.bloodbank.R;

public class AdminNotification extends AppCompatActivity {

    private ImageView ivback;
    private TextView tvTitle, tvMessage;
    String title = "", message = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_notification);
        title = getIntent().getStringExtra("title");
        message = getIntent().getStringExtra("message");
        init();
        clickListener();
    }

    private void clickListener() {
        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void init() {
        getSupportActionBar().hide();
        tvTitle = findViewById(R.id.tv_title);
        tvMessage = findViewById(R.id.tv_message);
        ivback = findViewById(R.id.iv_back);
        tvTitle.setText(title);
        tvMessage.setText(message);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}