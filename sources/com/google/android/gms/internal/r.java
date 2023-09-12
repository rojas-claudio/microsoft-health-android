package com.google.android.gms.internal;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ViewSwitcher;
import com.google.android.gms.internal.ac;
import com.google.android.gms.internal.bp;
import com.google.android.gms.internal.bu;
/* loaded from: classes.dex */
public final class r extends ac.a implements ag, aq, bi, bl, bp.a, q {
    private final aw dZ;
    private final a ea;
    private final s eb = new s(this);

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class a {
        public final String adUnitId;
        public final ViewSwitcher ec;
        public final x ed;
        public final Context ee;
        public final h ef;
        public final co eg;
        public ab eh;
        public cg ei;
        public ce ej;
        public ae ek;

        public a(Context context, x xVar, String str, co coVar) {
            if (xVar.ex) {
                this.ec = null;
            } else {
                this.ec = new ViewSwitcher(context);
                this.ec.setMinimumWidth(xVar.widthPixels);
                this.ec.setMinimumHeight(xVar.heightPixels);
                this.ec.setVisibility(4);
            }
            this.ed = xVar;
            this.adUnitId = str;
            this.ee = context;
            this.ef = new h(g.a(coVar.hP, context));
            this.eg = coVar;
        }
    }

    public r(Context context, x xVar, String str, aw awVar, co coVar) {
        this.ea = new a(context, xVar, str, coVar);
        this.dZ = awVar;
        cn.o("Use AdRequest.Builder.addTestDevice(\"" + cm.l(context) + "\") to get test ads on this device.");
        ci.i(context);
    }

    private void I() {
        cn.o("Ad closing.");
        if (this.ea.eh != null) {
            try {
                this.ea.eh.onAdClosed();
            } catch (RemoteException e) {
                cn.b("Could not call AdListener.onAdClosed().", e);
            }
        }
    }

    private void J() {
        cn.o("Ad leaving application.");
        if (this.ea.eh != null) {
            try {
                this.ea.eh.onAdLeftApplication();
            } catch (RemoteException e) {
                cn.b("Could not call AdListener.onAdLeftApplication().", e);
            }
        }
    }

    private void K() {
        cn.o("Ad opening.");
        if (this.ea.eh != null) {
            try {
                this.ea.eh.onAdOpened();
            } catch (RemoteException e) {
                cn.b("Could not call AdListener.onAdOpened().", e);
            }
        }
    }

    private void L() {
        cn.o("Ad finished loading.");
        if (this.ea.eh != null) {
            try {
                this.ea.eh.onAdLoaded();
            } catch (RemoteException e) {
                cn.b("Could not call AdListener.onAdLoaded().", e);
            }
        }
    }

    private boolean M() {
        boolean z = true;
        if (!ci.a(this.ea.ee.getPackageManager(), this.ea.ee.getPackageName(), "android.permission.INTERNET")) {
            if (!this.ea.ed.ex) {
                cm.a(this.ea.ec, this.ea.ed, "Missing internet permission in AndroidManifest.xml.");
            }
            z = false;
        }
        if (!ci.h(this.ea.ee)) {
            if (!this.ea.ed.ex) {
                cm.a(this.ea.ec, this.ea.ed, "Missing AdActivity with android:configChanges in AndroidManifest.xml.");
            }
            z = false;
        }
        if (!z) {
            this.ea.ec.setVisibility(0);
        }
        return z;
    }

    private void N() {
        if (this.ea.ej == null) {
            cn.q("Ad state was null when trying to ping click URLs.");
            return;
        }
        cn.m("Pinging click URLs.");
        if (this.ea.ej.eW != null) {
            ci.a(this.ea.ee, this.ea.eg.hP, this.ea.ej.eW);
        }
        if (this.ea.ej.hA == null || this.ea.ej.hA.eW == null) {
            return;
        }
        au.a(this.ea.ee, this.ea.eg.hP, this.ea.ej, this.ea.adUnitId, false, this.ea.ej.hA.eW);
    }

