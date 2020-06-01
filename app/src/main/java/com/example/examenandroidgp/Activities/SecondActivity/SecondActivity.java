package com.example.examenandroidgp.Activities.SecondActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.examenandroidgp.Helpers.Utils;
import com.example.examenandroidgp.MyVolley.MyVolley;
import com.example.examenandroidgp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.ButterKnife;

public class SecondActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Context context;
    private GoogleMap gMap;
    private MyVolley myVolley;
    private ProgressDialog progressDialog;
    private SimpleDateFormat formatter;
    private BroadcastReceiver receiver;
    private SecondActivityViewModel secondActivityViewModel;

    private long enqueue;
    private DownloadManager dm;

    private static final int REQUEST_STORAGE_PERMISSIONS = 1;
    private static final String DIR_UPX = "upx";
    private static String ZIP_NAME = "";
    private String downloadZIPRoute = "";
    private String upxDirPath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        ButterKnife.bind(this);
        context = this;

        secondActivityViewModel = new ViewModelProvider(this).get(SecondActivityViewModel.class);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initProgressDialog();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        initBroadcastReceiver();
    }

    private void initProgressDialog(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Realizando peticion y descarga de ZIP");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        myVolley = new MyVolley(this);
        secondActivityViewModel.setVolley(myVolley);
        gMap = googleMap;

        if (isNetworkAvailable()){
            makeVolleyRequest();
            progressDialog.show();
        }else{
            String message = "Debes estar conectado a internet para realizar la peticion REST";
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                }
            };
            Utils.createAlertDialog(context, message, dialogClickListener);
        }
    }

    private void initBroadcastReceiver(){
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                    long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(enqueue);
                    Cursor c = dm.query(query);
                    if (c.moveToFirst()) {
                        int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                        if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {
                            progressDialog.dismiss();
                            ArrayList<MarkerOptions> markerOptions = secondActivityViewModel.extractMarkersFromZip(upxDirPath, c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)), getContentResolver());
                            loadMarkersIntoMap(markerOptions);
                        }
                    }
                }
            }
        };
        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    private void loadMarkersIntoMap(ArrayList<MarkerOptions> markerOptions){
        for (MarkerOptions marker: markerOptions) {
            gMap.addMarker(marker);
        }
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(23.6345005f, -102.5527878f), 5f));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void makeVolleyRequest(){
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                if (response.has("success")){
                    try {
                        if (response.getBoolean("success")){
                            downloadZIPRoute = response.getString("message");
                            Snackbar.make(findViewById(android.R.id.content),
                                    "Success: "+response.getBoolean("success")+"\n"
                                        +"Code: "+response.getString("code"), 3000).show();
                            checkFilePermissionsForDownload(downloadZIPRoute);
                        }else{
                            Snackbar.make(findViewById(android.R.id.content), "El codigo de respuesta no es correcto", 3000).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Snackbar.make(findViewById(android.R.id.content), "Ocurrio un error en la peticion: "+
                        error.networkResponse.statusCode, 3000).show();
                progressDialog.dismiss();
            }
        };
        secondActivityViewModel.makeVolleyRequest(listener, errorListener);
    }

    private void checkFilePermissionsForDownload(String downloadUrl){
        if (ActivityCompat.checkSelfPermission(SecondActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(SecondActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_STORAGE_PERMISSIONS);
        }else{
            doDownload(downloadUrl);
        }
    }

    private void doDownload(String downloadUrl){
        if (createFolder()){
            ZIP_NAME = secondActivityViewModel.getZipName();
            dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(
                    Uri.parse(downloadUrl));
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, ZIP_NAME);
            enqueue = dm.enqueue(request);
        }
    }

    private boolean createFolder(){
        File fileUpxDir;
        upxDirPath = context.getExternalFilesDir(null).getAbsolutePath()+ "/" + DIR_UPX + "/";
        fileUpxDir = new File(context.getExternalFilesDir(null).getAbsolutePath(), DIR_UPX);
        if (!fileUpxDir.exists()) {
            if (!fileUpxDir.mkdirs()){
                Toast.makeText(context, "Error: No se creo el directorio público "+ DIR_UPX, Toast.LENGTH_SHORT).show();
                return false;
            }else{
                return true;
            }
        }else{
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_STORAGE_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    doDownload(downloadZIPRoute);
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                        String message = "Debes otorgar los permisos de almacenamiento para hacer uso de esta funcion";
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(SecondActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        REQUEST_STORAGE_PERMISSIONS);
                            }
                        };
                        Utils.createAlertDialog(context, message, dialogClickListener);
                    }else{
                        Snackbar.make(findViewById(android.R.id.content), "Por favor, activa los permisos de almacenamiento desde Configuraciones", 3000).show();
                    }
                }
                return;
            }
        }
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
