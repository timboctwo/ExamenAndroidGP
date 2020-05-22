package com.example.examenandroidgp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {

    private Activity problemActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btnPrimero,R.id.btnSegundo,R.id.btnTercero,R.id.btnCuarto})
    public void changeActivity(View view){
        switch (view.getId()){
            case R.id.btnPrimero:
                problemActivity = new FirstActivity();
                break;
            case R.id.btnSegundo:
                problemActivity = new SecondActivity();
                break;
            case R.id.btnTercero:
                problemActivity = new ThirdActivity();
                break;
            case R.id.btnCuarto:
                problemActivity = new FourthActivity();
                break;
            default:
                break;
        }
        Intent intent = new Intent(this, problemActivity.getClass());
        startActivity(intent);
    }
}

