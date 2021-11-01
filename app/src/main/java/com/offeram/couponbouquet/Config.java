package com.offeram.couponbouquet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.offeram.couponbouquet.models.CouponOutlet;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.content.Context.INPUT_METHOD_SERVICE;

//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.GooglePlayServicesUtil;

//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.GooglePlayServicesUtil;

public class Config {
//    public final static String URL = "http://www.offeram.com/api/";
//    public final static String URL = "https://www.offeram.com/test/api/";
//    public final static String URL = "http://restrobistrohospitality.com/offeram/api/";
//    public final static String URL = "https://www.offeram.com/test/apiv2/";   //live Url
//    public final static String URL = "http://credencetech.in/offeram/";   //testing Url
    public final static String URL = "https://offeram.com/apiv2/";   //Live Url with apiv2
//    public final static String URL = "https://www.offeram.com/test/apiv3/";   // Test Url after implementing prepared statement
    public final static String SMS_DELIMITER = "OFROTP";
    public final static String FailureMsg = "Oops!! Something went wrong, please try again...";
    public final static String GenerateCheckSum = "http://www.offeram.com/api/generateChecksum.php";
    public final static String VerifyCheckSum = "http://www.offeram.com/api/verifyChecksum.php";


    public static void showSnackBar(Activity activity, String message) {
        Snackbar snackbar = Snackbar
                .make(activity.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(Color.parseColor("#444444"));
        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.parseColor("#eeeeee"));
        textView.setTextSize(15);
        snackbar.show();
    }

//    public static boolean checkPlayServices(Activity activity) {
//        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
//        if (status != ConnectionResult.SUCCESS) {
//            GooglePlayServicesUtil.getErrorDialog(status, activity, 1001);
//            return false;
//        }
//        return true;
//    }


    public static void saveSharedPreferences(Context context, String key,
                                             String value) {
        SharedPreferences pref = context.getSharedPreferences("MyPref",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getSharedPreferences(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences("MyPref",
                Context.MODE_PRIVATE);
        return pref.getString(key, null);
    }

    public static boolean isConnectedToInternet(Context _context) {
        ConnectivityManager connectivity = (ConnectivityManager) _context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    public static void showAlertForInternet(final Activity context) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("No Internet Connection");
        alertDialog.setMessage("Please make sure that you are connected to the internet");
        alertDialog.setCancelable(false);
        alertDialog.setIcon(R.drawable.fail);

        alertDialog.setButton("Go to Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                context.startActivity(intent);
                context.finish();
            }
        });
        alertDialog.show();
    }

    public static JSONArray shuffleArray(JSONArray array) {
        JSONArray sliderArray = null;
        try {
            sliderArray = new JSONArray();
            Random rnd = new Random();
            for (int i = array.length() - 1; i > 0; i--) {
                int index = rnd.nextInt(i + 1);
                // Simple swap
                JSONObject sliderRow = array.getJSONObject(index);
                sliderArray.put(sliderRow);
            }
        } catch (JSONException je) {
            Log.e("In Config", "Shuffle Slider : " + je.getMessage());
        }
        return sliderArray;
    }

//    public static void startService(Context context) {
//        Intent alarmIntent = new Intent(context,
//                AlarmReceiver.class);
//
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
//                alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        int interval = 3000;
//        Calendar calendar = Calendar.getInstance();
////            calendar.set(Calendar.HOUR_OF_DAY, 11);
////            calendar.set(Calendar.MINUTE, 00);
////            calendar.set(Calendar.SECOND, 01);
//
//        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), calendar.getTimeInMillis() + 10 * 1000, pendingIntent);
//
//    }

    public static void hideSoftKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
        View view = ((Activity)context).getCurrentFocus();
        if (view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void showDialog(Context context, String message){
        final DialogPlus dialog = DialogPlus.newDialog(context)
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
        messageTv.setText(message);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public static List<CouponOutlet> sortListDistanceWise(List<CouponOutlet> data){
        List<CouponOutlet> tempList = new ArrayList<>();
        List<CouponOutlet> sortedList = new ArrayList<>();
        tempList.addAll(data);
        double dist;
        for (int i = 0; i < data.size(); i++) {
            dist = tempList.get(0).getDistance();
            int isMin = 0, selIndex = 0;
            for (CouponOutlet co : tempList) {
                if (dist > co.getDistance()) {
                    isMin = 1;
                    selIndex = tempList.indexOf(co);
                    dist = co.getDistance();
                }
            }

            sortedList.add(tempList.get(selIndex));
            tempList.remove(selIndex);
        }
//        for (CouponOutlet c : sortedList) {
//            Log.e("In Config", "Sorted List : " + c.getDistance());
//        }
        return sortedList;
    }

}
