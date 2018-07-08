package com.daddypool.dpm;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

//>>グラフ追加
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
//<<グラフ追加

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<JSONObject> {
    private TextView textView;
    private TextView textDiffData;
    private TextView textHashData;
    private TextView textBalData;
    private TextView textPaidData;
    private TextView textWorkersData;
    private TextView textMinerData;
    private TextView textHashu;
    private TextView textLuckDayData;
    private TextView textLuckHoursData;
    private TextView textSharesData;
    private EditText editText;
    private String fileName = "Address.txt";
    private String text ="";
    private LineChart mChart;
    private int[] HashHistorys = null;
    private int MaxHash;
    private int MinHash;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ここで1秒間スリープし、スプラッシュを表示させたままにする。
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        // スプラッシュthemeを通常themeに変更する
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);

        //メイン画面用
        setScreenMain();

        // ツールバーをアクションバーとしてセット
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // アイコンを指定
        toolbar.setNavigationIcon(R.drawable.daddypool48);

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
        textLuckDayData = findViewById(R.id.textLuckDayData);
        textLuckHoursData = findViewById(R.id.textLuckHoursData);
        textSharesData = findViewById(R.id.textSharesData);

        textView = findViewById(R.id.textView2);
        editText = findViewById(R.id.editText);

        //保存してあるアドレスがあれば読み込んで表示する
        String str = readFile(fileName);
        if (str != null) {
            textView.setText(str);
            editText.setText(str);
        } else {
            textView.setText(R.string.read_error);
        }
        // エディットテキストのテキストを取得
        text = editText.getText().toString();

        // JSONの取得
        GetJsonData();

       final Button buttonSave = findViewById(R.id.button);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // エディットテキストのテキストを取得
                text = editText.getText().toString();
                // JSONの取得
                GetJsonData();

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

        //QRコード読み取りボタンイベント
        final Button buttonQRRead = findViewById(R.id.btnQRRead);
        buttonQRRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new IntentIntegrator(MainActivity.this).initiateScan();
            }

        });
        //>>グラフ追加
        mChart = findViewById(R.id.line_chart);

        // Grid背景色
        mChart.setDrawGridBackground(true);

        // no description text
        mChart.getDescription().setEnabled(false);

        // Grid縦軸を破線
        XAxis xAxis = mChart.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawLabels(false);
        // 右側の目盛り
        mChart.getAxisRight().setEnabled(false);
        // add data
//        setData();

        mChart.animateX(2500);
        //mChart.invalidate();

        //凡例
        mChart.getLegend().setEnabled(false);
        // dont forget to refresh the drawing
        // mChart.invalidate();
        //<<グラフ追加

        //蛇口WebView用
