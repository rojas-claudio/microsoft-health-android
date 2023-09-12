package net.hockeyapp.android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import com.google.android.gms.plus.PlusShare;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.Thread;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.hockeyapp.android.utils.ConnectionManager;
import net.hockeyapp.android.utils.PrefsUtil;
import net.hockeyapp.android.utils.Util;
import org.acra.ACRAConstants;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
/* loaded from: classes.dex */
public class CrashManager {
    private static final String ALWAYS_SEND_KEY = "always_send_crash_reports";
    private static String identifier = null;
    private static String urlString = null;
    private static boolean submitting = false;

    public static void register(Context context, String appIdentifier) {
        register(context, Constants.BASE_URL, appIdentifier, null);
    }

    public static void register(Context context, String appIdentifier, CrashManagerListener listener) {
        register(context, Constants.BASE_URL, appIdentifier, listener);
    }

    public static void register(Context context, String urlString2, String appIdentifier, CrashManagerListener listener) {
        initialize(context, urlString2, appIdentifier, listener, false);
        execute(context, listener);
    }

    public static void initialize(Context context, String appIdentifier, CrashManagerListener listener) {
        initialize(context, Constants.BASE_URL, appIdentifier, listener, true);
    }

    public static void initialize(Context context, String urlString2, String appIdentifier, CrashManagerListener listener) {
        initialize(context, urlString2, appIdentifier, listener, true);
    }

    public static void execute(Context context, CrashManagerListener listener) {
        Boolean ignoreDefaultHandler = Boolean.valueOf(listener != null && listener.ignoreDefaultHandler());
        WeakReference<Context> weakContext = new WeakReference<>(context);
        int foundOrSend = hasStackTraces(weakContext);
        if (foundOrSend == 1) {
            Boolean autoSend = false;
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            Boolean autoSend2 = Boolean.valueOf(autoSend.booleanValue() | prefs.getBoolean(ALWAYS_SEND_KEY, false));
            if (listener != null) {
                autoSend2 = Boolean.valueOf(Boolean.valueOf(autoSend2.booleanValue() | listener.shouldAutoUploadCrashes()).booleanValue() | listener.onCrashesFound());
                listener.onNewCrashesFound();
            }
            if (!autoSend2.booleanValue()) {
                showDialog(weakContext, listener, ignoreDefaultHandler.booleanValue());
            } else {
                sendCrashes(weakContext, listener, ignoreDefaultHandler.booleanValue());
            }
        } else if (foundOrSend == 2) {
            if (listener != null) {
                listener.onConfirmedCrashesFound();
            }
            sendCrashes(weakContext, listener, ignoreDefaultHandler.booleanValue());
        } else {
            registerHandler(weakContext, listener, ignoreDefaultHandler.booleanValue());
        }
    }

    public static int hasStackTraces(WeakReference<Context> weakContext) {
        String[] filenames = searchForStackTraces();
        List<String> confirmedFilenames = null;
        if (filenames == null || filenames.length <= 0) {
            return 0;
        }
        if (weakContext != null) {
            try {
                Context context = weakContext.get();
                if (context != null) {
                    SharedPreferences preferences = context.getSharedPreferences(Constants.SDK_NAME, 0);
                    confirmedFilenames = Arrays.asList(preferences.getString("ConfirmedFilenames", "").split("\\|"));
                }
            } catch (Exception e) {
            }
        }
        if (confirmedFilenames != null) {
            for (String filename : filenames) {
                if (!confirmedFilenames.contains(filename)) {
                    return 1;
                }
            }
            return 2;
        }
        return 1;
    }

