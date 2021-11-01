package com.offeram.couponbouquet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Created by admin on 22-Sep-17.
 */

public class SmsReceiver extends BroadcastReceiver {

    private static SmsListener mListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle data = intent.getExtras();
        Log.e("In SmsReceiver", "In OnReceive");
        Object[] pdus = (Object[]) data.get("pdus");

        for (int i = 0; i < pdus.length; i++) {
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);

            String sender = smsMessage.getDisplayOriginatingAddress();
            String message = "";
            if (sender.contains(Config.SMS_DELIMITER)) {
                if (smsMessage.getDisplayMessageBody().contains(":")) {
//                String messageBody = smsMessage.getMessageBody();
                    message = smsMessage.getDisplayMessageBody().split(":")[1];
                    message = message.substring(1);
//                Log.e("SmsReceiver", "senderNum: " + sender + "; message: " + message);

                    //Pass on the text to our listener.
                    if(message != null) {
                         mListener.messageReceived(message);
                    }
                }
            }
            Log.e("SmsReceiver", "senderNum: " + sender + "; message: " + message);
        }

    }

    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }

}