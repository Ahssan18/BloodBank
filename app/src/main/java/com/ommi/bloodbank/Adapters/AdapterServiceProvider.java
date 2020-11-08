package com.ommi.bloodbank.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ommi.bloodbank.Models.ModelServiceProvider;
import com.ommi.bloodbank.R;
import com.ommi.bloodbank.ServicesUrls;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class AdapterServiceProvider extends RecyclerView.Adapter<AdapterServiceProvider.ServiceProviderView> {

    private Context context;

    public AdapterServiceProvider(Context context, List<ModelServiceProvider> list) {
        this.context = context;
        this.list = list;
    }

    private List<ModelServiceProvider> list;
    private int lastPosition = -1;
    @NonNull
    @Override
    public ServiceProviderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.custom_serviceprovider,parent,false);
        return new ServiceProviderView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceProviderView holder, final int position) {
        setAnimation(holder.itemView,position);
      holder.Name.setText(list.get(position).getName());
      holder.Phone.setText(list.get(position).getPhone());
      holder.Address.setText(list.get(position).getAddress());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.Desc.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
        }
      holder.Desc.setText(list.get(position).getParagraph());
        Glide.with(context)
                .load(ServicesUrls.IMAGE_URL_SERVICES+list.get(position).getImage())
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .into(holder.circleImageView);
      holder.Call.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts(
                      "tel", list.get(position).getPhone(), null));
              context.startActivity(phoneIntent);
          }
      });
      holder.Messege.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              // The number on which you want to send SMS
              context.startActivity(new Intent(Intent.ACTION_SENDTO, Uri.fromParts("sms", list.get(position).getPhone(), null)));
          }
      });
      if(list.size()-1==position)
      {
          holder.linearLayout.setPadding(0,0,0,20);
      }
    }
    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            animation.setDuration(1000);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ServiceProviderView extends RecyclerView.ViewHolder{
        private TextView Name,Phone,Address,Desc;
        private ImageView Call,Messege;
        private LinearLayout linearLayout;
        private CircleImageView circleImageView;
        public ServiceProviderView(@NonNull View itemView) {
            super(itemView);
            Name=itemView.findViewById(R.id.tv_name);
            Address=itemView.findViewById(R.id.tv_address);
            circleImageView=itemView.findViewById(R.id.civ_profile);
            Phone=itemView.findViewById(R.id.tv_phone);
            Desc=itemView.findViewById(R.id.tv_description);
            Call=itemView.findViewById(R.id.iv_phone);
            Messege =itemView.findViewById(R.id.iv_messege);
            linearLayout =itemView.findViewById(R.id.linear_custom);
        }
    }
}
