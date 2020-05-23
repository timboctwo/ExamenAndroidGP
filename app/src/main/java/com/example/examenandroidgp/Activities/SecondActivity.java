package com.example.examenandroidgp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.examenandroidgp.BuildConfig;
import com.example.examenandroidgp.MyVolley.MyVolley;
import com.example.examenandroidgp.R;
import com.google.android.gms.common.util.IOUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SecondActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Context context;
    private GoogleMap gMap;
    private MyVolley myVolley;
    private ProgressDialog progressDialog;
    private SimpleDateFormat formatter;
    private BroadcastReceiver receiver;

    private long enqueue;
    private DownloadManager dm;

    private static final String AUTHORITY= BuildConfig.APPLICATION_ID+".provider";
    private static final int REQUEST_STORAGE_PERMISSIONS = 1;
    private static final int BUFFER_SIZE = 4096;
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
        myVolley = new MyVolley(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        initProgressDialog();
        initBroadcastReceiver();
    }

    private void initProgressDialog(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Downloading ZIP");
    }

    @OnClick({R.id.fabDownloadFromCustomServer, R.id.fabDownloadFromUPXServer})
    public void toggleZipRequest(View view){
        switch (view.getId()){
            case R.id.fabDownloadFromCustomServer:
                downloadZIPRoute = "http://beta.timbocktu.com/upx.zip";
                downloadZip(downloadZIPRoute);
                break;
            case R.id.fabDownloadFromUPXServer:
                makeVolleyRequest();
                break;
            default:
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        Snackbar.make(findViewById(android.R.id.content), "Amarillo -> Peticion al servidor UPX\nAzul -> ZIP desde servidor personal", 5000).show();
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

                            Uri zipUri = Uri.parse(c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)));
                            String txtContent = getTxtFileContentFromZip(zipUri);
                            progressDialog.dismiss();
                            Log.i("txtContent", txtContent);
                            generateMarkersFromJSON(txtContent);
                        }
                    }
                }
            }
        };

        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private String getTxtFileContentFromZip(Uri zipUri){
        File zipFile = new File(zipUri.getPath());
        StringBuilder builder = new StringBuilder();
        try {
            ZipFile zip = new ZipFile(zipFile, ZipFile.OPEN_READ);
            ZipEntry zipEntry = zip.entries().nextElement();
            InputStream inputStream = zip.getInputStream(zipEntry);

            BufferedOutputStream bufferedOutputStream = null;

            String zipEntryName = zipEntry.getName();

            File txtFile = new File(upxDirPath+"/" +zipEntryName);

            if (txtFile.exists()) {
                txtFile = new File(upxDirPath+"/"+zipEntryName+formatter.format(new Date()));
            }
            if(zipEntry.isDirectory()){
                txtFile.mkdirs();
            }else{
                byte buffer[] = new byte[BUFFER_SIZE];
                FileOutputStream fileOutputStream = new FileOutputStream(txtFile);
                bufferedOutputStream = new BufferedOutputStream(fileOutputStream, BUFFER_SIZE);
                int count;

                while ((count = inputStream.read(buffer, 0, BUFFER_SIZE)) != -1) {
                    bufferedOutputStream.write(buffer, 0, count);
                }
                bufferedOutputStream.flush();
                bufferedOutputStream.close();

                Uri txtUriFile = Uri.fromFile(txtFile);

                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new InputStreamReader(getContentResolver().openInputStream(txtUriFile)));
                    String line = "";

                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null){
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    private void generateMarkersFromJSON(String rawJSON){
        try {
            JSONObject upxJSON = new JSONObject(rawJSON);
            JSONArray ubicacionesJsonArray = upxJSON.getJSONObject("CARGA_INICIAL").getJSONArray("UBICACIONES");
            for (int i = 0; i < ubicacionesJsonArray.length(); i++){
                JSONObject ubicacion = ubicacionesJsonArray.getJSONObject(i);
                LatLng latLng = new LatLng(ubicacion.getDouble("FNLATITUD"), ubicacion.getDouble("FNLONGITUD"));
                gMap.addMarker(new MarkerOptions().position(latLng).title(ubicacion.getString("FCNOMBRE")));
            }
            /** Coordenadas de México **/
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(23.6345005f, -102.5527878f), 5f));
            Log.i("Ubicaciones", ubicacionesJsonArray.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void makeVolleyRequest(){
        progressDialog.show();
        Map<String, Object> mapParams = new HashMap<>();
        mapParams.put("userId", 89602);
        mapParams.put("env", "dev");
        mapParams.put("os", "android");
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                if (response.has("success")){
                    try {
                        if (response.getBoolean("success")){
                            downloadZIPRoute = response.getString("message");
                            Log.i("downloadZIPRoute", downloadZIPRoute);
                            downloadZip(downloadZIPRoute);
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
        myVolley.makeuUPXRequest(listener, errorListener, new JSONObject(mapParams));
    }

    public void downloadZip(String downloadUrl) {
        if (ActivityCompat.checkSelfPermission(SecondActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(SecondActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_STORAGE_PERMISSIONS);
        }else{
            if (createFolder()){
                generateZipName();
                dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                DownloadManager.Request request = new DownloadManager.Request(
                        Uri.parse(downloadUrl));
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, ZIP_NAME);
                enqueue = dm.enqueue(request);
            }
        }
    }

    private void generateZipName(){
        formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault());
        Date now = new Date();
        ZIP_NAME = "upx"+formatter.format(now)+".zip";
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
                    downloadZip(downloadZIPRoute);
                } else {
                    ActivityCompat.requestPermissions(SecondActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_STORAGE_PERMISSIONS);
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
