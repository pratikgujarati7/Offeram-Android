package com.offeram.couponbouquet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import static com.offeram.couponbouquet.RedeemCouponActivity.REQUEST_CODE;

public class WebViewActivity extends AppCompatActivity {

    private static final String TAG = WebViewActivity.class.getSimpleName();
    private static final int PERMISSION_REQUEST_CODE = 1;
    Toolbar toolbar;
    WebView webView1;
    String title = "", url = "";
    StringBuilder html = null;
    ProgressDialog pd;
    String pdfUrl="";
    String message = "Woohoo!!! You have just registered yourself for WooHoo Tambola.\n" +
            "\n" +
            "You will recieve your ticket here on WhatsApp as well along with this message.\n" +
            "\n" +
            "-x-\n" +
            "How to Play Mega Tambola?\n" +
            "\n" +
            "Register by just signing up on Offeram.com/app\n" +
            "\n" +
            "Download your unique Mega Tambola ticket and print it.\n" +
            "\n" +
            "Keep an eye on:\n" +
            "1) WooHoo Screens\n" +
            "2) Social media handles of FB.com/WooHooSurat & FB.com/Offeram.com\n" +
            "3) Your WhatsApp and SMS inbox\n" +
            "4) Offeram Mobile App Tambola & Notifications space\n" +
            "\n" +
            "We will be daily releasing 3 numbers. That will be shown on all above media. You can follow any of them to keep yourself up in the game. Refer our Facebook page for current status of all numbers released till now: FB.com/WooHooSurat\n" +
            "\n" +
            "Keep cutting the numbers from your ticket and if you reach any Winning Point as shown in the Winning Point Chart then just WhatsApp us on 7055540333 (Offeram.com/WhatsApp)\n" +
            "\n" +
            "Those who will be the first to send the Winning Point on our WhatsApp number will be considered winner for that Winning Point.\n" +
            "\n" +
            "Winning Points are:\n" +
            "1) Temperature\n" +
            "2) Early 5\n" +
            "3) Corners\n" +
            "4) Row 1\n" +
            "5) Row 2\n" +
            "6) Row 3\n" +
            "7) Full House\n" +
            "\n" +
            "Also you can participate in the game till the last 3 nos are not out. So even if you start late still you can win the game.\n" +
            "-x-\n";
    private String sharemessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        Intent intent = getIntent();
        if (intent != null) {
            title = intent.getStringExtra("title");
            if (intent.hasExtra("title")) {
                title = intent.getStringExtra("title");
            }
            if (intent.hasExtra("sharemessage")) {
                sharemessage = intent.getStringExtra("sharemessage");
            }
//            else {
//                title = "Terms & Conditions";
//            }
            url = intent.getStringExtra("url");
            pdfUrl=url;
            if (url.contains(".pdf")) {
                String pdf = "http://docs.google.com/gview?embedded=true&url=";
                url = pdf + url;
            }
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        webView1 = (WebView) findViewById(R.id.webView1);
        webView1.getSettings().setJavaScriptEnabled(true);
        webView1.getSettings().setAllowContentAccess(true);
        webView1.setWebViewClient(new MyWebViewClient());
        webView1.loadUrl(url);

    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            pd = new ProgressDialog(WebViewActivity.this);
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
            if (pd != null)
                pd.dismiss();
        }
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        onBackPressed();
//        return true;
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (title.equals("Tambola Ticket")) {
            getMenuInflater().inflate(R.menu.menu_redeem_coupon_activity, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_share) {
            //String message = ""+url;
            if (!sharemessage.equals("") && sharemessage != null) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, sharemessage);
                startActivity(Intent.createChooser(sharingIntent, "Share using..."));
            }

        } else if (itemId == R.id.action_download) {

            if (Build.VERSION.SDK_INT >= 23) {
                if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Log.v(TAG,"Permission is granted");
                    new DownloadFile().execute(pdfUrl, "TambolaTicket.pdf");
                    return true;
                }else {
                    requestPermission();
                }
            }


        } else {
            onBackPressed();
        }
        return true;
    }
    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(WebViewActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(WebViewActivity.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(WebViewActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                    new DownloadFile().execute(pdfUrl, "TambolaTicket.pdf");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }


    private class DownloadFile extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(WebViewActivity.this);
            pd.setMessage("Downloading...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
            String fileName = strings[1];  // -> maven.pdf
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "Offeram");
            folder.mkdir();

            File pdfFile = new File(folder, fileName);

            try{
                pdfFile.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
            }
            FileDownloader.downloadFile(fileUrl, pdfFile);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pd.dismiss();
        }
    }

}
