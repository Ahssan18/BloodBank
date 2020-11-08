package com.ommi.bloodbank.Activities;

import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.ommi.bloodbank.Adapters.AdapterPublicMesseges;
import com.ommi.bloodbank.Models.ModelPublicMesseges;
import com.ommi.bloodbank.Models.ModelVotingRecord;
import com.ommi.bloodbank.PrefrenceManager;
import com.ommi.bloodbank.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class PublicMessegesActivity extends AppCompatActivity {
    private RecyclerView PublicMessagesRacycle;
    private EditText etPost;
    private ImageView ivBack, ivFlag, ivSend;
    private DatabaseReference reference;
    private List<ModelPublicMesseges> list;
    private List<ModelVotingRecord> listVotes;
    private boolean chk = false;
    private KProgressHUD progressHUD;
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_messeges);
        getSupportActionBar().hide();
        chk = true;
        init();
        clickListener();
        getDataFromFirebase();
    }

    public long getDifferenceBetweenDates(long startDate, long endDate) {
        long different = endDate - startDate;
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
        return different / daysInMilli;
    }

    private void getDataFromFirebase() {
        progressHUD.show();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressHUD.dismiss();
                list.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    final ModelPublicMesseges modelPublicMesseges = ds.getValue(ModelPublicMesseges.class);
                    long difference = getDifferenceBetweenDates(Date.parse(modelPublicMesseges.getDate()), Date.parse(getDateTime()));
                    if (difference >= 30) {
                        reference.child(modelPublicMesseges.getKey()).removeValue();
                    }
                    if (modelPublicMesseges.getCountry().equals(PrefrenceManager.getCountryCode(PublicMessegesActivity.this))) {
                        list.add(modelPublicMesseges);
                    }
                }
                getAllUserVotes();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressHUD.dismiss();
            }
        });

    }

    private void getAllUserVotes() {
        Log.d("sghgshfghsg", list.size() + "_");
        if (list.size() == 0) {
            PublicMessagesRacycle.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            PublicMessagesRacycle.setVisibility(View.VISIBLE);
        }

        DatabaseReference votingRef = FirebaseDatabase.getInstance().getReference().child("voting").child(PrefrenceManager.getUserId(PublicMessegesActivity.this));
        votingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ModelVotingRecord modelVotingRecord = ds.getValue(ModelVotingRecord.class);
                    listVotes.add(modelVotingRecord);
                }

                for (ModelPublicMesseges messages : list) {
                    for (ModelVotingRecord votes : listVotes) {
                        Log.d("voteval___", list.size() + " " + listVotes.size() + " " + messages.getKey() + " " + votes.getMessegeKey());
                        if (messages.getKey().equalsIgnoreCase(votes.getMessegeKey())) {
                            messages.setMyVote(Integer.parseInt(votes.getVotingVal()));
                            Log.d("voteval_if", votes.getMessegeKey());
                        }
                        Log.d("voteval_out", votes.getVotingVal() + ":");
                    }
                    initRecycle();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("errotsfjhsjfh", "firebase error");

            }
        });
    }

    private void initRecycle() {

        AdapterPublicMesseges adapterPublicMesseges = new AdapterPublicMesseges(PublicMessegesActivity.this, list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PublicMessegesActivity.this);
        PublicMessagesRacycle.setLayoutManager(linearLayoutManager);
        ((LinearLayoutManager) Objects.requireNonNull(PublicMessagesRacycle.getLayoutManager())).scrollToPositionWithOffset(list.size() - 1, 0);
        PublicMessagesRacycle.setAdapter(adapterPublicMesseges);
        adapterPublicMesseges.notifyDataSetChanged();

    }

    private void clickListener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String post, country = null, id, image, name, date, type, blood;
                post = etPost.getText().toString();
                Paint paint = new Paint();
                float width = paint.measureText(post);
                if (width > 120) {
                    etPost.setText("");
                    country = PrefrenceManager.getCountryCode(PublicMessegesActivity.this);
                    id = PrefrenceManager.getUserId(PublicMessegesActivity.this);
                    image = PrefrenceManager.getUserImage(PublicMessegesActivity.this);
                    name = PrefrenceManager.getUserName(PublicMessegesActivity.this);
                    type = PrefrenceManager.getUserType(PublicMessegesActivity.this);
                    blood = PrefrenceManager.getUserBloodGroup(PublicMessegesActivity.this);
                    date = getDateTime();
                    String key = reference.push().getKey();
                    ModelPublicMesseges modelPublicMesseges = new ModelPublicMesseges(post, country, id, image, name, date, type, blood, 0, 0, key);
                    modelPublicMesseges.setKey(key);
                    reference.child(key).setValue(modelPublicMesseges);

                } else {
                    Toast.makeText(PublicMessegesActivity.this, "Are you sure you enter wright post!", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private void init() {
        tvEmpty = findViewById(R.id.tv_empty);
        progressHUD = KProgressHUD.create(PublicMessegesActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        list = new ArrayList<>();
        listVotes = new ArrayList<>();
        ivFlag = findViewById(R.id.iv_flag_public);
        PublicMessagesRacycle = findViewById(R.id.recycle_public_messages);
        etPost = findViewById(R.id.messageArea);
        ivSend = findViewById(R.id.sendButton);
        ivBack = findViewById(R.id.iv_back);
        reference = FirebaseDatabase.getInstance().getReference().child("publicMesseges");
        Glide.with(PublicMessegesActivity.this).load("https://www.countryflags.io/" + PrefrenceManager.getCountryCode(PublicMessegesActivity.this) + "/shiny/64.png").centerCrop().placeholder(R.drawable.ik).into(ivFlag);

        if (PrefrenceManager.getDefaultLang(PublicMessegesActivity.this).equalsIgnoreCase("arabic")) {
            ivSend.setRotation(180);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}