package com.microsoft.kapp.utils;

import com.microsoft.band.util.StreamUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.lang3.Validate;
/* loaded from: classes.dex */
public class MultiReadInputStream extends InputStream {
    ByteArrayInputStream bais;
    byte[] bytes;

    public MultiReadInputStream(InputStream input) throws IOException {
        Validate.notNull(input, "input", new Object[0]);
        this.bytes = StreamUtils.toArray(input);
        this.bais = new ByteArrayInputStream(this.bytes);
    }

    @Override // java.io.InputStream
    public synchronized void reset() {
        this.bais.reset();
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        return this.bais.read();
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.bais.reset();
    }
}
