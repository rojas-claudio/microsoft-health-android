package com.microsoft.applicationinsights.library;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import com.microsoft.applicationinsights.library.config.ISenderConfig;
import com.microsoft.applicationinsights.logging.InternalLogging;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimerTask;
import java.util.zip.GZIPOutputStream;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class Sender {
    private static final String TAG = "Sender";
    private static Sender instance;
    protected final ISenderConfig config;
    private final HashMap<String, TimerTask> currentTasks = new HashMap<>(10);
    private static volatile boolean isSenderLoaded = false;
    private static final Object LOCK = new Object();

    protected Sender(ISenderConfig config) {
        this.config = config;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static void initialize(ISenderConfig config) {
        if (!isSenderLoaded) {
            synchronized (LOCK) {
                if (!isSenderLoaded) {
                    isSenderLoaded = true;
                    instance = new Sender(config);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static Sender getInstance() {
        if (instance == null) {
            InternalLogging.error(TAG, "getInstance was called before initialization");
        }
        return instance;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Type inference failed for: r0v0, types: [com.microsoft.applicationinsights.library.Sender$1] */
    public void sendDataOnAppStart() {
        new AsyncTask<Void, Void, Void>() { // from class: com.microsoft.applicationinsights.library.Sender.1
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public Void doInBackground(Void... params) {
                Sender.this.send();
                return null;
            }
        }.execute(new Void[0]);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void send() {
        File fileToSend;
        if (runningRequestCount() < 10) {
            Persistence persistence = Persistence.getInstance();
            if (persistence != null && (fileToSend = persistence.nextAvailableFile()) != null) {
                String persistedData = persistence.load(fileToSend);
                if (!persistedData.isEmpty()) {
                    InternalLogging.info(TAG, "sending persisted data", persistedData);
                    SendingTask sendingTask = new SendingTask(persistedData, fileToSend);
                    addToRunning(fileToSend.toString(), sendingTask);
                    sendingTask.run();
                    Thread sendingThread = new Thread(sendingTask);
                    sendingThread.setDaemon(false);
                    return;
                }
                persistence.deleteFile(fileToSend);
                send();
                return;
            }
            return;
        }
        InternalLogging.info(TAG, "We have already 10 pending reguests", "");
    }

    protected void addToRunning(String key, SendingTask task) {
        synchronized (LOCK) {
            this.currentTasks.put(key, task);
        }
    }

    protected void removeFromRunning(String key) {
        synchronized (LOCK) {
            this.currentTasks.remove(key);
        }
    }

    protected int runningRequestCount() {
        int size;
        synchronized (LOCK) {
            size = getInstance().currentTasks.size();
        }
        return size;
    }

    protected void onResponse(HttpURLConnection connection, int responseCode, String payload, File fileToSend) {
        Persistence persistence;
        boolean deleteFile = false;
        removeFromRunning(fileToSend.toString());
        StringBuilder builder = new StringBuilder();
        InternalLogging.info(TAG, "response code", Integer.toString(responseCode));
        boolean isExpected = responseCode > 199 && responseCode < 203;
        boolean isRecoverableError = responseCode == 429 || responseCode == 408 || responseCode == 500 || responseCode == 503 || responseCode == 511;
        if (isExpected || !isRecoverableError) {
            deleteFile = true;
        }
        if (isExpected) {
            onExpected(connection, builder);
            send();
        }
        if (deleteFile && (persistence = Persistence.getInstance()) != null) {
            persistence.deleteFile(fileToSend);
        }
        if (isRecoverableError) {
            onRecoverable(payload, fileToSend);
        }
        if (!isExpected) {
            onUnexpected(connection, responseCode, builder);
        }
    }

    protected void onExpected(HttpURLConnection connection, StringBuilder builder) {
        if (ApplicationInsights.isDeveloperMode()) {
            readResponse(connection, builder);
        }
    }

    protected void onUnexpected(HttpURLConnection connection, int responseCode, StringBuilder builder) {
        String message = String.format(Locale.ROOT, "Unexpected response code: %d", Integer.valueOf(responseCode));
        builder.append(message);
        builder.append("\n");
        InternalLogging.warn(TAG, message);
        readResponse(connection, builder);
    }

    protected void onRecoverable(String payload, File fileToSend) {
        InternalLogging.info(TAG, "Server error, persisting data", payload);
        Persistence persistence = Persistence.getInstance();
        if (persistence != null) {
            persistence.makeAvailable(fileToSend);
        }
    }

    private void readResponse(HttpURLConnection connection, StringBuilder builder) {
        BufferedReader reader = null;
        try {
            try {
                InputStream inputStream = connection.getErrorStream();
                if (inputStream == null) {
                    inputStream = connection.getInputStream();
                }
                if (inputStream != null) {
                    InputStreamReader streamReader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader reader2 = new BufferedReader(streamReader);
                    try {
                        for (String responseLine = reader2.readLine(); responseLine != null; responseLine = reader2.readLine()) {
                            builder.append(responseLine);
                        }
                        reader = reader2;
                    } catch (IOException e) {
                        e = e;
                        reader = reader2;
                        InternalLogging.warn(TAG, e.toString());
                        if (reader != null) {
                            try {
                                reader.close();
                                return;
                            } catch (IOException e2) {
                                InternalLogging.warn(TAG, e2.toString());
                                return;
                            }
                        }
                        return;
                    } catch (Throwable th) {
                        th = th;
                        reader = reader2;
                        if (reader != null) {
                            try {
                                reader.close();
                            } catch (IOException e3) {
                                InternalLogging.warn(TAG, e3.toString());
                            }
                        }
                        throw th;
                    }
                } else {
                    builder.append(connection.getResponseMessage());
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e4) {
                        InternalLogging.warn(TAG, e4.toString());
                    }
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (IOException e5) {
            e = e5;
        }
    }

    @TargetApi(19)
    protected Writer getWriter(HttpURLConnection connection) throws IOException {
        if (Build.VERSION.SDK_INT >= 19) {
            connection.addRequestProperty("Content-Encoding", "gzip");
            connection.setRequestProperty("Content-Type", "application/json");
            GZIPOutputStream gzip = new GZIPOutputStream(connection.getOutputStream(), true);
            return new OutputStreamWriter(gzip);
        }
        return new OutputStreamWriter(connection.getOutputStream());
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class SendingTask extends TimerTask {
        private final File fileToSend;
        private String payload;

        public SendingTask(String payload, File fileToSend) {
            this.payload = payload;
            this.fileToSend = fileToSend;
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            if (this.payload != null) {
                try {
                    InternalLogging.info(Sender.TAG, "sending persisted data", this.payload);
                    sendRequestWithPayload(this.payload);
                } catch (IOException e) {
                    InternalLogging.warn(Sender.TAG, "Couldn't send request with IOException: " + e.toString());
                }
            }
        }

        protected void sendRequestWithPayload(String payload) throws IOException {
            Writer writer = null;
            URL url = new URL(Sender.this.config.getEndpointUrl());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(Sender.this.config.getSenderReadTimeout());
            connection.setConnectTimeout(Sender.this.config.getSenderConnectTimeout());
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            try {
                try {
                    InternalLogging.info(Sender.TAG, "writing payload", payload);
                    writer = Sender.this.getWriter(connection);
                    writer.write(payload);
                    writer.flush();
                    connection.connect();
                    int responseCode = connection.getResponseCode();
                    Sender.this.onResponse(connection, responseCode, payload, this.fileToSend);
                    if (writer != null) {
                        try {
                            writer.close();
                        } catch (IOException e) {
                        }
                    }
                } catch (IOException e2) {
                    InternalLogging.warn(Sender.TAG, "Couldn't send data with IOException: " + e2.toString());
                    Persistence persistence = Persistence.getInstance();
                    if (persistence != null) {
                        persistence.makeAvailable(this.fileToSend);
                    }
                    if (writer != null) {
                        try {
                            writer.close();
                        } catch (IOException e3) {
                        }
                    }
                }
            } catch (Throwable th) {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e4) {
                    }
                }
                throw th;
            }
        }
    }
}
