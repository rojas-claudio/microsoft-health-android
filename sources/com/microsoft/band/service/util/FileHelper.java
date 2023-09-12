package com.microsoft.band.service.util;

import com.microsoft.band.internal.BandServiceMessage;
import com.microsoft.band.internal.util.KDKLog;
import com.microsoft.band.internal.util.Validation;
import com.microsoft.band.service.CargoServiceException;
import com.microsoft.band.util.StreamUtils;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
/* loaded from: classes.dex */
public abstract class FileHelper {
    public static final String ACTUAL_DATA = "data.bin";
    public static final String EPHEMERIS_DIR = "ephemeris";
    public static final String EPHEMERIS_UPDATE_EXT = ".ephbin";
    public static final String FIRMWARE_UPDATE_EXT = ".fwbin";
    public static final String JSON_DATA = "info.json";
    public static final String LAST_CHECKED = "lastchecked.bin";
    public static final String LAST_UPGRADED = "lastupgraded.bin";
    public static final int OPTIMAL_BULK_IO_BUFFER_SIZE = 4096;
    public static final String SENSORLOG_DIR = "sensorlog";
    public static final String TIMEZONE_DIR = "timezone";
    public static final String TIMEZONE_EXT = ".tzbin";
    private static final String TAG = FileHelper.class.getSimpleName();
    public static final String FIRMWARE_UPDATE_DIR = File.separator + "firmware";
    public static final String TEMP_WEBTILE_DIR = File.separator + "temp_webtile";
    public static final String WEBTILE_DIR = File.separator + "webtiles" + File.separator + "installed";
    public static final String PACKAGE_PATH = File.separator + "package";
    public static final String DATA_PATH = File.separator + "data";

