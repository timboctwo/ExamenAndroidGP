package com.example.examenandroidgp.Activities.FirstActivity;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Random;

public class ActivityFirstUseCase {

    private static Random r;

    private static LatLng generateRandomLatLng(){
        float lat = -90 + r.nextFloat() * (90 - (-90));
        float lng = -180 + r.nextFloat() * (180 - (-180));
        return new LatLng(lat, lng);
    }

    public static ArrayList<MarkerOptions> createRandomMarkers(String quantity){
        ArrayList<MarkerOptions> markerOptions = new ArrayList<>();
        r = new Random();
        for (int i = 0; i < Integer.parseInt(quantity); i++){
            markerOptions.add(new MarkerOptions().position(generateRandomLatLng()).title("Marker "+ (i +1)));
        }
        return markerOptions;
    }
}
