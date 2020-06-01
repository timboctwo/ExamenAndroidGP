package com.example.examenandroidgp.Activities.ThirdActivity;

import android.content.ContentResolver;
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ThirdActivityUseCase {

    private static final int BUFFER_SIZE = 4096;
    private static final String DIR_UPX = "upx";

    private static Format formatter;

    public static String getZipFileName(){
        formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault());
        Date now = new Date();
        return "upx"+formatter.format(now)+".zip";
    }

    public static String getTxtFileContentFromZip(String upxDirPath, Uri zipUri, ContentResolver contentResolver){
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
                txtFile = new File(upxDirPath+"/"+formatter.format(new Date())+zipEntryName);
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
                    reader = new BufferedReader(new InputStreamReader(contentResolver.openInputStream(txtUriFile)));
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

    public static ArrayList<MarkerOptions> generateMarkersFromJSON(String rawJSON){
        ArrayList<MarkerOptions> markers = new ArrayList<>();
        try {
            JSONObject upxJSON = new JSONObject(rawJSON);
            JSONArray ubicacionesJsonArray = upxJSON.getJSONObject("CARGA_INICIAL").getJSONArray("UBICACIONES");
            for (int i = 0; i < ubicacionesJsonArray.length(); i++){
                JSONObject ubicacion = ubicacionesJsonArray.getJSONObject(i);
                LatLng latLng = new LatLng(ubicacion.getDouble("FNLATITUD"), ubicacion.getDouble("FNLONGITUD"));
                markers.add(new MarkerOptions().position(latLng).title(ubicacion.getString("FCNOMBRE")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return markers;
    }

}
