package com.microsoft.band.util;

import com.microsoft.band.internal.util.KDKLog;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
/* loaded from: classes.dex */
public final class StreamUtils {
    private static final int BUFFER_SIZE = 8192;
    private static final String TAG = StreamUtils.class.getSimpleName();

    private StreamUtils() {
        throw new UnsupportedOperationException();
    }

    public static int toArray(InputStream stream, byte[] array, int offset, int count) throws IOException {
        ArraysUtil.checkOffsetAndCount(array.length, offset, count);
        byte[] buffer = new byte[8192];
        int remainRead = count;
        int spp = Math.min(8192, remainRead);
        while (true) {
            int b = stream.read(buffer, 0, spp);
            if (b <= -1 || remainRead <= 0) {
                break;
            }
            System.arraycopy(buffer, 0, array, offset, b);
            offset += b;
            remainRead -= b;
            spp = Math.min(8192, remainRead);
        }
        return count - remainRead;
    }

    public static String toString(InputStream input) throws IOException {
        StringWriter writer = new StringWriter();
        InputStreamReader reader = new InputStreamReader(input);
        copy(reader, writer);
        return writer.toString();
    }

    public static byte[] toArray(InputStream input) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        copy(input, baos);
        return baos.toByteArray();
    }

    public static long copy(Reader input, Writer output) throws IOException {
        char[] buffer = new char[8192];
        long count = 0;
        while (true) {
            int n = input.read(buffer);
            if (-1 != n) {
                output.write(buffer, 0, n);
                count += n;
            } else {
                return count;
            }
        }
    }

    public static void copy(InputStream inStream, OutputStream outStream) throws IOException {
        byte[] buffer = new byte[8192];
        while (true) {
            try {
                int b = inStream.read(buffer);
                if (b > -1) {
                    outStream.write(buffer, 0, b);
                } else {
                    return;
                }
            } finally {
                outStream.flush();
            }
        }
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                KDKLog.i(TAG, "CloseQuietly IOException", e);
            }
        }
    }
}
