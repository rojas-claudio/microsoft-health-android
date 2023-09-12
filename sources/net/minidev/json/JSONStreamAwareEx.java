package net.minidev.json;

import java.io.IOException;
/* loaded from: classes.dex */
public interface JSONStreamAwareEx extends JSONStreamAware {
    void writeJSONString(Appendable appendable, JSONStyle jSONStyle) throws IOException;
}
