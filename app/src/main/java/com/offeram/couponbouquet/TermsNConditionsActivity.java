package com.offeram.couponbouquet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class TermsNConditionsActivity extends AppCompatActivity {

    Toolbar toolbar;
    WebView webView1;
    String title = "", url = "";
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_n_conditions);

//        This activity is for the terms and conditions which were added in the beginning
//        for taking acceptance from the customer  -> This point was added afterwards.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Terms & Conditions");

        webView1 = (WebView) findViewById(R.id.webView1);

        url = "http://offeram.com/Offeram_Beginning_Terms_and_Conditions.html";

        webView1.getSettings().setJavaScriptEnabled(true);
        webView1.setWebViewClient(new MyWebViewClient());
        webView1.loadUrl(url);
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            pd = new ProgressDialog(TermsNConditionsActivity.this);
            pd.setMessage("Loading...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            pd.dismiss();
        }
    }

}
