package com.microsoft.kapp.telephony;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.Telephony;
import com.microsoft.kapp.database.LoggingContentResolver;
import com.microsoft.kapp.database.Projection;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.utils.Constants;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
/* loaded from: classes.dex */
public class MessageMetadataRetriever {
    private static final String TAG = MessageMetadataRetriever.class.getSimpleName();
    private final LoggingContentResolver mContentResolver;

    public MessageMetadataRetriever(LoggingContentResolver contentResolver) {
        Validate.notNull(contentResolver, "contentResolver");
        this.mContentResolver = contentResolver;
        KLog.v(TAG, "Initialized");
    }

    @SuppressLint({"NewApi"})
    public int retrieveLatestSmsMessageId() {
        return Build.VERSION.SDK_INT >= 19 ? retrieveLatestMessageId(Telephony.Sms.Inbox.CONTENT_URI) : retrieveLatestMessageId(Constants.SMS_CONTENT_PROVIDER_URI);
    }

    @SuppressLint({"NewApi"})
    public int retrieveLatestMmsMessageId() {
        return Build.VERSION.SDK_INT >= 19 ? retrieveLatestMessageId(Telephony.Mms.Inbox.CONTENT_URI) : retrieveLatestMessageId(Constants.MMS_CONTENT_PROVIDER_URI);
    }

