package com.ommi.bloodbank.Activities;

import android.os.Bundle;

import com.ommi.bloodbank.Models.ModelMessage;
import com.ommi.bloodbank.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ChatMessegesActivity extends AppCompatActivity {

    private String image, conId, otherId, name;
    private List<ModelMessage> listMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_messeges);
        image = getIntent().getExtras().getString("image");
        name = getIntent().getExtras().getString("name");
        conId = getIntent().getExtras().getString("con_id");
        otherId = getIntent().getExtras().getString("id");
        listMessages = new ArrayList<>();
        initFireBaseChat();
    }

    private void initFireBaseChat() {
        FirebaseDatabase.getInstance().getReference("Chat")
                .child(conId)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        getDataFromSnapShot(dataSnapshot);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        getDataFromSnapShot(dataSnapshot);
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void getDataFromSnapShot(DataSnapshot dataSnapshot) {
        ModelMessage message = dataSnapshot.getValue(ModelMessage.class);
        listMessages.add(message);
        initRecycleMessage();
    }

    private void initRecycleMessage() {

    }
}
