package com.microsoft.kapp.telephony;

import com.microsoft.kapp.diagnostics.Validate;
import java.util.Comparator;
/* loaded from: classes.dex */
class MessageMetadataIdComparator implements Comparator<MessageMetadata> {
    public static final MessageMetadataIdComparator DEFAULT = new MessageMetadataIdComparator();

    MessageMetadataIdComparator() {
    }

    @Override // java.util.Comparator
    public int compare(MessageMetadata lhs, MessageMetadata rhs) {
        Validate.notNull(lhs, "lhs");
        Validate.notNull(rhs, "rhs");
        int lhsId = lhs.getId();
        int rhsId = rhs.getId();
        if (lhsId == rhsId) {
            return 0;
        }
        if (lhsId < rhsId) {
            return -1;
        }
        return 1;
    }
}
