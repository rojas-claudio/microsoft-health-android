package com.google.android.gms.internal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import com.google.android.gms.ads.AdActivity;
import com.google.android.gms.internal.bn;
import com.google.android.gms.internal.cr;
/* loaded from: classes.dex */
public final class bf extends bn.a {
    private final Activity fD;
    private bh fE;
    private bj fF;
    private cq fG;
    private b fH;
    private bk fI;
    private FrameLayout fJ;
    private WebChromeClient.CustomViewCallback fK;
    private boolean fL = false;
    private boolean fM = false;
    private RelativeLayout fN;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class a extends Exception {
        public a(String str) {
            super(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class b {
        public final ViewGroup.LayoutParams fP;
        public final ViewGroup fQ;
        public final int index;

        public b(cq cqVar) throws a {
            this.fP = cqVar.getLayoutParams();
            ViewParent parent = cqVar.getParent();
            if (!(parent instanceof ViewGroup)) {
                throw new a("Could not get the parent of the WebView for an overlay.");
            }
            this.fQ = (ViewGroup) parent;
            this.index = this.fQ.indexOfChild(cqVar);
            this.fQ.removeView(cqVar);
            cqVar.i(true);
        }
    }

    public bf(Activity activity) {
        this.fD = activity;
    }

    private void T() {
        if (!this.fD.isFinishing() || this.fM) {
            return;
        }
        this.fM = true;
        if (this.fD.isFinishing()) {
            if (this.fG != null) {
                this.fG.as();
                this.fN.removeView(this.fG);
                if (this.fH != null) {
                    this.fG.i(false);
                    this.fH.fQ.addView(this.fG, this.fH.index, this.fH.fP);
                }
            }
            if (this.fE == null || this.fE.fT == null) {
                return;
            }
            this.fE.fT.B();
        }
    }

    private static RelativeLayout.LayoutParams a(int i, int i2, int i3, int i4) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(i3, i4);
        layoutParams.setMargins(i, i2, 0, 0);
        layoutParams.addRule(10);
        layoutParams.addRule(9);
        return layoutParams;
    }

    public static void a(Context context, bh bhVar) {
        Intent intent = new Intent();
        intent.setClassName(context, AdActivity.CLASS_NAME);
        intent.putExtra("com.google.android.gms.ads.internal.overlay.useClientJar", bhVar.eg.hS);
        bh.a(intent, bhVar);
        intent.addFlags(524288);
        context.startActivity(intent);
    }

    private void e(boolean z) throws a {
        this.fD.requestWindowFeature(1);
        Window window = this.fD.getWindow();
        window.setFlags(1024, 1024);
        setRequestedOrientation(this.fE.orientation);
        if (Build.VERSION.SDK_INT >= 11) {
            cn.m("Enabling hardware acceleration on the AdActivity window.");
            cj.a(window);
        }
        this.fN = new RelativeLayout(this.fD);
        this.fN.setBackgroundColor(-16777216);
        this.fD.setContentView(this.fN);
        boolean aD = this.fE.fU.aw().aD();
        if (z) {
            this.fG = cq.a(this.fD, this.fE.fU.av(), true, aD, null, this.fE.eg);
            this.fG.aw().a(null, null, this.fE.fV, this.fE.fZ, true);
            this.fG.aw().a(new cr.a() { // from class: com.google.android.gms.internal.bf.1
                @Override // com.google.android.gms.internal.cr.a
                public void a(cq cqVar) {
                    cqVar.at();
                }
            });
            if (this.fE.fz != null) {
                this.fG.loadUrl(this.fE.fz);
            } else if (this.fE.fY == null) {
                throw new a("No URL or HTML to display in ad overlay.");
            } else {
                this.fG.loadDataWithBaseURL(this.fE.fW, this.fE.fY, "text/html", "UTF-8", null);
            }
        } else {
            this.fG = this.fE.fU;
            this.fG.setContext(this.fD);
        }
        this.fG.a(this);
        this.fN.addView(this.fG, -1, -1);
        if (!z) {
            this.fG.at();
        }
        c(aD);
    }

    public bj Q() {
        return this.fF;
    }

    public void R() {
        if (this.fE != null) {
            setRequestedOrientation(this.fE.orientation);
        }
        if (this.fJ != null) {
            this.fD.setContentView(this.fN);
            this.fJ.removeAllViews();
            this.fJ = null;
        }
        if (this.fK != null) {
            this.fK.onCustomViewHidden();
            this.fK = null;
        }
    }

    public void S() {
        this.fN.removeView(this.fI);
        c(true);
    }

    public void a(View view, WebChromeClient.CustomViewCallback customViewCallback) {
        this.fJ = new FrameLayout(this.fD);
        this.fJ.setBackgroundColor(-16777216);
        this.fJ.addView(view, -1, -1);
        this.fD.setContentView(this.fJ);
        this.fK = customViewCallback;
    }

    public void b(int i, int i2, int i3, int i4) {
        if (this.fF != null) {
            this.fF.setLayoutParams(a(i, i2, i3, i4));
        }
    }

    public void c(int i, int i2, int i3, int i4) {
        if (this.fF == null) {
            this.fF = new bj(this.fD, this.fG);
            this.fN.addView(this.fF, 0, a(i, i2, i3, i4));
            this.fG.aw().j(false);
        }
    }

    public void c(boolean z) {
        this.fI = new bk(this.fD, z ? 50 : 32);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams.addRule(10);
        layoutParams.addRule(z ? 11 : 9);
        this.fI.d(this.fE.fX);
        this.fN.addView(this.fI, layoutParams);
    }

    public void close() {
        this.fD.finish();
    }

    public void d(boolean z) {
        if (this.fI != null) {
            this.fI.d(z);
        }
    }

    @Override // com.google.android.gms.internal.bn
    public void onCreate(Bundle savedInstanceState) {
        this.fL = savedInstanceState != null ? savedInstanceState.getBoolean("com.google.android.gms.ads.internal.overlay.hasResumed", false) : false;
        try {
            this.fE = bh.a(this.fD.getIntent());
            if (this.fE == null) {
                throw new a("Could not get info for ad overlay.");
            }
            if (savedInstanceState == null) {
                if (this.fE.fT != null) {
                    this.fE.fT.C();
                }
                if (this.fE.ga != 1 && this.fE.fS != null) {
                    this.fE.fS.y();
                }
            }
            switch (this.fE.ga) {
                case 1:
                    e(false);
                    return;
                case 2:
                    this.fH = new b(this.fE.fU);
                    e(false);
                    return;
                case 3:
                    e(true);
                    return;
                case 4:
                    if (this.fL) {
                        this.fD.finish();
                        return;
                    } else if (bc.a(this.fD, this.fE.fR, this.fE.fZ)) {
                        return;
                    } else {
                        this.fD.finish();
                        return;
                    }
                default:
                    throw new a("Could not determine ad overlay type.");
            }
        } catch (a e) {
            cn.q(e.getMessage());
            this.fD.finish();
        }
    }

    @Override // com.google.android.gms.internal.bn
    public void onDestroy() {
        if (this.fF != null) {
            this.fF.destroy();
        }
        if (this.fG != null) {
            this.fN.removeView(this.fG);
        }
        T();
    }

    @Override // com.google.android.gms.internal.bn
    public void onPause() {
        if (this.fF != null) {
            this.fF.pause();
        }
        R();
        if (this.fG != null && (!this.fD.isFinishing() || this.fH == null)) {
            ci.a(this.fG);
        }
        T();
    }

    @Override // com.google.android.gms.internal.bn
    public void onRestart() {
    }

    @Override // com.google.android.gms.internal.bn
    public void onResume() {
        if (this.fE != null && this.fE.ga == 4) {
            if (this.fL) {
                this.fD.finish();
            } else {
                this.fL = true;
            }
        }
        if (this.fG != null) {
            ci.b(this.fG);
        }
    }

    @Override // com.google.android.gms.internal.bn
    public void onSaveInstanceState(Bundle outBundle) {
        outBundle.putBoolean("com.google.android.gms.ads.internal.overlay.hasResumed", this.fL);
    }

    @Override // com.google.android.gms.internal.bn
    public void onStart() {
    }

    @Override // com.google.android.gms.internal.bn
    public void onStop() {
        T();
    }

    public void setRequestedOrientation(int requestedOrientation) {
        this.fD.setRequestedOrientation(requestedOrientation);
    }
}
