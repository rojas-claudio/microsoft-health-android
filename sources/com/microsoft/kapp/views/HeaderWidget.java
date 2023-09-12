package com.microsoft.kapp.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.microsoft.kapp.R;
/* loaded from: classes.dex */
public class HeaderWidget extends LinearLayout {
    private TextView mTextView;
    private TextView mTitleView;

    public HeaderWidget(Context context) {
        super(context);
        initialize(context, null);
    }

    public HeaderWidget(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context, attrs);
    }

    public HeaderWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    private void initialize(Context context, AttributeSet attrs) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.widget_header, this);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HeaderWidget);
        try {
            int defaultTextSize = getResources().getDimensionPixelSize(R.dimen.header_text_size);
            int defaultTitleTextSize = getResources().getDimensionPixelSize(R.dimen.header_title_text_size);
            int defaultPadding = getResources().getDimensionPixelSize(R.dimen.header_padding);
            int defaultColor = getResources().getColor(R.color.header_default_text_color);
            int headerTextSize = a.getDimensionPixelSize(2, defaultTextSize);
            int headerTitleTextSize = a.getDimensionPixelSize(1, defaultTitleTextSize);
            int headerTextPadding = a.getDimensionPixelSize(3, defaultPadding);
            int headerTextColor = a.getColor(0, defaultColor);
            this.mTitleView = (TextView) findViewById(R.id.header_title);
            this.mTextView = (TextView) findViewById(R.id.header_text);
            this.mTitleView.setTextSize(0, headerTitleTextSize);
            this.mTitleView.setTextColor(headerTextColor);
            this.mTitleView.setPadding(0, 0, 0, headerTextPadding);
            this.mTextView.setTextSize(0, headerTextSize);
            this.mTextView.setTextColor(headerTextColor);
        } finally {
            a.recycle();
        }
    }

    public String getText() {
        if (this.mTextView == null) {
            return null;
        }
        return this.mTextView.getText().toString();
    }

    public void setText(String headerText) {
        if (this.mTextView != null) {
            this.mTextView.setText(headerText);
        }
        layoutHeader();
    }

    public String getTitle() {
        if (this.mTitleView == null) {
            return null;
        }
        return this.mTitleView.getText().toString();
    }

    public void setTitle(String headerTitle) {
        if (this.mTitleView != null) {
            this.mTitleView.setText(headerTitle);
        }
        layoutHeader();
    }

    private void layoutHeader() {
        if (this.mTitleView != null && TextUtils.isEmpty(this.mTitleView.getText())) {
            this.mTitleView.setVisibility(8);
        }
    }
}
