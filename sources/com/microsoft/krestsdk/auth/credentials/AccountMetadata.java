package com.microsoft.krestsdk.auth.credentials;

import org.joda.time.LocalDate;
/* loaded from: classes.dex */
public class AccountMetadata {
    private LocalDate mProfileCreatedOn;
    private String mUserId;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AccountMetadata(String userId, LocalDate profileCreation) {
        this.mUserId = userId;
        this.mProfileCreatedOn = profileCreation;
    }

    public String getUserId() {
        return this.mUserId;
    }

    public LocalDate getProfileCreationDate() {
        return this.mProfileCreatedOn;
    }
}
