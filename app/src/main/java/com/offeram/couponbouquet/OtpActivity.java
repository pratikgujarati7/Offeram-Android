package com.offeram.couponbouquet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.offeram.couponbouquet.responses.Otp;
import com.offeram.couponbouquet.responses.Verify;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import me.philio.pinentry.PinEntryView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpActivity extends AppCompatActivity {

    //    EditText otpEt;
    TextView timerForOtpTv, proceedBtn, backTv, contactTv;
    LinearLayout resendOtpLayout;
    View resendView;
    PinEntryView otpPinView;
    String contactNumber = "", cityId = "", deviceId = "", deviceType = "0";
    Boolean isValid;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
//        SmsRetrieverClient client = SmsRetriever.getClient(this);
//        Task<Void> task = client.startSmsRetriever();
//        task.addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                // Successfully started retriever, expect broadcast intent
//                // ...
//            }
//        });
//
//        task.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                // Failed to start retriever, inspect Exception for more details
//                // ...
//            }
//        });

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("otp")) {
            contactNumber = intent.getStringExtra("contactNumber");
            cityId = intent.getStringExtra("cityId");
        }
        Log.e("In OtpActivity", " Contact : " + contactNumber + " CityId : " + cityId);

        if (Config.getSharedPreferences(OtpActivity.this, "token") == null) {
            deviceId = FirebaseInstanceId.getInstance().getToken();
        } else {
            deviceId = Config.getSharedPreferences(OtpActivity.this, "token");
        }

//        otpEt = (EditText) findViewById(R.id.otpEt);
        timerForOtpTv = (TextView) findViewById(R.id.timerForOtpTv);
        contactTv = (TextView) findViewById(R.id.contactTv);
//        resendOtpLayout = (LinearLayout) findViewById(R.id.resendOtpLayout);
//        resendOtpTv = (TextView) findViewById(R.id.resendOtpTv);
//        resendView = findViewById(R.id.resendView);
        otpPinView = (PinEntryView) findViewById(R.id.otpPinView);
        proceedBtn = (TextView) findViewById(R.id.proceedBtn);
        backTv = (TextView) findViewById(R.id.backTv);
//
        contactTv.setText(contactNumber);
//        if (!Config.getSharedPreferences(OtpActivity.this,"Otpmessage").equals("")||Config.getSharedPreferences(OtpActivity.this,"Otpmessage")!=null){
//            otpPinView.setText(Config.getSharedPreferences(OtpActivity.this,"Otpmessage"));
//        }
        //mySMSBroadcastReceiver.onReceive(this,intent);



//        SmsReceiver.bindListener(new SmsListener() {
//            @Override
//            public void messageReceived(String messageText) {
//                Log.e("In OtpActivity", "Message Val : " + messageText);
////                Toast.makeText(OtpActivity.this,"Message: "+messageText,Toast.LENGTH_LONG).show();
//                otpPinView.setText(messageText);
//            }
//        });
//

        countDownForResendingOtp();

        backTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
//
        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isValid = true;
                String otp = otpPinView.getText().toString();
                String message = "";
                if (otp.equals("")) {
                    isValid = false;
//                    Config.showSnackBar(OtpActivity.this, "Must enter OTP");
                    message = "Must enter OTP";
                }

                if (isValid) {
                    if (Config.isConnectedToInternet(OtpActivity.this)) {
                        pd = new ProgressDialog(OtpActivity.this);
                        pd.setCancelable(false);
                        pd.setMessage("Loading...");
                        pd.show();
                        RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
                        Log.e("In OtpActivity", "Verify Otp Params UserId : " + Config.getSharedPreferences(OtpActivity.this, "userId"));
                        Call<Verify> verifyCall = apiInterface.verifyOtp(Config.getSharedPreferences(OtpActivity.this, "userId"),otp);
                        verifyCall.enqueue(new Callback<Verify>() {
                            @Override
                            public void onResponse(Call<Verify> call, Response<Verify> response) {
                                pd.dismiss();
                                Log.e("In OtpActivity", "Response : " + new Gson().toJson(response.body()));
                                Verify v = response.body();
                                if (v.getSuccess() == 1) {
                                    final DialogPlus dialog = DialogPlus.newDialog(OtpActivity.this)
                                            .setContentHolder(new ViewHolder(R.layout.dialog_ok_layout))
                                            .setGravity(Gravity.CENTER)
                                            .create();
                                    dialog.show();

                                    View dialogView = dialog.getHolderView();
                                    TextView titleTv = (TextView) dialogView.findViewById(R.id.titleTv);
                                    TextView messageTv = (TextView) dialogView.findViewById(R.id.messageTv);
                                    Button okBtn = (Button) dialogView.findViewById(R.id.okBtn);
                                    titleTv.setVisibility(View.GONE);
                                    messageTv.setText("You have successfully verified your mobile number");

                                    okBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();

                                            AppEventsLogger logger = AppEventsLogger.newLogger(OtpActivity.this);
                                            logger.logEvent("Complete Registration");

                                            Config.saveSharedPreferences(OtpActivity.this, "isVerified", "1");

                                            if (Config.getSharedPreferences(OtpActivity.this,"isFreshUser")!=null){
                                                if (Config.getSharedPreferences(OtpActivity.this,"isFreshUser").equals("1")){
                                                    Intent intent = new Intent(OtpActivity.this, Referral.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);
                                                }else {
                                                    Intent intent = new Intent(OtpActivity.this, HomeActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);
                                                }
                                            }
                                        }
                                    });

                                } else {
                                    otpPinView.setText("");
                                    // Toast.makeText(OtpActivity.this, "Some Error Occurred. Please Try Again.", Toast.LENGTH_LONG).show();
                                    Config.showDialog(OtpActivity.this, v.getMessage());
                                }
                            }

                            @Override
                            public void onFailure(Call<Verify> call, Throwable t) {
                                pd.dismiss();
                                // Toast.makeText(OtpActivity.this, "Some Error Occurred. Please Try Again.", Toast.LENGTH_LONG).show();
                                Log.e("In OtpActivity", "In Verify Excp : " + t.getMessage());
                                Config.showDialog(OtpActivity.this, Config.FailureMsg);
                            }
                        });

                    } else {
                        Config.showAlertForInternet(OtpActivity.this);
                    }
                } else {
                    final DialogPlus errorDialog = DialogPlus.newDialog(OtpActivity.this)
                            .setContentHolder(new ViewHolder(R.layout.dialog_ok_layout))
                            .setGravity(Gravity.CENTER)
                            .create();
                    errorDialog.show();

                    View errorView = errorDialog.getHolderView();
                    TextView titleTv = (TextView) errorView.findViewById(R.id.titleTv);
                    TextView messageTv = (TextView) errorView.findViewById(R.id.messageTv);
                    Button okBtn = (Button) errorView.findViewById(R.id.okBtn);
                    titleTv.setVisibility(View.GONE);
                    messageTv.setText(message);

                    okBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            errorDialog.dismiss();
                        }
                    });

                }

            }
        });
