package com.ommi.bloodbank.Activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.ommi.bloodbank.PrefrenceManager;
import com.ommi.bloodbank.R;
import com.ommi.bloodbank.ServicesUrls;
import com.ommi.bloodbank.Utils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileImageActivity extends AppCompatActivity {

    private static final int RC_LOCATION = 5;
    private static final String TAG = "LogPlaces";
    private ImageView ivBack, ivPicker, ivDropDownIcon, ivServicesIcon;
    private CircleImageView ivProfile;
    private Button btnSubmit;
    private LinearLayout linearSpinner;
    private int Availibility = 0, Transfusion = 0, Plasma = 0;
    private EditText etname, etphone, etage;
    private TextView picAddress;
    private String Name, Phone, Age, Bloodgroup, Address, UserId, type, City, Country, Profiletype, Lat, Lng, CODE, StateCountry;
    private CheckBox typeUser, chkAvaible, ChkPlasma, ChkTransfusion;
    private Spinner spinnerBlood;
    private File fileImage;
    private String TypeRef = "register", Sex = "male";
    private ArrayAdapter adapter;
    private RadioGroup radioGroup;
    private RadioButton radioMale, radioFemale;
    private Context context = ProfileImageActivity.this;
    private String pic_url, gender_;
    private boolean chkfusion = false, chkdonate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_image_screen);
        getSupportActionBar().hide();
        getIntentData();
        init();
        if ("myprofile".equalsIgnoreCase(Profiletype)) {
            chkAvaible.setVisibility(View.VISIBLE);
//            findViewById(R.id.tv_avail_msg).setVisibility(View.VISIBLE);
            String avail = PrefrenceManager.getAvailability(ProfileImageActivity.this);
            String trans, Plas;
            trans = PrefrenceManager.getTransfusion(ProfileImageActivity.this);
            Plas = PrefrenceManager.getPlasmaDonation(ProfileImageActivity.this);
            gender_ = PrefrenceManager.getGender(ProfileImageActivity.this);
            if (gender_.equalsIgnoreCase("male")) {
                radioMale.setChecked(true);
            } else {
                radioFemale.setChecked(true);
            }
            if (avail.equalsIgnoreCase("0")) {
                chkAvaible.setChecked(true);
            } else {
                chkAvaible.setChecked(false);
            }
            if (trans.equalsIgnoreCase("1")) {
                ChkTransfusion.setChecked(true);
                chkfusion = true;

            } else {
                ChkTransfusion.setChecked(false);
            }
            if (Plas.equalsIgnoreCase("1")) {
                ChkPlasma.setChecked(true);
                chkdonate = true;
            } else {
                ChkPlasma.setChecked(false);
            }
            if (ChkPlasma.isChecked() || ChkTransfusion.isChecked()) {
                chkAvaible.setClickable(false);

            } else {
                chkAvaible.setClickable(true);

            }

            chkAvaible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        Availibility = 0;
                    } else {
                        Availibility = 1;

                    }
                }
            });


            ChkTransfusion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked) {
                        chkAvaible.setChecked(false);
                        chkAvaible.setClickable(false);
                        Transfusion = 1;
                    } else {
                        chkAvaible.setClickable(true);
                        Transfusion = 0;

                    }
                }
            });
            ChkPlasma.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        chkAvaible.setClickable(false);
                        chkAvaible.setChecked(false);
                        Plasma = 1;
                    } else {
                        chkAvaible.setClickable(true);
                        Plasma = 0;

                    }
                }
            });

            typeUser.setVisibility(View.GONE);
            etphone.setVisibility(View.GONE);
            etname.setText(PrefrenceManager.getUserName(context));
            etage.setText(PrefrenceManager.getUserAge(context));
            Country = PrefrenceManager.getUserCountry(ProfileImageActivity.this);
            City = PrefrenceManager.getUserCity(ProfileImageActivity.this);
            picAddress.setText(PrefrenceManager.getUserAddress(context));
            int pos = adapter.getPosition(PrefrenceManager.getUserBloodGroup(context));
            spinnerBlood.setSelection(pos);
            btnSubmit.setText(getResources().getString(R.string.text_update));
            Log.d("PICURl", ServicesUrls.IMAGE_URL + PrefrenceManager.getUserImage(this));
            if (PrefrenceManager.getUserImage(ProfileImageActivity.this) != null)
                Glide.with(context)
                        .load(ServicesUrls.IMAGE_URL + PrefrenceManager.getUserImage(context))
                        .centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .into(ivProfile);

        } else {

            etphone.setText(Phone);
            if (ChkPlasma.isChecked() || ChkTransfusion.isChecked()) {
                Toast.makeText(context, R.string.chk_avail_messege, Toast.LENGTH_SHORT).show();
                chkAvaible.setClickable(false);
            } else {
                chkAvaible.setClickable(true);

            }
        }
        clickListener();
    }

    private void getIntentData() {
        try {
            UserId = getIntent().getStringExtra("user_id");
            Phone = getIntent().getStringExtra("phone");
            TypeRef = getIntent().getStringExtra("type");

            if (Phone == null || Phone.equalsIgnoreCase("")) {
                Phone = PrefrenceManager.getUserPhone(ProfileImageActivity.this);
            }
            Profiletype = getIntent().getStringExtra("ref");
            if (UserId == null) {
                UserId = PrefrenceManager.getUserId(ProfileImageActivity.this);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Profiletype = "register";

        }
    }

    private void clickListener() {


        etage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                String Dob = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                etage.setText(Dob);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ivPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(ProfileImageActivity.this)
                        .crop()
                        .compress(1021)
                        .maxResultSize(240, 240)
                        .start();
            }
        });
        picAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placesApi(v);
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_female:
                        Sex = "female";
                        break;
                    case R.id.radio_male:
                        Sex = "male";
                        break;
                }
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Name = etname.getText().toString();
                Age = etage.getText().toString();
                Phone = etphone.getText().toString();
                Address = picAddress.getText().toString();


                if (typeUser.isChecked()) {
                    type = "donnar";
                } else {
                    type = "accepter";
                }
                if (!Name.isEmpty()) {
                    if (!Age.isEmpty()) {
                        if (!Address.isEmpty()) {
                            if (Bloodgroup.equalsIgnoreCase("A+") ||
                                    (Bloodgroup.equalsIgnoreCase("B+")) ||
                                    (Bloodgroup.equalsIgnoreCase("A-")) ||
                                    (Bloodgroup.equalsIgnoreCase("B-")) ||
                                    (Bloodgroup.equalsIgnoreCase("AB-")) ||
                                    (Bloodgroup.equalsIgnoreCase("AB+")) ||
                                    (Bloodgroup.equalsIgnoreCase("o-")) ||
                                    Bloodgroup.equalsIgnoreCase("o+")) {
                                ProfileApiCall();
                            } else {
                                Toast.makeText(context, "Select blood group!", Toast.LENGTH_SHORT).show();
                            }


                        } else {
                            Toast.makeText(ProfileImageActivity.this, "Enter your address", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(ProfileImageActivity.this, etage.getHint(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ProfileImageActivity.this, etname.getHint(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        spinnerBlood.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Bloodgroup = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
            CODE = addresses.get(0).getCountryCode();
            Lng = place.getLatLng().longitude + "";
            City = addresses.get(0).getLocality();
            Country = addresses.get(0).getCountryName();
            StateCountry = addresses.get(0).getAdminArea();
            PrefrenceManager.setUserCountry(context, Country);
            PrefrenceManager.setUserCity(context, City);
            PrefrenceManager.setUserAddress(context, place.getAddress());
            PrefrenceManager.setUserLattitude(context, Lat);
            PrefrenceManager.setUserLongitude(context, Lng);
            PrefrenceManager.setCountryCode(context, CODE);

        } else {
            try {
                assert data != null;
                Uri backgroundUri = data.getData();
                if (backgroundUri != null) {
                    fileImage = new File(Objects.requireNonNull(Utils.getRealPathFromURI_API19(ProfileImageActivity.this, backgroundUri)));
                    ivProfile.setImageURI(backgroundUri);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void ProfileApiCall() {
        Utils.showProgress(this);
        Builders.Any.B obj = Ion.with(ProfileImageActivity.this)
                .load("POST", ServicesUrls.REGISTER_PROFILE);
        obj.setMultipartParameter("name", Name);
        obj.setMultipartParameter("city", City);
        obj.setMultipartParameter("country", Country);
        obj.setMultipartParameter("state", StateCountry);
        obj.setMultipartParameter("address", Address);
        obj.setMultipartParameter("blood_type", Bloodgroup);
        obj.setMultipartParameter("user_id", UserId);
        obj.setMultipartParameter("age", Age);
        obj.setMultipartParameter("latitude", Lat);
        obj.setMultipartParameter("longitude", Lng);
        obj.setMultipartParameter("type", type);
        obj.setMultipartParameter("donate_plasma", Plasma + "");
        obj.setMultipartParameter("country_code", CODE);
        obj.setMultipartParameter("gender", Sex);
        obj.setMultipartParameter("blood_tranfusion", Transfusion + "");
        if (fileImage != null) {
            Log.d("file____", fileImage + "");
            obj.setMultipartFile("image", fileImage);
        }
        obj.setMultipartParameter("available", Availibility + "");
        obj.asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        Utils.dismissProgress();
                        if (result != null) {
                            Log.d("response__", result.toString());
                            boolean status = result.get("status").getAsBoolean();
                            String message = result.get("msg").getAsString();
                            if (status) {
                                Context context = ProfileImageActivity.this;
                                JsonObject user = result.getAsJsonObject("data");
                                String id, name, image, phone, gender, CountryCode, available, plasma, transfusion, country, city, address, latitude, longitude, age, blood_type, types;
                                id = user.get("id").getAsString();
                                name = user.get("name").getAsString();
                                image = user.get("image").getAsString();
                                phone = user.get("phone").getAsString();
                                country = user.get("country").getAsString();
                                city = user.get("city").getAsString();
                                address = user.get("address").getAsString();
                                latitude = user.get("latitude").getAsString();
                                longitude = user.get("longitude").getAsString();
                                age = user.get("age").getAsString();
                                blood_type = user.get("blood_type").getAsString();
                                available = user.get("available").getAsString();
                                plasma = user.get("donate_plasma").getAsString();
                                transfusion = user.get("blood_tranfusion").getAsString();
                                types = user.get("type").getAsString();
                                CountryCode = user.get("country_code").getAsString();
                                gender = user.get("gender").getAsString();
                                Log.d("Imgjsdfhsdhb", CountryCode);
                                PrefrenceManager.setPlasmaDonation(context, plasma);
                                PrefrenceManager.setGender(context, gender);
                                PrefrenceManager.setTransfusion(context, transfusion);
                                PrefrenceManager.setUserId(context, id);
                                PrefrenceManager.setUserName(context, name);
                                PrefrenceManager.setCountryCode(context, CountryCode);
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
                                PrefrenceManager.setAvailibility(context, available);
                                startActivity(new Intent(ProfileImageActivity.this, MainActivity.class).putExtra("src", "main"));
                                finish();
                            } else {
                                Toast.makeText(ProfileImageActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.d("response__", e.getMessage());
                        }

                    }
                });
    }

    private void init() {
        radioGroup = findViewById(R.id.radio_grp);
        radioMale = findViewById(R.id.radio_male);
        radioFemale = findViewById(R.id.radio_female);
        linearSpinner = findViewById(R.id.linear_spinner);
        spinnerBlood = findViewById(R.id.spinnerblood);
        typeUser = findViewById(R.id.checkBox);
        chkAvaible = findViewById(R.id.checkBox_available);
        ChkPlasma = findViewById(R.id.checkPlasma);
        ChkTransfusion = findViewById(R.id.checkBloodTransfusion);
        ivBack = findViewById(R.id.iv_back);
        ivProfile = findViewById(R.id.profile_image);
        ivPicker = findViewById(R.id.iv_pick_img);
        ivDropDownIcon = findViewById(R.id.ivSpinnerDropDown);
        ivServicesIcon = findViewById(R.id.iv_services);
        etname = findViewById(R.id.et_name);
        etphone = findViewById(R.id.et_phone);
        etage = findViewById(R.id.et_age);
        picAddress = findViewById(R.id.location);
        btnSubmit = findViewById(R.id.btn_submit);
        String[] blod = {getString(R.string.select_blood_group), "A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"};
        adapter = new ArrayAdapter(this, R.layout.spinner_custom, blod);
        spinnerBlood.setAdapter(adapter);
        if (PrefrenceManager.getDefaultLang(ProfileImageActivity.this).equalsIgnoreCase("arabic")) {
            linearSpinner.setRotation(180);
            ivServicesIcon.setRotation(180);
            ivDropDownIcon.setRotation(180);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.LEFT | Gravity.CENTER;
            ivDropDownIcon.setLayoutParams(params);
            spinnerBlood.setRotation(180);
            ivDropDownIcon.setPadding(0, 0, 20, 0);
            spinnerBlood.setPadding(0, 0, 50, 0);
            //ivServicesIcon.setPadding(10,10,10,10);
            //spinnerServices.setBa
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
