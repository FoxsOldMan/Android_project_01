package com.duanlian.fragmenthideandshowdemo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private Context mContext;

    public static final String CREATE_APP = "create table APP ("
            + "id integer primary key autoincrement, "
            + "appname text, "
            + "mydate integer, "//日期
            + "download real, "
            + "upload real, "
            + "SEND_SMS integer, "//发送短信
            + "READ_CONTACTS integer, "//访问联系人通讯录信息
            + "READ_CALL_LOG integer, "//读取通话记录
            + "ACCESS_FINE_LOCATION integer, "//获取精确位置
            + "READ_PHONE_STATE integer, "//获得IMEI号码
            + "ROOT integer, "//ROOT
            + "READ_PRECISE_PHONE_STATE integer, "//READ_PRECISE_PHONE_STATE 监听来电状态
            + "READ_SMS integer) ";//读取短消息内容

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_APP);
        Toast.makeText(mContext,"Create succeeded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists APP");
        onCreate(db);
    }
}
