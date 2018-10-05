package com.daddypool.dpm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class privacy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.privacy);
        WebView myWebView = (WebView) findViewById(R.id.webView2);
// JavaScriptを有効化

        // Web Storage を有効化
        myWebView.getSettings().setDomStorageEnabled(true);

        myWebView.setWebViewClient(new WebViewClient());

        myWebView.loadUrl("https://github.com/DaddyPool/DPM/wiki/PrivacyPolicy");
//        myWebView.loadUrl("https://www.google.com/");
    }
}