    public int retrieveLatestMessageId(Uri contentUri) {
        Cursor cursor = null;
        int messageId = 0;
        try {
            Projection projection = new Projection();
            int idColumnIndex = projection.addColumn("_id");
            String sortOrderClause = String.format(Locale.US, "%1$s DESC LIMIT 1", "_id");
            cursor = getContentResolver().query(contentUri, projection, null, null, sortOrderClause);
            if (cursor != null && cursor.moveToFirst()) {
                messageId = cursor.getInt(idColumnIndex);
            }
            return messageId;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @TargetApi(19)
    public List<MessageMetadata> retrieveNewMessageMetadataApi19(int lastSmsMessageId, int lastMmsMessageId) {
        Validate.isTrue(lastSmsMessageId >= 0, "lastSmsMessageId must be greater than or equal to 0.");
        Validate.isTrue(lastMmsMessageId >= 0, "lastMmsMessageId must be greater than or equal to 0.");
        List<MessageMetadata> messageMetadataList = new ArrayList<>();
        String selectionClauseSms = String.format(Locale.getDefault(), "(%1$s > %2$d)", "_id", Integer.valueOf(lastSmsMessageId));
        String selectionClauseMms = String.format(Locale.getDefault(), "(%1$s > %2$d)", "_id", Integer.valueOf(lastMmsMessageId));
        String sortOrderClause = String.format(Locale.getDefault(), "%1$s ASC", "_id");
        messageMetadataList.addAll(getUnreadMessages(Telephony.Sms.Inbox.CONTENT_URI, selectionClauseSms, sortOrderClause, MessageType.SMS));
        messageMetadataList.addAll(getUnreadMessages(Telephony.Mms.Inbox.CONTENT_URI, selectionClauseMms, sortOrderClause, MessageType.MMS));
        return messageMetadataList;
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x003c A[DONT_GENERATE] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.util.List<com.microsoft.kapp.telephony.MessageMetadata> getUnreadMessages(android.net.Uri r11, java.lang.String r12, java.lang.String r13, com.microsoft.kapp.telephony.MessageType r14) {
        /*
            r10 = this;
            java.util.ArrayList r9 = new java.util.ArrayList
            r9.<init>()
            com.microsoft.kapp.database.Projection r2 = new com.microsoft.kapp.database.Projection
            r2.<init>()
            java.lang.String r0 = "_id"
            int r8 = r2.addColumn(r0)
            r6 = 0
            com.microsoft.kapp.database.LoggingContentResolver r0 = r10.getContentResolver()     // Catch: java.lang.Throwable -> L40
            r4 = 0
            r1 = r11
            r3 = r12
            r5 = r13
            android.database.Cursor r6 = r0.query(r1, r2, r3, r4, r5)     // Catch: java.lang.Throwable -> L40
            if (r6 == 0) goto L3a
            boolean r0 = r6.moveToFirst()     // Catch: java.lang.Throwable -> L40
            if (r0 == 0) goto L3a
        L26:
            int r7 = r6.getInt(r8)     // Catch: java.lang.Throwable -> L40
            com.microsoft.kapp.telephony.MessageMetadata r0 = new com.microsoft.kapp.telephony.MessageMetadata     // Catch: java.lang.Throwable -> L40
            com.microsoft.kapp.telephony.MessageState r1 = com.microsoft.kapp.telephony.MessageState.UNREAD     // Catch: java.lang.Throwable -> L40
            r0.<init>(r7, r14, r1)     // Catch: java.lang.Throwable -> L40
            r9.add(r0)     // Catch: java.lang.Throwable -> L40
            boolean r0 = r6.moveToNext()     // Catch: java.lang.Throwable -> L40
            if (r0 != 0) goto L26
        L3a:
            if (r6 == 0) goto L3f
            r6.close()
        L3f:
            return r9
        L40:
            r0 = move-exception
            if (r6 == 0) goto L46
            r6.close()
        L46:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.microsoft.kapp.telephony.MessageMetadataRetriever.getUnreadMessages(android.net.Uri, java.lang.String, java.lang.String, com.microsoft.kapp.telephony.MessageType):java.util.List");
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x00aa A[DONT_GENERATE] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.util.List<com.microsoft.kapp.telephony.MessageMetadata> retrieveNewMessageMetadata(int r15, int r16) {
        /*
            r14 = this;
            if (r15 < 0) goto Lae
            r0 = 1
        L3:
            java.lang.String r1 = "lastSmsMessageId must be greater than or equal to 0."
            com.microsoft.kapp.diagnostics.Validate.isTrue(r0, r1)
            if (r16 < 0) goto Lb1
            r0 = 1
        Lc:
            java.lang.String r1 = "lastMmsMessageId must be greater than or equal to 0."
            com.microsoft.kapp.diagnostics.Validate.isTrue(r0, r1)
            r6 = 0
            java.util.ArrayList r9 = new java.util.ArrayList
            r9.<init>()
            com.microsoft.kapp.database.Projection r2 = new com.microsoft.kapp.database.Projection     // Catch: java.lang.Throwable -> Lb4
            r2.<init>()     // Catch: java.lang.Throwable -> Lb4
            java.lang.String r0 = "_id"
            int r8 = r2.addColumn(r0)     // Catch: java.lang.Throwable -> Lb4
            java.lang.String r0 = "type"
            int r11 = r2.addColumn(r0)     // Catch: java.lang.Throwable -> Lb4
            java.util.Locale r0 = java.util.Locale.getDefault()     // Catch: java.lang.Throwable -> Lb4
            java.lang.String r1 = "(%1$s > %2$d AND %3$s = %4$d) OR (%1$s > %5$d AND %6$s = %7$d)"
            r4 = 7
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch: java.lang.Throwable -> Lb4
            r12 = 0
            java.lang.String r13 = "_id"
            r4[r12] = r13     // Catch: java.lang.Throwable -> Lb4
            r12 = 1
            java.lang.Integer r13 = java.lang.Integer.valueOf(r16)     // Catch: java.lang.Throwable -> Lb4
            r4[r12] = r13     // Catch: java.lang.Throwable -> Lb4
            r12 = 2
            java.lang.String r13 = "msg_box"
            r4[r12] = r13     // Catch: java.lang.Throwable -> Lb4
            r12 = 3
            r13 = 1
            java.lang.Integer r13 = java.lang.Integer.valueOf(r13)     // Catch: java.lang.Throwable -> Lb4
            r4[r12] = r13     // Catch: java.lang.Throwable -> Lb4
            r12 = 4
            java.lang.Integer r13 = java.lang.Integer.valueOf(r15)     // Catch: java.lang.Throwable -> Lb4
            r4[r12] = r13     // Catch: java.lang.Throwable -> Lb4
            r12 = 5
            java.lang.String r13 = "type"
            r4[r12] = r13     // Catch: java.lang.Throwable -> Lb4
            r12 = 6
            r13 = 1
            java.lang.Integer r13 = java.lang.Integer.valueOf(r13)     // Catch: java.lang.Throwable -> Lb4
            r4[r12] = r13     // Catch: java.lang.Throwable -> Lb4
            java.lang.String r3 = java.lang.String.format(r0, r1, r4)     // Catch: java.lang.Throwable -> Lb4
            java.util.Locale r0 = java.util.Locale.getDefault()     // Catch: java.lang.Throwable -> Lb4
            java.lang.String r1 = "%1$s ASC"
            r4 = 1
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch: java.lang.Throwable -> Lb4
            r12 = 0
            java.lang.String r13 = "_id"
            r4[r12] = r13     // Catch: java.lang.Throwable -> Lb4
            java.lang.String r5 = java.lang.String.format(r0, r1, r4)     // Catch: java.lang.Throwable -> Lb4
            com.microsoft.kapp.database.LoggingContentResolver r0 = r14.getContentResolver()     // Catch: java.lang.Throwable -> Lb4
            android.net.Uri r1 = com.microsoft.kapp.utils.Constants.MMS_SMS_CONVERSATION_CONTENT_PROVIDER_URI     // Catch: java.lang.Throwable -> Lb4
            r4 = 0
            android.database.Cursor r6 = r0.query(r1, r2, r3, r4, r5)     // Catch: java.lang.Throwable -> Lb4
            if (r6 == 0) goto La8
            boolean r0 = r6.moveToFirst()     // Catch: java.lang.Throwable -> Lb4
            if (r0 == 0) goto La8
        L90:
            int r7 = r6.getInt(r8)     // Catch: java.lang.Throwable -> Lb4
            com.microsoft.kapp.telephony.MessageType r10 = r14.determineMessageType(r6, r11)     // Catch: java.lang.Throwable -> Lb4
            com.microsoft.kapp.telephony.MessageMetadata r0 = new com.microsoft.kapp.telephony.MessageMetadata     // Catch: java.lang.Throwable -> Lb4
            com.microsoft.kapp.telephony.MessageState r1 = com.microsoft.kapp.telephony.MessageState.UNREAD     // Catch: java.lang.Throwable -> Lb4
            r0.<init>(r7, r10, r1)     // Catch: java.lang.Throwable -> Lb4
            r9.add(r0)     // Catch: java.lang.Throwable -> Lb4
            boolean r0 = r6.moveToNext()     // Catch: java.lang.Throwable -> Lb4
            if (r0 != 0) goto L90
        La8:
            if (r6 == 0) goto Lad
            r6.close()
        Lad:
            return r9
        Lae:
            r0 = 0
            goto L3
        Lb1:
            r0 = 0
            goto Lc
        Lb4:
            r0 = move-exception
            if (r6 == 0) goto Lba
            r6.close()
        Lba:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.microsoft.kapp.telephony.MessageMetadataRetriever.retrieveNewMessageMetadata(int, int):java.util.List");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public MessageType determineMessageType(Cursor cursor, int typeColumnIndex) {
        return cursor.getType(typeColumnIndex) == 1 ? MessageType.SMS : MessageType.MMS;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public LoggingContentResolver getContentResolver() {
        return this.mContentResolver;
    }
}
