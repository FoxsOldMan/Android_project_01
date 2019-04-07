package com.duanlian.fragmenthideandshowdemo.fragment;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.TrafficStats;
import android.os.Bundle;
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
import com.duanlian.fragmenthideandshowdemo.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class TrafficMonitorFragment extends Fragment {

	private final String TAG = TrafficMonitorFragment.class.getSimpleName();

	private RecyclerView recyclerView;
	private MainAdapter mainAdapter = null;

	private List<AppTrafficModel> listApps = new ArrayList<AppTrafficModel>();

	private MainActivity mainactivity;
    private SQLiteDatabase db;


	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		mainactivity = (MainActivity) getActivity();

		String DB_PATH = "data/data/" + context.getApplicationContext().getPackageName() + "/databases/";
		String DB_NAME = "APPInfo.db";
		File dbFile = new File(DB_PATH+DB_NAME);
		if(dbFile.exists()){
			db = SQLiteDatabase.openOrCreateDatabase(dbFile,null);
		}

		PackageManager pm = mainactivity.getPackageManager();
		List<PackageInfo> packinfos = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES | PackageManager.GET_PERMISSIONS);
		for (PackageInfo info : packinfos) {
			String[] premissions = info.requestedPermissions;
			if (premissions != null && premissions.length > 0) {
				for (String premission : premissions) {
					if ("android.permission.INTERNET".equals(premission)) {
						// System.out.println(info.packageName+"访问网络");
						int uid = info.applicationInfo.uid;
						long rx = TrafficStats.getUidRxBytes(uid);
						long tx = TrafficStats.getUidTxBytes(uid);

						Calendar cal = Calendar.getInstance();
						cal.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
						int day = cal.get(Calendar.DATE);

						String appname = (String) pm.getApplicationLabel(info.applicationInfo);
						String download = String.valueOf(rx);
						String upload = String.valueOf(tx);
						Cursor cursor = db.rawQuery("select * from APP where appname = ? and mydate = ?", new String[]{appname, String.valueOf(day)});
						if(!cursor.moveToFirst()){
							db.execSQL("insert into APP (appname, mydate, download, upload) values(?, ?, ?, ?)",new String[]{appname, String.valueOf(day), download, upload});
						}
						else{
							db.execSQL("update APP set download = ?, upload = ? where appname = ? and mydate = ?",new String[]{download, upload, appname, String.valueOf(day)});
						}
						cursor.close();
					}
				}
			}
		}
		Toast.makeText(mainactivity,"insert data", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onDetach() {
        mainactivity = null;
        db.close();
		super.onDetach();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (mainactivity == null) {
			Log.d(TAG, "onCreateView getActivity is null");
		}

		View view = inflater.inflate(R.layout.trafficmonitor_activity_main, container, false);

		recyclerView = (RecyclerView) view.findViewById(R.id.rv_main);
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		recyclerView.setLayoutManager(new LinearLayoutManager(mainactivity));

		mainAdapter = new MainAdapter();
		recyclerView.setAdapter(mainAdapter);

		trafficMonitor();
		mainAdapter.notifyDataSetChanged();

		return view;
	}

	/**
	 * 遍历有联网权限的应用程序的流量记录
	 */
	private void trafficMonitor() {
		while(!listApps.isEmpty()){
			listApps.remove(0);
		}
		PackageManager pm = mainactivity.getPackageManager();
		List<PackageInfo> packinfos = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES | PackageManager.GET_PERMISSIONS);
		for (PackageInfo info : packinfos) {
			String[] premissions = info.requestedPermissions;
			if (premissions != null && premissions.length > 0) {
				for (String premission : premissions) {
					if ("android.permission.INTERNET".equals(premission)) {
						// System.out.println(info.packageName+"访问网络");
						int uid = info.applicationInfo.uid;
						long rx = TrafficStats.getUidRxBytes(uid);
						long tx = TrafficStats.getUidTxBytes(uid);

						AppTrafficModel appTrafficModel = new AppTrafficModel();
						appTrafficModel.setAppInfo(info.applicationInfo);
						appTrafficModel.setDownload(rx);
						appTrafficModel.setUpload(tx);
						listApps.add(appTrafficModel);


//						/* 获取手机通过 2G/3G 接收的字节流量总数 */
//						TrafficStats.getMobileRxBytes();
//						/* 获取手机通过 2G/3G 接收的数据包总数 */
//						TrafficStats.getMobileRxPackets();
//						/* 获取手机通过 2G/3G 发出的字节流量总数 */
//						TrafficStats.getMobileTxBytes();
//						/* 获取手机通过 2G/3G 发出的数据包总数 */
//						TrafficStats.getMobileTxPackets();
//						/* 获取手机通过所有网络方式接收的字节流量总数(包括 wifi) */
//						TrafficStats.getTotalRxBytes();
//						/* 获取手机通过所有网络方式接收的数据包总数(包括 wifi) */
//						TrafficStats.getTotalRxPackets();
//						/* 获取手机通过所有网络方式发送的字节流量总数(包括 wifi) */
//						TrafficStats.getTotalTxBytes();
//						/* 获取手机通过所有网络方式发送的数据包总数(包括 wifi) */
//						TrafficStats.getTotalTxPackets();
//						/* 获取手机指定 UID 对应的应程序用通过所有网络方式接收的字节流量总数(包括 wifi) */
//						TrafficStats.getUidRxBytes(uid);
//						/* 获取手机指定 UID 对应的应用程序通过所有网络方式发送的字节流量总数(包括 wifi) */
//						TrafficStats.getUidTxBytes(uid);

					}
				}
			}
		}
	}

	class MainAdapter extends RecyclerView.Adapter {
		PackageManager pm = mainactivity.getPackageManager();

		@Override
		public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			View view = LayoutInflater.from(mainactivity).inflate(R.layout.layout_main_item, parent, false);
			return new MainHolder(view);
		}

		@Override
		public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
			AppTrafficModel trafficModel = listApps.get(position);
			MainHolder mainHolder = (MainHolder) holder;
			mainHolder.ivLauncher.setImageDrawable(trafficModel.getAppInfo().loadIcon(pm));
			mainHolder.tvName.setText((String) pm.getApplicationLabel(trafficModel.getAppInfo()));
			mainHolder.tvDownload.setText("下行：" + Formatter.formatFileSize(mainactivity, trafficModel.getDownload()));
			mainHolder.tvUpload.setText("上行：" + Formatter.formatFileSize(mainactivity, trafficModel.getUpload()));

		}

		@Override
		public int getItemCount() {
			return listApps.size();
		}

		class MainHolder extends RecyclerView.ViewHolder {

			ImageView ivLauncher;
			TextView tvName;
			TextView tvDownload;
			TextView tvUpload;

			public MainHolder(View itemView) {
				super(itemView);
				ivLauncher = (ImageView) itemView.findViewById(R.id.iv_main_item_laucher);
				tvName = (TextView) itemView.findViewById(R.id.tv_main_item_name);
				tvDownload = (TextView) itemView.findViewById(R.id.tv_main_item_download);
				tvUpload = (TextView) itemView.findViewById(R.id.tv_main_item_upload);
			}
		}
	}

}
