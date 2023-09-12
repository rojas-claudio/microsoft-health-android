package net.hockeyapp.android.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.message.BasicHeader;
/* loaded from: classes.dex */
public class SimpleMultipartEntity implements HttpEntity {
    private static final char[] BOUNDARY_CHARS = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private String boundary;
    private boolean isSetFirst = false;
    private boolean isSetLast = false;
    private ByteArrayOutputStream out = new ByteArrayOutputStream();

    public SimpleMultipartEntity() {
        StringBuffer buffer = new StringBuffer();
        Random rand = new Random();
        for (int i = 0; i < 30; i++) {
            buffer.append(BOUNDARY_CHARS[rand.nextInt(BOUNDARY_CHARS.length)]);
        }
        this.boundary = buffer.toString();
    }

    public String getBoundary() {
        return this.boundary;
    }

    public void writeFirstBoundaryIfNeeds() throws IOException {
        if (!this.isSetFirst) {
            this.out.write(("--" + this.boundary + "\r\n").getBytes());
        }
        this.isSetFirst = true;
    }

    public void writeLastBoundaryIfNeeds() {
        if (!this.isSetLast) {
            try {
                this.out.write(("\r\n--" + this.boundary + "--\r\n").getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.isSetLast = true;
        }
    }

    public void addPart(String key, String value) throws IOException {
        writeFirstBoundaryIfNeeds();
        this.out.write(("Content-Disposition: form-data; name=\"" + key + "\"\r\n").getBytes());
        this.out.write("Content-Type: text/plain; charset=UTF-8\r\n".getBytes());
        this.out.write("Content-Transfer-Encoding: 8bit\r\n\r\n".getBytes());
        this.out.write(value.getBytes());
        this.out.write(("\r\n--" + this.boundary + "\r\n").getBytes());
    }

    public void addPart(String key, File value, boolean lastFile) throws IOException {
        addPart(key, value.getName(), new FileInputStream(value), lastFile);
    }

    public void addPart(String key, String fileName, InputStream fin, boolean lastFile) throws IOException {
        addPart(key, fileName, fin, "application/octet-stream", lastFile);
    }

    public void addPart(String key, String fileName, InputStream fin, String type, boolean lastFile) throws IOException {
        writeFirstBoundaryIfNeeds();
        try {
            this.out.write(("Content-Disposition: form-data; name=\"" + key + "\"; filename=\"" + fileName + "\"\r\n").getBytes());
            this.out.write(("Content-Type: " + type + "\r\n").getBytes());
            this.out.write("Content-Transfer-Encoding: binary\r\n\r\n".getBytes());
            byte[] tmp = new byte[4096];
            while (true) {
                int l = fin.read(tmp);
                if (l == -1) {
                    break;
                }
                this.out.write(tmp, 0, l);
            }
            this.out.flush();
            if (lastFile) {
                writeLastBoundaryIfNeeds();
            } else {
                this.out.write(("\r\n--" + this.boundary + "\r\n").getBytes());
            }
        } finally {
            try {
                fin.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public long getContentLength() {
        writeLastBoundaryIfNeeds();
        return this.out.toByteArray().length;
    }

    public Header getContentType() {
        return new BasicHeader("Content-Type", "multipart/form-data; boundary=" + getBoundary());
    }

    public boolean isChunked() {
        return false;
    }

    public boolean isRepeatable() {
        return false;
    }

    public boolean isStreaming() {
        return false;
    }

    public void writeTo(OutputStream outstream) throws IOException {
        outstream.write(this.out.toByteArray());
    }

    public Header getContentEncoding() {
        return null;
    }

    public void consumeContent() throws IOException, UnsupportedOperationException {
        if (isStreaming()) {
            throw new UnsupportedOperationException("Streaming entity does not implement #consumeContent()");
        }
    }

    public InputStream getContent() throws IOException, UnsupportedOperationException {
        return new ByteArrayInputStream(this.out.toByteArray());
    }
}
