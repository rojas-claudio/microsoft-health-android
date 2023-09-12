package com.microsoft.kapp.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.utils.ViewUtils;
/* loaded from: classes.dex */
public class FilterHeaderView extends LinearLayout {
    private CustomGlyphView mClose;
    private TextView mFilterHeader;
    private TextView mFilterSubHeader;

    public FilterHeaderView(Context context) {
        super(context);
        initialize(context, null);
    }

    public FilterHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    public FilterHeaderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context, attrs);
    }

    private void initialize(Context context, AttributeSet attrs) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.history_filters_list_header, this);
        TypedArray typedAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.FilterHeader);
        this.mClose = (CustomGlyphView) ViewUtils.getValidView(view, R.id.close_button_glyph, CustomGlyphView.class);
        this.mFilterHeader = (TextView) ViewUtils.getValidView(view, R.id.filter_header, TextView.class);
        this.mFilterSubHeader = (TextView) ViewUtils.getValidView(view, R.id.filter_sub_header, TextView.class);
        try {
            CharSequence filterHeaderText = typedAttributes.getText(0);
            CharSequence filterSubHeaderText = typedAttributes.getText(1);
            int aligment = typedAttributes.getInt(2, 0);
            if (!TextUtils.isEmpty(filterHeaderText)) {
                this.mFilterHeader.setText(filterHeaderText);
            }
            if (!TextUtils.isEmpty(filterSubHeaderText) && context.getString(R.string.no_value).equals(String.valueOf(filterSubHeaderText))) {
                this.mFilterSubHeader.setVisibility(4);
            }
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.mClose.getLayoutParams();
            if (aligment == 1) {
                layoutParams.removeRule(11);
                layoutParams.addRule(9);
                this.mClose.setLayoutParams(layoutParams);
            }
        } finally {
            typedAttributes.recycle();
        }
    }

    public void setCloseOnClickListener(View.OnClickListener listener) {
        this.mClose.setOnClickListener(listener);
    }
}
