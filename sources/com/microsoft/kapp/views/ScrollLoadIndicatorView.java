package com.microsoft.kapp.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import com.microsoft.kapp.R;
import com.microsoft.kapp.models.ScrollLoadStatus;
import com.microsoft.kapp.utils.ViewUtils;
/* loaded from: classes.dex */
public class ScrollLoadIndicatorView extends FrameLayout {
    private ProgressBar mProgress;

    public ScrollLoadIndicatorView(Context context) {
        super(context);
        initialize(context);
    }

    public ScrollLoadIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public ScrollLoadIndicatorView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context);
    }

    private void initialize(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.widget_scroll_load_indicator, this);
        this.mProgress = (ProgressBar) ViewUtils.getValidView(this, R.id.pull_load_progress, ProgressBar.class);
        setStatus(ScrollLoadStatus.COMPLETE);
    }

    public void setStatus(ScrollLoadStatus status) {
        switch (status) {
            case PARTIAL:
                setVisibility(0);
                this.mProgress.setVisibility(8);
                return;
            case LOADING:
                setVisibility(0);
                this.mProgress.setVisibility(0);
                return;
            case COMPLETE:
                setVisibility(8);
                return;
            default:
                return;
        }
    }
}
