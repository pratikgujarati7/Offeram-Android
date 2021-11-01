package com.offeram.couponbouquet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class PaymentFailed extends AppCompatActivity {

    Toolbar toolbar;
    Button tryAgainBtn;
    String versionId, amount, promoCode, isApplied;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_failed);

        Intent intent = getIntent();
        if (intent != null) {
            versionId = intent.getStringExtra("versionId");
            amount = intent.getStringExtra("amount");
            promoCode = intent.getStringExtra("promotionalCode");
            isApplied = intent.getStringExtra("isApplied");
        }
        Log.e("In PaymentFailed", "VersionId n Amt n PromoCode Val : " + versionId + ", " + amount + " n " + promoCode);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Payment Failed");

        tryAgainBtn = (Button) findViewById(R.id.tryAgainBtn);

        tryAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(PaymentFailed.this, PayTmActivity.class);
                intent.putExtra("versionId", versionId);
                intent.putExtra("amount", amount);
                intent.putExtra("promotionalCode", promoCode);
                intent.putExtra("isApplied", isApplied);
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
