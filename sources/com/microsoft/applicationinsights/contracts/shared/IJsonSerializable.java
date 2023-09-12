package com.microsoft.applicationinsights.contracts.shared;

import java.io.IOException;
import java.io.Writer;
/* loaded from: classes.dex */
public interface IJsonSerializable {
    void serialize(Writer writer) throws IOException;
}
