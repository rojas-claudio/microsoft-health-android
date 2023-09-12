package com.microsoft.kapp.fragments.feedback;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.fragments.BaseFragment;
import com.microsoft.kapp.utils.ViewUtils;
/* loaded from: classes.dex */
public class FeedbackDescriptionEditFragment extends BaseFragment {
    private EditText mEditText;
    private FeedbackDescriptionListener mListener;

    /* loaded from: classes.dex */
    public interface FeedbackDescriptionListener {
        String getFeedbackText();

        void onFeedbackDescriptionEntered(String str);
    }

    public static FeedbackDescriptionEditFragment newInstance() {
        return new FeedbackDescriptionEditFragment();
    }

    @Override // android.support.v4.app.Fragment
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof FeedbackDescriptionListener) {
            this.mListener = (FeedbackDescriptionListener) activity;
        }
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        this.mEditText.requestFocus();
    }

    @Override // android.support.v4.app.Fragment
    public void onDetach() {
        this.mListener = null;
        super.onDetach();
    }

    @Override // android.support.v4.app.Fragment
    public void onStart() {
        super.onStart();
        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService("input_method");
        inputManager.toggleSoftInput(0, 0);
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.feedback_description_edit_fragment, container, false);
        this.mEditText = (EditText) rootView.findViewById(R.id.feedback_description_text);
        TextView doneGlyph = (TextView) rootView.findViewById(R.id.done_text);
        TextView cancelGlyph = (TextView) rootView.findViewById(R.id.cancel);
        cancelGlyph.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.feedback.FeedbackDescriptionEditFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                ViewUtils.closeSoftKeyboard(FeedbackDescriptionEditFragment.this.getActivity(), v);
                FeedbackDescriptionEditFragment.this.getActivity().onBackPressed();
            }
        });
        this.mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: com.microsoft.kapp.fragments.feedback.FeedbackDescriptionEditFragment.2
            @Override // android.widget.TextView.OnEditorActionListener
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case 2:
                    case 6:
                        FeedbackDescriptionEditFragment.this.onDone(v);
                        return true;
                    default:
                        return false;
                }
            }
        });
        if (this.mListener != null) {
            this.mEditText.setText(this.mListener.getFeedbackText());
        }
        doneGlyph.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.feedback.FeedbackDescriptionEditFragment.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                FeedbackDescriptionEditFragment.this.onDone(v);
            }
        });
        return rootView;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onDone(View view) {
        ViewUtils.closeSoftKeyboard(getActivity(), view);
        if (this.mListener != null) {
            this.mListener.onFeedbackDescriptionEntered(this.mEditText.getText().toString());
        }
    }
}
