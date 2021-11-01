package com.offeram.couponbouquet;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.offeram.couponbouquet.responses.Common;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RedeemCouponActivity extends AppCompatActivity {

    static int REQUEST_CODE = 124;
    //    private IntentIntegrator qrScan;
    String couponId, pin = "", companyName, couponDetails, merchantId, searchText;
    Toolbar toolbar;
    //    EditText pinEt;
//    Button proceedBtn;
    //    private DecoratedBarcodeView barcodeView;
    private BeepManager beepManager;
    ProgressDialog pd;
    Boolean isValid;
    int isQrCode = 0, isResult = 0;

    // New Design Components
    TextInputLayout pinInput;
    EditText pinEt;
    DecoratedBarcodeView barcodeView;
    TextView titleTv, proceedBtn;

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            isQrCode = 1;
//            isResult = 0;
//            barcodeView.setStatusText(result.getText());
//            beepManager.playBeepSoundAndVibrate();
            if (result != null) {
                //if qrcode has nothing in it
                if (result.getText() == null) {
                    Toast.makeText(RedeemCouponActivity.this, "Result Not Found", Toast.LENGTH_LONG).show();
                } else {
//                try {
                    Log.e("In RedeemCouponAct", "Result : " + result.getText());
                    pin = result.getText();
                    if (!pin.equals("") && !pin.equals(null)) {
                        isResult++;
                    }

                    if (isResult == 1) {
                        Log.e("In RedeemCouponAct", "isResult Val : " + isResult);
                        callRedeemApi(pin);
                    }

                }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(RedeemCouponActivity.this, result.getText(), Toast.LENGTH_LONG).show();
//                }

            }
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };
    private String ping_id;
    private String is_ping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem_coupon_test);

        final Intent intent = getIntent();
        if (intent != null) {
            couponDetails = intent.getStringExtra("couponDetails");
            companyName = intent.getStringExtra("companyName");
            couponId = intent.getStringExtra("couponId");
            ping_id = intent.getStringExtra("ping_id");
            is_ping = intent.getStringExtra("is_ping");
//            merchantId = intent.getStringExtra("merchantId");
//            if (intent.hasExtra("searchText")) {
//                searchText = intent.getStringExtra("searchText");
//            }
        }
        Log.e("In RedeemCouponAct", "Coupon Id, MerchantId, Search Text : " + couponId + ", " + merchantId + ", " + searchText);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        titleTv = toolbar.findViewById(R.id.titleTv);
        titleTv.setText("Redeem This Offer");

