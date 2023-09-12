package com.microsoft.kapp.services;

import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.util.Log;
import com.microsoft.kapp.telephony.PhoneState;
import com.microsoft.kapp.utils.Constants;
import org.apache.commons.lang3.Validate;
/* loaded from: classes.dex */
public class CallDismissManagerImpl implements CallDismissManager {
    private static String TAG = CallDismissManagerImpl.class.getSimpleName();
    private Context mContext;
    private PhoneState mPhoneState;
    private int mPreviousCallVolume = -1;

    public CallDismissManagerImpl(Context context) {
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO, new Object[0]);
        this.mContext = context;
    }

    @Override // com.microsoft.kapp.services.CallDismissManager
    public void dismissCall() {
        if (Build.VERSION.SDK_INT <= 19) {
            dismissCallApi19();
        } else {
            dismissCallApi20();
        }
    }

    private synchronized void dismissCallApi20() {
        AudioManager audioManager = (AudioManager) this.mContext.getSystemService("audio");
        int streamVolume = audioManager.getStreamVolume(2);
        if (this.mPhoneState == PhoneState.RINGING && streamVolume != 0) {
            this.mPreviousCallVolume = streamVolume;
            audioManager.setStreamVolume(2, 0, 0);
        }
    }

    private void dismissCallApi19() {
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec("service call phone 5 \n");
        } catch (Exception e) {
            Log.e(TAG, "The call couldn't be disconnected");
        }
    }

    @Override // com.microsoft.kapp.services.CallDismissManager
    public synchronized void setPhoneState(PhoneState phoneState) {
        this.mPhoneState = phoneState;
        if (this.mPreviousCallVolume != -1) {
            AudioManager audioManager = (AudioManager) this.mContext.getSystemService("audio");
            audioManager.setStreamVolume(2, this.mPreviousCallVolume, 0);
            this.mPreviousCallVolume = -1;
        }
    }
}
