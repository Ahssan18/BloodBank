package com.ommi.bloodbank.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ommi.bloodbank.Activities.InboxActivity;
import com.ommi.bloodbank.Models.ModelConversation;
import com.ommi.bloodbank.Models.ModelPublicMesseges;
import com.ommi.bloodbank.Models.ModelVotingRecord;
import com.ommi.bloodbank.PrefrenceManager;
import com.ommi.bloodbank.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AdapterPublicMesseges extends RecyclerView.Adapter<AdapterPublicMesseges.CustomPublicMessegeView> {
    Context context;
    List<ModelPublicMesseges> list;
    private String other_id, image, name, type, blood, convertionId, myId, date;
    int scores, score;
    DatabaseReference mDatabase;
    DatabaseReference votingRef;
    private int lastPosition = -1;
    String key, votevalue, votingval;
    private boolean checkDown = false, checkUp = false;

    public AdapterPublicMesseges(Context context, List<ModelPublicMesseges> list) {
        this.context = context;
        this.list = list;
        mDatabase = FirebaseDatabase.getInstance().getReference().child("publicMesseges");
        votingRef = FirebaseDatabase.getInstance().getReference().child("voting").child(PrefrenceManager.getUserId(context));
    }

    @NonNull
    @Override
    public CustomPublicMessegeView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cutom_post, parent, false);
        return new CustomPublicMessegeView(view);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomPublicMessegeView holder, final int position) {
        //setAnimation(holder.itemView,position);
        if(PrefrenceManager.getDefaultLang(context).equalsIgnoreCase("arabic"))
        {
            holder.tvName.setGravity(Gravity.LEFT);
        }
        if (list.get(position).getId().equalsIgnoreCase(PrefrenceManager.getUserId(context))) {
            holder.linearLayout.setGravity(Gravity.RIGHT);
            holder.tvName.setGravity(Gravity.RIGHT);
            holder.cardPost.setRotation(180);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(15, 0, 0, 0);
            params1.setMargins(80, 0, 10, 10);
            holder.tvScore.setRotation(180);
            holder.cardPost.setLayoutParams(params1);
            holder.ivMessege.setVisibility(View.GONE);
            holder.tvPost.setLayoutParams(params);
            holder.tvPost.setRotation(180);
            holder.ivDown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "you cannot react with your post", Toast.LENGTH_SHORT).show();
                }
            });
            holder.ivUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(context, "you cannot react with your post", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            holder.ivDown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (list.get(position).getMyVote() == 1) {
                        score = list.get(position).getScore() - 1;
                        votevalue = "0";
                        changeFirebaseData(position, score, votevalue);
                    } else if (list.get(position).getMyVote() == 0) {
                        score = list.get(position).getScore() - 1;
                        votevalue = "-1";
                        changeFirebaseData(position, score, votevalue);
                    } else if (list.get(position).getMyVote() == -1) {
                        Toast.makeText(context, "you already vote it", Toast.LENGTH_SHORT).show();
                    }
                }
            });


            holder.ivUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (list.get(position).getMyVote() == 1) {
                        Toast.makeText(context, "you already vote it", Toast.LENGTH_SHORT).show();
                        } else if (list.get(position).getMyVote() == 0) {
                        score = list.get(position).getScore() + 1;
                        votevalue = "+1";
                        changeFirebaseData(position, score, votevalue);
                    } else if (list.get(position).getMyVote() == -1) {
                        score = list.get(position).getScore() + 1;
                        votevalue = "0";
                        changeFirebaseData(position, score, votevalue);
                    }
                    /*votingRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    String id = list.get(position).getId();
                                    key = list.get(position).getKey();
                                    if (ds.child(id).child(key).getKey().equalsIgnoreCase(key)) {
                                        if (ds.child(id).child(key).getKey().equals(key)) {
                                            ModelVotingRecord modelVotingRecord = ds.getValue(ModelVotingRecord.class);
                                            votingval = modelVotingRecord.getVotingVal();
                                            changePositiveVote(position);

                                        }
                                    } else {
                                        createVote(position);
                                        votingval = "0";
                                        changePositiveVote(position);
                                    }

                                }
                            } else {
                                createVote(position);
                                votingval = "0";
                                changePositiveVote(position);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });*/


                }
            });

        }
        holder.tvScore.setText(list.get(position).getScore() + "");
        holder.tvPost.setText(list.get(position).getPost());
        holder.tvName.setText(list.get(position).getName());
        holder.ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, list.get(position).getPost());
                context.startActivity(Intent.createChooser(intent, "Share with"));
            }
        });
        holder.ivMessege.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                other_id = list.get(position).getId();
                image = list.get(position).getImage();
                name = list.get(position).getName();
                type = list.get(position).getType();
                blood = list.get(position).getBloodGroup();
                myId = PrefrenceManager.getUserId(context);
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
                        .addOnCompleteListener((Activity) context, new OnCompleteListener<Void>() {
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



    private void createVote(final int position) {
        ModelVotingRecord modelVotingRecord = new ModelVotingRecord(list.get(position).getKey(), PrefrenceManager.getUserId(context), "0");
        votingRef.push().setValue(modelVotingRecord).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
//                votingRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                            String id = list.get(position).getId();
//                            key = list.get(position).getKey();
//                            Log.d("dashap_child_", ds.child(id).child(key).getKey() + "_" + key);
//                            if (ds.child(id).child(key).getKey().equals(key)) {
//                                ModelVotingRecord modelVotingRecord1 = ds.getValue(ModelVotingRecord.class);
//                                votingval = modelVotingRecord1.getVotingVal();
//                            }
//
//
//                        }
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });

            }
        });
    }
    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            animation.setDuration(600);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
    private void changeFirebaseData(final int position, final int scores,
                                    final String votevalue) {
        ModelVotingRecord modelVotingRecord = new ModelVotingRecord(list.get(position).getKey(), PrefrenceManager.getUserId(context), votevalue);
        votingRef.child(list.get(position).getKey()).setValue(modelVotingRecord).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mDatabase.child(list.get(position).getKey()).child("score").setValue(scores).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        list.get(position).setScore(scores);
                        //notifyDataSetChanged();
                    }
                });
            }
        });
    }

    private void createOtherUserConversation() {
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
                .addOnCompleteListener((Activity) context, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            goToChatScreen();
                        }
                    }
                });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private void goToChatScreen() {
        Intent intent = new Intent(context, InboxActivity.class);
        intent.putExtra("con_id", convertionId);
        intent.putExtra("image", image);
        intent.putExtra("name", name);
        intent.putExtra("id", other_id);
        context.startActivity(new Intent(intent));
        ((Activity) context).finish();
    }

    public class CustomPublicMessegeView extends RecyclerView.ViewHolder {

        private TextView tvPost, tvScore, tvName;
        private ImageView ivShare, ivMessege, ivUp, ivDown;
        private LinearLayout linearLayout;
        private CardView cardPost;

        public CustomPublicMessegeView(@NonNull View itemView) {
            super(itemView);
            tvPost = itemView.findViewById(R.id.tv_post);
            tvName = itemView.findViewById(R.id.tv_name);
            tvScore = itemView.findViewById(R.id.tv_val);
            ivShare = itemView.findViewById(R.id.iv_share);
            ivMessege = itemView.findViewById(R.id.iv_messege);
            ivUp = itemView.findViewById(R.id.iv_arrow_up);
            ivDown = itemView.findViewById(R.id.iv_arrow_down);
            linearLayout = itemView.findViewById(R.id.card_public_messege);
            cardPost = itemView.findViewById(R.id.card_post);
        }
    }
}
