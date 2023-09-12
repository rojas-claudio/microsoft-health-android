package com.microsoft.band.device.command;

import com.microsoft.band.device.DeviceConstants;
import com.microsoft.band.internal.BandDeviceConstants;
import com.microsoft.band.internal.util.BufferUtil;
import com.microsoft.band.internal.util.UUIDHelper;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public class NotificationCommand extends CommandWrite {
    private static final long serialVersionUID = 1;

    public NotificationCommand(DeviceConstants.NotificationID notificationId, String guid, byte[] notificationData) {
        super(BandDeviceConstants.Command.CargoNotification, null, getNotificationData(notificationId.getId(), guid, notificationData));
    }

    private static byte[] getNotificationData(int notificationId, String appId, byte[] dataBytes) {
        int dataSize = 0;
        if (dataBytes != null) {
            dataSize = dataBytes.length;
        }
        ByteBuffer buffer = BufferUtil.allocateLittleEndian(dataSize + 18);
        buffer.putShort((short) notificationId).put(UUIDHelper.toGuidArray(appId));
        if (dataSize > 0) {
            buffer.put(dataBytes);
        }
        return buffer.array();
    }
}
