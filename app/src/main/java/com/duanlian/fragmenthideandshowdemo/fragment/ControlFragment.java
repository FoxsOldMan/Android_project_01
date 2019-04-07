package com.duanlian.fragmenthideandshowdemo.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.duanlian.fragmenthideandshowdemo.MainActivity;
import com.duanlian.fragmenthideandshowdemo.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class ControlFragment extends android.support.v4.app.Fragment {
	private final String TAG = ControlFragment.class.getSimpleName();
	MainAdapter mainAdapter;
	RecyclerView listView;
	MainActivity mainActivity;
	List<Analyze_item> listresult = new ArrayList<Analyze_item>();
	SQLiteDatabase db;
	RelativeLayout view;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		System.err.println("onCreate()");

		super.onCreate(savedInstanceState);
	}

	@Override
	public void onDestroy() {
		System.err.println("onDestroy()");
		super.onDestroy();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onAttach(Context context) {
		while(!listresult.isEmpty()){
			listresult.remove(0);
		}
		super.onAttach(context);

		mainActivity = (MainActivity) getActivity();

		String DB_PATH = "data/data/" + context.getApplicationContext().getPackageName() + "/databases/";
		String DB_NAME = "APPInfo.db";
		File dbFile = new File(DB_PATH+DB_NAME);
		if(dbFile.exists()){
			db = SQLiteDatabase.openOrCreateDatabase(dbFile,null);
		}

		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		int day = cal.get(Calendar.DATE);

		Cursor cursor = db.rawQuery("select * from APP where download is not null and root is not null and mydate = ?", new String[]{String.valueOf(day)});
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
				item.setRisk(0.1632*item.getSEND_SMS() + 0.0799*item.getREAD_CONTACTS() + 0.0610*item.getREAD_CALL_LOG() + 0.0348*item.getACCESS_FINE_LOCATION()
							+ 0.0248*item.getREAD_PHONE_STATE() + 0.3577*item.getROOT() + 0.0620*item.getREAD_PRECISE_PHONE_STATE() + 0.2166*item.getREAD_SMS());


				listresult.add(item);
			} while (cursor.moveToNext());
			Toast.makeText(mainActivity,"amount:"+listresult.size(), Toast.LENGTH_SHORT).show();
		}
		else{
			Toast.makeText(mainActivity,"no data in DB today", Toast.LENGTH_SHORT).show();
		}
		cursor.close();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
		if (mainActivity == null) {
			Log.d(TAG, "onCreateView getActivity is null");
		}
//		System.err.println("onCreateView()");
		super.onCreate(savedInstanceState);

		view = (RelativeLayout) inflater.inflate(R.layout.control_list, container, false);
		listView = (RecyclerView) view.findViewById(R.id.control_list);
		view.removeView(listView);
		listView.setItemAnimator(new DefaultItemAnimator());
		listView.setLayoutManager(new LinearLayoutManager(mainActivity));

		mainAdapter = new MainAdapter();
		listView.setAdapter(mainAdapter);

		return listView;
	}

	class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainHolder> {
		PackageManager pm = mainActivity.getPackageManager();

		@Override
		public MainHolder onCreateViewHolder(ViewGroup parent, int viewType) {

			final View view = LayoutInflater.from(mainActivity).inflate(R.layout.control_item, parent, false);
			final MainHolder mainHolder = new MainHolder(view);

			return mainHolder;
		}

		//设置holder中的值
		@Override
		public void onBindViewHolder(MainHolder holder, int position) {
//			System.err.println("MainAdapter.onBindViewHolder()");
			Analyze_item analyze_item = listresult.get(position);
			MainHolder mainHolder = (MainHolder) holder;
			List<PackageInfo> packinfos = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES | PackageManager.GET_PERMISSIONS);
			for (PackageInfo info : packinfos) {
				if(analyze_item.getAppName().equals((String) pm.getApplicationLabel(info.applicationInfo))){
					analyze_item.setPackagename(info.packageName.toString());
					mainHolder.appImage.setImageDrawable(info.applicationInfo.loadIcon(pm));
					mainHolder.appName.setText("名称："+ analyze_item.getAppName());
					mainHolder.analyze_Result.setText("危险度："+analyze_item.getRisk());
					return;
				}
			}
			Toast.makeText(mainActivity,"APP名称不匹配", Toast.LENGTH_SHORT).show();

		}

		@Override
		public int getItemCount() {
//			System.err.println("MainAdapter.getItemCount()");
			return listresult.size();
		}

		//单个app项目的类
		class MainHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
			ImageView appImage;
			TextView appName;
			TextView analyze_Result;

			MainHolder(View itemView) {
				super(itemView);
				appImage = (ImageView) itemView.findViewById(R.id.app_image);
				appName = (TextView) itemView.findViewById(R.id.app_name);
				analyze_Result = (TextView) itemView.findViewById(R.id.app_result);

				appImage.setOnClickListener(this);
				appName.setOnClickListener(this);
			}

			@Override
			public void onClick(View v) {
				int position = getAdapterPosition();
				Intent i = new Intent();

				i.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
				if(listresult.get(position).getPackagename() == null){
					List<PackageInfo> packinfos = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES | PackageManager.GET_PERMISSIONS);
					for (PackageInfo info : packinfos) {
						if(listresult.get(position).getAppName().equals((String) pm.getApplicationLabel(info.applicationInfo))){
							listresult.get(position).setPackagename(info.packageName.toString());
							break;
						}
					}
				}
				i.setData(Uri.fromParts("package", listresult.get(position).getPackagename(), null));
				try {
					startActivity(i);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	class Analyze_item{
		String appName;
		String packagename = null;
		float Download;
		float Upload;
		int SEND_SMS, READ_CONTACTS, READ_CALL_LOG, ACCESS_FINE_LOCATION, READ_PHONE_STATE, ROOT, READ_PRECISE_PHONE_STATE, READ_SMS;
		double risk;

		public String getPackagename() {
			return packagename;
		}

		public void setPackagename(String packagename) {
			this.packagename = packagename;
		}

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


	public boolean[] check(String packageName) {
		boolean[] result = new boolean[8];

		result[0] = checkHelper("android.permmision.SEND_SMS", packageName);
		result[1] = checkHelper("android.permission.READ_CONTACTS", packageName);
		result[2] = checkHelper("android.permission.READ_CALL_LOG", packageName);
		result[3] = checkHelper("android.permission.ACCESS_FINE_LOCATION", packageName);
		result[4] = checkHelper("android.permission.READ_PHONE_STATE", packageName);
		result[5] = false;
		result[6] = checkHelper("android.permission.READ_PRECISE_PHONE_STATE", packageName);
		result[7] = checkHelper("android.permission.READ_SMS", packageName);

		return result;
	}

	public boolean checkHelper(String permissionName, String packageName) {
		PackageManager packageManager = mainActivity.getPackageManager();
		int temp = packageManager.checkPermission(permissionName, packageName);
		if (temp == PackageManager.PERMISSION_GRANTED){
			return true;
		} else {
			return false;
		}
	}
}