package com.ommi.bloodbank.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ommi.bloodbank.PrefrenceManager;
import com.ommi.bloodbank.R;

public class PrivacyPolicyActivity extends AppCompatActivity {
    private ImageView ivBack;
    private TextView privacy1,privacy3,privacy4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        getSupportActionBar().hide();
        init();
        clickListener();
    }
    private void clickListener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void init() {
        ivBack=findViewById(R.id.iv_back);
        privacy1=findViewById(R.id.privacy1);
        privacy3=findViewById(R.id.privacy3);
        privacy4=findViewById(R.id.tv_privacy3);
        if(PrefrenceManager.getDefaultLang(PrivacyPolicyActivity.this).equalsIgnoreCase("french"))
        {
            privacy1.setPadding(0,0,10,10);
            privacy3.setPadding(0,10,10,10);
        }else  if(PrefrenceManager.getDefaultLang(PrivacyPolicyActivity.this).equalsIgnoreCase("arabic"))
        {
            privacy1.setPadding(0,0,10,10);
            privacy4.setPadding(0,10,10,0);

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
