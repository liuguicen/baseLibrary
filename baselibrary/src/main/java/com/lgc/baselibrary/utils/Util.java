package com.lgc.baselibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Looper;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.lgc.baselibrary.baseComponent.BaseApplication;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created 0 Administrator on 2016/5/19.
 */
public class Util {

    public static int dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int dp2Px(float dp) {
        final float scale = BaseApplication.appContext.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int px2Dp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    public static int px2Dp(float px) {
        final float scale = BaseApplication.appContext.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    public static Drawable getDrawable(int id) {
        return ContextCompat.getDrawable(BaseApplication.appContext, id);
    }

    /**
     * @return 返回字符串的最后一个数字，没找到返回-1
     */
    public static int lastDigit(String s) {
        for (int i = s.length() - 1; i >= 0; i--) {
            if ('0' <= s.charAt(i) && s.charAt(i) <= '9') {
                return i;
            }
        }
        return -1;
    }

    /**
     * 点是否在View内部,注意参数值都是绝对坐标
     */
    public static boolean pointInView(float x, float y, View view) {
        if (view == null) return false;
        int[] xy = new int[2];
        view.getLocationOnScreen(xy);
        Rect bound = new Rect(xy[0], xy[1], xy[0] + view.getWidth(), xy[1] + view.getHeight());
        return bound.contains((int) (x + 0.5f), (int) (y + 0.5f));
    }

    /**
     * 隐藏输入法
     */
    public static void hideInputMethod(Context context, TextView tv) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(tv.getApplicationWindowToken(), 0);
    }

    public static final void hideInputMethod(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View focusView = activity.getCurrentFocus();
        if (focusView != null) {
            imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0); //强制隐藏键盘
        }
    }

    public static void showInputMethod(final View v) {
        showKeyBoard(v);
    }

    public static void showKeyBoard(final View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
        }
    }

    //显示虚拟键盘
    public static void showKeyboard(final View v, long delay)
    {
        if (delay > 0) {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                public void run() {
                    showKeyBoard(v);
                }
            }, delay);
        } else {
            showKeyBoard(v);
        }
    }

    //隐藏虚拟键盘
    public static void hideKeyboard(View v)
    {
        InputMethodManager imm = ( InputMethodManager ) v.getContext( ).getSystemService( Context.INPUT_METHOD_SERVICE );
        if ( imm.isActive( ) ) {
            imm.hideSoftInputFromWindow( v.getApplicationWindowToken( ) , 0 );

        }
    }

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void showToast(String msg) {
        Toast.makeText(BaseApplication.appContext, msg, Toast.LENGTH_LONG).show();
    }

    public static void T(Context context, Object s) {
        Toast.makeText(context, s.toString(), Toast.LENGTH_LONG).show();
    }

    /**
     * 把long 转换成 日期 再转换成String类型
     * transferLongToDate("yyyy-MM-dd HH:mm:ss",1245678944);
     */
   public static String long2Date(String dateFormat, Long millSec) {
       SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
       Date date = new Date(millSec);
       return sdf.format(date);
   }

    // 获取Map中的Value的Set
    public static <T> Set<T> map2set(Map<?, T> map) {
        Set<T> set = new LinkedHashSet<>();
        for (Map.Entry<?, T> entry : map.entrySet()) {
            set.add(entry.getValue());
        }
        return set;
    }

    /**
     * Created by Administrator on 2016/5/8.
     * 用于判断重复点击的，可以设置间隔时间
     */
    public static class DoubleClick {
        public static long sLastTime = -1;

        /**
         * 自定义的
         */
        public long mSelfDefine_lastTime;
        private final long mSelfDefine_TimeInterval;

        /**
         * 采用默认时间间隔
         */
        public static boolean isDoubleClick() {
            long curTime = System.currentTimeMillis();
            //貌似系统定义的双击正是300毫秒 ViewConfiguration.getDoubleTapTimeout()
            if (curTime - sLastTime < ViewConfiguration.getDoubleTapTimeout()) {
                sLastTime = curTime;
                return true;
            } else {
                sLastTime = curTime;
                return false;
            }
        }

        public static boolean isDoubleClick(long interval) {
            long curTime = System.currentTimeMillis();
            if (curTime - sLastTime < interval) {
                sLastTime = curTime;
                return true;
            } else {
                sLastTime = curTime;
                return false;
            }
        }

        public static void cancel() {
            sLastTime = -1;
        }

        /**
         * 自定义双击间隔时间
         *
         * @param time
         */
        public DoubleClick(long time) {
            mSelfDefine_lastTime = -1;
            mSelfDefine_TimeInterval = time;
        }

        public boolean isDoubleClick_m() {
            long curTime = System.currentTimeMillis();
            //貌似系统定义的双击正是300毫秒 ViewConfiguration.getDoubleTapTimeout()
            if (curTime - mSelfDefine_lastTime < mSelfDefine_TimeInterval) {
                mSelfDefine_lastTime = curTime;
                return true;
            } else {
                mSelfDefine_lastTime = curTime;
                return false;
            }
        }
    }

    public static void getMesureWH(View v, int[] WH) {
        int width = View.MeasureSpec.makeMeasureSpec((1 << 30) - 1, View.MeasureSpec.AT_MOST);
        int height = View.MeasureSpec.makeMeasureSpec((1 << 30) - 1, View.MeasureSpec.AT_MOST);
        v.measure(width, height);
        WH[0] = v.getMeasuredWidth();
        WH[1] = v.getMeasuredHeight();
    }

    /**
     * 获取两点间的位置
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    float getDis(float x1, float y1, float x2, float y2) {
        float dx = x1 - x2, dy = y1 - y2;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    public static int getColor(@NonNull Context context, @ColorRes int id) {
        return ContextCompat.getColor(context, id);
    }

    public static int getColor(@ColorRes int id) {
        return ContextCompat.getColor(BaseApplication.appContext, id);
    }

    public static Drawable getStateDrawable(Drawable src, ColorStateList colors, PorterDuff.Mode mode) {
        Drawable drawable = DrawableCompat.wrap(src);
        DrawableCompat.setTintList(drawable, colors);
        DrawableCompat.setTintMode(drawable, mode);
        return drawable;
    }

    public static boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    public static void fadeOut(View view, long time, Animation.AnimationListener ani) {
        if (view.getVisibility() != View.VISIBLE) return;

        // Since the button is still clickable before fade-out animation
        // ends, we disable the button first to block click.
        // view.setEnabled(false);
        Animation animation = new AlphaAnimation(1F, 0.5F);
        animation.setAnimationListener(ani);
        animation.setDuration(time);
        view.startAnimation(animation);
        // view.setVisibility(View.GONE);
    }

    public static boolean ismMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }
}
