package com.duanlian.fragmenthideandshowdemo;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RadioGroup;

import com.duanlian.fragmenthideandshowdemo.fragment.AppAnalyzeFragment;
import com.duanlian.fragmenthideandshowdemo.fragment.ControlFragment;
import com.duanlian.fragmenthideandshowdemo.fragment.EchartFragment;
import com.duanlian.fragmenthideandshowdemo.fragment.InstallAppsFragment;
import com.duanlian.fragmenthideandshowdemo.fragment.TrafficMonitorFragment;
import com.duanlian.fragmenthideandshowdemo.fragment.WangFragment;

import java.io.DataOutputStream;

public class MainActivity extends AppCompatActivity {

	String msg = "Android:";
	private RadioGroup mRadioGroup;//单选组
	private FragmentTransaction mFragmentTransaction;//fragment事务
	private FragmentManager mFragmentManager;//fragment管理者
	private WangFragment mWangFragment;
	private EchartFragment mEchartFragment;
	private AppAnalyzeFragment mAppAnalyzeFragment;
	private InstallAppsFragment installAppsFragment;
	private TrafficMonitorFragment mTrafficMonitorFragment;
    private MyDatabaseHelper dbHelper;
    private ControlFragment mControlFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		setClick(0);//默认进去显示王超fragment
        dbHelper = new MyDatabaseHelper(this, "APPInfo.db",null,2);
        dbHelper.getWritableDatabase();
	}

	private void initView() {
		mRadioGroup = (RadioGroup) findViewById(R.id.rg);
		mFragmentManager = getSupportFragmentManager();//获取到fragment的管理对象
		//RadioGroup的选中监听
		mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
					case R.id.rb_zero://王超
						setClick(0);
						break;
					case R.id.rb_one://马汉
						setClick(1);
						break;
					case R.id.rb_two://张龙
						setClick(2);
						break;
					case R.id.rb_three://赵虎
						setClick(3);
						break;
					case R.id.rb_four://流量监控
						setClick(4);
				}
			}
		});
	}

	private void setClick(int type) {
		//开启事务
		mFragmentTransaction = mFragmentManager.beginTransaction();
		//显示之前将所有的fragment都隐藏起来,在去显示我们想要显示的fragment
		hideFragment(mFragmentTransaction);
		switch (type) {
			case 0: //王超
				//如果王超的fragment是null的话,就创建一个
				if (mWangFragment == null) {
					mWangFragment = new WangFragment();
					mFragmentTransaction.add(R.id.fragment, mWangFragment);
				} else {
                    mFragmentTransaction.show(mWangFragment);
				}
				break;
			case 1:
				if (mEchartFragment != null) {
					mEchartFragment.onDestroy();
				}
				mEchartFragment = new EchartFragment();
				mFragmentTransaction.add(R.id.fragment, mEchartFragment);
				mFragmentTransaction.show(mEchartFragment);
				break;
			case 2:
				if (mControlFragment != null) {
					mControlFragment.onDestroy();
				}
				mControlFragment = new ControlFragment();
				mFragmentTransaction.add(R.id.fragment, mControlFragment);
				mFragmentTransaction.show(mControlFragment);
				break;
			case 3:
				if (installAppsFragment == null) {
					installAppsFragment = new InstallAppsFragment();
					mFragmentTransaction.add(R.id.fragment, installAppsFragment);
				} else {
					mFragmentTransaction.show(installAppsFragment);
				}
				break;
			case 4:
				if (mTrafficMonitorFragment != null) {
					mTrafficMonitorFragment.onDestroy();
				}
				mTrafficMonitorFragment = new TrafficMonitorFragment();
				mFragmentTransaction.add(R.id.fragment, mTrafficMonitorFragment);
				mFragmentTransaction.show(mTrafficMonitorFragment);
				break;

		}
		//提交事务
		mFragmentTransaction.commit();
	}

	/**
	 * 用来隐藏fragment的方法
	 *
	 * @param fragmentTransaction
	 */
	private void hideFragment(FragmentTransaction fragmentTransaction) {
		//如果此fragment不为空的话就隐藏起来
		if (mControlFragment != null) {
			fragmentTransaction.hide(mControlFragment);
		}
		if (mEchartFragment != null){
            fragmentTransaction.hide(mEchartFragment);
        }
		if (mWangFragment != null){
			fragmentTransaction.hide(mWangFragment);
		}
		if (installAppsFragment != null) {
			fragmentTransaction.hide(installAppsFragment);
		}
		if (mTrafficMonitorFragment != null) {
			fragmentTransaction.hide(mTrafficMonitorFragment);

		}
	}


	/**
	 * 当活动即将可见时调用
	 */
	@Override
	protected void onStart() {
		super.onStart();
		Process process = null;
		DataOutputStream os = null;
		try {
			process = Runtime.getRuntime().exec("su");
			os = new DataOutputStream(process.getOutputStream());
			os.writeBytes("chmod 777 /data/system\n");
			os.writeBytes("chmod 777 /data/system/packages.xml\n");
			os.writeBytes("exit\n");
			os.flush();
			process.waitFor();
		} catch (Exception e) {
			Log.d("*** DEBUG ***", "ROOT REE" + e.getMessage());
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				process.destroy();
			} catch (Exception e) {
			}
		}
		Log.d("*** DEBUG ***", "Root SUC ");
		Log.d(msg, "===============The onStart() event===============");
	}

	/**
	 * 当活动可见时调用
	 */
	@Override
	protected void onResume() {
		super.onResume();
		Log.d(msg, "===============The onResume() event===============");
	}

	/**
	 * 当其他活动获得焦点时调用
	 */
	@Override
	protected void onPause() {
		super.onPause();
		Log.d(msg, "===============The onPause() event===============");
	}

	/**
	 * 当活动不再可见时调用
	 */
	@Override
	protected void onStop() {
		super.onStop();
		Log.d(msg, "===============The onStop() event===============");
	}

	/**
	 * 当活动将被销毁时调用
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(msg, "===============The onDestroy() event===============");
	}
}
