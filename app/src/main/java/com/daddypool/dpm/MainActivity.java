package com.daddypool.dpm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WebView  myWebView = (WebView)findViewById(R.id.webView);
        //標準ブラウザをキャンセル
        myWebView.setWebViewClient(new WebViewClient());
        //アプリ起動時に読み込むURL
        myWebView.loadUrl("http://daddy-pool.work/");
    }
}
