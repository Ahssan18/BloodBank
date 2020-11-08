package com.ommi.bloodbank.Activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.ommi.bloodbank.Adapters.AdapterNotification;
import com.ommi.bloodbank.Models.ModelNotification;
import com.ommi.bloodbank.Models.ModelUserData;
import com.ommi.bloodbank.MySingleton;
import com.ommi.bloodbank.PrefrenceManager;
import com.ommi.bloodbank.R;
import com.ommi.bloodbank.ServicesUrls;
import com.ommi.bloodbank.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationActivity extends AppCompatActivity {

    private RecyclerView recyclerViewNotification;
    private ImageView ivBack, ivDeleteNotification;
    private TextView tvNodatafound;
    public static List<ModelNotification> NotificationList;
    public static List<ModelUserData> userDataList;
    private KProgressHUD progressHUD;
    AdapterNotification adapterNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        getSupportActionBar().hide();
        init();
        notificationApicall();
        clickListener();

    }

    private void notificationApicall() {
        progressHUD.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServicesUrls.NOTIFICATION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                NotificationList.clear();
                userDataList.clear();
                progressHUD.dismiss();

                Log.d("onResponse__", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.getInt("status");
                    if (status == 1) {
                        ivDeleteNotification.setVisibility(View.VISIBLE);
                        tvNodatafound.setVisibility(View.GONE);
                        recyclerViewNotification.setVisibility(View.VISIBLE);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject data = jsonArray.getJSONObject(i);
                            String id, blood, address, donnerId, requestId, date, name, senderid, title, messege;
                            JSONObject user = null;
                            user = data.getJSONObject("user");
                            senderid = user.getString("sender_id");
                            blood = data.getString("blood_type");
                            id = data.getString("id");
                            address = data.getString("address");
                            title = data.getString("title");
                            messege = data.getString("msg");
                            donnerId = data.getString("donor_id");
                            requestId = data.getString("request_id");
                            date = data.getString("created_at");
                            String u_id = "", u_name = "", Plasma = "", Transfusion = "", u_image = "", u_phone = "", u_country = "", u_city = "", u_address = "", u_lat = "", u_lng = "", u_age = "", u_blood = "", u_type = "", Status = "", TotalRating = "", DonationTime = "", Reward = "";

                            name = user.getString("name");
                            if (senderid.equals("1")) {
                                u_image = user.getString("ímage");
                            } else {
                                Log.d("SENDER_id", senderid + "_" + name);
                                u_id = user.getString("id");
                                u_name = user.getString("name");
                                u_image = user.getString("image");
                                u_phone = user.getString("phone");
                                u_country = user.getString("country");
                                u_city = user.getString("city");
                                u_address = user.getString("address");
                                u_lat = user.getString("latitude");
                                u_lng = user.getString("longitude");
                                u_age = user.getString("age");
                                u_blood = user.getString("blood_type");
                                u_type = user.getString("type");
                                Status = user.getString("available");
                                Reward = user.getString("reward");
                                TotalRating = user.getString("total_rating");
                                DonationTime = user.getString("blood_donate");
                                Plasma = user.getString("donate_plasma");
                                Transfusion = user.getString("blood_tranfusion");
                            }
                            ModelNotification modelNotification = new ModelNotification(id, blood, address, donnerId, requestId, date, name, DonationTime, TotalRating, Reward, senderid, title, messege);
                            NotificationList.add(modelNotification);

                            ModelUserData modelUserData = new ModelUserData(u_id, u_name, u_phone, u_image, u_country, u_city, u_address, u_lat, u_lng, u_age, u_blood, u_type, Status);
                            modelUserData.setPlasma(Plasma);
                            modelUserData.setBloodTransfusion(Transfusion);
                            userDataList.add(modelUserData);

                        }
                        initRecycle();
                    } else {
                        recyclerViewNotification.setVisibility(View.GONE);
                        tvNodatafound.setVisibility(View.VISIBLE);
                        Toast.makeText(NotificationActivity.this, "" + jsonObject.getString("messege"), Toast.LENGTH_SHORT).show();
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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("user_id", PrefrenceManager.getUserId(NotificationActivity.this));
                Log.d("Has_map_notification", hashMap + "_");
                return hashMap;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void initRecycle() {
        adapterNotification = new AdapterNotification(NotificationList, NotificationActivity.this);
        recyclerViewNotification.setLayoutManager(new LinearLayoutManager(NotificationActivity.this));
        recyclerViewNotification.setAdapter(adapterNotification);
    }


    private void clickListener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ivDeleteNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteNotification();
            }
        });
    }

    private void deleteNotification() {
        new MaterialAlertDialogBuilder(NotificationActivity.this)
                .setTitle("Delete Notification")
                .setMessage("Are you sure,You wanted to delete")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteNotificationApiCall();
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }

    private void deleteNotificationApiCall() {
        try {
            Utils.showProgress(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServicesUrls.DELETE_NOTIFICATION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    try {
                        Utils.dismissProgress();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.getInt("status");
                    String message = jsonObject.getString("message");
                    if (status == 1) {
                        ivDeleteNotification.setVisibility(View.GONE);
                        NotificationList.clear();
                        Toast.makeText(NotificationActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                        adapterNotification.notifyDataSetChanged();
                    } else {
                        Toast.makeText(NotificationActivity.this, "" + message, Toast.LENGTH_SHORT).show();

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
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap hashMap = new HashMap();
                hashMap.put("user_id", PrefrenceManager.getUserId(NotificationActivity.this));
                return hashMap;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }


    private void init() {
        tvNodatafound = findViewById(R.id.tv_no_datafound);
        progressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        NotificationList = new ArrayList<>();
        userDataList = new ArrayList<>();
        ivBack = findViewById(R.id.iv_back);
        ivDeleteNotification = findViewById(R.id.iv_delete_notification);
        recyclerViewNotification = findViewById(R.id.recycle_notification);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
