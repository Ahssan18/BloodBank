package com.ommi.bloodbank.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.ommi.bloodbank.MySingleton;
import com.ommi.bloodbank.PrefrenceManager;
import com.ommi.bloodbank.R;
import com.ommi.bloodbank.ServicesUrls;
import com.ommi.bloodbank.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VerifyCodeActivity extends AppCompatActivity {

    private PinView pinView;
    private Button Varify;
    private ImageView ivBack;
    private String userId;
    private String number;
    private FirebaseAuth mAuth;
    private String verificationId;
    private KProgressHUD progressHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_code);
        Utils.initLoader(this);
        FirebaseApp.initializeApp(this);
        try {
            userId = getIntent().getExtras().getString("user_id");
            number = getIntent().getExtras().getString("number");
            verificationId = getIntent().getExtras().getString("verification_id");
        } catch (Exception e) {
            e.printStackTrace();
        }
        mAuth = FirebaseAuth.getInstance();
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
        Varify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = pinView.getText().toString();
                if (code.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Enter Valid Code!", Toast.LENGTH_SHORT).show();
                } else {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            userCheckApi();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void userCheckApi() {
        progressHUD.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServicesUrls.USER_CHECK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Userchecvk", response + "_");
                progressHUD.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.getInt("status");
                    if (status == 1) {
                        String type = jsonObject.getString("type");
                        if (type.equalsIgnoreCase("login")) {
                            JSONObject user = jsonObject.getJSONObject("user");
                            Context context = VerifyCodeActivity.this;
                            String id, name, image, phone, country, Code, Gender, deviceToken, city, address, latitude, longitude, age, blood_type, types;
                            id = user.getString("id");
                            name = user.getString("name");
                            image = user.getString("image");
                            phone = user.getString("phone");
                            country = user.getString("country");
                            city = user.getString("city");
                            address = user.getString("address");
                            latitude = user.getString("latitude");
                            longitude = user.getString("longitude");
                            Gender = user.getString("gender");
                            Code = user.getString("country_code");
                            age = user.getString("age");
                            blood_type = user.getString("blood_type");
                            deviceToken = user.getString("type");
                            types = user.getString("type");
                            PrefrenceManager.setUserId(context, id);
                            PrefrenceManager.setUserName(context, name);
                            PrefrenceManager.setDecviceToken(context, deviceToken);
                            PrefrenceManager.setUserImage(context, image);
                            PrefrenceManager.setUserPhone(context, phone);
                            PrefrenceManager.setUserCountry(context, country);
                            PrefrenceManager.setUserCity(context, city);
                            PrefrenceManager.setUserAddress(context, address);
                            PrefrenceManager.setUserLattitude(context, latitude);
                            PrefrenceManager.setUserLongitude(context, longitude);
                            PrefrenceManager.setUserAge(context, age);
                            PrefrenceManager.setUserBloodGroup(context, blood_type);
                            PrefrenceManager.setUsertype(context, types);
                            PrefrenceManager.setCountryCode(context, Code);
                            PrefrenceManager.setGender(context, Gender);
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.putExtra("src","main");
                            startActivity(intent);
                            finish();
                        } else if (type.equalsIgnoreCase("registered")) {
                            String user_id = jsonObject.getString("user_id");
                            PrefrenceManager.setUserId(VerifyCodeActivity.this, user_id);
                            Intent intent = new Intent(VerifyCodeActivity.this, ProfileImageActivity.class);
                            intent.putExtra("user_id", user_id);
                            intent.putExtra("phone", number);
                            intent.putExtra("type", "register");
                            startActivity(intent);
                            finish();
                        }

                    } else {
                        startActivity(new Intent(VerifyCodeActivity.this, BlockActivity.class));
                        Toast.makeText(VerifyCodeActivity.this, "" + jsonObject.getString("message")+"from admin try with another number", Toast.LENGTH_LONG).show();
                        finish();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressHUD.dismiss();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressHUD.dismiss();
                error.printStackTrace();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("phone", number);
                hashMap.put("token", PrefrenceManager.getDeviceToken(VerifyCodeActivity.this));
                return hashMap;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void init() {
        pinView = findViewById(R.id.firstPinView);
        ivBack = findViewById(R.id.iv_back);
        Varify = findViewById(R.id.btn_submit);
        progressHUD = KProgressHUD.create(VerifyCodeActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
