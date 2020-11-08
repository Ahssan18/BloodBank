package com.ommi.bloodbank.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ommi.bloodbank.Activities.AllDonnarRequestDetailActivity;
import com.ommi.bloodbank.Fragments.RewardFragment;
import com.ommi.bloodbank.Models.ModelReward;
import com.ommi.bloodbank.PrefrenceManager;
import com.ommi.bloodbank.R;
import com.ommi.bloodbank.ServicesUrls;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdpterReward extends RecyclerView.Adapter<AdpterReward.RewardView> {
    private List<ModelReward> modelRewardList;

    public AdpterReward(List<ModelReward> modelRewardList, Context context) {
        this.modelRewardList = modelRewardList;
        this.context = context;
    }

    private Context context;
    private int lastPosition = -1;

    @NonNull
    @Override
    public RewardView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_reward, parent, false);
        return new RewardView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RewardView holder, final int position) {
        setAnimation(holder.itemView, position);
        if (PrefrenceManager.getDefaultLang(context).equalsIgnoreCase("arabic")) {
            holder.linearReward1.setPadding(30, 0, 0, 0);
            holder.linearReward2.setPadding(30, 0, 0, 0);
            //holder.tvSeq
            ViewGroup.MarginLayoutParams parameter = (FrameLayout.LayoutParams) holder.ivProfile.getLayoutParams();
            parameter.setMargins(0, -5, 35, 0); // left, top, right, bottom
            holder.ivProfile.setLayoutParams(parameter);

            holder.linearRewardPic.setGravity(Gravity.CENTER);
            holder.ivProfile.setPadding(0, 0, 0, 0);
            holder.tvSeq.setGravity(Gravity.RIGHT | Gravity.CENTER);
            holder.tvSeq.setPadding(0, 0, 10, 0);


        }

        holder.tvName.setText(modelRewardList.get(position).getName());
        Glide.with(context)
                .load("https://www.countryflags.io/" + RewardFragment.userDataList.get(position).getCountryCode() + "/shiny/64.png")
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .into(holder.ivFlag);
        holder.tvRating.setText(modelRewardList.get(position).getRating());
        holder.tvPoints.setText(modelRewardList.get(position).getRewardPoints());
        holder.tvBloodDonation.setText(modelRewardList.get(position).getNoofBloodDonation());
        String seq = position + 1 + "";
        holder.tvSeq.setText(seq);
        Log.d("Imge_url", ServicesUrls.IMAGE_URL + modelRewardList.get(position).getImage());
        Glide.with(context)
                .load(ServicesUrls.IMAGE_URL + modelRewardList.get(position).getImage())
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .into(holder.ivProfile);
        holder.ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AllDonnarRequestDetailActivity.class);
                Log.d("POsisufijfhjxshfjhjhb", position + "");
                intent.putExtra("position_", position + "");
                intent.putExtra("type", "reward");
                context.startActivity(intent);
            }
        });
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
        return modelRewardList.size();
    }

    public class RewardView extends RecyclerView.ViewHolder {
        CircleImageView ivProfile;
        TextView tvName, tvRating, tvBloodDonation, tvPoints, tvSeq;
        private View view;
        private ImageView ivFlag;
        private LinearLayout linearReward1, linearReward2, linearRewardPic;

        public RewardView(@NonNull View itemView) {
            super(itemView);
            ivProfile = itemView.findViewById(R.id.civ_profile);
            tvName = itemView.findViewById(R.id.tv_name);
            tvRating = itemView.findViewById(R.id.tv_rating);
            view = itemView.findViewById(R.id.view);
            tvSeq = itemView.findViewById(R.id.tv_seq);
            tvPoints = itemView.findViewById(R.id.tv_points);
            linearReward1 = itemView.findViewById(R.id.linear_reward1);
            linearReward2 = itemView.findViewById(R.id.linear_reward2);
            linearRewardPic = itemView.findViewById(R.id.linear_reward_pic);
            tvBloodDonation = itemView.findViewById(R.id.tv_blood_donation_nums);
            ivFlag = itemView.findViewById(R.id.iv_flag);
        }
    }
}
