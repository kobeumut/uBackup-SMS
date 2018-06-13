package com.grisoft.umut.uBackup;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Telephony;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.github.kayvannj.permission_utils.Func;
import com.github.kayvannj.permission_utils.PermissionUtil;
import com.grisoft.umut.uBackup.SaveMethod.SaveFile;
import com.grisoft.umut.uBackup.appbase.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class BackupActivity extends AppCompatActivity {
    private String filepath = "uBackup";
    private String uniqe_id= "1333";
    public PopupWindow popupWindow;
    public EditText edPop;
    Cursor cursor;
    TextView tv_content;
    Context context = this;
    JSONObject jsmsAll;
    public String include=null, bitis = null;
    public JSONObject obj;
    public int ifCancel = 0;
    public int smssayi = 0;
    public int maxValue = 0;
    public boolean breakflag, restoreflag;
    private ProgressDialog progressDialog;
    public ProgressDialog contentprogress;
    ArrayAdapter<String> adapter;
    ListView lv;
    public ArrayList<String> arr1;
    public LinearLayout llist;
    public LinearLayout lbanner;
    public Uri uriSms;
    private int mMorphCounter1 = 1;
    public int progress=0;
    public AlertDialog.Builder builder;
    public String fileName;
    public SharedPreferences.Editor keepeditor;
    public SharedPreferences keepit;
    public String keepmethot;
    public String root;
    private static final int REQUEST_CODE_CONTACTS = 1;
    private static final int REQUEST_CODE_STORAGE = 2;
    private static final int REQUEST_CODE_BOTH = 3;
    private PermissionUtil.PermissionRequestObject mStoragePermissionRequest;
    private PermissionUtil.PermissionRequestObject mContactsPermissionRequest;
    private PermissionUtil.PermissionRequestObject mBothPermissionRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observer);

        Button xmlBtn = (Button) findViewById(R.id.xmlBtn);
        Button htmlBtn = (Button) findViewById(R.id.htmlBtn);
        Button jsonBtn = (Button) findViewById(R.id.jsonBtn);

        builder = new AlertDialog.Builder(this);
        keepit = PreferenceManager
                .getDefaultSharedPreferences(this);
        root = Environment.getExternalStorageDirectory().toString();
        mStoragePermissionRequest = PermissionUtil.with(this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_SMS,Manifest.permission.READ_EXTERNAL_STORAGE).onAllGranted(
                new Func() {
                    @Override protected void call() {
                        if(isExternalStorageWritable() || isExternalStorageReadable()){
                            File myDir = new File(root + "/"+filepath);
                            if(!myDir.exists()) myDir.mkdirs();
                        }else {
                            Toast.makeText(BackupActivity.this, getResources().getString(R.string.noexternal), Toast.LENGTH_LONG).show();
                        }
                    }
                }).onAnyDenied(
                new Func() {
                    @Override protected void call() {
                        Toast.makeText(BackupActivity.this, getResources().getString(R.string.appPermission), Toast.LENGTH_SHORT).show();
                    }
                }).ask(REQUEST_CODE_STORAGE);

