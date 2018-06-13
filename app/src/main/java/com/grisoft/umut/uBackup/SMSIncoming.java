package com.grisoft.umut.uBackup;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Umut on 19.01.2016.
 */
public class SMSIncoming extends BroadcastReceiver {
    final SmsManager sms = SmsManager.getDefault();

    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();

                    Log.i("SmsReceiver", "senderNum: " + senderNum + "; message: " + message);
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context,
                            "senderNum: " + senderNum + ", message: " + message, duration);
                    toast.show();
                    SimpleDateFormat sdform = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    String date = sdform.format(new Date());

                    ContentValues contentValues = new ContentValues();


                    contentValues.put("date", date);
                    contentValues.put("sentSMS", senderNum);
                    contentValues.put("getSMS", "505050505");
                    contentValues.put("osms", message);
                    contentValues.put("isms", "-");
                    contentValues.put("isSend", 0);

                }
            }
        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" + e);

        }

    }

    public void add_database(String sentSMS, String getSMS, String osms, String isms) {
        SimpleDateFormat sdform = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String date = sdform.format(new Date());

        ContentValues contentValues = new ContentValues();


        contentValues.put("date", date);
        contentValues.put("sentSMS", sentSMS);
        contentValues.put("getSMS", getSMS);
        contentValues.put("osms", osms);
        contentValues.put("isms", isms);
        contentValues.put("isSend", 0);


    }
}


