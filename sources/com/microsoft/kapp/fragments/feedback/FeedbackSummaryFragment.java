package com.microsoft.kapp.fragments.feedback;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.fragments.BaseFragment;
/* loaded from: classes.dex */
public class FeedbackSummaryFragment extends BaseFragment {
    private TextView mDescriptionSummary;
    private Switch mIncludeLogSwitch;
    private FeedbackConfirmationListener mListener;

    /* loaded from: classes.dex */
    public interface FeedbackConfirmationListener {
        String getFeedbackSummaryText();

        void sendFeedback();
    }

    public static FeedbackSummaryFragment newInstance() {
        return new FeedbackSummaryFragment();
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        Telemetry.logPage(TelemetryConstants.PageViews.SEND_FEEDBACK_SUMMARY);
    }

    @Override // android.support.v4.app.Fragment
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof FeedbackConfirmationListener) {
            this.mListener = (FeedbackConfirmationListener) activity;
        }
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.feedback_summary_fragment, container, false);
        this.mDescriptionSummary = (TextView) rootView.findViewById(R.id.description_summary);
        if (this.mListener != null) {
            this.mDescriptionSummary.setText(this.mListener.getFeedbackSummaryText());
        }
        this.mIncludeLogSwitch = (Switch) rootView.findViewById(R.id.btnIncludeLogs);
        TextView sendFeedbackButton = (TextView) rootView.findViewById(R.id.send_button);
        sendFeedbackButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.feedback.FeedbackSummaryFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (FeedbackSummaryFragment.this.mListener != null) {
                    FeedbackSummaryFragment.this.mListener.sendFeedback();
                }
            }
        });
        return rootView;
    }

    public boolean shouldIncludeLogs() {
        return this.mIncludeLogSwitch.isChecked();
    }
}
