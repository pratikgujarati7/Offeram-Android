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

public class HowToPing extends AppCompatActivity {
    private TextView txt_content;
    private TextView txt_pinging_ans,startpingnow;
    String content="Your friend or family will recieve a notification and if they accept the offer then they will be able to use your PINGed offer if they are on the paid or active subscription of Offeram Mobile App. \n" +
            "\n" +
            "<br/><br/><br/>Similarly you may recieve PINGs from your friends or family.\n" +
            "\n" +
            "<br/><br/><br/>Of course a push notification will come and you can also find the same in the Activities tab whenever someone is PINGed.\n" +
            "\n" +
            "<br/><br/><br/>Once you have PINGed an offer you will not be able to use it from your account.";
    String pingedAns="<>br/>1.To share the happiness of saving with your friends & family \n" +
            "<br/><br/>2.At times it may happen that some offers are more relevant or usable to your friend or family, so it's better to give them the advantage.\n" +
            "<br/><br/>3.Plus to earn Offeram Coins";
    private Toolbar toolbar;
    private TextView titleTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_howtoping);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        titleTv = (TextView) findViewById(R.id.titleTv);
        titleTv.setText("How to Ping?");
        Init();
        txt_content.setText(Html.fromHtml(content), TextView.BufferType.SPANNABLE);
        txt_pinging_ans.setText(Html.fromHtml(pingedAns), TextView.BufferType.SPANNABLE);
        startpingnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HowToPing.this,HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    private void Init() {
        txt_content=(TextView)findViewById(R.id.txt_content);
        txt_pinging_ans=(TextView)findViewById(R.id.txt_pinging_ans);
        startpingnow=(TextView)findViewById(R.id.startpingnow);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
