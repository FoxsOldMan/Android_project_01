package com.duanlian.fragmenthideandshowdemo.bean;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;


/**
 * Created by dell1 on 2017/5/29.
 */
public class EchartsDataBean {

    private static Gson gson;
    private static EchartsLineBean lineBean;
    private static EchartsBarBean barBean;
    private static EchartsPieBean pieBean;
    private static EchartsDataBean echartsDataBean;


    private EchartsDataBean() {
    }

    public synchronized static EchartsDataBean getInstance() {
        if (echartsDataBean == null) {
            echartsDataBean = new EchartsDataBean();
            gson = new Gson();
            lineBean = new EchartsLineBean();
            barBean = new EchartsBarBean();
            pieBean = new EchartsPieBean();
        }
        return echartsDataBean;
    }



    public String getTodayEchartsLineJson() {
        SQLiteDatabase db;
        lineBean.title = "权限调用统计";
        lineBean.type = "line";
        lineBean.authority =
                new String[]{"发短信", "联系人", "读通话", "位置", "imei", "root", "听来电", "读短信"};
        lineBean.times =
                new int[]{0, 0, 0, 0, 0, 0, 0, 0};

        String DB_PATH = "data/data/" + "com.duanlian.fragmenthideandshowdemo" + "/databases/";
        String DB_NAME = "APPInfo.db";
        File dbFile = new File(DB_PATH+DB_NAME);
        if(dbFile.exists()){
            db = SQLiteDatabase.openOrCreateDatabase(dbFile,null);
            Log.d("EchartsDataBean","data catched");

            Calendar cal = Calendar.getInstance();
            cal.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
            int day = cal.get(Calendar.DATE);

            Cursor cursor = db.rawQuery("select * from APP where root is not null and mydate = ?", new String[]{String.valueOf(day)});
            if(cursor.moveToFirst()){
                do{
                    if(cursor.getInt(cursor.getColumnIndex("SEND_SMS")) != 0)
                        lineBean.times[0]++;
                    if(cursor.getInt(cursor.getColumnIndex("READ_CONTACTS")) != 0)
                        lineBean.times[1]++;
                    if(cursor.getInt(cursor.getColumnIndex("READ_CALL_LOG")) != 0)
                        lineBean.times[2]++;
                    if(cursor.getInt(cursor.getColumnIndex("ACCESS_FINE_LOCATION")) != 0)
                        lineBean.times[3]++;
                    if(cursor.getInt(cursor.getColumnIndex("READ_PHONE_STATE")) != 0)
                        lineBean.times[4]++;
                    if(cursor.getInt(cursor.getColumnIndex("ROOT")) != 0)
                        lineBean.times[5]++;
                    if(cursor.getInt(cursor.getColumnIndex("READ_PRECISE_PHONE_STATE")) != 0)
                        lineBean.times[6]++;
                    if(cursor.getInt(cursor.getColumnIndex("READ_SMS")) != 0)
                        lineBean.times[7]++;
                } while (cursor.moveToNext());
            }else{
                Log.d("EchartsDataBean","no data in db today");
            }
            Log.d("EchartsDataBean","data catched");
            cursor.close();
        }
        return gson.toJson(lineBean);
    }

    public String getYestodayEchartsLineJson(){
        SQLiteDatabase db;
        lineBean.title = "权限调用统计";
        lineBean.type = "line";
        lineBean.authority =
                new String[]{"发短信", "联系人", "读通话", "位置", "imei", "root", "听来电", "读短信"};
        lineBean.times =
                new int[]{0, 0, 0, 0, 0, 0, 0, 0};

        String DB_PATH = "data/data/" + "com.duanlian.fragmenthideandshowdemo" + "/databases/";
        String DB_NAME = "APPInfo.db";
        File dbFile = new File(DB_PATH+DB_NAME);
        if(dbFile.exists()){
            db = SQLiteDatabase.openOrCreateDatabase(dbFile,null);
            Log.d("EchartsDataBean","data catched");

            Calendar cal = Calendar.getInstance();
            cal.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
            int day = cal.get(Calendar.DATE);
            int month = cal.get(Calendar.MONTH)+1;
            int year = cal.get(Calendar.YEAR);
            if(day == 1){
                if(month == 4 || month == 6 || month == 9 || month == 11){
                    day = 30;
                }
                else if(month == 2){
                    if((year%4) == 0 && (year%100) != 0){
                        day = 29;
                    }else if((year%400) == 0){
                        day = 29;
                    }else{
                        day = 28;
                    }
                }else{
                    day =31;
                }
            }else {
                day -= 1;
            }
            Cursor cursor = db.rawQuery("select * from APP where root is not null and mydate = ?", new String[]{String.valueOf(day)});
            if(cursor.moveToFirst()){
                do{
                    if(cursor.getInt(cursor.getColumnIndex("SEND_SMS")) != 0)
                        lineBean.times[0]++;
                    if(cursor.getInt(cursor.getColumnIndex("READ_CONTACTS")) != 0)
                        lineBean.times[1]++;
                    if(cursor.getInt(cursor.getColumnIndex("READ_CALL_LOG")) != 0)
                        lineBean.times[2]++;
                    if(cursor.getInt(cursor.getColumnIndex("ACCESS_FINE_LOCATION")) != 0)
                        lineBean.times[3]++;
                    if(cursor.getInt(cursor.getColumnIndex("READ_PHONE_STATE")) != 0)
                        lineBean.times[4]++;
                    if(cursor.getInt(cursor.getColumnIndex("ROOT")) != 0)
                        lineBean.times[5]++;
                    if(cursor.getInt(cursor.getColumnIndex("READ_PRECISE_PHONE_STATE")) != 0)
                        lineBean.times[6]++;
                    if(cursor.getInt(cursor.getColumnIndex("READ_SMS")) != 0)
                        lineBean.times[7]++;
                } while (cursor.moveToNext());
            }else{
                Log.d("EchartsDataBean","no data in db Yestoday");
            }
            Log.d("EchartsDataBean","data catched");
            cursor.close();
        }
        return gson.toJson(lineBean);
    }

