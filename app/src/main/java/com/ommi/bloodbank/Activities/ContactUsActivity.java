package com.ommi.bloodbank.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ommi.bloodbank.MySingleton;
import com.ommi.bloodbank.PrefrenceManager;
import com.ommi.bloodbank.R;
import com.ommi.bloodbank.ServicesUrls;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ContactUsActivity extends AppCompatActivity {

    private EditText etName,etPhone,etEmail,etMessege;
    private String Name,Phone,Email,Messege;
    private Button btnSubmit;
    private ImageView ivBack;
    private KProgressHUD progressHUD;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        getSupportActionBar().hide();
        init();
        clickListener();
    }

    private void init() {
        progressHUD = KProgressHUD.create(ContactUsActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        etName=findViewById(R.id.et_name);
        etPhone=findViewById(R.id.et_phone);
        etEmail=findViewById(R.id.et_email);
        etMessege=findViewById(R.id.et_messege);
        btnSubmit=findViewById(R.id.btn_submit);
        ivBack=findViewById(R.id.iv_back);
        try {
            etName.setText(PrefrenceManager.getUserName(this));
            etPhone.setText(PrefrenceManager.getUserPhone(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clickListener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
       btnSubmit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Name=etName.getText().toString();
               Email=etEmail.getText().toString();
               Phone=etPhone.getText().toString();
               Messege=etMessege.getText().toString();
               if(!Name.isEmpty())
               {
                   if(!Email.isEmpty())
                   {
                       if(!Phone.isEmpty())
                       {
                           if(!Messege.isEmpty())
                           {
                               if(Email.contains("@") && Email.contains("."))
                               {
                                   if(Phone.contains("+"))
                                   {
                                       callContactUsApi();
                                   }else
                                   {
                                       Toast.makeText(ContactUsActivity.this, "Enter phone number with + and coutry code", Toast.LENGTH_SHORT).show();
                                   }

                               }else
                               {
                                   Toast.makeText(ContactUsActivity.this, "Enter valid email!", Toast.LENGTH_SHORT).show();
                               }
                           }else
                           {
                               Toast.makeText(ContactUsActivity.this, ""+etMessege.getHint(), Toast.LENGTH_SHORT).show();
                           }
                       }else
                       {
                           Toast.makeText(ContactUsActivity.this, ""+etPhone.getHint(), Toast.LENGTH_SHORT).show();
                       }
                   }else
                   {
                       Toast.makeText(ContactUsActivity.this, ""+etEmail.getHint(), Toast.LENGTH_SHORT).show();
                   }
               }else
               {
                   Toast.makeText(ContactUsActivity.this, ""+etName.getHint(), Toast.LENGTH_SHORT).show();
               }

           }
       });
    }

    private void callContactUsApi() {
        progressHUD.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, ServicesUrls.CONTACT_US, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                progressHUD.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.getInt("status");
                    if(status==1){
//                        Toast.makeText(ContactUsActivity.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        finish();
                    }else
                    {
                        Toast.makeText(ContactUsActivity.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressHUD.dismiss();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap=new HashMap<>();
                hashMap.put("user_id","");
                hashMap.put("name",Name);
                hashMap.put("phone",Phone);
                hashMap.put("email",Email);
                hashMap.put("message",Messege);
                return hashMap;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
