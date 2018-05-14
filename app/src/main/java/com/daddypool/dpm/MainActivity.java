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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            editText.setText(str);
        } else {
            textView.setText(R.string.read_error);
        }
        // エディットテキストのテキストを取得
        text = editText.getText().toString();

        // JSONの取得
        GetJsonData();

        // JSONの取得
//        //MyData 用
//        getLoaderManager().restartLoader(1, null, this);
//        //PoolStats 用
//        getLoaderManager().restartLoader(2, null, this);
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
        //>>グラフ追加
        mChart = findViewById(R.id.line_chart);

        // Grid背景色
        mChart.setDrawGridBackground(true);

        // no description text
        mChart.getDescription().setEnabled(true);

        // Grid縦軸を破線
        XAxis xAxis = mChart.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis leftAxis = mChart.getAxisLeft();
        // Y軸最大最小設定
        leftAxis.setAxisMaximum(150f);
        leftAxis.setAxisMinimum(0f);
        // Grid横軸を破線
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(true);

        // 右側の目盛り
        mChart.getAxisRight().setEnabled(false);

        // add data
        setData();

        mChart.animateX(2500);
        //mChart.invalidate();

        // dont forget to refresh the drawing
        // mChart.invalidate();
        //<<グラフ追加
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

//                    textDiffData.setText(jsonObject.getJSONObject("ZfYHAhLooYjJDUtKmzqA1ybkmVgz1Vimxe").getString("diff"));
//                    textHashData.setText(jsonObject.getJSONObject("ZfYHAhLooYjJDUtKmzqA1ybkmVgz1Vimxe").getString("hashrateString"));
//                    textBalData.setText(jsonObject.getJSONObject("ZfYHAhLooYjJDUtKmzqA1ybkmVgz1Vimxe").getString("balance"));
//                    textPaidData.setText(jsonObject.getJSONObject("ZfYHAhLooYjJDUtKmzqA1ybkmVgz1Vimxe").getString("paid"));
//                    textLuckDayData.setText(jsonObject.getJSONObject("ZfYHAhLooYjJDUtKmzqA1ybkmVgz1Vimxe").getString("luckDays"));
//                    textLuckHoursData.setText(jsonObject.getJSONObject("ZfYHAhLooYjJDUtKmzqA1ybkmVgz1Vimxe").getString("luckHours"));
//                    textSharesData.setText(jsonObject.getJSONObject("ZfYHAhLooYjJDUtKmzqA1ybkmVgz1Vimxe").getString("shares"));
                }
                //PoolStatsの表示
                if (loader.getId()==2){
                    JSONObject jsonObject = data.getJSONObject("pools");
                    textWorkersData.setText(jsonObject.getJSONObject("bitzeny").getString("workerCount"));
                    textMinerData.setText(jsonObject.getJSONObject("bitzeny").getString("minerCount"));
                    textHashu.setText(jsonObject.getJSONObject("bitzeny").getString("hashrateString"));
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
    }
    //グラフ追加
    private void setData() {
        // Entry()を使ってLineDataSetに設定できる形に変更してarrayを新しく作成
        int data[] = {116, 111, 112, 121, 102, 83,
                99, 101, 74, 105, 120, 112,
                109, 102, 107, 93, 82, 99, 110,
        };

        ArrayList<Entry> values = new ArrayList<>();

        for (int i = 0; i < data.length; i++) {
            values.add(new Entry(i, data[i], null, null));
        }

        LineDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {

            set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "DataSet");

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

            // set data
            mChart.setData(lineData);
        }
    }
}
