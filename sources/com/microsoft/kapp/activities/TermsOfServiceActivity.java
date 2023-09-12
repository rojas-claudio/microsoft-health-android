package com.microsoft.kapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.logging.http.FiddlerLogger;
import com.microsoft.kapp.models.FreStatus;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.FreUtils;
import com.microsoft.krestsdk.auth.credentials.CredentialsManager;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class TermsOfServiceActivity extends OobeBaseActivity {
    @Inject
    CredentialsManager mCredentialsManager;
    @Inject
    FiddlerLogger mFiddlerLogger;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.activities.OobeBaseActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oobe_terms_of_service_activity);
        TextView termsOfServiceTitle = (TextView) findViewById(R.id.oobe_title);
        String termsOfServiceTitleString = String.format(getResources().getString(R.string.oobe_terms_of_service_title), new Object[0]);
        TextView termsOfServiceText = (TextView) findViewById(R.id.oobe_subtitle);
        String termsOfServiceString = String.format(getResources().getString(R.string.oobe_terms_of_service_text), new Object[0]);
        TextView termsOfServicePrivacyLinkText = (TextView) findViewById(R.id.oobe_terms_of_service_privacy_link);
        String termsOfServicePrivacyString = String.format(getResources().getString(R.string.oobe_terms_of_service_privacy_link), "", "");
        termsOfServiceTitle.setText(termsOfServiceTitleString);
        termsOfServiceText.setText(termsOfServiceString);
        termsOfServicePrivacyLinkText.setText(termsOfServicePrivacyString);
        termsOfServicePrivacyLinkText.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.activities.TermsOfServiceActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse(Constants.PRIVACY_STATEMENT_URL));
                TermsOfServiceActivity.this.startActivity(viewIntent);
            }
        });
        TextView confirmText = (TextView) findViewById(R.id.oobe_confirm);
        confirmText.setText(R.string.I_agree);
        confirmText.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.activities.TermsOfServiceActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                TermsOfServiceActivity.this.mSettingsProvider.setFreStatus(FreStatus.TOS_SIGNED);
                FreUtils.freRedirect(TermsOfServiceActivity.this, TermsOfServiceActivity.this.mSettingsProvider);
            }
        });
        TextView cancelText = (TextView) findViewById(R.id.oobe_cancel);
        cancelText.setText(R.string.cancel);
        cancelText.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.activities.TermsOfServiceActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                TermsOfServiceActivity.this.mCredentialsManager.deleteCredentials(TermsOfServiceActivity.this);
                TermsOfServiceActivity.this.mSettingsProvider.handleLogout();
                TermsOfServiceActivity.this.mFiddlerLogger.cleanup();
                TermsOfServiceActivity.this.startActivity(new Intent(TermsOfServiceActivity.this, SignInActivity.class));
                TermsOfServiceActivity.this.finish();
            }
        });
    }
}