//
        timerForOtpTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("In OtpActivity", "In OnClick");
//                if(resendView.getB)
                countDownForResendingOtp();

                if (Config.isConnectedToInternet(OtpActivity.this)) {
                    pd = new ProgressDialog(OtpActivity.this);
                    pd.setCancelable(false);
                    pd.setMessage("Loading...");
                    pd.show();
                    RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
                    final Call<Otp> otp = apiInterface.getOtp(contactNumber, deviceId, deviceType, "1", cityId);
                    otp.enqueue(new Callback<Otp>() {
                        @Override
                        public void onResponse(Call<Otp> call, Response<Otp> response) {
                            pd.dismiss();
                            Otp o = response.body();
                            if (o.getSuccess() == 1) {
                                Config.saveSharedPreferences(OtpActivity.this, "userId", o.getUserId());
                                Config.saveSharedPreferences(OtpActivity.this, "versionId", o.getVersionId());
                                Config.saveSharedPreferences(OtpActivity.this, "paymentId", o.getPaymentId());
                                Config.saveSharedPreferences(OtpActivity.this, "versionName", o.getVersionName());
                                final DialogPlus errorDialog = DialogPlus.newDialog(OtpActivity.this)
                                        .setContentHolder(new ViewHolder(R.layout.dialog_ok_layout))
                                        .setGravity(Gravity.CENTER)
                                        .create();
                                errorDialog.show();

                                View errorView = errorDialog.getHolderView();
                                TextView titleTv = (TextView) errorView.findViewById(R.id.titleTv);
                                TextView messageTv = (TextView) errorView.findViewById(R.id.messageTv);
                                Button okBtn = (Button) errorView.findViewById(R.id.okBtn);
                                titleTv.setVisibility(View.GONE);
                                messageTv.setText("OTP has been successfully sent to your phone number");

                                okBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        errorDialog.dismiss();
                                    }
                                });

                            } else {
                                // Toast.makeText(OtpActivity.this, "Some Error Occurred. Please Try Again.", Toast.LENGTH_LONG).show();
                                Config.showDialog(OtpActivity.this, Config.FailureMsg);

                            }
                        }

                        @Override
                        public void onFailure(Call<Otp> call, Throwable t) {
                            pd.dismiss();
                            // Toast.makeText(OtpActivity.this, "Some Error Occurred. Please Try Again.", Toast.LENGTH_LONG).show();
                            Log.e("In OtpActivity", "In ResendOtp Excp : " + t.getMessage());
                            Config.showDialog(OtpActivity.this, Config.FailureMsg);
                        }
                    });
                } else {
                    Config.showAlertForInternet(OtpActivity.this);
                }

            }
        });
//
    }

    //
    public void countDownForResendingOtp() {
        new CountDownTimer(60 * 1000, 1000) {

            @Override
            public void onTick(long millis) {
//                resendOtpLayout.setEnabled(false);
//                resendOtpTv.setTextColor(Color.parseColor("#545454"));
//                resendView.setBackgroundColor(Color.parseColor("#545454"));
                timerForOtpTv.setEnabled(false);
                timerForOtpTv.setTextColor(getResources().getColor(R.color.lightGrey));
                int seconds = (int) (millis / 1000) % 60;
                int minutes = (int) ((millis / (1000 * 60)) % 60);
                int hours = (int) ((millis / (1000 * 60 * 60)) % 24);
                String text = String.format("%02d seconds", seconds);
//                Log.e("In OtpActivity", "Timer Val : " + text);
                timerForOtpTv.setText("resend OTP in 00:" + text);
            }

            @Override
            public void onFinish() {
//                resendOtpLayout.setEnabled(true);
//                resendOtpTv.setTextColor(Color.parseColor("#FDA607"));
//                resendView.setBackgroundColor(Color.parseColor("#FDA607"));
                timerForOtpTv.setEnabled(true);
                timerForOtpTv.setText("You can now request a new OTP");
            }
        }.start();
    }
}
