package com.duanlian.fragmenthideandshowdemo.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.duanlian.fragmenthideandshowdemo.R;
import com.duanlian.fragmenthideandshowdemo.viewAuthorityActivity;

import java.io.File;

/**
 * 王超页面
 */

public class WangFragment extends Fragment {

	String msg = "Android:";

	// 判断是否具有ROOT权限
	public static boolean is_root() {

		boolean res = false;

		try {
			if ((!new File("/system/bin/su").exists()) &&
					(!new File("/system/xbin/su").exists())) {
				res = false;
			} else {
				res = true;
			}
			;
		} catch (Exception e) {

		}
		return res;
	}

	// 获取ROOT权限
	public void get_root() {

		if (is_root()) {
			Toast.makeText(getActivity(), "已经具有ROOT权限!", Toast.LENGTH_LONG).show();
		} else {
			try {
				ProgressDialog.show(getActivity(),
						"ROOT", "正在获取ROOT权限...", true, true);
				Runtime.getRuntime().exec("su");
			} catch (Exception e) {
				Toast.makeText(getActivity(), "获取ROOT权限时出错!", Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = View.inflate(getActivity(), R.layout.fragment_wang, null);
        //TextView name = (TextView) view.findViewById(R.id.hello);
        /*name.setText("王超");
		Button button = (Button) view.findViewById(R.id.viewAuthority);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String str = "===============点击事件===============";
				Log.d(msg, str);
				startActivity(new Intent(getContext(), viewAuthorityActivity.class));
			}
		});
		Button openService = (Button) view.findViewById(R.id.openService);
		openService.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d("", "===============onlick startService===============");
				// startService(v);
			}
		});
		Button closeService = (Button) view.findViewById(R.id.closeService);
		closeService.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d("", "===============onclick stopService===============");
				//  stopService(v);
			}
		});*/

		Button getroot = (Button) view.findViewById(R.id.getroot);
		getroot.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				get_root();
			}
		});
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}


}
