package com.squareup.picasso;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestHandler;
import java.io.IOException;
import java.io.InputStream;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class ContentStreamRequestHandler extends RequestHandler {
    final Context context;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ContentStreamRequestHandler(Context context) {
        this.context = context;
    }

    @Override // com.squareup.picasso.RequestHandler
    public boolean canHandleRequest(Request data) {
        return "content".equals(data.uri.getScheme());
    }

    @Override // com.squareup.picasso.RequestHandler
    public RequestHandler.Result load(Request data) throws IOException {
        return new RequestHandler.Result(decodeContentStream(data), Picasso.LoadedFrom.DISK);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Bitmap decodeContentStream(Request data) throws IOException {
        InputStream is;
        ContentResolver contentResolver = this.context.getContentResolver();
        BitmapFactory.Options options = createBitmapOptions(data);
        if (requiresInSampleSize(options)) {
            is = null;
            try {
                is = contentResolver.openInputStream(data.uri);
                BitmapFactory.decodeStream(is, null, options);
                Utils.closeQuietly(is);
                calculateInSampleSize(data.targetWidth, data.targetHeight, options, data);
            } finally {
            }
        }
        is = contentResolver.openInputStream(data.uri);
        try {
            return BitmapFactory.decodeStream(is, null, options);
        } finally {
        }
    }
}
