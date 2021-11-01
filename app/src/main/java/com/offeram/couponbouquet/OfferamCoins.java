package com.offeram.couponbouquet;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.offeram.couponbouquet.models.UsedOffer;
import com.offeram.couponbouquet.responses.Common;

public class OfferamCoins extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView titleTv;
    String howcanone = "<br/> By using offers at various affiliates as per applicable T&amp;Cs. Different offers may have different valuation of coins starting from <font color=\"#00025F\">10 coins.</font>";
    String howcantwo = "<br/> By sharing about Offeram via the Referral section to your friends / family.\n" +
            "<br />\n" +
            "        <br />\n" +
            "Earn <font color=\"#00025F\"> 100 Coins for successful signup </font>using your referral code &amp; <font color=\"#00025F\">1000 Coins on successful purchase of Offeram Mobile App </font> subscription for 1 year.";

    String howcanthree = "<br />By sharing your genuine review about your experience at any of the associated affiliates earn <font color=\"#00025F\"> 10 Coins. </font>";


    String howcanfour = "<br />" +
            "By PINGing an Offer Coupon with your friends / family earn <font color=\"#00025F\"> 10 Coins </font>on successful acceptance.";
    String wherecan = "Offeram coins can be used for:\n" +
            "\n" +
            "<br/><br/>1) Reactivation of used offers \n" +
            "\n" +
            "<br/><br/>2) Extension of Validity of their subscription (Coming Soon)\n" +
            "\n" +
            "<br/><br/>3) Buying Offeram Gift Cards (Coming Soon)\n" +
            "\n" +
            "<br/><br/>4) Converting to cash in their PayTM wallet (Coming Soon)";
    private TextView txt_coin, txt_offeramcoin_ans_one, txt_offeramcoin_ans_two, txt_offeramcoin_ans_three, txt_offeramcoin_ans_four, txt_whereofferamcoin_ans;
    private String coin;
    private TextView txt_pinged_now,txt_used_offer,txt_refer_now;
    public String strOfferemCoinsLabel1Text;
    public String strOfferemCoinsLabel2Text;
    public String strOfferemCoinsLabel3Text;
    public String strOfferemCoinsLabel4Text;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offeram_coins);
        Init();
        Intent intent = getIntent();
        if (intent != null) {
            coin = intent.getStringExtra("coin");
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Common common = new Gson().fromJson(Config.getSharedPreferences(OfferamCoins.this, "splashResponse"), Common.class);
        if (common != null) {
            if (common.getSuccess() == 1) {
//                strReferAndEarnText = common.getReferAndEarnText();
                strOfferemCoinsLabel1Text = common.getOfferamCoinsLabel1Text();
                strOfferemCoinsLabel2Text = common.getOfferamCoinsLabel2Text();
                strOfferemCoinsLabel3Text = common.getOfferamCoinsLabel3Text();
                strOfferemCoinsLabel4Text = common.getOfferamCoinsLabel4Text();
            }
        }
        titleTv = (TextView) findViewById(R.id.titleTv);
        titleTv.setText("Offeram Coins");
        txt_coin.setText(coin);

        txt_offeramcoin_ans_one.setText(strOfferemCoinsLabel1Text+"");
        txt_offeramcoin_ans_two.setText(strOfferemCoinsLabel2Text+"");
        txt_offeramcoin_ans_three.setText(strOfferemCoinsLabel3Text+"");
        txt_offeramcoin_ans_four.setText(strOfferemCoinsLabel4Text+"");

//        txt_offeramcoin_ans_one.setText(Html.fromHtml(howcanone), TextView.BufferType.SPANNABLE);
//        txt_offeramcoin_ans_two.setText(Html.fromHtml(howcantwo), TextView.BufferType.SPANNABLE);
//        txt_offeramcoin_ans_three.setText(Html.fromHtml(howcanthree), TextView.BufferType.SPANNABLE);
//        txt_offeramcoin_ans_four.setText(Html.fromHtml(howcanfour), TextView.BufferType.SPANNABLE);
        txt_whereofferamcoin_ans.setText(Html.fromHtml(wherecan));



        txt_refer_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OfferamCoins.this,RefferAndEarn.class);
                startActivity(intent);
            }
        });
        txt_used_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OfferamCoins.this,UsedOffersActivity.class);
                startActivity(intent);
            }
        });
        txt_pinged_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OfferamCoins.this,HowToPing.class);
                startActivity(intent);
            }
        });
    }

    private void Init() {
        txt_coin = (TextView) findViewById(R.id.txt_coin);
        txt_offeramcoin_ans_one = (TextView) findViewById(R.id.txt_offeramcoin_ans_one);
        txt_offeramcoin_ans_two = (TextView) findViewById(R.id.txt_offeramcoin_ans_two);
        txt_offeramcoin_ans_three = (TextView) findViewById(R.id.txt_offeramcoin_ans_three);
        txt_offeramcoin_ans_four = (TextView) findViewById(R.id.txt_offeramcoin_ans_four);
        txt_whereofferamcoin_ans=(TextView)findViewById(R.id.txt_whereofferamcoin_ans);
        txt_pinged_now=(TextView)findViewById(R.id.txt_pinged_now);
        txt_used_offer=(TextView)findViewById(R.id.txt_used_offer);
        txt_refer_now=(TextView)findViewById(R.id.txt_refer_now);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
