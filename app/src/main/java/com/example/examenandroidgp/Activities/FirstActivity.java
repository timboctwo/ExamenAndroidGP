package com.example.examenandroidgp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.examenandroidgp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FirstActivity extends AppCompatActivity implements OnMapReadyCallback {

    @BindView(R.id.editTextNumberOfMarkers)
    EditText editTextNumberOfMarkers;

    private GoogleMap gMap;
    private Random r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        r = new Random();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        LatLng latLng = new LatLng(-34f, 151f);
        gMap.addMarker(new MarkerOptions().position(latLng).title("Marker inSyfney"));
        gMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    @OnClick(R.id.btnCreateMarkers)
    public void createRandomMarkers(View v){
        if (checkEmptyField()){
            Snackbar.make(findViewById(android.R.id.content),"Debes ingresar un numero", 3000).show();
        }else{
            displayRandomMarkers(editTextNumberOfMarkers.getText().toString().trim());
        }
    }

    private boolean checkEmptyField(){
        return (editTextNumberOfMarkers.getText().toString().trim().isEmpty());
    }

    private void displayRandomMarkers(String numberOfMarkers){
        /** Cleans the map and deletes previous markers **/
        gMap.clear();

        int iterations = Integer.parseInt(numberOfMarkers);
        for (int i = 0; i < iterations; i++){
            LatLng randomLatLng = generateRandomLatLng();
            gMap.addMarker(new MarkerOptions().position(randomLatLng).title("Marker "+ (i +1)));
        }

    }

    private LatLng generateRandomLatLng(){
        float lat = 0;
        float lng = 0;
        lat = -90 + r.nextFloat() * (90 - (-90));
        lng = -180 + r.nextFloat() * (180 - (-180));

        return new LatLng(lat, lng);
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
