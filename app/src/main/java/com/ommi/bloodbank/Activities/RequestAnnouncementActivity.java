package com.ommi.bloodbank.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ommi.bloodbank.Adapters.AdapterDonnar;
import com.ommi.bloodbank.Models.ModelRequest;
import com.ommi.bloodbank.Models.ModelUserData;
import com.ommi.bloodbank.MySingleton;
import com.ommi.bloodbank.PrefrenceManager;
import com.ommi.bloodbank.R;
import com.ommi.bloodbank.ServicesUrls;
import com.ommi.bloodbank.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestAnnouncementActivity extends AppCompatActivity {
    private RecyclerView recyclerViewRequest;
    private EditText etSearch;
    public FloatingActionButton floatingActionButton;
    private ImageView ivBack;
    private List<ModelRequest> requestList;
    public static List<ModelRequest> searchtList;
    public static List<ModelUserData> userDataList;
    private KProgressHUD progressHUD;
    private android.view.View View;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_announcement);
        getSupportActionBar().hide();
        Utils.initLoader(this);
        init();
        setData();
        clickListener();
    }
    private void clickListener() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                Intent requestBlood = new Intent(RequestAnnouncementActivity.this, RequsetBloodActivity.class);
                startActivity(requestBlood);
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                onBackPressed();
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchtList.clear();
                for (int i = 0; i < requestList.size(); i++) {
                    if (requestList.get(i).getName().trim().toLowerCase().contains(etSearch.getText().toString().trim().toLowerCase()) || requestList.get(i).getPhone().trim().toLowerCase().contains(etSearch.getText().toString().trim().toLowerCase()) || requestList.get(i).getAddress().trim().toLowerCase().contains(etSearch.getText().toString().trim().toLowerCase())
                    ) {
                        searchtList.add(requestList.get(i));
                    }
                }
                initRecycle();
            }
        });

    }

    private void setData() {
        allRequestApi();
    }
    private void allRequestApi() {
        progressHUD.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, ServicesUrls.All_REQUESTBLOOD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response___",response);
                progressHUD.dismiss();
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    int status=jsonObject.getInt("status");
                    if(status==1)
                    {
                        JSONArray jsonArray=jsonObject.getJSONArray("data");
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject data=jsonArray.getJSONObject(i);
                            JSONObject request=data.getJSONObject("requests");
                            JSONObject user=data.getJSONObject("user");
                            String name,blood,address,age,id,phone,city,country,text,lattitude,longitude,TotalRating,Reward,DonationNumber;
                            String username,userblood,Status,Plasma,TransFusion,useraddress,userage,userid,userphone,usercity,usercountry,usertype,userlat,userlng,userimg;
                            name=request.getString("name");
                            blood=request.getString("blood_type");
                            address=request.getString("address");
                            age=request.getString("age");
                            id=request.getString("id");
                            city=request.getString("city");
                            country=request.getString("country");
                            lattitude=request.getString("latitude");
                            longitude=request.getString("longitude");
                            text=request.getString("text");
                            phone=request.getString("phone");
                            //userdata
                            username=user.getString("name");
                            userblood=user.getString("blood_type");
                            useraddress=user.getString("address");
                            userage=user.getString("age");
                            userid=user.getString("id");
                            usercity=user.getString("city");
                            usercountry=user.getString("country");
                            userlat=user.getString("latitude");
                            userlng=user.getString("longitude");
                            usertype=user.getString("type");
                            userphone=user.getString("phone");
                            userimg=user.getString("image");
                            TotalRating=user.getString("total_rating");
                            DonationNumber=user.getString("blood_donate");
                            Reward=user.getString("reward");
                            Status=user.getString("available");
                            Plasma=user.getString("donate_plasma");
                            TransFusion=user.getString("blood_tranfusion");
                            ModelRequest modelRequest=new ModelRequest(id,name,age,address,phone,blood,country,city,lattitude,longitude,text,Reward,DonationNumber,TotalRating);
                            requestList.add(modelRequest);
                            searchtList.add(modelRequest);
                            ModelUserData modelUserData=new ModelUserData(userid,username,userphone,userimg,usercountry,usercity,useraddress,userlat,userlng,userage,userblood,usertype,Status);
                            modelUserData.setPlasma(Plasma);
                            modelUserData.setBloodTransfusion(TransFusion);
                            userDataList.add(modelUserData);
                        }
                        initRecycle();
                    }else
                    {
                        Toast.makeText(RequestAnnouncementActivity.this, ""+jsonObject.getString("messege"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressHUD.dismiss();
                Log.d("onErrorResponse",error+"");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap=new HashMap<>();
                hashMap.put("city", PrefrenceManager.getUserCity(RequestAnnouncementActivity.this));
                hashMap.put("country", PrefrenceManager.getUserCountry(RequestAnnouncementActivity.this));
                hashMap.put("id", PrefrenceManager.getUserId(RequestAnnouncementActivity.this));
                Log.d("HashMap_Request",hashMap+"");
                return hashMap;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(RequestAnnouncementActivity.this).addToRequestQueue(stringRequest);
    }

    private void initRecycle() {
        recyclerViewRequest.setLayoutManager(new LinearLayoutManager(RequestAnnouncementActivity.this));
        AdapterDonnar adapterDonnar=new AdapterDonnar(RequestAnnouncementActivity.this,null,searchtList,"request",recyclerViewRequest);
        recyclerViewRequest.setAdapter(adapterDonnar);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void init() {
        progressHUD = KProgressHUD.create(RequestAnnouncementActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        requestList=new ArrayList<>();
        searchtList=new ArrayList<>();
        userDataList=new ArrayList<>();
        recyclerViewRequest=findViewById(R.id.recycle_request);
        ivBack=findViewById(R.id.iv_back);
        etSearch=findViewById(R.id.et_search);
        floatingActionButton = findViewById(R.id.floatingActionButton);

    }
}