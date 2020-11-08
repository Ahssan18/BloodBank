package com.ommi.bloodbank.Activities;

import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.button.MaterialButton;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.ommi.bloodbank.Adapters.AdapterServiceProvider;
import com.ommi.bloodbank.Models.ModelServiceProvider;
import com.ommi.bloodbank.MySingleton;
import com.ommi.bloodbank.PrefrenceManager;
import com.ommi.bloodbank.R;
import com.ommi.bloodbank.ServicesUrls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ServicesProviderActivity extends AppCompatActivity {
    private RecyclerView recyclerViewServiceProvider;
    private List<ModelServiceProvider> servicesProviderList;
    private ImageView ivBack;
    private KProgressHUD progressHUD;
    private String id, name;
    private TextView tvProvider;
    private List<CountryData> countriesList;
    private List<CountryData> statesList;
    private LinearLayout LinearMannual, LinearAuto;
    private TextView etAddress, etCurrentLocation, tvAuto, tvMannual;
    private MaterialButton btnSubmit;
    private List<CountryData> cityList;
    private static final int RC_LOCATION = 5;
    private Spinner spinnercountry, spinnerstate, spinnercity;
    private String Blood, Address = null, country, city, lat, lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_provider);
        try {
            name = getIntent().getStringExtra("service_name");
        } catch (Exception e) {
            e.printStackTrace();
        }
        getSupportActionBar().hide();
        init();
        try {
            id = getIntent().getStringExtra("id_");
            lat = PrefrenceManager.getUserLattitude(ServicesProviderActivity.this);
            lng = PrefrenceManager.getUserLongitude(ServicesProviderActivity.this);
            servicesProviderApi();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setDataOnspinner();
        clickListener();

    }

    private void setDataOnspinner() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ServicesUrls.GET_COUTRIES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Responece", response + "_");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject data = jsonArray.getJSONObject(i);
                            String id, code, name;
                            id = data.getString("id");
                            code = data.getString("sortname");
                            name = data.getString("name");

                            CountryData countryData = new CountryData(name, id);
                            countriesList.add(countryData);
                        }
                        ArrayAdapter<CountryData> adapter =
                                new ArrayAdapter<CountryData>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, countriesList);
                        CountryData countryData = new CountryData("Select Country", "-1");
                        adapter.insert(countryData, 0);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, months);
                        spinnercountry.setAdapter(adapter);
                    } else {
//                        Toast.makeText(ServicesProviderActivity.this, "" + jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void clickListener() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                servicesProviderApi();
            }
        });
        etAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placesApi(v);
            }
        });
        LinearMannual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etCurrentLocation.setVisibility(View.GONE);
                etAddress.setVisibility(View.VISIBLE);
                LinearMannual.setBackgroundColor(getResources().getColor(R.color.colorRed));
                LinearAuto.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                tvMannual.setTextColor(getResources().getColor(R.color.colorWhite));
                tvAuto.setTextColor(getResources().getColor(R.color.colorRed));

            }
        });
        LinearAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearAuto.setBackgroundColor(getResources().getColor(R.color.colorRed));
                LinearMannual.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                tvMannual.setTextColor(getResources().getColor(R.color.colorRed));
                tvAuto.setTextColor(getResources().getColor(R.color.colorWhite));
                etCurrentLocation.setVisibility(View.VISIBLE);
                etAddress.setVisibility(View.GONE);
                setAddress();
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        /*spinnercountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0)
                {
                    position=position+1;
                }
                String countryid = countriesList.get(position).getCountry_id();
                getStatesBYCountryId(countryid);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerstate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    position = position + 1;
                }
                String stateid = statesList.get(position).getCountry_id();
                Log.d("State_list", stateid + "_");
                getCitiesById(stateid);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnercity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if (position != 0) {
                        String countryname = cityList.get(position).getCountryname();
                        if (countryname != null) {
                            servicesProviderApi(countryname);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
    }

    private void placesApi(View v) {
        Places.initialize(ServicesProviderActivity.this, getResources().getString(R.string.google_maps_key));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 5) && (resultCode == RESULT_OK)) {
            Place place = Autocomplete.getPlaceFromIntent(data);
            etAddress.setVisibility(View.VISIBLE);
            etAddress.setText(place.getAddress());
            Geocoder geocoder = new Geocoder(ServicesProviderActivity.this, Locale.getDefault());
            List<android.location.Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            lat = place.getLatLng().latitude + "";
            lng = place.getLatLng().longitude + "";
            city = addresses.get(0).getLocality();
            country = addresses.get(0).getCountryName();
            Address = etAddress.getText().toString();

        }
    }

    private void setAddress() {
        try {
            String Lat, Lng;
            Lat = PrefrenceManager.getUserLattitude(ServicesProviderActivity.this);
            Lng = PrefrenceManager.getUserLongitude(ServicesProviderActivity.this);
            Geocoder geocoder = new Geocoder(ServicesProviderActivity.this, Locale.getDefault());
            List<android.location.Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(Double.parseDouble(Lat), Double.parseDouble(Lng), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            lat = Lat;
            lng = Lng;
            city = addresses.get(0).getSubAdminArea();
            Address = addresses.get(0).getAddressLine(0);
            country = addresses.get(0).getCountryName();
            etCurrentLocation.setText(Address);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   /* private void getCitiesById(final String stateid) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServicesUrls.GET_Cities, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Responece_city", response + "_");
                cityList.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject data = jsonArray.getJSONObject(i);
                            String id, code, name;
                            id = data.getString("id");
                            name = data.getString("name");
                            CountryData countryData = new CountryData(name, id);
                            cityList.add(countryData);
                        }
                        ArrayAdapter<CountryData> adapter =
                                new ArrayAdapter<CountryData>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, cityList);
                        CountryData countryData = new CountryData("Select City", "-1");
                        adapter.insert(countryData, 0);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnercity.setAdapter(adapter);
                    } else {
                        Toast.makeText(ServicesProviderActivity.this, "No city against this state!", Toast.LENGTH_SHORT).show();
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
                hashMap.put("state_id", stateid);
                Log.d("State_id", stateid + "_");
                return hashMap;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void getStatesBYCountryId(final String countryid) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServicesUrls.GET_STATES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Responece", response + "_");
                statesList.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject data = jsonArray.getJSONObject(i);
                            String id, code, name;
                            id = data.getString("id");
                            name = data.getString("name");
                            CountryData countryData = new CountryData(name, id);
                            statesList.add(countryData);

                        }
                        ArrayAdapter<CountryData> adapter =
                                new ArrayAdapter<CountryData>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, statesList);
                        CountryData countryData = new CountryData("Select State", "-1");
                        adapter.insert(countryData, 0);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerstate.setAdapter(adapter);
                    } else {
//                        Toast.makeText(ServicesProviderActivity.this, "" + jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
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
                hashMap.put("country_id", countryid);
                return hashMap;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }*/

    private void servicesProviderApi() {
        servicesProviderList.clear();
        progressHUD.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServicesUrls.SERVICES_PROVIDER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressHUD.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.getInt("status");
                    Log.d("val_status",status+"_");
                    if (status == 1) {
                        findViewById(R.id.recycle_service_provider).setVisibility(View.VISIBLE);
                        findViewById(R.id.tv_empty).setVisibility(View.GONE);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject donnar = jsonArray.getJSONObject(i);
                            String img, name, paragraph, address, age, id, phone, image;
                            name = donnar.getString("name");
                            paragraph = donnar.getString("text");
                            address = donnar.getString("address");
                            id = donnar.getString("id");
                            phone = donnar.getString("phone");
                            image = donnar.getString("image");
                            ModelServiceProvider modelServiceProvider = new ModelServiceProvider(id, name, phone, address, paragraph, image);
                            servicesProviderList.add(modelServiceProvider);
                        }
                        initRecycle();
                    } else {
                        if (servicesProviderList.size() == 0) {
                            findViewById(R.id.recycle_service_provider).setVisibility(View.GONE);
                            findViewById(R.id.tv_empty).setVisibility(View.VISIBLE);
                        }
                    }
                } catch (JSONException e) {
                    progressHUD.dismiss();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("onErrorResponse", error + "");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("service_id", id);
                hashMap.put("latitude", lat);
                hashMap.put("longitude", lng);
                Log.d("HASh_MAp",hashMap+"_");
                return hashMap;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void initRecycle() {
        recyclerViewServiceProvider.setLayoutManager(new LinearLayoutManager(ServicesProviderActivity.this));
        AdapterServiceProvider adapterServiceProvider = new AdapterServiceProvider(ServicesProviderActivity.this, servicesProviderList);
        recyclerViewServiceProvider.setAdapter(adapterServiceProvider);
    }

    private void init() {
        countriesList = new ArrayList<>();
        statesList = new ArrayList<>();
        cityList = new ArrayList<>();
        progressHUD = KProgressHUD.create(ServicesProviderActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        spinnercity = findViewById(R.id.spinner_city);
        spinnercountry = findViewById(R.id.spinner_country);
        spinnerstate = findViewById(R.id.spinner_state);
        ivBack = findViewById(R.id.iv_back);
        tvProvider = findViewById(R.id.tv_provider);
        tvProvider.setText(name);
        servicesProviderList = new ArrayList<>();
        recyclerViewServiceProvider = findViewById(R.id.recycle_service_provider);
        btnSubmit = findViewById(R.id.btn_submit);
        etCurrentLocation = findViewById(R.id.et_loc);
        etAddress = findViewById(R.id.et_address);
        LinearAuto = findViewById(R.id.linear_auto);
        LinearMannual = findViewById(R.id.linear_manual);
        tvAuto = findViewById(R.id.tv_auto);
        tvMannual = findViewById(R.id.tv_manual);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private class CountryData {
        private String countryname;
        private String country_id;

        public CountryData() {
        }

        public CountryData(String contact_name, String contact_id) {
            this.countryname = contact_name;
            this.country_id = contact_id;
        }

        public String getCountryname() {
            return countryname;
        }

        public void setCountryname(String countryname) {
            this.countryname = countryname;
        }

        public String getCountry_id() {
            return country_id;
        }

        public void setCountry_id(String country_id) {
            this.country_id = country_id;
        }

        /**
         * Pay attention here, you have to override the toString method as the
         * ArrayAdapter will reads the toString of the given object for the name
         *
         * @return contact_name
         */
        @Override
        public String toString() {
            return countryname;
        }
    }
}
