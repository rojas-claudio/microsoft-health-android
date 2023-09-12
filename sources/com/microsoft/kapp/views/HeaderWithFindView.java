package com.microsoft.kapp.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.utils.ViewUtils;
/* loaded from: classes.dex */
public class HeaderWithFindView extends LinearLayout {
    private TextView mFind;

    public HeaderWithFindView(Context context) {
        super(context);
        initialize(context, null);
    }

    public HeaderWithFindView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    public HeaderWithFindView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context, attrs);
    }

    private void initialize(Context context, AttributeSet attrs) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.guided_workout_tile_header, this);
        TypedArray typedAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.HeaderWithFind);
        this.mFind = (TextView) ViewUtils.getValidView(view, R.id.header_find_title, TextView.class);
        try {
            CharSequence findText = typedAttributes.getText(0);
            if (!TextUtils.isEmpty(findText)) {
                this.mFind.setText(findText);
            }
        } finally {
            typedAttributes.recycle();
        }
    }
}
