package com.facebook.internal;

import android.os.Bundle;
import com.facebook.widget.FacebookDialog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
/* loaded from: classes.dex */
public class PendingCallStore {
    private static final String CALL_ID_ARRAY_KEY = "com.facebook.internal.PendingCallStore.callIdArrayKey";
    private static final String CALL_KEY_PREFIX = "com.facebook.internal.PendingCallStore.";
    private static PendingCallStore mInstance;
    private Map<String, FacebookDialog.PendingCall> pendingCallMap = new HashMap();

    public static PendingCallStore getInstance() {
        if (mInstance == null) {
            createInstance();
        }
        return mInstance;
    }

    private static synchronized void createInstance() {
        synchronized (PendingCallStore.class) {
            if (mInstance == null) {
                mInstance = new PendingCallStore();
            }
        }
    }

    public void trackPendingCall(FacebookDialog.PendingCall pendingCall) {
        if (pendingCall != null) {
            this.pendingCallMap.put(pendingCall.getCallId().toString(), pendingCall);
        }
    }

    public void stopTrackingPendingCall(UUID callId) {
        if (callId != null) {
            this.pendingCallMap.remove(callId.toString());
        }
    }

    public FacebookDialog.PendingCall getPendingCallById(UUID callId) {
        if (callId == null) {
            return null;
        }
        return this.pendingCallMap.get(callId.toString());
    }

    public void saveInstanceState(Bundle outState) {
        ArrayList<String> callIds = new ArrayList<>(this.pendingCallMap.keySet());
        outState.putStringArrayList(CALL_ID_ARRAY_KEY, callIds);
        for (FacebookDialog.PendingCall pendingCall : this.pendingCallMap.values()) {
            String stateKey = getSavedStateKeyForPendingCallId(pendingCall.getCallId().toString());
            outState.putParcelable(stateKey, pendingCall);
        }
    }

    public void restoreFromSavedInstanceState(Bundle savedInstanceState) {
        ArrayList<String> callIds = savedInstanceState.getStringArrayList(CALL_ID_ARRAY_KEY);
        if (callIds != null) {
            Iterator<String> it = callIds.iterator();
            while (it.hasNext()) {
                String callId = it.next();
                String stateKey = getSavedStateKeyForPendingCallId(callId);
                FacebookDialog.PendingCall pendingCall = (FacebookDialog.PendingCall) savedInstanceState.getParcelable(stateKey);
                if (pendingCall != null) {
                    this.pendingCallMap.put(pendingCall.getCallId().toString(), pendingCall);
                }
            }
        }
    }

    private String getSavedStateKeyForPendingCallId(String pendingCallId) {
        return CALL_KEY_PREFIX + pendingCallId;
    }
}
