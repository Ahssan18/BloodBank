package com.ommi.bloodbank.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.ommi.bloodbank.Adapters.AdapterConversation;
import com.ommi.bloodbank.Models.ModelConversation;
import com.ommi.bloodbank.PrefrenceManager;
import com.ommi.bloodbank.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    AdapterConversation adapterConversation;
    private RecyclerView recycleChat;
    private ImageView imageView;
    private List<ModelConversation> conversationList;
    private KProgressHUD progressHUD;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getSupportActionBar().hide();
        init();
        getDataFromFireBase();
        setData();
        clickListener();
    }

    private void clickListener() {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void getDataFromFireBase() {
        //pro//gressHUD.show();
        try {
            FirebaseDatabase.getInstance().getReference().child("Conversations").child(PrefrenceManager.getUserId(ChatActivity.this)).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    if (dataSnapshot != null) {
                        getDataFromSnapshot(dataSnapshot);
                    } else {
                        progressHUD.dismiss();
                    }
                    Log.d("FirebaseData___", "_onChildAdded");

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Log.d("FirebaseData___", "onChildChanged");
                    if (dataSnapshot != null) {
                        Log.d("" +
                                "", dataSnapshot + "");
                        for (int i = 0; i < conversationList.size(); i++) {

                            if (dataSnapshot.getKey().toString().equalsIgnoreCase(conversationList.get(i).getConversationId())) {
                                conversationList.remove(i);
                            }

                        }
                        getDataFromSnapshot(dataSnapshot);

                    }
                    progressHUD.dismiss();

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    Log.d("FirebaseData___", "onChildRemoved");
                    if (dataSnapshot != null) {
//                        getDataFromSnapshot(dataSnapshot);
                    }
                    progressHUD.dismiss();

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Log.d("FirebaseData___", "onChildAdded");
                    progressHUD.dismiss();

                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    progressHUD.dismiss();
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getDataFromSnapshot(DataSnapshot dataSnapshot) {
        progressHUD.dismiss();
        ModelConversation modelConversation = dataSnapshot.getValue(ModelConversation.class);
        conversationList.add(modelConversation);
        setData();
    }

    private void setData() {
        Collections.sort(conversationList, new Comparator<ModelConversation>() {
            @Override
            public int compare(ModelConversation u1, ModelConversation u2) {
                return u2.getDate().compareTo(u1.getDate());
            }
        });
        if (adapterConversation != null) {
            adapterConversation.notifyDataSetChanged();
        }
        recycleChat.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
        adapterConversation = new AdapterConversation(conversationList, ChatActivity.this, recycleChat);
        adapterConversation.notifyDataSetChanged();
        recycleChat.setAdapter(adapterConversation);

    }

    private void init() {
        progressHUD = KProgressHUD.create(ChatActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        recycleChat = findViewById(R.id.recycle_conversation);
        conversationList = new ArrayList<>();
        imageView=findViewById(R.id.iv_back);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}