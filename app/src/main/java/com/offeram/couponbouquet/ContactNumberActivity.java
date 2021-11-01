package com.offeram.couponbouquet;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.offeram.couponbouquet.adapters.CityAdapter;
import com.offeram.couponbouquet.models.City;
import com.offeram.couponbouquet.models.CityModel;
import com.offeram.couponbouquet.responses.Common;
import com.offeram.couponbouquet.responses.Otp;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactNumberActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText contactNumberEt;
    Button proceedBtn;
    TextView termsConditionsTv;
    String deviceId = "", deviceType = "0";
    Boolean isValid;
    ProgressDialog pd;
    private Spinner city_spinner;
    private String cityID;
    private String cityName;
    private int RESOLVE_HINT = 101;
    private GoogleApiClient apiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_number);

//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
////        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("Mobile Number");

        contactNumberEt = (EditText) findViewById(R.id.contactNumberEt);
        termsConditionsTv = (TextView) findViewById(R.id.termsConditionsTv);
        proceedBtn = (Button) findViewById(R.id.proceedBtn);
        city_spinner = (Spinner) findViewById(R.id.city_spinner);
        final Common cityjson = new Gson().fromJson(Config.getSharedPreferences(ContactNumberActivity.this, "splashResponse"), Common.class);

        if (cityjson.getAllCity().size() > 0) {
            ArrayList<CityModel> objects = new ArrayList<CityModel>();
            for (int k = 0; k < cityjson.getAllCity().size(); k++) {
                CityModel obj = new CityModel();
                obj.setAll(cityjson.getAllCity().get(k).getCity_name());
                objects.add(obj);
            }
            city_spinner.setAdapter(new CityAdapter(this, objects));
        }
        city_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                City obj = cityjson.getAllCity().get(position);
                cityID = obj.getCity_id();
                cityName = obj.getCity_name();
                Log.e("cityID:", cityID + "");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (Config.getSharedPreferences(ContactNumberActivity.this, "token") == null) {
            deviceId = FirebaseInstanceId.getInstance().getToken();
        } else {
            deviceId = Config.getSharedPreferences(ContactNumberActivity.this, "token");
        }

        termsConditionsTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactNumberActivity.this, WebViewActivity.class);
                intent.putExtra("title", "Terms & Conditions");
                intent.putExtra("url", "http://offeram.com/Offeram_Terms_and_Conditions.html");
                startActivity(intent);
            }
        });

        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isValid = true;
                final String contactNumber = contactNumberEt.getText().toString();
                String message = "";
                if (contactNumber.equals("")) {
                    isValid = false;
//                    Config.showSnackBar(ContactNumberActivity.this, "Must enter mobile number");
                    message = "Must enter mobile number";
                } else {
                    if (contactNumber.length() != 10) {
                        isValid = false;
//                        Config.showSnackBar(ContactNumberActivity.this, "Must contain only 10 digits");
                        message = "Must contain only 10 digits";
                    }
                }

                if (isValid) {
                    if (Config.isConnectedToInternet(ContactNumberActivity.this)) {
                        pd = new ProgressDialog(ContactNumberActivity.this);
                        pd.setCancelable(false);
                        pd.setMessage("Loading...");
                        pd.show();
                        RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
                        Call<Otp> otp = apiInterface.getOtp(contactNumber, deviceId, deviceType, "0", cityID);
                        otp.enqueue(new Callback<Otp>() {
                            @Override
                            public void onResponse(Call<Otp> call, Response<Otp> response) {
                                pd.dismiss();
                                Otp o = response.body();
                                if (o != null) {
                                    if (o.getSuccess() == 1) {
                                        Config.saveSharedPreferences(ContactNumberActivity.this, "userId", o.getUserId());
                                        Config.saveSharedPreferences(ContactNumberActivity.this, "versionId", o.getVersionId());
                                        Config.saveSharedPreferences(ContactNumberActivity.this, "paymentId", o.getPaymentId());
                                        Config.saveSharedPreferences(ContactNumberActivity.this, "versionName", o.getVersionName());
                                        Config.saveSharedPreferences(ContactNumberActivity.this, "contactNumber", contactNumber);
                                        Config.saveSharedPreferences(ContactNumberActivity.this, "cityID", cityID);
                                        Config.saveSharedPreferences(ContactNumberActivity.this, "cityName", cityName);
                                        Config.saveSharedPreferences(ContactNumberActivity.this, "isFreshUser", o.getIs_fresh_user());

                                        Intent intent = new Intent(ContactNumberActivity.this, OtpActivity.class);
                                        intent.putExtra("contactNumber", contactNumber);
                                        intent.putExtra("cityId", cityID);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(ContactNumberActivity.this, "Some Error Occurred. Please Try Again.", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Config.showDialog(ContactNumberActivity.this, Config.FailureMsg);
                                }
                            }

                            @Override
                            public void onFailure(Call<Otp> call, Throwable t) {
                                pd.dismiss();
                                Toast.makeText(ContactNumberActivity.this, "Some Error Occurred. Please Try Again.", Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        Config.showAlertForInternet(ContactNumberActivity.this);
                    }
                } else {
                    final DialogPlus errorDialog = DialogPlus.newDialog(ContactNumberActivity.this)
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
//        termsConditionsTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ContactNumberActivity.this, TermsNConditionsActivity.class);
//                startActivity(intent);
//            }
//        });
    }
}
