package com.microsoft.kapp.fragments.feedback;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.microsoft.kapp.R;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.fragments.AttachmentFragment;
import com.microsoft.kapp.fragments.BaseFragment;
import com.microsoft.kapp.views.CustomFontButton;
import com.microsoft.kapp.views.CustomFontTextView;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class FeedbackDescriptionFragment extends BaseFragment {
    private AttachmentFragment mAttachmentFragment;
    private ArrayList<Uri> mAttachmentUris;
    private FeedbackSummaryListener mListener;
    private CustomFontTextView mTextPreview;

    /* loaded from: classes.dex */
    public interface FeedbackSummaryListener {
        String getFeedbackText();

        void onEditTextButtonPressed();

        void onNextButtonPressed();

        void setAttachments(ArrayList<Uri> arrayList);
    }

    public static FeedbackDescriptionFragment newInstance() {
        return new FeedbackDescriptionFragment();
    }

    @Override // android.support.v4.app.Fragment
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof FeedbackSummaryListener) {
            this.mListener = (FeedbackSummaryListener) activity;
        }
    }

    @Override // android.support.v4.app.Fragment
    public void onDetach() {
        this.mListener = null;
        super.onDetach();
    }

    @Override // android.support.v4.app.Fragment
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.mAttachmentFragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstancxeState) {
        View rootView = inflater.inflate(R.layout.feedback_description_fragment, container, false);
        this.mTextPreview = (CustomFontTextView) rootView.findViewById(R.id.feedback_text_preview);
        this.mTextPreview.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.feedback.FeedbackDescriptionFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (FeedbackDescriptionFragment.this.mListener != null) {
                    FeedbackDescriptionFragment.this.mListener.setAttachments(FeedbackDescriptionFragment.this.mAttachmentFragment.getAttachmentUris());
                    FeedbackDescriptionFragment.this.mListener.onEditTextButtonPressed();
                }
            }
        });
        CustomFontButton doneGlyph = (CustomFontButton) rootView.findViewById(R.id.feedback_next_page);
        doneGlyph.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.feedback.FeedbackDescriptionFragment.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (FeedbackDescriptionFragment.this.mListener != null) {
                    FeedbackDescriptionFragment.this.mListener.setAttachments(FeedbackDescriptionFragment.this.mAttachmentFragment.getAttachmentUris());
                    FeedbackDescriptionFragment.this.mListener.onNextButtonPressed();
                }
            }
        });
        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        this.mAttachmentFragment = AttachmentFragment.newInstance();
        ft.replace(R.id.fragmentAttachments, this.mAttachmentFragment);
        ft.commit();
        if (this.mAttachmentUris != null) {
            this.mAttachmentFragment.setAttachmentUris(this.mAttachmentUris);
        }
        return rootView;
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        Telemetry.logPage(TelemetryConstants.PageViews.SEND_FEEDBACK_DESCRIPTION);
        if (this.mListener != null && this.mTextPreview != null) {
            this.mTextPreview.setText(this.mListener.getFeedbackText());
        }
    }

    public void setAttachments(ArrayList<Uri> imageUris) {
        this.mAttachmentUris = imageUris;
    }
}
