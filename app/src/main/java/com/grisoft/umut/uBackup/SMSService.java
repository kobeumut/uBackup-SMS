package com.grisoft.umut.uBackup;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Umut on 8.01.2016.
 */
public class SMSService extends Service {
    SMSContentObserver contentObserver;
    public Handler handler;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }
    @Override
    public void onCreate() {
        contentObserver = new SMSContentObserver(this, handler);
        Uri uri = Uri.parse("content://sms");
        getContentResolver().registerContentObserver(uri, true, contentObserver);
    }
}
