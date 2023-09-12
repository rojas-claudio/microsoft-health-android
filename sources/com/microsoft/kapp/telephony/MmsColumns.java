package com.microsoft.kapp.telephony;
/* loaded from: classes.dex */
public interface MmsColumns {
    public static final String CONTENT_TYPE_COLUMN_NAME = "ct_t";
    public static final int MESSAGE_BOX_ALL = 0;
    public static final int MESSAGE_BOX_DRAFT = 3;
    public static final int MESSAGE_BOX_INBOX = 1;
    public static final int MESSAGE_BOX_OUTBOX = 4;
    public static final int MESSAGE_BOX_SENT = 2;
    public static final String MMS_ADDR_ADDRESS = "address";
    public static final String MMS_ADDR_TYPE = "type";
    public static final String MMS_ADDR_TYPE_FROM = "137";
    public static final String MMS_ADDR_TYPE_TO = "151";
    public static final String MMS_CONTENT_TYPE_MIXED = "application/vnd.wap.multipart.mixed";
    public static final String MMS_CONTENT_TYPE_RELATED = "application/vnd.wap.multipart.related";
    public static final String MMS_PART_DATA = "_data";
    public static final String MMS_PART_ID = "_id";
    public static final String MMS_PART_MMS_ID = "mid";
    public static final String MMS_PART_TEXT = "text";
    public static final String MMS_PART_TYPE = "ct";
    public static final String MMS_PART_TYPE_IMAGE_PREFIX = "image/";
    public static final String MMS_PART_TYPE_TEXT = "text/plain";
    public static final String MSG_BOX_TYPE_COLUMN_NAME = "msg_box";
}
