package com.microsoft.kapp.version;
/* loaded from: classes.dex */
public interface VersionUpdateListener {
    void versionUpdateCheckFailed(VersionUpdateNotifier<?> versionUpdateNotifier, Exception exc);

    void versionUpdateDetected(VersionUpdateNotifier<?> versionUpdateNotifier, VersionUpdate versionUpdate);
}
