package com.jayway.jsonpath;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
/* loaded from: classes.dex */
public interface ParseContext {
    DocumentContext parse(File file) throws IOException;

    DocumentContext parse(InputStream inputStream);

    DocumentContext parse(InputStream inputStream, String str);

    DocumentContext parse(Object obj);

    DocumentContext parse(String str);
}
