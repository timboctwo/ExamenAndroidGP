package com.example.examenandroidgp.Activities.ThirdActivity;

import android.app.Application;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;

import com.example.examenandroidgp.R;

public class ThirdActivityViewModel extends AndroidViewModel {

    public ThirdActivityViewModel(Application application) {
        super(application);
    }

    public Button generateButton(){
        Button btn = new Button(getApplication());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(16, 16,16,16);
        btn.setTypeface(null, Typeface.BOLD);
        btn.setText("Botton creado desde la clase Java");
        btn.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        btn.setTextSize(12f);
        btn.setTextColor(getApplication().getResources().getColor(android.R.color.white));
        btn.setBackgroundColor(getApplication().getResources().getColor(R.color.colorPrimaryDark));
        btn.setLayoutParams(params);
        return btn;

    }

    public void startAsyncTask(){
        new WaitAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private class WaitAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getApplication().getApplicationContext(), "Iniciando Hilo ", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Toast.makeText(getApplication().getApplicationContext(), "Hubo un error al iniciar el Thread", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplication().getApplicationContext(), "El tiempo ha terminado, no se interrumpio el Hilo principal", Toast.LENGTH_SHORT).show();
        }
    }
}
