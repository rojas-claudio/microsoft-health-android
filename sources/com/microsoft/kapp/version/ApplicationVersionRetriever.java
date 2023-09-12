package com.microsoft.kapp.version;
/* loaded from: classes.dex */
public interface ApplicationVersionRetriever extends VersionRetriever {
    @Override // com.microsoft.kapp.version.VersionRetriever
    VersionUpdate getLatestVersion() throws VersionCheckException;
}