    public static File[] findFilesInPath(String path, final String extension) {
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            return null;
        }
        File[] files = fileDir.listFiles(new FilenameFilter() { // from class: com.microsoft.band.service.util.FileHelper.1
            @Override // java.io.FilenameFilter
            public boolean accept(File dir, String filename) {
                return filename.endsWith(extension);
            }
        });
        return files;
    }

    public static boolean deleteFilesInPath(String path, String extension) {
        boolean success = true;
        File[] files = findFilesInPath(path, extension);
        for (File file : files) {
            success &= file.delete();
        }
        return success;
    }

    public static boolean writeDataToFile(byte[] data, File file) throws CargoServiceException {
        boolean z = true;
        if (file == null || file.exists()) {
            return false;
        }
        BufferedOutputStream bos = null;
        try {
            try {
                if (file.createNewFile()) {
                    BufferedOutputStream bos2 = new BufferedOutputStream(new FileOutputStream(file));
                    try {
                        bos2.write(data);
                        bos2.flush();
                        KDKLog.i(TAG, "Done to write data to  %s.", file.getName());
                        StreamUtils.closeQuietly(bos2);
                    } catch (FileNotFoundException e) {
                        e = e;
                        throw new CargoServiceException(e.getMessage(), e, BandServiceMessage.Response.SERVICE_FILE_NOT_FOUND_ERROR);
                    } catch (IOException e2) {
                        e = e2;
                        throw new CargoServiceException(e.getMessage(), e, BandServiceMessage.Response.SERVICE_FILE_IO_ERROR);
                    } catch (Throwable th) {
                        th = th;
                        bos = bos2;
                        StreamUtils.closeQuietly(bos);
                        throw th;
                    }
                } else {
                    KDKLog.i(TAG, "Cannot create New File %s", file);
                    StreamUtils.closeQuietly(null);
                    z = false;
                }
                return z;
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (FileNotFoundException e3) {
            e = e3;
        } catch (IOException e4) {
            e = e4;
        }
    }

    public static byte[] readDataFromFile(String dir, String fileName) throws CargoServiceException {
        File file = new File(dir, fileName);
        return readDataFromFile(file);
    }

    public static byte[] readDataFromFile(File file) throws CargoServiceException {
        BufferedInputStream buf;
        byte[] data = null;
        if (file.exists()) {
            int fileSize = (int) file.length();
            data = new byte[fileSize];
            BufferedInputStream buf2 = null;
            try {
                try {
                    buf = new BufferedInputStream(new FileInputStream(file));
                } catch (Throwable th) {
                    th = th;
                }
            } catch (FileNotFoundException e) {
                e = e;
            } catch (IOException e2) {
                e = e2;
            }
            try {
                buf.read(data, 0, fileSize);
                KDKLog.i(TAG, "Read %d byte from  %s.", Integer.valueOf(fileSize), file.getName());
                StreamUtils.closeQuietly(buf);
            } catch (FileNotFoundException e3) {
                e = e3;
                throw new CargoServiceException(e.getMessage(), e, BandServiceMessage.Response.SERVICE_FILE_NOT_FOUND_ERROR);
            } catch (IOException e4) {
                e = e4;
                throw new CargoServiceException(e.getMessage(), e, BandServiceMessage.Response.SERVICE_FILE_IO_ERROR);
            } catch (Throwable th2) {
                th = th2;
                buf2 = buf;
                StreamUtils.closeQuietly(buf2);
                throw th;
            }
        }
        return data;
    }

    public static byte[] readBytesFromStream(InputStream inputstream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[4096];
        while (true) {
            int read = inputstream.read(buf, 0, buf.length);
            if (read != -1) {
                baos.write(buf, 0, read);
            } else {
                baos.flush();
                return baos.toByteArray();
            }
        }
    }

    public static long copyStreams(OutputStream outStream, InputStream inStream) throws CargoServiceException {
        byte[] buf = new byte[4096];
        long contentLength = 0;
        while (true) {
            try {
                int bytesRead = inStream.read(buf, 0, buf.length);
                if (bytesRead != -1) {
                    if (bytesRead > 0) {
                        outStream.write(buf, 0, bytesRead);
                        contentLength += bytesRead;
                    }
                } else {
                    outStream.flush();
                    return contentLength;
                }
            } catch (IOException e) {
                throw new CargoServiceException(e.getMessage(), BandServiceMessage.Response.SERVICE_FILE_IO_ERROR);
            }
        }
    }

    public static File directoryAtPath(String path) {
        File directory = new File(path);
        if (!directory.exists() && directory.mkdirs()) {
            KDKLog.d(TAG, "Created directory %s", directory);
        }
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(String.format("Unable to create directory at %s", directory));
        }
        return directory;
    }

    public static void deleteFile(File fileToDelete) throws CargoServiceException {
        if (fileToDelete != null && fileToDelete.exists() && fileToDelete.isFile()) {
            if (fileToDelete.delete()) {
                KDKLog.d(TAG, "Deleted file %s", fileToDelete);
                return;
            }
            throw new CargoServiceException(String.format("Failed to delete file %s", fileToDelete), BandServiceMessage.Response.SERVICE_FILE_DELETION_ERROR);
        }
    }

    public static void deleteDirectory(File directory, boolean recursive) throws CargoServiceException {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            if (recursive) {
                File[] arr$ = directory.listFiles();
                for (File file : arr$) {
                    if (file.isDirectory()) {
                        deleteDirectory(file, true);
                    } else {
                        deleteFile(file);
                    }
                }
            }
            if (!directory.delete()) {
                throw new CargoServiceException(String.format("Cannot delete directory %s", directory), BandServiceMessage.Response.SERVICE_FILE_DELETION_ERROR);
            }
        }
    }

    public static void deleteAllFilesInDirectory(File directory, boolean recursive) throws CargoServiceException {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            File[] arr$ = directory.listFiles();
            for (File file : arr$) {
                if (file.isDirectory()) {
                    if (recursive) {
                        deleteAllFilesInDirectory(file, true);
                    }
                } else {
                    deleteFile(file);
                }
            }
        }
    }

    public static void renameFileTo(File fileToRename, File newFileName) throws CargoServiceException {
        if (!fileToRename.renameTo(newFileName)) {
            throw new CargoServiceException(String.format("Failed to rename file %s to %s", fileToRename, newFileName), BandServiceMessage.Response.SERVICE_FILE_CREATION_ERROR);
        }
    }

    public static File createTempFile(String prefix, String suffix, File directory) throws CargoServiceException {
        try {
            File file = File.createTempFile(prefix, suffix, directory);
            return file;
        } catch (IOException e) {
            throw new CargoServiceException(e.getMessage(), BandServiceMessage.Response.SERVICE_FILE_CREATION_ERROR);
        }
    }

    public static File makeFirmwareFile(String directoryPath, String version, boolean tmp) throws CargoServiceException {
        File firmwareDirectory = directoryAtPath(directoryPath + FIRMWARE_UPDATE_DIR);
        String fileName = version.replace("-Debug:", ".");
        if (tmp) {
            File firmwareFile = createTempFile(fileName, null, firmwareDirectory);
            return firmwareFile;
        }
        File firmwareFile2 = new File(firmwareDirectory, fileName + FIRMWARE_UPDATE_EXT);
        return firmwareFile2;
    }

    public static File makeFile(String directoryPath, String directoryName, String fileName, boolean tmp) throws CargoServiceException {
        File directory = directoryAtPath(directoryPath + File.separator + directoryName);
        if (tmp) {
            File file = createTempFile(fileName, null, directory);
            return file;
        }
        File file2 = new File(directory, fileName);
        return file2;
    }

    public static String readStringFromFile(File file) throws IOException {
        if (!file.exists() || file.isDirectory()) {
            return null;
        }
        char[] chr = new char[4096];
        StringBuffer buffer = new StringBuffer();
        FileReader reader = new FileReader(file);
        while (true) {
            try {
                int len = reader.read(chr);
                if (len > 0) {
                    buffer.append(chr, 0, len);
                } else {
                    reader.close();
                    return buffer.toString();
                }
            } catch (Throwable th) {
                reader.close();
                throw th;
            }
        }
    }

    public static void writeStringToFile(String data, File file) throws IOException {
        Validation.validateNullParameter(file, "File");
        Validation.validateNullParameter(data, "data");
        FileWriter out = null;
        try {
            FileWriter out2 = new FileWriter(file);
            try {
                out2.write(data);
                out2.flush();
                KDKLog.i(TAG, "Done writing String to  %s.", file.getName());
                StreamUtils.closeQuietly(out2);
            } catch (Throwable th) {
                th = th;
                out = out2;
                StreamUtils.closeQuietly(out);
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public static void overwriteLongToFile(long data, File file) throws IOException {
        DataOutputStream dos;
        DataOutputStream dos2 = null;
        try {
            dos = new DataOutputStream(new FileOutputStream(file));
        } catch (Throwable th) {
            th = th;
        }
        try {
            dos.writeLong(data);
            dos.flush();
            KDKLog.i(TAG, "Done writing long to  %s.", file.getName());
            StreamUtils.closeQuietly(dos);
        } catch (Throwable th2) {
            th = th2;
            dos2 = dos;
            StreamUtils.closeQuietly(dos2);
            throw th;
        }
    }

    public static long readLongFromFile(File file) {
        DataInputStream dis;
        long lastValue = 0;
        if (file.exists() && !file.isDirectory()) {
            DataInputStream dis2 = null;
            try {
                try {
                    dis = new DataInputStream(new FileInputStream(file));
                } catch (Throwable th) {
                    th = th;
                }
            } catch (IOException e) {
                e = e;
            }
            try {
                lastValue = dis.readLong();
                KDKLog.i(TAG, "Done reading long from %s.", file.getName());
                StreamUtils.closeQuietly(dis);
            } catch (IOException e2) {
                e = e2;
                dis2 = dis;
                KDKLog.e(TAG, "Reading long value from file failed", e);
                StreamUtils.closeQuietly(dis2);
                return lastValue;
            } catch (Throwable th2) {
                th = th2;
                dis2 = dis;
                StreamUtils.closeQuietly(dis2);
                throw th;
            }
        }
        return lastValue;
    }
}
