package com.example.examenandroidgp.Activities.FirstActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.examenandroidgp.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FirstActivity extends AppCompatActivity implements OnMapReadyCallback {

    @BindView(R.id.editTextNumberOfMarkers)
    EditText editTextNumberOfMarkers;

    private GoogleMap gMap;
    private FirstActivityViewModel firstActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        firstActivityViewModel = new ViewModelProvider(this).get(FirstActivityViewModel.class);
        final Observer<String> observer = new Observer<String>() {
            @Override
            public void onChanged(String quantity) {
                ArrayList<MarkerOptions> randomMarkers = firstActivityViewModel.getRandomMarkers(quantity);
                setRandomMarkers(randomMarkers);
            }
        };

        firstActivityViewModel.getMarkersQuantity().observe(this, observer);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
    }

    private void setRandomMarkers(ArrayList<MarkerOptions> randomMarkers){
        gMap.clear();
        for (MarkerOptions marker: randomMarkers) {
            gMap.addMarker(marker);
        }
    }

    @OnClick(R.id.btnCreateMarkers)
    public void createRandomMarkers(View v){
        if (checkEmptyField()){
            Snackbar.make(findViewById(android.R.id.content),"Debes ingresar un numero", 3000).show();
        }else{
            firstActivityViewModel.setMarkersQuantity(editTextNumberOfMarkers.getText().toString().trim());
        }
    }

    private boolean checkEmptyField(){
        return (editTextNumberOfMarkers.getText().toString().trim().isEmpty());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
