package com.microsoft.kapp.views;

import android.content.Context;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.utils.ViewUtils;
/* loaded from: classes.dex */
public class DoubleTrackerStat extends SingleTrackerStat {
    private TextView mSubTitleView;
    private TextView mSubValueView;

    public DoubleTrackerStat(Context context, String title, int symbolResourceId) {
        super(context, title, symbolResourceId);
        super.initialize(context, title, symbolResourceId);
    }

    @Override // com.microsoft.kapp.views.SingleTrackerStat
    protected void inflateLayout(Context context) {
        inflate(context, R.layout.double_tracker_stat, this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.views.SingleTrackerStat
    public void initialize(Context context, String title, int symbolResourceId) {
        super.initialize(context, title, symbolResourceId);
        this.mSubTitleView = (TextView) ViewUtils.getValidView(this, R.id.txtSubStatTitle, TextView.class);
        this.mSubValueView = (TextView) ViewUtils.getValidView(this, R.id.txtSubStatValue, TextView.class);
    }

    public void setSubTitle(String subTitle) {
        this.mSubTitleView.setText(subTitle);
    }

    public void setSubTitle(int subTitleResId) {
        this.mSubTitleView.setText(subTitleResId);
    }

    public void setSubValue(CharSequence subValue) {
        this.mSubValueView.setText(subValue);
    }

    public String getSubTitle() {
        return this.mSubTitleView.getText().toString();
    }

    public String getSubValue() {
        return this.mSubValueView.getText().toString();
    }
}
