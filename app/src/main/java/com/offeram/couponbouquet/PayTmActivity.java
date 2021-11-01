package com.offeram.couponbouquet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.offeram.couponbouquet.responses.Common;
import com.offeram.couponbouquet.responses.GenerateChecksum;
import com.offeram.couponbouquet.responses.PurchaseVersion;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PayTmActivity extends AppCompatActivity {

    private int randomInt = 0;
    private PaytmPGService Service = null;
    String userId, contactNumber, versionId, amount, checkSumHash, promoCode, transactionId, isApplied;
    RetroApiInterface apiInterface;
    int paymentId = 0;
    String orderId = "";
    // paymentId var is made for sending the paymentid back to server which is retrieved from payment.php to purchase_version.php -> key as order_id
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_pay);

        Intent intent = getIntent();
        if (intent != null) {
            versionId = intent.getStringExtra("versionId");
            amount = intent.getStringExtra("amount");
            promoCode = intent.getStringExtra("promotionalCode");
            isApplied = intent.getStringExtra("isApplied");
            if (promoCode.equalsIgnoreCase("")) {
                promoCode = "0";
                isApplied = "1";
            }
        }
        Log.e("In TestPayAct", "VersionId n Amt n PromoCode Val : " + versionId + ", " + amount + " n " + promoCode);

        userId = Config.getSharedPreferences(PayTmActivity.this, "userId");
        contactNumber = Config.getSharedPreferences(PayTmActivity.this, "contactNumber");
