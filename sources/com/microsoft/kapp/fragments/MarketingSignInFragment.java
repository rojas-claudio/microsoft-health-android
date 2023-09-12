package com.microsoft.kapp.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.activities.DialogManager;
import com.microsoft.kapp.activities.DialogPriority;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.utils.DialogManagerImpl;
import com.microsoft.kapp.utils.ViewUtils;
import java.lang.ref.WeakReference;
/* loaded from: classes.dex */
public class MarketingSignInFragment extends Fragment {
    private WeakReference<Callbacks> mWeakListener;

    /* loaded from: classes.dex */
    public interface Callbacks {
        void onSkipMarketScreen();
    }

    public static Fragment newInstance() {
        return new MarketingSignInFragment();
    }

    public DialogManager getDialogManager() {
        Activity activity = getActivity();
        return DialogManagerImpl.getDialogManager(activity);
    }

    @Override // android.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_oobe_marketing_login, container, false);
        if (rootView != null) {
            Button mNextButton = (Button) ViewUtils.getValidView(rootView, R.id.next_button, Button.class);
            mNextButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.MarketingSignInFragment.1
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    MarketingSignInFragment.this.skipToNextPage();
                }
            });
            TextView mWhyLink = (TextView) ViewUtils.getValidView(rootView, R.id.why_link, TextView.class);
            mWhyLink.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.MarketingSignInFragment.2
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    MarketingSignInFragment.this.getDialogManager().showDialog(MarketingSignInFragment.this.getActivity(), Integer.valueOf((int) R.string.oobe_marketing_why_link_title), Integer.valueOf((int) R.string.oobe_marketing_why_link_message), DialogPriority.LOW);
                }
            });
        }
        return rootView;
    }

    @Override // android.app.Fragment
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mWeakListener = new WeakReference<>((Callbacks) activity);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void skipToNextPage() {
        Callbacks listener;
        if (this.mWeakListener != null && (listener = this.mWeakListener.get()) != null) {
            listener.onSkipMarketScreen();
        }
    }

    @Override // android.app.Fragment
    public void onResume() {
        super.onResume();
        Telemetry.logPage(TelemetryConstants.PageViews.OOBE_LOGIN_MARKETING);
    }
}
