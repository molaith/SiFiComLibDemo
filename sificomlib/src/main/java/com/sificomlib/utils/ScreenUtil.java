package com.sificomlib.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;

import com.sificomlib.logger.SFLogger;

public class ScreenUtil {
    public int screenWidth = 0;
    public int screenHeight = 0;
    public float density = 0;
    public int densityDpi = 0;
    public float densityDpiX = 0;
    public float densityDpiY = 0;
    public float scaleDensity = 0;
    private boolean isInitialed = false;

    private static ScreenUtil instance = null;

    public static ScreenUtil getInstance() {
        if (instance == null) {
            instance = new ScreenUtil();
        }
        return instance;
    }

    private ScreenUtil() {
    }

    public void init(Context activityContext) {
        if (isInitialed) {
            return;
        }
        DisplayMetrics dm = activityContext.getResources().getDisplayMetrics();
        StringBuilder sb = new StringBuilder();
        sb.append("_______  显示屏幕信息:  ");
        sb.append("\ndensity         :").append(dm.density);
        sb.append("\ndensityDpi      :").append(dm.densityDpi);
        sb.append("\nheightPixels    :").append(dm.heightPixels);
        sb.append("\nwidthPixels     :").append(dm.widthPixels);
        sb.append("\nscaledDensity   :").append(dm.scaledDensity);
        sb.append("\nxdpi            :").append(dm.xdpi);
        sb.append("\nydpi            :").append(dm.ydpi);
        SFLogger.logD(sb.toString());

        density = dm.density;
        densityDpi = dm.densityDpi;
        scaleDensity = dm.scaledDensity;
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        densityDpiX = dm.xdpi;
        densityDpiY = dm.ydpi;
//        density         :3.5
//        densityDpi      :560
//        heightPixels    :2712
//        widthPixels     :1440
//        scaledDensity   :3.5
//        xdpi            :537.882
//        ydpi            :537.882


//        density         :1.0
//        densityDpi      :160
//        heightPixels    :1775
//        widthPixels     :1080
//        scaledDensity   :1.0
//        xdpi            :150.0
//        ydpi            :150.0
        isInitialed = true;
    }

    private int getStatusHeight(Activity activity) {
        int statusHeight = 0;
        Rect localRect = new Rect();
        activity.getWindow().getDecorView()
                .getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass
                        .getField("status_bar_height").get(localObject)
                        .toString());
                statusHeight = activity.getResources().getDimensionPixelSize(i5);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }

    public int dip2px(float dipValue) {
        return (int) (dipValue * density + 0.5f);
    }

    public int px2dip(float pxValue) {
        return (int) (pxValue / density + 0.5f);
    }

    public int sp2px(float dipValue) {
        return (int) (dipValue * scaleDensity + 0.5f);
    }

    public int px2sp(float pxValue) {
        return (int) (pxValue / scaleDensity + 0.5f);
    }


}
