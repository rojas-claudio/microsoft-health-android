package com.microsoft.kapp.telephony.event;

import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.telephony.MessageMetadata;
import java.util.EventObject;
/* loaded from: classes.dex */
public class MessageEvent extends EventObject {
    private static final long serialVersionUID = -3592709706579555562L;
    private final MessageMetadata mMetadata;

    public MessageEvent(Object source, MessageMetadata metadata) {
        super(validateSource(source));
        Validate.notNull(metadata, "metadata");
        this.mMetadata = metadata;
    }

    public MessageMetadata getMetadata() {
        return this.mMetadata;
    }

    private static Object validateSource(Object source) {
        Validate.notNull(source, "source");
        return source;
    }
}
