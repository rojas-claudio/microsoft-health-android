package org.acra.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import org.acra.ACRA;
/* loaded from: classes.dex */
public final class PackageManagerWrapper {
    private final Context context;

    public PackageManagerWrapper(Context context) {
        this.context = context;
    }

    public boolean hasPermission(String permission) {
        PackageManager pm = this.context.getPackageManager();
        if (pm == null) {
            return false;
        }
        try {
            return pm.checkPermission(permission, this.context.getPackageName()) == 0;
        } catch (RuntimeException e) {
            return false;
        }
    }

    public PackageInfo getPackageInfo() {
        PackageManager pm = this.context.getPackageManager();
        if (pm == null) {
            return null;
        }
        try {
            return pm.getPackageInfo(this.context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            Log.v(ACRA.LOG_TAG, "Failed to find PackageInfo for current App : " + this.context.getPackageName());
            return null;
        } catch (RuntimeException e2) {
            return null;
        }
    }
}
