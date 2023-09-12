package com.squareup.picasso;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestHandler;
import java.io.IOException;
import java.io.InputStream;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class AssetRequestHandler extends RequestHandler {
    protected static final String ANDROID_ASSET = "android_asset";
    private static final int ASSET_PREFIX_LENGTH = "file:///android_asset/".length();
    private final AssetManager assetManager;

    public AssetRequestHandler(Context context) {
        this.assetManager = context.getAssets();
    }

    @Override // com.squareup.picasso.RequestHandler
    public boolean canHandleRequest(Request data) {
        Uri uri = data.uri;
        return "file".equals(uri.getScheme()) && !uri.getPathSegments().isEmpty() && ANDROID_ASSET.equals(uri.getPathSegments().get(0));
    }

    @Override // com.squareup.picasso.RequestHandler
    public RequestHandler.Result load(Request data) throws IOException {
        String filePath = data.uri.toString().substring(ASSET_PREFIX_LENGTH);
        return new RequestHandler.Result(decodeAsset(data, filePath), Picasso.LoadedFrom.DISK);
    }

    Bitmap decodeAsset(Request data, String filePath) throws IOException {
        BitmapFactory.Options options = createBitmapOptions(data);
        if (requiresInSampleSize(options)) {
            InputStream is = null;
            try {
                is = this.assetManager.open(filePath);
                BitmapFactory.decodeStream(is, null, options);
                Utils.closeQuietly(is);
                calculateInSampleSize(data.targetWidth, data.targetHeight, options, data);
            } finally {
            }
        }
        InputStream is2 = this.assetManager.open(filePath);
        try {
            return BitmapFactory.decodeStream(is2, null, options);
        } finally {
        }
    }
}
