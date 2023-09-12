package com.microsoft.kapp.fragments;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.microsoft.kapp.R;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.strappsettings.StrappSettingsManager;
import com.microsoft.kapp.utils.AppConfigurationManager;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.views.CustomFontButton;
import com.microsoft.kapp.views.CustomFontTextView;
import com.microsoft.kapp.views.CustomGlyphView;
import com.microsoft.kapp.views.PicassoImageView;
import com.squareup.picasso.Picasso;
import java.lang.ref.WeakReference;
import javax.inject.Inject;
import org.apache.commons.lang3.StringEscapeUtils;
/* loaded from: classes.dex */
public class StarbucksOverviewFragment extends BaseFragment {
    private static final String TAG = StarbucksOverviewFragment.class.getSimpleName();
    private static final int numberOfDigitsBeforeSeperatorInCardNumber = 4;
    @Inject
    AppConfigurationManager mAppConfigurationManager;
    private CustomGlyphView mBackButton;
    private CustomFontTextView mCardNumberText;
    private CustomFontButton mRemoveCardButton;
    private WeakReference<OnRemoveCardListener> mRemoveCardListener;
    @Inject
    StrappSettingsManager mSettingsManager;
    private PicassoImageView mStarbucksFrontImage;
    private CustomFontTextView mStarbucksLoginLink;

    /* loaded from: classes.dex */
    public interface OnRemoveCardListener {
        void moveToNoCardFragment();
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_starbucks_overview, container, false);
        this.mCardNumberText = (CustomFontTextView) ViewUtils.getValidView(view, R.id.card_number, CustomFontTextView.class);
        String starbucksCardNumber = this.mSettingsManager.getStarbucksCardNumber();
        if (starbucksCardNumber != null) {
            this.mCardNumberText.setText(formatCardNumber(starbucksCardNumber));
        } else {
            this.mCardNumberText.setText(getString(R.string.empty));
        }
        this.mStarbucksLoginLink = (CustomFontTextView) ViewUtils.getValidView(view, R.id.starbucks_settings_description, CustomFontTextView.class);
        String starbucksLoginText = getStarbucksHyperlink(this.mAppConfigurationManager.getStarbucksUrlDisplayString(getActivity()), this.mAppConfigurationManager.getStarbucksUrl());
        this.mStarbucksLoginLink.setText(Html.fromHtml(starbucksLoginText));
        this.mStarbucksLoginLink.setMovementMethod(LinkMovementMethod.getInstance());
        this.mBackButton = (CustomGlyphView) ViewUtils.getValidView(view, R.id.starbucks_overview_back_button, CustomGlyphView.class);
        this.mBackButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.StarbucksOverviewFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                StarbucksOverviewFragment.this.getActivity().finish();
            }
        });
        this.mRemoveCardButton = (CustomFontButton) ViewUtils.getValidView(view, R.id.starbucks_remove_card_button, CustomFontButton.class);
        this.mRemoveCardButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.StarbucksOverviewFragment.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                StarbucksOverviewFragment.this.mSettingsManager.setStarbucksCardNumber("");
                OnRemoveCardListener listener = StarbucksOverviewFragment.this.getRemoveCardListener();
                if (listener != null) {
                    listener.moveToNoCardFragment();
                }
            }
        });
        this.mStarbucksFrontImage = (PicassoImageView) ViewUtils.getValidView(view, R.id.starbucks_front, PicassoImageView.class);
        String starbucksImageUrl = this.mAppConfigurationManager.getStarbucksFrontImageUrl();
        if (!TextUtils.isEmpty(starbucksImageUrl)) {
            Picasso.with(getActivity()).load(starbucksImageUrl).into(this.mStarbucksFrontImage);
        }
        return view;
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        Telemetry.logEvent(TelemetryConstants.PageViews.SETTINGS_BAND_MANAGE_TILES_CONNECTED_STARBUCKS_OVERVIEW);
    }

    public void setRemoveCardListener(OnRemoveCardListener onRemoveCardListener) {
        this.mRemoveCardListener = new WeakReference<>(onRemoveCardListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public OnRemoveCardListener getRemoveCardListener() {
        if (this.mRemoveCardListener == null) {
            return null;
        }
        OnRemoveCardListener listener = this.mRemoveCardListener.get();
        return listener;
    }

    private String getStarbucksHyperlink(String text, String url) {
        try {
            String textEscaped = StringEscapeUtils.escapeHtml3(text);
            String urlEscaped = StringEscapeUtils.escapeHtml3(url);
            String formatted = String.format(Constants.HTML_LINK_FORMAT_STRING, urlEscaped, textEscaped);
            return String.format(getResources().getString(R.string.settings_starbucks_card_settings_descriptions), formatted);
        } catch (Exception e) {
            KLog.e(TAG, "Exception in getHyperlink", e);
            return null;
        }
    }

    private String formatCardNumber(String cardNumber) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cardNumber.length(); i++) {
            if (i > 0 && i % 4 == 0) {
                sb.append(' ');
            }
            sb.append(cardNumber.charAt(i));
        }
        return sb.toString();
    }
}
