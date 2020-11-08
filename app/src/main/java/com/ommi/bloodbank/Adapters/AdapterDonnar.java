package com.ommi.bloodbank.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ommi.bloodbank.Activities.AllDonnarRequestDetailActivity;
import com.ommi.bloodbank.Activities.InboxActivity;
import com.ommi.bloodbank.Activities.RequestAnnouncementActivity;
import com.ommi.bloodbank.Models.ModelConversation;
import com.ommi.bloodbank.Models.ModelDonnar;
import com.ommi.bloodbank.Models.ModelRequest;
import com.ommi.bloodbank.PrefrenceManager;
import com.ommi.bloodbank.R;
import com.ommi.bloodbank.ServicesUrls;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterDonnar extends RecyclerView.Adapter<AdapterDonnar.CustomDonnar> {
    private String other_id = null, myId, image, blood, type, name, convertionId, date;
    public AdapterDonnar(Context context, List<ModelDonnar> listDonnar, List<ModelRequest> ListRequest, String type,RecyclerView recyclerView) {
        this.context = context;
        this.ListDonnar = listDonnar;
        this.ListRequest = ListRequest;
        this.Type = type;
        this.recyclerView=recyclerView;
    }
    RecyclerView recyclerView;
    private Context context;
    private List<ModelDonnar> ListDonnar;
    private List<ModelRequest> ListRequest;
    private String Type;
    private int lastPosition = -1;

    @NonNull
    @Override
    public CustomDonnar onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_donnar, parent, false);
        return new CustomDonnar(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomDonnar holder, final int position) {
        setAnimation(holder.itemView,position);
        Log.d("type__",Type);



        if (Type.equalsIgnoreCase("donnar")) {
            if(position==ListDonnar.size()-1)
            {
                LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(16,0,16,20);
                holder.linearCustom.setLayoutParams(layoutParams);
            }
            holder.Name.setText(ListDonnar.get(position).getName());
            holder.Addres.setText(ListDonnar.get(position).getAddress());
            holder.Phone.setText(ListDonnar.get(position).getPhone());
            holder.Group.setText(ListDonnar.get(position).getGroup());
            holder.linearCustom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, AllDonnarRequestDetailActivity.class);
                    intent.putExtra("position_",position+"");
                    intent.putExtra("type","donnar");
                    context.startActivity(intent);
                }
            });
            Glide
                    .with(context)
                    .load(ServicesUrls.IMAGE_URL + ListDonnar.get(position).getImg())
                    .centerCrop()
                    .placeholder(R.drawable.placeholder)
                    .into(holder.circleImageView);
            holder.ivCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts(
                            "tel", ListDonnar.get(position).getPhone(), null));
                    context.startActivity(phoneIntent);
                }
            });
            holder.ivMessege.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    other_id = ListDonnar.get(position).getId();
                    image = ListDonnar.get(position).getImg();
                    name = ListDonnar.get(position).getName();
                    type = ListDonnar.get(position).getType();
                    blood = ListDonnar.get(position).getGroup();
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
    else

    {
        if(position==ListRequest.size()-1)
        {
            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(15,0,15,230);
            holder.linearCustom.setLayoutParams(layoutParams);
        }else
        {
             LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(15,0,15,2);
            holder.linearCustom.setLayoutParams(layoutParams);
        }

        holder.Name.setText(ListRequest.get(position).getName());
        holder.Addres.setText(ListRequest.get(position).getAddress());
        holder.Phone.setText(ListRequest.get(position).getPhone());
        holder.Group.setText(ListRequest.get(position).getBlood());
        holder.ivCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts(
                        "tel", ListRequest.get(position).getPhone(), null));
                context.startActivity(phoneIntent);
            }
        });
        holder.linearCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, AllDonnarRequestDetailActivity.class);
                intent.putExtra("type","accepter");
                intent.putExtra("position_",position+"");
                context.startActivity(intent);
            }
        });
        Glide
                .with(context)
                .load(ServicesUrls.IMAGE_URL + RequestAnnouncementActivity.userDataList.get(position).getImage())
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .into(holder.circleImageView);
        holder.ivMessege.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                other_id = RequestAnnouncementActivity.userDataList.get(position).getId();
                image = RequestAnnouncementActivity.userDataList.get(position).getImage();
                name = RequestAnnouncementActivity.userDataList.get(position).getName();
                type = RequestAnnouncementActivity.userDataList.get(position).getUsertype();
                blood = RequestAnnouncementActivity.userDataList.get(position).getBloodGroup();
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


    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        if(Type.equalsIgnoreCase("donnar")) {
            return ListDonnar.size();
        }
        else  {
            return ListRequest.size();
        }

    }

    public class CustomDonnar extends RecyclerView.ViewHolder{

        private TextView Name,Group,Phone,Addres;
        private CircleImageView circleImageView;
        private LinearLayout linearCustom;
        private ImageView ivCall,ivMessege;
        public CustomDonnar(@NonNull View itemView) {
            super(itemView);
            Name=itemView.findViewById(R.id.tv_name);
            Addres=itemView.findViewById(R.id.tv_address);
            Phone=itemView.findViewById(R.id.tv_phone);
            Group =itemView.findViewById(R.id.tv_group);
            linearCustom =itemView.findViewById(R.id.linear_custom);
            circleImageView =itemView.findViewById(R.id.civ_profile);
            ivCall =itemView.findViewById(R.id.iv_phone);
            ivMessege =itemView.findViewById(R.id.iv_messege);
        }
    }
    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
    private void setAnimation(View viewToAnimate, int position)
    {
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            animation.setDuration(1000);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
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
    private void goToChatScreen() {
        Intent intent = new Intent(context, InboxActivity.class);
        intent.putExtra("con_id", convertionId);
        intent.putExtra("image", image);
        intent.putExtra("name", name);
        intent.putExtra("id", other_id);
        intent.putExtra("src","conversation");
        Log.d("CHAT_DATA",convertionId+"_"+image+"_"+name+"_"+other_id);
        context.startActivity(new Intent(intent));

    }
}
