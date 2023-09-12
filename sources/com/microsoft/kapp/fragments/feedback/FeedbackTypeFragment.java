package com.microsoft.kapp.fragments.feedback;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.fragments.BaseFragment;
import com.microsoft.kapp.utils.Constants;
import java.util.HashMap;
/* loaded from: classes.dex */
public class FeedbackTypeFragment extends BaseFragment {
    private static final String FEEDBACK_TYPES = "feedback_types";
    private String[] mFeedbackTypes;
    private FeedbackTypeListener mListener;

    /* loaded from: classes.dex */
    public interface FeedbackTypeListener {
        void onFeedbackTypeSelected(String str);
    }

    public static FeedbackTypeFragment newInstance(String[] feedbackTypes) {
        FeedbackTypeFragment fragment = new FeedbackTypeFragment();
        Bundle args = new Bundle();
        args.putStringArray(FEEDBACK_TYPES, feedbackTypes);
        fragment.setArguments(args);
        return fragment;
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mFeedbackTypes = getArguments().getStringArray(FEEDBACK_TYPES);
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        Telemetry.logPage("Feedback/Selection");
    }

    @Override // android.support.v4.app.Fragment
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof FeedbackTypeListener) {
            this.mListener = (FeedbackTypeListener) activity;
        }
    }

    @Override // android.support.v4.app.Fragment
    public void onDetach() {
        this.mListener = null;
        super.onDetach();
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.feedback_type_fragment, container, false);
        if (this.mFeedbackTypes != null) {
            ListView typeList = (ListView) rootView.findViewById(R.id.lstFeedbackTypes);
            typeList.setAdapter((ListAdapter) new ArrayAdapter(getActivity(), 17367043, this.mFeedbackTypes));
            typeList.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.microsoft.kapp.fragments.feedback.FeedbackTypeFragment.1
                @Override // android.widget.AdapterView.OnItemClickListener
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            FeedbackTypeFragment.this.logEventType(TelemetryConstants.Events.SendFeedbackSelection.Dimensions.BUG_REPORT);
                            if (FeedbackTypeFragment.this.mListener != null) {
                                FeedbackTypeFragment.this.mListener.onFeedbackTypeSelected(FeedbackTypeFragment.this.mFeedbackTypes[position]);
                                return;
                            }
                            return;
                        case 1:
                            FeedbackTypeFragment.this.logEventType(TelemetryConstants.Events.SendFeedbackSelection.Dimensions.HELP);
                            Intent supportIntent = new Intent("android.intent.action.VIEW", Uri.parse(Constants.HELP_URL));
                            FeedbackTypeFragment.this.startActivity(supportIntent);
                            return;
                        case 2:
                            FeedbackTypeFragment.this.logEventType(TelemetryConstants.Events.SendFeedbackSelection.Dimensions.USERVOICE);
                            Intent suggestionIntent = new Intent("android.intent.action.VIEW", Uri.parse(Constants.SUGGEST_AN_IDEA_URL));
                            FeedbackTypeFragment.this.startActivity(suggestionIntent);
                            return;
                        default:
                            return;
                    }
                }
            });
        }
        return rootView;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logEventType(String type) {
        HashMap<String, String> properties = new HashMap<>();
        properties.put("Selection", type);
        Telemetry.logEvent("Feedback/Selection", properties, null);
    }
}
