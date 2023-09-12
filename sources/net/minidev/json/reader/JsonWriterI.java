package net.minidev.json.reader;

import java.io.IOException;
import net.minidev.json.JSONStyle;
/* loaded from: classes.dex */
public interface JsonWriterI<T> {
    <E extends T> void writeJSONString(E e, Appendable appendable, JSONStyle jSONStyle) throws IOException;
}
