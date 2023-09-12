package com.microsoft.applicationinsights.library;

import android.content.Context;
import com.microsoft.applicationinsights.contracts.shared.IJsonSerializable;
import com.microsoft.applicationinsights.logging.InternalLogging;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.UUID;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class Persistence {
    private static final String AI_SDK_DIRECTORY = "/com.microsoft.applicationinsights";
    private static final String HIGH_PRIO_DIRECTORY = "/highpriority/";
    private static final String REGULAR_PRIO_DIRECTORY = "/regularpriority/";
    private static final String TAG = "Persistence";
    private static Persistence instance;
    private final ArrayList<File> servedFiles;
    private WeakReference<Context> weakContext;
    private static volatile boolean isPersistenceLoaded = false;
    private static final Object LOCK = new Object();
    private static final Integer MAX_FILE_COUNT = 50;

    protected Persistence(Context context) {
        this.weakContext = new WeakReference<>(context);
        createDirectoriesIfNecessary();
        this.servedFiles = new ArrayList<>(51);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static void initialize(Context context) {
        if (!isPersistenceLoaded) {
            synchronized (LOCK) {
                if (!isPersistenceLoaded) {
                    isPersistenceLoaded = true;
                    instance = new Persistence(context);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static Persistence getInstance() {
        if (instance == null) {
            InternalLogging.error(TAG, "getInstance was called before initialization");
        }
        return instance;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void persist(IJsonSerializable[] data, Boolean highPriority) {
        Sender sender;
        if (!isFreeSpaceAvailable(highPriority).booleanValue()) {
            InternalLogging.warn(TAG, "No free space on disk to flush data.");
            Sender.getInstance().send();
            return;
        }
        StringBuilder buffer = new StringBuilder();
        try {
            buffer.append('[');
            for (int i = 0; i < data.length; i++) {
                if (i > 0) {
                    buffer.append(',');
                }
                StringWriter stringWriter = new StringWriter();
                data[i].serialize(stringWriter);
                buffer.append(stringWriter.toString());
            }
            buffer.append(']');
            String serializedData = buffer.toString();
            Boolean isSuccess = Boolean.valueOf(persist(serializedData, highPriority));
            if (isSuccess.booleanValue() && (sender = Sender.getInstance()) != null) {
                sender.send();
            }
        } catch (IOException e) {
            InternalLogging.warn(TAG, "Failed to save data with exception: " + e.toString());
        }
    }

    protected boolean persist(String data, Boolean highPriority) {
        FileOutputStream outputStream;
        String uuid = UUID.randomUUID().toString();
        Boolean isSuccess = false;
        Context context = getContext();
        if (context != null) {
            try {
                File filesDir = getContext().getFilesDir();
                if (highPriority.booleanValue()) {
                    outputStream = new FileOutputStream(new File(filesDir + AI_SDK_DIRECTORY + HIGH_PRIO_DIRECTORY + uuid), true);
                } else {
                    outputStream = new FileOutputStream(new File(filesDir + AI_SDK_DIRECTORY + REGULAR_PRIO_DIRECTORY + uuid), true);
                }
                outputStream.write(data.getBytes());
                outputStream.close();
                isSuccess = true;
            } catch (Exception e) {
                InternalLogging.warn(TAG, "Failed to save data with exception: " + e.toString());
            }
        }
        return isSuccess.booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String load(File file) {
        StringBuilder buffer = new StringBuilder();
        if (file != null) {
            try {
                FileInputStream inputStream = new FileInputStream(file);
                InputStreamReader streamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(streamReader);
                while (true) {
                    String str = reader.readLine();
                    if (str == null) {
                        break;
                    }
                    buffer.append(str);
                }
                reader.close();
            } catch (Exception e) {
                InternalLogging.warn(TAG, "Error reading telemetry data from file with exception message " + e.getMessage());
            }
        }
        return buffer.toString();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public File nextAvailableFile() {
        File file = nextHighPrioFile();
        return file != null ? file : nextRegularPrioFile();
    }

    private File nextHighPrioFile() {
        Context context = getContext();
        if (context != null) {
            String path = context.getFilesDir() + AI_SDK_DIRECTORY + HIGH_PRIO_DIRECTORY;
            File directory = new File(path);
            return nextAvailableFileInDirectory(directory);
        }
        InternalLogging.warn(TAG, "Couldn't provide next file, the context for persistence is null");
        return null;
    }

    private File nextRegularPrioFile() {
        Context context = getContext();
        if (context != null) {
            String path = context.getFilesDir() + AI_SDK_DIRECTORY + REGULAR_PRIO_DIRECTORY;
            File directory = new File(path);
            return nextAvailableFileInDirectory(directory);
        }
        InternalLogging.warn(TAG, "Couldn't provide next file, the context for persistence is null");
        return null;
    }

    private File nextAvailableFileInDirectory(File directory) {
        File file;
        synchronized (LOCK) {
            if (directory != null) {
                File[] files = directory.listFiles();
                if (files != null && files.length > 0) {
                    for (int i = 0; i < files.length - 1; i++) {
                        file = files[i];
                        if (!this.servedFiles.contains(file)) {
                            this.servedFiles.add(file);
                            break;
                        }
                    }
                }
            }
            file = null;
        }
        return file;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void deleteFile(File file) {
        if (file != null) {
            synchronized (LOCK) {
                boolean deletedFile = file.delete();
                if (!deletedFile) {
                    InternalLogging.warn(TAG, "Error deleting telemetry file " + file.toString());
                } else {
                    this.servedFiles.remove(file);
                }
            }
            return;
        }
        InternalLogging.warn(TAG, "Couldn't delete file, the reference to the file was null");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void makeAvailable(File file) {
        synchronized (LOCK) {
            if (file != null) {
                this.servedFiles.remove(file);
            }
        }
    }

    private Boolean isFreeSpaceAvailable(Boolean highPriority) {
        boolean z;
        synchronized (LOCK) {
            Context context = getContext();
            if (context != null) {
                String path = highPriority.booleanValue() ? context.getFilesDir() + AI_SDK_DIRECTORY + HIGH_PRIO_DIRECTORY : getContext().getFilesDir() + AI_SDK_DIRECTORY + REGULAR_PRIO_DIRECTORY;
                File dir = new File(path);
                z = Boolean.valueOf(dir.listFiles().length < MAX_FILE_COUNT.intValue());
            } else {
                z = false;
            }
        }
        return z;
    }

    private void createDirectoriesIfNecessary() {
        String filesDirPath = getContext().getFilesDir().getPath();
        File dir = new File(filesDirPath + AI_SDK_DIRECTORY + HIGH_PRIO_DIRECTORY);
        if (!dir.exists()) {
            if (dir.mkdirs()) {
                InternalLogging.info(TAG, "Successfully created regular directory", "high priority");
            } else {
                InternalLogging.info(TAG, "Error creating directory", "high priority");
            }
        }
        File dir2 = new File(filesDirPath + AI_SDK_DIRECTORY + REGULAR_PRIO_DIRECTORY);
        if (!dir2.exists()) {
            if (dir2.mkdirs()) {
                InternalLogging.info(TAG, "Successfully created regular directory", "regular priority");
            } else {
                InternalLogging.info(TAG, "Error creating directory", "regular priority");
            }
        }
    }

    private Context getContext() {
        if (this.weakContext == null) {
            return null;
        }
        Context context = this.weakContext.get();
        return context;
    }
}
