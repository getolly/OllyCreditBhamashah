package com.ollycredit.features.receivers.Incoming_sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.google.gson.Gson;
import com.ollycredit.utils.GlobalConstants;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by ch8n on 3/8/17.
 */

public class IncomingSms extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {

        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    Log.i("SmsReceiver", new Gson().toJson(currentMessage));

                    String address = currentMessage.getDisplayOriginatingAddress();

                    if (address.contains(GlobalConstants.OTP_SMS_MSG_ADDRESS_ONE)
                            || address.contains(GlobalConstants.OTP_SMS_MSG_ADDRESS_TWO)
                            || address.contains(GlobalConstants.OTP_SMS_MSG_ADDRESS_THREE)) {
                        String otpString = currentMessage.getDisplayMessageBody().trim().split("is")[1].trim();
                        Log.e("otp ", otpString);
//                        Intent myIntent = new Intent(GlobalUtils.OTP_ACTION_VALUE);
//                        myIntent.putExtra("otp", otpString);
//                        LocalBroadcastManager.getInstance(context).sendBroadcast(myIntent);
                        EventBus.getDefault().postSticky(otpString);
                    }
                }
            }
        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" + e);

        }
    }
}
