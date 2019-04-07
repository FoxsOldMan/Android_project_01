package com.duanlian.fragmenthideandshowdemo.fragment;

import android.content.Context;
import android.content.Intent;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

public class InstallAppsFragment extends android.support.v4.app.Fragment {
	MainAdapter mainAdapter;
	RecyclerView listView;
	MainActivity mainActivity;
	List<MyAppInfo> apps;
	RelativeLayout view;
	private SQLiteDatabase db;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		System.err.println("onCreate()");

		super.onCreate(savedInstanceState);
	}

	@Override
	public void onDestroy() {
		System.err.println("onDestroy()");
		super.onDestroy();
		mainActivity.finish();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
//		System.err.println("onActivityCreated()");
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onAttach(Context context) {
//		System.err.println("onAttach()");
		apps = getAllAppInfos();
		super.onAttach(context);
		mainActivity = (MainActivity) getActivity();
		String DB_PATH = "data/data/" + context.getApplicationContext().getPackageName() + "/databases/";
		String DB_NAME = "APPInfo.db";
		File dbFile = new File(DB_PATH+DB_NAME);
		if(dbFile.exists()){
			db = SQLiteDatabase.openOrCreateDatabase(dbFile,null);
		}
///////////////////////////////////////////////////

		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		int day = cal.get(Calendar.DATE);

		for(int i = 0;i < apps.size();i++){
			MyAppInfo myAppInfo = apps.get(i);

			String appname = myAppInfo.getAppName();
			String SEND_SMS, READ_CONTACTS, READ_CALL_LOG, ACCESS_FINE_LOCATION, READ_PHONE_STATE, ROOT, READ_PRECISE_PHONE_STATE, READ_SMS;
			if(myAppInfo.getAuthority()[0]){
				SEND_SMS = "1";
			}else{
				SEND_SMS = "0";
			}
			if(myAppInfo.getAuthority()[1]){
				READ_CONTACTS = "1";
			}else{
				READ_CONTACTS = "0";
			}
			if(myAppInfo.getAuthority()[2]){
				READ_CALL_LOG = "1";
			}else{
				READ_CALL_LOG = "0";
			}
			if(myAppInfo.getAuthority()[3]){
				ACCESS_FINE_LOCATION = "1";
			}else{
				ACCESS_FINE_LOCATION = "0";
			}
			if(myAppInfo.getAuthority()[4]){
				READ_PHONE_STATE = "1";
			}else{
				READ_PHONE_STATE = "0";
			}
			if(myAppInfo.getAuthority()[5]){
				ROOT = "1";
			}else{
				ROOT = "0";
			}
			if(myAppInfo.getAuthority()[6]){
				READ_PRECISE_PHONE_STATE = "1";
			}else{
				READ_PRECISE_PHONE_STATE = "0";
			}
			if(myAppInfo.getAuthority()[7]){
				READ_SMS = "1";
			}else{
				READ_SMS = "0";
			}

			Cursor cursor = db.rawQuery("select * from APP where appname = ? and mydate = ?", new String[]{appname, String.valueOf(day)});
			if(!cursor.moveToFirst()){
				db.execSQL("insert into APP (appname, mydate, SEND_SMS, READ_CONTACTS, READ_CALL_LOG, ACCESS_FINE_LOCATION, READ_PHONE_STATE, ROOT, READ_PRECISE_PHONE_STATE, READ_SMS) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",new String[]{appname, String.valueOf(day), SEND_SMS, READ_CONTACTS, READ_CALL_LOG, ACCESS_FINE_LOCATION, READ_PHONE_STATE, ROOT, READ_PRECISE_PHONE_STATE, READ_SMS});
			}
			else{
				db.execSQL("update APP set SEND_SMS = ?, READ_CONTACTS = ?, READ_CALL_LOG = ?, ACCESS_FINE_LOCATION = ?, READ_PHONE_STATE = ?, ROOT = ?, READ_PRECISE_PHONE_STATE = ?, READ_SMS = ? where appname = ? and mydate = ?",new String[]{SEND_SMS, READ_CONTACTS, READ_CALL_LOG, ACCESS_FINE_LOCATION, READ_PHONE_STATE, ROOT, READ_PRECISE_PHONE_STATE, READ_SMS, appname, String.valueOf(day)});
			}
			cursor.close();
		}
		Toast.makeText(mainActivity,"insert data", Toast.LENGTH_SHORT).show();
/////////////////////////////////////////////////////
	}


	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//		System.err.println("onCreateView()");
		super.onCreate(savedInstanceState);
		PackageManager pm = mainActivity.getPackageManager();

		view = (RelativeLayout) inflater.inflate(R.layout.layout_view_apps, container, false);
		listView = (RecyclerView) view.findViewById(R.id.app_list);
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

//			System.err.println("MainAdapter.onCreateViewHolder()");
			final View view = LayoutInflater.from(mainActivity).inflate(R.layout.layout_app, parent, false);
			final MainHolder mainHolder = new MainHolder(view);
			return mainHolder;
		}

		//设置holder中的值
		@Override
		public void onBindViewHolder(MainHolder holder, int position) {
//			System.err.println("MainAdapter.onBindViewHolder()");
			MyAppInfo myAppInfo = apps.get(position);
			MainHolder mainHolder = (MainHolder) holder;
			mainHolder.appImage.setImageDrawable(myAppInfo.getImage());
			mainHolder.appName.setText(myAppInfo.getAppName());
			mainHolder.SEND_SMS.setText("" + myAppInfo.getAuthority()[0]);
			mainHolder.READ_CONTACTS.setText("" + myAppInfo.getAuthority()[1]);
			mainHolder.READ_CALL_LOG.setText("" + myAppInfo.getAuthority()[2]);
			mainHolder.ACCESS_FINE_LOCATION.setText("" + myAppInfo.getAuthority()[3]);
			mainHolder.READ_PHONE_STATE.setText("" + myAppInfo.getAuthority()[4]);
			mainHolder.ROOT.setText("" + myAppInfo.getAuthority()[5]);
			mainHolder.READ_PRECISE_PHONE_STATE.setText("" + myAppInfo.getAuthority()[6]);
			mainHolder.READ_SMS.setText("" + myAppInfo.getAuthority()[7]);

		}

		@Override
		public int getItemCount() {
//			System.err.println("MainAdapter.getItemCount()");
			return apps.size();
		}

		//单个app项目的类
		class MainHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
			ImageView appImage;
			TextView appName;
			TextView SEND_SMS;
			TextView READ_CONTACTS;
			TextView READ_CALL_LOG;
			TextView ACCESS_FINE_LOCATION;
			TextView READ_PHONE_STATE;
			TextView ROOT;
			TextView READ_PRECISE_PHONE_STATE;
			TextView READ_SMS;

			MainHolder(View itemView) {
				super(itemView);
//				System.err.println("MainAdapter.MainHolder.MainHolder()");
				appImage = (ImageView) itemView.findViewById(R.id.app_image);
				appName = (TextView) itemView.findViewById(R.id.app_name);
				SEND_SMS = (TextView) itemView.findViewById(R.id.SEND_SMS);
				READ_CONTACTS = (TextView) itemView.findViewById(R.id.READ_CONTACTS);
				READ_CALL_LOG = (TextView) itemView.findViewById(R.id.READ_CALL_LOG);
				ACCESS_FINE_LOCATION = (TextView) itemView.findViewById(R.id.ACCESS_FINE_LOCATION);
				READ_PHONE_STATE = (TextView) itemView.findViewById(R.id.READ_PHONE_STATE);
				ROOT = (TextView) itemView.findViewById(R.id.ROOT);
				READ_PRECISE_PHONE_STATE = (TextView) itemView.findViewById(R.id.READ_PRECISE_PHONE_STATE);
				READ_SMS = (TextView) itemView.findViewById(R.id.READ_SMS);

//				RelativeLayout relativeLayout = (RelativeLayout)itemView.findViewById(R.id.sec0);
//				relativeLayout.setOnClickListener(this);
			}

			@Override
			public void onClick(View v) {
				int position = getAdapterPosition();
				Intent i = new Intent();
//		ComponentName componentName = ComponentName.unflattenFromString("com.samsung.android.packageinstaller/com.android.packageinstaller.permission.ui.ManagePermissionsActivity");
//		i.setComponent(componentName);
				i.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
				i.setData(Uri.fromParts("package", apps.get(position).getPackagename(), null));
				try {
					startActivity(i);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	protected List<MyAppInfo> getAllAppInfos() {
//		System.err.println("getAllAppInfos()");
		mainActivity = (MainActivity) getContext();
//		System.err.println(mainActivity.getPackageName());
		List<MyAppInfo> list = new ArrayList<MyAppInfo>();
		PackageManager packageManager = mainActivity.getPackageManager(); //得到应用的packgeManager
		Intent intent = new Intent(); //创建一个主界面的intent
		intent.setAction(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		List<ResolveInfo> ResolveInfos = packageManager.queryIntentActivities(intent, 0); //得到包含应用信息的列表

		for (ResolveInfo ri : ResolveInfos) { // 遍历
			String packageName = ri.activityInfo.packageName; //得到包名
			Drawable icon = ri.loadIcon(packageManager); //得到图标
			String appName = ri.loadLabel(packageManager).toString(); //得到应用名称
			MyAppInfo appInfo = new MyAppInfo(icon, appName); //封装应用信息对象
			appInfo.setPackagename(packageName);
			boolean[] temp = check(packageName);
			appInfo.setAuthority(temp);
			list.add(appInfo); //添加到list
		}
		return list;
	}

	public boolean[] check(String packageName) {
		boolean[] result = new boolean[8];
		/*
		  0.SEND_SMS                 发送短信
		  1.READ_CONTACTS            访问联系人通讯录信息
		  2.READ_CALL_LOG            读取通话记录
		  3.ACCESS_FINE_LOCATION     获取精确位置
		  4.READ_PHONE_STATE         获得IMEI号码

		  5.ROOT

		  6.READ_PRECISE_PHONE_STATE 监听来电状态
		  7.READ_SMS                 读取短消息内容
		 */
		result[0] = checkHelper("android.permmision.SEND_SMS", packageName);
		result[1] = checkHelper("android.permission.READ_CONTACTS", packageName);
		result[2] = checkHelper("android.permission.READ_CALL_LOG", packageName);
		result[3] = checkHelper("android.permission.ACCESS_FINE_LOCATION", packageName);
		result[4] = checkHelper("android.permission.READ_PHONE_STATE", packageName);
		result[5] = false;
		result[6] = checkHelper("android.permission.READ_PRECISE_PHONE_STATE", packageName);
		result[7] = checkHelper("android.permission.READ_SMS", packageName);

//		System.err.println(packageName + ":\t\t\t\t\t" +
////				result[0] + "," + result[1] + "," + result[2] + "," + result[3] + "," +
////				result[4] + "," + result[5] + "," + result[6] + "," + result[7]);
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