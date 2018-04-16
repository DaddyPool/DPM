package com.daddypool.dpm;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AsyncJsonLoader extends AsyncTaskLoader<JSONObject> {
private String urlText;

public AsyncJsonLoader(Context context, String urlText){
        super(context);
        this.urlText = urlText;
        }

@Override
public JSONObject loadInBackground(){
        HttpURLConnection connection = null;

        try{
        URL url = new URL(urlText);
        connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        }
        catch (MalformedURLException exception){
        // 処理なし
        }
        catch (IOException exception){
        // 処理なし
        }

        try{
        BufferedInputStream inputStream = new BufferedInputStream(connection.getInputStream());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1){
        if (length > 0){
        outputStream.write(buffer, 0, length);
        }
        }

        JSONObject json = new JSONObject(new String(outputStream.toByteArray()));
        return json;
        }
        catch (IOException exception){
        // 処理なし
        }
        catch (JSONException e) {
        e.printStackTrace();
        }
        return null;
        }
        }