    public static void submitStackTraces(WeakReference<Context> weakContext, CrashManagerListener listener) {
        String[] list = searchForStackTraces();
        Boolean successful = false;
        if (list == null || list.length <= 0) {
            return;
        }
        Log.d("HockeyApp", "Found " + list.length + " stacktrace(s).");
        for (int index = 0; index < list.length; index++) {
            try {
                try {
                    String filename = list[index];
                    String stacktrace = contentsOfFile(weakContext, filename);
                    if (stacktrace.length() > 0) {
                        Log.d("HockeyApp", "Transmitting crash data: \n" + stacktrace);
                        DefaultHttpClient httpClient = ConnectionManager.getInstance().getHttpClient();
                        HttpPost httpPost = new HttpPost(getURLString());
                        List<NameValuePair> parameters = new ArrayList<>();
                        parameters.add(new BasicNameValuePair("raw", stacktrace));
                        parameters.add(new BasicNameValuePair("userID", contentsOfFile(weakContext, filename.replace(ACRAConstants.REPORTFILE_EXTENSION, ".user"))));
                        parameters.add(new BasicNameValuePair("contact", contentsOfFile(weakContext, filename.replace(ACRAConstants.REPORTFILE_EXTENSION, ".contact"))));
                        parameters.add(new BasicNameValuePair(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_DESCRIPTION, contentsOfFile(weakContext, filename.replace(ACRAConstants.REPORTFILE_EXTENSION, ".description"))));
                        parameters.add(new BasicNameValuePair("sdk", Constants.SDK_NAME));
                        parameters.add(new BasicNameValuePair("sdk_version", Constants.SDK_VERSION));
                        httpPost.setEntity(new UrlEncodedFormEntity(parameters, "UTF-8"));
                        httpClient.execute(httpPost);
                        successful = true;
                    }
                    if (successful.booleanValue()) {
                        Log.d("HockeyApp", "Transmission succeeded");
                        deleteStackTrace(weakContext, list[index]);
                        if (listener != null) {
                            listener.onCrashesSent();
                        }
                    } else {
                        Log.d("HockeyApp", "Transmission failed, will retry on next register() call");
                        if (listener != null) {
                            listener.onCrashesNotSent();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (successful.booleanValue()) {
                        Log.d("HockeyApp", "Transmission succeeded");
                        deleteStackTrace(weakContext, list[index]);
                        if (listener != null) {
                            listener.onCrashesSent();
                        }
                    } else {
                        Log.d("HockeyApp", "Transmission failed, will retry on next register() call");
                        if (listener != null) {
                            listener.onCrashesNotSent();
                        }
                    }
                }
            } catch (Throwable th) {
                if (successful.booleanValue()) {
                    Log.d("HockeyApp", "Transmission succeeded");
                    deleteStackTrace(weakContext, list[index]);
                    if (listener != null) {
                        listener.onCrashesSent();
                    }
                } else {
                    Log.d("HockeyApp", "Transmission failed, will retry on next register() call");
                    if (listener != null) {
                        listener.onCrashesNotSent();
                    }
                }
                throw th;
            }
        }
    }

    public static void deleteStackTraces(WeakReference<Context> weakContext) {
        String[] list = searchForStackTraces();
        if (list != null && list.length > 0) {
            Log.d("HockeyApp", "Found " + list.length + " stacktrace(s).");
            for (int index = 0; index < list.length; index++) {
                if (weakContext != null) {
                    try {
                        Log.d("HockeyApp", "Delete stacktrace " + list[index] + ".");
                        deleteStackTrace(weakContext, list[index]);
                        Context context = weakContext.get();
                        if (context != null) {
                            context.deleteFile(list[index]);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static void initialize(Context context, String urlString2, String appIdentifier, CrashManagerListener listener, boolean registerHandler) {
        if (context != null) {
            urlString = urlString2;
            identifier = Util.sanitizeAppIdentifier(appIdentifier);
            Constants.loadFromContext(context);
            if (identifier == null) {
                identifier = Constants.APP_PACKAGE;
            }
            if (registerHandler) {
                Boolean ignoreDefaultHandler = Boolean.valueOf(listener != null && listener.ignoreDefaultHandler());
                WeakReference<Context> weakContext = new WeakReference<>(context);
                registerHandler(weakContext, listener, ignoreDefaultHandler.booleanValue());
            }
        }
    }

    private static void showDialog(final WeakReference<Context> weakContext, final CrashManagerListener listener, final boolean ignoreDefaultHandler) {
        Context context = null;
        if (weakContext != null) {
            Context context2 = weakContext.get();
            context = context2;
        }
        if (context != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(Strings.get(listener, 0));
            builder.setMessage(Strings.get(listener, 1));
            builder.setNegativeButton(Strings.get(listener, 2), new DialogInterface.OnClickListener() { // from class: net.hockeyapp.android.CrashManager.1
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    if (CrashManagerListener.this != null) {
                        CrashManagerListener.this.onUserDeniedCrashes();
                    }
                    CrashManager.deleteStackTraces(weakContext);
                    CrashManager.registerHandler(weakContext, CrashManagerListener.this, ignoreDefaultHandler);
                }
            });
            builder.setNeutralButton(Strings.get(listener, 3), new DialogInterface.OnClickListener() { // from class: net.hockeyapp.android.CrashManager.2
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    Context context3 = null;
                    if (weakContext != null) {
                        context3 = (Context) weakContext.get();
                    }
                    if (context3 != null) {
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context3);
                        prefs.edit().putBoolean(CrashManager.ALWAYS_SEND_KEY, true).commit();
                        CrashManager.sendCrashes(weakContext, listener, ignoreDefaultHandler);
                    }
                }
            });
            builder.setPositiveButton(Strings.get(listener, 4), new DialogInterface.OnClickListener() { // from class: net.hockeyapp.android.CrashManager.3
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    CrashManager.sendCrashes(weakContext, listener, ignoreDefaultHandler);
                }
            });
            builder.create().show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Type inference failed for: r0v2, types: [net.hockeyapp.android.CrashManager$4] */
    public static void sendCrashes(final WeakReference<Context> weakContext, final CrashManagerListener listener, boolean ignoreDefaultHandler) {
        saveConfirmedStackTraces(weakContext);
        registerHandler(weakContext, listener, ignoreDefaultHandler);
        if (!submitting) {
            submitting = true;
            new Thread() { // from class: net.hockeyapp.android.CrashManager.4
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    CrashManager.submitStackTraces(weakContext, listener);
                    boolean unused = CrashManager.submitting = false;
                }
            }.start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void registerHandler(WeakReference<Context> weakContext, CrashManagerListener listener, boolean ignoreDefaultHandler) {
        if (Constants.APP_VERSION != null && Constants.APP_PACKAGE != null) {
            Thread.UncaughtExceptionHandler currentHandler = Thread.getDefaultUncaughtExceptionHandler();
            if (currentHandler != null) {
                Log.d("HockeyApp", "Current handler class = " + currentHandler.getClass().getName());
            }
            if (currentHandler instanceof ExceptionHandler) {
                ((ExceptionHandler) currentHandler).setListener(listener);
                return;
            } else {
                Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(currentHandler, listener, ignoreDefaultHandler));
                return;
            }
        }
        Log.d("HockeyApp", "Exception handler not set because version or package is null.");
    }

    private static String getURLString() {
        return urlString + "api/2/apps/" + identifier + "/crashes/";
    }

    private static void deleteStackTrace(WeakReference<Context> weakContext, String filename) {
        Context context;
        if (weakContext != null && (context = weakContext.get()) != null) {
            context.deleteFile(filename);
            String user = filename.replace(ACRAConstants.REPORTFILE_EXTENSION, ".user");
            context.deleteFile(user);
            String contact = filename.replace(ACRAConstants.REPORTFILE_EXTENSION, ".contact");
            context.deleteFile(contact);
            String description = filename.replace(ACRAConstants.REPORTFILE_EXTENSION, ".description");
            context.deleteFile(description);
        }
    }

    private static String contentsOfFile(WeakReference<Context> weakContext, String filename) {
        Context context;
        if (weakContext == null || (context = weakContext.get()) == null) {
            return null;
        }
        StringBuilder contents = new StringBuilder();
        BufferedReader reader = null;
        try {
            try {
                BufferedReader reader2 = new BufferedReader(new InputStreamReader(context.openFileInput(filename)));
                while (true) {
                    try {
                        String line = reader2.readLine();
                        if (line == null) {
                            break;
                        }
                        contents.append(line);
                        contents.append(System.getProperty("line.separator"));
                    } catch (FileNotFoundException e) {
                        reader = reader2;
                        if (reader != null) {
                            try {
                                reader.close();
                            } catch (IOException e2) {
                            }
                        }
                        return contents.toString();
                    } catch (IOException e3) {
                        e = e3;
                        reader = reader2;
                        e.printStackTrace();
                        if (reader != null) {
                            try {
                                reader.close();
                            } catch (IOException e4) {
                            }
                        }
                        return contents.toString();
                    } catch (Throwable th) {
                        th = th;
                        reader = reader2;
                        if (reader != null) {
                            try {
                                reader.close();
                            } catch (IOException e5) {
                            }
                        }
                        throw th;
                    }
                }
                if (reader2 != null) {
                    try {
                        reader2.close();
                        reader = reader2;
                    } catch (IOException e6) {
                        reader = reader2;
                    }
                } else {
                    reader = reader2;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (FileNotFoundException e7) {
        } catch (IOException e8) {
            e = e8;
        }
        return contents.toString();
    }

    private static void saveConfirmedStackTraces(WeakReference<Context> weakContext) {
        Context context;
        if (weakContext != null && (context = weakContext.get()) != null) {
            try {
                String[] filenames = searchForStackTraces();
                SharedPreferences preferences = context.getSharedPreferences(Constants.SDK_NAME, 0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("ConfirmedFilenames", joinArray(filenames, "|"));
                PrefsUtil.applyChanges(editor);
            } catch (Exception e) {
            }
        }
    }

    private static String joinArray(String[] array, String delimiter) {
        StringBuffer buffer = new StringBuffer();
        for (int index = 0; index < array.length; index++) {
            buffer.append(array[index]);
            if (index < array.length - 1) {
                buffer.append(delimiter);
            }
        }
        return buffer.toString();
    }

    private static String[] searchForStackTraces() {
        if (Constants.FILES_PATH != null) {
            Log.d("HockeyApp", "Looking for exceptions in: " + Constants.FILES_PATH);
            File dir = new File(Constants.FILES_PATH + "/");
            boolean created = dir.mkdir();
            if (!created && !dir.exists()) {
                return new String[0];
            }
            FilenameFilter filter = new FilenameFilter() { // from class: net.hockeyapp.android.CrashManager.5
                @Override // java.io.FilenameFilter
                public boolean accept(File dir2, String name) {
                    return name.endsWith(ACRAConstants.REPORTFILE_EXTENSION);
                }
            };
            return dir.list(filter);
        }
        Log.d("HockeyApp", "Can't search for exception as file path is null.");
        return null;
    }
}
