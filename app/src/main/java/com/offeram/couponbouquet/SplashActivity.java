
package com.offeram.couponbouquet;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.gson.Gson;
import com.offeram.couponbouquet.models.Contact;
import com.offeram.couponbouquet.responses.Common;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, ResultCallback<LocationSettingsResult>, LocationListener {

    final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest locationRequest;
    PendingResult<LocationSettingsResult> result;
    int REQUEST_CHECK_SETTINGS = 100;
    String latitude = "", longitude = "", deviceId = "";
    int isLatLongUpdate = 0;
    ProgressDialog pd;

    public String strReferAndEarnText;
    public String strOfferemCoinsLabel1Text;
    public String strOfferemCoinsLabel2Text;
    public String strOfferemCoinsLabel3Text;
    public String strOfferemCoinsLabel4Text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Log.e("In SplashAct", "In OnCreate");

//        if (Config.isConnectedToInternet(SplashActivity.this))
//        Config.saveSharedPreferences(SplashActivity.this, "latitude", latitude);
//        Config.saveSharedPreferences(SplashActivity.this, "longitude", longitude);
        new SplashTask().execute();
        Config.saveSharedPreferences(SplashActivity.this, "isDemo", "0");
        Config.saveSharedPreferences(SplashActivity.this, "IsClosedTambolaAlert", "0");
//        else
//            Config.showAlertForInternet(SplashActivity.this);
    }

    class SplashTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            try {
                PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
                for (Signature signature : info.signatures) {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    String hashKey = new String(Base64.encode(md.digest(), 0));
                    Log.e("In SplashAct", "Hash Key: " + hashKey);
                }
            } catch (Exception e) {
                Log.e("In SplashAct", "Excp Msg : " + e.getMessage());
            }

            List<String> permissionsNeeded = new ArrayList<String>();
            final List<String> permissionsList = new ArrayList<String>();
