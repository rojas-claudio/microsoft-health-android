package com.microsoft.kapp.utils;

import android.app.Activity;
import android.content.Intent;
import com.microsoft.kapp.R;
import com.microsoft.kapp.activities.DeviceConnectActivity;
import com.microsoft.kapp.activities.HomeActivity;
import com.microsoft.kapp.activities.OobeConnectPhoneActivity;
import com.microsoft.kapp.activities.OobeEnableNotificationsActivity;
import com.microsoft.kapp.activities.OobeFirmwareUpdateActivity;
import com.microsoft.kapp.activities.OobeProfileActivity;
import com.microsoft.kapp.activities.OobeReadyActivity;
import com.microsoft.kapp.activities.SignInActivity;
import com.microsoft.kapp.activities.TermsOfServiceActivity;
import com.microsoft.kapp.models.FreStatus;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.krestsdk.auth.SignInContext;
/* loaded from: classes.dex */
public class FreUtils {
    private static final int ACTIVITY_ENTRY_TRANSITION = 2130968581;
    private static final int ACTIVITY_EXIT_TRANSITION = 2130968582;

    public static boolean freRedirect(Activity activity, SettingsProvider settingsProvider) {
        FreStatus status = settingsProvider.getFreStatus();
        switch (status) {
            case NOT_SHOWN:
                Intent signInIntent = new Intent(activity, SignInActivity.class);
                signInIntent.putExtra(SignInActivity.ARG_IN_SIGN_CONTEXT, SignInContext.FRE);
                startNextActivity(activity, signInIntent);
                return true;
            case LOGGED_IN:
                startNextActivity(activity, new Intent(activity, TermsOfServiceActivity.class));
                return true;
            case TOS_SIGNED:
                startNextActivity(activity, new Intent(activity, OobeProfileActivity.class));
                return true;
            case CREATED_PROFILE:
                startNextActivity(activity, new Intent(activity, OobeReadyActivity.class));
                return true;
            case SKIP_REMAINING_OOBE_STEPS:
            case OOBE_READY:
                settingsProvider.setFreStatus(FreStatus.SHOWN);
                Intent intent = new Intent(activity, HomeActivity.class);
                intent.addFlags(32768);
                intent.addFlags(268435456);
                startNextActivityNoAnimation(activity, intent);
                return true;
            case SHOWN:
                return false;
            default:
                return false;
        }
    }

    public static boolean devicePairingRedirect(Activity activity, SettingsProvider settingsProvider) {
        return devicePairingRedirect(activity, settingsProvider, true);
    }

    public static boolean devicePairingRedirect(Activity activity, SettingsProvider settingsProvider, boolean shouldCloseActivity) {
        FreStatus status = settingsProvider.getFreStatus();
        switch (status) {
            case SKIP_REMAINING_OOBE_STEPS:
            case OOBE_READY:
                settingsProvider.setFreStatus(FreStatus.SHOWN);
                Intent intent = new Intent(activity, HomeActivity.class);
                intent.addFlags(32768);
                intent.addFlags(268435456);
                startNextActivityNoAnimation(activity, intent);
                return true;
            case SHOWN:
            case DEVICE_CONNECT_START:
                startNextActivity(activity, new Intent(activity, DeviceConnectActivity.class), shouldCloseActivity);
                return true;
            case CONNECTED_DEVICE:
                startNextActivity(activity, new Intent(activity, OobeFirmwareUpdateActivity.class), shouldCloseActivity);
                return true;
            case DEVICE_CONNECTION_SKIPPED:
                startNextActivity(activity, new Intent(activity, OobeConnectPhoneActivity.class));
                return true;
            case FIRMWARE_VERSION_CHECKED:
                if (CommonUtils.areNotificationsSupported() && !CommonUtils.areNotificationsEnabled(activity)) {
                    startNextActivity(activity, new Intent(activity, OobeEnableNotificationsActivity.class));
                    return true;
                }
                startNextActivity(activity, new Intent(activity, OobeReadyActivity.class), shouldCloseActivity);
                return true;
            case NOTIFICATION_ENABLE_COMPLETE:
                startNextActivity(activity, new Intent(activity, OobeReadyActivity.class), shouldCloseActivity);
                return true;
            case PHONE_BIOMETRICS_ESTABLISHED:
                startNextActivity(activity, new Intent(activity, OobeReadyActivity.class), shouldCloseActivity);
                return true;
            default:
                return false;
        }
    }

    public static boolean isOOBEComplete(SettingsProvider mSettingsProvider) {
        return mSettingsProvider.getUserProfile() != null && mSettingsProvider.getUserProfile().isOobeComplete();
    }

    private static void startNextActivity(Activity activity, Intent intent) {
        startNextActivity(activity, intent, true);
    }

    private static void startNextActivity(Activity activity, Intent intent, boolean shouldCloseActivity) {
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        if (shouldCloseActivity) {
            activity.finish();
        }
    }

    private static void startNextActivityNoAnimation(Activity activity, Intent intent) {
        intent.setFlags(65536);
        activity.startActivity(intent);
        activity.finish();
    }
}
