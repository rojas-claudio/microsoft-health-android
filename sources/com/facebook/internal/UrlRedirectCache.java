package com.facebook.internal;

import android.content.Context;
import com.facebook.LoggingBehavior;
import com.facebook.internal.FileLruCache;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class UrlRedirectCache {
    private static volatile FileLruCache urlRedirectCache;
    static final String TAG = UrlRedirectCache.class.getSimpleName();
    private static final String REDIRECT_CONTENT_TAG = String.valueOf(TAG) + "_Redirect";

    UrlRedirectCache() {
    }

    static synchronized FileLruCache getCache(Context context) throws IOException {
        FileLruCache fileLruCache;
        synchronized (UrlRedirectCache.class) {
            if (urlRedirectCache == null) {
                urlRedirectCache = new FileLruCache(context.getApplicationContext(), TAG, new FileLruCache.Limits());
            }
            fileLruCache = urlRedirectCache;
        }
        return fileLruCache;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static URI getRedirectedUri(Context context, URI uri) {
        if (uri == null) {
            return null;
        }
        String uriString = uri.toString();
        InputStreamReader reader = null;
        try {
            FileLruCache cache = getCache(context);
            boolean redirectExists = false;
            InputStreamReader reader2 = null;
            while (true) {
                try {
                    InputStream stream = cache.get(uriString, REDIRECT_CONTENT_TAG);
                    if (stream == null) {
                        break;
                    }
                    redirectExists = true;
                    reader = new InputStreamReader(stream);
                    char[] buffer = new char[128];
                    StringBuilder urlBuilder = new StringBuilder();
                    while (true) {
                        int bufferLength = reader.read(buffer, 0, buffer.length);
                        if (bufferLength <= 0) {
                            break;
                        }
                        urlBuilder.append(buffer, 0, bufferLength);
                    }
                    Utility.closeQuietly(reader);
                    uriString = urlBuilder.toString();
                    reader2 = reader;
                } catch (IOException e) {
                    reader = reader2;
                    Utility.closeQuietly(reader);
                    return null;
                } catch (URISyntaxException e2) {
                    reader = reader2;
                    Utility.closeQuietly(reader);
                    return null;
                } catch (Throwable th) {
                    th = th;
                    reader = reader2;
                    Utility.closeQuietly(reader);
                    throw th;
                }
            }
            if (!redirectExists) {
                Utility.closeQuietly(reader2);
                return null;
            }
            URI uri2 = new URI(uriString);
            Utility.closeQuietly(reader2);
            return uri2;
        } catch (IOException e3) {
        } catch (URISyntaxException e4) {
        } catch (Throwable th2) {
            th = th2;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void cacheUriRedirect(Context context, URI fromUri, URI toUri) {
        if (fromUri != null && toUri != null) {
            OutputStream redirectStream = null;
            try {
                FileLruCache cache = getCache(context);
                redirectStream = cache.openPutStream(fromUri.toString(), REDIRECT_CONTENT_TAG);
                redirectStream.write(toUri.toString().getBytes());
            } catch (IOException e) {
            } finally {
                Utility.closeQuietly(redirectStream);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void clearCache(Context context) {
        try {
            getCache(context).clearCache();
        } catch (IOException e) {
            Logger.log(LoggingBehavior.CACHE, 5, TAG, "clearCache failed " + e.getMessage());
        }
    }
}
