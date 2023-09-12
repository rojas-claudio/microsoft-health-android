package com.squareup.picasso;

import android.content.Context;
import android.net.Uri;
import com.microsoft.kapp.style.utils.MetricSpannerUtils;
import com.microsoft.kapp.utils.Constants;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.OkUrlFactory;
import com.squareup.picasso.Downloader;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
/* loaded from: classes.dex */
public class OkHttpDownloader implements Downloader {
    static final String RESPONSE_SOURCE_ANDROID = "X-Android-Response-Source";
    static final String RESPONSE_SOURCE_OKHTTP = "OkHttp-Response-Source";
    private final OkUrlFactory urlFactory;

    public OkHttpDownloader(Context context) {
        this(Utils.createDefaultCacheDir(context));
    }

    public OkHttpDownloader(File cacheDir) {
        this(cacheDir, Utils.calculateDiskCacheSize(cacheDir));
    }

    public OkHttpDownloader(Context context, long maxSize) {
        this(Utils.createDefaultCacheDir(context), maxSize);
    }

    public OkHttpDownloader(File cacheDir, long maxSize) {
        this(new OkHttpClient());
        try {
            this.urlFactory.client().setCache(new com.squareup.okhttp.Cache(cacheDir, maxSize));
        } catch (IOException e) {
        }
    }

    public OkHttpDownloader(OkHttpClient client) {
        this.urlFactory = new OkUrlFactory(client);
    }

    protected HttpURLConnection openConnection(Uri uri) throws IOException {
        HttpURLConnection connection = this.urlFactory.open(new URL(uri.toString()));
        connection.setConnectTimeout(Constants.SYNC_HOMEDATA_TIMEOUT);
        connection.setReadTimeout(20000);
        return connection;
    }

    protected OkHttpClient getClient() {
        return this.urlFactory.client();
    }

    @Override // com.squareup.picasso.Downloader
    public Downloader.Response load(Uri uri, boolean localCacheOnly) throws IOException {
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
        String responseSource = connection.getHeaderField(RESPONSE_SOURCE_OKHTTP);
        if (responseSource == null) {
            responseSource = connection.getHeaderField(RESPONSE_SOURCE_ANDROID);
        }
        long contentLength = connection.getHeaderFieldInt("Content-Length", -1);
        boolean fromCache = Utils.parseResponseSourceHeader(responseSource);
        return new Downloader.Response(connection.getInputStream(), fromCache, contentLength);
    }

    @Override // com.squareup.picasso.Downloader
    public void shutdown() {
        com.squareup.okhttp.Cache cache = this.urlFactory.client().getCache();
        if (cache != null) {
            try {
                cache.close();
            } catch (IOException e) {
            }
        }
    }
}
