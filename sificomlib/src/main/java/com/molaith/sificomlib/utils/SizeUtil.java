package com.molaith.sificomlib.utils;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by molaith on 2017/4/18.
 */

public class SizeUtil {
    private static final String TAG = SizeUtil.class.getSimpleName();

    public static float measureTextHeight(TextView textView, String text) {
        TextPaint textPaint = textView.getPaint();
        Rect rect = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), rect);
        return rect.height();
    }

    private static int dealScaledWidth(float baseDensity, float realDensity, int size, float reference) {
        int realDip = (int) (size / realDensity);
        int sizeExpact = (int) (realDip * baseDensity);
        int scaledSize = (int) (sizeExpact * reference);
        Log.d("molaith", "realWidthDip: " + realDip);
        Log.d("molaith", "widthExpact: " + sizeExpact);

        Log.d("molaith", "width: " + size);
        Log.d("molaith", "scaledWidth: " + scaledSize);
        return scaledSize;
    }

    /**
     * @param context
     * @param text
     * @param textSizeSP
     * @return
     */
    public static float measureTextHeight(Context context, String text, float textSizeSP) {
        TextView textView = new TextView(context);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(lp);
        textView.setTextSize(textSizeSP);
        return measureTextWidth(textView, text);
    }

    /**
     * 英文字符计算出来的值比measureTextHeight(TextView,text)计算出来的值大，中文字符没有此现象
     *
     * @param text
     * @param textSizeInPixel
     * @return
     */
    public static float measureTextHeight(String text, float textSizeInPixel) {
        Paint paint = new Paint();
        paint.setTextSize(textSizeInPixel);
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect.height();
    }

    /**
     * @param textView
     * @param text
     * @return
     */
    public static float measureTextWidth(TextView textView, String text) {
        TextPaint textPaint = textView.getPaint();
        return textPaint.measureText(text);
    }

    /**
     * @param context
     * @param text
     * @param textSizeSP
     * @return
     */
    public static float measureTextWidth(Context context, String text, float textSizeSP) {
        TextView textView = new TextView(context);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(lp);
        textView.setTextSize(textSizeSP);
        return measureTextWidth(textView, text);
    }

    /**
     * 英文字符计算出来的值比measureTextWidth(TextView,text)计算出来的值大，中文字符没有此现象
     *
     * @param text
     * @param textSizeInPixel
     * @return
     */
    public static float measureTextWidth(String text, float textSizeInPixel) {
        Paint paint = new Paint();
        paint.setTextSize(textSizeInPixel);
        return paint.measureText(text);
    }
}
