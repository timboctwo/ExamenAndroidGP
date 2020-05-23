package com.example.examenandroidgp.MyVolley;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class MyVolley {

    private static final int MY_DEFAULT_TIMEOUT = 15000;

    private Context context;
    private RequestQueue queue;

    public MyVolley(Context context){
        this.context = context;
    }

    private RequestQueue getInstance(){
        if (queue == null){
            queue = Volley.newRequestQueue(context);
        }
        return queue;
    }

    public void makeuUPXRequest(Response.Listener<JSONObject> listener, Response.ErrorListener errorListener,JSONObject params){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                "https://apisls.upaxdev.com/task/initial_load",
                params,
                listener,
                errorListener
        );
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        getInstance().add(jsonObjectRequest);
    }

}
