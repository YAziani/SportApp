package com.example.mb7.sportappbp.Activity;

import android.app.ProgressDialog;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mb7.sportappbp.R;

import org.w3c.dom.Text;

public class ActivityKompass extends AppCompatActivity {
    private WebView webViewkompass;
    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actitivity_kompass);
        InitializeControlls();
        SetControlValues();
        super.onStart();
    }

    private void InitializeControlls() {
        webViewkompass = (WebView) findViewById(R.id.webview_kompass);
    }

    private void SetControlValues() {
        webViewkompass.getSettings().setJavaScriptEnabled(true);
        webViewkompass.loadUrl("https://kompass-8720f.firebaseapp.com/");
        webViewkompass.clearView();
        webViewkompass.measure(100, 100);
        webViewkompass.getSettings().setUseWideViewPort(true);
        webViewkompass.getSettings().setLoadWithOverviewMode(true);
        progressBar = ProgressDialog.show(this, getString(R.string.kompass), getString(R.string.wird_geladen));

        webViewkompass.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }
            }


        });
    }

}
