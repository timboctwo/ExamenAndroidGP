package com.example.examenandroidgp.MyVolley;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class MyVolley {

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

    public void makeRequest(Response.Listener<JSONObject> listener, Response.ErrorListener errorListener,String params){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                "http://upaxdev.com/ws/webresources/generic/getData"+params,
                new JSONObject(),
                listener,
                errorListener
        );
        getInstance().add(jsonObjectRequest);
    }

}
