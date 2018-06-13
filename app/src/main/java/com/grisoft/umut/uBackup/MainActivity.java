package com.grisoft.umut.uBackup;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Telephony;


import com.vlonjatg.android.apptourlibrary.AppTour;
import com.vlonjatg.android.apptourlibrary.MaterialSlide;

/**
 * Created by Umut on 15.02.2016.
 */
public class MainActivity extends AppTour {

    public Boolean flag;
    public SharedPreferences applicationpreferences;
    public SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void init(Bundle savedInstanceState) {

         applicationpreferences = PreferenceManager
                .getDefaultSharedPreferences(MainActivity.this);

        editor = applicationpreferences .edit();

        flag = applicationpreferences .getBoolean("flag", false);

        if (flag) {
            onDonePressed();
        }else {
            String defaultPackage = null;

            defaultPackage = Telephony.Sms.getDefaultSmsPackage(this);
            editor.putBoolean("flag", true);
            final String myPackageName = getPackageName();
            if (!Telephony.Sms.getDefaultSmsPackage(this).equals(myPackageName)) {
                editor.putString("defaultPackage", defaultPackage);
                BackupActivity ba = new BackupActivity();
                ba.keepit = PreferenceManager
                        .getDefaultSharedPreferences(this);
                ba.keepeditor = ba.keepit.edit();
                ba.keepeditor.putString("defaultPackage", defaultPackage);
                ba.keepeditor.commit();
            }
            editor.commit();
        }

        int firstColor = Color.parseColor("#0097A7");
        int secondColor = Color.parseColor("#FFA000");
        int thirdColor = Color.parseColor("#FF5722");


        int customSlideColor = Color.parseColor("#642EFE");

        //Create pre-created fragments
        MaterialSlide firstSlide = MaterialSlide.newInstance(R.drawable.slaytphone, getResources().getString(R.string.firsttouch),
                getResources().getString(R.string.firstDesc), Color.WHITE, Color.WHITE);

        MaterialSlide secondSlide = MaterialSlide.newInstance(R.drawable.cybr, getResources().getString(R.string.multiformat),
                getResources().getString(R.string.multiDesc), Color.WHITE, Color.WHITE);

        MaterialSlide thirdSlide = MaterialSlide.newInstance(R.drawable.html, getResources().getString(R.string.innovator),
                getResources().getString(R.string.innovaDesc), Color.WHITE, Color.WHITE);
        MaterialSlide fourthSlide = MaterialSlide.newInstance(R.drawable.permission, getResources().getString(R.string.fourthTitle),
                getResources().getString(R.string.fourthSlide), Color.WHITE, Color.WHITE);


        //Add slides
        addSlide(firstSlide, firstColor);
        addSlide(secondSlide, secondColor);
        addSlide(thirdSlide, thirdColor);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT)
        {
            addSlide(fourthSlide, getResources().getColor(R.color.purple));
        }

        //Custom slide
        addSlide(new CustomSlide(), customSlideColor);

        //Customize tour
        setSkipButtonTextColor(Color.WHITE);
        setNextButtonColorToWhite();
        setDoneButtonTextColor(Color.WHITE);
    }

    @Override
    public void onSkipPressed() {
        setCurrentSlide(3);

    }

    @Override
    public void onDonePressed() {

        Intent intent = new Intent(this, BackupActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onResume() {
        super.onResume();
        String name = "Uygulama Turu";


        // [END screen_view_hit]
    }
}