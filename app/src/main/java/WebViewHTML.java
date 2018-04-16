import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.daddypool.dpm.R;

public class WebViewHTML extends Activity implements View.OnClickListener {
    private Handler handler = new Handler();

    @SuppressLint("JavascriptInterface")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        WebView web = (WebView) findViewById(R.id.webView1);
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setBuiltInZoomControls(true);
        web.setWebViewClient(new ViewSourceClient());
        web.addJavascriptInterface(this, "activity");

        Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        EditText text = (EditText) findViewById(R.id.editText1);
        String url = text.getText().toString();
        WebView web = (WebView) findViewById(R.id.webView1);
        web.loadUrl(url);
    }

    public void viewSource(final String src) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                TextView text = (TextView) findViewById(R.id.textView1);
                text.setText(src);
            }
        });
    }

    private static class ViewSourceClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            view.loadUrl("javascript:window.activity.viewSource(document.documentElement.outerHTML);");
        }
    }
}