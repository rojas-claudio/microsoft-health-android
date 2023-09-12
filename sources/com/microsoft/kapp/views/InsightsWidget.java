package com.microsoft.kapp.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.microsoft.kapp.R;
/* loaded from: classes.dex */
public class InsightsWidget extends LinearLayout {
    TextView mInsightsActionMsgView;
    TextView mInsightsMsgView;

    public InsightsWidget(Context context) {
        super(context);
        init(context);
    }

    public InsightsWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public InsightsWidget(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.widget_insights, this);
        this.mInsightsMsgView = (TextView) findViewById(R.id.insights_msg);
        this.mInsightsActionMsgView = (TextView) findViewById(R.id.insights_action_msg);
        this.mInsightsActionMsgView.setVisibility(8);
    }

    public void setMessage(String message) {
        this.mInsightsMsgView.setText(message);
    }

    public void setActionMessage(String actionMessage) {
        this.mInsightsActionMsgView.setText(actionMessage);
        this.mInsightsActionMsgView.setVisibility(0);
    }
}
