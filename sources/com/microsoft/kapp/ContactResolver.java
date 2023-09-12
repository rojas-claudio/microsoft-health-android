package com.microsoft.kapp;

import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import com.microsoft.kapp.database.LoggingContentResolver;
import com.microsoft.kapp.database.Projection;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.telephony.PhoneNumberPrettifier;
import org.apache.commons.lang3.StringUtils;
/* loaded from: classes.dex */
public class ContactResolver {
    private final PhoneNumberPrettifier mPrettifier;
    private final LoggingContentResolver mResolver;

    public ContactResolver(LoggingContentResolver resolver, PhoneNumberPrettifier prettifier) {
        Validate.notNull(resolver, "resolver");
        Validate.notNull(prettifier, "prettifier");
        this.mResolver = resolver;
        this.mPrettifier = prettifier;
    }

    public String resolveDisplayName(String number) {
        Validate.notNullOrEmpty(number, "number");
        String displayName = null;
        Projection projection = new Projection();
        int displayNameColumnIndex = projection.addColumn("display_name");
        Cursor cursor = null;
        try {
            cursor = this.mResolver.query(Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number)), projection, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                displayName = cursor.getString(displayNameColumnIndex);
            }
            if (StringUtils.isEmpty(displayName)) {
                return this.mPrettifier.prettify(number);
            }
            return displayName;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
