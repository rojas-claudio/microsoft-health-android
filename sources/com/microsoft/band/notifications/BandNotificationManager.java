package com.microsoft.band.notifications;

import com.microsoft.band.BandIOException;
import com.microsoft.band.BandPendingResult;
import java.util.Date;
import java.util.UUID;
/* loaded from: classes.dex */
public interface BandNotificationManager {
    BandPendingResult<Void> sendMessage(UUID uuid, String str, String str2, Date date, MessageFlags messageFlags) throws BandIOException;

    BandPendingResult<Void> showDialog(UUID uuid, String str, String str2) throws BandIOException;

    BandPendingResult<Void> vibrate(VibrationType vibrationType) throws BandIOException;
}
