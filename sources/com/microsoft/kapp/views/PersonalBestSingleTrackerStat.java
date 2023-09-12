package com.microsoft.kapp.views;

import android.content.Context;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.utils.ViewUtils;
/* loaded from: classes.dex */
public class PersonalBestSingleTrackerStat extends SingleTrackerStat {
    private TextView mDateView;

    public PersonalBestSingleTrackerStat(Context context, String title) {
        super(context, title);
    }

    public PersonalBestSingleTrackerStat(Context context, String title, int symbolResourceId) {
        super(context, title, symbolResourceId);
    }

    @Override // com.microsoft.kapp.views.SingleTrackerStat
    protected void inflateLayout(Context context) {
        inflate(context, R.layout.personal_best_single_tracker_stat, this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.views.SingleTrackerStat
    public void initialize(Context context, String title, int symbolResourceId) {
        super.initialize(context, title, symbolResourceId);
        this.mDateView = (TextView) ViewUtils.getValidView(this, R.id.txt_stat_date, TextView.class);
    }

    public void setDate(CharSequence date) {
        this.mDateView.setText(date);
    }

    public String getDate() {
        return this.mDateView.getText().toString();
    }
}
