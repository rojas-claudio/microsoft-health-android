package com.microsoft.kapp.telephony;

import android.database.Cursor;
import android.net.Uri;
import com.microsoft.kapp.database.LoggingContentResolver;
import com.microsoft.kapp.database.Projection;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.utils.Constants;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
/* loaded from: classes.dex */
public class MessageMetadataByThreadRetriever extends MessageMetadataRetriever {
    private final String TAG;
    private final MessageThreadsRetriever mThreadsRetriever;

    public MessageMetadataByThreadRetriever(LoggingContentResolver contentResolver, MessageThreadsRetriever threadsRetriever) {
        super(contentResolver);
        this.TAG = MessageMetadataByThreadRetriever.class.getSimpleName();
        Validate.notNull(threadsRetriever, "threadsRetriever");
        this.mThreadsRetriever = threadsRetriever;
        KLog.v(this.TAG, "Initialized");
    }

    @Override // com.microsoft.kapp.telephony.MessageMetadataRetriever
    public List<MessageMetadata> retrieveNewMessageMetadata(int lastSmsMessageId, int lastMmsMessageId) {
        Validate.isTrue(lastSmsMessageId >= 0, "lastSmsMessageId must be greater than or equal to 0.");
        Validate.isTrue(lastMmsMessageId >= 0, "lastMmsMessageId must be greater than or equal to 0.");
        int[] threads = this.mThreadsRetriever.retrieveThreads();
        List<MessageMetadata> messageIdentifiers = new ArrayList<>();
        if (threads != null) {
            for (int threadId : threads) {
                Cursor cursor = null;
                boolean skippedSms = false;
                boolean skippedMms = false;
                try {
                    Projection projection = new Projection();
                    int idColumnIndex = projection.addColumn("_id");
                    int typeColumnIndex = projection.addColumn("type");
                    int messageBoxTypeColumnIndex = projection.addColumn(MmsColumns.MSG_BOX_TYPE_COLUMN_NAME);
                    String sortOrderClause = String.format(Locale.getDefault(), "%1$s DESC", "_id");
                    cursor = getContentResolver().query(Uri.withAppendedPath(Constants.MMS_SMS_CONVERSATION_CONTENT_PROVIDER_URI, String.valueOf(threadId)), projection, null, null, sortOrderClause);
                    if (cursor != null) {
                        while (cursor.moveToNext()) {
                            int id = cursor.getInt(idColumnIndex);
                            MessageType type = determineMessageType(cursor, typeColumnIndex);
                            if (skippedSms && skippedMms) {
                                break;
                            } else if (type == MessageType.SMS && id <= lastSmsMessageId) {
                                skippedSms = true;
                            } else if (type == MessageType.MMS && id <= lastMmsMessageId) {
                                skippedMms = true;
                            } else {
                                boolean isInboxMessage = (type == MessageType.MMS && cursor.getInt(messageBoxTypeColumnIndex) == 1) || (type == MessageType.SMS && cursor.getInt(typeColumnIndex) == 1);
                                if (isInboxMessage) {
                                    MessageMetadata metadata = new MessageMetadata(id, type, MessageState.UNREAD);
                                    int index = Collections.binarySearch(messageIdentifiers, metadata, MessageMetadataIdComparator.DEFAULT);
                                    if (index >= 0) {
                                        KLog.logPrivate(this.TAG, "Duplicated message id '%d' detected. Ignoring it", Integer.valueOf(id));
                                    } else {
                                        messageIdentifiers.add(index ^ (-1), metadata);
                                    }
                                }
                            }
                        }
                    }
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            }
        }
        return messageIdentifiers;
    }
}
