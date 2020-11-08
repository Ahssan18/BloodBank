package com.ommi.bloodbank.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ommi.bloodbank.BuildConfig;
import com.ommi.bloodbank.MySingleton;
import com.ommi.bloodbank.PrefrenceManager;
import com.ommi.bloodbank.R;
import com.ommi.bloodbank.ServicesUrls;
import com.ommi.bloodbank.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvProfile, tvContactus, tvAboutus, tvRate, tvShare, tvPrivacy, tvLogout, tvDelete;
    private ImageView ivBack;
    private ScrollView scrollView;
    private Animation animation;
    private LinearLayout linearProfile, linearDelete, linearContactus, linearAboutus, linearRateus, linearShare, linearPrivacy, linearLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportActionBar().hide();
        init();
        clickListener();
    }

    private void clickListener() {
        tvProfile.setOnClickListener(this);
        tvContactus.setOnClickListener(this);
        tvAboutus.setOnClickListener(this);
        tvRate.setOnClickListener(this);
        tvShare.setOnClickListener(this);
        tvPrivacy.setOnClickListener(this);
        tvLogout.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        tvDelete.setOnClickListener(this);
    }

    private void init() {
        Utils.initLoader(this);
        linearProfile = findViewById(R.id.linear_myprofile);
        linearDelete = findViewById(R.id.linear_delete);
        linearContactus = findViewById(R.id.linear_contactus);
        linearAboutus = findViewById(R.id.linear_aboutus);
        linearRateus = findViewById(R.id.linear_rateus);
        linearPrivacy = findViewById(R.id.linear_privacy);
        linearShare = findViewById(R.id.linear_share);
        linearLogout = findViewById(R.id.linear_logout);
        if (PrefrenceManager.getDefaultLang(SettingActivity.this).equalsIgnoreCase("arabic")) {
            animation = AnimationUtils.loadAnimation(SettingActivity.this, android.R.anim.slide_in_left);
        } else {
            animation = AnimationUtils.loadAnimation(SettingActivity.this, R.anim.right_to_left);
        }
        animation.setDuration(1500);
        linearProfile.startAnimation(animation);
        linearContactus.startAnimation(animation);
        linearAboutus.startAnimation(animation);
        linearRateus.startAnimation(animation);
        linearDelete.startAnimation(animation);
        linearPrivacy.startAnimation(animation);
        linearShare.startAnimation(animation);
        linearLogout.startAnimation(animation);
        ivBack = findViewById(R.id.iv_back);
        tvProfile = findViewById(R.id.tv_myprofile);
        tvContactus = findViewById(R.id.tv_contacus);
        tvAboutus = findViewById(R.id.tv_aboutus);
        tvRate = findViewById(R.id.tv_rateus);
        tvShare = findViewById(R.id.tv_share);
        tvPrivacy = findViewById(R.id.tv_privacypolict);
        tvDelete = findViewById(R.id.tv_delete_account);
        tvLogout = findViewById(R.id.tv_logout);
        if (PrefrenceManager.getDefaultLang(SettingActivity.this).equalsIgnoreCase("arabic")) {
            tvProfile.setGravity(Gravity.RIGHT | Gravity.CENTER);
            tvContactus.setGravity(Gravity.RIGHT | Gravity.CENTER);
            tvAboutus.setGravity(Gravity.RIGHT | Gravity.CENTER);
            tvRate.setGravity(Gravity.RIGHT | Gravity.CENTER);
            tvShare.setGravity(Gravity.RIGHT | Gravity.CENTER);
            tvPrivacy.setGravity(Gravity.RIGHT | Gravity.CENTER);
            tvLogout.setGravity(Gravity.RIGHT | Gravity.CENTER);
            tvDelete.setGravity(Gravity.RIGHT | Gravity.CENTER);

        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();


        switch (id) {
            case R.id.tv_myprofile:
                Intent intent = new Intent(SettingActivity.this, ProfileImageActivity.class);
                intent.putExtra("ref", "myprofile");
                intent.putExtra("type", "update");
                startActivity(intent);
                break;
            case R.id.tv_contacus:
                startActivity(new Intent(SettingActivity.this, ContactUsActivity.class));
                break;
            case R.id.tv_delete_account:
                LinearLayout delete, cancel;
                final View dialogView = LayoutInflater.from(this).inflate(R.layout.delete_user, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();
                delete = dialogView.findViewById(R.id.linear_delete);
                cancel = dialogView.findViewById(R.id.linear_cancel);
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteAccountApiCall();
                        alertDialog.dismiss();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();

                break;
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_aboutus:
                startActivity(new Intent(SettingActivity.this, AboutUsActivity.class));
                break;
            case R.id.tv_rateus:
                alertDialogue();
                break;
            case R.id.tv_share:
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                    String shareMessage = "\nLet me recommend you this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {
                    //e.toString();
                }
                break;
            case R.id.tv_privacypolict:
                Intent browserIntent = new Intent(getApplicationContext(), PrivacyPolicyActivity.class
                );
                startActivity(browserIntent);
                break;
            case R.id.tv_logout:
                String lang = PrefrenceManager.getDefaultLang(SettingActivity.this);
                PrefrenceManager.getDefaultSharedPrefrence(SettingActivity.this).edit().clear().apply();
                PrefrenceManager.setDefaultLang(SettingActivity.this, lang);
                finishAffinity();
                Intent intent1 = new Intent(SettingActivity.this, EnterPhoneActivity.class);
                FirebaseMessaging.getInstance().unsubscribeFromTopic(PrefrenceManager.getUserId(SettingActivity.this));
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
                finish();
                break;
        }
    }

    private void deleteAccountApiCall() {
        try {
            Utils.showProgress(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServicesUrls.Delete_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Utils.dismissProgress();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");
                    String message = jsonObject.getString("message");

                    if (status) {
                        PrefrenceManager.getDefaultSharedPrefrence(SettingActivity.this).edit().clear().apply();
                        finishAffinity();
                        Intent intent1 = new Intent(SettingActivity.this, EnterPhoneActivity.class);
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(PrefrenceManager.getUserId(SettingActivity.this));
                        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent1);
                        finish();
                    } else {
                        Toast.makeText(SettingActivity.this, "" + message, Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    Utils.dismissProgress();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("user_id", PrefrenceManager.getUserId(SettingActivity.this));
                return hashMap;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void getPointApiCall() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServicesUrls.GET_POINTS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.getInt("status");
                    if (status == 1) {

                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("user_id", PrefrenceManager.getUserId(SettingActivity.this));
                return hashMap;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void alertDialogue() {
        Log.d("alert_dialog", "true");
        LinearLayout rate, share, exit;
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.custom_rate_share, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        rate = dialogView.findViewById(R.id.linear_rate);
        share = dialogView.findViewById(R.id.linear_share);
        exit = dialogView.findViewById(R.id.linear_exit);
        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPointApiCall();
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                alertDialog.dismiss();
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

}