//        WebView myWebView = (WebView) findViewById(R.id.webView);
//        myWebView.setWebViewClient(new WebViewClient());
//        myWebView.loadUrl("http://daddy.starfree.jp/");

    }

    //蛇口用画面遷移部分
    private void setScreenMain(){
        setContentView(R.layout.activity_main);

        Button sendButton = findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setScreenSub();
            }
        });
    }

    private void setScreenSub(){
        setContentView(R.layout.activity_sub);

        Button returnButton = findViewById(R.id.return_button);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setScreenMain();
            }
        });
    }
    @Override
    //QRコード読み取り後の処理
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        String qrAddress = null;
        qrAddress = result.getContents();
        if(qrAddress != null) {
            //読み取った情報を入力テキストへセット
//            qrAddress.substring(8,34);
            qrAddress.trim();
            editText.setText( qrAddress.substring(8,42));
            Log.d("readQR", qrAddress);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
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

    @Override
    public Loader<JSONObject> onCreateLoader(int id, Bundle args) {

        String urlText = "";

        //MyData 用Json取得　API（http://zny.daddy-pool.work/api/worker_stats?）
        if (id == 1){
//         urlText = "http://zny.daddy-pool.work/api/worker_stats?ZfYHAhLooYjJDUtKmzqA1ybkmVgz1Vimxe";
            urlText = "http://zny.daddy-pool.work/api/worker_stats?"+ text ;
        }

        //PoolStats 用Json取得　API（http://zny.daddy-pool.work/api/stats）
        if (id == 2){
            urlText = "http://zny.daddy-pool.work/api/stats";
        }
        //グラフ 用Json取得　API（http://zny.daddy-pool.work/api/worker_stats?）
        if (id == 3){
//         urlText = "http://zny.daddy-pool.work/api/worker_stats?ZfYHAhLooYjJDUtKmzqA1ybkmVgz1Vimxe";
            urlText = "http://zny.daddy-pool.work/api/worker_stats?"+ text ;
        }
        AsyncJsonLoader jsonLoader = new AsyncJsonLoader(this, urlText);
        jsonLoader.forceLoad();
        return  jsonLoader;

    }

    @Override
    public void onLoadFinished(Loader<JSONObject> loader, JSONObject data) {
        if (data != null) {

            try {
                //MyData の表示
                if (loader.getId()==1){
                    JSONObject jsonObject = data.getJSONObject("workers");

                    textDiffData.setText(jsonObject.getJSONObject(text).getString("diff"));
                    textHashData.setText(jsonObject.getJSONObject(text).getString("hashrateString"));
                    textBalData.setText(jsonObject.getJSONObject(text).getString("balance"));
                    textPaidData.setText(jsonObject.getJSONObject(text).getString("paid"));
                    textLuckDayData.setText(jsonObject.getJSONObject(text).getString("luckDays"));
                    textLuckHoursData.setText(jsonObject.getJSONObject(text).getString("luckHours"));
                    textSharesData.setText(jsonObject.getJSONObject(text).getString("shares"));



                }
                //PoolStatsの表示
                if (loader.getId()==2){
                    JSONObject jsonObject = data.getJSONObject("pools");
                    textWorkersData.setText(jsonObject.getJSONObject("bitzeny").getString("workerCount"));
                    textMinerData.setText(jsonObject.getJSONObject("bitzeny").getString("minerCount"));
                    textHashu.setText(jsonObject.getJSONObject("bitzeny").getString("hashrateString"));
                }
                if (loader.getId()==3){
                    //グラフに値をセット
                    JSONObject jsonObject = data.getJSONObject("history");
                    JSONArray jsonArray = jsonObject.getJSONArray(text);


                    int j = 0;

                    if (jsonArray.length() == 0){return;}
                    if (jsonArray.length() <= 20){
                        HashHistorys = new int[jsonArray.length()];
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            HashHistorys[j] = jsonObject1.getInt("hashrate");
                            j++;
                        }
                    }
                    else{
                        //全てを表示すると多いのでループカウントを２０からに設定。
                        HashHistorys = new int[jsonArray.length() - 20];
                        for (int i = 20; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            HashHistorys[j] = jsonObject1.getInt("hashrate");
                            j++;
                        }
                    }

                    //グラフY軸表示用に最大値と最小値を取得しておく
                    MaxHash = 0;
                    MinHash = 0;
                    for (int i = 1; i < HashHistorys.length; i++) {
                        int v = HashHistorys[i];
                        if (v > MaxHash) {
                            MaxHash = v;
                        }
                        if (v < MinHash) {
                            MinHash = v;
                        }
                    }
                 setData();

                }

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

    //APIからJson
     public void GetJsonData() {
        // JSONの取得
        //MyData 用
        getLoaderManager().restartLoader(1, null, this);
        //PoolStats 用
        getLoaderManager().restartLoader(2, null, this);
         //グラフ 用
        getLoaderManager().restartLoader(3, null, this);
    }
    //グラフ追加
    private void setData() {


        ArrayList<Entry> values = new ArrayList<>();

        if (HashHistorys == null){return;}
        else{for (int i = 0; i < HashHistorys.length; i++) {
            values.add(new Entry(i, HashHistorys[i], null, null));
        }}



        LineDataSet set1;

        if (mChart.getData() != null &&
               mChart.getData().getDataSetCount() > 0) {

           set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
           set1.setValues(values);
           mChart.getData().notifyDataChanged();
           mChart.notifyDataSetChanged();
           mChart.invalidate();
       }

//        if (mChart.getData() != null &&
//                mChart.getData().getDataSetCount() > 0) {
//
//            set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
//            set1.setValues(values);
//            mChart.getData().notifyDataChanged();
//            mChart.notifyDataSetChanged();
//            // Y軸最大最小設定
//            //自分のデータの最大最小プラスマイナス1000としておく
//            YAxis leftAxis = mChart.getAxisLeft();
//            leftAxis.setAxisMaximum(MaxHash+1000);
//            leftAxis.setAxisMinimum(MinHash-1000);
//        } else {
//
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "");

            set1.setDrawIcons(false);
            set1.setColor(Color.BLACK);
            set1.setCircleColor(Color.BLACK);
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(0f);
            set1.setDrawFilled(true);
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);

            set1.setFillColor(Color.BLUE);

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1); // add the datasets

            // create a data object with the datasets
            LineData lineData = new LineData(dataSets);

            mChart.notifyDataSetChanged();
            mChart.invalidate();

            YAxis leftAxis = mChart.getAxisLeft();
            // Y軸最大最小設定
            //自分のデータの最大最小プラスマイナス1000としておく
            leftAxis.setAxisMaximum(MaxHash+1000);
            leftAxis.setAxisMinimum(MinHash-1000);
            // Grid横軸を破線
            leftAxis.enableGridDashedLine(10f, 10f, 0f);
            leftAxis.setDrawZeroLine(true);



            // set data
            mChart.setData(lineData);

//        }
    }
}
