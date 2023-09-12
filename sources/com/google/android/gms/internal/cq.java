package com.google.android.gms.internal;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.MutableContextWrapper;
import android.net.Uri;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.facebook.internal.ServerProtocol;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
/* loaded from: classes.dex */
public final class cq extends WebView implements DownloadListener {
    private final Object eJ;
    private x fg;
    private final h go;
    private final cr hT;
    private final MutableContextWrapper hU;
    private final co hV;
    private bf hW;
    private boolean hX;
    private boolean hY;

    private cq(MutableContextWrapper mutableContextWrapper, x xVar, boolean z, boolean z2, h hVar, co coVar) {
        super(mutableContextWrapper);
        this.eJ = new Object();
        this.hU = mutableContextWrapper;
        this.fg = xVar;
        this.hX = z;
        this.go = hVar;
        this.hV = coVar;
        setBackgroundColor(0);
        WebSettings settings = getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSavePassword(false);
        ci.a(mutableContextWrapper, coVar.hP, settings);
        if (Build.VERSION.SDK_INT >= 17) {
            ck.a(getContext(), settings);
        } else if (Build.VERSION.SDK_INT >= 11) {
            cj.a(getContext(), settings);
        }
        setDownloadListener(this);
        if (Build.VERSION.SDK_INT >= 11) {
            this.hT = new ct(this, z2);
        } else {
            this.hT = new cr(this, z2);
        }
        setWebViewClient(this.hT);
        if (Build.VERSION.SDK_INT >= 14) {
            setWebChromeClient(new cu(this));
        } else if (Build.VERSION.SDK_INT >= 11) {
            setWebChromeClient(new cs(this));
        }
        aA();
    }

    public static cq a(Context context, x xVar, boolean z, boolean z2, h hVar, co coVar) {
        return new cq(new MutableContextWrapper(context), xVar, z, z2, hVar, coVar);
    }

    private void aA() {
        synchronized (this.eJ) {
            if (this.hX || this.fg.ex) {
                if (Build.VERSION.SDK_INT < 14) {
                    cn.m("Disabling hardware acceleration on an overlay.");
                    aB();
                } else {
                    cn.m("Enabling hardware acceleration on an overlay.");
                    aC();
                }
            } else if (Build.VERSION.SDK_INT < 18) {
                cn.m("Disabling hardware acceleration on an AdView.");
                aB();
            } else {
                cn.m("Enabling hardware acceleration on an AdView.");
                aC();
            }
        }
    }

    private void aB() {
        synchronized (this.eJ) {
            if (!this.hY && Build.VERSION.SDK_INT >= 11) {
                cj.c(this);
            }
            this.hY = true;
        }
    }

    private void aC() {
        synchronized (this.eJ) {
            if (this.hY && Build.VERSION.SDK_INT >= 11) {
                cj.d(this);
            }
            this.hY = false;
        }
    }

    public void a(Context context, x xVar) {
        synchronized (this.eJ) {
            this.hU.setBaseContext(context);
            this.hW = null;
            this.fg = xVar;
            this.hX = false;
            ci.b(this);
            loadUrl("about:blank");
            this.hT.reset();
        }
    }

    public void a(bf bfVar) {
        synchronized (this.eJ) {
            this.hW = bfVar;
        }
    }

    public void a(String str, Map<String, ?> map) {
        StringBuilder sb = new StringBuilder();
        sb.append("javascript:AFMA_ReceiveMessage('");
        sb.append(str);
        sb.append("'");
        if (map != null) {
            try {
                String jSONObject = ci.l(map).toString();
                sb.append(",");
                sb.append(jSONObject);
            } catch (JSONException e) {
                cn.q("Could not convert AFMA event parameters to JSON.");
                return;
            }
        }
        sb.append(");");
        cn.p("Dispatching AFMA event: " + ((Object) sb));
        loadUrl(sb.toString());
    }

    public void as() {
        HashMap hashMap = new HashMap(1);
        hashMap.put(ServerProtocol.FALLBACK_DIALOG_PARAM_VERSION, this.hV.hP);
        a("onhide", hashMap);
    }

    public void at() {
        HashMap hashMap = new HashMap(1);
        hashMap.put(ServerProtocol.FALLBACK_DIALOG_PARAM_VERSION, this.hV.hP);
        a("onshow", hashMap);
    }

    public bf au() {
        bf bfVar;
        synchronized (this.eJ) {
            bfVar = this.hW;
        }
        return bfVar;
    }

    public x av() {
        x xVar;
        synchronized (this.eJ) {
            xVar = this.fg;
        }
        return xVar;
    }

    public cr aw() {
        return this.hT;
    }

    public h ax() {
        return this.go;
    }

    public co ay() {
        return this.hV;
    }

    public boolean az() {
        boolean z;
        synchronized (this.eJ) {
            z = this.hX;
        }
        return z;
    }

    public void i(boolean z) {
        synchronized (this.eJ) {
            this.hX = z;
            aA();
        }
    }

    @Override // android.webkit.DownloadListener
    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long size) {
        try {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setDataAndType(Uri.parse(url), mimeType);
            getContext().startActivity(intent);
        } catch (ActivityNotFoundException e) {
            cn.m("Couldn't find an Activity to view url/mimetype: " + url + " / " + mimeType);
        }
    }

    @Override // android.webkit.WebView, android.widget.AbsoluteLayout, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int i = Integer.MAX_VALUE;
        synchronized (this.eJ) {
            if (isInEditMode() || this.hX) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                return;
            }
            int mode = View.MeasureSpec.getMode(widthMeasureSpec);
            int size = View.MeasureSpec.getSize(widthMeasureSpec);
            int mode2 = View.MeasureSpec.getMode(heightMeasureSpec);
            int size2 = View.MeasureSpec.getSize(heightMeasureSpec);
            int i2 = (mode == Integer.MIN_VALUE || mode == 1073741824) ? size : Integer.MAX_VALUE;
            if (mode2 == Integer.MIN_VALUE || mode2 == 1073741824) {
                i = size2;
            }
            if (this.fg.widthPixels > i2 || this.fg.heightPixels > i) {
                cn.q("Not enough space to show ad. Needs " + this.fg.widthPixels + "x" + this.fg.heightPixels + ", but only has " + size + "x" + size2);
                if (getVisibility() != 8) {
                    setVisibility(4);
                }
                setMeasuredDimension(0, 0);
            } else {
                if (getVisibility() != 8) {
                    setVisibility(0);
                }
                setMeasuredDimension(this.fg.widthPixels, this.fg.heightPixels);
            }
        }
    }

    @Override // android.webkit.WebView, android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        if (this.go != null) {
            this.go.a(event);
        }
        return super.onTouchEvent(event);
    }

    public void setContext(Context context) {
        this.hU.setBaseContext(context);
    }
}
