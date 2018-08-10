package com.daddypool.dpm;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class faucet extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.faucet);
        WebView myWebView = (WebView) findViewById(R.id.webView);
// JavaScriptを有効化
        myWebView.getSettings().setJavaScriptEnabled(true);

        // Web Storage を有効化
        myWebView.getSettings().setDomStorageEnabled(true);

        myWebView.setWebViewClient(new WebViewClient());

        myWebView.loadUrl("http://daddy.starfree.jp/");
//        myWebView.loadUrl("https://www.google.com/");
    }
}
