package com.ommi.bloodbank.Activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.ommi.bloodbank.PrefrenceManager;
import com.ommi.bloodbank.R;

import java.util.Locale;

public class Splash extends AppCompatActivity {

    private ImageView ivSplash;
    private String lang, Language;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        init();
        Language = PrefrenceManager.getDefaultLang(Splash.this);
        if (!PrefrenceManager.getDefaultLang(Splash.this).equals("")) {
            Log.d("OutPut", "if" + "_");
            if (Language.equalsIgnoreCase("English")) {
                lang = "en";
            } else if (Language.equalsIgnoreCase("French")) {
                lang = "fr";
            } else if (Language.equalsIgnoreCase("Arabic")) {
                lang = "ar";
            }
        } else {
            lang = Locale.getDefault().getLanguage();
            Log.d("OutPut", "else" + "_" + lang);
            if (lang.equals("ar")) {
                lang = "ar";

            } else if (lang.equals("fr")) {
                lang = "fr";

            } else {
                lang = "en";

            }
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
        ScaleAnimation scal = new ScaleAnimation(0, 1f, 0, 1f, Animation.RELATIVE_TO_SELF, (float) 0.5, Animation.RELATIVE_TO_SELF, (float) 0.5);
        scal.setDuration(2000);
        scal.setFillAfter(true);
        ivSplash.setAnimation(scal);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (PrefrenceManager.getUserId(Splash.this) != null) {
                    Intent intent = new Intent(Splash.this, MainActivity.class);
                    intent.putExtra("src", "main");
                    startActivity(intent);
                    finish();
                } else if (!PrefrenceManager.getDefaultLang(Splash.this).equals("")) {
                    startActivity(new Intent(Splash.this, EnterPhoneActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(Splash.this, ChooseLanguageActivity.class));
                    finish();
                }

            }
        }, 2000);


    }


    private void init() {
        ivSplash = findViewById(R.id.ivSplash);

    }
}

