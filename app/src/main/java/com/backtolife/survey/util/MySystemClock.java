package com.backtolife.survey.util;

public class MySystemClock  {
    public double nowSeconds(){
        return (double) System.currentTimeMillis() / 1000.;
    }

    public static double getNowSeconds(){
        return new MySystemClock().nowSeconds();
    }
}
