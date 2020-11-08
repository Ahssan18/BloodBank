package com.ommi.bloodbank.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ommi.bloodbank.Adapters.AdapterServices;
import com.ommi.bloodbank.Models.ModelServices;
import com.ommi.bloodbank.MySingleton;
import com.ommi.bloodbank.PrefrenceManager;
import com.ommi.bloodbank.R;
import com.ommi.bloodbank.ServicesUrls;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ServicesFragment extends Fragment {

    private RecyclerView recyclerViewServices;
    private List<ModelServices> servicesList;
    private View view;
    private KProgressHUD progressHUD;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_services, container, false);
        init();
        setData();
        return view;
    }

    private void setData() {
        allServicesApi();
    }

    private void allServicesApi() {
       progressHUD.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServicesUrls.All_SERVICES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               progressHUD.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.d("sercices_list",response+"_");
                    int status = jsonObject.getInt("status");
                    if (status == 1) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject donnar = jsonArray.getJSONObject(i);
                            String img, name, id;
                            name = donnar.getString("name");
                            img = donnar.getString("image");
                            id = donnar.getString("id");
                            ModelServices modelServices = new ModelServices(id, img, name);
                            servicesList.add(modelServices);
                        }
                        initRecycler();
                    } else {
                        Toast.makeText(getActivity(), "" + jsonObject.getString("messege"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressHUD.dismiss();
                Log.d("onErrorResponse", error + "");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap=new HashMap();
                hashMap.put("lang", PrefrenceManager.getDefaultLang(getActivity()));
                Log.d("languade",PrefrenceManager.getDefaultLang(getActivity()));
                return hashMap;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    private void initRecycler() {
        recyclerViewServices.setLayoutManager(new GridLayoutManager(getActivity(),2));
        AdapterServices adapterServices = new AdapterServices(getActivity(), servicesList, recyclerViewServices);
        recyclerViewServices.setAdapter(adapterServices);
    }


    private void init() {
        progressHUD = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        recyclerViewServices = view.findViewById(R.id.recycle_sercices);
        servicesList = new ArrayList<>();
        getActivity().findViewById(R.id.spinner_land).setVisibility(View.GONE);
    }

}
