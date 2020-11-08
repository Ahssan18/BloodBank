package com.ommi.bloodbank.Adapters;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ommi.bloodbank.Models.ModelMessage;
import com.ommi.bloodbank.PrefrenceManager;
import com.ommi.bloodbank.R;

import java.util.List;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageView> {
    private Context context;
    private List<ModelMessage> list;

    public MessageAdapter(Context context, List<ModelMessage> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public MessageView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_messege, parent, false);
        return new MessageView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageView holder, int position) {
        if (list.get(position).getSender().equalsIgnoreCase(PrefrenceManager.getUserId(context))) {
            Log.d("aasdnhj", "Sender id_" + list.get(position).getSender());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(100, 10, 10, 10);
            holder.layoutMessage.setGravity(Gravity.RIGHT);
            holder.linearMessege.setLayoutParams(layoutParams);
            holder.linearMessege.setBackgroundResource(R.drawable.bg_cardmessege_red);
            holder.tvMessege.setGravity(Gravity.RIGHT);
            holder.tvMessege.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            Log.d("Match", position + "_" + list.get(position).getSender());
        } else {
            holder.tvMessege.setGravity(Gravity.LEFT);

            holder.layoutMessage.setGravity(Gravity.LEFT);
        }
        holder.tvMessege.setText(list.get(position).getMessage());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.tvMessege.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
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

    public class MessageView extends RecyclerView.ViewHolder {
        private TextView tvMessege;
        private LinearLayout layoutMessage;
        private LinearLayout linearMessege;

        public MessageView(@NonNull View itemView) {
            super(itemView);
            tvMessege = itemView.findViewById(R.id.tv_message);
            layoutMessage = itemView.findViewById(R.id.layoutMessage);
            linearMessege = itemView.findViewById(R.id.linear_messeges);
        }
    }
}

