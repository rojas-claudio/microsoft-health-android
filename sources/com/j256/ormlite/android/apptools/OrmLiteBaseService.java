package com.j256.ormlite.android.apptools;

import android.app.Service;
import android.content.Context;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
/* loaded from: classes.dex */
public abstract class OrmLiteBaseService<H extends OrmLiteSqliteOpenHelper> extends Service {
    private volatile boolean created = false;
    private volatile boolean destroyed = false;
    private volatile H helper;

    public H getHelper() {
        if (this.helper == null) {
            if (!this.created) {
                throw new IllegalStateException("A call has not been made to onCreate() yet so the helper is null");
            }
            if (this.destroyed) {
                throw new IllegalStateException("A call to onDestroy has already been made and the helper cannot be used after that point");
            }
            throw new IllegalStateException("Helper is null for some unknown reason");
        }
        return this.helper;
    }

    public ConnectionSource getConnectionSource() {
        return getHelper().getConnectionSource();
    }

    @Override // android.app.Service
    public void onCreate() {
        if (this.helper == null) {
            this.helper = getHelperInternal(this);
            this.created = true;
        }
        super.onCreate();
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        releaseHelper(this.helper);
        this.destroyed = true;
    }

    protected H getHelperInternal(Context context) {
        H newHelper = (H) OpenHelperManager.getHelper(context);
        return newHelper;
    }

    protected void releaseHelper(H helper) {
        OpenHelperManager.releaseHelper();
        this.helper = null;
    }
}