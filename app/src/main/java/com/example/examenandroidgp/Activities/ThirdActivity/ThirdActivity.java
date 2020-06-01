package com.example.examenandroidgp.Activities.ThirdActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.examenandroidgp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ThirdActivity extends AppCompatActivity {

    @BindView(R.id.lnrLytActivityThird)
    LinearLayout lnrLytActivityThird;

    private ThirdActivityViewModel thirdActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        ButterKnife.bind(this);

        thirdActivityViewModel = new ViewModelProvider(this).get(ThirdActivityViewModel.class);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        createButton();
    }

    private void createButton(){
        Button button = thirdActivityViewModel.generateButton();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thirdActivityViewModel.startAsyncTask();
            }
        });
        lnrLytActivityThird.addView(button);

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
