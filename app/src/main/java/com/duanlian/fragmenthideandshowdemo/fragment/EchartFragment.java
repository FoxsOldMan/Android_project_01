package com.duanlian.fragmenthideandshowdemo.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.duanlian.fragmenthideandshowdemo.MainActivity;
import com.duanlian.fragmenthideandshowdemo.MyDatabaseHelper;
import com.duanlian.fragmenthideandshowdemo.R;
import com.duanlian.fragmenthideandshowdemo.bean.EchartsDataBean;
import com.duanlian.fragmenthideandshowdemo.bean.EchartsLineBean;

import java.io.File;

public class EchartFragment extends Fragment {
    String TAG;
    private ProgressDialog dialog;
    private MainActivity mainactivity;

    View view;
    Button linechartBt;
    Button barchartBt;
//    Button piechartBt;
    Button yestodayBt;
    Button beforeBt;
    WebView chartshowWb;
    ImageView ivLeft;
    ImageView ivRight;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainactivity = (MainActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainactivity = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        if (mainactivity == null) {
            Log.d(TAG, "onCreateView getActivity is null");
        }
        view = inflater.inflate(R.layout.chart_fragment, container, false);
        chartshowWb = (WebView) view.findViewById(R.id.chartshow_wb);
        linechartBt = (Button) view.findViewById(R.id.linechart_bt);
        linechartBt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                chartshowWb.loadUrl("javascript:createChart('line'," + EchartsDataBean.getInstance().getTodayEchartsLineJson() + ");");
            }
        });
        barchartBt = (Button) view.findViewById(R.id.barchart_bt);
        barchartBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chartshowWb.loadUrl("javascript:createChart('bar'," + EchartsDataBean.getInstance().getEchartsBarJson() + ");");
            }
        });
        yestodayBt = (Button) view.findViewById(R.id.yestodaychart_bt);
        yestodayBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chartshowWb.loadUrl("javascript:createChart('bar'," + EchartsDataBean.getInstance().getYestodayEchartsBarJson() + ");");
            }
        });
        beforeBt = (Button) view.findViewById(R.id.beforechart_bt);
        beforeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chartshowWb.loadUrl("javascript:createChart('bar'," + EchartsDataBean.getInstance().getBeforeEchartsBarJson() + ");");
            }
        });
//        piechartBt = (Button) view.findViewById(R.id.piechart_bt);
//        piechartBt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                chartshowWb.loadUrl("javascript:createChart('pie'," + EchartsDataBean.getInstance().getEchartsPieJson() + ");");
//            }
//        });
        ivLeft = (ImageView) view.findViewById(R.id.iv_left);
        ivRight = (ImageView) view.findViewById(R.id.iv_right);

        initData();
        initListener();
        chartshowWb.loadUrl("javascript:createChart('line'," + EchartsDataBean.getInstance().getTodayEchartsLineJson() + ");");
        view.findViewById(R.id.rl_bottom).setVisibility(View.GONE);
        Toast.makeText(mainactivity,"init", Toast.LENGTH_SHORT).show();
        return view;
    }

    private void initData() {
        dialog = new ProgressDialog(mainactivity);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("玩儿命加载中...");

        TAG = this.getClass().getName();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initListener() {

        //进行webwiev的一堆设置
        chartshowWb.getSettings().setAllowFileAccess(true);
        chartshowWb.getSettings().setJavaScriptEnabled(true);
        chartshowWb.loadUrl("file:///android_asset/echart/myechart.html");
        chartshowWb.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                dialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //最好在这里调用js代码 以免网页未加载完成

                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
    }

    String newDataX = "['13:20', '13:30', '13:40', '13:50', '14:00', '14:10', '14:20', '14:30', " +
            "'14:40','14:50','15:00','15:10','15:20','15:30','15:40','15:50','16:00'," +
            "'16:10','16:20','16:30']";

    String newDataY = "[0 ,70,60, 60, 90, 56, 150, 60, 80,0 ,70,60, 60,60,60,140,60,0 ,70,60]";


    /**
     * start 和 end 意为拖放的起始点 范围均为  0-100;
     * js中设置的默认初始值 , 和 activity中设置的默认值   两者必须一致, 不然会有错乱
     */
    int start = 20, end = 85;

    private void dealwithLeft() {
        if (start >= 5) {
            start -= 5;
            if (end <= 105 && end >= start + 15) {
                end -= 5;
            }
            if (!ivRight.isEnabled()) {
                ivRight.setEnabled(true);
                ivRight.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.right_blue_bg));
            }
            chartshowWb.loadUrl("javascript:updateZoomState(" + start + "," + end + ");");
        } else {
            if (ivLeft.isEnabled()) {
                ivLeft.setEnabled(false);
                ivLeft.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.left_gray_bg));
            }
            Log.i(TAG, "start == " + start + "  end== " + end);
        }
    }

    private void dealwithRight() {
        if (end <= 100) {
            end += 5;
            if (start < end - 15) {
                start += 5;
            }
            if (!ivLeft.isEnabled()) {
                ivLeft.setEnabled(true);
                ivLeft.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.left_blue_bg));
            }
            chartshowWb.loadUrl("javascript:updateZoomState(" + start + "," + end + ");");
        } else {
            if (ivRight.isEnabled()) {
                ivRight.setEnabled(false);
                ivRight.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.right_gray_bg));
            }
            Log.i(TAG, "start == " + start + "  end== " + end);
        }
    }
}
