package com.google.android.gms.internal;

import android.content.Context;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
/* loaded from: classes.dex */
public final class cl extends cg {
    private final String hM;
    private final String hN;
    private final Context mContext;

    public cl(Context context, String str, String str2) {
        this.mContext = context;
        this.hM = str;
        this.hN = str2;
    }

    @Override // com.google.android.gms.internal.cg
    public void ac() {
        try {
            cn.p("Pinging URL: " + this.hN);
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(this.hN).openConnection();
            ci.a(this.mContext, this.hM, true, httpURLConnection);
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode < 200 || responseCode >= 300) {
                cn.q("Received non-success response code " + responseCode + " from pinging URL: " + this.hN);
            }
            httpURLConnection.disconnect();
        } catch (IOException e) {
            cn.q("Error while pinging URL: " + this.hN + ". " + e.getMessage());
        }
    }

    @Override // com.google.android.gms.internal.cg
    public void onStop() {
    }
}