    public String getBeforeEchartsLineJson(){
        SQLiteDatabase db;
        lineBean.title = "权限调用统计";
        lineBean.type = "line";
        lineBean.authority =
                new String[]{"发短信", "联系人", "读通话", "位置", "imei", "root", "听来电", "读短信"};
        lineBean.times =
                new int[]{0, 0, 0, 0, 0, 0, 0, 0};

        String DB_PATH = "data/data/" + "com.duanlian.fragmenthideandshowdemo" + "/databases/";
        String DB_NAME = "APPInfo.db";
        File dbFile = new File(DB_PATH+DB_NAME);
        if(dbFile.exists()){
            db = SQLiteDatabase.openOrCreateDatabase(dbFile,null);
            Log.d("EchartsDataBean","data catched");

            Calendar cal = Calendar.getInstance();
            cal.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
            int day = cal.get(Calendar.DATE);
            int month = cal.get(Calendar.MONTH)+1;
            int year = cal.get(Calendar.YEAR);
            if(day == 2){
                if(month == 4 || month == 6 || month == 9 || month == 11){
                    day = 30;
                }
                else if(month == 2){
                    if((year%4) == 0 && (year%100) != 0){
                        day = 29;
                    }else if((year%400) == 0){
                        day = 29;
                    }else{
                        day = 28;
                    }
                }else{
                    day =31;
                }
            }else if(day == 1){
                if(month == 4 || month == 6 || month == 9 || month == 11){
                    day = 29;
                }
                else if(month == 2){
                    if((year%4) == 0 && (year%100) != 0){
                        day = 28;
                    }else if((year%400) == 0){
                        day = 28;
                    }else{
                        day = 27;
                    }
                }else{
                    day =30;
                }
            }else{
                day -= 2;
            }
            Cursor cursor = db.rawQuery("select * from APP where root is not null and mydate = ?", new String[]{String.valueOf(day)});
            if(cursor.moveToFirst()){
                do{
                    if(cursor.getInt(cursor.getColumnIndex("SEND_SMS")) != 0)
                        lineBean.times[0]++;
                    if(cursor.getInt(cursor.getColumnIndex("READ_CONTACTS")) != 0)
                        lineBean.times[1]++;
                    if(cursor.getInt(cursor.getColumnIndex("READ_CALL_LOG")) != 0)
                        lineBean.times[2]++;
                    if(cursor.getInt(cursor.getColumnIndex("ACCESS_FINE_LOCATION")) != 0)
                        lineBean.times[3]++;
                    if(cursor.getInt(cursor.getColumnIndex("READ_PHONE_STATE")) != 0)
                        lineBean.times[4]++;
                    if(cursor.getInt(cursor.getColumnIndex("ROOT")) != 0)
                        lineBean.times[5]++;
                    if(cursor.getInt(cursor.getColumnIndex("READ_PRECISE_PHONE_STATE")) != 0)
                        lineBean.times[6]++;
                    if(cursor.getInt(cursor.getColumnIndex("READ_SMS")) != 0)
                        lineBean.times[7]++;
                } while (cursor.moveToNext());
            }else{
                Log.d("EchartsDataBean","no data in db before Yestoday");
            }
            Log.d("EchartsDataBean","data catched");
            cursor.close();
        }

        return gson.toJson(lineBean);
    }

