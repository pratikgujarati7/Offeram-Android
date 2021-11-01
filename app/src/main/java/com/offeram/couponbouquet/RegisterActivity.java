package com.offeram.couponbouquet;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    LinearLayout tncLayout, loginLayout;
    ImageView imageIv, nameIv, contactIv;
    TextInputLayout nameInput, contactInput;
    EditText nameEt, contactNumberEt;
    TextView registerBtn, backTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        tncLayout = findViewById(R.id.tncLayout);
        loginLayout = findViewById(R.id.loginLayout);
        imageIv = findViewById(R.id.imageIv);
        nameIv = findViewById(R.id.nameIv);
        contactIv = findViewById(R.id.contactIv);
        nameInput = findViewById(R.id.nameInput);
        contactInput = findViewById(R.id.contactInput);
        nameEt = findViewById(R.id.nameEt);
        contactNumberEt = findViewById(R.id.contactNumberEt);
        registerBtn = findViewById(R.id.registerBtn);
        backTv = findViewById(R.id.backTv);

    }
}
