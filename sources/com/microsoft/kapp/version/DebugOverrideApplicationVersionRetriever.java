package com.microsoft.kapp.version;

import com.microsoft.kapp.diagnostics.Validate;
/* loaded from: classes.dex */
public class DebugOverrideApplicationVersionRetriever implements ApplicationVersionRetriever {
    private final VersionManagerDebugOverride mOverride;

    public DebugOverrideApplicationVersionRetriever(VersionManagerDebugOverride override) {
        Validate.notNull(override, "override");
        this.mOverride = override;
    }

    @Override // com.microsoft.kapp.version.ApplicationVersionRetriever, com.microsoft.kapp.version.VersionRetriever
    public VersionUpdate getLatestVersion() throws VersionCheckException {
        return new VersionUpdate(Version.parse(this.mOverride.getLatestAppVersion()), true);
    }
}
