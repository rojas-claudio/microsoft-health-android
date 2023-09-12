package com.google.android.gms.internal;

import android.app.Activity;
import android.content.Context;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import java.lang.ref.WeakReference;
/* loaded from: classes.dex */
public class es {
    protected em mz;
    protected a np;

    /* loaded from: classes.dex */
    public static final class a {
        public int bottom;
        public int gravity;
        public int left;
        public IBinder nq;
        public int nr;
        public int right;
        public int top;

        private a(int i, IBinder iBinder) {
            this.nr = -1;
            this.left = 0;
            this.top = 0;
            this.right = 0;
            this.bottom = 0;
            this.gravity = i;
            this.nq = iBinder;
        }

        public Bundle ca() {
            Bundle bundle = new Bundle();
            bundle.putInt("popupLocationInfo.gravity", this.gravity);
            bundle.putInt("popupLocationInfo.displayId", this.nr);
            bundle.putInt("popupLocationInfo.left", this.left);
            bundle.putInt("popupLocationInfo.top", this.top);
            bundle.putInt("popupLocationInfo.right", this.right);
            bundle.putInt("popupLocationInfo.bottom", this.bottom);
            return bundle;
        }
    }

    /* loaded from: classes.dex */
    private static final class b extends es implements View.OnAttachStateChangeListener, ViewTreeObserver.OnGlobalLayoutListener {
        private boolean mK;
        private WeakReference<View> ns;

        protected b(em emVar, int i) {
            super(emVar, i);
            this.mK = false;
        }

        private void f(View view) {
            Display display;
            int i = -1;
            if (ek.bO() && (display = view.getDisplay()) != null) {
                i = display.getDisplayId();
            }
            IBinder windowToken = view.getWindowToken();
            int[] iArr = new int[2];
            view.getLocationInWindow(iArr);
            int width = view.getWidth();
            int height = view.getHeight();
            this.np.nr = i;
            this.np.nq = windowToken;
            this.np.left = iArr[0];
            this.np.top = iArr[1];
            this.np.right = iArr[0] + width;
            this.np.bottom = iArr[1] + height;
            if (this.mK) {
                bX();
                this.mK = false;
            }
        }

        @Override // com.google.android.gms.internal.es
        protected void Q(int i) {
            this.np = new a(i, null);
        }

        @Override // com.google.android.gms.internal.es
        public void bX() {
            if (this.np.nq != null) {
                super.bX();
            } else {
                this.mK = this.ns != null;
            }
        }

        @Override // com.google.android.gms.internal.es
        public void e(View view) {
            this.mz.bT();
            if (this.ns != null) {
                View view2 = this.ns.get();
                Context context = this.mz.getContext();
                if (view2 == null && (context instanceof Activity)) {
                    view2 = ((Activity) context).getWindow().getDecorView();
                }
                if (view2 != null) {
                    view2.removeOnAttachStateChangeListener(this);
                    ViewTreeObserver viewTreeObserver = view2.getViewTreeObserver();
                    if (ek.bN()) {
                        viewTreeObserver.removeOnGlobalLayoutListener(this);
                    } else {
                        viewTreeObserver.removeGlobalOnLayoutListener(this);
                    }
                }
            }
            this.ns = null;
            Context context2 = this.mz.getContext();
            if (view == null && (context2 instanceof Activity)) {
                View findViewById = ((Activity) context2).findViewById(16908290);
                if (findViewById == null) {
                    findViewById = ((Activity) context2).getWindow().getDecorView();
                }
                ep.c("PopupManager", "You have not specified a View to use as content view for popups. Falling back to the Activity content view which may not work properly in future versions of the API. Use setViewForPopups() to set your content view.");
                view = findViewById;
            }
            if (view == null) {
                ep.d("PopupManager", "No content view usable to display popups. Popups will not be displayed in response to this client's calls. Use setViewForPopups() to set your content view.");
                return;
            }
            f(view);
            this.ns = new WeakReference<>(view);
            view.addOnAttachStateChangeListener(this);
            view.getViewTreeObserver().addOnGlobalLayoutListener(this);
        }

        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            View view;
            if (this.ns == null || (view = this.ns.get()) == null) {
                return;
            }
            f(view);
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewAttachedToWindow(View v) {
            f(v);
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewDetachedFromWindow(View v) {
            this.mz.bT();
            v.removeOnAttachStateChangeListener(this);
        }
    }

    private es(em emVar, int i) {
        this.mz = emVar;
        Q(i);
    }

    public static es a(em emVar, int i) {
        return ek.bK() ? new b(emVar, i) : new es(emVar, i);
    }

    protected void Q(int i) {
        this.np = new a(i, new Binder());
    }

    public void bX() {
        this.mz.a(this.np.nq, this.np.ca());
    }

    public Bundle bY() {
        return this.np.ca();
    }

    public IBinder bZ() {
        return this.np.nq;
    }

    public void e(View view) {
    }

    public void setGravity(int gravity) {
        this.np.gravity = gravity;
    }
}
