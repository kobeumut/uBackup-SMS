package com.grisoft.umut.uBackup;

/**
 * Created by Umut on 3.01.2016.
 */
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SMSContentObserver extends ContentObserver {

    Context context;
    Handler handler;
    public long lastId;
    private static int initialPos;
    private static final Uri uriSMS = Uri.parse("content://sms/sent");

    public SMSContentObserver(Context c, Handler handler) {
        super(handler);
        // TODO Auto-generated constructor stub
        this.context = c;
        this.handler = handler;
        initialPos = getLastMsgId();
    }
    public int getLastMsgId() {
        Cursor cur = context.getContentResolver().query(uriSMS, null, null, null, null);
        cur.moveToFirst();
        int lastMsgId = cur.getInt(cur.getColumnIndex("_id"));
        Log.i("Son SMS:", "Last sent message id: " + String.valueOf(lastMsgId));
        return lastMsgId;
    }
    protected void queryLastSentSMS() {

        new Thread(new Runnable() {

            @Override
            public void run() {
                Cursor cur =
                        context.getContentResolver().query(uriSMS, null, null, null, null);

                if (cur.moveToNext()) {


                    try {
                        if (initialPos != getLastMsgId()) {
                            // Here you get the last sms. Do what you want.
                            //int smsid = cur.getInt(cur.getColumnIndex("_id"));
                            //String name = cur.getString(cur.getColumnIndex("person"));
                            String mynumber = cur.getString(cur.getColumnIndex("address"));
                            String mytext = cur.getString(cur.getColumnIndex("body"));
                            Date tarih = new Date(cur.getLong(cur.getColumnIndex("date")));
                 //           System.out.println(" Receiver Ph no :"+mynumber);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            String date = sdf.format(new Date(cur.getLong(cur.getColumnIndex("date"))));
                            new BackupActivity().showData();
                            // Then, set initialPos to the current position.
                            initialPos = getLastMsgId();
                        }
                    } catch (Exception e) {
                        // Treat exception here
                        Log.e("SMS Alma:",e.toString());
                    }
                }
                cur.close();
            }
        }).start();

    }
/*    public void saveData(String mynumber, String mytext, String name, String yournumber, String Yourtext, String date){
        SMSData sD = new SMSData();
        sD.setMynumber(mynumber);
        sD.setMytext(mytext);
        sD.setName(name);
        sD.setYournumber(yournumber);
        sD.setYourtext(Yourtext);
        sD.setDate(date);
        Realm realm = Realm.getInstance(context);
        realm.beginTransaction();
        realm.copyToRealm(sD);
        realm.commitTransaction();
    }*/
    @Override
    public void onChange(boolean selfChange) {
        // TODO Auto-generated method stub
        super.onChange(selfChange);
        queryLastSentSMS();
 /*       Uri outMMS = Uri.parse("content://sms/sent");
        //Desc descending ASC in ascending order
        Cursor cursor = context.getContentResolver().query(outMMS, null, null, null, "date DESC");
        if(cursor != null){
            System.out.println("the number is " + cursor.getCount());
            StringBuilder  builder = new StringBuilder();
            cursor.moveToFirst();
                String mynumber = cursor.getString(cursor.getColumnIndex("address"));
                String mytext = cursor.getString(cursor.getColumnIndex("body"));
                if (lastId == cursor.getLong(cursor.getColumnIndex("_id"))){
                    builder.append("CGönderen:" + cursor.getString(cursor.getColumnIndex("address")));
                    builder.append("CMesaj: "+cursor.getString(cursor.getColumnIndex("body"))+"\n");
                }else{
                    builder.append("Gönderen:" + cursor.getString(cursor.getColumnIndex("address")));
                    builder.append("Mesaj: "+cursor.getString(cursor.getColumnIndex("body"))+"\n");
                    lastId = cursor.getLong(cursor.getColumnIndex("_id"));
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
                    String date = sdf.format(new Date());
                    saveData(mynumber,mytext,"","","",date);
                }


            cursor.close();
            String builder2 = builder.toString();
            handler.obtainMessage(1, builder2).sendToTarget();
        }*/
    }

}