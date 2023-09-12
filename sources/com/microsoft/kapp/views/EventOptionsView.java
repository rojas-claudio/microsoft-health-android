package com.microsoft.kapp.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.utils.ViewUtils;
/* loaded from: classes.dex */
public class EventOptionsView extends LinearLayout {
    private TextView mDeleteEventButton;
    private TextView mRenameEventButton;

    /* loaded from: classes.dex */
    public interface OnEventOptionSelected {
        void onDeleteEventPressed();

        void onRenameEventPressed();
    }

    public EventOptionsView(Context context) {
        super(context);
        initialize();
    }

    public EventOptionsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public EventOptionsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }

    private void initialize() {
        inflate(getContext(), R.layout.event_options_layout, this);
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mRenameEventButton = (TextView) ViewUtils.getValidView(this, R.id.event_rename_button, TextView.class);
        this.mDeleteEventButton = (TextView) ViewUtils.getValidView(this, R.id.event_delete_button, TextView.class);
    }

    public void setOnEventOptionSelectedListener(final OnEventOptionSelected onEventOptionSelected) {
        this.mRenameEventButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.views.EventOptionsView.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                onEventOptionSelected.onRenameEventPressed();
            }
        });
        this.mDeleteEventButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.views.EventOptionsView.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                onEventOptionSelected.onDeleteEventPressed();
            }
        });
    }
}
