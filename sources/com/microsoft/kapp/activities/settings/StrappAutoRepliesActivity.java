package com.microsoft.kapp.activities.settings;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.activities.BaseActivity;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.models.strapp.DefaultStrappUUID;
import com.microsoft.kapp.strappsettings.StrappSettingsManager;
import com.microsoft.kapp.utils.ActivityUtils;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.widgets.ConfirmationBar;
import java.util.UUID;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class StrappAutoRepliesActivity extends BaseActivity {
    public static final String EXTRA_UUID_STRAPPID = "uuid_strappId";
    private ConfirmationBar mConfirmationBar;
    private TextView mErrorText;
    private TextView mHeader;
    private FrameLayout mLoadingFrameLayout;
    private EditText mReply1Edit;
    private EditText mReply2Edit;
    private EditText mReply3Edit;
    private EditText mReply4Edit;
    private UUID mStrappId;
    @Inject
    StrappSettingsManager mStrappSettingsManager;
    private String mStringStrappId;
    private static final String UUID_STRING_STRAPP_MESSAGING = DefaultStrappUUID.STRAPP_MESSAGING.toString();
    private static final String UUID_STRING_STRAPP_CALLS = DefaultStrappUUID.STRAPP_CALLS.toString();

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.activities.BaseActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strapp_auto_replies);
        getWindow().setSoftInputMode(16);
        getWindow().setSoftInputMode(3);
        this.mHeader = (TextView) ActivityUtils.getAndValidateView(this, R.id.strapp_auto_replies_header_text, TextView.class);
        this.mErrorText = (TextView) ActivityUtils.getAndValidateView(this, R.id.strapp_auto_replies_error_text, TextView.class);
        this.mReply1Edit = (EditText) ActivityUtils.getAndValidateView(this, R.id.strapp_auto_replies_reply_1_edit, EditText.class);
        this.mReply2Edit = (EditText) ActivityUtils.getAndValidateView(this, R.id.strapp_auto_replies_reply_2_edit, EditText.class);
        this.mReply3Edit = (EditText) ActivityUtils.getAndValidateView(this, R.id.strapp_auto_replies_reply_3_edit, EditText.class);
        this.mReply4Edit = (EditText) ActivityUtils.getAndValidateView(this, R.id.strapp_auto_replies_reply_4_edit, EditText.class);
        this.mConfirmationBar = (ConfirmationBar) ActivityUtils.getAndValidateView(this, R.id.confirmation_bar, ConfirmationBar.class);
        this.mConfirmationBar.setVisibility(0);
        this.mLoadingFrameLayout = (FrameLayout) ActivityUtils.getAndValidateView(this, R.id.loading_frame_layout, FrameLayout.class);
        this.mLoadingFrameLayout.setOnClickListener(null);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.mStrappId = (UUID) extras.getSerializable("uuid_strappId");
            this.mStringStrappId = this.mStrappId.toString();
        }
        if (!isValidStrapp(this.mStringStrappId)) {
            finish();
            return;
        }
        Validate.notNull(this.mStrappSettingsManager, "mStrappSettingsManager");
        initializeUIForStrapp();
        initializeSettings();
        TextWatcher editWatcher = new TextWatcher() { // from class: com.microsoft.kapp.activities.settings.StrappAutoRepliesActivity.1
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable s) {
                StrappAutoRepliesActivity.this.mErrorText.setVisibility(8);
            }
        };
        this.mReply1Edit.addTextChangedListener(editWatcher);
        this.mReply2Edit.addTextChangedListener(editWatcher);
        this.mReply3Edit.addTextChangedListener(editWatcher);
        this.mReply4Edit.addTextChangedListener(editWatcher);
        this.mConfirmationBar.setOnConfirmButtonClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.activities.settings.StrappAutoRepliesActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (StrappAutoRepliesActivity.this.validateReplies()) {
                    StrappAutoRepliesActivity.this.saveSettings();
                    return;
                }
                StrappAutoRepliesActivity.this.mErrorText.setVisibility(0);
                StrappAutoRepliesActivity.this.mErrorText.setText(R.string.notification_settings_auto_reply_save_failed_message);
            }
        });
        this.mConfirmationBar.setOnCancelButtonClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.activities.settings.StrappAutoRepliesActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                StrappAutoRepliesActivity.this.finish();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.activities.BaseActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        logPageViewForTelemetry();
    }

    private void logPageViewForTelemetry() {
        if (isMessagingStrapp(this.mStringStrappId)) {
            Telemetry.logPage(TelemetryConstants.PageViews.SETTINGS_BAND_MANAGE_TILES_CONNECTED_EDIT_CUSTOM_RESPONSES_SMS);
        } else if (isCallsStrapp(this.mStringStrappId)) {
            Telemetry.logPage(TelemetryConstants.PageViews.SETTINGS_BAND_MANAGE_TILES_CONNECTED_EDIT_CUSTOM_RESPONSES_CALLS);
        }
    }

    private void initializeSettings() {
        if (isMessagingStrapp(this.mStringStrappId)) {
            this.mReply1Edit.setText(this.mStrappSettingsManager.getMessagingAutoReply(1));
            this.mReply2Edit.setText(this.mStrappSettingsManager.getMessagingAutoReply(2));
            this.mReply3Edit.setText(this.mStrappSettingsManager.getMessagingAutoReply(3));
            this.mReply4Edit.setText(this.mStrappSettingsManager.getMessagingAutoReply(4));
        } else if (isCallsStrapp(this.mStringStrappId)) {
            this.mReply1Edit.setText(this.mStrappSettingsManager.getCallsAutoReply(1));
            this.mReply2Edit.setText(this.mStrappSettingsManager.getCallsAutoReply(2));
            this.mReply3Edit.setText(this.mStrappSettingsManager.getCallsAutoReply(3));
            this.mReply4Edit.setText(this.mStrappSettingsManager.getCallsAutoReply(4));
        }
        this.mReply1Edit.setSelection(this.mReply1Edit.getText().length());
        this.mReply1Edit.requestFocus();
        ViewUtils.closeSoftKeyboard(this, this.mReply1Edit);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveSettings() {
        this.mStrappSettingsManager.setTransactionAutoReplies(this.mStrappId, this.mReply1Edit.getText().toString(), this.mReply2Edit.getText().toString(), this.mReply3Edit.getText().toString(), this.mReply4Edit.getText().toString());
        finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean validateReplies() {
        return ("".equals(this.mReply1Edit.getText().toString()) && "".equals(this.mReply2Edit.getText().toString()) && "".equals(this.mReply3Edit.getText().toString()) && "".equals(this.mReply4Edit.getText().toString())) ? false : true;
    }

    private void initializeUIForStrapp() {
        if (isMessagingStrapp(this.mStringStrappId)) {
            this.mHeader.setText(R.string.strapp_auto_replies_messaging_header);
        } else if (isCallsStrapp(this.mStringStrappId)) {
            this.mHeader.setText(R.string.strapp_auto_replies_calls_header);
        }
    }

    private boolean isValidStrapp(String strappId) {
        return isMessagingStrapp(strappId) || isCallsStrapp(strappId);
    }

    private boolean isMessagingStrapp(String strappId) {
        return UUID_STRING_STRAPP_MESSAGING.equalsIgnoreCase(strappId);
    }

    private static boolean isCallsStrapp(String strappId) {
        return UUID_STRING_STRAPP_CALLS.equalsIgnoreCase(strappId);
    }
}
