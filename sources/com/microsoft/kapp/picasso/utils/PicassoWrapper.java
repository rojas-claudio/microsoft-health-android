package com.microsoft.kapp.picasso.utils;

import android.content.Context;
import android.net.Uri;
import com.microsoft.kapp.logging.KLog;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import java.io.File;
/* loaded from: classes.dex */
public class PicassoWrapper {
    private static final String TAG = PicassoWrapper.class.getSimpleName();
    private Picasso mPicasso;

    private PicassoWrapper(Context context) {
        this.mPicasso = null;
        this.mPicasso = Picasso.with(context);
    }

    public static PicassoWrapper with(Context context) {
        return new PicassoWrapper(context);
    }

    public RequestCreator load(String path) {
        if (path == null || path.trim().length() != 0) {
            return this.mPicasso.load(path);
        }
        KLog.e(TAG, "Path must not be empty.");
        return this.mPicasso.load((String) null);
    }

    public RequestCreator load(Uri uri) {
        return this.mPicasso.load(uri);
    }

    public RequestCreator load(int resourceId) {
        return this.mPicasso.load(resourceId);
    }

    public RequestCreator load(File file) {
        return this.mPicasso.load(file);
    }
}
