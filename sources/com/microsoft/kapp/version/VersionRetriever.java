package com.microsoft.kapp.version;
/* loaded from: classes.dex */
public interface VersionRetriever {
    VersionUpdate getLatestVersion() throws VersionCheckException;
}
