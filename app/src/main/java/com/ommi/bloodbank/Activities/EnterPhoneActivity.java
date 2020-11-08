package com.ommi.bloodbank.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.ommi.bloodbank.PrefrenceManager;
import com.ommi.bloodbank.R;
import com.rilixtech.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class EnterPhoneActivity extends AppCompatActivity {

    private Button btnSendCode;
    CountryCodePicker ccp;
    private TextView tvPrivacy;
    private KProgressHUD progressHUD;
    private AppCompatEditText etPhone;
    private PhoneAuthProvider.ForceResendingToken mResendToken = null;
    private FirebaseAuth mAuth;
    private KProgressHUD kProgressHUD;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String userId;
    private CheckBox chkPrivacy;
    private LinearLayout linearPhone, linearCCP;
    private static final String TAG = "EnterPhoneActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_phone);
        getSupportActionBar().hide();

        init();
        setDeviceToken();
        clickListener();

    }

    private void setDeviceToken() {


        if (PrefrenceManager.getDeviceToken(this).equalsIgnoreCase("")) {
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {
                    String newToken = instanceIdResult.getToken();
                    Log.d("Devive_token", newToken + "_");
                    PrefrenceManager.setDecviceToken(EnterPhoneActivity.this, newToken);
                }
            });
        }
    }


    private void clickListener() {
        tvPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EnterPhoneActivity.this, PrivacyPolicyActivity.class);
                startActivity(intent);
            }
        });
        btnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chkPrivacy.isChecked()) {
                    ccp.registerPhoneNumberTextView(etPhone);
                    ccp.getPhoneNumber();
                    if (ccp.isValid()) {
                        kProgressHUD.show();
                        sendOTPFireBase();
//                        Toast.makeText(EnterPhoneActivity.this, ""+ccp.getNumber(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EnterPhoneActivity.this, "Phone number is invalid! Remove entered phone number and you see hint in number textfield", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(EnterPhoneActivity.this, "You donot check privacy policy box!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    private void sendOTPFireBase() {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.d("firebase_auth_error", e.getMessage());
                kProgressHUD.dismiss();
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                mResendToken = token;
                kProgressHUD.dismiss();
                Toast.makeText(getApplicationContext(), "Code Sent!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), VerifyCodeActivity.class);
                intent.putExtra("user_id", userId);
                intent.putExtra("verification_id", verificationId);
                intent.putExtra("number", ccp.getNumber());
                startActivity(intent);
                finish();
            }
        };


//        PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                ccp.getNumber(),        // Phone number to verify
//                60,                 // Timeout duration
//                TimeUnit.SECONDS,   // Unit of timeout
//                EnterPhoneActivity.this,               // Activity (for callback binding)
//                mCallbacks);
        PhoneAuthProvider.verifyPhoneNumber(PhoneAuthOptions.newBuilder().setPhoneNumber(ccp.getNumber()).setTimeout(60L, TimeUnit.SECONDS).setActivity(EnterPhoneActivity.this).setCallbacks(mCallbacks).build());

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            kProgressHUD.dismiss();
                            Log.d("numberVerification", task.isSuccessful() + "");
                            Intent intent = new Intent(getApplicationContext(), VerifyCodeActivity.class);
                            intent.putExtra("user_id", userId);
                            intent.putExtra("number", ccp.getNumber());
                            intent.putExtra("type", "forget");
                            startActivity(intent);
                            finish();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void init() {
        linearPhone = findViewById(R.id.linear_phone);
        linearCCP = findViewById(R.id.linear_ccp);
        chkPrivacy = findViewById(R.id.chk_privacy);
        tvPrivacy = findViewById(R.id.tv_privacy_policy);
        ccp = findViewById(R.id.ccp);
        progressHUD = KProgressHUD.create(EnterPhoneActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);

        mAuth = FirebaseAuth.getInstance();
        kProgressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        btnSendCode = findViewById(R.id.btn_submit);
        etPhone = findViewById(R.id.et_phone);
        if (PrefrenceManager.getDefaultLang(EnterPhoneActivity.this).equalsIgnoreCase("arabic")) {
            /*etPhone.setGravity(Gravity.LEFT);
            etPhone.setRotation(180);
            linearPhone.setRotation(180);
            linearCCP.setGravity(180);*/
        } else {
            etPhone.setGravity(Gravity.LEFT);
        }
    }
}
