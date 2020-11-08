package com.ommi.bloodbank.Activities;

import android.content.Intent;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
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
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.ommi.bloodbank.Adapters.AdapterSpinner;
import com.ommi.bloodbank.Models.ModelServiceCategories;
import com.ommi.bloodbank.MySingleton;
import com.ommi.bloodbank.PrefrenceManager;
import com.ommi.bloodbank.R;
import com.ommi.bloodbank.ServicesUrls;
import com.ommi.bloodbank.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddServicesActivity extends AppCompatActivity {
    private ImageView ivBack, ivPicker, ivDropDownIcon, ivServicesIcon;
    private static final int RC_LOCATION = 5;
    private CircleImageView ivProfile;
    private TextView picAddress;
    private Button btnSubmit;
    private File fileImage;
    private LinearLayout linearSpinner;
    private String Name, Phone, Age, ServiceId = null, Address, City, Country, Profiletype, Lat, Lng, messege;
    private Spinner spinnerServices;
    private EditText etname, etphone, etMessege;
    private List<ModelServiceCategories> modelServiceCategoriesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_services);
        getSupportActionBar().hide();
        init();
        callServicesApi();
        clickListener();
        etname.setVisibility(View.VISIBLE);
    }

    private void callServicesApi() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServicesUrls.All_SERVICES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.getInt("status");
                    if (status == 1) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject services = jsonArray.getJSONObject(i);
                            String id, name;
                            id = services.getString("id");
                            name = services.getString("name");
                            ModelServiceCategories modelServiceCategories = new ModelServiceCategories(name, id);
                            modelServiceCategoriesList.add(modelServiceCategories);
                        }
                        ModelServiceCategories let = new ModelServiceCategories(getString(R.string.select_service), "-1");
                        modelServiceCategoriesList.add(0, let);
                        setDatatoSpinner();
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
                hashMap.put("lang", PrefrenceManager.getDefaultLang(AddServicesActivity.this));
                return hashMap;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(AddServicesActivity.this).addToRequestQueue(stringRequest);
    }

    private void setDatatoSpinner() {
        AdapterSpinner adapterSpinner = new AdapterSpinner(AddServicesActivity.this, modelServiceCategoriesList);
        spinnerServices.setAdapter(adapterSpinner);
    }

    private void clickListener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        spinnerServices.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    ServiceId = "-1";
                } else {

                    ServiceId = modelServiceCategoriesList.get(position).getServiveId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Name = etname.getText().toString();
                Phone = etphone.getText().toString();
                Address = picAddress.getText().toString();
                messege = etMessege.getText().toString();
                if (!Phone.isEmpty() && Phone.contains("+")) {
                    if (!Name.isEmpty()) {
                        if (!messege.isEmpty()) {
                            if (!Address.isEmpty()) {
                                if (ServiceId != null || !ServiceId.equalsIgnoreCase("-1")) {
                                    ProfileApiCall();

                                } else {
                                    Toast.makeText(AddServicesActivity.this, "Select blood group!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(AddServicesActivity.this, "Enter your address", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(AddServicesActivity.this, etMessege.getHint(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(AddServicesActivity.this, etname.getHint(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddServicesActivity.this, "Enter Phone number including + sign", Toast.LENGTH_SHORT).show();
                }

            }
        });
        picAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placesApi(v);
            }
        });
        ivPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(AddServicesActivity.this)
                        .crop()
                        .compress(1021)
                        .maxResultSize(240, 240)
                        .start();
            }
        });
    }

    private void init() {
        linearSpinner = findViewById(R.id.linear_spinner);
        modelServiceCategoriesList = new ArrayList<>();
        ivBack = findViewById(R.id.iv_back);
        ivProfile = findViewById(R.id.profile_image);
        ivPicker = findViewById(R.id.iv_pick_img);
        etname = findViewById(R.id.et_name);
        ivDropDownIcon = findViewById(R.id.ivSpinnerDropDown);
        ivServicesIcon = findViewById(R.id.iv_services);
        btnSubmit = findViewById(R.id.btn_submit);
        spinnerServices = findViewById(R.id.spinnerServices);
        etphone = findViewById(R.id.et_phone);
        etMessege = findViewById(R.id.et_messege);
        picAddress = findViewById(R.id.location);
        if (PrefrenceManager.getDefaultLang(AddServicesActivity.this).equalsIgnoreCase("arabic")) {
            linearSpinner.setRotation(180);
            ivServicesIcon.setRotation(180);
            ivDropDownIcon.setRotation(180);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.LEFT | Gravity.CENTER;
            ivDropDownIcon.setLayoutParams(params);
            spinnerServices.setRotation(180);
            ivDropDownIcon.setPadding(0, 0, 20, 0);
            spinnerServices.setPadding(0, 0, 50, 0);
            //ivServicesIcon.setPadding(10,10,10,10);
            //spinnerServices.setBa
        }
    }

    private void placesApi(View v) {
        Places.initialize(v.getContext(), getResources().getString(R.string.google_maps_key));
        if (!Places.isInitialized()) {
        } else {
            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);
            Intent intent = new Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.OVERLAY, fields)
                    .build(v.getContext());
            startActivityForResult(intent, RC_LOCATION);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 5) && (resultCode == RESULT_OK)) {
            Place place = Autocomplete.getPlaceFromIntent(data);
            picAddress.setText(place.getAddress());
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<android.location.Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Lat = place.getLatLng().latitude + "";
            Address = addresses.get(0).getAddressLine(0);
            Lng = place.getLatLng().longitude + "";
            City = addresses.get(0).getLocality();
            Country = addresses.get(0).getCountryName();
        } else {
            Uri backgroundUri = data.getData();
            if (backgroundUri != null) {
                fileImage = new File(Utils.getRealPathFromURI_API19(AddServicesActivity.this, backgroundUri));
                ivProfile.setImageURI(backgroundUri);
            }
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void ProfileApiCall() {
        Utils.showProgress(this);
        Builders.Any.B obj = Ion.with(AddServicesActivity.this)
                .load("POST", ServicesUrls.ADD_SERVICE_PROVIDER);
        obj.setMultipartParameter("name", Name);
        obj.setMultipartParameter("phone", Phone);
        obj.setMultipartParameter("city", City);
        obj.setMultipartParameter("country", Country);
        obj.setMultipartParameter("address", Address);
        obj.setMultipartParameter("message", messege);
        obj.setMultipartParameter("service_id", ServiceId);
        obj.setMultipartParameter("latitude", Lat);
        obj.setMultipartParameter("longitude", Lng);
        if (fileImage != null)
            obj.setMultipartFile("img", fileImage);
        Log.d("FileURL", "img\n" + fileImage + "name\n" + Name + "city\n" + City + "Country\n" + Country + "Address\n" + Address + "_SERvice_id\n" + ServiceId + "Phone\n" + Phone + "messege\n" + messege);
        obj.asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        Utils.dismissProgress();
                        if (result != null) {
                            Log.d("response__", result.toString());
                            int status = result.get("status").getAsInt();
                            String message = result.get("message").getAsString();
                            if (status == 1) {
                                finish();
                            } else {
                                Toast.makeText(AddServicesActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.d("response__", e.getMessage());
                        }

                    }
                });
    }

}
