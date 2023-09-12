package com.microsoft.kapp.views;

import android.content.Context;
import com.microsoft.kapp.R;
/* loaded from: classes.dex */
public class DescriptiveTextTrackerStat extends SingleTrackerStat {
    public DescriptiveTextTrackerStat(Context context, String title) {
        super(context, title);
    }

    public DescriptiveTextTrackerStat(Context context, String title, int symbolResourceId) {
        super(context, title, symbolResourceId);
    }

    @Override // com.microsoft.kapp.views.SingleTrackerStat
    protected void inflateLayout(Context context) {
        inflate(context, R.layout.descriptive_text_stat, this);
    }
}
