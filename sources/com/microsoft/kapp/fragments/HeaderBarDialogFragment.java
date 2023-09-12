package com.microsoft.kapp.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import com.microsoft.kapp.activities.HeaderBarFragmentActivity;
/* loaded from: classes.dex */
public abstract class HeaderBarDialogFragment extends DialogFragment implements TextView.OnEditorActionListener {
    private HeaderBarFragmentAdapter mAdapter = new HeaderBarFragmentAdapter();
    private DialogInterface.OnCancelListener mCancelListener;
    private DialogInterface.OnDismissListener mDismissListener;

    public HeaderBarFragmentActivity getHeaderBarActivity() {
        return this.mAdapter.getHeaderBarActivity();
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener listener) {
        this.mDismissListener = listener;
    }

    public void setOnCancelListener(DialogInterface.OnCancelListener listener) {
        this.mCancelListener = listener;
    }

    @Override // android.support.v4.app.Fragment
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setOnKeyListener(new View.OnKeyListener() { // from class: com.microsoft.kapp.fragments.HeaderBarDialogFragment.1
            @Override // android.view.View.OnKeyListener
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == 4) {
                    HeaderBarDialogFragment.this.dismiss();
                    return true;
                }
                return false;
            }
        });
    }

    @Override // android.support.v4.app.DialogFragment, android.support.v4.app.Fragment
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mAdapter.onAttach(activity);
    }

    @Override // android.support.v4.app.DialogFragment, android.support.v4.app.Fragment
    public void onDetach() {
        super.onDetach();
        this.mAdapter.onDetach();
        this.mDismissListener = null;
        this.mCancelListener = null;
    }

    @Override // android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        this.mAdapter.onResume();
    }

    @Override // android.support.v4.app.Fragment
    public void onPause() {
        super.onPause();
        this.mAdapter.onPause();
    }

    @Override // android.support.v4.app.DialogFragment, android.content.DialogInterface.OnDismissListener
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (this.mDismissListener != null) {
            this.mDismissListener.onDismiss(dialog);
        }
    }

    @Override // android.support.v4.app.DialogFragment, android.content.DialogInterface.OnCancelListener
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (this.mCancelListener != null) {
            this.mCancelListener.onCancel(dialog);
        }
    }

    @Override // android.widget.TextView.OnEditorActionListener
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == 6) {
            dismiss();
            return true;
        }
        return false;
    }
}
