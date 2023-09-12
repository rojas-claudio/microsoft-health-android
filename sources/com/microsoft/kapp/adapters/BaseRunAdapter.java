package com.microsoft.kapp.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;
import java.text.DateFormat;
import java.util.List;
/* loaded from: classes.dex */
public abstract class BaseRunAdapter<T> extends ArrayAdapter<T> {
    protected DateFormat mDateFormat;
    protected int mLayoutId;

    public BaseRunAdapter(Context context, int layoutResourceId, List<T> values) {
        super(context, layoutResourceId, values);
        this.mLayoutId = layoutResourceId;
        this.mDateFormat = android.text.format.DateFormat.getDateFormat(context);
    }
}
