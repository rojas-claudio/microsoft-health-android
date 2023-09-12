package com.microsoft.kapp.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import com.microsoft.kapp.KApplicationGraph;
import com.microsoft.kapp.R;
import com.microsoft.kapp.ThemeManager;
import javax.inject.Inject;
/* loaded from: classes.dex */
public abstract class BaseTile extends FrameLayout {
    private View.OnClickListener mOnClickListener;
    @Inject
    ThemeManager mThemeManager;

    public abstract int getBackgroundColor();

    public abstract String getTelemetryName();

    public BaseTile(Context context) {
        this(context, null);
    }

    public BaseTile(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.confirmationBarStyle);
    }

    public BaseTile(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mOnClickListener = null;
        KApplicationGraph.getApplicationGraph().inject(this);
    }

    @Override // android.view.View
    public void setOnClickListener(View.OnClickListener onClickListener) {
        super.setOnClickListener(onClickListener);
        this.mOnClickListener = onClickListener;
    }

    public void disableOnClickListener() {
        super.setOnClickListener(null);
    }

    public void enableOnClickListener() {
        super.setOnClickListener(this.mOnClickListener);
    }

    public boolean hasData() {
        return true;
    }
}