//        pinEt = (EditText) findViewById(R.id.pinEt);
//        proceedBtn = (Button) findViewById(R.id.proceedBtn);
//        barcodeView = (DecoratedBarcodeView) findViewById(R.id.barcode_scanner);
//
//        barcodeView.decodeSingle(callback);
//        barcodeView.setStatusText("");
//        beepManager = new BeepManager(this);
//        //        qrScan = new IntentIntegrator(this);
////        qrScan.initiateScan();
////        qrScan.setOrientationLocked(false);
////        qrScan.setBeepEnabled(false);
//
//        proceedBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                isValid = true;
//                if (isQrCode != 1) {
//                    pin = pinEt.getText().toString();
//                }
//                String message = "";
//                if (pin.equals("")) {
//                    isValid = false;
//                    message = "Must enter pin or scan QR code";
////                    Config.showSnackBar(RedeemCouponActivity.this, "Must enter PIN");
//                }
//
//                if (isValid) {
//                    callRedeemApi(pin);
//                } else {
//                    final DialogPlus errorDialog = DialogPlus.newDialog(RedeemCouponActivity.this)
//                            .setContentHolder(new ViewHolder(R.layout.dialog_ok_layout))
//                            .setGravity(Gravity.CENTER)
//                            .create();
//                    errorDialog.show();
//
//                    View errorView = errorDialog.getHolderView();
//                    TextView titleTv = (TextView) errorView.findViewById(R.id.titleTv);
//                    TextView messageTv = (TextView) errorView.findViewById(R.id.messageTv);
//                    Button okBtn = (Button) errorView.findViewById(R.id.okBtn);
//                    titleTv.setVisibility(View.GONE);
//                    messageTv.setText(message);
//
//                    okBtn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            errorDialog.dismiss();
//                        }
//                    });
//
//                }
//            }
//        });

        pinInput = findViewById(R.id.pinInput);
        pinEt = findViewById(R.id.pinEt);
        barcodeView = findViewById(R.id.barcodeView);
        proceedBtn = findViewById(R.id.proceedBtn);

        if (ActivityCompat.checkSelfPermission(RedeemCouponActivity.this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(RedeemCouponActivity.this,
                    new String[]{android.Manifest.permission.CAMERA}, REQUEST_CODE);
        } else {
            barcodeView.decodeSingle(callback);
            barcodeView.setStatusText("");
            beepManager = new BeepManager(this);
        }

        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isValid = true;
                pinInput.setError(null);
                if (isQrCode != 1) {
                    pin = pinEt.getText().toString();
                }
                String message = "";
                if (pin.equals("")) {
                    isValid = false;
                    message = "Must enter pin or scan QR code";
//                    Config.showSnackBar(RedeemCouponActivity.this, "Must enter PIN");
                }

                if (isValid) {
                    callRedeemApi(pin);
                } else {
//                    final DialogPlus errorDialog = DialogPlus.newDialog(RedeemCouponActivity.this)
//                            .setContentHolder(new ViewHolder(R.layout.dialog_ok_layout))
//                            .setGravity(Gravity.CENTER)
//                            .create();
//                    errorDialog.show();
//
//                    View errorView = errorDialog.getHolderView();
//                    TextView titleTv = (TextView) errorView.findViewById(R.id.titleTv);
//                    TextView messageTv = (TextView) errorView.findViewById(R.id.messageTv);
//                    Button okBtn = (Button) errorView.findViewById(R.id.okBtn);
//                    titleTv.setVisibility(View.GONE);
//                    messageTv.setText(message);
//
//                    okBtn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            errorDialog.dismiss();
//                        }
//                    });
                    pinInput.setError(message);

                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    barcodeView.decodeSingle(callback);
                    barcodeView.setStatusText("");
                    beepManager = new BeepManager(this);
                }
            }
        } else {
            ActivityCompat.requestPermissions(RedeemCouponActivity.this,
                    new String[]{Manifest.permission.CAMERA}, REQUEST_CODE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        barcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        barcodeView.pause();
    }

    public void callRedeemApi(String pin) {
        if (Config.isConnectedToInternet(RedeemCouponActivity.this)) {
            pd = new ProgressDialog(RedeemCouponActivity.this);
            pd.setCancelable(false);
            pd.setMessage("Loading...");
            pd.show();
            RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
            String userId = Config.getSharedPreferences(RedeemCouponActivity.this, "userId");
            String paymentId = Config.getSharedPreferences(RedeemCouponActivity.this, "paymentId");
            Log.e("In RedeemCouponAct", "Params : " + userId + ", " + couponId + ", " + paymentId + ", " + pin);
            Call<Common> call = apiInterface.redeemAmountByPin(userId, couponId, paymentId, pin,is_ping,ping_id);
            call.enqueue(new Callback<Common>() {
                @Override
                public void onResponse(Call<Common> call, Response<Common> response) {
                    pd.dismiss();
                    if (response.body().getSuccess() == 1) {
//                                    Toast.makeText(RedeemCouponActivity.this, "Amount redeemed successfully", Toast.LENGTH_LONG).show();
                        final DialogPlus successDialog = DialogPlus.newDialog(RedeemCouponActivity.this)
                                .setContentHolder(new ViewHolder(R.layout.dialog_redeemed_layout))
                                .setGravity(Gravity.CENTER)
                                .setCancelable(false)
                                .create();
                        successDialog.show();

                        View errorView = successDialog.getHolderView();
                        TextView titleTv = (TextView) errorView.findViewById(R.id.titleTv);
                        TextView messageTv = (TextView) errorView.findViewById(R.id.messageTv);
                        Button okBtn = (Button) errorView.findViewById(R.id.okBtn);
//                                        titleTv.setVisibility(View.GONE);
//                                        messageTv.setText("You have successfully redeemed this coupon");

                        okBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                successDialog.dismiss();
//                                finish();
                                if (Config.getSharedPreferences(RedeemCouponActivity.this, "isShowRateDialog") == null) {
                                    final Dialog rateDialog = new Dialog(RedeemCouponActivity.this);
                                    rateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    rateDialog.setContentView(R.layout.dialog_rate_us_layout);
                                    TextView titleTv = rateDialog.findViewById(R.id.titleTv);
                                    TextView messageTv = rateDialog.findViewById(R.id.messageTv);
                                    final CheckBox neverCb = rateDialog.findViewById(R.id.neverCb);
                                    Button rateNowBtn = rateDialog.findViewById(R.id.rateNowBtn);
                                    Button rateLaterBtn = rateDialog.findViewById(R.id.rateLaterBtn);

                                    titleTv.setText("Rate our App");
                                    messageTv.setText("If you like our app, please take a moment to rate it on play store!");
                                    rateNowBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            rateDialog.dismiss();
                                            startActivity(new Intent(Intent.ACTION_VIEW,
                                                    Uri.parse("http://play.google.com/store/apps/details?id="
                                                            + getApplicationContext().getPackageName())));
                                        }
                                    });

                                    rateLaterBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            rateDialog.dismiss();
                                            if (neverCb.isChecked()) {
                                                // Store flag for not to show rate us dialog
                                                rateDialog.dismiss();
                                                Config.saveSharedPreferences(RedeemCouponActivity.this, "isShowRateDialog", "0");
                                            }
                                            Intent intent=new Intent(RedeemCouponActivity.this,HomeActivity.class);
                                            startActivity(intent);
                                            finish();

                                        }
                                    });



                                    int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.8);
                                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                                    rateDialog.getWindow().setLayout(width, height);

                                    rateDialog.show();

//                                Intent intent1 = new Intent(RedeemCouponActivity.this, MerchantDetailsActivity.class);
////                                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
//                                intent1.putExtra("merchantId", merchantId);
//                                if(searchText != null && !searchText.equalsIgnoreCase("")) {
//                                    intent1.putExtra("searchText", searchText);
//                                }
//                                startActivity(intent1);
                                }else {
                                    Intent intent=new Intent(RedeemCouponActivity.this,HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
                    } else {
//                                    Toast.makeText(RedeemCouponActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        final DialogPlus errorDialog = DialogPlus.newDialog(RedeemCouponActivity.this)
                                .setContentHolder(new ViewHolder(R.layout.dialog_ok_layout))
                                .setGravity(Gravity.CENTER)
                                .create();
                        errorDialog.show();

                        View errorView = errorDialog.getHolderView();
                        TextView titleTv = (TextView) errorView.findViewById(R.id.titleTv);
                        TextView messageTv = (TextView) errorView.findViewById(R.id.messageTv);
                        Button okBtn = (Button) errorView.findViewById(R.id.okBtn);
                        titleTv.setVisibility(View.GONE);
                        messageTv.setText(response.body().getMessage());

                        okBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                errorDialog.dismiss();
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<Common> call, Throwable t) {
                    pd.dismiss();
//                    Toast.makeText(RedeemCouponActivity.this, "Some Error Occurred. Please Try Again.", Toast.LENGTH_LONG).show();
                    Config.showDialog(RedeemCouponActivity.this, Config.FailureMsg);
                }
            });
        } else {
            Config.showAlertForInternet(RedeemCouponActivity.this);
        }

    }

    public void pause(View view) {
        barcodeView.pause();
    }

    public void resume(View view) {
        barcodeView.resume();
    }

    public void triggerScan(View view) {
        barcodeView.decodeSingle(callback);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//        if (result != null) {
//            //if qrcode has nothing in it
//            if (result.getContents() == null) {
//                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
//            } else {
////                try {
//                Log.e("In QRScanAct", "Result : " + result.getContents());
//
////                } catch (JSONException e) {
////                    e.printStackTrace();
//                Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
////                }
//            }
//        } else {
//            super.onActivityResult(requestCode, resultCode, data);
//        }
//    }


//    @Override
//    public void onBackPressed() {
////        super.onBackPressed();
//        finish();
//        Intent intent = new Intent(RedeemCouponActivity.this, OfferDetailsActivity.class);
//        intent.putExtra("couponDetails", couponDetails);
//        intent.putExtra("companyName", companyName);
//        startActivity(intent);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
