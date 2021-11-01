package com.offeram.couponbouquet;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String token = FirebaseInstanceId.getInstance().getToken();
        Config.saveSharedPreferences(getApplicationContext(), "token", token);
        Log.e("In FirebaseIdService", "Token : " + token);
    }
}
