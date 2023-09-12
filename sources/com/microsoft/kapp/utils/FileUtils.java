package com.microsoft.kapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Pair;
import com.microsoft.band.util.StreamUtils;
import com.microsoft.kapp.logging.KLog;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.lang3.Validate;
/* loaded from: classes.dex */
public class FileUtils {
    private static final int BUFFER_SIZE = 2048;
    private static final String PICTURES_FOLDER = "kapp_pictures";
    private static final String TAG = FileUtils.class.getSimpleName();

    public static File zip(List<Pair<String, InputStream>> inputStreams, File fileDir, String outputFileName) {
        ZipOutputStream zipOutputFileStream;
        byte[] fileData = new byte[2048];
        File file = new File(fileDir, outputFileName);
        String path = file.getAbsolutePath();
        ZipOutputStream zipOutputFileStream2 = null;
        try {
            try {
                FileOutputStream outputFileStream = new FileOutputStream(path);
                zipOutputFileStream = new ZipOutputStream(new BufferedOutputStream(outputFileStream));
            } catch (Exception e) {
                ex = e;
            }
        } catch (Throwable th) {
            th = th;
        }
        try {
            for (Pair<String, InputStream> inputStreamPair : inputStreams) {
                BufferedInputStream bufferInputStream = new BufferedInputStream((InputStream) inputStreamPair.second, 2048);
                ZipEntry zipFileEntry = new ZipEntry((String) inputStreamPair.first);
                try {
                    zipOutputFileStream.putNextEntry(zipFileEntry);
                    while (true) {
                        int count = bufferInputStream.read(fileData, 0, 2048);
                        if (count == -1) {
                            break;
                        }
                        zipOutputFileStream.write(fileData, 0, count);
                    }
                    zipOutputFileStream.closeEntry();
                } finally {
                }
            }
            StreamUtils.closeQuietly(zipOutputFileStream);
        } catch (Exception e2) {
            ex = e2;
            zipOutputFileStream2 = zipOutputFileStream;
            KLog.e(TAG, "zip() failed", ex);
            StreamUtils.closeQuietly(zipOutputFileStream2);
            return file;
        } catch (Throwable th2) {
            th = th2;
            zipOutputFileStream2 = zipOutputFileStream;
            StreamUtils.closeQuietly(zipOutputFileStream2);
            throw th;
        }
        return file;
    }

