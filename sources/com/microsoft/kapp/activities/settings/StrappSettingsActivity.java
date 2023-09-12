package com.microsoft.kapp.activities.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.activities.BaseActivity;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.models.strapp.DefaultStrappUUID;
import com.microsoft.kapp.strappsettings.StrappSettingsManager;
import com.microsoft.kapp.utils.ActivityUtils;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.Formatter;
import com.microsoft.kapp.views.CustomGlyphView;
import java.util.UUID;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class StrappSettingsActivity extends BaseActivity {
    public static final String EXTRA_UUID_STRAPPID = "uuid_strappId";
    private Button mAdditionalSettingsButton;
    private TextView mAdditionalSettingsHeaderText;
    private LinearLayout mAdditionalSettingsLayout;
    private boolean mAdjusted;
    private CustomGlyphView mBackArrow;
    private LinearLayout mEditRepliesInstructions;
    private boolean mInitialized;
    private FrameLayout mLoadingFrameLayout;
    private TextView mNotificationsInstructionalText;
    private Switch mNotificationsSwitch;
    private UUID mStrappId;
    private TextView mStrappSettingsHeader;
    @Inject
    StrappSettingsManager mStrappSettingsManager;
    private TextView mStrappSettingsSwitchLabel;
    private StrappType mStrappType;
    private String mStringStrappId;
    private static final String UUID_STRING_STRAPP_CALENDAR = DefaultStrappUUID.STRAPP_CALENDAR.toString();
    private static final String UUID_STRING_STRAPP_MESSAGING = DefaultStrappUUID.STRAPP_MESSAGING.toString();
    private static final String UUID_STRING_STRAPP_CALLS = DefaultStrappUUID.STRAPP_CALLS.toString();
    private static final String UUID_STRING_STRAPP_EMAIL = DefaultStrappUUID.STRAPP_EMAIL.toString();
    private static final String UUID_STRING_STRAPP_FACEBOOK = DefaultStrappUUID.STRAPP_FACEBOOK.toString();
    private static final String UUID_STRING_STRAPP_FACEBOOK_MESSENGER = DefaultStrappUUID.STRAPP_FACEBOOK_MESSENGER.toString();
    private static final String UUID_STRING_STRAPP_NOTIFICATION_CENTER = DefaultStrappUUID.STRAPP_NOTIFICATION_CENTER.toString();
    private static final String UUID_STRING_STRAPP_TWITTER = DefaultStrappUUID.STRAPP_TWITTER.toString();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public enum StrappType {
        MESSAGING,
        CALLS,
        CALENDAR,
        EMAIL,
        FACEBOOK,
        FACEBOOK_MESSENGER,
        NOTIFICATIONCENTER,
        TWITTER,
        UNKNOWN
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.activities.BaseActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strapp_settings);
        this.mBackArrow = (CustomGlyphView) ActivityUtils.getAndValidateView(this, R.id.back, CustomGlyphView.class);
        this.mStrappSettingsHeader = (TextView) ActivityUtils.getAndValidateView(this, R.id.strapp_settings_header_text, TextView.class);
        this.mNotificationsInstructionalText = (TextView) ActivityUtils.getAndValidateView(this, R.id.notifications_instructional_text, TextView.class);
        this.mNotificationsSwitch = (Switch) ActivityUtils.getAndValidateView(this, R.id.notifications_switch, Switch.class);
        this.mStrappSettingsSwitchLabel = (TextView) ActivityUtils.getAndValidateView(this, R.id.notifications_switch_text, TextView.class);
        this.mAdditionalSettingsLayout = (LinearLayout) ActivityUtils.getAndValidateView(this, R.id.additional_settings_layout, LinearLayout.class);
        this.mEditRepliesInstructions = (LinearLayout) ActivityUtils.getAndValidateView(this, R.id.edit_replies_instructional_text, LinearLayout.class);
        this.mAdditionalSettingsHeaderText = (TextView) ActivityUtils.getAndValidateView(this, R.id.additional_settings_header_text, TextView.class);
        this.mAdditionalSettingsButton = (Button) ActivityUtils.getAndValidateView(this, R.id.additional_settings_button, Button.class);
        this.mLoadingFrameLayout = (FrameLayout) ActivityUtils.getAndValidateView(this, R.id.loading_frame_layout, FrameLayout.class);
        this.mLoadingFrameLayout.setOnClickListener(null);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.mStrappId = (UUID) extras.getSerializable("uuid_strappId");
            this.mStringStrappId = this.mStrappId.toString();
        }
        this.mStrappType = getStrappType(this.mStringStrappId);
        Validate.notNull(this.mStrappSettingsManager, "mStrappSettingsManager");
        this.mBackArrow.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.activities.settings.StrappSettingsActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                ActivityUtils.performBackButton(StrappSettingsActivity.this);
            }
        });
        this.mNotificationsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.microsoft.kapp.activities.settings.StrappSettingsActivity.2
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (StrappSettingsActivity.this.mInitialized) {
                    StrappSettingsActivity.this.mAdjusted = true;
                }
                StrappSettingsActivity.this.mStrappSettingsSwitchLabel.setText(isChecked ? R.string.notification_settings_notification_on : R.string.notification_settings_notification_off);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.activities.BaseActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        logPageViewForTelemetry();
        this.mInitialized = false;
        initializeUIForStrapp();
        initializeSettings();
    }

    private void logPageViewForTelemetry() {
        switch (this.mStrappType) {
            case CALENDAR:
                Telemetry.logPage(TelemetryConstants.PageViews.SETTINGS_BAND_MANAGE_TILES_CONNECTED_NOTIFICATIONS_CALENDAR);
                return;
            case CALLS:
                Telemetry.logPage(TelemetryConstants.PageViews.SETTINGS_BAND_MANAGE_TILES_CONNECTED_NOTIFICATIONS_CALLS);
                return;
            case EMAIL:
                Telemetry.logPage(TelemetryConstants.PageViews.SETTINGS_BAND_MANAGE_TILES_CONNECTED_NOTIFICATIONS_EMAIL);
                return;
            case FACEBOOK:
                Telemetry.logPage(TelemetryConstants.PageViews.SETTINGS_BAND_MANAGE_TILES_CONNECTED_NOTIFICATIONS_FACEBOOK);
                return;
            case FACEBOOK_MESSENGER:
                Telemetry.logPage(TelemetryConstants.PageViews.SETTINGS_BAND_MANAGE_TILES_CONNECTED_NOTIFICATIONS_FACEBOOK_MESSENGER);
                return;
            case MESSAGING:
                Telemetry.logPage(TelemetryConstants.PageViews.SETTINGS_BAND_MANAGE_TILES_CONNECTED_NOTIFICATIONS_SMS);
                return;
            case NOTIFICATIONCENTER:
                Telemetry.logPage(TelemetryConstants.PageViews.SETTINGS_BAND_MANAGE_TILES_CONNECTED_NOTIFICATIONS_NOTIFICATION_CENTER);
                return;
            case TWITTER:
                Telemetry.logPage(TelemetryConstants.PageViews.SETTINGS_BAND_MANAGE_TILES_CONNECTED_NOTIFICATIONS_TWITTER);
                return;
            case UNKNOWN:
            default:
                return;
        }
    }

    @Override // android.app.Activity
    public void onBackPressed() {
        if (this.mAdjusted) {
            saveSettings();
        } else {
            finish();
        }
    }

    private static StrappType getStrappType(String strappId) {
        if (isMessagingStrapp(strappId)) {
            return StrappType.MESSAGING;
        }
        if (isCallsStrapp(strappId)) {
            return StrappType.CALLS;
        }
        if (isCalendarStrapp(strappId)) {
            return StrappType.CALENDAR;
        }
        if (isEmailStrapp(strappId)) {
            return StrappType.EMAIL;
        }
        if (isFacebookStrapp(strappId)) {
            return StrappType.FACEBOOK;
        }
        if (isFacebookMessengerStrapp(strappId)) {
            return StrappType.FACEBOOK_MESSENGER;
        }
        if (isNotificationCenterStrapp(strappId)) {
            return StrappType.NOTIFICATIONCENTER;
        }
        if (isTwitterStrapp(strappId)) {
            return StrappType.TWITTER;
        }
        return StrappType.UNKNOWN;
    }

    private void initializeSettings() {
        boolean notificationsEnabled = false;
        if (isMessagingStrapp(this.mStringStrappId)) {
            notificationsEnabled = this.mStrappSettingsManager.isMessagingDeviceNotificationsEnabled();
        } else if (isCallsStrapp(this.mStringStrappId)) {
            notificationsEnabled = this.mStrappSettingsManager.isCallsDeviceNotificationsEnabled();
        } else if (isCalendarStrapp(this.mStringStrappId)) {
            notificationsEnabled = this.mStrappSettingsManager.isCalendarDeviceNotificationsEnabled();
        } else if (isEmailStrapp(this.mStringStrappId)) {
            notificationsEnabled = this.mStrappSettingsManager.isEmailDeviceNotificationsEnabled();
        } else if (isFacebookStrapp(this.mStringStrappId)) {
            notificationsEnabled = this.mStrappSettingsManager.isFacebookDeviceNotificationsEnabled();
        } else if (isFacebookMessengerStrapp(this.mStringStrappId)) {
            notificationsEnabled = this.mStrappSettingsManager.isFacebookMessengerDeviceNotificationsEnabled();
        } else if (isNotificationCenterStrapp(this.mStringStrappId)) {
            notificationsEnabled = this.mStrappSettingsManager.isNotificationCenterDeviceNotificationsEnabled();
        } else if (isTwitterStrapp(this.mStringStrappId)) {
            notificationsEnabled = this.mStrappSettingsManager.isTwitterDeviceNotificationsEnabled();
        }
        this.mNotificationsSwitch.setChecked(notificationsEnabled);
        this.mStrappSettingsSwitchLabel.setText(notificationsEnabled ? R.string.notification_settings_notification_on : R.string.notification_settings_notification_off);
        this.mInitialized = true;
    }

    private void saveSettings() {
        boolean notificationsEnabled = this.mNotificationsSwitch.isChecked();
        this.mStrappSettingsManager.setTransactionNotificationsEnabled(this.mStrappId, notificationsEnabled);
        this.mStrappSettingsManager.setTransactionNotificationsChanged(true);
        finish();
    }

    private void initializeUIForStrapp() {
        if (isMessagingStrapp(this.mStringStrappId)) {
            this.mStrappSettingsHeader.setText(R.string.notification_settings_messaging_header);
            this.mNotificationsInstructionalText.setText(R.string.notification_settings_notifications_messaging_instructions);
            addExisitingReplies();
            this.mAdditionalSettingsLayout.setVisibility(0);
            this.mAdditionalSettingsHeaderText.setText(R.string.notification_settings_messaging_reply_header);
            addAutoRepliesListener();
        } else if (isCallsStrapp(this.mStringStrappId)) {
            this.mStrappSettingsHeader.setText(R.string.notification_settings_calls_header);
            this.mNotificationsInstructionalText.setText(R.string.notification_settings_notifications_calls_instructions);
            addExisitingReplies();
            this.mAdditionalSettingsLayout.setVisibility(0);
            this.mAdditionalSettingsHeaderText.setText(R.string.notification_settings_calls_reply_header);
            addAutoRepliesListener();
        } else if (isCalendarStrapp(this.mStringStrappId)) {
            this.mStrappSettingsHeader.setText(R.string.notification_settings_calendar_header);
            this.mNotificationsInstructionalText.setText(R.string.notification_settings_notifications_calendar_instructions);
            this.mAdditionalSettingsLayout.setVisibility(8);
        } else if (isEmailStrapp(this.mStringStrappId)) {
            this.mStrappSettingsHeader.setText(R.string.notification_settings_email_header);
            this.mNotificationsInstructionalText.setText(R.string.notification_settings_notifications_email_instructions);
            this.mAdditionalSettingsLayout.setVisibility(8);
        } else if (isFacebookStrapp(this.mStringStrappId)) {
            this.mStrappSettingsHeader.setText(R.string.notification_settings_facebook_header);
            this.mNotificationsInstructionalText.setText(R.string.notification_settings_notifications_facebook_instructions);
            this.mAdditionalSettingsLayout.setVisibility(8);
        } else if (isFacebookMessengerStrapp(this.mStringStrappId)) {
            this.mStrappSettingsHeader.setText(R.string.notification_settings_facebook_messenger_header);
            this.mNotificationsInstructionalText.setText(R.string.notification_settings_notifications_facebook_messenger_instructions);
            this.mAdditionalSettingsLayout.setVisibility(8);
        } else if (isNotificationCenterStrapp(this.mStringStrappId)) {
            this.mStrappSettingsHeader.setText(R.string.notification_settings_notification_center_header);
            this.mNotificationsInstructionalText.setText(R.string.notification_settings_notifications_notification_center_instructions);
            this.mAdditionalSettingsLayout.setVisibility(0);
            this.mAdditionalSettingsHeaderText.setVisibility(8);
            this.mAdditionalSettingsButton.setText(R.string.notification_settings_notification_center_enable_apps_text);
            this.mAdditionalSettingsButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.activities.settings.StrappSettingsActivity.3
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    Intent notificationSettingsIntent = new Intent(StrappSettingsActivity.this, NotificationCenterSettingsActivity.class);
                    StrappSettingsActivity.this.startActivity(notificationSettingsIntent);
                }
            });
        } else if (isTwitterStrapp(this.mStringStrappId)) {
            this.mStrappSettingsHeader.setText(R.string.notification_settings_twitter_header);
            this.mNotificationsInstructionalText.setText(R.string.notification_settings_notifications_twitter_instructions);
            this.mAdditionalSettingsLayout.setVisibility(8);
        }
    }

    private void addAutoRepliesListener() {
        this.mAdditionalSettingsButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.activities.settings.StrappSettingsActivity.4
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                Intent notificationSettingsIntent = new Intent(StrappSettingsActivity.this, StrappAutoRepliesActivity.class);
                notificationSettingsIntent.putExtra("uuid_strappId", StrappSettingsActivity.this.mStrappId);
                StrappSettingsActivity.this.startActivity(notificationSettingsIntent);
            }
        });
    }

    private static boolean isMessagingStrapp(String strappId) {
        return UUID_STRING_STRAPP_MESSAGING.equalsIgnoreCase(strappId);
    }

    private static boolean isCallsStrapp(String strappId) {
        return UUID_STRING_STRAPP_CALLS.equalsIgnoreCase(strappId);
    }

    private static boolean isCalendarStrapp(String strappId) {
        return UUID_STRING_STRAPP_CALENDAR.equalsIgnoreCase(strappId);
    }

    private static boolean isEmailStrapp(String strappId) {
        return UUID_STRING_STRAPP_EMAIL.equalsIgnoreCase(strappId);
    }

    private static boolean isFacebookStrapp(String strappId) {
        return UUID_STRING_STRAPP_FACEBOOK.equalsIgnoreCase(strappId);
    }

    private static boolean isFacebookMessengerStrapp(String strappId) {
        return UUID_STRING_STRAPP_FACEBOOK_MESSENGER.equalsIgnoreCase(strappId);
    }

    private static boolean isNotificationCenterStrapp(String strappId) {
        return UUID_STRING_STRAPP_NOTIFICATION_CENTER.equalsIgnoreCase(strappId);
    }

    private static boolean isTwitterStrapp(String strappId) {
        return UUID_STRING_STRAPP_TWITTER.equalsIgnoreCase(strappId);
    }

    private void addExisitingReplies() {
        this.mEditRepliesInstructions.removeAllViews();
        String text = "";
        for (int i = 1; i <= Constants.NUMBER_OF_AUTO_REPLIES; i++) {
            if (isMessagingStrapp(this.mStringStrappId)) {
                text = this.mStrappSettingsManager.getMessagingAutoReply(i);
            } else if (isCallsStrapp(this.mStringStrappId)) {
                text = this.mStrappSettingsManager.getCallsAutoReply(i);
            }
            if (!text.isEmpty()) {
                addTextViewToEditRepliesInstruction(text);
            }
        }
    }

    private void addTextViewToEditRepliesInstruction(String text) {
        TextView tv = new TextView(this, null, R.style.Font_smallBody);
        tv.setText(Formatter.addQuotationMarks(text));
        tv.setTextColor(getResources().getColor(R.color.greyHigh));
        this.mEditRepliesInstructions.addView(tv);
    }
}
