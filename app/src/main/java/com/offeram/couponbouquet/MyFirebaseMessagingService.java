package com.offeram.couponbouquet;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by HOME on 13-Sep-16.
 */
public class MyFirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    int isForeground = 0;
    private Context context = this;
    private Bitmap bitmap;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.e("In FirebaseMsgService", "In OnMsgReceived");
        if (remoteMessage.getData().size() > 0) {
            showNotification(remoteMessage.getData().get("Message"));
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            String data = remoteMessage.getNotification().getBody();
            showNotification(data);
        }
    }

    private void showNotification(String messageBody) {
        String title = "", message = "", id = "", merchantId = "",url="";
        Log.e("In FirebaseMsgService", "In SendNotification : " + messageBody);
        int code = 100;

        try {
            JSONObject jsonObject = new JSONObject(messageBody);
            code = jsonObject.getInt("code");
            id = jsonObject.getString("notification_id");
            title = jsonObject.getString("notification_title");
            message = jsonObject.getString("notification_message");
            if (jsonObject.has("image")){
                url = jsonObject.getString("image");
            }
            if (jsonObject.has("merchant_id")) {
                merchantId = jsonObject.getString("merchant_id");
            }

        } catch (JSONException e) {
            Log.e("Exception", "Exception : " + e.getMessage());
        }
        bitmap = getBitmapfromUrl(url);
        Intent intent = null;
//        try {
//            if (code == 116 || code == 118) {
        ActivityManager activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (getApplicationContext().getPackageName().equals(appProcess.processName)) {
                Log.e("In If ", "PackageName Same : " + appProcess.processName);
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    Log.e("Foreground App", appProcess.processName);
                    isForeground = 1;
//                    Handler handler = new Handler(Looper.getMainLooper());
//                    final String finalMessage = message;
//                    final String finalTitle = title;
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            AlertDialog dialog = new AlertDialog.Builder(new ContextThemeWrapper(getApplicationContext(), R.style.AppTheme))
//                                    .setTitle(finalTitle)
//                                    .setMessage(finalMessage)
//                                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            Intent intent1 = new Intent(context, HomeActivity.class);
//                                            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                            intent1.putExtra("from", "FirebaseMessaging");
//                                            startActivity(intent1);
//                                        }
//                                    })
//                                    .create();
//                            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//                            dialog.show();
                    Intent intent1 = new Intent(context, NotificationDialogActivity.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent1.putExtra("id", id);
                    intent1.putExtra("title", title);
                    intent1.putExtra("message", message);
                    startActivity(intent1);
//                        }
//                    }, 1000);

                } else {
                    Log.e("Background App", appProcess.processName);
                    isForeground = 0;
                }
            }
        }
//            }
//        } catch (SecurityException e) {

//            Log.e("In FirebaseMsgService", "Excp Msg : " + e.getMessage());
//        }

        if (!merchantId.equalsIgnoreCase("") && !merchantId.equalsIgnoreCase("0")) {
            intent = new Intent(this, MerchantDetailsActivity.class);
            intent.putExtra("merchantId", merchantId);
        } else {
            intent = new Intent(this, HomeActivity.class);
        }
        intent.putExtra("from", "FirebaseMessaging");
        Log.e("In FirebaseMsgService", "Message Value is : " + message);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(MyFirebaseMessagingService.this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = "Offeram";
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(channelId);
            if (notificationChannel == null) {
                int importance = NotificationManager.IMPORTANCE_HIGH; //Set the importance level
                notificationChannel = new NotificationChannel(channelId, "Offeram", importance);
                notificationManager.createNotificationChannel(notificationChannel);
            }
            if (isForeground == 0) {
                if (bitmap==null){
                    Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    android.support.v4.app.NotificationCompat.Builder notificationBuilder = new android.support.v4.app.NotificationCompat.Builder(this)
                            .setSmallIcon(getNotificationIcon())
                            .setColor(context.getResources().getColor(R.color.colorPrimary))
                            .setContentTitle(title)
                            .setContentText(message)
                            .setAutoCancel(true)
                            .setChannelId(channelId)
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent);


                    notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

                }else {
                    Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    android.support.v4.app.NotificationCompat.Builder notificationBuilder = new android.support.v4.app.NotificationCompat.Builder(this)
                            .setSmallIcon(getNotificationIcon())
                            .setColor(context.getResources().getColor(R.color.colorPrimary))
                            .setContentTitle(title)
                            .setContentText(message)
                            .setAutoCancel(true)
                            .setChannelId(channelId)
                            .setSound(defaultSoundUri)
                            .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap))
                            .setContentIntent(pendingIntent);


                    notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
                }

            }

        } else {
            if (isForeground == 0) {
                if (bitmap==null){
                    Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    android.support.v4.app.NotificationCompat.Builder notificationBuilder = new android.support.v4.app.NotificationCompat.Builder(this)
                            .setSmallIcon(getNotificationIcon())
                            .setColor(context.getResources().getColor(R.color.colorPrimary))
                            .setContentTitle(title)
                            .setContentText(message)
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent);

                    notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
                }else {
                    Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    android.support.v4.app.NotificationCompat.Builder notificationBuilder = new android.support.v4.app.NotificationCompat.Builder(this)
                            .setSmallIcon(getNotificationIcon())
                            .setColor(context.getResources().getColor(R.color.colorPrimary))
                            .setContentTitle(title)
                            .setContentText(message)
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap))
                            .setContentIntent(pendingIntent);

                    notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
                }

            }
        }

    }

    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.whitenotificationicon : R.drawable.ic_launcher;
    }


}


