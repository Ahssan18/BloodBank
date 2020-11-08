package com.ommi.bloodbank.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.FirebaseDatabase;
import com.ommi.bloodbank.Activities.InboxActivity;
import com.ommi.bloodbank.Models.ModelConversation;
import com.ommi.bloodbank.PrefrenceManager;
import com.ommi.bloodbank.R;
import com.ommi.bloodbank.ServicesUrls;

import java.util.List;

public class AdapterConversation extends RecyclerView.Adapter<AdapterConversation.CustomConversation> {

    private RecyclerView recyclerView;
    private List<ModelConversation> modelConversationList;
    private int lastPosition = -1;

    public AdapterConversation(List<ModelConversation> modelConversationList, Context context, RecyclerView recyclerView) {
        this.modelConversationList = modelConversationList;
        this.context = context;
        this.recyclerView = recyclerView;
        Log.d("AdapterConversation", "call form char fragment ");

    }

    private Context context;

    @NonNull
    @Override
    public CustomConversation onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_conversation, parent, false);
        return new CustomConversation(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomConversation holder, final int position) {
        setAnimation(holder.itemView, position);
        if (PrefrenceManager.getDefaultLang(context).equalsIgnoreCase("arabic")) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(300, LinearLayout.LayoutParams.WRAP_CONTENT);
            params1.setMargins(0, 0, 0, 0);
            params.gravity = Gravity.LEFT;
            params1.gravity = Gravity.RIGHT;
            holder.BloodGroup.setLayoutParams(params);
            holder.BloodGroup.setGravity(Gravity.LEFT | Gravity.CENTER);
            holder.BloodGroup.setPadding(110, 0, 0, 0);
            holder.Name.setGravity(Gravity.RIGHT);
            holder.Type.setGravity(Gravity.RIGHT);
            holder.tvNewMsg.setLayoutParams(params1);
            holder.tvNewMsg.setGravity(Gravity.RIGHT);
        }
        if (modelConversationList.get(position).getUsetType().equalsIgnoreCase("donnar"))
            holder.Type.setText("(Donor)");
        holder.BloodGroup.setText(modelConversationList.get(position).getBloodGroup());
        holder.Name.setText(modelConversationList.get(position).getName());
        //holder.Last_messege.setText(modelConversationList.get(position).getMessege());
        Glide.with(context)
                .load(ServicesUrls.IMAGE_URL + modelConversationList.get(position).getImage())
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .into(holder.ivProfile);
        if (modelConversationList.size() - 1 == position) {
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            holder.linearCustom.setPadding(0, 0, 0, 10);
        }
        if (modelConversationList.get(position).getReadStatus().equalsIgnoreCase("0")) {
            holder.tvNewMsg.setVisibility(View.VISIBLE);
            holder.tvNewMsg.setText(modelConversationList.get(position).getMessege());
        } else {
            holder.tvNewMsg.setVisibility(View.GONE);
        }
        holder.linearConversation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, InboxActivity.class);
                intent.putExtra("image", modelConversationList.get(position).getImage());
                intent.putExtra("name", modelConversationList.get(position).getName());
                intent.putExtra("con_id", modelConversationList.get(position).getConversationId());
                intent.putExtra("id", modelConversationList.get(position).getOtherUserId());
                intent.putExtra("src", "conversation");
                context.startActivity(intent);
            }
        });
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popup = new PopupMenu(context, holder.ivDelete);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.delete_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.delete:
                                showAlertDelete(position);
                                break;
                        }
                        return true;
                    }
                });

                popup.show();
            }
        });
    }

    private void showAlertDelete(final int position) {
        new MaterialAlertDialogBuilder(context)
                .setTitle("Delete Notification")
                .setMessage("Are you sure,You wanted to delete")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteConversationData(position);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }

    private void deleteConversationData(int position) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("Conversations")
                .child(PrefrenceManager.getUserId(context)).child(modelConversationList.get(position).getConversationId())
                .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                                       /* FirebaseDatabase database1=FirebaseDatabase.getInstance();
                                        database1.getReference().child("Chat").child(conId).removeValue();*/
                notifyDataSetChanged();
                Toast.makeText(context, "Delete Successfuly", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            animation.setDuration(1000);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return modelConversationList.size();
    }

    public class CustomConversation extends RecyclerView.ViewHolder {

        private ImageView ivProfile, ivDelete;
        private TextView Name, BloodGroup, Type, Last_messege, tvNewMsg;
        private LinearLayout linearConversation, linearCustom;

        public CustomConversation(@NonNull View itemView) {
            super(itemView);
            ivProfile = itemView.findViewById(R.id.profile_image);
            ivDelete = itemView.findViewById(R.id.iv_delete);
            Name = itemView.findViewById(R.id.tv_name);
            tvNewMsg = itemView.findViewById(R.id.tv_newmsg);
            BloodGroup = itemView.findViewById(R.id.tv_group);
            Type = itemView.findViewById(R.id.tv_type);
            Last_messege = itemView.findViewById(R.id.tv_last_msg);
            linearConversation = itemView.findViewById(R.id.linear_conversation);
            linearCustom = itemView.findViewById(R.id.linear_conversation_custom);
        }
    }
}
