package com.facebook.internal;

import android.content.Context;
import com.facebook.LoggingBehavior;
import com.facebook.internal.FileLruCache;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class ImageResponseCache {
    static final String TAG = ImageResponseCache.class.getSimpleName();
    private static volatile FileLruCache imageCache;

    ImageResponseCache() {
    }

    static synchronized FileLruCache getCache(Context context) throws IOException {
        FileLruCache fileLruCache;
        synchronized (ImageResponseCache.class) {
            if (imageCache == null) {
                imageCache = new FileLruCache(context.getApplicationContext(), TAG, new FileLruCache.Limits());
            }
            fileLruCache = imageCache;
        }
        return fileLruCache;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static InputStream getCachedImageStream(URI url, Context context) {
        if (url == null || !isCDNURL(url)) {
            return null;
        }
        try {
            FileLruCache cache = getCache(context);
            InputStream imageStream = cache.get(url.toString());
            return imageStream;
        } catch (IOException e) {
            Logger.log(LoggingBehavior.CACHE, 5, TAG, e.toString());
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static InputStream interceptAndCacheImageStream(Context context, HttpURLConnection connection) throws IOException {
        if (connection.getResponseCode() != 200) {
            return null;
        }
        URL url = connection.getURL();
        InputStream stream = connection.getInputStream();
        try {
            if (isCDNURL(url.toURI())) {
                FileLruCache cache = getCache(context);
                return cache.interceptAndPut(url.toString(), new BufferedHttpInputStream(stream, connection));
            }
            return stream;
        } catch (IOException e) {
            return stream;
        } catch (URISyntaxException e2) {
            return stream;
        }
    }

    private static boolean isCDNURL(URI url) {
        if (url != null) {
            String uriHost = url.getHost();
            if (uriHost.endsWith("fbcdn.net")) {
                return true;
            }
            if (uriHost.startsWith("fbcdn") && uriHost.endsWith("akamaihd.net")) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void clearCache(Context context) {
        try {
            getCache(context).clearCache();
        } catch (IOException e) {
            Logger.log(LoggingBehavior.CACHE, 5, TAG, "clearCache failed " + e.getMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class BufferedHttpInputStream extends BufferedInputStream {
        HttpURLConnection connection;

        BufferedHttpInputStream(InputStream stream, HttpURLConnection connection) {
            super(stream, 8192);
            this.connection = connection;
        }

        @Override // java.io.BufferedInputStream, java.io.FilterInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            super.close();
            Utility.disconnectQuietly(this.connection);
        }
    }
}