//        startPayment("");
//        For random generated order id
//        Random randomGenerator = new Random();
//        randomInt = randomGenerator.nextInt(1000);

        pd = new ProgressDialog(PayTmActivity.this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        apiInterface = new RetroApiClient().getClient().create(RetroApiInterface.class);
//        Log.e("In TestPayAct", "Order Id : " + "acc" + String.valueOf(randomInt));
//        Log.e("In TestPayAct", "Order Id : " + "order1234");
        Call<Common> call = apiInterface.getOrderId(userId, versionId, amount, promoCode);
        call.enqueue(new Callback<Common>() {
            @Override
            public void onResponse(Call<Common> call, Response<Common> response) {
//                pd.dismiss();
                Common c = response.body();
                if (c.getSuccess() == 1) {
                    orderId = c.getOrderId();
                    paymentId = Integer.parseInt(c.getPaymentId());
                    Log.e("In TestPayAct", "OrderId, PaymentId n Amount : " + orderId + ", " + paymentId + " n " + amount);
//                    Call<GenerateChecksum> call1 = apiInterface.generateChecksum("acc" + String.valueOf(randomInt), amount);
                    Call<GenerateChecksum> call1;
//                    if (!isApplied.equals("0")) {
                    Log.e("In TestPayAct", "In IF promoCode : " + promoCode);
                    call1 = apiInterface.generateChecksum(orderId + "", amount, userId, contactNumber);
//                    } else {
//                        Log.e("In TestPayAct", "In Else promoCode : " + promoCode);
//                        call1 = apiInterface.generateChecksumWithPromoCode(orderId + "", amount, userId, promoCode);
//                    }
//        Call<GenerateChecksum> call = apiInterface.generateChecksum();
                    call1.enqueue(new Callback<GenerateChecksum>() {
                        @Override
                        public void onResponse(Call<GenerateChecksum> call1, Response<GenerateChecksum> response) {
                            pd.dismiss();
                            GenerateChecksum gc = response.body();
                            String paytmStatus = gc.getPaytmStatus();
                            if (paytmStatus.equalsIgnoreCase("1")) {
                                checkSumHash = gc.getChecksumHash();
                                startPayment(checkSumHash, orderId, paymentId, promoCode);
                            }
                        }

                        @Override
                        public void onFailure(Call<GenerateChecksum> call1, Throwable t) {
                            pd.dismiss();
                            Log.e("In PayAct", "Generate Checksum OnFailure : " + t.getMessage());
                            Toast.makeText(PayTmActivity.this, "Some Error Occurred. Please Try Again.", Toast.LENGTH_LONG).show();
                        }
                    });

                } else if (c.getSuccess() == 0) {
                    pd.dismiss();
                    final DialogPlus errorDialog = DialogPlus.newDialog(PayTmActivity.this)
                            .setContentHolder(new ViewHolder(R.layout.dialog_ok_layout))
                            .setGravity(Gravity.CENTER)
                            .create();
                    errorDialog.show();

                    View errorDialogView = errorDialog.getHolderView();
                    TextView titleTv = (TextView) errorDialogView.findViewById(R.id.titleTv);
                    TextView messageTv = (TextView) errorDialogView.findViewById(R.id.messageTv);
                    Button okBtn = (Button) errorDialogView.findViewById(R.id.okBtn);
                    titleTv.setVisibility(View.GONE);
                    messageTv.setText("Oops!! Something went wrong. Please Try Again");

                    okBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            errorDialog.dismiss();
                            finish();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Common> call, Throwable t) {
                pd.dismiss();
                Log.e("In PayAct", "GetOrderId OnFailure : " + t.getMessage());
                Toast.makeText(PayTmActivity.this, "Some Error Occurred. Please Try Again.", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void startPayment(String checkSumHash, final String orderId, final int paymentId, final String promoCode) {
        Log.e("In TestPayAct", "In StartPayment Method : " + checkSumHash + "  n  " + promoCode);


        //for testing environment
        /* Service = PaytmPGService.getStagingService(); */
        //for production environment
        Service = PaytmPGService.getProductionService();

        /*PaytmMerchant constructor takes two parameters
        1) Checksum generation url
        2) Checksum verification url
        Merchant should replace the below values with his values*/

//        PaymentMerchant was applicable for paytm v1.0
//        PaytmMerchant Merchant = new PaytmMerchant(Config.GenerateCheckSum, Config.VerifyCheckSum);

        //below parameter map is required to construct PaytmOrder object, Merchant should replace below map values with his own values
        Map<String, String> paramMap = new HashMap<String, String>();

        //these are mandatory parameters, first two are not needed
//        paramMap.put("REQUEST_TYPE", "DEFAULT");
//        paramMap.put("THEME", "merchant");
//        paramMap.put("ORDER_ID", "acc" + String.valueOf(randomInt));
//        paramMap.put("CUST_ID", 123456);
//        paramMap.put("TXN_AMOUNT", "1");
        //MID provided by paytm

        // Live Credentials For Paytm
        paramMap.put("MID", "MAGNAD01013772099418");
        paramMap.put("CUST_ID", userId);
        paramMap.put("ORDER_ID", orderId);
        paramMap.put("CHANNEL_ID", "WAP");
        paramMap.put("INDUSTRY_TYPE_ID", "Retail109");
        paramMap.put("WEBSITE", "MAGNADWAP");
        paramMap.put("TXN_AMOUNT", amount);
        paramMap.put("CALLBACK_URL", "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=" + orderId);
//        paramMap.put("CHECKSUMHASH", "wwAM7fKiKEj82PG3zrDDqjMCA9m6Um4wx4/EQHJDt6ThUQhuZPw/NwtMGcOZpJ4OzB4Q7WsLln4mq6L0gU0rpc01OK65cnPxcEyfFmCECqM=");
        paramMap.put("CHECKSUMHASH", checkSumHash);
        paramMap.put("MERC_UNQ_REF", contactNumber);
        if (isApplied.equals("0")) {
            paramMap.put("PROMO_CAMP_ID", promoCode);
        }

      /*  Test Credentials For Paytm
        paramMap.put("MID", "MagnAD84965361037964");
        paramMap.put("CUST_ID", userId);
        paramMap.put("ORDER_ID", orderId);
        paramMap.put("CHANNEL_ID", "WAP");
        paramMap.put("INDUSTRY_TYPE_ID", "Retail");
        paramMap.put("WEBSITE", "APP_STAGING");
        paramMap.put("TXN_AMOUNT", amount);
        paramMap.put("CALLBACK_URL", "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp");
//        paramMap.put("CHECKSUMHASH", "wwAM7fKiKEj82PG3zrDDqjMCA9m6Um4wx4/EQHJDt6ThUQhuZPw/NwtMGcOZpJ4OzB4Q7WsLln4mq6L0gU0rpc01OK65cnPxcEyfFmCECqM=");
        paramMap.put("CHECKSUMHASH", checkSumHash);
        if (!promoCode.equals("0")) {
            paramMap.put("PROMO_CAMP_ID", promoCode);
        }  */

        PaytmOrder Order = new PaytmOrder(paramMap);

        Service.initialize(Order, null);
        Service.startPaymentTransaction(PayTmActivity.this, true,
                false, new PaytmPaymentTransactionCallback() {
                    @Override
                    public void onTransactionResponse(Bundle bundle) {
                        Log.e("In TestPayAct", "In OnTransactionResponse : " + bundle);
                        String responseCode = bundle.get("RESPCODE").toString();
                        String responseMsg = bundle.get("RESPMSG").toString();
                        Log.e("In TestPayAct", "Response Code : " + responseCode);

                        if (responseCode.equals("01")) {
//                    Toast.makeText(PayTmActivity.this, "Transaction Done Successfully...", Toast.LENGTH_LONG).show();
                            transactionId = bundle.get("TXNID").toString();
//                    pd = new ProgressDialog(PayTmActivity.this);
//                    pd.setMessage("Loading");
//                    pd.setCancelable(false);
//                    pd.show();
                            apiInterface = new RetroApiClient().getClient().create(RetroApiInterface.class);
                            Call<PurchaseVersion> call = apiInterface.purchaseVersion(userId, versionId, amount, promoCode, paymentId + "", transactionId);
                            call.enqueue(new Callback<PurchaseVersion>() {
                                @Override
                                public void onResponse(Call<PurchaseVersion> call, Response<PurchaseVersion> response) {
//                            pd.dismiss();
                                    PurchaseVersion pv = response.body();
                                    finish();
                                    Log.e("In PayAct", "PaymentId n sideMenuText : " + pv.getPaymentId() + ", " + pv.getSideMenuText());
                                    Config.saveSharedPreferences(PayTmActivity.this, "versionId", "15");
                                    Config.saveSharedPreferences(PayTmActivity.this, "isDemo", "0");
                                    Config.saveSharedPreferences(PayTmActivity.this, "paymentId", pv.getPaymentId() + "");
                                    Config.saveSharedPreferences(PayTmActivity.this, "versionName", pv.getVersionName());
                                    Config.saveSharedPreferences(PayTmActivity.this, "sideMenuText", pv.getSideMenuText());
                                    Intent intent = new Intent(PayTmActivity.this, PaymentSuccess.class);
                                    startActivity(intent);
                                }

                                @Override
                                public void onFailure(Call<PurchaseVersion> call, Throwable t) {
//                            pd.dismiss();
                                    Toast.makeText(PayTmActivity.this, "Some Error Occurred. Please Try Again.", Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {
//                    Toast.makeText(PayTmActivity.this, "Transaction Cancelled By the User...", Toast.LENGTH_LONG).show();
                            if (responseCode.equals("701")) {
                                final DialogPlus errorDialog = DialogPlus.newDialog(PayTmActivity.this)
                                        .setContentHolder(new ViewHolder(R.layout.dialog_ok_layout))
                                        .setGravity(Gravity.CENTER)
                                        .create();
                                errorDialog.show();

                                View errorDialogView = errorDialog.getHolderView();
                                TextView titleTv = (TextView) errorDialogView.findViewById(R.id.titleTv);
                                TextView messageTv = (TextView) errorDialogView.findViewById(R.id.messageTv);
                                Button okBtn = (Button) errorDialogView.findViewById(R.id.okBtn);
                                titleTv.setVisibility(View.GONE);
                                messageTv.setText(responseMsg);

                                okBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        errorDialog.dismiss();
                                        finish();
                                    }
                                });

                            } else {
                                finish();
                                Intent intent = new Intent(PayTmActivity.this, PaymentFailed.class);
                                intent.putExtra("versionId", versionId);
                                intent.putExtra("amount", amount);
                                intent.putExtra("promotionalCode", promoCode);
                                intent.putExtra("isApplied", isApplied);
                                startActivity(intent);
                            }
                        }
                    }

                    @Override
                    public void networkNotAvailable() {
                        Log.e("In TestPayAct", "network unavailable : ");
                    }

                    @Override
                    public void clientAuthenticationFailed(String s) {
                        Log.e("In TestPayAct", "clientAuthenticationFailed : " + s);
                    }

                    @Override
                    public void someUIErrorOccurred(String s) {
                        Log.e("In TestPayAct", "someUIErrorOccurred : " + s);
                    }

                    @Override
                    public void onErrorLoadingWebPage(int i, String s, String s1) {
                        Log.e("In TestPayAct", "errorLoadingWebPage : " + i + "\n" + s + "\n" + s1);
                    }

                    @Override
                    public void onBackPressedCancelTransaction() {
                        Log.e("In TestPayAct", "In OnBackPressCancelTransaction");
                        finish();
                    }

                    @Override
                    public void onTransactionCancel(String s, Bundle bundle) {
                        Log.e("In TestPayAct", "In OnTransactionCancel : " + bundle);
                    }

                    public void onTransactionFailure(String s, Bundle bundle) {
                        Log.e("In TestPayAct", "Transaction Failure : " + s + "\n" + bundle);
                    }

                });

    }
}
