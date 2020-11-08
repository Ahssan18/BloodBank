package com.ommi.bloodbank.Activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.ommi.bloodbank.PrefrenceManager;
import com.ommi.bloodbank.R;

import java.util.Locale;

public class ChooseLanguageActivity extends AppCompatActivity implements View.OnClickListener {

    private MaterialButton btnNext, btnEnglish, btnArabic, btnFrench;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_language);
        init();
        clickListener();
    }

    private void clickListener() {
        btnNext.setOnClickListener(this);
        btnArabic.setOnClickListener(this);
        btnEnglish.setOnClickListener(this);
        btnFrench.setOnClickListener(this);

    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();
        getSupportActionBar().hide();
        btnNext = findViewById(R.id.btn_next);
        btnArabic = findViewById(R.id.btn_arbic);
        btnFrench = findViewById(R.id.btn_french);
        btnEnglish = findViewById(R.id.btn_english);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                String lang = null;
                String Language = PrefrenceManager.getDefaultLang(ChooseLanguageActivity.this);
                if (Language.equalsIgnoreCase("English")) {
                    lang = "en";
                } else if (Language.equalsIgnoreCase("French")) {
                    lang = "fr";
                } else if (Language.equalsIgnoreCase("Arabic")) {
                    lang = "ar";
                }
                Resources resources = getResources();
                DisplayMetrics dm = resources.getDisplayMetrics();
                Configuration config = resources.getConfiguration();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    config.setLocale(new Locale(lang.toLowerCase()));
                } else {
                    config.locale = new Locale(lang.toLowerCase());
                }
                resources.updateConfiguration(config, dm);
                mAuth.signInWithEmailAndPassword("m.redachellali@gmail.com", "123456789")
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(ChooseLanguageActivity.this, EnterPhoneActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Login failed! Please try again later", Toast.LENGTH_LONG).show();
                               }
                            }
                        });
               /* mAuth.createUserWithEmailAndPassword("m.redachellali@gmail.com", "123456789")
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
//                                    Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_LONG).show();



                                } else {
                                    Toast.makeText(getApplicationContext(), "Registration failed! Please try again later", Toast.LENGTH_LONG).show();
//                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });*/
                break;
            case R.id.btn_arbic:
                btnArabic.setBackgroundColor(getResources().getColor(R.color.colorRed));
                btnArabic.setTextColor(Color.WHITE);
                btnFrench.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                btnFrench.setTextColor(getResources().getColor(R.color.colorRed));
                btnEnglish.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                btnEnglish.setTextColor(getResources().getColor(R.color.colorRed));
                PrefrenceManager.setDefaultLang(ChooseLanguageActivity.this, "Arabic");

                break;
            case R.id.btn_french:
                btnFrench.setBackgroundColor(getResources().getColor(R.color.colorRed));
                btnFrench.setTextColor(Color.WHITE);
                btnArabic.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                btnArabic.setTextColor(getResources().getColor(R.color.colorRed));
                btnEnglish.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                btnEnglish.setTextColor(getResources().getColor(R.color.colorRed));
                PrefrenceManager.setDefaultLang(ChooseLanguageActivity.this, "French");
                break;
            case R.id.btn_english:
                btnEnglish.setBackgroundColor(getResources().getColor(R.color.colorRed));
                btnEnglish.setTextColor(Color.WHITE);
                btnArabic.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                btnArabic.setTextColor(getResources().getColor(R.color.colorRed));
                btnFrench.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                btnFrench.setTextColor(getResources().getColor(R.color.colorRed));
                PrefrenceManager.setDefaultLang(ChooseLanguageActivity.this, "English");
                break;
        }
    }
}