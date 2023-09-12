package com.microsoft.kapp.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.microsoft.kapp.R;
/* loaded from: classes.dex */
public class RadioTextView extends LinearLayout {
    private int mColorSelected;
    private int mColorUnselected;
    private View.OnClickListener mOnClickListener;
    private OnTextViewSelectedListener mOnTextViewSelectedListener;

    /* loaded from: classes.dex */
    public interface OnTextViewSelectedListener {
        void onTextViewSelected(TextView textView, int i);
    }

    public RadioTextView(Context context) {
        super(context);
        this.mOnClickListener = new View.OnClickListener() { // from class: com.microsoft.kapp.views.RadioTextView.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                OnTextViewSelectedListener listener = RadioTextView.this.mOnTextViewSelectedListener;
                for (int i = 0; i < RadioTextView.this.getChildCount(); i++) {
                    if (RadioTextView.this.getChildAt(i) instanceof TextView) {
                        TextView curr = (TextView) RadioTextView.this.getChildAt(i);
                        if (curr != v) {
                            curr.setTextColor(RadioTextView.this.mColorUnselected);
                        } else {
                            curr.setTextColor(RadioTextView.this.mColorSelected);
                            if (listener != null) {
                                listener.onTextViewSelected(curr, curr.getId());
                            }
                        }
                    }
                }
            }
        };
        initialize(context, null);
    }

    public RadioTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mOnClickListener = new View.OnClickListener() { // from class: com.microsoft.kapp.views.RadioTextView.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                OnTextViewSelectedListener listener = RadioTextView.this.mOnTextViewSelectedListener;
                for (int i = 0; i < RadioTextView.this.getChildCount(); i++) {
                    if (RadioTextView.this.getChildAt(i) instanceof TextView) {
                        TextView curr = (TextView) RadioTextView.this.getChildAt(i);
                        if (curr != v) {
                            curr.setTextColor(RadioTextView.this.mColorUnselected);
                        } else {
                            curr.setTextColor(RadioTextView.this.mColorSelected);
                            if (listener != null) {
                                listener.onTextViewSelected(curr, curr.getId());
                            }
                        }
                    }
                }
            }
        };
        initialize(context, attrs);
    }

    public RadioTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mOnClickListener = new View.OnClickListener() { // from class: com.microsoft.kapp.views.RadioTextView.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                OnTextViewSelectedListener listener = RadioTextView.this.mOnTextViewSelectedListener;
                for (int i = 0; i < RadioTextView.this.getChildCount(); i++) {
                    if (RadioTextView.this.getChildAt(i) instanceof TextView) {
                        TextView curr = (TextView) RadioTextView.this.getChildAt(i);
                        if (curr != v) {
                            curr.setTextColor(RadioTextView.this.mColorUnselected);
                        } else {
                            curr.setTextColor(RadioTextView.this.mColorSelected);
                            if (listener != null) {
                                listener.onTextViewSelected(curr, curr.getId());
                            }
                        }
                    }
                }
            }
        };
        initialize(context, attrs);
    }

    private void initialize(Context context, AttributeSet attrs) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.widget_radio_text_view, this);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RadioTextView);
        try {
            setOptions(a.getTextArray(0), a.getColor(1, -7829368), a.getColor(2, -16777216));
        } finally {
            a.recycle();
        }
    }

    public void setOptions(CharSequence[] options, int selectedColor, int unselectedColor) {
        if (options != null) {
            removeAllViews();
            this.mColorSelected = selectedColor;
            this.mColorUnselected = unselectedColor;
            int i = 0;
            while (i < options.length) {
                TextView v = new TextView(getContext());
                v.setText(options[i]);
                v.setGravity(17);
                v.setTextColor(i == 0 ? this.mColorSelected : this.mColorUnselected);
                v.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1.0f));
                v.setOnClickListener(this.mOnClickListener);
                v.setId(i);
                addView(v);
                i++;
            }
        }
    }

    public void setOnTextViewSelectedListener(OnTextViewSelectedListener listener) {
        this.mOnTextViewSelectedListener = listener;
    }
}
