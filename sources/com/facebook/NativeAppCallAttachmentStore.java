package com.facebook;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import com.facebook.NativeAppCallContentProvider;
import com.facebook.internal.Utility;
import com.facebook.internal.Validate;
import com.microsoft.kapp.utils.Constants;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
/* loaded from: classes.dex */
public final class NativeAppCallAttachmentStore implements NativeAppCallContentProvider.AttachmentDataSource {
    static final String ATTACHMENTS_DIR_NAME = "com.facebook.NativeAppCallAttachmentStore.files";
    private static final String TAG = NativeAppCallAttachmentStore.class.getName();
    private static File attachmentsDirectory;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface ProcessAttachment<T> {
        void processAttachment(T t, File file) throws IOException;
    }

    public void addAttachmentsForCall(Context context, UUID callId, Map<String, Bitmap> imageAttachments) {
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO);
        Validate.notNull(callId, "callId");
        Validate.containsNoNulls(imageAttachments.values(), "imageAttachments");
        Validate.containsNoNullOrEmpty(imageAttachments.keySet(), "imageAttachments");
        addAttachments(context, callId, imageAttachments, new ProcessAttachment<Bitmap>() { // from class: com.facebook.NativeAppCallAttachmentStore.1
            @Override // com.facebook.NativeAppCallAttachmentStore.ProcessAttachment
            public void processAttachment(Bitmap attachment, File outputFile) throws IOException {
                FileOutputStream outputStream = new FileOutputStream(outputFile);
                try {
                    attachment.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                } finally {
                    Utility.closeQuietly(outputStream);
                }
            }
        });
    }

    public void addAttachmentFilesForCall(Context context, UUID callId, Map<String, File> imageAttachmentFiles) {
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO);
        Validate.notNull(callId, "callId");
        Validate.containsNoNulls(imageAttachmentFiles.values(), "imageAttachmentFiles");
        Validate.containsNoNullOrEmpty(imageAttachmentFiles.keySet(), "imageAttachmentFiles");
        addAttachments(context, callId, imageAttachmentFiles, new ProcessAttachment<File>() { // from class: com.facebook.NativeAppCallAttachmentStore.2
            @Override // com.facebook.NativeAppCallAttachmentStore.ProcessAttachment
            public void processAttachment(File attachment, File outputFile) throws IOException {
                FileOutputStream outputStream = new FileOutputStream(outputFile);
                FileInputStream inputStream = null;
                try {
                    FileInputStream inputStream2 = new FileInputStream(attachment);
                    try {
                        byte[] buffer = new byte[1024];
                        while (true) {
                            int len = inputStream2.read(buffer);
                            if (len > 0) {
                                outputStream.write(buffer, 0, len);
                            } else {
                                Utility.closeQuietly(outputStream);
                                Utility.closeQuietly(inputStream2);
                                return;
                            }
                        }
                    } catch (Throwable th) {
                        th = th;
                        inputStream = inputStream2;
                        Utility.closeQuietly(outputStream);
                        Utility.closeQuietly(inputStream);
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
            }
        });
    }

    private <T> void addAttachments(Context context, UUID callId, Map<String, T> attachments, ProcessAttachment<T> processor) {
        if (attachments.size() != 0) {
            if (attachmentsDirectory == null) {
                cleanupAllAttachments(context);
            }
            ensureAttachmentsDirectoryExists(context);
            List<File> filesToCleanup = new ArrayList<>();
            try {
                for (Map.Entry<String, T> entry : attachments.entrySet()) {
                    String attachmentName = entry.getKey();
                    T attachment = entry.getValue();
                    File file = getAttachmentFile(callId, attachmentName, true);
                    filesToCleanup.add(file);
                    processor.processAttachment(attachment, file);
                }
            } catch (IOException exception) {
                Log.e(TAG, "Got unexpected exception:" + exception);
                for (File file2 : filesToCleanup) {
                    try {
                        file2.delete();
                    } catch (Exception e) {
                    }
                }
                throw new FacebookException(exception);
            }
        }
    }

    public void cleanupAttachmentsForCall(Context context, UUID callId) {
        File dir = getAttachmentsDirectoryForCall(callId, false);
        Utility.deleteDirectory(dir);
    }

    @Override // com.facebook.NativeAppCallContentProvider.AttachmentDataSource
    public File openAttachment(UUID callId, String attachmentName) throws FileNotFoundException {
        if (Utility.isNullOrEmpty(attachmentName) || callId == null) {
            throw new FileNotFoundException();
        }
        try {
            return getAttachmentFile(callId, attachmentName, false);
        } catch (IOException e) {
            throw new FileNotFoundException();
        }
    }

    static synchronized File getAttachmentsDirectory(Context context) {
        File file;
        synchronized (NativeAppCallAttachmentStore.class) {
            if (attachmentsDirectory == null) {
                attachmentsDirectory = new File(context.getCacheDir(), ATTACHMENTS_DIR_NAME);
            }
            file = attachmentsDirectory;
        }
        return file;
    }

    File ensureAttachmentsDirectoryExists(Context context) {
        File dir = getAttachmentsDirectory(context);
        dir.mkdirs();
        return dir;
    }

    File getAttachmentsDirectoryForCall(UUID callId, boolean create) {
        if (attachmentsDirectory == null) {
            return null;
        }
        File dir = new File(attachmentsDirectory, callId.toString());
        if (create && !dir.exists()) {
            dir.mkdirs();
            return dir;
        }
        return dir;
    }

    File getAttachmentFile(UUID callId, String attachmentName, boolean createDirs) throws IOException {
        File dir = getAttachmentsDirectoryForCall(callId, createDirs);
        if (dir == null) {
            return null;
        }
        try {
            return new File(dir, URLEncoder.encode(attachmentName, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    void cleanupAllAttachments(Context context) {
        File dir = getAttachmentsDirectory(context);
        Utility.deleteDirectory(dir);
    }
}
