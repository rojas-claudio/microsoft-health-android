package com.google.android.gms.plus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import com.google.android.gms.internal.dm;
import com.google.android.gms.internal.dp;
import com.google.android.gms.internal.fm;
/* loaded from: classes.dex */
public final class PlusOneButton extends FrameLayout {
    public static final int ANNOTATION_BUBBLE = 1;
    public static final int ANNOTATION_INLINE = 2;
    public static final int ANNOTATION_NONE = 0;
    public static final int DEFAULT_ACTIVITY_REQUEST_CODE = -1;
    public static final int SIZE_MEDIUM = 1;
    public static final int SIZE_SMALL = 0;
    public static final int SIZE_STANDARD = 3;
    public static final int SIZE_TALL = 2;
    private String hN;
    private int mSize;
    private View re;
    private int rf;
    private int rg;
    private OnPlusOneClickListener rh;

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public class DefaultOnPlusOneClickListener implements View.OnClickListener, OnPlusOneClickListener {
        private final OnPlusOneClickListener ri;

        public DefaultOnPlusOneClickListener(OnPlusOneClickListener proxy) {
            this.ri = proxy;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            Intent intent = (Intent) PlusOneButton.this.re.getTag();
            if (this.ri != null) {
                this.ri.onPlusOneClick(intent);
            } else {
                onPlusOneClick(intent);
            }
        }

        @Override // com.google.android.gms.plus.PlusOneButton.OnPlusOneClickListener
        public void onPlusOneClick(Intent intent) {
            Context context = PlusOneButton.this.getContext();
            if (!(context instanceof Activity) || intent == null) {
                return;
            }
            ((Activity) context).startActivityForResult(intent, PlusOneButton.this.rg);
        }
    }

    /* loaded from: classes.dex */
    public interface OnPlusOneClickListener {
        void onPlusOneClick(Intent intent);
    }

    public PlusOneButton(Context context) {
        this(context, null);
    }

    public PlusOneButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mSize = getSize(context, attrs);
        this.rf = getAnnotation(context, attrs);
        this.rg = -1;
        p(getContext());
        if (isInEditMode()) {
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static int getAnnotation(Context context, AttributeSet attrs) {
        String a = dp.a("http://schemas.android.com/apk/lib/com.google.android.gms.plus", "annotation", context, attrs, true, false, "PlusOneButton");
        if ("INLINE".equalsIgnoreCase(a)) {
            return 2;
        }
        return !"NONE".equalsIgnoreCase(a) ? 1 : 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static int getSize(Context context, AttributeSet attrs) {
        String a = dp.a("http://schemas.android.com/apk/lib/com.google.android.gms.plus", "size", context, attrs, true, false, "PlusOneButton");
        if ("SMALL".equalsIgnoreCase(a)) {
            return 0;
        }
        if ("MEDIUM".equalsIgnoreCase(a)) {
            return 1;
        }
        return "TALL".equalsIgnoreCase(a) ? 2 : 3;
    }

    private void p(Context context) {
        if (this.re != null) {
            removeView(this.re);
        }
        this.re = fm.a(context, this.mSize, this.rf, this.hN, this.rg);
        setOnPlusOneClickListener(this.rh);
        addView(this.re);
    }

    public void initialize(String url, int activityRequestCode) {
        dm.a(getContext() instanceof Activity, "To use this method, the PlusOneButton must be placed in an Activity. Use initialize(PlusClient, String, OnPlusOneClickListener).");
        this.hN = url;
        this.rg = activityRequestCode;
        p(getContext());
    }

    public void initialize(String url, OnPlusOneClickListener plusOneClickListener) {
        this.hN = url;
        this.rg = 0;
        p(getContext());
        setOnPlusOneClickListener(plusOneClickListener);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        this.re.layout(0, 0, right - left, bottom - top);
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        View view = this.re;
        measureChild(view, widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(view.getMeasuredWidth(), view.getMeasuredHeight());
    }

    public void setAnnotation(int annotation) {
        this.rf = annotation;
        p(getContext());
    }

    public void setOnPlusOneClickListener(OnPlusOneClickListener listener) {
        this.rh = listener;
        this.re.setOnClickListener(new DefaultOnPlusOneClickListener(listener));
    }

    public void setSize(int size) {
        this.mSize = size;
        p(getContext());
    }
}