    private void O() {
        if (this.ea.ej != null) {
            this.ea.ej.fU.destroy();
            this.ea.ej = null;
        }
    }

    private void a(int i) {
        cn.q("Failed to load ad: " + i);
        if (this.ea.eh != null) {
            try {
                this.ea.eh.onAdFailedToLoad(i);
            } catch (RemoteException e) {
                cn.b("Could not call AdListener.onAdFailedToLoad().", e);
            }
        }
    }

    private void b(View view) {
        this.ea.ec.addView(view, new ViewGroup.LayoutParams(-2, -2));
    }

    private void b(boolean z) {
        if (this.ea.ej == null) {
            cn.q("Ad state was null when trying to ping impression URLs.");
            return;
        }
        cn.m("Pinging Impression URLs.");
        if (this.ea.ej.eX != null) {
            ci.a(this.ea.ee, this.ea.eg.hP, this.ea.ej.eX);
        }
        if (this.ea.ej.hA != null && this.ea.ej.hA.eX != null) {
            au.a(this.ea.ee, this.ea.eg.hP, this.ea.ej, this.ea.adUnitId, z, this.ea.ej.hA.eX);
        }
        if (this.ea.ej.fm == null || this.ea.ej.fm.eT == null) {
            return;
        }
        au.a(this.ea.ee, this.ea.eg.hP, this.ea.ej, this.ea.adUnitId, z, this.ea.ej.fm.eT);
    }

    private boolean b(ce ceVar) {
        if (ceVar.gI) {
            try {
                View view = (View) com.google.android.gms.dynamic.c.b(ceVar.fn.getView());
                View nextView = this.ea.ec.getNextView();
                if (nextView != null) {
                    this.ea.ec.removeView(nextView);
                }
                try {
                    b(view);
                } catch (Throwable th) {
                    cn.b("Could not add mediation view to view hierarchy.", th);
                    return false;
                }
            } catch (RemoteException e) {
                cn.b("Could not get View from mediation adapter.", e);
                return false;
            }
        }
        if (this.ea.ec.getChildCount() > 1) {
            this.ea.ec.showNext();
        }
        if (this.ea.ej != null) {
            View nextView2 = this.ea.ec.getNextView();
            if (nextView2 instanceof cq) {
                ((cq) nextView2).a(this.ea.ee, this.ea.ed);
            } else if (nextView2 != null) {
                this.ea.ec.removeView(nextView2);
            }
            if (this.ea.ej.fn != null) {
                try {
                    this.ea.ej.fn.destroy();
                } catch (RemoteException e2) {
                    cn.q("Could not destroy previous mediation adapter.");
                }
            }
        }
        this.ea.ec.setVisibility(0);
        return true;
    }

