package com.ommi.bloodbank.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ommi.bloodbank.PrefrenceManager;
import com.ommi.bloodbank.R;

import static com.ommi.bloodbank.R.drawable.ic_launcher_background;

public class AboutUsActivity extends AppCompatActivity {
    private ImageView ivBack,ivSon,ivMother;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
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
        textView=findViewById(R.id.tv_para2);
        ivSon=findViewById(R.id.ivson);
        ivMother=findViewById(R.id.ivmother);
       if(PrefrenceManager.getDefaultLang(AboutUsActivity.this).equalsIgnoreCase("french"))
       {
           //textView.setPadding(20,0,10,0);
       }else if(PrefrenceManager.getDefaultLang(AboutUsActivity.this).equalsIgnoreCase("Arabic"))
       {
           ivMother.setBackgroundResource(R.drawable.son);
           ivSon.setBackgroundResource(R.drawable.mother);
       }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
