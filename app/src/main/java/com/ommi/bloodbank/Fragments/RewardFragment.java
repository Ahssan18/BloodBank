package com.ommi.bloodbank.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.ommi.bloodbank.Adapters.AdpterReward;
import com.ommi.bloodbank.Models.ModelReward;
import com.ommi.bloodbank.Models.ModelUserData;
import com.ommi.bloodbank.MySingleton;
import com.ommi.bloodbank.PrefrenceManager;
import com.ommi.bloodbank.R;
import com.ommi.bloodbank.ServicesUrls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RewardFragment extends Fragment {

    private View view;
    private RecyclerView recyclerViewReward;
    public static List<ModelReward> RewardList;
    AdpterReward adpterReward;
    public static List<ModelUserData> userDataList;
    private Spinner spinnerMonth, spinnerDay, spinnerYear;
    private TextView tvYear, tvMonth, tvMessege;
    private LinearLayout linearMonth, linearYear, linearDay;
    String input;
    String month[];
    private boolean chk = false;
    String Day, Month, Year;
    private KProgressHUD progressHUD;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_reward, container, false);
        init();
        getActivity().findViewById(R.id.spinner_land).setVisibility(View.GONE);
        //get current date
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        Day = "today" + "";
        //get current month
        DateFormat dateFormat = new SimpleDateFormat("MM");
        Date date = new Date();
        int v = Integer.parseInt(dateFormat.format(date));
        Month = v + "";
        //get current year
        Calendar calendar = Calendar.getInstance();
        int years = calendar.get(Calendar.YEAR);
        Year = years + "";
        month = new String[]{getString(R.string.january), getString(R.string.feb), getString(R.string.march), getString(R.string.april), getString(R.string.may), getString(R.string.june), getString(R.string.jully), getString(R.string.august), getString(R.string.september), getString(R.string.october), getString(R.string.november), getString(R.string.december)};
        String months[] = new String[3];
        for (int i = 0; i < 3; i++) {
            int j = 0;
            if (v == 0) {
                v = 12;
            }
            months[i] = month[v - 1];
            j++;
            v--;
        }
        Log.d("Array_months", months.toString());
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, months);
        spinnerMonth.setAdapter(arrayAdapter);

        Log.d("Year_num", years + "");
        String Years[] = new String[3];
        for (int j = 0; j < 3; j++) {
            Years[j] = years + "";
            years = years - 1;
        }
        ArrayAdapter adapterYear = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, Years);
        spinnerYear.setAdapter(adapterYear);
        String days[] = {getString(R.string.now), getString(R.string.previous), getString(R.string.previous_weak)};
        ArrayAdapter adapterDays = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, days);
        spinnerDay.setAdapter(adapterDays);

        setData();
        clickListener();
        return view;
    }

    private void clickListener() {


        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Year = parent.getItemAtPosition(position).toString();
                if (chk)
                    setData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Month = parent.getItemAtPosition(position).toString();
                for (int i = 0; i < 12; i++) {
                    if (Month.equalsIgnoreCase(month[i])) {
                        Month = i + 1 + "";
                    }
                }
                if (chk)
                    setData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tvMessege.setVisibility(View.GONE);
                Day = parent.getItemAtPosition(position).toString();
                if (position == 0) {
                    Day = "today";
                } else if (position == 1) {
                    Day = "7";
                } else if (position == 2) {
                    Day = "3";
                }

                if (chk)
                    setData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setData() {
        RewardList.clear();
        userDataList.clear();
        if (adpterReward != null) {
            adpterReward.notifyDataSetChanged();
        }
        progressHUD.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServicesUrls.GET_HEREOS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                chk = true;
                progressHUD.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.d("RESPONE_REWARD", response);
                    int status = jsonObject.getInt("status");
                    tvMessege.setVisibility(View.GONE);
                    recyclerViewReward.setVisibility(View.VISIBLE);
                    if (status == 1) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String date, name, countrycode, rating, plasma, BloodTransfusion, noofbloodDonation, points, Status, img, id, phone, country, city, address, lat, lng, age, blooggroup, usertype;
                            date = jsonObject1.getString("created_at");
                            phone = jsonObject1.getString("phone");
                            country = jsonObject1.getString("country");
                            city = jsonObject1.getString("city");
                            address = jsonObject1.getString("address");
                            lat = jsonObject1.getString("latitude");
                            lng = jsonObject1.getString("longitude");
                            age = jsonObject1.getString("age");
                            blooggroup = jsonObject1.getString("blood_type");
                            usertype = jsonObject1.getString("type");
                            id = jsonObject1.getString("id");
                            name = jsonObject1.getString("name");
                            rating = jsonObject1.getString("total_rating");
                            points = jsonObject1.getString("reward");
                            noofbloodDonation = jsonObject1.getString("blood_donate");
                            img = jsonObject1.getString("image");
                            Status = jsonObject1.getString("available");
                            plasma = jsonObject1.getString("donate_plasma");
                            countrycode = jsonObject1.getString("country_code");
                            BloodTransfusion = jsonObject1.getString("blood_tranfusion");
                            ModelUserData modelUserData = new ModelUserData(id, name, phone, img, country, city, address, lat, lng, age, blooggroup, usertype, Status);
                            modelUserData.setPlasma(plasma);
                            modelUserData.setCountryCode(countrycode);
                            modelUserData.setBloodTransfusion(BloodTransfusion);
                            userDataList.add(modelUserData);
                            ModelReward modelReward = new ModelReward(id, name, rating, points, noofbloodDonation, img, date);
                            RewardList.add(modelReward);
                        }
                        initRecycle();

                    } else {
                        tvMessege.setVisibility(View.VISIBLE);
                        recyclerViewReward.setVisibility(View.GONE);


//                        Toast.makeText(getActivity(), "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressHUD.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("id", PrefrenceManager.getUserId(getActivity()));
                hashMap.put("city", PrefrenceManager.getUserCity(getActivity()));
                hashMap.put("country", PrefrenceManager.getUserCountry(getActivity()));
                hashMap.put("day", Day);
                hashMap.put("month", Month);
                hashMap.put("year", Year);
                Log.d("Reward_params", hashMap + "_");
                return hashMap;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    private void initRecycle() {
        adpterReward = new AdpterReward(RewardList, getActivity());
        recyclerViewReward.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewReward.setAdapter(adpterReward);
        adpterReward.notifyDataSetChanged();
    }

    private void init() {
        progressHUD = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        RewardList = new ArrayList<>();
        userDataList = new ArrayList<>();
        spinnerMonth = view.findViewById(R.id.spinner_month);
        spinnerDay = view.findViewById(R.id.spinnerDay);
        spinnerYear = view.findViewById(R.id.spinneyYear);
        recyclerViewReward = view.findViewById(R.id.recycleReward);
        linearMonth = view.findViewById(R.id.linearMonth);
        linearDay = view.findViewById(R.id.linearDays);
        linearYear = view.findViewById(R.id.linearYear);
        tvMessege = view.findViewById(R.id.tv_message);

    }
}
