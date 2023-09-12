package com.microsoft.kapp.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.utils.ViewUtils;
/* loaded from: classes.dex */
public class SyncToBand extends LinearLayout {
    private CustomGlyphView mBandGlyph;
    private ViewGroup mDownloadContainer;
    private CustomGlyphView mDownloadGlyph;
    private CharSequence mOnBandTitle;
    private FrameLayout mSyncContainer;
    private ProgressBar mSyncProgress;
    private CharSequence mSyncTitle;
    private TextView mSyncTitleTextView;

    /* loaded from: classes.dex */
    public enum SyncStatus {
        SyncRequired,
        SyncStart,
        Synced
    }

    public SyncToBand(Context context) {
        super(context);
        initialize(context, null);
    }

    public SyncToBand(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs);
    }

    public SyncToBand(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    protected void initialize(Context context, AttributeSet attrs) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.sync_to_band, this);
        TypedArray typedAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.SyncToBand);
        try {
            this.mSyncTitle = typedAttributes.getText(0);
            this.mOnBandTitle = typedAttributes.getText(1);
            this.mSyncTitleTextView = (TextView) ViewUtils.getValidView(view, R.id.golf_sync_course_to_band, TextView.class);
            this.mBandGlyph = (CustomGlyphView) ViewUtils.getValidView(view, R.id.glyph_band, CustomGlyphView.class);
            this.mSyncContainer = (FrameLayout) ViewUtils.getValidView(view, R.id.golf_sync_circle_container, FrameLayout.class);
            this.mSyncProgress = (ProgressBar) ViewUtils.getValidView(view, R.id.sync_progress, ProgressBar.class);
            this.mDownloadGlyph = (CustomGlyphView) ViewUtils.getValidView(view, R.id.glyph_download_circle, CustomGlyphView.class);
            this.mDownloadContainer = (ViewGroup) ViewUtils.getValidView(view, R.id.golf_sync_circle_container, FrameLayout.class);
            setState(SyncStatus.SyncRequired);
        } catch (Exception e) {
            typedAttributes.recycle();
        }
    }

    public void setState(SyncStatus status) {
        if (SyncStatus.SyncRequired.equals(status)) {
            this.mSyncTitleTextView.setText(this.mSyncTitle);
            this.mBandGlyph.setVisibility(8);
            this.mSyncContainer.setVisibility(0);
            this.mSyncProgress.setVisibility(8);
            this.mDownloadGlyph.setVisibility(0);
        } else if (SyncStatus.SyncStart.equals(status)) {
            this.mSyncTitleTextView.setText(this.mSyncTitle);
            this.mBandGlyph.setVisibility(8);
            this.mSyncContainer.setVisibility(0);
            this.mSyncProgress.setVisibility(0);
            this.mDownloadGlyph.setVisibility(8);
        } else {
            this.mSyncTitleTextView.setText(this.mOnBandTitle);
            this.mBandGlyph.setVisibility(0);
            this.mSyncContainer.setVisibility(8);
        }
    }

    public void setSyncClickListnere(View.OnClickListener listener) {
        this.mDownloadContainer.setOnClickListener(listener);
    }
}
