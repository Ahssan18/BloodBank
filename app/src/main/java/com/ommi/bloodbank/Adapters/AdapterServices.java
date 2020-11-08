package com.ommi.bloodbank.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ommi.bloodbank.Activities.ServicesProviderActivity;
import com.ommi.bloodbank.Models.ModelServices;
import com.ommi.bloodbank.R;
import com.ommi.bloodbank.ServicesUrls;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterServices extends RecyclerView.Adapter<AdapterServices.ServicesView> {
    private Context context;
    private RecyclerView recyclerView;

    public AdapterServices(Context context, List<ModelServices> servicesList,RecyclerView recyclerView) {
        this.context = context;
        this.servicesList = servicesList;
        this.recyclerView=recyclerView;
    }

    private List<ModelServices> servicesList;
    @NonNull
    @Override
    public ServicesView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.custom_services,parent,false);
        return new ServicesView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServicesView holder, final int position) {
        Glide.with(context)
                .load(ServicesUrls.IMAGE_URL_SERVICES+servicesList.get(position).getImage())
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .into(holder.ivProfile);
        Log.d("URL_IMAGE_SERVICES",ServicesUrls.IMAGE_URL_SERVICES+servicesList.get(position).getImage());
      holder.Name.setText(servicesList.get(position).getName());
      holder.linearServices.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent=new Intent(context, ServicesProviderActivity.class);
              intent.putExtra("id_",servicesList.get(position).getId()+"");
              intent.putExtra("service_name",servicesList.get(position).getName());
              context.startActivity(intent);
          }
      });
      if(servicesList.size()-1==position)
      {
          recyclerView.setPadding(0,0,0,20);
      }
    }

    @Override
    public int getItemCount() {
        return servicesList.size();
    }

    public class ServicesView extends RecyclerView.ViewHolder{
        private ImageView ivProfile;
        private TextView Name;
        private LinearLayout linearServices;
        public ServicesView(@NonNull View itemView) {
            super(itemView);
            ivProfile=itemView.findViewById(R.id.profile_image);
            Name=itemView.findViewById(R.id.tv_name);
            linearServices=itemView.findViewById(R.id.linear_services);
        }
    }
}