    public static File saveBitmapToDisk(Bitmap bitmap, String fileName) throws IOException {
        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), PICTURES_FOLDER);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File tmpImage = new File(dir, fileName);
        FileOutputStream fos = new FileOutputStream(tmpImage);
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            return tmpImage;
        } finally {
            StreamUtils.closeQuietly(fos);
        }
    }

    public static File saveBitmapToLocalStorage(Bitmap bitmap, String fileName, Context context) throws IOException {
        File dir = new File(context.getCacheDir(), PICTURES_FOLDER);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File tmpImage = new File(dir, fileName);
        try {
            if (tmpImage.exists()) {
                cleanupDir(tmpImage);
            }
        } catch (Exception e) {
        }
        FileOutputStream fos = new FileOutputStream(tmpImage);
        try {
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, fos);
            return tmpImage;
        } finally {
            StreamUtils.closeQuietly(fos);
        }
    }

    public static void clearLocalImageStorage(Context context) {
        cleanupDir(new File(context.getCacheDir(), PICTURES_FOLDER));
    }

    public static boolean zipFilesInDir(File dir, File zipFile) {
        ZipOutputStream zipOutputStream;
        Validate.notNull(dir, "dir", new Object[0]);
        Validate.notNull(zipFile, "zipfile", new Object[0]);
        ZipOutputStream zipOutputStream2 = null;
        try {
            try {
                zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile));
            } catch (Throwable th) {
                th = th;
            }
        } catch (Exception e) {
            ex = e;
        }
        try {
            addDir(dir, dir, zipOutputStream);
            StreamUtils.closeQuietly(zipOutputStream);
            zipOutputStream2 = zipOutputStream;
            return true;
        } catch (Exception e2) {
            ex = e2;
            zipOutputStream2 = zipOutputStream;
            KLog.e(TAG, "zipFilesInDir() failed", ex);
            StreamUtils.closeQuietly(zipOutputStream2);
            return false;
        } catch (Throwable th2) {
            th = th2;
            zipOutputStream2 = zipOutputStream;
            StreamUtils.closeQuietly(zipOutputStream2);
            throw th;
        }
    }

    private static void addDir(File root, File dirObj, ZipOutputStream zipOutputStream) throws IOException {
        FileInputStream fileInputStream = null;
        try {
            File[] files = dirObj.listFiles();
            if (files != null) {
                byte[] tmpBuf = new byte[2048];
                int i = 0;
                FileInputStream fileInputStream2 = null;
                while (i < files.length) {
                    try {
                        if (files[i].isDirectory()) {
                            addDir(root, files[i], zipOutputStream);
                            fileInputStream = fileInputStream2;
                        } else {
                            fileInputStream = new FileInputStream(files[i].getAbsolutePath());
                            zipOutputStream.putNextEntry(new ZipEntry(root.toURI().relativize(files[i].toURI()).getPath()));
                            while (true) {
                                int len = fileInputStream.read(tmpBuf);
                                if (len <= 0) {
                                    break;
                                }
                                zipOutputStream.write(tmpBuf, 0, len);
                            }
                            zipOutputStream.closeEntry();
                            fileInputStream.close();
                        }
                        i++;
                        fileInputStream2 = fileInputStream;
                    } catch (Throwable th) {
                        th = th;
                        fileInputStream = fileInputStream2;
                        StreamUtils.closeQuietly(fileInputStream);
                        throw th;
                    }
                }
                fileInputStream = fileInputStream2;
            }
            StreamUtils.closeQuietly(fileInputStream);
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public static boolean cleanupDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (String str : children) {
                boolean success = cleanupDir(new File(dir, str));
                if (!success) {
                    return false;
                }
            }
            return true;
        }
        return dir.delete();
    }

    public static String readFileContent(String filePath) throws IOException {
        Validate.notNull(filePath, "filePath", new Object[0]);
        return readInputStream(new FileInputStream(new File(filePath)));
    }

    public static String readFileContentFromAssets(String filename, Context context) throws IOException {
        Validate.notNull(filename, "filePath", new Object[0]);
        return readInputStream(context.getAssets().open(filename));
    }

    private static String readInputStream(InputStream stream) throws IOException {
        BufferedReader reader;
        BufferedReader reader2 = null;
        try {
            reader = new BufferedReader(new InputStreamReader(stream));
        } catch (Throwable th) {
            th = th;
        }
        try {
            StringBuilder out = new StringBuilder();
            while (true) {
                String line = reader.readLine();
                if (line != null) {
                    out.append(line);
                } else {
                    String sb = out.toString();
                    StreamUtils.closeQuietly(reader);
                    return sb;
                }
            }
        } catch (Throwable th2) {
            th = th2;
            reader2 = reader;
            StreamUtils.closeQuietly(reader2);
            throw th;
        }
    }

    public static boolean deleteFile(String filePath) {
        Validate.notNull(filePath, "filePath", new Object[0]);
        File responseFile = new File(filePath);
        if (responseFile.exists() && responseFile.isFile()) {
            return responseFile.delete();
        }
        return false;
    }

    public static void writeStringToFile(File file, String content) throws IOException {
        BufferedWriter bufferedwriter;
        Validate.notNull(file, "file", new Object[0]);
        BufferedWriter bufferedwriter2 = null;
        try {
            FileWriter fileWriter = new FileWriter(file);
            try {
                bufferedwriter = new BufferedWriter(fileWriter);
            } catch (Throwable th) {
                th = th;
            }
            try {
                bufferedwriter.write(content);
                StreamUtils.closeQuietly(bufferedwriter);
            } catch (Throwable th2) {
                th = th2;
                bufferedwriter2 = bufferedwriter;
                StreamUtils.closeQuietly(bufferedwriter2);
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
        }
    }

    public static void writeStreamToFile(File file, InputStream inStream) throws IOException {
        Validate.notNull(file, "file", new Object[0]);
        FileOutputStream outputStream = new FileOutputStream(file, false);
        try {
            byte[] responseBytes = new byte[8192];
            int totalBytesRead = 0;
            while (true) {
                int currentBytesRead = inStream.read(responseBytes, 0, 8192);
                if (currentBytesRead != -1) {
                    outputStream.write(responseBytes, 0, currentBytesRead);
                    totalBytesRead += currentBytesRead;
                } else {
                    return;
                }
            }
        } finally {
            StreamUtils.closeQuietly(outputStream);
        }
    }

    public static InputStream getInputStream(String file) throws FileNotFoundException {
        return new FileInputStream(file);
    }

    public static void Delete(File file) {
        DeleteRecursive(file);
    }

    private static void DeleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            File[] arr$ = fileOrDirectory.listFiles();
            for (File child : arr$) {
                DeleteRecursive(child);
            }
        }
        fileOrDirectory.delete();
    }

    public static void sortFilesbyLastModified(List<File> files) {
        Collections.sort(files, new Comparator<File>() { // from class: com.microsoft.kapp.utils.FileUtils.1
            @Override // java.util.Comparator
            public int compare(File file1, File file2) {
                return Long.valueOf(file1.lastModified()).compareTo(Long.valueOf(file2.lastModified()));
            }
        });
    }

    public static void copyStream(InputStream fileIn, OutputStream packageOut) throws IOException {
        byte[] buf = new byte[1024];
        while (true) {
            int len = fileIn.read(buf);
            if (len > 0) {
                packageOut.write(buf, 0, len);
            } else {
                return;
            }
        }
    }

    public static void copyFileUsingFileChannels(File source, File dest) throws IOException {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0L, inputChannel.size());
        } finally {
            StreamUtils.closeQuietly(inputChannel);
            StreamUtils.closeQuietly(outputChannel);
        }
    }
}
