package com.duanlian.fragmenthideandshowdemo.fragment;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.TrafficStats;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.duanlian.fragmenthideandshowdemo.MainActivity;
import com.duanlian.fragmenthideandshowdemo.MyDatabaseHelper;
import com.duanlian.fragmenthideandshowdemo.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class AppAnalyzeFragment extends Fragment {
    private final String TAG = AppAnalyzeFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private MainAdapter mainAdapter = null;

    private List<Analyze_item> listresult = new ArrayList<Analyze_item>();

    private MainActivity mainactivity;
    private SQLiteDatabase db;

    @Override
    public void onAttach(Context context) {
        while(!listresult.isEmpty()){
            listresult.remove(0);
        }
        super.onAttach(context);
        mainactivity = (MainActivity) getActivity();

        String DB_PATH = "data/data/" + context.getApplicationContext().getPackageName() + "/databases/";
        String DB_NAME = "APPInfo.db";
        File dbFile = new File(DB_PATH+DB_NAME);
        if(dbFile.exists()){
            db = SQLiteDatabase.openOrCreateDatabase(dbFile,null);
        }

        Cursor cursor = db.rawQuery("select * from APP where download is not null and root is not null", null);
        if(cursor.moveToFirst()){
            do{
                Analyze_item item = new Analyze_item();
                item.setAppName(cursor.getString(cursor.getColumnIndex("appname")));
                item.setDownload(cursor.getFloat(cursor.getColumnIndex("download")));
                item.setUpload(cursor.getFloat(cursor.getColumnIndex("upload")));
                item.setSEND_SMS(cursor.getInt(cursor.getColumnIndex("SEND_SMS")));
                item.setREAD_CONTACTS(cursor.getInt(cursor.getColumnIndex("READ_CONTACTS")));
                item.setREAD_CALL_LOG(cursor.getInt(cursor.getColumnIndex("READ_CALL_LOG")));
                item.setACCESS_FINE_LOCATION(cursor.getInt(cursor.getColumnIndex("ACCESS_FINE_LOCATION")));
                item.setREAD_PHONE_STATE(cursor.getInt(cursor.getColumnIndex("READ_PHONE_STATE")));
                item.setROOT(cursor.getInt(cursor.getColumnIndex("ROOT")));
                item.setREAD_PRECISE_PHONE_STATE(cursor.getInt(cursor.getColumnIndex("READ_PRECISE_PHONE_STATE")));
                item.setREAD_SMS(cursor.getInt(cursor.getColumnIndex("READ_SMS")));
                if(item.getUpload() > 0){
                    item.setRisk(0.1632*item.getSEND_SMS() + 0.0799*item.getREAD_CONTACTS() + 0.0610*item.getREAD_CALL_LOG() + 0.0348*item.getACCESS_FINE_LOCATION()
                            + 0.0248*item.getREAD_PHONE_STATE() + 0.3577*item.getROOT() + 0.0620*item.getREAD_PRECISE_PHONE_STATE() + 0.2166*item.getREAD_SMS());
                }else{
                    item.setRisk(0);
                }

                listresult.add(item);
            } while (cursor.moveToNext());
            Toast.makeText(mainactivity,"amount:"+listresult.size(), Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(mainactivity,"no data in DB", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainactivity = null;
        db.close();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mainactivity == null) {
            Log.d(TAG, "onCreateView getActivity is null");
        }

        View view = inflater.inflate(R.layout.dataanalyze, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.app_analyze);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(mainactivity));

        mainAdapter = new MainAdapter();
        recyclerView.setAdapter(mainAdapter);

        mainAdapter.notifyDataSetChanged();
        return view;
    }


    class MainAdapter extends RecyclerView.Adapter{
        PackageManager pm = mainactivity.getPackageManager();

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mainactivity).inflate(R.layout.dataanalyze_item, parent, false);
            return new MainHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Analyze_item analyze_item = listresult.get(position);
            AppAnalyzeFragment.MainAdapter.MainHolder mainHolder = (AppAnalyzeFragment.MainAdapter.MainHolder) holder;

            List<PackageInfo> packinfos = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES | PackageManager.GET_PERMISSIONS);
            for (PackageInfo info : packinfos) {
                if(analyze_item.getAppName().equals((String) pm.getApplicationLabel(info.applicationInfo))){
                    mainHolder.appImage.setImageDrawable(info.applicationInfo.loadIcon(pm));
                    mainHolder.appName.setText("名称："+ analyze_item.getAppName());
                    mainHolder.analyze_Result.setText("危险度："+analyze_item.getRisk());
                    return;
                }
            }
            Toast.makeText(mainactivity,"APP名称不匹配", Toast.LENGTH_SHORT).show();
        }

        @Override
        public int getItemCount() {
            return listresult.size();
        }

        class MainHolder extends RecyclerView.ViewHolder {

            ImageView appImage;
            TextView appName;
            TextView analyze_Result;

            public MainHolder(View itemView) {
                super(itemView);
                appImage = (ImageView) itemView.findViewById(R.id.app_analyze_image);
                appName = (TextView) itemView.findViewById(R.id.app_analyze_name);
                analyze_Result = (TextView) itemView.findViewById(R.id.app_analyze_result);
            }
        }
    }


    class Analyze_item{
        String appName;
        float Download;
        float Upload;
        int SEND_SMS, READ_CONTACTS, READ_CALL_LOG, ACCESS_FINE_LOCATION, READ_PHONE_STATE, ROOT, READ_PRECISE_PHONE_STATE, READ_SMS;
        double risk;

        public double getRisk() {
            return risk;
        }

        public void setRisk(double risk) {
            this.risk = risk;
        }

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public float getDownload() {
            return Download;
        }

        public void setDownload(float download) {
            Download = download;
        }

        public float getUpload() {
            return Upload;
        }

        public void setUpload(float upload) {
            Upload = upload;
        }

        public int getSEND_SMS() {
            return SEND_SMS;
        }

        public void setSEND_SMS(int SEND_SMS) {
            this.SEND_SMS = SEND_SMS;
        }

        public int getREAD_CONTACTS() {
            return READ_CONTACTS;
        }

        public void setREAD_CONTACTS(int READ_CONTACTS) {
            this.READ_CONTACTS = READ_CONTACTS;
        }

        public int getREAD_CALL_LOG() {
            return READ_CALL_LOG;
        }

        public void setREAD_CALL_LOG(int READ_CALL_LOG) {
            this.READ_CALL_LOG = READ_CALL_LOG;
        }

        public int getACCESS_FINE_LOCATION() {
            return ACCESS_FINE_LOCATION;
        }

        public void setACCESS_FINE_LOCATION(int ACCESS_FINE_LOCATION) {
            this.ACCESS_FINE_LOCATION = ACCESS_FINE_LOCATION;
        }

        public int getREAD_PHONE_STATE() {
            return READ_PHONE_STATE;
        }

        public void setREAD_PHONE_STATE(int READ_PHONE_STATE) {
            this.READ_PHONE_STATE = READ_PHONE_STATE;
        }

        public int getROOT() {
            return ROOT;
        }

        public void setROOT(int ROOT) {
            this.ROOT = ROOT;
        }

        public int getREAD_PRECISE_PHONE_STATE() {
            return READ_PRECISE_PHONE_STATE;
        }

        public void setREAD_PRECISE_PHONE_STATE(int READ_PRECISE_PHONE_STATE) {
            this.READ_PRECISE_PHONE_STATE = READ_PRECISE_PHONE_STATE;
        }

        public int getREAD_SMS() {
            return READ_SMS;
        }

        public void setREAD_SMS(int READ_SMS) {
            this.READ_SMS = READ_SMS;
        }
    }
}
