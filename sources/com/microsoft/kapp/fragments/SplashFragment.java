package com.microsoft.kapp.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.microsoft.kapp.R;
import com.microsoft.kapp.diagnostics.Validate;
/* loaded from: classes.dex */
public class SplashFragment extends Fragment {
    private int mCountdownInterval;

    /* loaded from: classes.dex */
    public interface SplashFragmentCalls {
        void onSplashFragmentComplete();
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_splash, container, false);
        return rootView;
    }

    @Override // android.support.v4.app.Fragment
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Validate.isTrue(activity instanceof SplashFragmentCalls, "attaching Activity must implement SplashFragmentCalls");
        this.mCountdownInterval = activity.getResources().getInteger(R.integer.splash_display_interval_millis);
    }

    @Override // android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        CountDownTimer timer = new CountDownTimer(this.mCountdownInterval, this.mCountdownInterval) { // from class: com.microsoft.kapp.fragments.SplashFragment.1
            @Override // android.os.CountDownTimer
            public void onTick(long millisUntilFinished) {
            }

            @Override // android.os.CountDownTimer
            public void onFinish() {
                SplashFragment.this.onCountdownComplete();
            }
        };
        timer.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onCountdownComplete() {
        if (isResumed()) {
            ((SplashFragmentCalls) getActivity()).onSplashFragmentComplete();
        }
    }
}
