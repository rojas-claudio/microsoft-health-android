package com.microsoft.kapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.microsoft.kapp.R;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.strappsettings.StrappSettingsManager;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.views.CustomFontButton;
import com.microsoft.kapp.views.CustomGlyphView;
import java.lang.ref.WeakReference;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class StarbucksNoCardsOverviewFragment extends BaseFragment {
    private CustomFontButton mAddCardButton;
    private WeakReference<OnAddCardClickListener> mAddCardClickListener;
    private CustomGlyphView mBackButton;
    @Inject
    StrappSettingsManager mSettingsManager;

    /* loaded from: classes.dex */
    public interface OnAddCardClickListener {
        void moveToManageCardsFragment();
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_starbucks_no_card_overview, container, false);
        this.mBackButton = (CustomGlyphView) ViewUtils.getValidView(view, R.id.starbucks_overview_back_button, CustomGlyphView.class);
        this.mBackButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.StarbucksNoCardsOverviewFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                StarbucksNoCardsOverviewFragment.this.getActivity().finish();
            }
        });
        this.mAddCardButton = (CustomFontButton) ViewUtils.getValidView(view, R.id.starbucks_add_card_button, CustomFontButton.class);
        this.mAddCardButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.StarbucksNoCardsOverviewFragment.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                OnAddCardClickListener listener = StarbucksNoCardsOverviewFragment.this.getAddCardClickListener();
                if (listener != null) {
                    listener.moveToManageCardsFragment();
                }
            }
        });
        return view;
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        Telemetry.logEvent(TelemetryConstants.PageViews.SETTINGS_BAND_MANAGE_TILES_CONNECTED_STARBUCKS_OVERVIEW);
    }

    public void setAddCardClickListener(OnAddCardClickListener onAddCardClickListener) {
        this.mAddCardClickListener = new WeakReference<>(onAddCardClickListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public OnAddCardClickListener getAddCardClickListener() {
        if (this.mAddCardClickListener == null) {
            return null;
        }
        OnAddCardClickListener listener = this.mAddCardClickListener.get();
        return listener;
    }
}
