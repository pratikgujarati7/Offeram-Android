package com.offeram.couponbouquet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class ErrorDialogForPromoActivity extends AppCompatActivity {

    TextView titleTv, messageTv;
    Button okBtn;
    String message, type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_error_dialog_for_promo);

        setFinishOnTouchOutside(false);

        final Intent intent = getIntent();
        if (intent != null) {
            message = intent.getStringExtra("message");
            if (intent.hasExtra("type")) {
                type = intent.getStringExtra("type");
            }
        }

        titleTv = (TextView) findViewById(R.id.titleTv);
        messageTv = (TextView) findViewById(R.id.messageTv);
        okBtn = (Button) findViewById(R.id.okBtn);

        titleTv.setVisibility(View.GONE);
        messageTv.setText(message);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                finish();
                if (type.equals("")) {
                    finish();
                } else {
                    Intent intent1 = new Intent(ErrorDialogForPromoActivity.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent1);
                }
            }
        });

    }
}
