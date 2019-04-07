package com.duanlian.fragmenthideandshowdemo.bean;

import java.util.Arrays;

/**
 * Created by dell1 on 2017/5/29.
 */
public class EchartsBarBean {

    public String type1;
    public String title;
    public String[] times;
    public int[] data1;


    @Override
    public String toString() {
        return "EchartsBarBean{" +
                "type1='" + type1 + '\'' +
                ", title='" + title + '\'' +
                ", times=" + Arrays.toString(times) +
                ", data1=" + Arrays.toString(data1) +
                '}';
    }

}
