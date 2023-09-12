package net.hockeyapp.android.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import com.facebook.internal.ServerProtocol;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Locale;
import net.hockeyapp.android.Constants;
import net.hockeyapp.android.Tracking;
import net.hockeyapp.android.UpdateManagerListener;
import net.hockeyapp.android.utils.VersionCache;
import net.hockeyapp.android.utils.VersionHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes.dex */
public class CheckUpdateTask extends AsyncTask<Void, String, JSONArray> {
    protected static final String APK = "apk";
    protected static final String INTENT_EXTRA_JSON = "json";
    protected static final String INTENT_EXTRA_URL = "url";
    private static final int MAX_NUMBER_OF_VERSIONS = 25;
    protected String appIdentifier;
    private Context context;
    protected UpdateManagerListener listener;
    protected Boolean mandatory;
    protected String urlString;
    private long usageTime;

    public CheckUpdateTask(WeakReference<? extends Context> weakContext, String urlString) {
        this(weakContext, urlString, null);
    }

    public CheckUpdateTask(WeakReference<? extends Context> weakContext, String urlString, String appIdentifier) {
        this(weakContext, urlString, appIdentifier, null);
    }

    public CheckUpdateTask(WeakReference<? extends Context> weakContext, String urlString, String appIdentifier, UpdateManagerListener listener) {
        this.urlString = null;
        this.appIdentifier = null;
        this.context = null;
        this.mandatory = false;
        this.usageTime = 0L;
        this.appIdentifier = appIdentifier;
        this.urlString = urlString;
        this.listener = listener;
        Context ctx = null;
        if (weakContext != null) {
            Context ctx2 = weakContext.get();
            ctx = ctx2;
        }
        if (ctx != null) {
            this.context = ctx.getApplicationContext();
            this.usageTime = Tracking.getUsageTime(ctx);
            Constants.loadFromContext(ctx);
        }
    }

    public void attach(WeakReference<? extends Context> weakContext) {
        Context ctx = null;
        if (weakContext != null) {
            Context ctx2 = weakContext.get();
            ctx = ctx2;
        }
        if (ctx != null) {
            this.context = ctx.getApplicationContext();
            Constants.loadFromContext(ctx);
        }
    }

    public void detach() {
        this.context = null;
    }

    protected int getVersionCode() {
        return Integer.parseInt(Constants.APP_VERSION);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public JSONArray doInBackground(Void... args) {
        int versionCode;
        JSONArray json;
        try {
            versionCode = getVersionCode();
            json = new JSONArray(VersionCache.getVersionInfo(this.context));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!getCachingEnabled() || !findNewVersion(json, versionCode)) {
            URL url = new URL(getURLString(INTENT_EXTRA_JSON));
            URLConnection connection = createConnection(url);
            connection.connect();
            InputStream inputStream = new BufferedInputStream(connection.getInputStream());
            String jsonString = convertStreamToString(inputStream);
            inputStream.close();
            JSONArray json2 = new JSONArray(jsonString);
            if (findNewVersion(json2, versionCode)) {
                return limitResponseSize(json2);
            }
            return null;
        }
        return json;
    }

    protected URLConnection createConnection(URL url) throws IOException {
        URLConnection connection = url.openConnection();
        connection.addRequestProperty("User-Agent", "HockeySDK/Android");
        if (Build.VERSION.SDK_INT <= 9) {
            connection.setRequestProperty("connection", "close");
        }
        return connection;
    }

    private boolean findNewVersion(JSONArray json, int versionCode) {
        for (int index = 0; index < json.length(); index++) {
            try {
                JSONObject entry = json.getJSONObject(index);
                boolean largerVersionCode = entry.getInt(ServerProtocol.FALLBACK_DIALOG_PARAM_VERSION) > versionCode;
                boolean newerApkFile = entry.getInt(ServerProtocol.FALLBACK_DIALOG_PARAM_VERSION) == versionCode && VersionHelper.isNewerThanLastUpdateTime(this.context, entry.getLong("timestamp"));
                boolean minRequirementsMet = VersionHelper.compareVersionStrings(entry.getString("minimum_os_version"), VersionHelper.mapGoogleVersion(Build.VERSION.RELEASE)) <= 0;
                if ((largerVersionCode || newerApkFile) && minRequirementsMet) {
                    if (entry.has("mandatory")) {
                        this.mandatory = Boolean.valueOf(entry.getBoolean("mandatory"));
                        return true;
                    } else {
                        return true;
                    }
                }
            } catch (JSONException e) {
                return false;
            }
        }
        return false;
    }

    private JSONArray limitResponseSize(JSONArray json) {
        JSONArray result = new JSONArray();
        for (int index = 0; index < Math.min(json.length(), 25); index++) {
            try {
                result.put(json.get(index));
            } catch (JSONException e) {
            }
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public void onPostExecute(JSONArray updateInfo) {
        if (updateInfo != null) {
            if (this.listener != null) {
                this.listener.onUpdateAvailable(updateInfo, getURLString(APK));
            }
        } else if (this.listener != null) {
            this.listener.onNoUpdateAvailable();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void cleanUp() {
        this.urlString = null;
        this.appIdentifier = null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getURLString(String format) {
        StringBuilder builder = new StringBuilder();
        builder.append(this.urlString);
        builder.append("api/2/apps/");
        builder.append(this.appIdentifier != null ? this.appIdentifier : this.context.getPackageName());
        builder.append("?format=" + format);
        String deviceIdentifier = Settings.Secure.getString(this.context.getContentResolver(), "android_id");
        if (deviceIdentifier != null) {
            builder.append("&udid=" + encodeParam(Settings.Secure.getString(this.context.getContentResolver(), "android_id")));
        }
        builder.append("&os=Android");
        builder.append("&os_version=" + encodeParam(Constants.ANDROID_VERSION));
        builder.append("&device=" + encodeParam(Constants.PHONE_MODEL));
        builder.append("&oem=" + encodeParam(Constants.PHONE_MANUFACTURER));
        builder.append("&app_version=" + encodeParam(Constants.APP_VERSION));
        builder.append("&sdk=" + encodeParam(Constants.SDK_NAME));
        builder.append("&sdk_version=" + encodeParam(Constants.SDK_VERSION));
        builder.append("&lang=" + encodeParam(Locale.getDefault().getLanguage()));
        builder.append("&usage_time=" + this.usageTime);
        return builder.toString();
    }

    private String encodeParam(String param) {
        try {
            return URLEncoder.encode(param, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean getCachingEnabled() {
        return true;
    }

    private static String convertStreamToString(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream), 1024);
        StringBuilder stringBuilder = new StringBuilder();
        while (true) {
            try {
                try {
                    String line = reader.readLine();
                    if (line != null) {
                        stringBuilder.append(line + "\n");
                    } else {
                        try {
                            break;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } finally {
                    try {
                        inputStream.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
            } catch (IOException e3) {
                e3.printStackTrace();
            }
        }
        inputStream.close();
        return stringBuilder.toString();
    }
}
