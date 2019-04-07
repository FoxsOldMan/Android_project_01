package com.duanlian.fragmenthideandshowdemo.fragment;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service {
	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("", "===============on start command===============");
		// Let it continue running until it is stopped.
		Toast.makeText(this, "服务已经启动", Toast.LENGTH_LONG).show();
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		Log.d("", "on destory");
		super.onDestroy();
		Toast.makeText(this, "服务已经停止", Toast.LENGTH_LONG).show();
	}
}