//            if (!addPermission(permissionsList, Manifest.permission.CAMERA))
//                permissionsNeeded.add("Camera");
            if (!addPermission(permissionsList, Manifest.permission.READ_CONTACTS))
                permissionsNeeded.add("Read Contacts");
            if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
                permissionsNeeded.add("Location");
            if (permissionsList.size() > 0) {
                if (permissionsNeeded.size() > 0) {
                    // Need Rationale
                    String message = "You need to grant access to " + permissionsNeeded.get(0);
                    for (int i = 1; i < permissionsNeeded.size(); i++)
                        message = message + ", " + permissionsNeeded.get(i);
                    showMessageOKCancel(message,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(SplashActivity.this, permissionsList.toArray(new String[permissionsList.size()]),
                                            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                                }
                            });
                    return;
                } else {
                    getDataAccordingToPermission();
                }
            } else {
                getDataAccordingToPermission();
            }

        }
    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (ContextCompat.checkSelfPermission(SplashActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            return false;
        }
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.app.AlertDialog.Builder(SplashActivity.this)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getDataAccordingToPermission();
                    }
                })
                .create()
                .show();
    }

    private void getDataAccordingToPermission() {
        // Note :- This function is made to check whether location permission is granted or not and if it is granted then
        // to check whether LocationSettingsResult dialog should be displayed or not (i.e. GPS is on or off)
        Log.e("In SplashAct", "In getDataAccorToPermission");
        if (ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Log.e("In GetData", "In IF Permission Granted");
            if (mGoogleApiClient == null) {
                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addApi(LocationServices.API)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this).build();
                mGoogleApiClient.connect();
                locationRequest = LocationRequest.create();
                locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                locationRequest.setInterval(30 * 1000);
                locationRequest.setFastestInterval(5 * 1000);
            } else {
                getDataOnSplash();
            }
        } else {
            Log.e("In GetData", "In Else Permission Denied");
            // Note :- Direct api call as permissions are denied
            latitude = "0";
            longitude = "0";
            Config.saveSharedPreferences(SplashActivity.this, "latitude", latitude);
            Config.saveSharedPreferences(SplashActivity.this, "longitude", longitude);
            getDataOnSplash();
        }
    }

    public void getDataOnSplash() {
        if (Config.isConnectedToInternet(SplashActivity.this)) {
//                pd = new ProgressDialog(SplashActivity.this);
//                pd.setCancelable(false);
//                pd.setMessage("Loading...");
//                pd.show();
            String userId = "";  // Note :- Used for getting count for unread notifications
            if (Config.getSharedPreferences(SplashActivity.this, "userId") != null) {
                userId = Config.getSharedPreferences(SplashActivity.this, "userId");
            } else {
                userId = "0";
            }
            RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
            Call<Common> call = apiInterface.getDataOnSplash(userId);
            call.enqueue(new Callback<Common>() {
                @Override
                public void onResponse(Call<Common> call, Response<Common> response) {
//                        pd.dismiss();
                    Log.e("In SplashAct", "GetData Response : " + new Gson().toJson(response));
                    Common c = response.body();
                    if (c != null) {
                        if (c.getSuccess() == 1) {
                            Config.saveSharedPreferences(SplashActivity.this, "isUpdate", c.getIsUpdate() + "");

//                            Config.saveSharedPreferences();
//                            strReferAndEarnText = c.getReferAndEarnText();
//                            strOfferemCoinsLabel1Text = c.getOfferamCoinsLabel1Text();
//                            strOfferemCoinsLabel2Text = c.getOfferamCoinsLabel2Text();
//                            strOfferemCoinsLabel3Text = c.getOfferamCoinsLabel3Text();
//                            strOfferemCoinsLabel4Text = c.getOfferamCoinsLabel4Text();
                            Gson gson = new Gson();
                            String splashJson = gson.toJson(c, Common.class);
                            Config.saveSharedPreferences(SplashActivity.this, "splashResponse", splashJson);
                            Config.saveSharedPreferences(SplashActivity.this, "paymentId", c.getPaymentId());
                            finish();
                            if (c.getAllCity().size() > 0) {
                                Config.saveSharedPreferences(SplashActivity.this, "splashResponse", splashJson);
                            }
                            if (Config.getSharedPreferences(SplashActivity.this, "showWelcomeScreen") == null) {
                                Intent intent = new Intent(SplashActivity.this, WelcomeActivity.class);
                                startActivity(intent);
                            } else {
                                if (Config.getSharedPreferences(SplashActivity.this, "userId") != null) {
                                    // Retrieve user contacts and store it in database
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (ContextCompat.checkSelfPermission(SplashActivity.this,
                                                    Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                                                //  Custom Data
//                                            Config.saveSharedPreferences(SplashActivity.this, "isSyncContacts", "1");
                                                if (Config.getSharedPreferences(SplashActivity.this, "isSyncContacts") == null)
                                                    getContacts();
                                                else
                                                    Log.e("In SplashAct", "Contacts already synced");
                                            }
                                        }
                                    }).start();

                                    if (Config.getSharedPreferences(SplashActivity.this, "isVerified") != null) {
                                        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(SplashActivity.this, ContactNumberActivity.class);
                                        startActivity(intent);
                                    }
                                } else {
                                    Intent intent = new Intent(SplashActivity.this, ContactNumberActivity.class);
                                    startActivity(intent);
                                }
                            }
                        } else {
//                            Toast.makeText(SplashActivity.this, "Some Error Occurred. Please Try Again.", Toast.LENGTH_LONG).show();
                            Config.showDialog(SplashActivity.this, c.getMessage());
                        }
                    } else {
                        Config.showDialog(SplashActivity.this, Config.FailureMsg);
                    }

                }

                @Override
                public void onFailure(Call<Common> call, Throwable t) {
//                        pd.dismiss();
//                        Toast.makeText(SplashActivity.this, "Some Error Occurred. Please Try Again.", Toast.LENGTH_LONG).show();
                    Log.e("In SplashAct", "In OnFailure Msg : " + t.getMessage());
                    Config.showDialog(SplashActivity.this, Config.FailureMsg);
                }
            });
        } else {
            Config.showAlertForInternet(SplashActivity.this);
        }
    }

    public void getContacts() {
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null,
                null, null, null);
        List<Contact> contactList = new ArrayList<>();
        if (cursor.getCount() > 0) {
//            Log.e("In GetContacts", "In If");
            while (cursor.moveToNext()) {
//                Log.e("In GetContacts", "In While : " + cursor.getCount());
                Contact c = new Contact();
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
//                Log.e("In GetContacts", "Name : " + name);
                c.setName(name);
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
//                        Log.e("In GetContacts", "Number : " + phoneNumber);
                        c.setNumber(phoneNumber);
                    }
                    phoneCursor.close();

                }
                // Add the contact model to the list
                contactList.add(c);
            }
            cursor.close();
        }

        Log.e("In GetContacts", "List : " + contactList.size());
        String userId = Config.getSharedPreferences(SplashActivity.this, "userId");
        String contactJson = new Gson().toJson(contactList);
