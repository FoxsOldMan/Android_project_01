package com.duanlian.fragmenthideandshowdemo.bean;

import java.util.Arrays;

/**
 * Created by dell1 on 2017/5/28.
 */
public class EchartsLineBean {

    public String type;
    public String title;
    public int maxValue;
    public int minValue;
    public String[] authority;
    public int[] times;


    @Override
    public String toString() {
        return "EchartsLineBean{" +
                "type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", maxValue=" + maxValue +
                ", minValue=" + minValue +
                ", authority=" + Arrays.toString(authority) +
                ", times=" + Arrays.toString(times) +
                '}';
    }

//    public String type;
//    public String title;
//    public int maxValue;
//    public int minValue;
//    public String[] times;
//    public int[] steps;
//
//
//    @Override
//    public String toString() {
//        return "EchartsLineBean{" +
//                "type='" + type + '\'' +
//                ", title='" + title + '\'' +
//                ", maxValue=" + maxValue +
//                ", minValue=" + minValue +
//                ", times=" + Arrays.toString(times) +
//                ", steps=" + Arrays.toString(steps) +
//                '}';
//    }
}
