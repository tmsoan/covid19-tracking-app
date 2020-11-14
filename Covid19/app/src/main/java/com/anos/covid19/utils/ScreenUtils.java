package com.anos.covid19.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

public class ScreenUtils {
    static int BASE_WIDTH = 360;
    static int SCREEN_HEIGHT = 0;
    static int SCREEN_WIDTH = 0;

    public static int[] getScreenSize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(
                Context.WINDOW_SERVICE);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displaymetrics);

        SCREEN_HEIGHT = displaymetrics.heightPixels;
        SCREEN_WIDTH = displaymetrics.widthPixels;
        return new int[]{SCREEN_WIDTH, SCREEN_HEIGHT};
    }

    public static int getScreenWidth() {
        return SCREEN_WIDTH;
    }

    public static int getScreenHeight() {
        return SCREEN_HEIGHT;
    }

    public static void showSoftKeyboard(Context context, View v) {
        InputMethodManager imm =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
    }

    public static void hideSoftKeyboard(Context context, View v) {
        if (context != null) {
            InputMethodManager imm =
                    (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    public static float dpToPx(Context context, float dp) {
        if (context == null) {
            return -1;
        }
        return dp * context.getResources().getDisplayMetrics().density + 0.5f;
    }

    public static float pxToDp(Context context, float px) {
        if (context == null) {
            return -1;
        }
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static int dpToPxInt(Context context, float dp) {
        return (int) (dpToPx(context, dp) + 0.5f);
    }


    public static int getWidthItemByScreenW(int widthInDP) {
        return SCREEN_WIDTH * widthInDP / BASE_WIDTH;
    }

    public static ViewGroup.LayoutParams setLayoutSize(View view, float widthDp, float heightDp) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = ScreenUtils.dpToPxInt(view.getContext(), widthDp);
        layoutParams.height = ScreenUtils.dpToPxInt(view.getContext(), heightDp);
        view.requestLayout();
        return layoutParams;
    }
}
