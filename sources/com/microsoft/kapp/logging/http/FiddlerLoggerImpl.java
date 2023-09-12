package com.microsoft.kapp.logging.http;

import android.content.Context;
import android.util.Xml;
import com.microsoft.band.util.StreamUtils;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.logging.LogConfiguration;
import com.microsoft.kapp.logging.models.LogMode;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.utils.FileUtils;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import org.joda.time.DateTime;
import org.xmlpull.v1.XmlSerializer;
/* loaded from: classes.dex */
public class FiddlerLoggerImpl implements FiddlerLogger {
    private static final String FIDDLER_CACHE_FOLDER = "fiddler";
    private static final String FIDDLER_FILENAME = "fiddler.saz";
    private static final String FILENAME_FORMAT_STR = "%05d";
    private static final String FOLDER_RAW = "raw";
    private static final int MAX_SESSIONS = 100;
    private static final int MAX_SESSION_NUMBER = 10000;
    private static final String PROTOCOL = "HTTP";
    private static final String PROTOCOL_VERSION = "1.1";
    private static final String TAG = FiddlerLoggerImpl.class.getName();
    private Context mContext;
    private int mCurrentTransactionID;
    private LogConfiguration mLogConfiguration;
    private SettingsProvider mSettingsProvider;
    private int mStartTransactionID;

    public FiddlerLoggerImpl(Context context, SettingsProvider settingsProvider, LogConfiguration logConfiguration) {
        this.mContext = context;
        this.mSettingsProvider = settingsProvider;
        this.mLogConfiguration = logConfiguration;
        this.mStartTransactionID = this.mSettingsProvider.getFiddlerStartSessionId();
        this.mCurrentTransactionID = this.mSettingsProvider.getFiddlerCurrentSessionId();
        File fiddlerDir = new File(this.mContext.getFilesDir(), FIDDLER_CACHE_FOLDER + File.separator + FOLDER_RAW + File.separator);
        fiddlerDir.mkdirs();
    }

    @Override // com.microsoft.kapp.logging.http.FiddlerLogger
    public synchronized void logHttpCall(HttpTransaction transaction) {
        if (this.mLogConfiguration.getLogMode() != LogMode.DO_NOT_LOG_PRIVATE_DATA) {
            writeRawRequestFile(transaction);
            writeRawResponseFile(transaction);
            writeMetadataFile(transaction);
            this.mCurrentTransactionID++;
            if (this.mCurrentTransactionID > MAX_SESSION_NUMBER) {
                cleanup();
            } else if (this.mCurrentTransactionID - this.mStartTransactionID > 100) {
                while (this.mCurrentTransactionID - this.mStartTransactionID > 100) {
                    deleteSession(this.mStartTransactionID);
                    this.mStartTransactionID++;
                }
            }
            this.mSettingsProvider.setFiddlerCurrentSessionId(this.mCurrentTransactionID);
            this.mSettingsProvider.setFiddlerStartSessionId(this.mStartTransactionID);
        }
    }

