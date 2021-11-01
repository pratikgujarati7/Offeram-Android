package com.offeram.couponbouquet;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.offeram.couponbouquet.adapters.ContactListAdapter;
import com.offeram.couponbouquet.responses.Common;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PingActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView titleTv, contactTv, pingBtn;
    TextInputLayout contactInput;
    public EditText contactNumberEt;
    RecyclerView contactList;
    ArrayList<HashMap<String, String>> list = new ArrayList<>();
    ContactListAdapter adapter;
    String couponId = "", contactNumber = "";
    Cursor cursor;
    Boolean isValid;
    ProgressDialog pd;
    Handler mHandler;
    public int REQUEST_CODE = 124;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ping);

        Intent intent = getIntent();
        if (intent.hasExtra("couponId")) {
            couponId = intent.getStringExtra("couponId");
        }
        Log.e("In PingActivity", "CouponId Val : " + couponId);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        titleTv = (TextView) toolbar.findViewById(R.id.titleTv);
        titleTv.setText("Ping This Offer");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        contactTv = (TextView) findViewById(R.id.contactTv);
        pingBtn = (TextView) findViewById(R.id.pingBtn);
        contactInput = (TextInputLayout) findViewById(R.id.contactInput);
        contactNumberEt = (EditText) findViewById(R.id.contactNumberEt);
        contactList = (RecyclerView) findViewById(R.id.contactList);
        contactList.setLayoutManager(new LinearLayoutManager(PingActivity.this));
        contactList.setVisibility(View.GONE);

//        pd = new ProgressDialog(PingActivity.this);
//        pd.setMessage("Loading...");
//        pd.setCancelable(false);
//        pd.show();
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                if (ContextCompat.checkSelfPermission(PingActivity.this, Manifest.permission.READ_CONTACTS)
//                        == PackageManager.PERMISSION_GRANTED)
//                    getContacts();
//                else
//                    ActivityCompat.requestPermissions(PingActivity.this,
//                            new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE);
//            }
//        }).start();
//
//        mHandler = new Handler() {
//            public void handleMessage(Message msg) {
//                pd.dismiss();
//            }
//        };

        contactNumberEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString() != null && adapter != null) {
                    Log.e("In PingActivity", "OnTextChanged : " + charSequence.toString());
                    adapter.getFilter().filter(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        pingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isValid = true;
                String message = "";
                contactNumber = contactNumberEt.getText().toString();
                if (contactNumber.equalsIgnoreCase("")) {
                    isValid = false;
                    message = "Must enter contact number";
                }

                if (isValid) {
                    if (Config.isConnectedToInternet(PingActivity.this)) {
                        pd = new ProgressDialog(PingActivity.this);
                        pd.setMessage("Loading...");
                        pd.setCancelable(false);
                        pd.show();

                        String userId = Config.getSharedPreferences(PingActivity.this, "userId");
                        Log.e("In PingActivity", "Params : UserId -> " + userId + ", CouponId -> " + couponId
                                + " n Contact -> " + contactNumber);
                        RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
                        Call<Common> call = apiInterface.sharePing(userId, couponId, contactNumber);
                        call.enqueue(new Callback<Common>() {
                            @Override
                            public void onResponse(Call<Common> call, Response<Common> response) {
                                pd.dismiss();
                                Common c = response.body();
                                if (c.getSuccess() == 1) {
                                    final DialogPlus dialog = DialogPlus.newDialog(PingActivity.this)
                                            .setContentHolder(new ViewHolder(R.layout.dialog_ok_layout))
                                            .setContentHeight(LinearLayout.LayoutParams.WRAP_CONTENT)
                                            .setCancelable(false)
                                            .setGravity(Gravity.CENTER)
                                            .create();
                                    dialog.show();

                                    View errorView = dialog.getHolderView();
                                    TextView titleTv = (TextView) errorView.findViewById(R.id.titleTv);
                                    TextView messageTv = (TextView) errorView.findViewById(R.id.messageTv);
                                    Button okBtn = (Button) errorView.findViewById(R.id.okBtn);
                                    titleTv.setVisibility(View.GONE);
                                    messageTv.setText(c.getMessage());
                                    okBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                            Intent intent1=new Intent(PingActivity.this,HomeActivity.class);
                                            intent1.putExtra("from","");
                                            startActivity(intent1);
                                            finish();
                                        }
                                    });
                                } else {
                                    Config.showDialog(PingActivity.this, c.getMessage());
                                }
                            }

                            @Override
                            public void onFailure(Call<Common> call, Throwable t) {
                                pd.dismiss();
                                Log.e("In PingActivity", "OnFailure : " + t.getMessage());
                                Config.showDialog(PingActivity.this, Config.FailureMsg);
                            }
                        });

                    } else {
                        Config.showAlertForInternet(PingActivity.this);
                    }
                } else {
                    Config.showDialog(PingActivity.this, message);
                }
            }
        });

    }

    public void getContacts() {
        ContentResolver contentResolver = getContentResolver();
        cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null,
                null, null, null);
        if (cursor.getCount() > 0) {
//            Log.e("In GetContacts", "In If");
            while (cursor.moveToNext()) {
//                Log.e("In GetContacts", "In While : " + cursor.getCount());
                HashMap<String, String> map = new HashMap<>();
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
//                Log.e("In PingActivity", "Name : " + name);
                map.put("name", name);
                String contact_id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {
                    //This is to read multiple phone numbers associated with the same contact
                    Cursor phoneCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{contact_id}, null);

                    if (phoneCursor.moveToFirst()) {
                        String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex
                                (ContactsContract.CommonDataKinds.Phone.NUMBER));
//                        Log.e("In PingActivity", "Number : " + phoneNumber);
                        map.put("number", phoneNumber);
                    }
                    phoneCursor.close();

                }
//                Log.e("In PingActivity", "Hashmap val : " + map);
                // Add the contact to the ArrayList
                list.add(map);
            }
            cursor.close();
        }
        Log.e("In PingActivity", "List : " + list);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(0);
                if (list.size() > 0) {
                    adapter = new ContactListAdapter(PingActivity.this, list);
                    contactList.setAdapter(adapter);
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
                    getContacts();
                }
            }
        } else {
            ActivityCompat.requestPermissions(PingActivity.this,
                    new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
