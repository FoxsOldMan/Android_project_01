package com.duanlian.fragmenthideandshowdemo;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


//@TargetApi(23)
public class viewAuthorityActivity extends Activity {

	protected void test() throws IOException {
		BufferedWriter out = new BufferedWriter(new FileWriter(new File("/data/system/appops.xml").getAbsolutePath()));
		out.close();
	}

	//小米界面
	private void gotoMiuiPermission() {
		Intent i = new Intent("miui.intent.action.APP_PERM_EDITOR");
		ComponentName componentName = new ComponentName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
		i.setComponent(componentName);
		i.putExtra("extra_pkgname", getPackageName());
		try {
			startActivity(i);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//sumsang
	private void gotoSumsangPermission() {
		Intent i = new Intent();
		ComponentName componentName = ComponentName.unflattenFromString("com.samsung.android.packageinstaller/com.android.packageinstaller.permission.ui.ManagePermissionsActivity");
//		i.setComponent(componentName);
		i.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
		i.setData(Uri.fromParts("package", getPackageName(), null));
		try {
			startActivity(i);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view);
		Button viewButton = (Button) findViewById(R.id.viewButton);
		viewButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				checkAuthority();
			}
		});

		Button changeAuthorityButton = (Button) findViewById(R.id.changeAuthority);
		changeAuthorityButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				gotoSumsangPermission();
			}
		});
	}

	/**
	 * packageName 微信/QQ的包名
	 * 微信包名   com.tencent.mm
	 * QQ包名     com.tencent.mobileqq
	 */
	protected void checkAuthority() {
		Log.d("", "===============into checkAuthority()===============");
		boolean isinstalled = isInstallApp("com.tencent.mm");
		if (isinstalled) {
			PackageManager packageManager = getPackageManager();
			int temp = packageManager.checkPermission("android.permission.WRITE_EXTERNAL_STORAGE", "com.tencent.mm");
			if (temp == PackageManager.PERMISSION_GRANTED)
				Toast.makeText(this, "true", Toast.LENGTH_LONG).show();
			else
				Toast.makeText(this, "false", Toast.LENGTH_LONG).show();
		} else
			Toast.makeText(this, "not install QQ", Toast.LENGTH_LONG).show();
	}

	//检查是否安装指定APP
	protected boolean isInstallApp(String packageName) {
		Log.d("", "===============isInstallApp()===============");
		try {
			getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
			return true;
		} catch (PackageManager.NameNotFoundException e) {
			return false;
		}
	}

}
