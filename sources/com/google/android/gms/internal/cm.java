package com.google.android.gms.internal;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
/* loaded from: classes.dex */
public final class cm {
    public static final Handler hO = new Handler(Looper.getMainLooper());

    public static int a(Context context, int i) {
        return a(context.getResources().getDisplayMetrics(), i);
    }

    public static int a(DisplayMetrics displayMetrics, int i) {
        return (int) TypedValue.applyDimension(1, i, displayMetrics);
    }

    public static void a(ViewGroup viewGroup, x xVar, String str) {
        cn.q(str);
        a(viewGroup, xVar, str, -65536, -16777216);
    }

    private static void a(ViewGroup viewGroup, x xVar, String str, int i, int i2) {
        if (viewGroup.getChildCount() != 0) {
            return;
        }
        Context context = viewGroup.getContext();
        TextView textView = new TextView(context);
        textView.setGravity(17);
        textView.setText(str);
        textView.setTextColor(i);
        textView.setBackgroundColor(i2);
        FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.setBackgroundColor(i);
        int a = a(context, 3);
        frameLayout.addView(textView, new FrameLayout.LayoutParams(xVar.widthPixels - a, xVar.heightPixels - a, 17));
        viewGroup.addView(frameLayout, xVar.widthPixels, xVar.heightPixels);
    }

    public static boolean aq() {
        return Build.DEVICE.startsWith("generic");
    }

    public static boolean ar() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public static void b(ViewGroup viewGroup, x xVar, String str) {
        a(viewGroup, xVar, str, -16777216, -1);
    }

    public static String l(Context context) {
        return l((Settings.Secure.getString(context.getContentResolver(), "android_id") == null || aq()) ? "emulator" : "emulator");
    }

    public static String l(String str) {
        for (int i = 0; i < 2; i++) {
            try {
                MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                messageDigest.update(str.getBytes());
                return String.format(Locale.US, "%032X", new BigInteger(1, messageDigest.digest()));
            } catch (NoSuchAlgorithmException e) {
            }
        }
        return null;
    }
}
