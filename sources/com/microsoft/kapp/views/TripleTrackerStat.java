package com.microsoft.kapp.views;

import android.content.Context;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.utils.ViewUtils;
/* loaded from: classes.dex */
public class TripleTrackerStat extends SingleTrackerStat {
    private TextView mLeftSubTitleView;
    private TextView mLeftSubValueView;
    private TextView mRightSubTitleView;
    private TextView mRightSubValueView;

    public TripleTrackerStat(Context context, String title, int styleResourceId) {
        super(context, title, styleResourceId);
    }

    @Override // com.microsoft.kapp.views.SingleTrackerStat
    protected void inflateLayout(Context context) {
        inflate(context, R.layout.triple_tracker_stat, this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.views.SingleTrackerStat
    public void initialize(Context context, String title, int symbolResourceId) {
        super.initialize(context, title, symbolResourceId);
        this.mLeftSubTitleView = (TextView) ViewUtils.getValidView(this, R.id.txtLeftSubStatTitle, TextView.class);
        this.mRightSubTitleView = (TextView) ViewUtils.getValidView(this, R.id.txtRightSubStatTitle, TextView.class);
        this.mLeftSubValueView = (TextView) ViewUtils.getValidView(this, R.id.txtLeftSubStatValue, TextView.class);
        this.mRightSubValueView = (TextView) ViewUtils.getValidView(this, R.id.txtRightSubStatValue, TextView.class);
    }

    public void setLeftTitle(String subTitle) {
        this.mLeftSubTitleView.setText(subTitle);
    }

    public void setLeftTitle(int subTitleResId) {
        this.mLeftSubTitleView.setText(subTitleResId);
    }

    public void setRightTitle(String subTitle) {
        this.mRightSubTitleView.setText(subTitle);
    }

    public void setRightTitle(int subTitleResId) {
        this.mRightSubTitleView.setText(subTitleResId);
    }

    public void setLeftValue(String subValue) {
        this.mLeftSubValueView.setText(subValue);
    }

    public void setRightValue(String subValue) {
        this.mRightSubValueView.setText(subValue);
    }

    public void setLeftValue(CharSequence subValue) {
        this.mLeftSubValueView.setText(subValue);
    }

    public void setRightValue(CharSequence subValue) {
        this.mRightSubValueView.setText(subValue);
    }

    public String getLeftTitle() {
        return this.mLeftSubTitleView.getText().toString();
    }

    public String getRightTitle() {
        return this.mRightSubTitleView.getText().toString();
    }

    public String getLeftValue() {
        return this.mLeftSubValueView.getText().toString();
    }

    public String getRightValue() {
        return this.mRightSubValueView.getText().toString();
    }
}
