package com.microsoft.band;
/* loaded from: classes.dex */
public final class DeviceProfileStatus {
    public final LinkStatus deviceLinkStatus;
    public final LinkStatus userLinkStatus;

    /* loaded from: classes.dex */
    public enum LinkStatus {
        EMPTY,
        MATCHING,
        NON_MATCHING
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DeviceProfileStatus(LinkStatus device, LinkStatus user) {
        this.deviceLinkStatus = device;
        this.userLinkStatus = user;
    }
}
