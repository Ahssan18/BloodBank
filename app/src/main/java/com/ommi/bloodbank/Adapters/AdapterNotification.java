package com.ommi.bloodbank.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.ommi.bloodbank.Activities.AdminNotification;
import com.ommi.bloodbank.Activities.AllDonnarRequestDetailActivity;
import com.ommi.bloodbank.Activities.NotificationActivity;
import com.ommi.bloodbank.Models.ModelNotification;
import com.ommi.bloodbank.MySingleton;
import com.ommi.bloodbank.PrefrenceManager;
import com.ommi.bloodbank.R;
import com.ommi.bloodbank.ServicesUrls;
import com.ommi.bloodbank.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class AdapterNotification extends RecyclerView.Adapter<AdapterNotification.NotificationView> {
    public AdapterNotification(List<ModelNotification> list, Context context) {
        this.list = list;
        this.context = context;
        Utils.initLoader(context);
    }

    private List<ModelNotification> list;
    private Context context;
    private int lastPosition = -1;

    @NonNull
    @Override
    public NotificationView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_notification, parent, false);
        return new NotificationView(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final NotificationView holder, final int position) {
        setAnimation(holder.itemView, position);
        holder.RequestBlood.setText(list.get(position).getName());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.BloodType.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
        }
        if (list.get(position).getSender_id().equals("1")) {
            holder.BloodType.setText(list.get(position).getTitle());

        } else {
            holder.BloodType.setText(list.get(position).getName() + context.getString(R.string.lookingFor) + list.get(position).getBlood() + context.getString(R.string.Blood_in) + list.get(position).getAddress());

        }
        holder.ivmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, holder.ivmore);
                popup.getMenuInflater().inflate(R.menu.delete_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.delete:
                                new MaterialAlertDialogBuilder(context)
                                        .setTitle("Delete Notification")
                                        .setMessage("Are you sure,You wanted to delete")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                deleteNotificationApiCall(position);
                                            }
                                        })
                                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        })
                                        .show();
                                break;
                        }
                        return true;
                    }
                });

                popup.show();
            }
        });
        holder.Address.setText(list.get(position).getAddress());
        holder.Date.setText(list.get(position).getDate());
        Glide.with(context)
                .load(ServicesUrls.IMAGE_URL + NotificationActivity.userDataList.get(position).getImage())
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .into(holder.ivProfile);
        holder.linearLayoutNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.get(position).getSender_id().equals("1")) {
                    Intent intent = new Intent(context, AdminNotification.class);
                    intent.putExtra("title", list.get(position).getTitle());
                    intent.putExtra("message", list.get(position).getMsg());
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, AllDonnarRequestDetailActivity.class);
                    intent.putExtra("position_", position + "");
                    intent.putExtra("type", "notification");
                    context.startActivity(intent);
                    ((Activity) context).finish();

                }


            }
        });
    }

    private void deleteNotificationApiCall(final int position) {
        try {
            Utils.showProgress(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServicesUrls.DELETE_NOTIFICATION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    try {
                        Utils.dismissProgress();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.getInt("status");
                    String message = jsonObject.getString("message");
                    if (status == 1) {
                        list.remove(position);
                        Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                    } else {
                        Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    Utils.dismissProgress();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap hashMap = new HashMap();
                hashMap.put("notification_id", list.get(position).getId());
                hashMap.put("user_id", PrefrenceManager.getUserId(context));
                return hashMap;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
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
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class NotificationView extends RecyclerView.ViewHolder {

        private TextView RequestBlood, BloodType, Address, Date;
        private LinearLayout linearLayoutNotification;
        private CircleImageView ivProfile;
        private ImageView ivmore;

        public NotificationView(@NonNull View itemView) {
            super(itemView);
            RequestBlood = itemView.findViewById(R.id.tv_name);
            BloodType = itemView.findViewById(R.id.tv_blood_type);
            Address = itemView.findViewById(R.id.tv_address);
            Date = itemView.findViewById(R.id.tv_date);
            ivProfile = itemView.findViewById(R.id.civ_profile);
            ivmore = itemView.findViewById(R.id.iv_more);
            linearLayoutNotification = itemView.findViewById(R.id.linear_custom);

        }
    }
}
