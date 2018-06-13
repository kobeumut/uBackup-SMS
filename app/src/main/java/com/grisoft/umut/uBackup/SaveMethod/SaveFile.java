package com.grisoft.umut.uBackup.SaveMethod;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Umut on 12.02.2016.
 */
public class SaveFile {
    public String Code;
    public String StartCode(String Method, Integer SMSCount, String date) {
        switch (Method){
            case "xml":
                Code = "<?xml version='1.0' encoding='UTF-8' standalone='yes' ?>\n" +
                        "<!--File Created By UmutBey v1 on "+date+"-->\n" +
                        "<?xml-stylesheet type=\"text/xsl\" href=\"sms.xsl\"?>\n" +
                        "<smses count="+SMSCount+">\n";
                break;
            case "xls":

                break;

            case "csv":

                break;
            case "html":
                Code = "<!DOCTYPE html>\n" + //TODO gönderme alma seçeneğini de belirt
                        "<html>\n" +
                        "<head>\n" +
                        "<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">"+
                        "<meta name=\"author\" content=\"Umut ADALI\">"+
                        "<style>\n" +
                        ".hide {display: none;}"+
                        "table {\n" +
                        "    width:100%;\n" +
                        "}\n" +
                        "table tr:nth-child(even) {\n" +
                        "    background-color: #eee;\n" +
                        "}\n" +
                        "table tr:nth-child(odd) {\n" +
                        "   background-color:#fff;\n" +
                        "}\n" +
                        "table th\t{\n" +
                        "    background-color: black;\n" +
                        "    color: white;\n" +
                        "}\n" +
                        "</style>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "\n" +
                        "SMS Count: "+SMSCount+" File Created By UmutBey v1 on "+date+
                        "<table>\n" +
                        "  <tr>\n" +
                        "    <th>Address</th>\n" +
                        "    <th>Body</th>\t\t\n" +
                        "    <th>Type</th>\t\t\n" +
                        "    <th>Date</th>\n" +
                        "  </tr>\n";

                break;
        }
        return Code;
    }

    public String BodyCode(String Method, String address, String body, String date, Long rdate, int type){
        switch (Method){
            case "xml":
                Code= "<sms protocol=\"0\" address=\""+address+"\" body=\""+body+"\"  date=\""+rdate+"\" type=\""+type+"\" realdate=\""+date+"\" realdate subject=\"null\">"+"\n";
                break;
            case "xls":
                break;

            case "csv":

                break;
            case "html":
                Code =  "  <tr>\n"+
                        "    <td>"+address+"</td>\n" +
                        "    <td>"+body+"</td>\t\t\n" +
                        "    <td>"+type+"</td>\t\t\n" +
                        "    <td>"+date+"</td>\n"+
                        "</tr>\n";
                break;

        }
        return Code;
    }

    public String EndCode(String Method, String Baslangic, String Body){
        switch (Method){
            case "xml":
                String Son = "\n</smses>";
                Code=Baslangic+Body+Son;
                break;
            case "xls":

                break;
            case "json":

                break;
            case "csv":

                break;
            case "html":
                Son =   "</table>\n" +
                        "</body>\n" +
                        "</html>\n";
                Code=Baslangic+Body+Son;
                break;

        }
        return Code;
    }
    public void SaveData(String Methot, String params){

        //TODO eğer external varsa ve ayarlardan seçilmişse aşağıdakini göster yoksa internala kaydet
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/uBackup");
        myDir.mkdirs();
        SimpleDateFormat sdformcreate = new SimpleDateFormat("dd.MM.yyyy-HH.mm");
        String createddate = sdformcreate.format(new Date());
        String fname = "Backup-"+ createddate +"."+Methot;

        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);


                OutputStreamWriter outputWriter=new OutputStreamWriter(out);
                outputWriter.write(params);
                outputWriter.close();




        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
