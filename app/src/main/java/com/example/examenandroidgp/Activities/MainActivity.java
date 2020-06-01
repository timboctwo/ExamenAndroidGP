package com.example.examenandroidgp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.examenandroidgp.Activities.FirstActivity.FirstActivity;
import com.example.examenandroidgp.Activities.FourthActivity.FourthActivity;
import com.example.examenandroidgp.Activities.SecondActivity.SecondActivity;
import com.example.examenandroidgp.Activities.ThirdActivity.ThirdActivity;
import com.example.examenandroidgp.R;

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

