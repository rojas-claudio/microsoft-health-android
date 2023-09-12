package com.microsoft.kapp.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.activities.DialogPriority;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.strappsettings.StrappSettingsManager;
import com.microsoft.kapp.utils.AppConfigurationManager;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.views.FormattedNumberEditText;
import com.microsoft.kapp.views.PicassoImageView;
import com.microsoft.kapp.widgets.ConfirmationBar;
import com.squareup.picasso.Picasso;
import java.lang.ref.WeakReference;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class StarbucksAddCardFragment extends BaseFragment {
    private WeakReference<AddCardListener> mAddCardListener;
    @Inject
    AppConfigurationManager mAppConfigurationManager;
    private FormattedNumberEditText mCardNumber;
    private ConfirmationBar mConfirmationBar;
    @Inject
    StrappSettingsManager mSettingsManager;
    private PicassoImageView mStarbucksCardBack;

    /* loaded from: classes.dex */
    public interface AddCardListener {
        void onAddCard();

        void onCancelCard();
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_starbucks_add_card, container, false);
        this.mConfirmationBar = (ConfirmationBar) ViewUtils.getValidView(view, R.id.confirmation_bar, ConfirmationBar.class);
        this.mConfirmationBar.setOnConfirmButtonClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.StarbucksAddCardFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                StarbucksAddCardFragment.this.onAddCardClick();
            }
        });
        this.mConfirmationBar.setOnCancelButtonClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.StarbucksAddCardFragment.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                AddCardListener listener = StarbucksAddCardFragment.this.getOnAddCardListener();
                if (listener != null) {
                    listener.onCancelCard();
                }
            }
        });
        this.mCardNumber = (FormattedNumberEditText) ViewUtils.getValidView(view, R.id.card_number, FormattedNumberEditText.class);
        this.mStarbucksCardBack = (PicassoImageView) ViewUtils.getValidView(view, R.id.starbucks_card_back, PicassoImageView.class);
        String starbucksImageUrl = this.mAppConfigurationManager.getStarbucksBackImageUrl();
        if (!TextUtils.isEmpty(starbucksImageUrl)) {
            Picasso.with(getActivity()).load(starbucksImageUrl).into(this.mStarbucksCardBack);
        }
        String starbucksCardNumber = this.mSettingsManager.getStarbucksCardNumber();
        this.mCardNumber.setNumber(starbucksCardNumber);
        this.mCardNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: com.microsoft.kapp.fragments.StarbucksAddCardFragment.3
            @Override // android.widget.TextView.OnEditorActionListener
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == 6) {
                    StarbucksAddCardFragment.this.onAddCardClick();
                    return false;
                }
                return false;
            }
        });
        return view;
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        Telemetry.logEvent(TelemetryConstants.PageViews.SETTINGS_BAND_MANAGE_TILES_CONNECTED_STARBUCKS_ADD_CARD);
    }

    public void setOnAddCardListener(AddCardListener addCardListener) {
        this.mAddCardListener = new WeakReference<>(addCardListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public AddCardListener getOnAddCardListener() {
        if (this.mAddCardListener == null) {
            return null;
        }
        AddCardListener listener = this.mAddCardListener.get();
        return listener;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onAddCardClick() {
        String cardNumber = this.mCardNumber.getNumber();
        if (cardNumber == null || cardNumber.length() != 16) {
            getDialogManager().showDialog(getActivity(), Integer.valueOf((int) R.string.settings_starbucks_incorrect_card_title), Integer.valueOf((int) R.string.settings_starbucks_incorrect_card_message), DialogPriority.LOW);
            return;
        }
        this.mSettingsManager.setStarbucksCardNumber(cardNumber);
        AddCardListener listener = getOnAddCardListener();
        if (listener != null) {
            listener.onAddCard();
        }
    }
}
