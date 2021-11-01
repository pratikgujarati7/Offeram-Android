package com.offeram.couponbouquet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.appevents.AppEventsLogger;

public class PaymentSuccess extends AppCompatActivity {

    Toolbar toolbar;
    TextView messageTv;
    Button goToHomeBtn;
    String from = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_success);

        AppEventsLogger logger = AppEventsLogger.newLogger(PaymentSuccess.this);
        logger.logEvent("Coupon Purchased");

        Intent intent = getIntent();
        if(intent.hasExtra("from")){
            from = intent.getStringExtra("from");  // Set when coupon is redeemed or discount is 100%
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Payment Success");

        messageTv = (TextView) findViewById(R.id.messageTv);
        goToHomeBtn = (Button) findViewById(R.id.goToHomeBtn);

//        if(from.equalsIgnoreCase("Redeem")){
//            messageTv.setText("Thank You! Your order has been completed successfully");
//        }

        goToHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(PaymentSuccess.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}
