package com.microsoft.kapp.telephony;

import com.microsoft.kapp.ContactResolver;
import java.util.Date;
/* loaded from: classes.dex */
public interface Message {
    String getBody();

    String getDisplayName();

    int getId();

    boolean getIsRead();

    String getNumber();

    Date getTimestamp();

    void resolveDisplayName(ContactResolver contactResolver);
}
