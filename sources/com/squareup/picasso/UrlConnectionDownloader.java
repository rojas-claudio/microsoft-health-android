package com.squareup.picasso;

import android.content.Context;
import android.net.Uri;
import android.net.http.HttpResponseCache;
import android.os.Build;
import com.microsoft.kapp.style.utils.MetricSpannerUtils;
import com.microsoft.kapp.utils.Constants;
import com.squareup.picasso.Downloader;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
/* loaded from: classes.dex */
public class UrlConnectionDownloader implements Downloader {
    static final String RESPONSE_SOURCE = "X-Android-Response-Source";
    static volatile Object cache;
    private static final Object lock = new Object();
    private final Context context;

    public UrlConnectionDownloader(Context context) {
        this.context = context.getApplicationContext();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public HttpURLConnection openConnection(Uri path) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(path.toString()).openConnection();
        connection.setConnectTimeout(Constants.SYNC_HOMEDATA_TIMEOUT);
        connection.setReadTimeout(20000);
        return connection;
    }

    @Override // com.squareup.picasso.Downloader
    public Downloader.Response load(Uri uri, boolean localCacheOnly) throws IOException {
        if (Build.VERSION.SDK_INT >= 14) {
            installCacheIfNeeded(this.context);
        }
        HttpURLConnection connection = openConnection(uri);
        connection.setUseCaches(true);
        if (localCacheOnly) {
            connection.setRequestProperty("Cache-Control", "only-if-cached,max-age=2147483647");
        }
        int responseCode = connection.getResponseCode();
        if (responseCode >= 300) {
            connection.disconnect();
            throw new Downloader.ResponseException(responseCode + MetricSpannerUtils.METRIC_UNITS_SEPARATOR_SPACE + connection.getResponseMessage());
        }
        long contentLength = connection.getHeaderFieldInt("Content-Length", -1);
        boolean fromCache = Utils.parseResponseSourceHeader(connection.getHeaderField(RESPONSE_SOURCE));
        return new Downloader.Response(connection.getInputStream(), fromCache, contentLength);
    }

    @Override // com.squareup.picasso.Downloader
    public void shutdown() {
        if (Build.VERSION.SDK_INT >= 14 && cache != null) {
            ResponseCacheIcs.close(cache);
        }
    }

    private static void installCacheIfNeeded(Context context) {
        if (cache == null) {
            try {
                synchronized (lock) {
                    if (cache == null) {
                        cache = ResponseCacheIcs.install(context);
                    }
                }
            } catch (IOException e) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class ResponseCacheIcs {
        private ResponseCacheIcs() {
        }

        static Object install(Context context) throws IOException {
            File cacheDir = Utils.createDefaultCacheDir(context);
            HttpResponseCache cache = HttpResponseCache.getInstalled();
            if (cache == null) {
                long maxSize = Utils.calculateDiskCacheSize(cacheDir);
                return HttpResponseCache.install(cacheDir, maxSize);
            }
            return cache;
        }

        static void close(Object cache) {
            try {
                ((HttpResponseCache) cache).close();
            } catch (IOException e) {
            }
        }
    }
}
