package com.microsoft.kapp.views;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.utils.Formatter;
import com.microsoft.kapp.utils.ViewUtils;
/* loaded from: classes.dex */
public class SingleTrackerStat extends BaseTrackerStat {
    private TextView mValueView;

    public SingleTrackerStat(Context context, String title) {
        super(context);
        initialize(context, title, -1);
    }

    public SingleTrackerStat(Context context, String title, int symbolResourceId) {
        super(context);
        initialize(context, title, symbolResourceId);
    }

    protected void inflateLayout(Context context) {
        inflate(context, R.layout.single_tracker_stat, this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void initialize(Context context, String title, int symbolResourceId) {
        inflateLayout(context);
        super.initialize();
        String contentDescBase = getContentDescBase(title);
        setTitleContentDesc(contentDescBase + "_title");
        setTitle(title);
        if (symbolResourceId >= 0) {
            setTitleSymbol(symbolResourceId);
        } else {
            setTitleSymbolVisibility(8);
        }
        this.mValueView = (TextView) ViewUtils.getValidView(this, R.id.txtStatValue, TextView.class);
    }

    public void setValue(CharSequence value) {
        this.mValueView.setText(value);
    }

    public String getValue() {
        return this.mValueView.getText().toString();
    }

    public void ensureValue(CharSequence value, Context context) {
        if (TextUtils.isEmpty(value)) {
            CharSequence notAvailable = Formatter.getSubtextSpannable(context, context.getString(R.string.no_value), 0);
            this.mValueView.setText(notAvailable);
            return;
        }
        this.mValueView.setText(value);
    }
}
