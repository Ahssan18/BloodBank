package com.ommi.bloodbank.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ommi.bloodbank.Models.ModelReviews;
import com.ommi.bloodbank.PrefrenceManager;
import com.ommi.bloodbank.R;
import com.ommi.bloodbank.ServicesUrls;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterReviews extends RecyclerView.Adapter<AdapterReviews.CustomReview> {
    public AdapterReviews(Context context, List<ModelReviews> list) {
        this.context = context;
        this.list = list;
    }

    private Context context;
    private List<ModelReviews> list;

    @NonNull
    @Override
    public CustomReview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.custom_review,parent,false);
        return new CustomReview(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomReview holder, int position) {
        if(PrefrenceManager.getDefaultLang(context).equalsIgnoreCase("arabic"))
        {

            holder.tvDate.setGravity(Gravity.LEFT);
            holder.tvComment.setGravity(Gravity.RIGHT);
            holder.tvDate.setPadding(20,0,0,0);
        }
     holder.tvName.setText(list.get(position).getName());
     holder.tvDate.setText(list.get(position).getDate());
     holder.tvComment.setText(list.get(position).getReview());
     holder.ratingBar.setRating(list.get(position).getRating());
        Log.d("Imge_Url_review",ServicesUrls.IMAGE_URL+list.get(position).getImage());
        try {
            Glide.with(context)
                    .load(ServicesUrls.IMAGE_URL+list.get(position).getImage())
                    .centerCrop()
                    .placeholder(R.drawable.placeholder)
                    .into(holder.ivProfile);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CustomReview extends RecyclerView.ViewHolder{
        private CircleImageView ivProfile;
        private TextView tvName,tvDate,tvComment;
        private RatingBar ratingBar;
        private ScrollView scrollView;
        public CustomReview(@NonNull View itemView) {
            super(itemView);
            ivProfile=itemView.findViewById(R.id.c_iv);
            tvName=itemView.findViewById(R.id.tv_name);
            tvDate=itemView.findViewById(R.id.tv_date);
            tvComment=itemView.findViewById(R.id.tv_review);
            ratingBar=itemView.findViewById(R.id.rating_bar);
        }
    }
}
