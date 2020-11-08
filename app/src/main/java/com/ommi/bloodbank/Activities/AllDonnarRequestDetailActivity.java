package com.ommi.bloodbank.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.ommi.bloodbank.Adapters.AdapterReviews;
import com.ommi.bloodbank.Fragments.RewardFragment;
import com.ommi.bloodbank.Models.ModelConversation;
import com.ommi.bloodbank.Models.ModelDonnar;
import com.ommi.bloodbank.Models.ModelReviews;
import com.ommi.bloodbank.MySingleton;
import com.ommi.bloodbank.PrefrenceManager;
import com.ommi.bloodbank.R;
import com.ommi.bloodbank.ServicesUrls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllDonnarRequestDetailActivity extends AppCompatActivity {

    DatabaseReference myRef;
    private String other_id = null, myId, image, blood, type, name, convertionId, date, Status;
    private ImageView ivBack, ivPhone, ivMessege, ivArrow;
    private String Name, Image, Blood, Phone, Address, Age, Type, comment, Id;
    private int position;
    ModelDonnar modelDonnar;
    private AdapterReviews adapterReviews;
    private Float Rating = Float.parseFloat("1.0");
    private int chk = 0;
    private KProgressHUD progressHUD;
    private List<ModelReviews> list;
    private RecyclerView recyclerReview;
    private CheckBox ChkPlasma, ChkTransfusion;
    private ScrollView scrollView;
    private MaterialButton BtnSubmit;
    private CircleImageView Profile;
    private RatingBar ratingBar;
    private EditText etComment;
    private CardView cardAllreviews, cardReviewFrom;
    private TextView tvName, tvBlood, tvPhone, tvAddress, tvAge, tvRating, tvDonationNum, tvScore, tvAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_donnar_request_detail);
        getSupportActionBar().hide();
        try {
            Type = getIntent().getStringExtra("type");
            Id = getIntent().getStringExtra("id");
            position = Integer.parseInt(getIntent().getStringExtra("position_"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        init();
        setData();
        clickListener();
    }

    private void setData() {
        if (Type.equalsIgnoreCase("accepter")) {
            Phone = RequestAnnouncementActivity.userDataList.get(position).getPhone();
            Glide
                    .with(AllDonnarRequestDetailActivity.this)
                    .load(ServicesUrls.IMAGE_URL + RequestAnnouncementActivity.userDataList.get(position).getImage())
                    .centerCrop()
                    .placeholder(R.drawable.placeholder)
                    .into(Profile);
            tvName.setText(RequestAnnouncementActivity.userDataList.get(position).getName());
            tvBlood.setText(RequestAnnouncementActivity.userDataList.get(position).getBloodGroup());
            tvAddress.setText(RequestAnnouncementActivity.userDataList.get(position).getAddress() + " " + RequestAnnouncementActivity.userDataList.get(position).getCity() + " " + RequestAnnouncementActivity.userDataList.get(position).getCountry());
            tvAge.setText(RequestAnnouncementActivity.userDataList.get(position).getAge());
            tvPhone.setText(RequestAnnouncementActivity.userDataList.get(position).getPhone());
            String plasma, bloodTransfusion;
            plasma = RequestAnnouncementActivity.userDataList.get(position).getPlasma();
            bloodTransfusion = RequestAnnouncementActivity.userDataList.get(position).getBloodTransfusion();
            if (plasma.equalsIgnoreCase("0")) {
                ChkPlasma.setVisibility(View.GONE);
            } else {
                ChkPlasma.setVisibility(View.VISIBLE);
            }
            if (bloodTransfusion.equalsIgnoreCase("0")) {
                ChkTransfusion.setVisibility(View.GONE);
            } else {
                ChkTransfusion.setVisibility(View.VISIBLE);
            }
            tvDonationNum.setText(RequestAnnouncementActivity.searchtList.get(position).getDonationTime() + "");
            tvScore.setText(RequestAnnouncementActivity.searchtList.get(position).getReward() + "");
            tvRating.setText(RequestAnnouncementActivity.searchtList.get(position).getTotalRating() + "");
        } else
            if (Type.equalsIgnoreCase("detail")) {
            getUserData();
        } else
            if (Type.equalsIgnoreCase("donnar")) {
            tvAvailable.setVisibility(View.VISIBLE);
            getAllReviews();
            cardReviewFrom.setVisibility(View.VISIBLE);
            cardAllreviews.setVisibility(View.VISIBLE);
            Phone = FilterResultActivity.searchlist.get(position).getPhone();
            Glide
                    .with(AllDonnarRequestDetailActivity.this)
                    .load(ServicesUrls.IMAGE_URL + FilterResultActivity.searchlist.get(position).getImg())
                    .centerCrop()
                    .placeholder(R.drawable.placeholder)
                    .into(Profile);
            tvName.setText(FilterResultActivity.searchlist.get(position).getName());
            tvBlood.setText(FilterResultActivity.searchlist.get(position).getGroup());
            tvAddress.setText(FilterResultActivity.searchlist.get(position).getAddress() + " " + FilterResultActivity.searchlist.get(position).getCity() + " " + FilterResultActivity.searchlist.get(position).getCountry());
            tvAge.setText(FilterResultActivity.searchlist.get(position).getAge());
            tvPhone.setText(FilterResultActivity.searchlist.get(position).getPhone());
            if (FilterResultActivity.searchlist.get(position).getStatus().equalsIgnoreCase("0")) {
                Status = getString(R.string.available);
            } else {
                Status = getString(R.string.not_available);
            }
            String plasma, bloodTransfusion;
            plasma = FilterResultActivity.searchlist.get(position).getPlasma();
            bloodTransfusion = FilterResultActivity.searchlist.get(position).getBloodTransfusion();
            if (plasma.equalsIgnoreCase("0")) {
                ChkPlasma.setVisibility(View.GONE);
            } else {
                ChkPlasma.setVisibility(View.VISIBLE);
            }
            if (bloodTransfusion.equalsIgnoreCase("0")) {
                ChkTransfusion.setVisibility(View.GONE);
            } else {
                ChkTransfusion.setVisibility(View.VISIBLE);
            }
            tvAvailable.setText(Status);
            tvDonationNum.setText(FilterResultActivity.searchlist.get(position).getDonationTime() + "");
            tvScore.setText(FilterResultActivity.searchlist.get(position).getReward() + "");
            tvRating.setText(FilterResultActivity.searchlist.get(position).getTotalRating() + "");


        }
            else if (Type.equalsIgnoreCase("notification")) {
            Phone = NotificationActivity.userDataList.get(position).getPhone();
            Glide
                    .with(AllDonnarRequestDetailActivity.this)
                    .load(ServicesUrls.IMAGE_URL + NotificationActivity.userDataList.get(position).getImage())
                    .centerCrop()
                    .placeholder(R.drawable.placeholder)
                    .into(Profile);
            String plasma, bloodTransfusion;
            plasma = NotificationActivity.userDataList.get(position).getPlasma();
            bloodTransfusion = NotificationActivity.userDataList.get(position).getBloodTransfusion();
            if (plasma.equalsIgnoreCase("0")) {
                ChkPlasma.setVisibility(View.GONE);
            } else {
                ChkPlasma.setVisibility(View.VISIBLE);
            }
            if (bloodTransfusion.equalsIgnoreCase("0")) {
                ChkTransfusion.setVisibility(View.GONE);
            } else {
                ChkTransfusion.setVisibility(View.VISIBLE);
            }
            tvName.setText(NotificationActivity.userDataList.get(position).getName());
            tvBlood.setText(NotificationActivity.userDataList.get(position).getBloodGroup());
            tvAddress.setText(NotificationActivity.userDataList.get(position).getAddress() + " " + NotificationActivity.userDataList.get(position).getCity() + " " + NotificationActivity.userDataList.get(position).getCountry());
            tvAge.setText(NotificationActivity.userDataList.get(position).getAge());
            tvPhone.setText(NotificationActivity.userDataList.get(position).getPhone());
            tvDonationNum.setText(NotificationActivity.NotificationList.get(position).getDonationTime() + "");
            tvScore.setText(NotificationActivity.NotificationList.get(position).getReward() + "");
            tvRating.setText(NotificationActivity.NotificationList.get(position).getTotalRating() + "");
        } else
            if (Type.equalsIgnoreCase("reward")) {
            tvAvailable.setVisibility(View.VISIBLE);
            cardAllreviews.setVisibility(View.VISIBLE);
            TranslateAnimation animate = new TranslateAnimation(
                    0,                 // fromXDelta
                    0,                 // toXDelta
                    recyclerReview.getHeight(),  // fromYDelta
                    0);                // toYDelta
            animate.setDuration(1500);
            animate.setFillAfter(true);
            recyclerReview.startAnimation(animate);
            getAllReviews();
            if (RewardFragment.userDataList.get(position).getStatus().equalsIgnoreCase("0")) {
                Status = getString(R.string.available);
            } else {
                Status = getString(R.string.not_available);
            }
            String plasma, bloodTransfusion;
            plasma = RewardFragment.userDataList.get(position).getPlasma();
            bloodTransfusion = RewardFragment.userDataList.get(position).getBloodTransfusion();
            if (plasma.equalsIgnoreCase("0")) {
                ChkPlasma.setVisibility(View.GONE);
            } else {
                ChkPlasma.setVisibility(View.VISIBLE);
            }
            if (bloodTransfusion.equalsIgnoreCase("0")) {
                ChkTransfusion.setVisibility(View.GONE);
            } else {
                ChkTransfusion.setVisibility(View.VISIBLE);
            }
            tvAvailable.setText(Status);
            Phone = RewardFragment.userDataList.get(position).getPhone();
            Glide
                    .with(AllDonnarRequestDetailActivity.this)
                    .load(ServicesUrls.IMAGE_URL + RewardFragment.userDataList.get(position).getImage())
                    .centerCrop()
                    .placeholder(R.drawable.placeholder)
                    .into(Profile);
            tvName.setText(RewardFragment.userDataList.get(position).getName());
            tvBlood.setText(RewardFragment.userDataList.get(position).getBloodGroup());
            tvAddress.setText(RewardFragment.userDataList.get(position).getAddress() + " " + RewardFragment.userDataList.get(position).getCity() + " " + RewardFragment.userDataList.get(position).getCountry());
            tvAge.setText(RewardFragment.userDataList.get(position).getAge());
            tvPhone.setText(RewardFragment.userDataList.get(position).getPhone());
            tvDonationNum.setText(RewardFragment.RewardList.get(position).getNoofBloodDonation() + "");
            tvScore.setText(RewardFragment.RewardList.get(position).getRewardPoints() + "");
            tvRating.setText(RewardFragment.RewardList.get(position).getRating() + "");
        }


    }

    private void getUserData() {
        progressHUD.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServicesUrls.USER_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        progressHUD.dismiss();
                        JSONObject data = jsonObject.getJSONObject("data");
                        String id, name, image, phone, country, city, address, age, bllodType, userType, DonateTime, gender, countrycode, Plasma, transfusion, available, totalrating, reward;
                        id = data.getString("id");
                        name = data.getString("name");
                        image = data.getString("image");
                        phone = data.getString("phone");
                        address = data.getString("address");
                        city = data.getString("city");
                        age = data.getString("age");
                        bllodType = data.getString("blood_type");
                        userType = data.getString("type");
                        gender = data.getString("gender");
                        countrycode = data.getString("country_code");
                        Plasma = data.getString("donate_plasma");
                        transfusion = data.getString("blood_tranfusion");
                        available = data.getString("available");
                        totalrating = data.getString("total_rating");
                        reward = data.getString("reward");
                        DonateTime = data.getString("blood_donate");
                        modelDonnar = new ModelDonnar(id, name, address, age, "", "", "", city, phone, bllodType, image, userType, totalrating, DonateTime, reward, available);
                        modelDonnar.setPlasma(Plasma);
                        modelDonnar.setBloodTransfusion(transfusion);
                        Phone = modelDonnar.getPhone();
                        Glide
                                .with(AllDonnarRequestDetailActivity.this)
                                .load(ServicesUrls.IMAGE_URL + modelDonnar.getImg())
                                .centerCrop()
                                .placeholder(R.drawable.placeholder)
                                .into(Profile);
                        tvName.setText(modelDonnar.getName());
                        tvBlood.setText(modelDonnar.getGroup());
                        tvAddress.setText(modelDonnar.getAddress() + modelDonnar.getAddress());
                        tvAge.setText(modelDonnar.getAge());
                        tvPhone.setText(modelDonnar.getPhone());
                        String plasma, bloodTransfusion;
                        plasma = modelDonnar.getPlasma();
                        bloodTransfusion = modelDonnar.getBloodTransfusion();
                        if (plasma.equalsIgnoreCase("0")) {
                            ChkPlasma.setVisibility(View.GONE);
                        } else {
                            ChkPlasma.setVisibility(View.VISIBLE);
                        }
                        if (bloodTransfusion.equalsIgnoreCase("0")) {
                            ChkTransfusion.setVisibility(View.GONE);
                        } else {
                            ChkTransfusion.setVisibility(View.VISIBLE);
                        }
                        if (available.equalsIgnoreCase("0")) {
                            tvAvailable.setVisibility(View.VISIBLE);
                        }
                        tvDonationNum.setText(modelDonnar.getDonationTime() + "");
                        tvScore.setText(modelDonnar.getReward() + "");
                        tvRating.setText(modelDonnar.getTotalRating() + "");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("user_id", Id);
                return hashMap;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void getAllReviews() {
        // progressHUD.show();
        list.clear();
        if (adapterReviews != null) {
            adapterReviews.notifyDataSetChanged();
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServicesUrls.ALL_REWARS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.getInt("status");
                    if (status == 1) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            String date, name, rating, noofbloodDonation, points, img, id, review;
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            JSONObject feedback = jsonObject1.getJSONObject("feedback");
                            JSONObject user = jsonObject1.getJSONObject("user");
                            date = feedback.getString("created_at");
                            review = feedback.getString("feedback");
                            id = user.getString("id");
                            name = user.getString("name");
                            rating = feedback.getString("rating");
                            img = user.getString("image");
                            ModelReviews modelReviews = new ModelReviews(img, name, review, date, id, Float.parseFloat(rating));
                            list.add(modelReviews);
                        }
                        initRecycle();
                    } else {
                        cardAllreviews.setVisibility(View.GONE);
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
                if (Type.equalsIgnoreCase("donnar")) {
                    hashMap.put("user_id", FilterResultActivity.searchlist.get(position).getId());
                } else {
                    hashMap.put("user_id", RewardFragment.userDataList.get(position).getId());
                }
                Log.d("HAsh_MAp_getReview", hashMap + "_");

                return hashMap;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void initRecycle() {
        cardAllreviews.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                recyclerReview.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(1500);
        animate.setFillAfter(true);
        recyclerReview.startAnimation(animate);
        recyclerReview.setLayoutManager(new LinearLayoutManager(this));
        adapterReviews = new AdapterReviews(AllDonnarRequestDetailActivity.this, list);
        recyclerReview.setAdapter(adapterReviews);
        adapterReviews.notifyDataSetChanged();
    }

    private void clickListener() {


        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (rating < 1.0f) {
                    ratingBar.setRating(1.0f);
                    Rating = Float.parseFloat(String.valueOf(1.0));
                } else {
                    Rating = rating;
                }
            }
        });
        BtnSubmit.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             comment = etComment.getText().toString();

                                             if (!comment.isEmpty()) {
                                                 feedBackApiCall();
                                             } else {
                                                 Toast.makeText(AllDonnarRequestDetailActivity.this, "Type comment", Toast.LENGTH_SHORT).show();
                                             }

                                         }
                                     }
        );
        ivArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chk == 0) {
                    recyclerReview.setVisibility(View.VISIBLE);
                    ivArrow.setImageResource(R.drawable.ic_upward);
                    scrollView.fullScroll(View.FOCUS_DOWN);
                    recyclerReview.scrollToPosition(list.size() - 1);
//                    adapterReviews.notifyDataSetChanged();
                    recyclerReview.setNestedScrollingEnabled(false);

                    chk = 1;
                } else {
                    recyclerReview.setVisibility(View.GONE);
                    ivArrow.setImageResource(R.drawable.ic_down_arrow);
                    chk = 0;
                }
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ivPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts(
                        "tel", Phone, null));
                startActivity(phoneIntent);
            }
        });
        ivMessege.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context context = AllDonnarRequestDetailActivity.this;
                if (Type.equalsIgnoreCase("donnar")) {
                    other_id = FilterResultActivity.searchlist.get(position).getId();
                    image = FilterResultActivity.searchlist.get(position).getImg();
                    name = FilterResultActivity.searchlist.get(position).getName();
                    type = FilterResultActivity.searchlist.get(position).getType();
                    blood = FilterResultActivity.searchlist.get(position).getGroup();

                } else if (Type.equalsIgnoreCase("accepter")) {
                    other_id = RequestAnnouncementActivity.userDataList.get(position).getId();
                    image = RequestAnnouncementActivity.userDataList.get(position).getImage();
                    name = RequestAnnouncementActivity.userDataList.get(position).getName();
                    type = RequestAnnouncementActivity.userDataList.get(position).getUsertype();
                    blood = RequestAnnouncementActivity.userDataList.get(position).getBloodGroup();
                } else if (Type.equalsIgnoreCase("notification")) {
                    other_id = NotificationActivity.userDataList.get(position).getId();
                    image = NotificationActivity.userDataList.get(position).getImage();
                    name = NotificationActivity.userDataList.get(position).getName();
                    type = NotificationActivity.userDataList.get(position).getUsertype();
                    blood = NotificationActivity.userDataList.get(position).getBloodGroup();
                } else if (Type.equalsIgnoreCase("reward")) {
                    other_id = RewardFragment.userDataList.get(position).getId();
                    image = RewardFragment.userDataList.get(position).getImage();
                    name = RewardFragment.userDataList.get(position).getName();
                    type = RewardFragment.userDataList.get(position).getUsertype();
                    blood = RewardFragment.userDataList.get(position).getBloodGroup();
                } else if (Type.equalsIgnoreCase("detail")) {
                    other_id = modelDonnar.getId();
                    image = modelDonnar.getImg();
                    name = modelDonnar.getName();
                    type = modelDonnar.getType();
                    blood = modelDonnar.getGroup();
                }
                myId = PrefrenceManager.getUserId(AllDonnarRequestDetailActivity.this);
                if (Integer.parseInt(myId) < Integer.parseInt(other_id)) {
                    convertionId = myId + other_id;
                } else {
                    convertionId = other_id + myId;
                }
                date = getDateTime();

                final ModelConversation modelConversation = new ModelConversation(image, name, type, blood, myId, other_id, convertionId, date);
                modelConversation.setMessege("");
                modelConversation.setReadStatus("0");
                FirebaseDatabase.getInstance().getReference("Conversations")
                        .child(myId)
                        .child(convertionId)
                        .setValue(modelConversation)
                        .addOnCompleteListener(AllDonnarRequestDetailActivity.this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    createOtherUserConversation();
                                }
                            }
                        });
            }
        });
    }

    private void feedBackApiCall() {
        progressHUD.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServicesUrls.FEED_BACK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    progressHUD.dismiss();
                    int status = jsonObject.getInt("status");
                    if (status == 1) {
                        etComment.setText("");
                        getAllReviews();

                    } else {
                        Toast.makeText(AllDonnarRequestDetailActivity.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
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
                hashMap.put("user_id", PrefrenceManager.getUserId(AllDonnarRequestDetailActivity.this));
                hashMap.put("my_id", FilterResultActivity.searchlist.get(position).getId());
                hashMap.put("feedback", comment);
                hashMap.put("rating", Rating + "");
                Log.d("HAshMap_rate", hashMap + "");
                return hashMap;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private void createOtherUserConversation() {
        Context context = AllDonnarRequestDetailActivity.this;
        String image = PrefrenceManager.getUserImage(context);
        String name = PrefrenceManager.getUserName(context);
        String type = PrefrenceManager.getUserType(context);
        String blood = PrefrenceManager.getUserBloodGroup(context);
        ModelConversation modelConversation = new ModelConversation(image, name, type, blood, other_id, myId, convertionId, date);
        modelConversation.setMessege("");
        modelConversation.setReadStatus("0");
        FirebaseDatabase.getInstance().getReference("Conversations")
                .child(other_id)
                .child(convertionId)
                .setValue(modelConversation)
                .addOnCompleteListener(AllDonnarRequestDetailActivity.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            goToChatScreen();
                        }
                    }
                });
    }

    private void goToChatScreen() {
        Intent intent = new Intent(AllDonnarRequestDetailActivity.this, InboxActivity.class);
        intent.putExtra("con_id", convertionId);
        intent.putExtra("image", image);
        intent.putExtra("name", name);
        intent.putExtra("id", other_id);
        intent.putExtra("src", "conversation");
        startActivity(new Intent(intent));
        finish();
    }

    private void init() {
        progressHUD = KProgressHUD.create(AllDonnarRequestDetailActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        BtnSubmit = findViewById(R.id.btn_submit);
        ratingBar = findViewById(R.id.rating_bar);
        etComment = findViewById(R.id.et_comment);
        list = new ArrayList<>();
        ivPhone = findViewById(R.id.iv_phone);
        ivMessege = findViewById(R.id.iv_messege);
        ivBack = findViewById(R.id.iv_back);
        Profile = findViewById(R.id.profile_image);
        tvName = findViewById(R.id.tv_name);
        tvBlood = findViewById(R.id.tv_blood_type);
        tvPhone = findViewById(R.id.tv_phone);
        tvAvailable = findViewById(R.id.tv_available);
        tvAddress = findViewById(R.id.tv_location);
        tvAge = findViewById(R.id.tv_age);
        ChkPlasma = findViewById(R.id.checkPlasma);
        ChkTransfusion = findViewById(R.id.checkBloodTransfusion);
        tvRating = findViewById(R.id.tv_rating);
        tvDonationNum = findViewById(R.id.tv_blood_donation_nums);
        tvScore = findViewById(R.id.tv_score);
        ivArrow = findViewById(R.id.iv_arrow);
        recyclerReview = findViewById(R.id.recycle_review);
        scrollView = findViewById(R.id.parent_scroll);
        cardAllreviews = findViewById(R.id.card_all_reviews);
        cardReviewFrom = findViewById(R.id.card_review_form);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
