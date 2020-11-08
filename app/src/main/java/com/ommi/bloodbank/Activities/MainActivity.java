package com.ommi.bloodbank.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
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

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.ommi.bloodbank.Fragments.ChatFragment;
import com.ommi.bloodbank.Fragments.FilterFragment;
import com.ommi.bloodbank.Fragments.RequestFragment;
import com.ommi.bloodbank.Fragments.RewardFragment;
import com.ommi.bloodbank.Fragments.ServicesFragment;
import com.ommi.bloodbank.PrefrenceManager;
import com.ommi.bloodbank.R;

import java.util.List;
import java.util.Locale;

import static android.widget.Toast.LENGTH_SHORT;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    boolean chk = false;
    int res = 0, check = 0;
    private static final int REQUEST_CHECK_SETTINGS = 999;
    private Spinner SpinnerLang;
    private BroadcastReceiver receiver;
    private String Language = "English";
    private LinearLayout linearToolbar, linearFragment, linearBottomNav, linearBootomNav2, linearFragmentLayout;
    private ImageView ivSetting, ivNotification, ivDontionLayer, ivDonnar, ivRequest, ivChat, ivHereos, ivServices, ivAddServices, ivFlag;
    private ImageView ivDontionLcayer, ivRequestLayer, ivCharLayer, ivHereoesLayer, ivServicesLayer;
    private TextView tvTitle, tvNofitication;
    private FrameLayout frameLayoutNotification;
    private LinearLayout linearDonnar, linearAccepter, linearServices, linearHereoes, linearChat;
    FusedLocationProviderClient client;
    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location location = locationResult.getLastLocation();
            if (location != null) {
                PrefrenceManager.setUserLattitude(MainActivity.this, location.getLatitude() + "");
                PrefrenceManager.setUserLongitude(MainActivity.this, location.getLongitude() + "");
                Log.d("location_", location.getLatitude() + " , " + location.getLongitude());
            } else {
                Toast.makeText(MainActivity.this, "Location null", LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        FirebaseMessaging.getInstance().subscribeToTopic(PrefrenceManager.getUserId(this));
        requestPermission();
//        createLocationRequest();
        init();
//        IntentFilter filter = new IntentFilter("com.broadcast.fro.message");
        /*receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(context, "Broadcast Receive", LENGTH_SHORT).show();
            }
        };
        registerReceiver(receiver, filter);*/
        clickListener();
    }

    @Override
    protected void onDestroy() {
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
        super.onDestroy();
    }


    protected void createLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                Log.d("position_", "success");
                requestLocationUpdates();
            }
        });
        task.addOnFailureListener(this, new OnFailureListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    try {
                        Log.d("position_", e.getMessage());
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        // Ignore the error.
                        Log.d("position_", e.getMessage());
                    }
                }
            }
        });
    }

    private void requestLocationUpdates() {
        Log.d("Location__", "called");
        LocationRequest request = new LocationRequest();
        request.setInterval(20000);
        request.setFastestInterval(20000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        client = LocationServices.getFusedLocationProviderClient(this);
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            client.requestLocationUpdates(request, locationCallback, null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                requestLocationUpdates();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    private void clickListener() {

        SpinnerLang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (++check > 1) {
                    if (position == 0) {
                        Language = "English";
                    } else if (position == 1) {
                        Language = "Arabic";
                    } else {
                        Language = "French";
                    }
//                    Language = parent.getItemAtPosition(position).toString();
                    ContextCompat.getColor(MainActivity.this, R.color.colorWhite);
                    PrefrenceManager.setDefaultLang(MainActivity.this, Language);
                    String lang = "en";
                    if (position == 0) {
                        lang = "en";
                    } else if (position == 2) {
                        lang = "fr";
                    } else if (position == 1) {
                        lang = "ar";
                    }
                    Resources resources = getResources();
                    DisplayMetrics dm = resources.getDisplayMetrics();
                    Configuration config = resources.getConfiguration();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        config.setLocale(new Locale(lang.toLowerCase()));
                    } else {
                        config.locale = new Locale(lang.toLowerCase());
                    }
                    resources.updateConfiguration(config, dm);
                    startActivity(getIntent());
                    //recreate();

                }
//                Toast.makeText(RequsetBloodActivity.this,  parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        linearDonnar.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                unselect();
                ivFlag.setVisibility(View.GONE);
                ivSetting.setVisibility(View.VISIBLE);
                ivAddServices.setVisibility(View.GONE);
                frameLayoutNotification.setVisibility(View.VISIBLE);
                select(ivDonnar, ivDontionLayer);
                replaceFragment(new FilterFragment());
                tvTitle.setText(getString(R.string.app_name_));

            }
        });
        linearAccepter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unselect();
                ivAddServices.setVisibility(View.GONE);
                ivFlag.setVisibility(View.VISIBLE);
                ivSetting.setVisibility(View.VISIBLE);
                Glide.with(MainActivity.this)
                        .load("https://www.countryflags.io/" + PrefrenceManager.getCountryCode(MainActivity.this) + "/shiny/64.png")
                        .centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .into(ivFlag);
                frameLayoutNotification.setVisibility(View.VISIBLE);
                select(ivRequest, ivRequestLayer);
                replaceFragment(new RequestFragment());
                tvTitle.setText(getString(R.string.title_request));
            }
        });
        linearChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unselect();
                ivFlag.setVisibility(View.GONE);
                ivAddServices.setVisibility(View.GONE);
                ivSetting.setVisibility(View.VISIBLE);
                frameLayoutNotification.setVisibility(View.VISIBLE);
                select(ivChat, ivCharLayer);
                replaceFragment(new ChatFragment());
                tvTitle.setText(getString(R.string.title_chats));
            }
        });
        linearServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unselect();
                ivSetting.setVisibility(View.VISIBLE);
                ivFlag.setVisibility(View.GONE);
                ivAddServices.setVisibility(View.VISIBLE);
                frameLayoutNotification.setVisibility(View.GONE);
                select(ivServices, ivServicesLayer);
                replaceFragment(new ServicesFragment());
                tvTitle.setText(getString(R.string.title_services));
            }
        });
        linearHereoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unselect();
                ivSetting.setVisibility(View.VISIBLE);
                ivFlag.setVisibility(View.GONE);
                ivAddServices.setVisibility(View.GONE);
                frameLayoutNotification.setVisibility(View.VISIBLE);
                select(ivHereos, ivHereoesLayer);
                replaceFragment(new RewardFragment());
                tvTitle.setText(getString(R.string.title_heroes));

            }
        });

        ivSetting.setOnClickListener(this);
        ivNotification.setOnClickListener(this);
        ivAddServices.setOnClickListener(this);

    }

    private void select(ImageView imageView, ImageView imageViewLayer) {
        //imageView.setPadding(0, 0, 0, 0);
        imageViewLayer.setVisibility(View.GONE);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) imageView.getLayoutParams();
        //params.setMargins(0, 0, 0, 10);
        imageView.setLayoutParams(params);
    }

    private void unselect() {
        /*ivRequest.setPadding(15, 15, 15, 15);
        ivDonnar.setPadding(15, 15, 15, 15);
        ivChat.setPadding(15, 15, 15, 15);
        ivServices.setPadding(15, 15, 15, 15);
        ivHereos.setPadding(15, 15, 15, 15);*/
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) ivRequest.getLayoutParams();
        ViewGroup.MarginLayoutParams donnar = (ViewGroup.MarginLayoutParams) ivDonnar.getLayoutParams();
        ViewGroup.MarginLayoutParams chat = (ViewGroup.MarginLayoutParams) ivChat.getLayoutParams();
        ViewGroup.MarginLayoutParams services = (ViewGroup.MarginLayoutParams) ivServices.getLayoutParams();
        ViewGroup.MarginLayoutParams heroes = (ViewGroup.MarginLayoutParams) ivHereos.getLayoutParams();
       /* params.setMargins(0, 10, 0, 0);
        donnar.setMargins(0, 10, 0, 0);
        chat.setMargins(0, 10, 0, 0);
        heroes.setMargins(0, 10, 0, 0);
        services.setMargins(0, 10, 0, 0);*/
        ivRequest.setLayoutParams(params);
        ivChat.setLayoutParams(chat);
        ivServices.setLayoutParams(services);
        ivDonnar.setLayoutParams(donnar);
        ivHereos.setLayoutParams(heroes);
        ivDontionLayer.setVisibility(View.VISIBLE);
        ivRequestLayer.setVisibility(View.VISIBLE);
        ivCharLayer.setVisibility(View.VISIBLE);
        ivServicesLayer.setVisibility(View.VISIBLE);
        ivHereoesLayer.setVisibility(View.VISIBLE);

    }

    private void requestPermission() {
        Dexter.withContext(this).withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            createLocationRequest();
                        } else {
                            Toast.makeText(MainActivity.this, "Permission Denied", LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void init() {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        linearToolbar = findViewById(R.id.linear_toolbar);
        tvNofitication = findViewById(R.id.tv_notification_number);
        frameLayoutNotification = findViewById(R.id.frame_notification);
        linearFragment = findViewById(R.id.linear_fragment);
        linearBottomNav = findViewById(R.id.bottom_navigation_linear);
        linearBootomNav2 = findViewById(R.id.linearBottomNav);
        linearDonnar = findViewById(R.id.linear_donnar);
        SpinnerLang = findViewById(R.id.spinner_land);
        linearAccepter = findViewById(R.id.linear_accepter);
        linearHereoes = findViewById(R.id.linear_hereos);
        linearChat = findViewById(R.id.linear_chat);
        linearServices = findViewById(R.id.linear_services);
        ivSetting = findViewById(R.id.iv_setting);
        ivNotification = findViewById(R.id.iv_nottification);
        ivDonnar = findViewById(R.id.iv_donnar);
        ivDontionLayer = findViewById(R.id.iv_donnar_layer);
        ivRequestLayer = findViewById(R.id.iv_request_layer);
        ivCharLayer = findViewById(R.id.iv_chat_layer);
        ivServicesLayer = findViewById(R.id.iv_services_layer);
        ivHereoesLayer = findViewById(R.id.iv_hereoes_layer);
        ivRequest = findViewById(R.id.iv_request);
        ivHereos = findViewById(R.id.iv_hereos);
        ivServices = findViewById(R.id.iv_services);
        ivAddServices = findViewById(R.id.iv_add_event);
        ivFlag = findViewById(R.id.iv_flag);
        ivChat = findViewById(R.id.iv_chat);
        linearFragmentLayout = findViewById(R.id.linear_fragment_layout);
        tvTitle = findViewById(R.id.tv_titlebar_title);
        if (getIntent().getStringExtra("src").equalsIgnoreCase("inbox")) {
            replaceFragment(new ChatFragment());

        } else {

            replaceFragment(new FilterFragment());
        }
        int height = displayMetrics.heightPixels;
        float scaleFactor = displayMetrics.density;
        String blood[] = {"English", "عربي", "Français"};
        ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, R.layout.spinner_custom_lang, blood);
        SpinnerLang.setAdapter(adapter);
        int pos;

        if (PrefrenceManager.getDefaultLang(MainActivity.this).equalsIgnoreCase("english")) {
            pos = 0;
        } else if (PrefrenceManager.getDefaultLang(MainActivity.this).equalsIgnoreCase("arabic")) {
            pos = 1;
        } else {
            pos = 2;
        }
        SpinnerLang.setSelection(pos);
        if (height / scaleFactor <= 600) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1);
            LinearLayout.LayoutParams l2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 8);
            LinearLayout.LayoutParams l3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1);
            linearToolbar.setLayoutParams(lp);
            linearBottomNav.setLayoutParams(l3);
            linearFragmentLayout.setLayoutParams(l2);

        }

        if (PrefrenceManager.getDefaultLang(MainActivity.this).equalsIgnoreCase("arabic")) {
            linearBootomNav2.setRotation(180);
            ivDonnar.setRotation(180);
            ivChat.setRotation(180);
            ivServices.setRotation(180);
            ivHereos.setRotation(180);
            ivRequest.setRotation(180);

        }

        unselect();
        select(ivDonnar, ivDontionLayer);


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_setting:
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                break;
            case R.id.iv_nottification:
                tvNofitication.setVisibility(View.GONE);
                startActivity(new Intent(MainActivity.this, NotificationActivity.class));
                break;
            case R.id.iv_add_event:
                startActivity(new Intent(MainActivity.this, AddServicesActivity.class));
                break;
        }

    }


    private void replaceFragment(Fragment donnerFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.linear_fragment, donnerFragment);
//        transaction.addToBackStack(null);
        transaction.commit();

    }

    @Override
    public void onBackPressed() {

        if (chk) {
            chk = false;
            finishAffinity();
        } else {
            chk = true;
            Toast.makeText(this, "press again to exit!", LENGTH_SHORT).show();
        }
    }
}
