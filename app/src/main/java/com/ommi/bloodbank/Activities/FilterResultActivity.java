package com.ommi.bloodbank.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.ommi.bloodbank.Models.ModelDonnar;
import com.ommi.bloodbank.MySingleton;
import com.ommi.bloodbank.PrefrenceManager;
import com.ommi.bloodbank.R;
import com.ommi.bloodbank.ServicesUrls;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilterResultActivity extends AppCompatActivity {
    public static List<ModelDonnar> list;
    public static List<ModelDonnar> searchlist;
    String  Blood, Address, city, country, lat, lng;
    private RecyclerView recycleDonnar;
    private View view;
    private EditText etSearch;
    private KProgressHUD progressHUD;
    private ImageView ivBack;
    private AdapterDonnar adapterDonnar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_result);
        getSupportActionBar().hide();
        try {
            Blood=getIntent().getExtras().getString("blood");
            Address=getIntent().getExtras().getString("address");
            city=getIntent().getExtras().getString("city");
            country=getIntent().getExtras().getString("country");
            lat=getIntent().getExtras().getString("latitude");
            lng=getIntent().getExtras().getString("longitude");
        } catch (Exception e) {
            e.printStackTrace();
        }

        init();
        setData();
        clickListener();
    }

    private void clickListener() {

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                searchlist.clear();
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getName().trim().toLowerCase().contains(etSearch.getText().toString().trim().toLowerCase()) || list.get(i).getPhone().trim().toLowerCase().contains(etSearch.getText().toString().trim().toLowerCase()) || list.get(i).getAddress().trim().toLowerCase().contains(etSearch.getText().toString().trim().toLowerCase())
                    ) {
                        searchlist.add(list.get(i));
                    }
                }
                initRecycle();
            }
        });
    }

    private void setData() {
        allDonnerApi();
    }

    private void allDonnerApi() {
        searchlist.clear();
        if(adapterDonnar!=null)
        {
            adapterDonnar.notifyDataSetChanged();
        }
        progressHUD.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServicesUrls.FILTER_DONNAR, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("onResponseFilter", response);
                progressHUD.dismiss();
                searchlist.clear();
                list.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.getInt("status");
                    if (status == 1) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject donnar = jsonArray.getJSONObject(i);
                            String img, name, blood, address, age, id, phone,plasma,BloodTransfusion, type, lat, lng, city, country,Status,Reward,TotalRating,DonateTime;
                            name = donnar.getString("name");
                            blood = donnar.getString("blood_type");
                            address = donnar.getString("address");
                            img = donnar.getString("image");
                            age = donnar.getString("age");
                            id = donnar.getString("id");
                            phone = donnar.getString("phone");
                            type = donnar.getString("type");
                            lat = donnar.getString("latitude");
                            lng = donnar.getString("longitude");
                            city = donnar.getString("city");
                            country = donnar.getString("country");
                            TotalRating = donnar.getString("total_rating");
                            Reward = donnar.getString("reward");
                            DonateTime = donnar.getString("blood_donate");
                            Status = donnar.getString("available");
                            plasma = donnar.getString("donate_plasma");
                            BloodTransfusion = donnar.getString("blood_tranfusion");
                            ModelDonnar modelDonnar = new ModelDonnar(id, name, address, age, lat, lng, country, city, phone, blood, img, type,TotalRating,DonateTime,Reward,Status);
                            modelDonnar.setPlasma(plasma);
                            modelDonnar.setBloodTransfusion(BloodTransfusion);
                            list.add(modelDonnar);
                            searchlist.add(modelDonnar);
                        }
                        initRecycle();
                    } else if (status == 0) {
                        searchlist.clear();
                        initRecycle();
                        Toast.makeText(FilterResultActivity.this, "No Record matches!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressHUD.dismiss();
                Log.d("onErrorResponse", error + "");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();

                hashMap.put("city", city);
                hashMap.put("country", country);
                hashMap.put("address", Address);
                hashMap.put("latitude", lat);
                hashMap.put("longitude", lng);
                hashMap.put("blood_type", Blood);
                hashMap.put("id", PrefrenceManager.getUserId(FilterResultActivity.this));
                Log.d("HashMap_Donnars",hashMap+"");
                return hashMap;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(FilterResultActivity.this).addToRequestQueue(stringRequest);
    }

    private void initRecycle() {
        adapterDonnar = new AdapterDonnar(FilterResultActivity.this, searchlist, null, "donnar", recycleDonnar);
        recycleDonnar.setLayoutManager(new LinearLayoutManager(FilterResultActivity.this));
        recycleDonnar.setAdapter(adapterDonnar);
    }

    private void init() {
        progressHUD = KProgressHUD.create(FilterResultActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        etSearch = findViewById(R.id.et_search);
        ivBack=findViewById(R.id.iv_back);
        list = new ArrayList<>();
        searchlist = new ArrayList<>();
        recycleDonnar = findViewById(R.id.recycle_donnar);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
