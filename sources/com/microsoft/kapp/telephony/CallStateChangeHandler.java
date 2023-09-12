package com.microsoft.kapp.telephony;

import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
/* loaded from: classes.dex */
public class CallStateChangeHandler {
    private static final String TAG = CallStateChangeHandler.class.getSimpleName();
    private final IncomingCallContextFactory mFactory;
    private IncomingCallContext mIncomingCallContext;
    private CallState mLastState = CallState.IDLE;
    private boolean mIsFirstTransition = true;

    public CallStateChangeHandler(IncomingCallContextFactory factory) {
        Validate.notNull(factory, "factory");
        this.mFactory = factory;
    }

    public IncomingCallContext getIncomingCallContext(int state, String incomingNumber) throws InvalidCallStateException {
        CallState callState = CallState.valueOf(state);
        KLog.logPrivate(TAG, "getIncomingCallContext s:%s, in:%s", callState, incomingNumber);
        KLog.logPrivate(TAG, "Transitioning from %s to %s.", this.mLastState, callState);
        if (this.mIsFirstTransition) {
            this.mIsFirstTransition = false;
            if (state == 0) {
                return null;
            }
        }
        boolean isValidTransition = false;
        boolean shouldClearIncomingCallContext = false;
        if (callState != this.mLastState) {
            switch (this.mLastState) {
                case IDLE:
                    if (callState == CallState.OFFHOOK) {
                        this.mIncomingCallContext = null;
                        isValidTransition = true;
                        break;
                    } else if (callState == CallState.RINGING) {
                        this.mIncomingCallContext = this.mFactory.create(incomingNumber);
                        isValidTransition = true;
                        break;
                    }
                    break;
                case OFFHOOK:
                    if (callState == CallState.IDLE) {
                        if (this.mIncomingCallContext != null) {
                            this.mIncomingCallContext.setState(PhoneState.HUNG_UP);
                        }
                        isValidTransition = true;
                        break;
                    } else if (callState == CallState.RINGING) {
                        this.mIncomingCallContext = this.mFactory.create(incomingNumber);
                        isValidTransition = true;
                        break;
                    }
                    break;
                case RINGING:
                    if (callState == CallState.IDLE) {
                        this.mIncomingCallContext.setState(PhoneState.MISSED);
                        shouldClearIncomingCallContext = true;
                        isValidTransition = true;
                        break;
                    } else if (callState == CallState.OFFHOOK) {
                        if (this.mIncomingCallContext.getState() != PhoneState.PICKED_UP) {
                            this.mIncomingCallContext.setState(PhoneState.PICKED_UP);
                        }
                        isValidTransition = true;
                        break;
                    }
                    break;
            }
        }
        CallState lastState = this.mLastState;
        this.mLastState = callState;
        if (!isValidTransition) {
            throw new InvalidCallStateException(lastState, callState);
        }
        IncomingCallContext incomingCallContext = this.mIncomingCallContext;
        if (shouldClearIncomingCallContext) {
            this.mIncomingCallContext = null;
            return incomingCallContext;
        }
        return incomingCallContext;
    }
}