    private bu.a c(v vVar) {
        PackageInfo packageInfo;
        Bundle bundle;
        ApplicationInfo applicationInfo = this.ea.ee.getApplicationInfo();
        try {
            packageInfo = this.ea.ee.getPackageManager().getPackageInfo(applicationInfo.packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
        }
        if (this.ea.ed.ex || this.ea.ec.getParent() == null) {
            bundle = null;
        } else {
            int[] iArr = new int[2];
            this.ea.ec.getLocationOnScreen(iArr);
            int i = iArr[0];
            int i2 = iArr[1];
            DisplayMetrics displayMetrics = this.ea.ee.getResources().getDisplayMetrics();
            int width = this.ea.ec.getWidth();
            int height = this.ea.ec.getHeight();
            int i3 = (!this.ea.ec.isShown() || i + width <= 0 || i2 + height <= 0 || i > displayMetrics.widthPixels || i2 > displayMetrics.heightPixels) ? 0 : 1;
            bundle = new Bundle(5);
            bundle.putInt("x", i);
            bundle.putInt("y", i2);
            bundle.putInt("width", width);
            bundle.putInt("height", height);
            bundle.putInt("visible", i3);
        }
        return new bu.a(bundle, vVar, this.ea.ed, this.ea.adUnitId, applicationInfo, packageInfo, cf.al(), cf.hB, this.ea.eg);
    }

    @Override // com.google.android.gms.internal.bl
    public void A() {
        J();
    }

    @Override // com.google.android.gms.internal.bi
    public void B() {
        if (this.ea.ed.ex) {
            O();
        }
        I();
    }

    @Override // com.google.android.gms.internal.bi
    public void C() {
        if (this.ea.ed.ex) {
            b(false);
        }
        K();
    }

    @Override // com.google.android.gms.internal.aq
    public void D() {
        y();
    }

    @Override // com.google.android.gms.internal.aq
    public void E() {
        B();
    }

    @Override // com.google.android.gms.internal.aq
    public void F() {
        A();
    }

    @Override // com.google.android.gms.internal.aq
    public void G() {
        C();
    }

    @Override // com.google.android.gms.internal.aq
    public void H() {
        if (this.ea.ej != null) {
            cn.q("Mediation adapter " + this.ea.ej.fo + " refreshed, but mediation adapters should never refresh.");
        }
        b(true);
        L();
    }

    @Override // com.google.android.gms.internal.ac
    public void a(ab abVar) {
        dm.w("setAdListener must be called on the main UI thread.");
        this.ea.eh = abVar;
    }

    @Override // com.google.android.gms.internal.ac
    public void a(ae aeVar) {
        dm.w("setAppEventListener must be called on the main UI thread.");
        this.ea.ek = aeVar;
    }

    @Override // com.google.android.gms.internal.bp.a
    public void a(ce ceVar) {
        this.ea.ei = null;
        if (ceVar.errorCode == -1) {
            return;
        }
        boolean z = ceVar.gB.extras != null ? ceVar.gB.extras.getBoolean("_noRefresh", false) : false;
        if (this.ea.ed.ex) {
            ci.a(ceVar.fU);
        } else if (!z) {
            if (ceVar.fa > 0) {
                this.eb.a(ceVar.gB, ceVar.fa);
            } else if (ceVar.hA != null && ceVar.hA.fa > 0) {
                this.eb.a(ceVar.gB, ceVar.hA.fa);
            } else if (!ceVar.gI && ceVar.errorCode == 2) {
                this.eb.d(ceVar.gB);
            }
        }
        if (ceVar.errorCode == 3 && ceVar.hA != null && ceVar.hA.eY != null) {
            cn.m("Pinging no fill URLs.");
            au.a(this.ea.ee, this.ea.eg.hP, ceVar, this.ea.adUnitId, false, ceVar.hA.eY);
        }
        if (ceVar.errorCode != -2) {
            a(ceVar.errorCode);
        } else if (!this.ea.ed.ex && !b(ceVar)) {
            a(0);
        } else {
            if (this.ea.ej != null && this.ea.ej.fp != null) {
                this.ea.ej.fp.a((aq) null);
            }
            if (ceVar.fp != null) {
                ceVar.fp.a(this);
            }
            this.ea.ej = ceVar;
            if (!this.ea.ed.ex) {
                b(false);
            }
            L();
        }
    }

    @Override // com.google.android.gms.internal.ag
    public void a(String str, String str2) {
        if (this.ea.ek != null) {
            try {
                this.ea.ek.a(str, str2);
            } catch (RemoteException e) {
                cn.b("Could not call the AppEventListener.", e);
            }
        }
    }

    @Override // com.google.android.gms.internal.ac
    public boolean a(v vVar) {
        cq a2;
        cq cqVar;
        dm.w("loadAd must be called on the main UI thread.");
        if (this.ea.ei != null) {
            cn.q("An ad request is already in progress. Aborting.");
            return false;
        } else if (this.ea.ed.ex && this.ea.ej != null) {
            cn.q("An interstitial is already loading. Aborting.");
            return false;
        } else if (M()) {
            cn.o("Starting ad request.");
            this.eb.cancel();
            bu.a c = c(vVar);
            if (this.ea.ed.ex) {
                cq a3 = cq.a(this.ea.ee, this.ea.ed, false, false, this.ea.ef, this.ea.eg);
                a3.aw().a(this, null, this, this, true);
                cqVar = a3;
            } else {
                View nextView = this.ea.ec.getNextView();
                if (nextView instanceof cq) {
                    a2 = (cq) nextView;
                    a2.a(this.ea.ee, this.ea.ed);
                } else {
                    if (nextView != null) {
                        this.ea.ec.removeView(nextView);
                    }
                    a2 = cq.a(this.ea.ee, this.ea.ed, false, false, this.ea.ef, this.ea.eg);
                    b(a2);
                }
                a2.aw().a(this, this, this, this, false);
                cqVar = a2;
            }
            this.ea.ei = bp.a(this.ea.ee, c, this.ea.ef, cqVar, this.dZ, this);
            return true;
        } else {
            return false;
        }
    }

    public void b(v vVar) {
        ViewParent parent = this.ea.ec.getParent();
        if ((parent instanceof View) && ((View) parent).isShown() && ci.am()) {
            a(vVar);
            return;
        }
        cn.o("Ad is not visible. Not refreshing ad.");
        this.eb.d(vVar);
    }

    @Override // com.google.android.gms.internal.ac
    public void destroy() {
        dm.w("destroy must be called on the main UI thread.");
        this.ea.eh = null;
        this.ea.ek = null;
        this.eb.cancel();
        stopLoading();
        if (this.ea.ec != null) {
            this.ea.ec.removeAllViews();
        }
        if (this.ea.ej == null || this.ea.ej.fU == null) {
            return;
        }
        this.ea.ej.fU.destroy();
    }

    @Override // com.google.android.gms.internal.ac
    public boolean isReady() {
        dm.w("isReady must be called on the main UI thread.");
        return this.ea.ei == null && this.ea.ej != null;
    }

    @Override // com.google.android.gms.internal.ac
    public void pause() {
        dm.w("pause must be called on the main UI thread.");
        if (this.ea.ej != null) {
            ci.a(this.ea.ej.fU);
        }
    }

    @Override // com.google.android.gms.internal.ac
    public void resume() {
        dm.w("resume must be called on the main UI thread.");
        if (this.ea.ej != null) {
            ci.b(this.ea.ej.fU);
        }
    }

    @Override // com.google.android.gms.internal.ac
    public void showInterstitial() {
        dm.w("showInterstitial must be called on the main UI thread.");
        if (!this.ea.ed.ex) {
            cn.q("Cannot call showInterstitial on a banner ad.");
        } else if (this.ea.ej == null) {
            cn.q("The interstitial has not loaded.");
        } else if (this.ea.ej.fU.az()) {
            cn.q("The interstitial is already showing.");
        } else {
            this.ea.ej.fU.i(true);
            if (!this.ea.ej.gI) {
                bf.a(this.ea.ee, new bh(this, this, this, this.ea.ej.fU, this.ea.ej.orientation, this.ea.eg));
                return;
            }
            try {
                this.ea.ej.fn.showInterstitial();
            } catch (RemoteException e) {
                cn.b("Could not show interstitial.", e);
                O();
            }
        }
    }

    @Override // com.google.android.gms.internal.ac
    public void stopLoading() {
        dm.w("stopLoading must be called on the main UI thread.");
        if (this.ea.ej != null) {
            this.ea.ej.fU.stopLoading();
            this.ea.ej = null;
        }
        if (this.ea.ei != null) {
            this.ea.ei.cancel();
        }
    }

    @Override // com.google.android.gms.internal.q
    public void y() {
        N();
    }

    @Override // com.google.android.gms.internal.ac
    public com.google.android.gms.dynamic.b z() {
        dm.w("getAdFrame must be called on the main UI thread.");
        return com.google.android.gms.dynamic.c.g(this.ea.ec);
    }
}
