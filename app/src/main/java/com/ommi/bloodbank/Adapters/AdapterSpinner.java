package com.ommi.bloodbank.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ommi.bloodbank.Models.ModelServiceCategories;
import com.ommi.bloodbank.R;

import java.util.List;

public class AdapterSpinner extends ArrayAdapter {
    public AdapterSpinner(Context context, List<ModelServiceCategories> list) {
        super(context, 0,list);
        this.context = context;
        this.list = list;
    }

    Context context;
    List<ModelServiceCategories> list;

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }
    private View initView(int position,View convertview,ViewGroup Parent)
    {
        if(convertview==null)
        {
            convertview= LayoutInflater.from(context).inflate(R.layout.custom_spinner,Parent,false);

        }
        TextView textView=convertview.findViewById(R.id.text1);
        textView.setText( list.get(position).getServiveName());
        return convertview;
    }
}
