package com.microsoft.kapp.fragments;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.utils.Constants;
import javax.inject.Inject;
import org.apache.commons.lang3.StringEscapeUtils;
/* loaded from: classes.dex */
public class AboutFragment extends BaseFragment {
    @Inject
    SettingsProvider mSettingsProvider;

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.settings_fragment_about, container, false);
        Context context = getActivity().getApplicationContext();
        TextView versionTextView = (TextView) rootView.findViewById(R.id.about_version);
        versionTextView.setText(getVersionText(context));
        TextView termsTextView = (TextView) rootView.findViewById(R.id.about_terms);
        String termsOfUse = getHyperlink(context.getString(R.string.about_termsofuse_text), Constants.TERMS_OF_USE_URL);
        termsTextView.setText(Html.fromHtml(termsOfUse));
        termsTextView.setText(Html.fromHtml(termsOfUse));
        termsTextView.setMovementMethod(LinkMovementMethod.getInstance());
        TextView privacyTextView = (TextView) rootView.findViewById(R.id.about_privacy);
        String privacy = getHyperlink(context.getString(R.string.about_privacy_text), Constants.PRIVACY_STATEMENT_URL);
        privacyTextView.setText(Html.fromHtml(privacy));
        privacyTextView.setMovementMethod(LinkMovementMethod.getInstance());
        TextView thirdPartyTextView = (TextView) rootView.findViewById(R.id.about_third_party);
        String thirdPartyNotices = getHyperlink(context.getString(R.string.about_third_party_notices_text), Constants.THIRD_PARTY_NOTICES_URL);
        thirdPartyTextView.setText(Html.fromHtml(thirdPartyNotices));
        thirdPartyTextView.setMovementMethod(LinkMovementMethod.getInstance());
        TextView feedbackTextView = (TextView) rootView.findViewById(R.id.about_feedback);
        String feedback = getHyperlink(context.getString(R.string.about_feedback_text), Constants.RATE_AND_REVIEW_URL);
        feedbackTextView.setText(Html.fromHtml(feedback));
        feedbackTextView.setMovementMethod(LinkMovementMethod.getInstance());
        return rootView;
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        Telemetry.logPage(TelemetryConstants.PageViews.SETTINGS_USER_ABOUT);
        setTopMenuBarColor(getResources().getColor(R.color.settingsMenuBar));
    }

    private String getHyperlink(String text, String url) {
        try {
            String textEscaped = StringEscapeUtils.escapeHtml3(text);
            String urlEscaped = StringEscapeUtils.escapeHtml3(url);
            return String.format(Constants.HTML_LINK_FORMAT_STRING, urlEscaped, textEscaped);
        } catch (Exception e) {
            KLog.e(this.TAG, "Exception in getHyperlink", e);
            return null;
        }
    }

    private String getVersionText(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException exception) {
            KLog.e(this.TAG, "Exception in getVersionText", exception);
            return null;
        }
    }
}
