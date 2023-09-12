package com.microsoft.kapp.logging.images;

import android.content.Context;
import android.graphics.Bitmap;
import com.microsoft.band.util.StreamUtils;
import com.microsoft.kapp.logging.BaseLogger;
import com.microsoft.kapp.logging.LogConstants;
import com.microsoft.kapp.logging.Logger;
import com.microsoft.kapp.logging.models.LogEntry;
import com.microsoft.kapp.utils.FileUtils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.UUID;
/* loaded from: classes.dex */
public class ImageLogger extends BaseLogger {
    private Logger mMessageLogger;

    public ImageLogger(Context context, Logger messageLogger) {
        super(context);
        this.mMessageLogger = messageLogger;
    }

    @Override // com.microsoft.kapp.logging.Logger
    public void packageLogs(String baseDir) {
        FileInputStream fileIn = null;
        FileOutputStream fileOut = null;
        try {
            File outputImagesDir = new File(baseDir, LogConstants.DIAGNOSTIC_IMAGES_FOLDER);
            outputImagesDir.mkdirs();
            File imageDir = getImageDir();
            if (imageDir.isDirectory()) {
                File[] imageFiles = imageDir.listFiles();
                int len$ = imageFiles.length;
                int i$ = 0;
                FileOutputStream fileOut2 = null;
                FileInputStream fileIn2 = null;
                while (i$ < len$) {
                    try {
                        File file = imageFiles[i$];
                        fileIn = new FileInputStream(file);
                        try {
                            File outputFile = new File(outputImagesDir, file.getName());
                            fileOut = new FileOutputStream(outputFile, false);
                            FileUtils.copyStream(fileIn, fileOut);
                            fileIn.close();
                            fileOut.close();
                            i$++;
                            fileOut2 = fileOut;
                            fileIn2 = fileIn;
                        } catch (Exception e) {
                            fileOut = fileOut2;
                            StreamUtils.closeQuietly(fileOut);
                            StreamUtils.closeQuietly(fileIn);
                            return;
                        } catch (Throwable th) {
                            th = th;
                            fileOut = fileOut2;
                            StreamUtils.closeQuietly(fileOut);
                            StreamUtils.closeQuietly(fileIn);
                            throw th;
                        }
                    } catch (Exception e2) {
                        fileOut = fileOut2;
                        fileIn = fileIn2;
                    } catch (Throwable th2) {
                        th = th2;
                        fileOut = fileOut2;
                        fileIn = fileIn2;
                    }
                }
                fileOut = fileOut2;
                fileIn = fileIn2;
            }
            StreamUtils.closeQuietly(fileOut);
            StreamUtils.closeQuietly(fileIn);
        } catch (Exception e3) {
        } catch (Throwable th3) {
            th = th3;
        }
    }

    @Override // com.microsoft.kapp.logging.BaseLogger, com.microsoft.kapp.logging.Logger
    public void log(LogEntry logentry) {
        if (logentry.getImage() != null) {
            String fileName = saveImageToDisk(logentry.getImage());
            logentry.setFileReference(LogConstants.DIAGNOSTIC_IMAGES_FOLDER + File.separator + fileName);
        }
        this.mMessageLogger.log(logentry);
    }

    private String saveImageToDisk(Bitmap image) {
        String fileName;
        byte[] bitmapdata;
        FileOutputStream fos;
        FileOutputStream fos2 = null;
        try {
            UUID imageName = UUID.randomUUID();
            File imageDir = getImageDir();
            fileName = imageName.toString() + ".png";
            File bitmapFile = new File(imageDir, fileName);
            bitmapFile.createNewFile();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 0, bos);
            bitmapdata = bos.toByteArray();
            fos = new FileOutputStream(bitmapFile);
        } catch (Exception e) {
        } catch (Throwable th) {
            th = th;
        }
        try {
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
            StreamUtils.closeQuietly(fos);
            return fileName;
        } catch (Exception e2) {
            fos2 = fos;
            StreamUtils.closeQuietly(fos2);
            return null;
        } catch (Throwable th2) {
            th = th2;
            fos2 = fos;
            StreamUtils.closeQuietly(fos2);
            throw th;
        }
    }

    private File getImageDir() {
        File diagnosticsDir = new File(this.mContext.getFilesDir(), LogConstants.DIAGNOSTICS_FOLDER);
        File imageDir = new File(diagnosticsDir, LogConstants.DIAGNOSTIC_IMAGES_FOLDER);
        imageDir.mkdirs();
        return imageDir;
    }
}
