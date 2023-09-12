package com.google.android.gms.internal;

import android.content.Context;
import android.net.Uri;
import android.view.MotionEvent;
import com.j256.ormlite.stmt.query.SimpleComparison;
/* loaded from: classes.dex */
public class h {
    private d dz;
    private String dw = "googleads.g.doubleclick.net";
    private String dx = "/pagead/ads";
    private String[] dy = {".doubleclick.net", ".googleadservices.com", ".googlesyndication.com"};
    private final c dA = new c();

    public h(d dVar) {
        this.dz = dVar;
    }

    private Uri a(Uri uri, Context context, String str, boolean z) throws i {
        try {
            if (uri.getQueryParameter("ms") != null) {
                throw new i("Query parameter already exists: ms");
            }
            return a(uri, "ms", z ? this.dz.a(context, str) : this.dz.a(context));
        } catch (UnsupportedOperationException e) {
            throw new i("Provided Uri is not in a valid state");
        }
    }

    private Uri a(Uri uri, String str, String str2) throws UnsupportedOperationException {
        String uri2 = uri.toString();
        int indexOf = uri2.indexOf("&adurl");
        if (indexOf == -1) {
            indexOf = uri2.indexOf("?adurl");
        }
        return indexOf != -1 ? Uri.parse(uri2.substring(0, indexOf + 1) + str + SimpleComparison.EQUAL_TO_OPERATION + str2 + "&" + uri2.substring(indexOf + 1)) : uri.buildUpon().appendQueryParameter(str, str2).build();
    }

    public Uri a(Uri uri, Context context) throws i {
        try {
            return a(uri, context, uri.getQueryParameter("ai"), true);
        } catch (UnsupportedOperationException e) {
            throw new i("Provided Uri is not in a valid state");
        }
    }

    public void a(MotionEvent motionEvent) {
        this.dz.a(motionEvent);
    }

    public boolean a(Uri uri) {
        if (uri == null) {
            throw new NullPointerException();
        }
        try {
            String host = uri.getHost();
            for (String str : this.dy) {
                if (host.endsWith(str)) {
                    return true;
                }
            }
            return false;
        } catch (NullPointerException e) {
            return false;
        }
    }

    public d g() {
        return this.dz;
    }
}
