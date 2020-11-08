package com.ommi.bloodbank.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.ommi.bloodbank.Adapters.MessageAdapter;
import com.ommi.bloodbank.Models.ModelConversation;
import com.ommi.bloodbank.Models.ModelMessage;
import com.ommi.bloodbank.MySingleton;
import com.ommi.bloodbank.PrefrenceManager;
import com.ommi.bloodbank.R;
import com.ommi.bloodbank.ServicesUrls;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class InboxActivity extends AppCompatActivity {
    private EmojiconEditText emojiconEditText;
    private LinearLayout rootView, linear_ChatBox;
    private ImageView ivEmoji, ivBack, ivSendData;
    private RecyclerView recyclerViewMesseges;
    private CircleImageView ivProfile;
    private int check = 0, check2 = 0;
    String text;
    private MessageAdapter messageAdapter;
    private TextView tvName;
    private List<ModelMessage> list;
    private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    private String contentType = "application/json";
    private String serverKey = "";
    private String image, conId, otherId, name, Src;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        Objects.requireNonNull(getSupportActionBar()).hide();
        image = getIntent().getExtras().getString("image");
        name = getIntent().getExtras().getString("name");
        conId = getIntent().getExtras().getString("con_id");
        otherId = getIntent().getExtras().getString("id");
        Src = getIntent().getExtras().getString("src");
        serverKey = "key=" + getString(R.string.serverkey);
        FirebaseDatabase.getInstance().getReference().child("Conversations").child(PrefrenceManager.getUserId(this)).child(conId).child("readStatus").setValue("1");
        list = new ArrayList<>();
        init();
        clickListener();
        initFireBaseChat();
    }

    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private void clickListener() {
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = rootView.getRootView().getHeight() - rootView.getHeight();

                if (heightDiff > 100) {
                    Log.e("MyActivity___", "keyboard opened" + heightDiff);
                } else {
                    Log.e("MyActivity___", "keyboard closed" + heightDiff);
                }
            }
        });
        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InboxActivity.this, AllDonnarRequestDetailActivity.class);
                intent.putExtra("type", "detail");
                intent.putExtra("id", otherId);
                startActivity(intent);
                finish();
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Src.equalsIgnoreCase("notification")) {
                    Intent intent = new Intent(InboxActivity.this, MainActivity.class);
                    intent.putExtra("src", "inbox");
                    startActivity(intent);
                    finish();
                } else {
                    onBackPressed();
                }
            }
        });
        ivSendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text = emojiconEditText.getText().toString().trim();
                if (!text.isEmpty()) {
                    sendMessege();
                }
            }
        });
    }

    private void sendMessege() {
        ModelMessage modelMessage = new ModelMessage();
        modelMessage.setMessage(text);
        modelMessage.setName(PrefrenceManager.getUserName(InboxActivity.this));
        modelMessage.setReceiver(otherId);
        modelMessage.setSender(PrefrenceManager.getUserId(InboxActivity.this));
        modelMessage.setDate(getDateTime());
        modelMessage.setStatus("1");
        modelMessage.setLastMessege(text);
        modelMessage.setMessegeStatus("0");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("Conversations")
                .child(PrefrenceManager.getUserId(InboxActivity.this)).child(conId).child(otherId).removeValue();
        FirebaseDatabase.getInstance().getReference("Chat").child(conId).push().setValue(modelMessage);
        FirebaseDatabase.getInstance().getReference("Conversations").child(PrefrenceManager.getUserId(InboxActivity.this)).child(conId).child("date").setValue(getDateTime());
        ModelConversation modelConversation = new ModelConversation();
        modelConversation.setName(PrefrenceManager.getUserName(InboxActivity.this));
        modelConversation.setImage(PrefrenceManager.getUserImage(InboxActivity.this));
        modelConversation.setOtherUserId(PrefrenceManager.getUserId(InboxActivity.this));
        modelConversation.setMyUserID(otherId);
        modelConversation.setConversationId(conId);
        modelConversation.setDate(getDateTime());
        modelConversation.setReadStatus("0");
        modelConversation.setMessege(text);
        modelConversation.setUsetType(PrefrenceManager.getUserType(InboxActivity.this));
        modelConversation.setBloodGroup(PrefrenceManager.getUserBloodGroup(InboxActivity.this));
        database.getReference("Conversations")
                .child(otherId).child(conId).setValue(modelConversation)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("sfhwhg", "success");
                        sendNotification();
                    }
                });

        emojiconEditText.setText("");
    }

    private void sendNotification() {
        JSONObject notification = new JSONObject();
        JSONObject notificationBody = new JSONObject();
        try {
            notificationBody.put("title", PrefrenceManager.getUserName(this));
            notificationBody.put("message", text);
            notification.put("to", "/topics/" + otherId);
            notificationBody.put("id", PrefrenceManager.getUserId(this));
            notificationBody.put("img", PrefrenceManager.getUserImage(this));
            notificationBody.put("conversation_id", conId);
            notification.put("data", notificationBody);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("ondata____", response + "");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ondata____", error + "");

                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private void setData() {
        messageAdapter = new MessageAdapter(InboxActivity.this, list);
        recyclerViewMesseges.setLayoutManager(new LinearLayoutManager(InboxActivity.this));
//        recyclerViewMesseges.smoothScrollToPosition(list.size() - 1);
        ((LinearLayoutManager) Objects.requireNonNull(recyclerViewMesseges.getLayoutManager())).scrollToPositionWithOffset(list.size() - 1, 0);
        recyclerViewMesseges.setAdapter(messageAdapter);
    }

    private void init() {
        list = new ArrayList<>();
        tvName = findViewById(R.id.tv_name);
        ivProfile = findViewById(R.id.civ_profile);
        Glide.with(InboxActivity.this)
                .load(ServicesUrls.IMAGE_URL + image)
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .into(ivProfile);
        tvName.setText(name);
        recyclerViewMesseges = findViewById(R.id.recycle_messages);
        rootView = findViewById(R.id.rootview);
        linear_ChatBox = findViewById(R.id.linear_messge_send);
        emojiconEditText = findViewById(R.id.messageArea);
        ivEmoji = findViewById(R.id.ivEmoji);
        ivBack = findViewById(R.id.iv_back);
        ivSendData = findViewById(R.id.sendButton);
        EmojIconActions emojiIcon = new EmojIconActions(this, rootView, emojiconEditText, ivEmoji, "#495C66", "#DCE1E2", "#E6EBEF");
        emojiIcon.ShowEmojIcon();
        if (PrefrenceManager.getDefaultLang(InboxActivity.this).equalsIgnoreCase("arabic")) {
            ivSendData.setRotation(180);
        }
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
        if (getDifferenceBetweenDates(Date.parse(message.getDate()), Date.parse(getDateTime())) >= 30) {

            FirebaseDatabase.getInstance().getReference("Chat")
                    .child(conId).child(dataSnapshot.getKey()).removeValue();

        }
        list.add(message);
        setData();
    }

    public long getDifferenceBetweenDates(long startDate, long endDate) {
        long different = endDate - startDate;
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
        return different / daysInMilli;
    }
}
