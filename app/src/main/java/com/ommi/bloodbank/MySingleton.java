package com.ommi.bloodbank;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MySingleton {

    private static MySingleton instace;
    private RequestQueue requestQueue;
    private static Context context;

    private MySingleton(Context context) {
        this.context=context;
        requestQueue=getRequestQueu();
    }

    private RequestQueue getRequestQueu() {
        if(requestQueue==null)
        {
            requestQueue= Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized MySingleton getInstance(Context context){
       if(instace==null)
       {
           instace=new MySingleton(context);
       }
       return instace;
    }
    public <T> void addToRequestQueue(Request<T> request)
    {
       getRequestQueu().add(request);
    }
}
