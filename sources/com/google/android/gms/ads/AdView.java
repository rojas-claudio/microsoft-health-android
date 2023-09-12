package com.google.android.gms.ads;

import android.content.Context;
import android.os.RemoteException;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.dynamic.b;
import com.google.android.gms.dynamic.c;
import com.google.android.gms.internal.aa;
import com.google.android.gms.internal.ac;
import com.google.android.gms.internal.av;
import com.google.android.gms.internal.cm;
import com.google.android.gms.internal.cn;
import com.google.android.gms.internal.t;
import com.google.android.gms.internal.u;
import com.google.android.gms.internal.v;
import com.google.android.gms.internal.x;
import com.google.android.gms.internal.z;
/* loaded from: classes.dex */
public final class AdView extends ViewGroup {
    private AdSize c;
    private final av dS;
    private AdListener dT;
    private ac dU;
    private String dV;
    private a dW;

    public AdView(Context context) {
        super(context);
        this.dS = new av();
    }

    public AdView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AdView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.dS = new av();
        try {
            aa aaVar = new aa(context, attrs);
            this.c = aaVar.getAdSize();
            this.dV = aaVar.getAdUnitId();
            if (isInEditMode()) {
                cm.b(this, new x(context, this.c), "Ads by Google");
            }
        } catch (IllegalArgumentException e) {
            cm.a(this, new x(context, AdSize.BANNER), e.getMessage());
        }
    }

    private void c(String str) throws RemoteException {
        if (this.c == null || this.dV == null) {
            d(str);
        }
        Context context = getContext();
        this.dU = u.a(context, new x(context, this.c), this.dV, this.dS);
        if (this.dT != null) {
            this.dU.a(new t(this.dT));
        }
        if (this.dW != null) {
            this.dU.a(new z(this.dW));
        }
        x();
    }

    private void d(String str) {
        if (this.dU == null) {
            throw new IllegalStateException("The ad size and ad unit ID must be set on AdView before " + str + " is called.");
        }
    }

    private void x() {
        try {
            b z = this.dU.z();
            if (z == null) {
                return;
            }
            addView((View) c.b(z));
        } catch (RemoteException e) {
            cn.b("Failed to get an ad frame.", e);
        }
    }

    public void destroy() {
        try {
            if (this.dU != null) {
                this.dU.destroy();
            }
        } catch (RemoteException e) {
            cn.b("Failed to destroy AdView.", e);
        }
    }

    public AdListener getAdListener() {
        return this.dT;
    }

    public AdSize getAdSize() {
        return this.c;
    }

    public String getAdUnitId() {
        return this.dV;
    }

    public void loadAd(AdRequest adRequest) {
        try {
            if (this.dU == null) {
                c("loadAd");
            }
            if (this.dU.a(new v(getContext(), adRequest))) {
                this.dS.c(adRequest.v());
            }
        } catch (RemoteException e) {
            cn.b("Failed to load ad.", e);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        View childAt = getChildAt(0);
        if (childAt == null || childAt.getVisibility() == 8) {
            return;
        }
        int measuredWidth = childAt.getMeasuredWidth();
        int measuredHeight = childAt.getMeasuredHeight();
        int i = ((right - left) - measuredWidth) / 2;
        int i2 = ((bottom - top) - measuredHeight) / 2;
        childAt.layout(i, i2, measuredWidth + i, measuredHeight + i2);
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int i;
        int i2 = 0;
        View childAt = getChildAt(0);
        if (childAt != null && childAt.getVisibility() != 8) {
            measureChild(childAt, widthMeasureSpec, heightMeasureSpec);
            i = childAt.getMeasuredWidth();
            i2 = childAt.getMeasuredHeight();
        } else if (this.c != null) {
            Context context = getContext();
            i = this.c.getWidthInPixels(context);
            i2 = this.c.getHeightInPixels(context);
        } else {
            i = 0;
        }
        setMeasuredDimension(resolveSize(Math.max(i, getSuggestedMinimumWidth()), widthMeasureSpec), resolveSize(Math.max(i2, getSuggestedMinimumHeight()), heightMeasureSpec));
    }

    public void pause() {
        try {
            if (this.dU != null) {
                this.dU.pause();
            }
        } catch (RemoteException e) {
            cn.b("Failed to call pause.", e);
        }
    }

    public void resume() {
        try {
            if (this.dU != null) {
                this.dU.resume();
            }
        } catch (RemoteException e) {
            cn.b("Failed to call resume.", e);
        }
    }

    public void setAdListener(AdListener adListener) {
        try {
            this.dT = adListener;
            if (this.dU != null) {
                this.dU.a(adListener != null ? new t(adListener) : null);
            }
        } catch (RemoteException e) {
            cn.b("Failed to set the AdListener.", e);
        }
    }

    public void setAdSize(AdSize adSize) {
        if (this.c != null) {
            throw new IllegalStateException("The ad size can only be set once on AdView.");
        }
        this.c = adSize;
        requestLayout();
    }

    public void setAdUnitId(String adUnitId) {
        if (this.dV != null) {
            throw new IllegalStateException("The ad unit ID can only be set once on AdView.");
        }
        this.dV = adUnitId;
    }
}
