package com.microsoft.kapp.telephony;

import com.microsoft.kapp.ContactResolver;
import com.microsoft.kapp.diagnostics.Validate;
import java.util.Date;
/* loaded from: classes.dex */
public class SmsMessage implements Message {
    private final String mBody;
    private final Date mDate;
    private String mDisplayName;
    private final int mId;
    private boolean mIsDisplayNameResolved;
    private final boolean mIsRead;
    private final String mNumber;

    public SmsMessage(int id, String number, Date date, String body, boolean isRead) {
        Validate.isTrue(id > 0, "id must be greater than 0");
        Validate.notNullOrEmpty(number, "number");
        this.mId = id;
        this.mNumber = number;
        this.mDate = date;
        this.mBody = body;
        this.mIsRead = isRead;
    }

    @Override // com.microsoft.kapp.telephony.Message
    public int getId() {
        return this.mId;
    }

    @Override // com.microsoft.kapp.telephony.Message
    public String getNumber() {
        return this.mNumber;
    }

    @Override // com.microsoft.kapp.telephony.Message
    public Date getTimestamp() {
        return this.mDate;
    }

    @Override // com.microsoft.kapp.telephony.Message
    public String getBody() {
        return this.mBody;
    }

    @Override // com.microsoft.kapp.telephony.Message
    public boolean getIsRead() {
        return this.mIsRead;
    }

    @Override // com.microsoft.kapp.telephony.Message
    public String getDisplayName() {
        return this.mDisplayName == null ? this.mNumber : this.mDisplayName;
    }

    @Override // com.microsoft.kapp.telephony.Message
    public void resolveDisplayName(ContactResolver resolver) {
        Validate.notNull(resolver, "resolver");
        if (!this.mIsDisplayNameResolved) {
            this.mDisplayName = resolver.resolveDisplayName(this.mNumber);
            this.mIsDisplayNameResolved = true;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("#: ").append(getNumber()).append(" Body: ").append(getBody()).append(" Name: ").append(getDisplayName()).append(" Time: ").append(getTimestamp());
        return sb.toString();
    }
}
