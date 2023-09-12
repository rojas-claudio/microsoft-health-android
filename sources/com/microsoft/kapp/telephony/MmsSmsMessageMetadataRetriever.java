package com.microsoft.kapp.telephony;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.Telephony;
import com.microsoft.band.device.CargoSms;
import com.microsoft.band.util.StreamUtils;
import com.microsoft.kapp.R;
import com.microsoft.kapp.database.LoggingContentResolver;
import com.microsoft.kapp.database.Projection;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.utils.Constants;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class MmsSmsMessageMetadataRetriever {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static final String TAG;
    private final LoggingContentResolver mContentResolver;
    public Context mContext;
    private int mLatestMmsMessageId;
    private int mLatestSmsMessageId;
    private final MessageMetadataRetriever mRetriever;

    static {
        $assertionsDisabled = !MmsSmsMessageMetadataRetriever.class.desiredAssertionStatus();
        TAG = MmsSmsMessageMetadataRetriever.class.getSimpleName();
    }

    @Inject
    public MmsSmsMessageMetadataRetriever(LoggingContentResolver contentResolver, MessageMetadataRetriever retriever, Context context) {
        Validate.notNull(contentResolver, "contentResolver");
        Validate.notNull(retriever, "retriever");
        this.mContentResolver = contentResolver;
        this.mRetriever = retriever;
        this.mContext = context;
        this.mLatestSmsMessageId = this.mRetriever.retrieveLatestSmsMessageId();
        this.mLatestMmsMessageId = this.mRetriever.retrieveLatestMmsMessageId();
    }

    public List<MessageMetadata> retrieveNewMessageMetadata() {
        List<MessageMetadata> messageMetadataList;
        if (Build.VERSION.SDK_INT >= 19) {
            messageMetadataList = this.mRetriever.retrieveNewMessageMetadataApi19(this.mLatestSmsMessageId, this.mLatestMmsMessageId);
        } else {
            messageMetadataList = this.mRetriever.retrieveNewMessageMetadata(this.mLatestSmsMessageId, this.mLatestMmsMessageId);
        }
        boolean haveSms = false;
        boolean haveMms = false;
        if (messageMetadataList != null && messageMetadataList.size() > 0) {
            for (int i = messageMetadataList.size() - 1; i >= 0; i--) {
                MessageMetadata messageMetadata = messageMetadataList.get(i);
                if (!haveSms && messageMetadata.getMessageType() == MessageType.SMS) {
                    this.mLatestSmsMessageId = messageMetadata.getId();
                    haveSms = true;
                }
                if (!haveMms && messageMetadata.getMessageType() == MessageType.MMS) {
                    this.mLatestMmsMessageId = messageMetadata.getId();
                    haveMms = true;
                }
            }
        }
        return messageMetadataList;
    }

    public Message retrieveMessage(MessageMetadata metadata) {
        Validate.notNull(metadata, "metadata");
        switch (metadata.getMessageType()) {
            case SMS:
                return Build.VERSION.SDK_INT >= 19 ? retrieveSmsMessageApi19(metadata) : retrieveSmsMessage(metadata);
            case MMS:
                return Build.VERSION.SDK_INT >= 19 ? retrieveMmsMessageApi19(metadata) : retrieveMmsMessage(metadata);
            default:
                return null;
        }
    }

    @TargetApi(19)
    private SmsMessage retrieveSmsMessageApi19(MessageMetadata metadata) {
        SmsMessage message;
        if ($assertionsDisabled || metadata != null) {
            Cursor cursor = null;
            try {
                Projection projection = new Projection();
                int idColumnIndex = projection.addColumn("_id");
                int addressColumnIndex = projection.addColumn("address");
                int dateColumnIndex = projection.addColumn("date");
                int bodyColumnIndex = projection.addColumn(SmsColumns.BODY_COLUMN_NAME);
                int readColumnIndex = projection.addColumn(SmsColumns.READ_COLUMN_NAME);
                String selectionClause = String.format(Locale.ENGLISH, "%1$s = %2$d", "_id", Integer.valueOf(metadata.getId()));
                cursor = this.mContentResolver.query(Telephony.Sms.Inbox.CONTENT_URI, projection, selectionClause, null, null);
                if (cursor == null || !cursor.moveToFirst()) {
                    message = null;
                } else {
                    int id = cursor.getInt(idColumnIndex);
                    String number = cursor.getString(addressColumnIndex);
                    long date = cursor.getLong(dateColumnIndex);
                    String body = cursor.getString(bodyColumnIndex);
                    boolean isRead = cursor.getInt(readColumnIndex) == 1;
                    message = new SmsMessage(id, number, new Date(date), body, isRead);
                }
                return message;
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        throw new AssertionError("metadata cannot be null");
    }

    private SmsMessage retrieveSmsMessage(MessageMetadata metadata) {
        SmsMessage message;
        if ($assertionsDisabled || metadata != null) {
            Cursor cursor = null;
            try {
                Projection projection = new Projection();
                int idColumnIndex = projection.addColumn("_id");
                int addressColumnIndex = projection.addColumn("address");
                int dateColumnIndex = projection.addColumn("date");
                int bodyColumnIndex = projection.addColumn(SmsColumns.BODY_COLUMN_NAME);
                int readColumnIndex = projection.addColumn(SmsColumns.READ_COLUMN_NAME);
                String selectionClause = String.format(Locale.ENGLISH, "%1$s = %2$d", "_id", Integer.valueOf(metadata.getId()));
                cursor = this.mContentResolver.query(Constants.SMS_CONTENT_PROVIDER_URI, projection, selectionClause, null, null);
                if (cursor == null || !cursor.moveToFirst()) {
                    message = null;
                } else {
                    int id = cursor.getInt(idColumnIndex);
                    String number = cursor.getString(addressColumnIndex);
                    long date = cursor.getLong(dateColumnIndex);
                    String body = cursor.getString(bodyColumnIndex);
                    boolean isRead = cursor.getInt(readColumnIndex) == 1;
                    message = new SmsMessage(id, number, new Date(date), body, isRead);
                }
                return message;
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        throw new AssertionError("metadata cannot be null");
    }

    @TargetApi(19)
    private MmsMessage retrieveMmsMessageApi19(MessageMetadata metadata) {
        MmsMessage message;
        Validate.notNull(metadata, "metadata");
        Cursor cursor = null;
        try {
            Projection projection = new Projection();
            int idColumnIndex = projection.addColumn("_id");
            int dateColumnIndex = projection.addColumn("date");
            int readColumnIndex = projection.addColumn(SmsColumns.READ_COLUMN_NAME);
            String selectionClause = String.format(Locale.ENGLISH, "%1$s = %2$d", "_id", Integer.valueOf(metadata.getId()));
            cursor = this.mContentResolver.query(Telephony.Mms.Inbox.CONTENT_URI, projection, selectionClause, null, null);
            if (cursor == null || !cursor.moveToFirst()) {
                message = null;
            } else {
                int id = cursor.getInt(idColumnIndex);
                long date = cursor.getLong(dateColumnIndex);
                long date2 = date * 1000;
                boolean isRead = cursor.getInt(readColumnIndex) == 1;
                List<String> numbers = getMmsNumbers(id);
                String body = getMmsText(id);
                CargoSms.NotificationMmsType messageType = getMmsType(id);
                message = new MmsMessage(id, numbers, new Date(date2), body, messageType, isRead);
            }
            return message;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private MmsMessage retrieveMmsMessage(MessageMetadata metadata) {
        MmsMessage message;
        Validate.notNull(metadata, "metadata");
        Cursor cursor = null;
        try {
            Projection projection = new Projection();
            int idColumnIndex = projection.addColumn("_id");
            int dateColumnIndex = projection.addColumn("date");
            int readColumnIndex = projection.addColumn(SmsColumns.READ_COLUMN_NAME);
            String selectionClause = String.format(Locale.ENGLISH, "%1$s = %2$d", "_id", Integer.valueOf(metadata.getId()));
            cursor = this.mContentResolver.query(Constants.MMS_CONTENT_PROVIDER_URI, projection, selectionClause, null, null);
            if (cursor == null || !cursor.moveToFirst()) {
                message = null;
            } else {
                int id = cursor.getInt(idColumnIndex);
                long date = cursor.getLong(dateColumnIndex);
                long date2 = date * 1000;
                boolean isRead = cursor.getInt(readColumnIndex) == 1;
                List<String> numbers = getMmsNumbers(id);
                String body = getMmsText(id);
                CargoSms.NotificationMmsType messageType = getMmsType(id);
                message = new MmsMessage(id, numbers, new Date(date2), body, messageType, isRead);
            }
            return message;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    String getMmsText(int id) {
        Validate.isTrue(id >= 0, "id should not be -ve");
        String body = null;
        Cursor cursor = null;
        Uri uri = Uri.parse(Constants.MMS_DATA_URI_STRING);
        try {
            Projection projection = new Projection();
            int idColumnIndex = projection.addColumn("_id");
            int typeColumnIndex = projection.addColumn(MmsColumns.MMS_PART_TYPE);
            int dataColumnIndex = projection.addColumn(MmsColumns.MMS_PART_DATA);
            int textColumnIndex = projection.addColumn(MmsColumns.MMS_PART_TEXT);
            String selectionClause = String.format(Locale.ENGLISH, "%1$s = %2$d", MmsColumns.MMS_PART_MMS_ID, Integer.valueOf(id));
            cursor = this.mContentResolver.query(uri, projection, selectionClause, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String partId = cursor.getString(idColumnIndex);
                    String type = cursor.getString(typeColumnIndex);
                    if (MmsColumns.MMS_PART_TYPE_TEXT.equals(type)) {
                        String data = cursor.getString(dataColumnIndex);
                        if (data != null) {
                            body = getMmsPartData(partId);
                        } else {
                            body = cursor.getString(textColumnIndex);
                        }
                        if (body != null) {
                            break;
                        }
                    }
                } while (cursor.moveToNext());
            }
            return (body == null || body.length() <= 0) ? this.mContext.getResources().getString(R.string.mms_default_text) : body;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x0058, code lost:
        r7 = com.microsoft.band.device.CargoSms.NotificationMmsType.IMAGE;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    com.microsoft.band.device.CargoSms.NotificationMmsType getMmsType(int r13) {
        /*
            r12 = this;
            r0 = 1
            r4 = 0
            if (r13 < 0) goto L60
        L4:
            java.lang.String r4 = "id should not be -ve"
            com.microsoft.kapp.diagnostics.Validate.isTrue(r0, r4)
            r6 = 0
            java.lang.String r0 = "content://mms/part/"
            android.net.Uri r1 = android.net.Uri.parse(r0)
            com.microsoft.band.device.CargoSms$NotificationMmsType r7 = com.microsoft.band.device.CargoSms.NotificationMmsType.UNKNOWN
            com.microsoft.kapp.database.Projection r2 = new com.microsoft.kapp.database.Projection     // Catch: java.lang.Throwable -> L69
            r2.<init>()     // Catch: java.lang.Throwable -> L69
            java.lang.String r0 = "ct"
            int r9 = r2.addColumn(r0)     // Catch: java.lang.Throwable -> L69
            java.util.Locale r0 = java.util.Locale.ENGLISH     // Catch: java.lang.Throwable -> L69
            java.lang.String r4 = "%1$s = %2$d"
            r5 = 2
            java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch: java.lang.Throwable -> L69
            r10 = 0
            java.lang.String r11 = "mid"
            r5[r10] = r11     // Catch: java.lang.Throwable -> L69
            r10 = 1
            java.lang.Integer r11 = java.lang.Integer.valueOf(r13)     // Catch: java.lang.Throwable -> L69
            r5[r10] = r11     // Catch: java.lang.Throwable -> L69
            java.lang.String r3 = java.lang.String.format(r0, r4, r5)     // Catch: java.lang.Throwable -> L69
            com.microsoft.kapp.database.LoggingContentResolver r0 = r12.mContentResolver     // Catch: java.lang.Throwable -> L69
            r4 = 0
            r5 = 0
            android.database.Cursor r6 = r0.query(r1, r2, r3, r4, r5)     // Catch: java.lang.Throwable -> L69
            if (r6 == 0) goto L5a
            boolean r0 = r6.moveToFirst()     // Catch: java.lang.Throwable -> L69
            if (r0 == 0) goto L5a
        L49:
            java.lang.String r8 = r6.getString(r9)     // Catch: java.lang.Throwable -> L69
            if (r8 == 0) goto L62
            java.lang.String r0 = "image/"
            boolean r0 = org.apache.commons.lang3.StringUtils.containsIgnoreCase(r8, r0)     // Catch: java.lang.Throwable -> L69
            if (r0 == 0) goto L62
            com.microsoft.band.device.CargoSms$NotificationMmsType r7 = com.microsoft.band.device.CargoSms.NotificationMmsType.IMAGE     // Catch: java.lang.Throwable -> L69
        L5a:
            if (r6 == 0) goto L5f
            r6.close()
        L5f:
            return r7
        L60:
            r0 = r4
            goto L4
        L62:
            boolean r0 = r6.moveToNext()     // Catch: java.lang.Throwable -> L69
            if (r0 != 0) goto L49
            goto L5a
        L69:
            r0 = move-exception
            if (r6 == 0) goto L6f
            r6.close()
        L6f:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.microsoft.kapp.telephony.MmsSmsMessageMetadataRetriever.getMmsType(int):com.microsoft.band.device.CargoSms$NotificationMmsType");
    }

    private String getMmsPartData(String id) {
        BufferedReader bufferedReader;
        Validate.notNull(id, "id");
        Uri.Builder builder = Uri.parse(Constants.MMS_DATA_URI_STRING).buildUpon();
        builder.appendPath(String.valueOf(id));
        Uri partURI = builder.build();
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader2 = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            try {
                inputStream = this.mContentResolver.openInputStream(partURI);
                if (inputStream != null) {
                    InputStreamReader inputStreamReader2 = new InputStreamReader(inputStream, "UTF-8");
                    try {
                        bufferedReader = new BufferedReader(inputStreamReader2);
                    } catch (IOException e) {
                        ex = e;
                        inputStreamReader = inputStreamReader2;
                    } catch (Throwable th) {
                        th = th;
                        inputStreamReader = inputStreamReader2;
                    }
                    try {
                        for (String temp = bufferedReader.readLine(); temp != null; temp = bufferedReader.readLine()) {
                            stringBuilder.append(temp);
                        }
                        bufferedReader2 = bufferedReader;
                        inputStreamReader = inputStreamReader2;
                    } catch (IOException e2) {
                        ex = e2;
                        bufferedReader2 = bufferedReader;
                        inputStreamReader = inputStreamReader2;
                        KLog.e(TAG, ex.getMessage(), ex);
                        StreamUtils.closeQuietly(bufferedReader2);
                        StreamUtils.closeQuietly(inputStreamReader);
                        StreamUtils.closeQuietly(inputStream);
                        return stringBuilder.toString();
                    } catch (Throwable th2) {
                        th = th2;
                        bufferedReader2 = bufferedReader;
                        inputStreamReader = inputStreamReader2;
                        StreamUtils.closeQuietly(bufferedReader2);
                        StreamUtils.closeQuietly(inputStreamReader);
                        StreamUtils.closeQuietly(inputStream);
                        throw th;
                    }
                }
                StreamUtils.closeQuietly(bufferedReader2);
                StreamUtils.closeQuietly(inputStreamReader);
                StreamUtils.closeQuietly(inputStream);
            } catch (IOException e3) {
                ex = e3;
            }
            return stringBuilder.toString();
        } catch (Throwable th3) {
            th = th3;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x00b3 A[Catch: all -> 0x00c3, TryCatch #0 {all -> 0x00c3, blocks: (B:5:0x002b, B:7:0x0073, B:9:0x0079, B:12:0x008f, B:26:0x00cd, B:28:0x00d5, B:14:0x0095, B:15:0x00ad, B:17:0x00b3, B:19:0x00bf), top: B:33:0x002b }] */
    /* JADX WARN: Removed duplicated region for block: B:31:0x00dd A[DONT_GENERATE] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    java.util.List<java.lang.String> getMmsNumbers(int r23) {
        /*
            r22 = this;
            if (r23 < 0) goto Lca
            r1 = 1
        L3:
            java.lang.String r5 = "id should not be -ve"
            com.microsoft.kapp.diagnostics.Validate.isTrue(r1, r5)
            r10 = 0
            java.util.ArrayList r13 = new java.util.ArrayList
            r13.<init>()
            java.lang.String r1 = "content://mms/"
            android.net.Uri r1 = android.net.Uri.parse(r1)
            android.net.Uri$Builder r9 = r1.buildUpon()
            java.lang.String r1 = java.lang.String.valueOf(r23)
            r9.appendPath(r1)
            java.lang.String r1 = "addr"
            r9.appendPath(r1)
            android.net.Uri r2 = r9.build()
            com.microsoft.kapp.database.Projection r3 = new com.microsoft.kapp.database.Projection     // Catch: java.lang.Throwable -> Lc3
            r3.<init>()     // Catch: java.lang.Throwable -> Lc3
            java.lang.String r1 = "address"
            int r8 = r3.addColumn(r1)     // Catch: java.lang.Throwable -> Lc3
            java.lang.String r1 = "type"
            int r19 = r3.addColumn(r1)     // Catch: java.lang.Throwable -> Lc3
            java.util.Locale r1 = java.util.Locale.getDefault()     // Catch: java.lang.Throwable -> Lc3
            java.lang.String r5 = "%1$s = %2$s OR %1$s = %3$s"
            r6 = 3
            java.lang.Object[] r6 = new java.lang.Object[r6]     // Catch: java.lang.Throwable -> Lc3
            r20 = 0
            java.lang.String r21 = "type"
            r6[r20] = r21     // Catch: java.lang.Throwable -> Lc3
            r20 = 1
            java.lang.String r21 = "137"
            r6[r20] = r21     // Catch: java.lang.Throwable -> Lc3
            r20 = 2
            java.lang.String r21 = "151"
            r6[r20] = r21     // Catch: java.lang.Throwable -> Lc3
            java.lang.String r4 = java.lang.String.format(r1, r5, r6)     // Catch: java.lang.Throwable -> Lc3
            r0 = r22
            com.microsoft.kapp.database.LoggingContentResolver r1 = r0.mContentResolver     // Catch: java.lang.Throwable -> Lc3
            r5 = 0
            r6 = 0
            android.database.Cursor r10 = r1.query(r2, r3, r4, r5, r6)     // Catch: java.lang.Throwable -> Lc3
            java.util.ArrayList r17 = new java.util.ArrayList     // Catch: java.lang.Throwable -> Lc3
            r17.<init>()     // Catch: java.lang.Throwable -> Lc3
            r11 = 0
            if (r10 == 0) goto L95
            boolean r1 = r10.moveToFirst()     // Catch: java.lang.Throwable -> Lc3
            if (r1 == 0) goto L95
        L79:
            java.lang.String r7 = r10.getString(r8)     // Catch: java.lang.Throwable -> Lc3
            r0 = r19
            java.lang.String r18 = r10.getString(r0)     // Catch: java.lang.Throwable -> Lc3
            java.lang.String r1 = "137"
            r0 = r18
            boolean r1 = r1.equals(r0)     // Catch: java.lang.Throwable -> Lc3
            if (r1 == 0) goto Lcd
            r11 = r7
        L8f:
            boolean r1 = r10.moveToNext()     // Catch: java.lang.Throwable -> Lc3
            if (r1 != 0) goto L79
        L95:
            r13.add(r11)     // Catch: java.lang.Throwable -> Lc3
            r0 = r22
            android.content.Context r1 = r0.mContext     // Catch: java.lang.Throwable -> Lc3
            java.lang.String r5 = "phone"
            java.lang.Object r16 = r1.getSystemService(r5)     // Catch: java.lang.Throwable -> Lc3
            android.telephony.TelephonyManager r16 = (android.telephony.TelephonyManager) r16     // Catch: java.lang.Throwable -> Lc3
            java.lang.String r14 = r16.getLine1Number()     // Catch: java.lang.Throwable -> Lc3
            java.util.Iterator r12 = r17.iterator()     // Catch: java.lang.Throwable -> Lc3
        Lad:
            boolean r1 = r12.hasNext()     // Catch: java.lang.Throwable -> Lc3
            if (r1 == 0) goto Ldb
            java.lang.Object r15 = r12.next()     // Catch: java.lang.Throwable -> Lc3
            java.lang.String r15 = (java.lang.String) r15     // Catch: java.lang.Throwable -> Lc3
            boolean r1 = org.apache.commons.lang3.StringUtils.containsIgnoreCase(r15, r14)     // Catch: java.lang.Throwable -> Lc3
            if (r1 != 0) goto Lad
            r13.add(r15)     // Catch: java.lang.Throwable -> Lc3
            goto Lad
        Lc3:
            r1 = move-exception
            if (r10 == 0) goto Lc9
            r10.close()
        Lc9:
            throw r1
        Lca:
            r1 = 0
            goto L3
        Lcd:
            r0 = r17
            boolean r1 = r0.contains(r7)     // Catch: java.lang.Throwable -> Lc3
            if (r1 != 0) goto L8f
            r0 = r17
            r0.add(r7)     // Catch: java.lang.Throwable -> Lc3
            goto L8f
        Ldb:
            if (r10 == 0) goto Le0
            r10.close()
        Le0:
            return r13
        */
        throw new UnsupportedOperationException("Method not decompiled: com.microsoft.kapp.telephony.MmsSmsMessageMetadataRetriever.getMmsNumbers(int):java.util.List");
    }
}
