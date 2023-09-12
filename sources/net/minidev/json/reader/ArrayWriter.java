package net.minidev.json.reader;

import java.io.IOException;
import net.minidev.json.JSONStyle;
import net.minidev.json.JSONValue;
/* loaded from: classes.dex */
public class ArrayWriter implements JsonWriterI<Object> {
    @Override // net.minidev.json.reader.JsonWriterI
    public <E> void writeJSONString(E value, Appendable out, JSONStyle compression) throws IOException {
        compression.arrayStart(out);
        boolean needSep = false;
        Object[] arr$ = (Object[]) value;
        for (Object o : arr$) {
            if (needSep) {
                compression.objectNext(out);
            } else {
                needSep = true;
            }
            JSONValue.writeJSONString(o, out, compression);
        }
        compression.arrayStop(out);
    }
}
