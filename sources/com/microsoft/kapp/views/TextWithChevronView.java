package com.microsoft.kapp.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import com.microsoft.kapp.R;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.utils.ViewUtils;
/* loaded from: classes.dex */
public class TextWithChevronView extends RelativeLayout {
    private CustomFontTextView mTextView;
    private View mView;

    public TextWithChevronView(Context context) {
        super(context);
        initialize(context, null);
    }

    public TextWithChevronView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs);
    }

    public TextWithChevronView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    public void setClickListener(View.OnClickListener clickListener) {
        Validate.notNull(clickListener, "click listener");
        this.mView.setOnClickListener(clickListener);
    }

    private void initialize(Context context, AttributeSet attributes) {
        TypedArray typedAttributes = getContext().obtainStyledAttributes(attributes, R.styleable.TextWithChevron);
        try {
            this.mView = inflate(context, R.layout.text_with_chevron, this);
            CharSequence titleText = typedAttributes.getText(0);
            this.mTextView = (CustomFontTextView) ViewUtils.getValidView(this, R.id.titleText, CustomFontTextView.class);
            this.mTextView.setText(titleText);
        } finally {
            typedAttributes.recycle();
        }
    }
}
