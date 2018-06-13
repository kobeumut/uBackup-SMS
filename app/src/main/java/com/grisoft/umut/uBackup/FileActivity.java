/*
package com.arneca.umut.observeractivity;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

*/
/**
 * Created by Umut on 13.02.2016.
 *//*



public class FileActivity extends ListActivity {

    String str;
    ArrayList<String> al;
    ArrayAdapter<String> adapter;
    ListView lv;

    @SuppressLint("SdCardPath")
    protected FileActivity() {
        Intent int1 = getIntent();
        ArrayList<String> arr1 = GetFiles(Environment.getExternalStorageDirectory().getPath());
        adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_expandable_list_item_1, arr1);
        lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(adapter);
    }

    private ArrayList<String> GetFiles(String path) {
        ArrayList<String> arr2 = new ArrayList<String>();
        File file = new File(path);
        File[] allfiles = file.listFiles();
        if (allfiles.length == 0) {
            return null;
        } else {
            for (int i = 0; i < allfiles.length; i++) {
                arr2.add(allfiles[i].getName());
            }
        }

        return arr2;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        Log.e("tıkla", String.valueOf(position));
        super.onListItemClick(l, v, position, id);
    }
}
*/
