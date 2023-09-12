package com.google.android.gms.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import com.google.android.gms.R;
import com.google.android.gms.ads.AdSize;
/* loaded from: classes.dex */
public final class aa {
    private final AdSize c;
    private final String dV;

    public aa(Context context, AttributeSet attributeSet) {
        TypedArray obtainAttributes = context.getResources().obtainAttributes(attributeSet, R.styleable.AdsAttrs);
        String string = obtainAttributes.getString(0);
        if (TextUtils.isEmpty(string)) {
            throw new IllegalArgumentException("Required XML attribute \"adSize\" was missing.");
        }
        this.c = f(string)[0];
        this.dV = obtainAttributes.getString(1);
        if (TextUtils.isEmpty(this.dV)) {
            throw new IllegalArgumentException("Required XML attribute \"adUnitId\" was missing.");
        }
    }

    private static AdSize[] f(String str) {
        String[] split = str.split("\\s*,\\s*");
        AdSize[] adSizeArr = new AdSize[split.length];
        for (int i = 0; i < split.length; i++) {
            String trim = split[i].trim();
            if (trim.matches("^(\\d+|FULL_WIDTH)\\s*[xX]\\s*(\\d+|AUTO_HEIGHT)$")) {
                String[] split2 = trim.split("[xX]");
                split2[0] = split2[0].trim();
                split2[1] = split2[1].trim();
                try {
                    adSizeArr[i] = new AdSize("FULL_WIDTH".equals(split2[0]) ? -1 : Integer.parseInt(split2[0]), "AUTO_HEIGHT".equals(split2[1]) ? -2 : Integer.parseInt(split2[1]));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Could not parse XML attribute \"adSize\": " + trim);
                }
            } else if ("BANNER".equals(trim)) {
                adSizeArr[i] = AdSize.BANNER;
            } else if ("FULL_BANNER".equals(trim)) {
                adSizeArr[i] = AdSize.FULL_BANNER;
            } else if ("LEADERBOARD".equals(trim)) {
                adSizeArr[i] = AdSize.LEADERBOARD;
            } else if ("MEDIUM_RECTANGLE".equals(trim)) {
                adSizeArr[i] = AdSize.MEDIUM_RECTANGLE;
            } else if ("SMART_BANNER".equals(trim)) {
                adSizeArr[i] = AdSize.SMART_BANNER;
            } else if (!"WIDE_SKYSCRAPER".equals(trim)) {
                throw new IllegalArgumentException("Could not parse XML attribute \"adSize\": " + trim);
            } else {
                adSizeArr[i] = AdSize.WIDE_SKYSCRAPER;
            }
        }
        if (adSizeArr.length == 0) {
            throw new IllegalArgumentException("Could not parse XML attribute \"adSize\": " + str);
        }
        return adSizeArr;
    }

    public AdSize getAdSize() {
        return this.c;
    }

    public String getAdUnitId() {
        return this.dV;
    }
}
