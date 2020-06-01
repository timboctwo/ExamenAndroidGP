package com.example.examenandroidgp.Activities.FirstActivity;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.examenandroidgp.Activities.FirstActivity.ActivityFirstUseCase;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class FirstActivityViewModel extends AndroidViewModel {

    private MutableLiveData<String> markersQuantity;

    public FirstActivityViewModel(Application application) {
        super(application);
        markersQuantity = new MutableLiveData<>();
    }

    public LiveData<String> getMarkersQuantity(){
        return markersQuantity;
    }

    public void setMarkersQuantity(String quantity){
        markersQuantity.setValue(quantity);
    }

    public ArrayList<MarkerOptions> getRandomMarkers(String quantity){
        return ActivityFirstUseCase.createRandomMarkers(quantity);
    }



}
