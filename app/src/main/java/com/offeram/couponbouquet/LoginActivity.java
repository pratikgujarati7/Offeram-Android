package com.offeram.couponbouquet;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    LinearLayout tncLayout, registerLayout;
    ImageView imageIv, contactIv;
    TextInputLayout contactInput;
    EditText contactNumberEt;
    TextView loginBtn, backTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tncLayout = findViewById(R.id.tncLayout);
        registerLayout = findViewById(R.id.registerLayout);
        imageIv = findViewById(R.id.imageIv);
        contactIv = findViewById(R.id.contactIv);
        contactInput = findViewById(R.id.contactInput);
        contactNumberEt = findViewById(R.id.contactNumberEt);
        loginBtn = findViewById(R.id.loginBtn);
        backTv = findViewById(R.id.backTv);
    }
}