    public String getEchartsBarJson() {
        SQLiteDatabase db;
        barBean.title = "权限调用统计";
        barBean.type1 = "调用次数";
        barBean.times = new String[]{"发短信", "联系人", "读通话", "位置", "imei", "root", "听来电", "读短信"};
        barBean.data1 = new int[]{0, 0, 0, 0, 0, 0, 0, 0};

        String DB_PATH = "data/data/" + "com.duanlian.fragmenthideandshowdemo" + "/databases/";
        String DB_NAME = "APPInfo.db";
        File dbFile = new File(DB_PATH+DB_NAME);
        if(dbFile.exists()){
            db = SQLiteDatabase.openOrCreateDatabase(dbFile,null);
            Log.d("EchartsDataBean","data catched");

            Calendar cal = Calendar.getInstance();
            cal.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
            int day = cal.get(Calendar.DATE);

            Cursor cursor = db.rawQuery("select * from APP where root is not null and mydate = ?", new String[]{String.valueOf(day)});
            if(cursor.moveToFirst()){
                do{
                    if(cursor.getInt(cursor.getColumnIndex("SEND_SMS")) != 0)
                        barBean.data1[0]++;
                    if(cursor.getInt(cursor.getColumnIndex("READ_CONTACTS")) != 0)
                        barBean.data1[1]++;
                    if(cursor.getInt(cursor.getColumnIndex("READ_CALL_LOG")) != 0)
                        barBean.data1[2]++;
                    if(cursor.getInt(cursor.getColumnIndex("ACCESS_FINE_LOCATION")) != 0)
                        barBean.data1[3]++;
                    if(cursor.getInt(cursor.getColumnIndex("READ_PHONE_STATE")) != 0)
                        barBean.data1[4]++;
                    if(cursor.getInt(cursor.getColumnIndex("ROOT")) != 0)
                        barBean.data1[5]++;
                    if(cursor.getInt(cursor.getColumnIndex("READ_PRECISE_PHONE_STATE")) != 0)
                        barBean.data1[6]++;
                    if(cursor.getInt(cursor.getColumnIndex("READ_SMS")) != 0)
                        barBean.data1[7]++;
                } while (cursor.moveToNext());
            }else{
                Log.d("EchartsDataBean","no data in db today");
            }
            Log.d("EchartsDataBean","data catched");
            cursor.close();
        }
        return gson.toJson(barBean);
    }

    public String getYestodayEchartsBarJson(){
        SQLiteDatabase db;
        barBean.title = "权限调用统计";
        barBean.type1 = "调用次数";
        barBean.times = new String[]{"发短信", "联系人", "读通话", "位置", "imei", "root", "听来电", "读短信"};
        barBean.data1 = new int[]{0, 0, 0, 0, 0, 0, 0, 0};

        String DB_PATH = "data/data/" + "com.duanlian.fragmenthideandshowdemo" + "/databases/";
        String DB_NAME = "APPInfo.db";
        File dbFile = new File(DB_PATH+DB_NAME);
        if(dbFile.exists()){
            db = SQLiteDatabase.openOrCreateDatabase(dbFile,null);
            Log.d("EchartsDataBean","data catched");

            Calendar cal = Calendar.getInstance();
            cal.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
            int day = cal.get(Calendar.DATE);
            int month = cal.get(Calendar.MONTH)+1;
            int year = cal.get(Calendar.YEAR);
            if(day == 1){
                if(month == 4 || month == 6 || month == 9 || month == 11){
                    day = 30;
                }
                else if(month == 2){
                    if((year%4) == 0 && (year%100) != 0){
                        day = 29;
                    }else if((year%400) == 0){
                        day = 29;
                    }else{
                        day = 28;
                    }
                }else{
                    day =31;
                }
            }else{
                day -= 1;
            }
            Cursor cursor = db.rawQuery("select * from APP where root is not null and mydate = ?", new String[]{String.valueOf(day)});
            if(cursor.moveToFirst()){
                do{
                    if(cursor.getInt(cursor.getColumnIndex("SEND_SMS")) != 0)
                        barBean.data1[0]++;
                    if(cursor.getInt(cursor.getColumnIndex("READ_CONTACTS")) != 0)
                        barBean.data1[1]++;
                    if(cursor.getInt(cursor.getColumnIndex("READ_CALL_LOG")) != 0)
                        barBean.data1[2]++;
                    if(cursor.getInt(cursor.getColumnIndex("ACCESS_FINE_LOCATION")) != 0)
                        barBean.data1[3]++;
                    if(cursor.getInt(cursor.getColumnIndex("READ_PHONE_STATE")) != 0)
                        barBean.data1[4]++;
                    if(cursor.getInt(cursor.getColumnIndex("ROOT")) != 0)
                        barBean.data1[5]++;
                    if(cursor.getInt(cursor.getColumnIndex("READ_PRECISE_PHONE_STATE")) != 0)
                        barBean.data1[6]++;
                    if(cursor.getInt(cursor.getColumnIndex("READ_SMS")) != 0)
                        barBean.data1[7]++;
                } while (cursor.moveToNext());
            }else{
                barBean.data1 = new int[]{4, 8, 7, 3, 5, 0, 1, 2};
                Log.d("EchartsDataBean","no data in db today");
            }
            Log.d("EchartsDataBean","data catched");
            cursor.close();
        }
        return gson.toJson(barBean);
    }

