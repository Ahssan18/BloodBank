package com.ommi.bloodbank.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.ommi.bloodbank.Adapters.AdapterConversation;
import com.ommi.bloodbank.Models.ModelConversation;
import com.ommi.bloodbank.PrefrenceManager;
import com.ommi.bloodbank.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ChatFragment extends Fragment {


    AdapterConversation adapterConversation;
    private RecyclerView recycleChat;
    private List<ModelConversation> conversationList;
    private KProgressHUD progressHUD;
    private View view;
    private TextView tvEmpty;
    private BroadcastReceiver receiver;
    private DatabaseReference reference;
    private IntentFilter intentFilter;
    private String ref = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat, container, false);
        getActivity().findViewById(R.id.spinner_land).setVisibility(View.GONE);
        init();
        try {
            ref = getActivity().getIntent().getStringExtra("from");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (ref == null) {
            getDataFromFireBase();
        } else {
            receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    Toast.makeText(context, "Broadcast", Toast.LENGTH_SHORT).show();
                    conversationList.clear();
                    getDataFromFireBase();
                }
            };
        }

        intentFilter = new IntentFilter("com.broadcast.fcm.message");
        getActivity().registerReceiver(receiver, intentFilter);
        return view;
    }

    @Override
    public void onDestroy() {
        if (receiver != null) {
            getActivity().unregisterReceiver(receiver);
        }
        super.onDestroy();
    }

    @Override
    public void onResume() {
        getActivity().registerReceiver(receiver, intentFilter);
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        super.onResume();
    }

    private void getDataFromFireBase() {
        reference = FirebaseDatabase.getInstance().getReference().child("Conversations").child(PrefrenceManager.getUserId(getActivity()));
        progressHUD.show();
        try {
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d("ONDATACHANGECALL", dataSnapshot.exists() + "_");
                    progressHUD.dismiss();
                    conversationList.clear();
                    if (dataSnapshot.exists()) {
                        recycleChat.setVisibility(View.VISIBLE);
                        tvEmpty.setVisibility(View.GONE);
                        reference.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                Log.d("onChildAdded", "_onChildAdded" + dataSnapshot.exists());
                                if (dataSnapshot.exists()) {
                                    getDataFromSnapshot(dataSnapshot);
                                }

                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                if (dataSnapshot.exists()) {
                                    for (int i = 0; i < conversationList.size(); i++) {

                                        if (dataSnapshot.getKey().toString().equalsIgnoreCase(conversationList.get(i).getConversationId())) {
                                            conversationList.remove(i);
                                        }

                                    }
                                    getDataFromSnapshot(dataSnapshot);
                                }


                            }

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                                Log.d("FirebaseData___", "onChildRemoved");
                                if (dataSnapshot != null) {
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

                    } else {
                        recycleChat.setVisibility(View.GONE);
                        tvEmpty.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

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
        Log.d("CONVERSATION_LST__", conversationList.size() + "");
        recycleChat.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapterConversation = new AdapterConversation(conversationList, getActivity(), recycleChat);
        recycleChat.setAdapter(adapterConversation);


    }


    private void init() {
        progressHUD = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        recycleChat = view.findViewById(R.id.recycle_conversation);
        tvEmpty = view.findViewById(R.id.tv_empty);
        conversationList = new ArrayList<>();
    }
}
