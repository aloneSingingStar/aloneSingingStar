package com.alonesingingstar.utils;

import android.content.res.Resources;

import com.alonesingingstar.App;

public class Tool {
    private static Resources res;
    static {
        res = App.getApp().getResources();
    }
    public static int dp2px(float dpValue){
        return (int) (dpValue*res.getDisplayMetrics().density+0.5f);
    }
    public static int px2dp(float pxValue){
        return (int) (pxValue/res.getDisplayMetrics().density+0.5f);
    }
}
