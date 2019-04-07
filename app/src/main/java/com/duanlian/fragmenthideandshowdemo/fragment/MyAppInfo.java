package com.duanlian.fragmenthideandshowdemo.fragment;

import android.graphics.drawable.Drawable;

public class MyAppInfo {
	private Drawable image;
	private String appName;
	private String packagename;
	private boolean[] authority;

	public boolean[] getAuthority() {
		return authority;
	}

	public void setAuthority(boolean[] authority) {
		this.authority = authority;
	}

	public String getPackagename() {
		return packagename;
	}

	public void setPackagename(String packagename) {
		this.packagename = packagename;
	}

	MyAppInfo(Drawable image, String appName) {
		this.image = image;
		this.appName = appName;
	}

	public MyAppInfo() {

	}

	public Drawable getImage() {
		return image;
	}

	public void setImage(Drawable image) {
		this.image = image;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}
}