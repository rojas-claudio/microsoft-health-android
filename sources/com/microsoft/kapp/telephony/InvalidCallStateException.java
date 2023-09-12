package com.microsoft.kapp.telephony;

import com.microsoft.kapp.diagnostics.Validate;
/* loaded from: classes.dex */
public class InvalidCallStateException extends Exception {
    private static final long serialVersionUID = 966308415912609214L;
    private CallState mStateFrom;
    private CallState mStateTo;

    public InvalidCallStateException(CallState stateFrom, CallState stateTo) {
        Validate.notNull(stateFrom, "stateFrom");
        Validate.notNull(stateTo, "stateTo");
        this.mStateFrom = stateFrom;
        this.mStateTo = stateTo;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        String message = String.format("The call state transition is invalid. Cannot transit from '%s' to '%s'.", this.mStateFrom, this.mStateTo);
        return message;
    }
}
