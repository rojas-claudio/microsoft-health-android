package net.hockeyapp.android.utils;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpProtocolParams;
/* loaded from: classes.dex */
public class ConnectionManager {
    private HttpClient httpClient;

    private ConnectionManager() {
        BasicHttpParams basicHttpParams = new BasicHttpParams();
        HttpProtocolParams.setVersion(basicHttpParams, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(basicHttpParams, "utf-8");
        basicHttpParams.setBooleanParameter("http.protocol.expect-continue", false);
        basicHttpParams.setParameter("http.useragent", "HockeySDK/Android");
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        SSLSocketFactory sslSocketFactory = SSLSocketFactory.getSocketFactory();
        sslSocketFactory.setHostnameVerifier(SSLSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        registry.register(new Scheme("https", sslSocketFactory, 443));
        ThreadSafeClientConnManager manager = new ThreadSafeClientConnManager(basicHttpParams, registry);
        this.httpClient = new DefaultHttpClient(manager, basicHttpParams);
    }

    /* loaded from: classes.dex */
    private static class ConnectionManagerHolder {
        public static final ConnectionManager INSTANCE = new ConnectionManager();

        private ConnectionManagerHolder() {
        }
    }

    public static ConnectionManager getInstance() {
        return ConnectionManagerHolder.INSTANCE;
    }

    public HttpClient getHttpClient() {
        return this.httpClient;
    }
}