    private void writeMetadataFile(HttpTransaction transaction) {
        FileOutputStream fileOutputStream;
        OutputStreamWriter outputStreamWriter;
        FileOutputStream fileOutputStream2 = null;
        OutputStreamWriter outputStreamWriter2 = null;
        try {
            try {
                fileOutputStream = new FileOutputStream(getMetadataFileForSession(this.mCurrentTransactionID));
                try {
                    outputStreamWriter = new OutputStreamWriter(fileOutputStream);
                } catch (Exception e) {
                    ex = e;
                    fileOutputStream2 = fileOutputStream;
                } catch (Throwable th) {
                    th = th;
                    fileOutputStream2 = fileOutputStream;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Exception e2) {
            ex = e2;
        }
        try {
            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(outputStreamWriter);
            serializer.startDocument("UTF-8", true);
            serializer.startTag("", "Session");
            serializer.attribute("", "SID", String.valueOf(this.mCurrentTransactionID));
            serializer.startTag("", "SessionTimers");
            DateTime requestTime = transaction.getRequestTime();
            if (requestTime != null) {
                serializer.attribute("", "ClientConnected", requestTime.toString());
                serializer.attribute("", "ClientBeginRequest", requestTime.toString());
                serializer.attribute("", "GotRequestHeaders", requestTime.toString());
                serializer.attribute("", "ClientDoneRequest", requestTime.toString());
                serializer.attribute("", "ServerConnected", requestTime.toString());
                serializer.attribute("", "FiddlerBeginRequest", requestTime.toString());
                serializer.attribute("", "ServerGotRequest", requestTime.toString());
            }
            DateTime responseHeadersTime = transaction.getResponseHeadersTime();
            if (responseHeadersTime != null) {
                serializer.attribute("", "ServerBeginResponse", responseHeadersTime.toString());
                serializer.attribute("", "GotResponseHeaders", responseHeadersTime.toString());
                serializer.attribute("", "ServerDoneResponse", responseHeadersTime.toString());
                serializer.attribute("", "ClientBeginResponse", responseHeadersTime.toString());
            }
            DateTime responseTime = transaction.getResponseTime();
            if (responseTime != null) {
                serializer.attribute("", "ClientDoneResponse", responseTime.toString());
            }
            serializer.endTag("", "SessionTimers");
            serializer.startTag("", "SessionFlags");
            serializer.startTag("", "SessionFlag");
            serializer.attribute("", "N", "x-processinfo");
            serializer.attribute("", "V", "fiddler-on-android");
            serializer.endTag("", "SessionFlag");
            serializer.endTag("", "SessionFlags");
            serializer.endTag("", "Session");
            serializer.flush();
            outputStreamWriter.flush();
            StreamUtils.closeQuietly(outputStreamWriter);
            StreamUtils.closeQuietly(fileOutputStream);
            outputStreamWriter2 = outputStreamWriter;
            fileOutputStream2 = fileOutputStream;
        } catch (Exception e3) {
            ex = e3;
            outputStreamWriter2 = outputStreamWriter;
            fileOutputStream2 = fileOutputStream;
            KLog.d(TAG, "writeMetadataFile", ex);
            StreamUtils.closeQuietly(outputStreamWriter2);
            StreamUtils.closeQuietly(fileOutputStream2);
        } catch (Throwable th3) {
            th = th3;
            outputStreamWriter2 = outputStreamWriter;
            fileOutputStream2 = fileOutputStream;
            StreamUtils.closeQuietly(outputStreamWriter2);
            StreamUtils.closeQuietly(fileOutputStream2);
            throw th;
        }
    }

    private void writeRawResponseFile(HttpTransaction transaction) {
        FileOutputStream fileOutputStream;
        BufferedOutputStream bufferedOutputStream;
        FileOutputStream fileOutputStream2 = null;
        BufferedOutputStream bufferedOutputStream2 = null;
        try {
            try {
                fileOutputStream = new FileOutputStream(getResponseFileForSession(this.mCurrentTransactionID));
                try {
                    bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
                } catch (Exception e) {
                    ex = e;
                    fileOutputStream2 = fileOutputStream;
                } catch (Throwable th) {
                    th = th;
                    fileOutputStream2 = fileOutputStream;
                }
            } catch (Exception e2) {
                ex = e2;
            }
        } catch (Throwable th2) {
            th = th2;
        }
        try {
            bufferedOutputStream.write(String.format("%s/%s %s %s\n", PROTOCOL, PROTOCOL_VERSION, Integer.valueOf(transaction.getStatusCode()), transaction.getReason()).getBytes());
            KHTTPHeader[] responseHeaders = transaction.getResponseHeaders();
            if (responseHeaders != null) {
                for (KHTTPHeader header : responseHeaders) {
                    bufferedOutputStream.write(String.format("%s: %s\n", header.getName(), header.getValue()).getBytes());
                }
            }
            bufferedOutputStream.write("\n".getBytes());
            byte[] response = transaction.getResponse();
            if (response != null) {
                bufferedOutputStream.write(response);
            }
            bufferedOutputStream.flush();
            StreamUtils.closeQuietly(bufferedOutputStream);
            StreamUtils.closeQuietly(fileOutputStream);
        } catch (Exception e3) {
            ex = e3;
            bufferedOutputStream2 = bufferedOutputStream;
            fileOutputStream2 = fileOutputStream;
            KLog.d(TAG, "writeRawResponseFile", ex);
            StreamUtils.closeQuietly(bufferedOutputStream2);
            StreamUtils.closeQuietly(fileOutputStream2);
        } catch (Throwable th3) {
            th = th3;
            bufferedOutputStream2 = bufferedOutputStream;
            fileOutputStream2 = fileOutputStream;
            StreamUtils.closeQuietly(bufferedOutputStream2);
            StreamUtils.closeQuietly(fileOutputStream2);
            throw th;
        }
    }

    private void writeRawRequestFile(HttpTransaction transaction) {
        FileOutputStream fileOutputStream;
        BufferedOutputStream bufferedOutputStream;
        FileOutputStream fileOutputStream2 = null;
        BufferedOutputStream bufferedOutputStream2 = null;
        try {
            try {
                fileOutputStream = new FileOutputStream(getRequestFileForSession(this.mCurrentTransactionID));
                try {
                    bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
                } catch (Exception e) {
                    ex = e;
                    fileOutputStream2 = fileOutputStream;
                } catch (Throwable th) {
                    th = th;
                    fileOutputStream2 = fileOutputStream;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Exception e2) {
            ex = e2;
        }
        try {
            bufferedOutputStream.write(String.format("%s %s %s/%s\n", transaction.getMethod(), transaction.getUri(), PROTOCOL, PROTOCOL_VERSION).getBytes());
            boolean hasHost = false;
            KHTTPHeader[] requestHeaders = transaction.getRequestHeaders();
            if (requestHeaders != null) {
                for (KHTTPHeader header : requestHeaders) {
                    bufferedOutputStream.write(String.format("%s: %s\n", header.getName(), header.getValue()).getBytes());
                    if ("Host".equalsIgnoreCase(header.getName())) {
                        hasHost = true;
                    }
                }
            }
            if (!hasHost) {
                bufferedOutputStream.write(String.format("%s: %s\n", "Host", transaction.getUri().getHost()).getBytes());
            }
            bufferedOutputStream.write("\n".getBytes());
            byte[] request = transaction.getRequest();
            if (request != null) {
                bufferedOutputStream.write(request);
            }
            bufferedOutputStream.flush();
            StreamUtils.closeQuietly(bufferedOutputStream);
            StreamUtils.closeQuietly(fileOutputStream);
            bufferedOutputStream2 = bufferedOutputStream;
            fileOutputStream2 = fileOutputStream;
        } catch (Exception e3) {
            ex = e3;
            bufferedOutputStream2 = bufferedOutputStream;
            fileOutputStream2 = fileOutputStream;
            KLog.d(TAG, "writeRawRequestFile", ex);
            StreamUtils.closeQuietly(bufferedOutputStream2);
            StreamUtils.closeQuietly(fileOutputStream2);
        } catch (Throwable th3) {
            th = th3;
            bufferedOutputStream2 = bufferedOutputStream;
            fileOutputStream2 = fileOutputStream;
            StreamUtils.closeQuietly(bufferedOutputStream2);
            StreamUtils.closeQuietly(fileOutputStream2);
            throw th;
        }
    }

    @Override // com.microsoft.kapp.logging.http.FiddlerLogger
    public synchronized String createArchive(String baseFolderLocation) {
        File outputFile;
        writeContentTypesFile();
        File outputHttpDir = new File(baseFolderLocation, "http");
        outputHttpDir.mkdirs();
        outputFile = new File(outputHttpDir, FIDDLER_FILENAME);
        outputFile.delete();
        return FileUtils.zipFilesInDir(new File(this.mContext.getFilesDir(), FIDDLER_CACHE_FOLDER), outputFile) ? outputFile.getPath() : null;
    }

    private void writeContentTypesFile() {
        FileOutputStream fileOutputStream;
        FileOutputStream fileOutputStream2 = null;
        try {
            File fiddlerDir = new File(this.mContext.getFilesDir(), FIDDLER_CACHE_FOLDER);
            fileOutputStream = new FileOutputStream(new File(fiddlerDir, "[Content_Types].xml"));
        } catch (IOException e) {
        } catch (Throwable th) {
            th = th;
        }
        try {
            fileOutputStream.write("<?xml version=\"1.0\" encoding=\"utf-8\" ?><Types xmlns=\"http://schemas.openxmlformats.org/package/2006/content-types\"><Default Extension=\"htm\" ContentType=\"text/html\" /><Default Extension=\"xml\" ContentType=\"application/xml\" /><Default Extension=\"txt\" ContentType=\"text/plain\" /></Types>".getBytes());
            StreamUtils.closeQuietly(fileOutputStream);
        } catch (IOException e2) {
            fileOutputStream2 = fileOutputStream;
            StreamUtils.closeQuietly(fileOutputStream2);
        } catch (Throwable th2) {
            th = th2;
            fileOutputStream2 = fileOutputStream;
            StreamUtils.closeQuietly(fileOutputStream2);
            throw th;
        }
    }

    private File getRequestFileForSession(int sessionID) {
        File fiddlerDir = new File(this.mContext.getFilesDir(), FIDDLER_CACHE_FOLDER);
        return new File(fiddlerDir, File.separator + FOLDER_RAW + File.separator + String.format(FILENAME_FORMAT_STR, Integer.valueOf(sessionID)) + "_c.txt");
    }

    private File getResponseFileForSession(int sessionID) {
        File fiddlerDir = new File(this.mContext.getFilesDir(), FIDDLER_CACHE_FOLDER);
        return new File(fiddlerDir, File.separator + FOLDER_RAW + File.separator + String.format(FILENAME_FORMAT_STR, Integer.valueOf(sessionID)) + "_s.txt");
    }

    private File getMetadataFileForSession(int sessionID) {
        File fiddlerDir = new File(this.mContext.getFilesDir(), FIDDLER_CACHE_FOLDER);
        return new File(fiddlerDir, File.separator + FOLDER_RAW + File.separator + String.format(FILENAME_FORMAT_STR, Integer.valueOf(sessionID)) + "_m.xml");
    }

    @Override // com.microsoft.kapp.logging.http.FiddlerLogger
    public synchronized void cleanup() {
        File fiddlerDir = new File(this.mContext.getFilesDir(), FIDDLER_CACHE_FOLDER);
        FileUtils.cleanupDir(fiddlerDir);
        this.mCurrentTransactionID = 1;
        this.mStartTransactionID = 1;
        this.mSettingsProvider.setFiddlerCurrentSessionId(this.mCurrentTransactionID);
        this.mSettingsProvider.setFiddlerStartSessionId(this.mStartTransactionID);
    }

    private void deleteSession(int sessionID) {
        getRequestFileForSession(sessionID).delete();
        getResponseFileForSession(sessionID).delete();
        getMetadataFileForSession(sessionID).delete();
    }
}
