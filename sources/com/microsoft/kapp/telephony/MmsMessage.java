package com.microsoft.kapp.telephony;

import android.text.TextUtils;
import com.microsoft.band.device.CargoSms;
import com.microsoft.kapp.ContactResolver;
import com.microsoft.kapp.diagnostics.Validate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/* loaded from: classes.dex */
public class MmsMessage implements Message {
    private final String mBody;
    private final Date mDate;
    private final List<String> mDisplayNameList;
    private final int mId;
    private boolean mIsDisplayNameResolved;
    private final boolean mIsRead;
    private final CargoSms.NotificationMmsType mMessageType;
    private final String mNumber;
    private final List<String> mNumberList;

    public MmsMessage(int id, List<String> numbers, Date date, String body, CargoSms.NotificationMmsType type, boolean isRead) {
        Validate.isTrue(id > 0, "id must be greater than 0");
        Validate.notNull(numbers, "numbers");
        Validate.isTrue(numbers.size() > 0, "Numbers should contain sender address");
        this.mId = id;
        this.mNumber = numbers.get(0);
        this.mDate = date;
        this.mBody = body;
        this.mMessageType = type;
        this.mIsRead = isRead;
        this.mNumberList = numbers;
        this.mDisplayNameList = new ArrayList();
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
        if (this.mDisplayNameList == null || this.mDisplayNameList.size() <= 0) {
            return null;
        }
        return this.mDisplayNameList.get(0);
    }

    public List<String> getReceiversNameList() {
        if (this.mDisplayNameList == null || this.mDisplayNameList.size() <= 1) {
            return null;
        }
        return this.mDisplayNameList.subList(1, this.mDisplayNameList.size());
    }

    public CargoSms.NotificationMmsType getMessageType() {
        return this.mMessageType;
    }

    @Override // com.microsoft.kapp.telephony.Message
    public void resolveDisplayName(ContactResolver resolver) {
        Validate.notNull(resolver, "resolver");
        if (!this.mIsDisplayNameResolved) {
            for (String number : this.mNumberList) {
                String displayName = "";
                if (!TextUtils.isEmpty(number)) {
                    displayName = resolver.resolveDisplayName(number);
                }
                if (displayName == null) {
                    displayName = number;
                }
                if (!this.mDisplayNameList.contains(displayName)) {
                    this.mDisplayNameList.add(displayName);
                }
            }
            this.mIsDisplayNameResolved = true;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("#: ").append(getNumber()).append(" Body: ").append(getBody()).append(" Name: ").append(getDisplayName()).append(" Time: ").append(getTimestamp());
        return sb.toString();
    }
}
