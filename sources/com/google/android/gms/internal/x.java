package com.google.android.gms.internal;

import android.content.Context;
import android.os.Parcel;
import android.util.DisplayMetrics;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
/* loaded from: classes.dex */
public final class x implements SafeParcelable {
    public static final y CREATOR = new y();
    public final String ew;
    public final boolean ex;
    public final int height;
    public final int heightPixels;
    public final int versionCode;
    public final int width;
    public final int widthPixels;

    public x() {
        this(1, "interstitial_mb", 0, 0, true, 0, 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public x(int i, String str, int i2, int i3, boolean z, int i4, int i5) {
        this.versionCode = i;
        this.ew = str;
        this.height = i2;
        this.heightPixels = i3;
        this.ex = z;
        this.width = i4;
        this.widthPixels = i5;
    }

    public x(Context context, AdSize adSize) {
        int i;
        this.versionCode = 1;
        this.ex = false;
        this.width = adSize.getWidth();
        this.height = adSize.getHeight();
        boolean z = this.width == -1;
        boolean z2 = this.height == -2;
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        if (z) {
            this.widthPixels = a(displayMetrics);
            i = (int) (this.widthPixels / displayMetrics.density);
        } else {
            int i2 = this.width;
            this.widthPixels = cm.a(displayMetrics, this.width);
            i = i2;
        }
        int c = z2 ? c(displayMetrics) : this.height;
        this.heightPixels = cm.a(displayMetrics, c);
        if (z || z2) {
            this.ew = i + "x" + c + "_as";
        } else {
            this.ew = adSize.toString();
        }
    }

    public static int a(DisplayMetrics displayMetrics) {
        return displayMetrics.widthPixels;
    }

    public static int b(DisplayMetrics displayMetrics) {
        return (int) (c(displayMetrics) * displayMetrics.density);
    }

    private static int c(DisplayMetrics displayMetrics) {
        int i = (int) (displayMetrics.heightPixels / displayMetrics.density);
        if (i <= 400) {
            return 32;
        }
        return i <= 720 ? 50 : 90;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        y.a(this, out, flags);
    }
}