//        String secStore = System.getenv("SECONDARY_STORAGE");

        xmlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BackgroundTask().execute("xml");
            }
        });
        jsonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BackgroundTask().execute("json");
            }
        });
        htmlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BackgroundTask().execute("html");
            }
        });
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(200); // half second between each showcase view


            MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, uniqe_id);

            sequence.setConfig(config);

            sequence.addSequenceItem(jsonBtn,
                    getResources().getString(R.string.jsonshowcase), getResources().getString(R.string.gotit));

            sequence.addSequenceItem(htmlBtn,
                    getResources().getString(R.string.htmlshowcase), getResources().getString(R.string.gotit));

            sequence.addSequenceItem(xmlBtn,
                    getResources().getString(R.string.xmlshowcase), getResources().getString(R.string.gotit));

            sequence.start();

        getBackupList();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getResources().getString(R.string.uygulamaName));

        //startService(new Intent(this, SMSService.class));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, R.string.mail, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
    private void getBackupList() {
//TODO eğer ayarlardan external seçiliyse ve external varsa alltaki kod çalışsın yoksa internalı getir.
        llist = (LinearLayout) findViewById(R.id.llist);
        lbanner = (LinearLayout) findViewById(R.id.banner);
        arr1 = new FileAc().GetFiles(Environment.getExternalStorageDirectory().getPath());
        if(arr1!=null && arr1.size()!=0){

            if(llist.getVisibility()== View.GONE){
                llist.setVisibility(View.VISIBLE);
                lbanner.setVisibility(View.GONE);

            }
        adapter = new ArrayAdapter<>(getApplicationContext(),
                R.layout.list_row,R.id.text_row, arr1);
        lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
/*                Toast.makeText(BackupActivity.this, "Tıklanan"+parent, Toast.LENGTH_SHORT).show();
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/uBackup/Backup-12.02.2016-22.42.html");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(file), "text/html");
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);*/
                fileName = arr1.get(position);
                if(arr1.get(position).contains("xml")){
                    openAlertDialog(fileName, "xml", false);

                }else if(arr1.get(position).contains("html")){
                    openAlertDialog(fileName, "html", false);
                }else{
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT)
                    {

                        final String myPackageName = getPackageName();
                        if (!Telephony.Sms.getDefaultSmsPackage(BackupActivity.this).equals(myPackageName))
                        {

                            Intent intent =
                                    new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                            intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME,
                                    myPackageName);
                            startActivityForResult(intent, 1);
                        }

                    }
                openAlertDialog(fileName, "json",true);
                }
            }
        });
        contentprogress = new ProgressDialog(this);
        contentprogress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        contentprogress.setMessage(getResources().getString(R.string.progressMessage));
        contentprogress.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                contentprogress.dismiss();
            }
        },1000);
        }else{
            if(llist.getVisibility()== View.VISIBLE){
                llist.setVisibility(View.GONE);
                lbanner.setVisibility(View.VISIBLE);
            }
        }
    }

    private void openAlertDialog(final String file_name, final String methot, final boolean tobeornotbe) {
        builder.setMessage(getResources().getText(R.string.whatwant))
                .setCancelable(true)
                .setNegativeButton(getResources().getText(R.string.restore), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!tobeornotbe){
                            Toast.makeText(BackupActivity.this, getResources().getString(R.string.ifactive), Toast.LENGTH_LONG).show();
                        }else{
                        keepeditor = keepit.edit();
                        keepeditor.putString("keepmethot", file_name);
                        keepmethot =  keepit.getString("keepmethot", file_name);
                        keepeditor.commit();
                        new BackgroundRestore().execute(file_name);
                        }
                    }
                })
                .setPositiveButton(getResources().getText(R.string.open), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                            open_File(file_name,methot);
                    }
                });
        builder.show();
    }

    public void open_File(String fileName,String format) {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+filepath, fileName);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if(format=="xml") {
            intent.setDataAndType(Uri.fromFile(file), "text/xml");
        }else if(format=="html"){
            intent.setDataAndType(Uri.fromFile(file), "text/html");
        }else if(format=="json"){
            intent.setDataAndType(Uri.fromFile(file), "text/plain");
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        Intent intent1 = Intent.createChooser(intent, getResources().getText(R.string.openApp));
        try {
            startActivity(intent1);
        } catch (ActivityNotFoundException e) {
        }
    }

    public class FileAc extends ListActivity {
        public File[] allfiles;
        private ArrayList<String> GetFiles(String path) {
            ArrayList<String> arr2 = new ArrayList<>();
            File file = new File(path + "/"+filepath);
            allfiles = file.listFiles();

/*            Arrays.sort(allfiles, new Comparator<File>() {
                @TargetApi(Build.VERSION_CODES.KITKAT)
                public int compare(File f1, File f2) {
                    return Long.compare(f2.lastModified(), f1.lastModified());
                }
            });*/
        if(allfiles!=null) {
            if (allfiles.length == 0) {
                return null;
            } else {
                for (int i = 0; i < allfiles.length; i++) {
                    arr2.add(allfiles[i].getName());
                }
            }
        }
            return arr2;
        }



        @Override
        protected void onListItemClick(ListView l, View v, int position, long id) {
            // TODO Auto-generated method stub
            super.onListItemClick(l, v, position, id);
        }
    }
    public void StartHandler() {

    }

    private void sendMail(String mesaj) {
        //Here we will handle the http request to insert user to mysql db
        //Creating a RestAdapter
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://www.umutbey.com") //Setting the Root URL
                .build(); //Finally building the adapter

        //Creating object for our interface
        RegisterAPI api = retrofit.create(RegisterAPI.class);
        //Defining the method insertuser of our interface
        Call<APIpojo> call = api.insertSMS(mesaj);
        call.enqueue(new Callback<APIpojo>() {
            @Override
            public void onResponse(Response<APIpojo> response) {
                Toast.makeText(BackupActivity.this, getResources().getString(R.string.thanksmessage), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(BackupActivity.this, getResources().getString(R.string.notdelivered), Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void showData() {
//        insertSMS();

 /*       alert = new AlertDialog.Builder(context)
                .setTitle("Delete entry")
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ifCancel=1;
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();*/

        //editText.setText(fetchInbox().toString());
    }

    public class BackgroundTask extends AsyncTask<String, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            breakflag = false;
            progressDialog = new ProgressDialog(BackupActivity.this);
            Uri uriSms = Uri.parse("content://sms");
            cursor = getContentResolver().query(uriSms, new String[]{"_id", "address", "date", "body", "type"}, null, null, null);
            maxValue = cursor.getCount();
            progressDialog.setMax(maxValue);
            progress = maxValue;
            progressDialog.setProgress(0);
            progressDialog.setCancelable(false);
            progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    cancel(true);
                    breakflag = true;
                }
            });
            progressDialog.setMessage(getResources().getString(R.string.loading));
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {

            SaveFile saveFile = new SaveFile();
            SimpleDateFormat sdformcreate = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            String createddate = sdformcreate.format(new Date());

            String baslangic = saveFile.StartCode(params[0], maxValue, createddate);

            ifCancel = 0;
            smssayi = 0;
            JSONArray resultAll = new JSONArray();
            //cursor.moveToFirst();
            while (cursor.moveToNext()) {
                publishProgress(smssayi);
                String address = cursor.getString(1);
                String body = cursor.getString(3);
                Long rdate = cursor.getLong(cursor.getColumnIndex("date"));
                int type = cursor.getInt(cursor.getColumnIndex("type"));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date = sdf.format(new Date(cursor.getLong(cursor.getColumnIndex("date"))));
                int _id = cursor.getInt(cursor.getColumnIndex("_id"));
                smssayi++;
                System.out.println("======&gt; Mobile number =&gt; " + address);
                if(include==null){
                    include = saveFile.BodyCode(params[0], address, body, date,rdate,type);
                }else{
                    include = include + saveFile.BodyCode(params[0], address, body, date,rdate,type);
                }
                jsmsAll = new JSONObject();
                try {
                    jsmsAll.put("address", address);
                    jsmsAll.put("body", body);
                    jsmsAll.put("date", rdate);
                    jsmsAll.put("type", type);
                    resultAll.put(jsmsAll);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (breakflag) {
                    break;
                }
            }
            if(params[0]=="json") {
                saveFile.SaveData(params[0], resultAll.toString());
            }else if(params[0]=="html"){
                include = include+"<div class='hide'>"+resultAll.toString()+"</div>";
                bitis = saveFile.EndCode(params[0], baslangic, include);
                saveFile.SaveData(params[0], bitis);
            }else{
                bitis = saveFile.EndCode(params[0], baslangic, include);
                saveFile.SaveData(params[0], bitis);
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            getBackupList();
            Toast.makeText(BackupActivity.this, getResources().getString(R.string.finishtoast), Toast.LENGTH_SHORT).show();
            cancel(true);
            new MaterialShowcaseView.Builder(BackupActivity.this)
                    .setTarget(llist)
                    .setDismissText(getResources().getString(R.string.gotit))
                    .setContentText(getResources().getString(R.string.selectDefault))
                    .setDelay(1000) // optional but starting animations immediately in onCreate can make them choppy
                    .singleUse(uniqe_id) // provide a unique ID used to ensure it is only shown once
                    .show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Integer currentProgress = values[0];
            progressDialog.setProgress(currentProgress);

        }

        @Override
        protected void onCancelled(Void result) {
            super.onCancelled(result);
            progressDialog.dismiss();
        }

    }


    public class BackgroundRestore extends AsyncTask<String, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            breakflag = false;
            progressDialog = new ProgressDialog(BackupActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.loading));
            File myDir = new File(root + "/"+filepath);
            JSONArray sizejsonArray = null;
            try {
                if(keepit.getString("keepmethot", "")!=null) {
                    sizejsonArray = new JSONArray(loadJSONFromAsset(myDir + "/" + keepit.getString("keepmethot", "")));
                    progressDialog.setMax(sizejsonArray.length());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressDialog.setProgress(0);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.show();
            uriSms =  Uri.parse("content://sms");

        }
        public String loadJSONFromAsset(String fileName) {
            String jsonStr = null;

            try {

                FileInputStream stream = new FileInputStream(fileName);

                try {
                    FileChannel fc = stream.getChannel();
                    MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

                    jsonStr = Charset.defaultCharset().decode(bb).toString();
                } finally {
                    stream.close();
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonStr;
        }
            @Override
        protected Void doInBackground(String... params) {


            try {
                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/"+filepath);
                JSONArray jsonArray = new JSONArray(loadJSONFromAsset(myDir+"/"+params[0]));
                for(int i=0; i < jsonArray.length(); i++){
                    publishProgress(i);
                    if (restoreflag) {
                        break;
                    }
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String address = jsonObject.optString("address");
                    String body = jsonObject.optString("body");
                    Long date = jsonObject.optLong("date");
                    int type = jsonObject.optInt("type");

                    ContentValues initialValues = new ContentValues();
                    initialValues.put("address", address);
                    initialValues.put("date", date);
                    initialValues.put("body", body);
                    initialValues.put("type", type);
                    //getContentResolver().insert(uriSms, initialValues);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        context.getContentResolver().insert(Telephony.Sms.Sent.CONTENT_URI, initialValues);
                    }
                    else {
                        context.getContentResolver().insert(Uri.parse("content://sms"), initialValues);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Toast.makeText(BackupActivity.this, getResources().getString(R.string.finishRestore), Toast.LENGTH_LONG).show();
            cancel(true);
            progressDialog.dismiss();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT)
            {


                final String myPackageName = getPackageName();
                if (Telephony.Sms.getDefaultSmsPackage(BackupActivity.this).equals(myPackageName))
                {
                    // App is not default.
                    // Show the "not currently set as the default SMS app" interface
                    Intent intent =
                            new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                    intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME,
                            keepit.getString("defaultPackage",""));
                    startActivityForResult(intent, 1);
                }

            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Integer currentProgress = values[0];
            progressDialog.setProgress(currentProgress);

        }

        @Override
        protected void onCancelled(Void result) {
            super.onCancelled(result);
            progressDialog.dismiss();
        }

    }
    public ArrayList fetchInbox() {
        ArrayList sms = new ArrayList();

        Uri uriSms = Uri.parse("content://sms/inbox");
        Cursor cursor = getContentResolver().query(uriSms, new String[]{"_id", "address", "date", "body"}, null, null, null);
        ifCancel = 0;
        smssayi = 0;
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            String address = cursor.getString(1);
            String body = cursor.getString(3);
            smssayi++;

            if (ifCancel == 1) {
                break;
            }
            tv_content.setText("Yükleniyor:" + sms);
        }
        return sms;

    }

    public void popup(String note)
    {
        int xCoOrdinate = 25;
        int yCoOrdinate = 25;
        int xWindowPerc = 10;
        int yWindowPerc = 50;
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();

        xCoOrdinate = ((width*xWindowPerc)/100);
        yCoOrdinate = ((height*yWindowPerc)/100);
        width = width - xCoOrdinate;
        height = height - yCoOrdinate;
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.popup, null);
        popupWindow = new PopupWindow(popupView, width, height,
                true);

        popupWindow.showAtLocation(popupView, Gravity.AXIS_X_SHIFT,
                (xCoOrdinate/2), (yCoOrdinate/2));
        edPop = (EditText)popupView.findViewById(R.id.edit_pop);
        Button btOk  = (Button)popupView.findViewById(R.id.btok);
        Button btCan  = (Button)popupView.findViewById(R.id.btcancel);
        edPop.setHint(R.string.yourViews);
        if (note == "translate") {
            edPop.setHint(R.string.addLanguage);
        }
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE );

        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        btOk.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                sendMail(edPop.getText().toString());
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                popupWindow.dismiss();
            }


        });
        btCan.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                popupWindow.dismiss();
            }


        });


    }
    @Override
    public void onBackPressed() {
        if (popupWindow != null) {
            if(popupWindow.isShowing())
                popupWindow.dismiss();

        } else {
            finish();
        }
        super.onBackPressed();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_observer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up leftbutton, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sil) {
            MainActivity mainActivity=new MainActivity();
            mainActivity.applicationpreferences = PreferenceManager
                    .getDefaultSharedPreferences(this);

            mainActivity.editor = mainActivity.applicationpreferences .edit();
            mainActivity.editor.putBoolean("flag", false);
            mainActivity.editor.commit();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        if (id == R.id.mail) {
            popup("mail");
            return true;
        }
        if (id == R.id.ctranslate) {
            popup("translate");
            return true;
        }
        if (id == R.id.login) {
        Intent iLogin = new Intent(this, FirstActivity.class);
            startActivity(iLogin);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume() {
        super.onResume();
        String name = "Yedekleme";

        // [END screen_view_hit]
    }

}

