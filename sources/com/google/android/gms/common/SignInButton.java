package com.google.android.gms.common;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import com.google.android.gms.dynamic.e;
import com.google.android.gms.internal.Cdo;
import com.google.android.gms.internal.dm;
import com.google.android.gms.internal.dn;
/* loaded from: classes.dex */
public final class SignInButton extends FrameLayout implements View.OnClickListener {
    public static final int COLOR_DARK = 0;
    public static final int COLOR_LIGHT = 1;
    public static final int SIZE_ICON_ONLY = 2;
    public static final int SIZE_STANDARD = 0;
    public static final int SIZE_WIDE = 1;
    private int jc;
    private View jd;
    private View.OnClickListener je;
    private int mSize;

    public SignInButton(Context context) {
        this(context, null);
    }

    public SignInButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SignInButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.je = null;
        setStyle(0, 0);
    }

    private static Button c(Context context, int i, int i2) {
        Cdo cdo = new Cdo(context);
        cdo.a(context.getResources(), i, i2);
        return cdo;
    }

    private void p(Context context) {
        if (this.jd != null) {
            removeView(this.jd);
        }
        try {
            this.jd = dn.d(context, this.mSize, this.jc);
        } catch (e.a e) {
            Log.w("SignInButton", "Sign in button not found, using placeholder instead");
            this.jd = c(context, this.mSize, this.jc);
        }
        addView(this.jd);
        this.jd.setEnabled(isEnabled());
        this.jd.setOnClickListener(this);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (this.je == null || view != this.jd) {
            return;
        }
        this.je.onClick(this);
    }

    public void setColorScheme(int colorScheme) {
        setStyle(this.mSize, colorScheme);
    }

    @Override // android.view.View
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        this.jd.setEnabled(enabled);
    }

    @Override // android.view.View
    public void setOnClickListener(View.OnClickListener listener) {
        this.je = listener;
        if (this.jd != null) {
            this.jd.setOnClickListener(this);
        }
    }

    public void setSize(int buttonSize) {
        setStyle(buttonSize, this.jc);
    }

    public void setStyle(int buttonSize, int colorScheme) {
        boolean z = true;
        dm.a(buttonSize >= 0 && buttonSize < 3, "Unknown button size " + buttonSize);
        if (colorScheme < 0 || colorScheme >= 2) {
            z = false;
        }
        dm.a(z, "Unknown color scheme " + colorScheme);
        this.mSize = buttonSize;
        this.jc = colorScheme;
        p(getContext());
    }
}
