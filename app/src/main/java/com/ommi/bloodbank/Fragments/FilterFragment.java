package com.ommi.bloodbank.Fragments;

import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import com.ommi.bloodbank.Activities.BlockActivity;
import com.ommi.bloodbank.Activities.FilterResultActivity;
import com.ommi.bloodbank.MySingleton;
import com.ommi.bloodbank.PrefrenceManager;
import com.ommi.bloodbank.R;
import com.ommi.bloodbank.ServicesUrls;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class FilterFragment extends Fragment {
    private View view;
    private ImageView ivDropDownIcon, ivServicesIcon, ivBloodChart;
    private static final int RC_LOCATION = 5;
    private Spinner SpinnerBlood;
    BroadcastReceiver broadCastReceiver;
    private LinearLayout linearSpinner;
    private TextView etAddress, etCurrentLocation, tvAuto, tvMannual;
    private String Blood, Address = null, country, city, lat, lng;
    private MaterialButton btnSubmit;
    private TextView tvTotal, tvA, tvA_, tvB, tvB_, tvAB, tvAB_, tvO, tvO_;
    private KProgressHUD progressHUD;
    private LinearLayout LinearMannual, LinearAuto;
    private BroadcastReceiver broadcastReceiver;
    private TextView tvPray;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_filter, container, false);
        init();
        getActivity().findViewById(R.id.spinner_land).setVisibility(View.VISIBLE);
        checkUserStatus();
        countDonnarApi();
        broadCastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                countDonnarApi();
            }
        };

        getActivity().registerReceiver(broadCastReceiver,
                new IntentFilter("com.broadcast.fro.message"));
        clickListener();
        return view;
    }

    @Override
    public void onResume() {
        getActivity().registerReceiver(broadCastReceiver,
                new IntentFilter("com.broadcast.fro.message"));
        super.onResume();
    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(broadCastReceiver);
        super.onDestroy();
    }

    private void checkUserStatus() {
        Log.d("dhahfjhsfh", "run" + "_");
        progressHUD.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServicesUrls.USER_STATUS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressHUD.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.d("dhahfjhsfh", response + "_");
                    int status = jsonObject.getInt("status");
                    if (status != 1) {
                        startActivity(new Intent(getActivity(), BlockActivity.class));
                        getActivity().finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressHUD.dismiss();

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
                hashMap.put("user_id", PrefrenceManager.getUserId(getActivity()));
                return hashMap;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }


    private void countDonnarApi() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServicesUrls.COUNT_DONNARS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.getInt("status");
                    if (status == 1) {
                        int total, A, B, A_, B_, AB, AB_, O, O_, unseen;
                        total = jsonObject.getInt("donnar");
                        unseen = jsonObject.getInt("unseen");
                        A = jsonObject.getInt("A+");
                        A_ = jsonObject.getInt("A-");
                        B = jsonObject.getInt("B+");
                        B_ = jsonObject.getInt("B-");
                        AB = jsonObject.getInt("AB+");
                        AB_ = jsonObject.getInt("AB-");
                        O = jsonObject.getInt("O+");
                        O_ = jsonObject.getInt("O-");
                        //tvTotal.setText(total + "");
                        ValueAnimator animator = ValueAnimator.ofInt(0, total);
                        animator.setDuration(2000);
                        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            public void onAnimationUpdate(ValueAnimator animation) {
                                tvTotal.setText(animation.getAnimatedValue().toString());
                            }
                        });
                        animator.start();
                        TextView tvNotification = null;
                        try {
                            tvNotification = getActivity().findViewById(R.id.tv_notification_number);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (unseen > 0) {
                            tvNotification.setVisibility(View.VISIBLE);
                            tvNotification.setText(unseen + "");
                        } else {
                            tvNotification.setVisibility(View.GONE);
                        }
                        tvA.setText(A + "");
                        tvA_.setText(A_ + "");
                        tvB.setText(B + "");
                        tvB_.setText(B_ + "");
                        tvO.setText(O + "");
                        tvO_.setText(O_ + "");
                        tvAB.setText(AB + "");
                        tvAB_.setText(AB_ + "");
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
                HashMap<String, String> hashMap = new HashMap();
                hashMap.put("user_id", PrefrenceManager.getUserId(getActivity()));
                return hashMap;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    private void placesApi(View v) {
        Places.initialize(getActivity(), getResources().getString(R.string.google_maps_key));
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

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 5) && (resultCode == RESULT_OK)) {
            Place place = Autocomplete.getPlaceFromIntent(data);
            etAddress.setVisibility(View.VISIBLE);
            etAddress.setText(place.getAddress());
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
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

    private void clickListener() {
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

        SpinnerBlood.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Blood = parent.getItemAtPosition(position).toString();
//                Toast.makeText(RequsetBloodActivity.this,  parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Blood.equalsIgnoreCase("A+") ||
                        (Blood.equalsIgnoreCase("B+")) ||
                        (Blood.equalsIgnoreCase("A-")) ||
                        (Blood.equalsIgnoreCase("B-")) ||
                        (Blood.equalsIgnoreCase("AB-")) ||
                        (Blood.equalsIgnoreCase("AB+")) ||
                        (Blood.equalsIgnoreCase("o-")) ||
                        Blood.equalsIgnoreCase("o+")) {
                    if ((!etCurrentLocation.getText().toString().isEmpty()) || (!etAddress.getText().toString().isEmpty())) {
                        if (!Address.isEmpty()) {
                            Intent intent = new Intent(getActivity(), FilterResultActivity.class);
                            intent.putExtra("blood", Blood);
                            intent.putExtra("address", Address);
                            intent.putExtra("city", city);
                            intent.putExtra("country", country);
                            intent.putExtra("latitude", lat);
                            intent.putExtra("longitude", lng);
                            intent.putExtra("type", "filter");
                            Log.d("Data___", Blood + "_" + Address + "_" + city + "_" + country + "_" + lat + "_" + lng);
                            startActivity(intent);

                        } else {
                            Toast.makeText(getActivity(), "Enter you address", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Please Enter you address or current location!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Select Blood Group", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setAddress() {
        try {
            String Lat, Lng;
            Lat = PrefrenceManager.getUserLattitude(getActivity());
            Lng = PrefrenceManager.getUserLongitude(getActivity());
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
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

    private void init() {
        progressHUD = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        tvTotal = view.findViewById(R.id.tv_total);
        linearSpinner = view.findViewById(R.id.linear_spinner);
        ivDropDownIcon = view.findViewById(R.id.ivSpinnerDropDown);
        ivServicesIcon = view.findViewById(R.id.ivBloodDrop);
        ivBloodChart = view.findViewById(R.id.iv_blood_mach_chart);
        tvA = view.findViewById(R.id.tv_A);
        tvA_ = view.findViewById(R.id.tv_A_);
        tvB_ = view.findViewById(R.id.tv_B_);
        tvB = view.findViewById(R.id.tv_b);
        tvAB = view.findViewById(R.id.tv_AB);
        tvAB_ = view.findViewById(R.id.tv_AB_);
        tvO = view.findViewById(R.id.tv_o);
        tvO_ = view.findViewById(R.id.tv_o_);
        SpinnerBlood = view.findViewById(R.id.spinnerbloods);
        etAddress = view.findViewById(R.id.et_address);
        btnSubmit = view.findViewById(R.id.btn_submit);
        etCurrentLocation = view.findViewById(R.id.et_loc);
        etAddress = view.findViewById(R.id.et_address);
        LinearAuto = view.findViewById(R.id.linear_auto);
        LinearMannual = view.findViewById(R.id.linear_manual);
        tvAuto = view.findViewById(R.id.tv_auto);
        tvMannual = view.findViewById(R.id.tv_manual);
        tvPray = view.findViewById(R.id.praymymother);

        String blood[] = {getString(R.string.select_blood_group), "A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"};
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.spinner_custom, blood);
        SpinnerBlood.setAdapter(adapter);
        if (PrefrenceManager.getDefaultLang(getActivity()).equalsIgnoreCase("arabic")) {
            LinearMannual.setBackgroundResource(R.drawable.manuual_bg_arabic);
            linearSpinner.setRotation(180);
            tvPray.setGravity(Gravity.CENTER);
            ivBloodChart.setBackgroundResource(R.drawable.arabic_blood_chart);
            ivServicesIcon.setRotation(180);
            ivDropDownIcon.setRotation(180);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.LEFT | Gravity.CENTER;
            ivDropDownIcon.setLayoutParams(params);
            SpinnerBlood.setRotation(180);
            ivDropDownIcon.setPadding(0, 0, 20, 0);
            SpinnerBlood.setPadding(0, 0, 50, 0);
            //ivServicesIcon.setPadding(10,10,10,10);
            //spinnerServices.setBa
        } else if (PrefrenceManager.getDefaultLang(getActivity()).equalsIgnoreCase("french")) {
            ivBloodChart.setBackgroundResource(R.drawable.french_blood_chart);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                tvPray.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
                tvPray.setGravity(Gravity.LEFT | Gravity.CENTER);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                tvPray.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
            }
            tvPray.setGravity(Gravity.CENTER);
            ivBloodChart.setBackgroundResource(R.drawable.chart);

        }
    }

}
