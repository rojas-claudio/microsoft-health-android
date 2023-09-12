package com.microsoft.kapp.version;

import com.facebook.internal.ServerProtocol;
import com.microsoft.kapp.diagnostics.Validate;
/* loaded from: classes.dex */
public class VersionUpdate {
    private final boolean mIsRequired;
    private final Version mVersion;

    public VersionUpdate(Version version, boolean isRequired) {
        Validate.notNull(version, ServerProtocol.FALLBACK_DIALOG_PARAM_VERSION);
        this.mVersion = version;
        this.mIsRequired = isRequired;
    }

    public Version getVersion() {
        return this.mVersion;
    }

    public boolean getIsRequired() {
        return this.mIsRequired;
    }
}