    public String getBeforeEchartsBarJson(){
        SQLiteDatabase db;
        barBean.title = "权限调用统计";
        barBean.type1 = "调用次数";
        barBean.times = new String[]{"发短信", "联系人", "读通话", "位置", "imei", "root", "听来电", "读短信"};
        barBean.data1 = new int[]{0, 0, 0, 0, 0, 0, 0, 0};

        String DB_PATH = "data/data/" + "com.duanlian.fragmenthideandshowdemo" + "/databases/";
        String DB_NAME = "APPInfo.db";
        File dbFile = new File(DB_PATH+DB_NAME);
        if(dbFile.exists()){
            db = SQLiteDatabase.openOrCreateDatabase(dbFile,null);
            Log.d("EchartsDataBean","data catched");

            Calendar cal = Calendar.getInstance();
            cal.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
            int day = cal.get(Calendar.DATE);
            int month = cal.get(Calendar.MONTH)+1;
            int year = cal.get(Calendar.YEAR);
            if(day == 2){
                if(month == 4 || month == 6 || month == 9 || month == 11){
                    day = 30;
                }
                else if(month == 2){
                    if((year%4) == 0 && (year%100) != 0){
                        day = 29;
                    }else if((year%400) == 0){
                        day = 29;
                    }else{
                        day = 28;
                    }
                }else{
                    day =31;
                }
            }else if(day == 1){
                if(month == 4 || month == 6 || month == 9 || month == 11){
                    day = 29;
                }
                else if(month == 2){
                    if((year%4) == 0 && (year%100) != 0){
                        day = 28;
                    }else if((year%400) == 0){
                        day = 28;
                    }else{
                        day = 27;
                    }
                }else{
                    day =30;
                }
            }else{
                day -= 2;
            }
            Cursor cursor = db.rawQuery("select * from APP where root is not null and mydate = ?", new String[]{String.valueOf(day)});
            if(cursor.moveToFirst()){
                do{
                    if(cursor.getInt(cursor.getColumnIndex("SEND_SMS")) != 0)
                        barBean.data1[0]++;
                    if(cursor.getInt(cursor.getColumnIndex("READ_CONTACTS")) != 0)
                        barBean.data1[1]++;
                    if(cursor.getInt(cursor.getColumnIndex("READ_CALL_LOG")) != 0)
                        barBean.data1[2]++;
                    if(cursor.getInt(cursor.getColumnIndex("ACCESS_FINE_LOCATION")) != 0)
                        barBean.data1[3]++;
                    if(cursor.getInt(cursor.getColumnIndex("READ_PHONE_STATE")) != 0)
                        barBean.data1[4]++;
                    if(cursor.getInt(cursor.getColumnIndex("ROOT")) != 0)
                        barBean.data1[5]++;
                    if(cursor.getInt(cursor.getColumnIndex("READ_PRECISE_PHONE_STATE")) != 0)
                        barBean.data1[6]++;
                    if(cursor.getInt(cursor.getColumnIndex("READ_SMS")) != 0)
                        barBean.data1[7]++;
                } while (cursor.moveToNext());
            }else{
                barBean.data1 = new int[]{6, 8, 8, 0, 5, 0, 3, 2};
                Log.d("EchartsDataBean","no data in db today");
            }
            Log.d("EchartsDataBean","data catched");
            cursor.close();
        }
        return gson.toJson(barBean);
    }

    public String getEchartsPieJson() {
        pieBean.title = "支付宝用户访问来源";
        pieBean.type = "pie";
        pieBean.names = new String[]{"直接访问", "邮件营销", "联盟广告", "视频广告", "搜索引擎"};
        pieBean.values = new ArrayList<>();
        EchartsPieBean.Value value;
        for (int i = 0; i < pieBean.names.length; i++) {
            value = new EchartsPieBean.Value();
            value.name = pieBean.names[i];
            value.value = 20 * i;
            pieBean.values.add(value);
        }
        return gson.toJson(pieBean);
    }
}
