package com.offeram.couponbouquet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.offeram.couponbouquet.responses.Common;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationDialogActivity extends AppCompatActivity {

    TextView titleTv, messageTv;
    Button okBtn;
    String id = "", userId = "", title = "", message = "";
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = (int) (metrics.widthPixels * 0.80);
        setContentView(R.layout.activity_notification_dialog);
        getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);

        setFinishOnTouchOutside(false);

        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getStringExtra("id");
            title = intent.getStringExtra("title");
            message = intent.getStringExtra("message");
        }
        Log.e("In NotificationAct", "Id -> " + id + ", title -> " + title + " n message -> " + message);

        titleTv = findViewById(R.id.titleTv);
        messageTv = findViewById(R.id.messageTv);
        okBtn = findViewById(R.id.okBtn);

        titleTv.setText(title);
        messageTv.setText(message);
        userId = Config.getSharedPreferences(NotificationDialogActivity.this, "userId");

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                if (Config.isConnectedToInternet(NotificationDialogActivity.this)) {
//                    pd = new ProgressDialog(NotificationDialogActivity.this);
//                    pd.setCancelable(false);
//                    pd.setMessage("Loading...");
//                    pd.show();
                    RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
                    Call<Common> call = apiInterface.markAsRead(userId, id);
                    call.enqueue(new Callback<Common>() {
                        @Override
                        public void onResponse(Call<Common> call, Response<Common> response) {
//                            pd.dismiss();
                            Common c = response.body();
                            if (c != null) {
                                if (c.getSuccess() == 1) {
                                    Log.e("In NotificationAct", "Success : " + c.getSuccess());
                                }
                            } else {
                                Config.showDialog(NotificationDialogActivity.this, Config.FailureMsg);
                            }

                        }

                        @Override
                        public void onFailure(Call<Common> call, Throwable t) {
//                            pd.dismiss();
                            Log.e("In NotificationAct", "OnFailure Msg : " + t.getMessage());
//                            Config.showDialog(NotificationDialogActivity.this, Config.FailureMsg);
                        }
                    });
                } else {
                    Config.showAlertForInternet(NotificationDialogActivity.this);
                }
            }
        });

    }
}
