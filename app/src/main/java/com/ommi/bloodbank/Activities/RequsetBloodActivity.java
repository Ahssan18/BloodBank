package com.ommi.bloodbank.Activities;

import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RequsetBloodActivity extends AppCompatActivity {
    private static final int RC_LOCATION = 5;
    private ImageView ivBack, ivFilter, ivDropDownIcon, ivServicesIcon;
    private EditText etphone, etMessege;
    private TextView tvAddress;
    private LinearLayout linearSpinner;
    private Spinner BloodGroups;
    private String Phone, Address, BloodGroup, Messege, Lat, Lng, City, Country;
    private Button btnSubmit;
    private KProgressHUD progressHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requset_blood);
        getSupportActionBar().hide();
        init();
        clickListener();
    }

    private void placesApi(View v) {
        Places.initialize(v.getContext(), getResources().getString(R.string.google_maps_key));
        if (!Places.isInitialized()) {
        } else {
            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);
            // Start the autocomplete intent.
            Intent intent = new Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.OVERLAY, fields)
                    .build(v.getContext());
            startActivityForResult(intent, RC_LOCATION);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 5) && (resultCode == RESULT_OK)) {
            try {
                Place place = Autocomplete.getPlaceFromIntent(data);
                tvAddress.setText(place.getAddress());
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<android.location.Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Lat = place.getLatLng().latitude + "";
                Lng = place.getLatLng().longitude + "";
                City = addresses.get(0).getLocality();
                Country = addresses.get(0).getCountryName();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }

    private void clickListener() {
        tvAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placesApi(v);
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        BloodGroups.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                BloodGroup = parent.getItemAtPosition(position).toString();
//                Toast.makeText(RequsetBloodActivity.this,  parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Phone = etphone.getText().toString();
                Address = tvAddress.getText().toString();
                Messege = etMessege.getText().toString();
                if (!Phone.isEmpty()) {
                    if (Phone.contains("+")) {
                        if (BloodGroup.equalsIgnoreCase("A+") ||
                                (BloodGroup.equalsIgnoreCase("B+")) ||
                                (BloodGroup.equalsIgnoreCase("A-")) ||
                                (BloodGroup.equalsIgnoreCase("B-")) ||
                                (BloodGroup.equalsIgnoreCase("AB-")) ||
                                (BloodGroup.equalsIgnoreCase("AB+")) ||
                                (BloodGroup.equalsIgnoreCase("o-")) ||
                                BloodGroup.equalsIgnoreCase("o+")) {
                            if (!Messege.isEmpty()) {
                                requestBloodApiCall();

                            } else {
                                Toast.makeText(RequsetBloodActivity.this, "" + etMessege.getHint(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(RequsetBloodActivity.this, "Select blood group", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RequsetBloodActivity.this, "Enter Phone number including + and country code", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RequsetBloodActivity.this, "" + etphone.getHint(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void requestBloodApiCall() {
        progressHUD.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServicesUrls.REQUEST_BLOOD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressHUD.dismiss();
                Log.d("onResponse", response + "_");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        finish();
                    } else {
                        Toast.makeText(RequsetBloodActivity.this, "" + jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressHUD.dismiss();
                Log.d("onResponse", error + "Error");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("phone", Phone);
                hashMap.put("address", Address);
                hashMap.put("blood_type", BloodGroup);
                hashMap.put("latitude", Lat);
                hashMap.put("longitude", Lng);
                hashMap.put("text", Messege);
                hashMap.put("age", PrefrenceManager.getUserAge(RequsetBloodActivity.this));
                hashMap.put("city", City);
                hashMap.put("country", Country);
                hashMap.put("name", PrefrenceManager.getUserName(RequsetBloodActivity.this));
                hashMap.put("user_id", PrefrenceManager.getUserId(RequsetBloodActivity.this));
                Log.d("hashmap_request", hashMap + "_");
                return hashMap;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void init() {
        progressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        ivBack = findViewById(R.id.iv_back);
        btnSubmit = findViewById(R.id.btn_submit);
        etphone = findViewById(R.id.et_phone);
        try {
            etphone.setText(PrefrenceManager.getUserPhone(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
        etMessege = findViewById(R.id.et_messege);
        linearSpinner = findViewById(R.id.linear_spinner);
        ivDropDownIcon = findViewById(R.id.ivSpinnerDropDown);
        ivServicesIcon = findViewById(R.id.iv_services);
        tvAddress = findViewById(R.id.et_address);
        BloodGroups = findViewById(R.id.spinnerblood);
        String[] blod = {getString(R.string.select_blood_group), "A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"};
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.spinner_custom, blod);
        BloodGroups.setAdapter(adapter);
        if (PrefrenceManager.getDefaultLang(RequsetBloodActivity.this).equalsIgnoreCase("arabic")) {
            linearSpinner.setRotation(180);
            ivServicesIcon.setRotation(180);
            ivDropDownIcon.setRotation(180);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.LEFT | Gravity.CENTER;
            ivDropDownIcon.setLayoutParams(params);
            BloodGroups.setRotation(180);
            ivDropDownIcon.setPadding(0, 0, 20, 0);
            BloodGroups.setPadding(0, 0, 50, 0);
            //ivServicesIcon.setPadding(10,10,10,10);
            //spinnerServices.setBa
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
