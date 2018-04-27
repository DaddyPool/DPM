package com.daddypool.dpm;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<JSONObject> {
    private TextView textView;
    private TextView textDiffData;
    private TextView textHashData;
    private TextView textBalData;
    private TextView textPaidData;
    private TextView textWorkersData;
    private TextView textMinerData;
    private TextView textHashu;
    private EditText editText;
    private String fileName = "Address.txt";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //>>  広告用
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        //<<  広告用

        textDiffData = findViewById(R.id.textDiffData);
        textHashData = findViewById(R.id.textHashData);
        textBalData = findViewById(R.id.textBalData);
        textPaidData = findViewById(R.id.textPaidData);
        textWorkersData = findViewById(R.id.textWorkersData);
        textMinerData = findViewById(R.id.textMinerData);
        textHashu = findViewById(R.id.textHash);
        // JSONの取得
        getLoaderManager().restartLoader(1, null, this);
        getLoaderManager().restartLoader(2, null, this);
     //   WebView  myWebView = (WebView)findViewById(R.id.WebView);
        //標準ブラウザをキャンセル
     //   myWebView.setWebViewClient(new WebViewClient());
        //アプリ起動時に読み込むURL
     //   myWebView.loadUrl("http://daddy-pool.work/api/worker_stats?ZfYHAhLooYjJDUtKmzqA1ybkmVgz1Vimxe");


        textView = findViewById(R.id.textView2);
        editText = findViewById(R.id.editText);

        //保存してあるアドレスがあれば読み込んで表示する
        String str = readFile(fileName);
        if (str != null) {
            textView.setText(str);
        } else {
            textView.setText(R.string.read_error);
        }

        Button buttonSave = findViewById(R.id.button);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // エディットテキストのテキストを取得
                String text = editText.getText().toString();

                saveFile(fileName, text);
                if(text.length() == 0){
                    textView.setText(R.string.no_text);
                }
                else{
                    textView.setText(R.string.saved);
                }
                String str = readFile(fileName);
                if (str != null) {
                    textView.setText(str);
                } else {
                    textView.setText(R.string.read_error);
                }

            }
        });
    }

    // ファイルを保存
    public void saveFile(String file, String str) {

        // try-with-resources
        try (FileOutputStream fileOutputstream = openFileOutput(file,
                Context.MODE_PRIVATE);){

            fileOutputstream.write(str.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // ファイルを読み出し
    public String readFile(String file) {
        String text = null;

        // try-with-resources
        try (FileInputStream fileInputStream = openFileInput(file);
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(fileInputStream, "UTF-8"));
        ) {

            String lineBuffer;
            while ((lineBuffer = reader.readLine()) != null) {
                text = lineBuffer;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return text;
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public Loader<JSONObject> onCreateLoader(int id, Bundle args) {

        String urlText = "";

        if (id == 1){

         urlText = "http://zny.daddy-pool.work/api/worker_stats?ZfYHAhLooYjJDUtKmzqA1ybkmVgz1Vimxe";

        }
        if (id == 2){
            urlText = "http://zny.daddy-pool.work/api/stats";
        }
        AsyncJsonLoader jsonLoader = new AsyncJsonLoader(this, urlText);
        jsonLoader.forceLoad();
        return  jsonLoader;

    }

    @Override
    public void onLoadFinished(Loader<JSONObject> loader, JSONObject data) {
        if (data != null) {

            try {
                if (loader.getId()==1){
                    JSONObject jsonObject = data.getJSONObject("workers");


                    textDiffData.setText(jsonObject.getJSONObject("ZfYHAhLooYjJDUtKmzqA1ybkmVgz1Vimxe").getString("diff"));
                    textHashData.setText(jsonObject.getJSONObject("ZfYHAhLooYjJDUtKmzqA1ybkmVgz1Vimxe").getString("hashrateString"));
                    textBalData.setText(jsonObject.getJSONObject("ZfYHAhLooYjJDUtKmzqA1ybkmVgz1Vimxe").getString("balance"));
                    textPaidData.setText(jsonObject.getJSONObject("ZfYHAhLooYjJDUtKmzqA1ybkmVgz1Vimxe").getString("paid"));
                }
                if (loader.getId()==2){
                    JSONObject jsonObject = data.getJSONObject("pools");
                    textWorkersData.setText(jsonObject.getJSONObject("bitzeny").getString("workerCount"));
                    textMinerData.setText(jsonObject.getJSONObject("bitzeny").getString("minerCount"));
                    textHashu.setText(jsonObject.getJSONObject("bitzeny").getString("hashrateString"));
                }


//                textView3.setText(jsonObject.getString("ZfYHAhLooYjJDUtKmzqA1ybkmVgz1Vimxe"));
            } catch (JSONException e) {
                Log.d("onLoadFinished","JSONのパースに失敗しました。 JSONException=" + e);
            }
        }else{
            Log.d("onLoadFinished", "onLoadFinished error!");
        }
    }

    @Override
    public void onLoaderReset(Loader<JSONObject> loader) {
        // 処理なし
    }
}
