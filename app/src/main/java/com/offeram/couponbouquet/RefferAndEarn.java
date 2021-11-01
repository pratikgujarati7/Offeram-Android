package com.offeram.couponbouquet;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.offeram.couponbouquet.responses.Common;

public class RefferAndEarn extends AppCompatActivity {
    private TextView txt_amount, txt_link;
    private ImageView img_qr;
    private ImageView img_socialmedia;
    private Toolbar toolbar;
    private TextView titleTv;
    private TextView txt_learnmore;
    private TextView txt_code, share_invitiation;
    private LinearLayout ll_code;
    String codeone = "";
    private TextView txt_content;

    public String strReferAndEarnText;
    public String strOfferemCoinsLabel1Text;
    public String strOfferemCoinsLabel2Text;
    public String strOfferemCoinsLabel3Text;
    public String strOfferemCoinsLabel4Text;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referandearn);
        Init();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txt_content = findViewById(R.id.txt_content);

        Common common = new Gson().fromJson(Config.getSharedPreferences(RefferAndEarn.this, "splashResponse"), Common.class);
        if (common != null) {
            if (common.getSuccess() == 1) {
                strReferAndEarnText = common.getReferAndEarnText();
                strOfferemCoinsLabel1Text = common.getOfferamCoinsLabel1Text();
                strOfferemCoinsLabel2Text = common.getOfferamCoinsLabel2Text();
                strOfferemCoinsLabel3Text = common.getOfferamCoinsLabel3Text();
                strOfferemCoinsLabel4Text = common.getOfferamCoinsLabel4Text();
            }
        }
        txt_content.setText(strReferAndEarnText+"");
        titleTv = (TextView) findViewById(R.id.titleTv);
        titleTv.setText("Refer & Earn");
        Intent intent = getIntent();
        final String code = intent.getStringExtra("refercode");

        txt_code.setText(code);
        ll_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Referral code is copied.", code);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(), "Referral code is copied.", Toast.LENGTH_SHORT).show();
            }
        });
        share_invitiation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "Offeram");
//                    String sAux = "Check out Reward Cards :\nA Card Wallet app for your smart phone to store all your Reward Cards, " +
//                            "Loyalty Cards, Store Cards, Membership Cards and many other cards. Download it now from \n" +
//                            "https://tinyurl.com/ydanl28q";
                    String sAux = "Woohoo!!! I am using Offeram and gonna save over ₹1,00,000+ Use my referral code - " + code + "  to sign up and activate your subscription to Dil Kholke Discounts. Download now: Offeram.com/app";
                    //sAux = sAux + "https://play.google.com/store/apps/details?id=com.innovativeiteration.rewardsapp \n\n";
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "choose one"));

                } catch (Exception e) {
                    //e.toString();
                }
            }
        });
        //txt_amount.setText(Config.getSharedPreferences(RefferAndEarn.this,"offeramCoin"));

        //txt_link.setText(Config.getSharedPreferences(RefferAndEarn.this,"referUrl"));
//        txt_learnmore.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(RefferAndEarn.this,OfferamCoins.class);
//                startActivity(intent);
//            }
//        });
//        img_socialmedia.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String sendmessage=txt_link.getText().toString();
//                try {
//                    Intent i = new Intent(Intent.ACTION_SEND);
//                    i.setType("text/plain");
//                    i.putExtra(Intent.EXTRA_SUBJECT, "Offeram");
////                    String sAux = "Check out Reward Cards :\nA Card Wallet app for your smart phone to store all your Reward Cards, " +
////                            "Loyalty Cards, Store Cards, Membership Cards and many other cards. Download it now from \n" +
////                            "https://tinyurl.com/ydanl28q";
//                    String sAux = sendmessage;
//                    //sAux = sAux + "https://play.google.com/store/apps/details?id=com.innovativeiteration.rewardsapp \n\n";
//                    i.putExtra(Intent.EXTRA_TEXT, sAux);
//                    startActivity(Intent.createChooser(i, "choose one"));
//
//                } catch (Exception e) {
//                    //e.toString();
//                }
//            }
//        });

    }

    private void Init() {
//        txt_amount=(TextView)findViewById(R.id.txt_amount);
//        txt_link=(TextView)findViewById(R.id.txt_link);
        txt_learnmore = (TextView) findViewById(R.id.txt_learnmore);
        txt_code = (TextView) findViewById(R.id.txt_code);
        share_invitiation = (TextView) findViewById(R.id.share_invitiation);
        ll_code = (LinearLayout) findViewById(R.id.ll_code);
//        img_qr=(ImageView)findViewById(R.id.img_qr);
//        img_socialmedia=(ImageView)findViewById(R.id.img_socialmedia);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