//        Log.e("In GetContacts", "Contact JSON : " + contactJson);

        Log.e("In GetContacts", "Params : UserId -> " + userId + " Contact JSON -> " + contactJson);
        RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
        Call<Common> call = apiInterface.syncContacts(userId, contactJson);
        call.enqueue(new Callback<Common>() {
            @Override
            public void onResponse(Call<Common> call, Response<Common> response) {
                Common c = response.body();
                Log.e("In SyncContacts", "Response : " + new Gson().toJson(c));
                if (c != null && c.getSuccess() == 1) {
                    Config.saveSharedPreferences(SplashActivity.this, "isSyncContacts", "1");
                }
            }

            @Override
            public void onFailure(Call<Common> call, Throwable t) {
                Log.e("In SyncContacts", "OnFailure Msg : " + t.getMessage());
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
//                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_CONTACTS, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
//                perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                if (perms.get(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED &&
                        perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mGoogleApiClient = new GoogleApiClient.Builder(this)
                            .addApi(LocationServices.API)
                            .addConnectionCallbacks(this)
                            .addOnConnectionFailedListener(this).build();
                    mGoogleApiClient.connect();
                    locationRequest = LocationRequest.create();
                    locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                    locationRequest.setInterval(30 * 1000);
                    locationRequest.setFastestInterval(5 * 1000);
                } else {
                    Log.e("In SplashAct", "Some Permission are denied");
//                    Toast.makeText(SplashActivity.this, "Some Permissions are Denied", Toast.LENGTH_SHORT)
//                            .show();
//                    finish();
                    getDataAccordingToPermission();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.e("onConnected", "onConnected");
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        builder.build()
                );

        result.setResultCallback(this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        Log.e("onResult", "onResult : " + status);
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                if (location != null) {
                    Log.e("In SplashAct", "In OnResult IF");
                    latitude = "" + location.getLatitude();
                    longitude = "" + location.getLongitude();
                    Config.saveSharedPreferences(SplashActivity.this, "latitude", latitude);
                    Config.saveSharedPreferences(SplashActivity.this, "longitude", longitude);
                    getDataOnSplash();
                } else {
                    Log.e("In SplashAct", "In OnResult ELSE");
                    latitude = "0";
                    longitude = "0";
                    if (isLatLongUpdate == 0)
                        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
                    else
                        getDataOnSplash();
                }
                Log.e("In SplashAct", "In OnResult LatLong Values : " + latitude + " n " + longitude);

                break;

            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                //  Location settings are not satisfied. Show the user a dialog

                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().

                    status.startResolutionForResult(SplashActivity.this, REQUEST_CHECK_SETTINGS);

                } catch (IntentSender.SendIntentException e) {

                    //failed to show
                }
                break;

            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                // Location settings are unavailable so not possible to show any dialog now
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CHECK_SETTINGS) {
            Log.e("onActivityResult", "onActivityResult : " + resultCode);
            if (resultCode == RESULT_OK) {
                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addApi(LocationServices.API)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this).build();
                mGoogleApiClient.connect();
                locationRequest = LocationRequest.create();
                locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                locationRequest.setInterval(30 * 1000);
                locationRequest.setFastestInterval(5 * 1000);

            } else {
//                Toast.makeText(getApplicationContext(), "GPS is not enabled", Toast.LENGTH_LONG).show();
                Config.saveSharedPreferences(SplashActivity.this, "latitude", "0");
                Config.saveSharedPreferences(SplashActivity.this, "longitude", "0");
                getDataAccordingToPermission();
            }

        } else {
            Config.saveSharedPreferences(SplashActivity.this, "latitude", "0");
            Config.saveSharedPreferences(SplashActivity.this, "longitude", "0");
            getDataAccordingToPermission();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e("In SplashAct", "In OnLocationChanged");
        ++isLatLongUpdate;
        if (isLatLongUpdate == 1) {
            if (location != null) {
                latitude = "" + location.getLatitude();
                longitude = "" + location.getLongitude();
            } else {
                latitude = "0";
                longitude = "0";
            }
            Log.e("In SplashAct", "In OnLocChanged LatLong Values : " + latitude + " n " + longitude);
            Config.saveSharedPreferences(SplashActivity.this, "latitude", latitude);
            Config.saveSharedPreferences(SplashActivity.this, "longitude", longitude);
            getDataOnSplash();
        }
    }
}
