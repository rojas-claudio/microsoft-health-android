package com.microsoft.kapp.models.strapp;

import com.microsoft.kapp.diagnostics.Validate;
/* loaded from: classes.dex */
public class StrappState {
    private final StrappDefinition mDefinition;
    private boolean mIsEnabled;
    private boolean mIsStateChangeAllowed;

    public StrappState(StrappDefinition definition) {
        Validate.notNull(definition, "definition");
        this.mDefinition = definition;
    }

    public StrappDefinition getDefinition() {
        return this.mDefinition;
    }

    public boolean isEnabled() {
        return this.mIsEnabled;
    }

    public void setIsEnabled(boolean isEnabled) {
        this.mIsEnabled = isEnabled;
    }

    public boolean isStateChangeAllowed() {
        return this.mIsStateChangeAllowed;
    }

    public void setIsStateChangeAllowed(boolean isStateChangeAllowed) {
        this.mIsStateChangeAllowed = isStateChangeAllowed;
    }
}
