package com.offeram.couponbouquet;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;

public class AboutUsActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView titleTv, companyNameTv, addressTv, phoneNumberTv, emailTv;
    WebView webView1;
    String title = "", url = "";
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        titleTv = (TextView) findViewById(R.id.titleTv);
        titleTv.setText("About Us");

        companyNameTv = (TextView) findViewById(R.id.companyNameTv);
        addressTv = (TextView) findViewById(R.id.addressTv);
        phoneNumberTv = (TextView) findViewById(R.id.phoneNumberTv);
        emailTv = (TextView) findViewById(R.id.emailTv);
        webView1 = (WebView) findViewById(R.id.webView1);

        webView1.setBackgroundColor(Color.TRANSPARENT);
        webView1.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
        String htmlText = " %s ";
        webView1.loadData(String.format(htmlText, "<html>\n" +
                " <head></head>\n" +
                " <body style=\"text-align:justify;color:#545454;font-size:14;\">\n" +
                "  Offeram.com - Dil Kholke Discounts is a concept by MagnADism wherein we provide discount offers of " +
                "  various brands covering various segments like Food &amp; Beverages, Health &amp; Beauty, Shopping & " +
                "  Fashion, Automobile Services, Broadband Internet Services and many more to our customers via mobile app, " +
                "  discount coupon booklet and gift cards" +
                " </body>\n" +
                " </html>\n"), "text/html", "utf-8");

        companyNameTv.setText("MagnADism");
//        addressTv.setText("U/20, Amizara Complex,\n" +
//                "Above Klassic Restaurant,\n" +
//                "Opp. Jilla Seva Sadan - 2,\n" +
//                "Athwalines, Surat - 395001");
        addressTv.setText("2, Shrenik Park Society,\n" +
                "Opp. Prime Arcade,\n" +
                "Nr. Shree Rameshvar Mahadev Temple,\n" +
                "Chandrashekhar Azad Marg,\n" +
                "Adajan, Surat - 395009");
        emailTv.setText("contact@offeram.com");
        phoneNumberTv.setText("7055540333");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
