package com.example.examenandroidgp.Activities.SecondActivity;

import android.app.Application;
import android.content.ContentResolver;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;

import com.android.volley.Response;
import com.example.examenandroidgp.MyVolley.MyVolley;
import com.example.examenandroidgp.Activities.ThirdActivity.ThirdActivityUseCase;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SecondActivityViewModel extends AndroidViewModel {

    private MyVolley myVolley;

    public SecondActivityViewModel(Application application){
        super(application);
    }

    public void setVolley(MyVolley volley){
        this.myVolley = volley;
    }

    public void makeVolleyRequest(Response.Listener<JSONObject> listener, Response.ErrorListener errorListener){
        Map<String, Object> mapParams = new HashMap<>();
        mapParams.put("userId", 89602);
        mapParams.put("env", "dev");
        mapParams.put("os", "android");
        myVolley.makeuUPXRequest(listener, errorListener, new JSONObject(mapParams));
    }

    public String getZipName(){
        return ThirdActivityUseCase.getZipFileName();
    }

    public ArrayList<MarkerOptions> extractMarkersFromZip(String dirLocation, String uriString, ContentResolver contentResolver){
        Uri zipUri = Uri.parse(uriString);
        String txtContent = ThirdActivityUseCase.getTxtFileContentFromZip(dirLocation, zipUri, contentResolver);
        Log.i("txtContent", txtContent);
        return ThirdActivityUseCase.generateMarkersFromJSON(txtContent);
    }

}
