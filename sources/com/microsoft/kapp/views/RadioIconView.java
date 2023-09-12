package com.microsoft.kapp.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.microsoft.kapp.R;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class RadioIconView extends LinearLayout {
    private int mContainerHeightPixels;
    private int mContainerMarginPixels;
    private int mIconWidthHeightPixels;
    private View.OnClickListener mOnClickListener;
    private OnIconSelectedListener mOnIconSelectedListener;
    private int mSelectedDrawable;
    private List<Pair<Integer, Integer>> mTabData;
    private int mUnselectedDrawable;

    /* loaded from: classes.dex */
    public interface OnIconSelectedListener {
        void onIconSelected(int i);
    }

    public RadioIconView(Context context) {
        super(context);
        this.mOnClickListener = new View.OnClickListener() { // from class: com.microsoft.kapp.views.RadioIconView.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                OnIconSelectedListener listener = RadioIconView.this.mOnIconSelectedListener;
                for (int i = 0; i < RadioIconView.this.getChildCount(); i++) {
                    View child = RadioIconView.this.getChildAt(i);
                    if (child instanceof FrameLayout) {
                        FrameLayout currentView = (FrameLayout) child;
                        if (currentView != view) {
                            currentView.setBackgroundResource(RadioIconView.this.mUnselectedDrawable);
                        } else {
                            currentView.setBackgroundResource(RadioIconView.this.mSelectedDrawable);
                            if (listener != null) {
                                listener.onIconSelected(currentView.getId());
                            }
                        }
                    }
                }
            }
        };
        initialize(context, null);
    }

    public RadioIconView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mOnClickListener = new View.OnClickListener() { // from class: com.microsoft.kapp.views.RadioIconView.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                OnIconSelectedListener listener = RadioIconView.this.mOnIconSelectedListener;
                for (int i = 0; i < RadioIconView.this.getChildCount(); i++) {
                    View child = RadioIconView.this.getChildAt(i);
                    if (child instanceof FrameLayout) {
                        FrameLayout currentView = (FrameLayout) child;
                        if (currentView != view) {
                            currentView.setBackgroundResource(RadioIconView.this.mUnselectedDrawable);
                        } else {
                            currentView.setBackgroundResource(RadioIconView.this.mSelectedDrawable);
                            if (listener != null) {
                                listener.onIconSelected(currentView.getId());
                            }
                        }
                    }
                }
            }
        };
        initialize(context, attrs);
    }

    public RadioIconView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mOnClickListener = new View.OnClickListener() { // from class: com.microsoft.kapp.views.RadioIconView.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                OnIconSelectedListener listener = RadioIconView.this.mOnIconSelectedListener;
                for (int i = 0; i < RadioIconView.this.getChildCount(); i++) {
                    View child = RadioIconView.this.getChildAt(i);
                    if (child instanceof FrameLayout) {
                        FrameLayout currentView = (FrameLayout) child;
                        if (currentView != view) {
                            currentView.setBackgroundResource(RadioIconView.this.mUnselectedDrawable);
                        } else {
                            currentView.setBackgroundResource(RadioIconView.this.mSelectedDrawable);
                            if (listener != null) {
                                listener.onIconSelected(currentView.getId());
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
        inflater.inflate(R.layout.widget_radio_icon_view, this);
        if (attrs != null) {
            TypedArray a = null;
            try {
                a = context.obtainStyledAttributes(attrs, R.styleable.RadioIconView);
                this.mSelectedDrawable = a.getResourceId(0, R.drawable.exercise_chart_tab_selected);
                this.mUnselectedDrawable = a.getResourceId(1, R.drawable.exercise_chart_tab_unselected);
                this.mContainerHeightPixels = a.getDimensionPixelSize(2, getDimension(R.dimen.radio_icon_container_height));
                this.mContainerMarginPixels = a.getDimensionPixelSize(3, getDimension(R.dimen.radio_icon_container_margin));
                this.mIconWidthHeightPixels = a.getDimensionPixelSize(4, getDimension(R.dimen.radio_icon_icon_width_height));
            } finally {
                if (a != null) {
                    a.recycle();
                }
            }
        }
    }

    public void setOptions(int[] icons) {
        List<Pair<Integer, Integer>> iconPairs = new ArrayList<>();
        for (int i = 0; i < icons.length; i++) {
            iconPairs.add(new Pair<>(Integer.valueOf(icons[i]), Integer.valueOf(i)));
        }
        setOptions(iconPairs);
    }

    public void setOptions(List<Pair<Integer, Integer>> tabData) {
        this.mTabData = tabData;
        rebuildUI();
    }

    public void setOnIconSelectedListener(OnIconSelectedListener listener) {
        this.mOnIconSelectedListener = listener;
    }

    private void rebuildUI() {
        removeAllViews();
        if (this.mTabData != null) {
            int i = 0;
            while (i < this.mTabData.size()) {
                Pair<Integer, Integer> option = this.mTabData.get(i);
                FrameLayout frame = new FrameLayout(getContext());
                int backgroundResourceId = i == 0 ? this.mSelectedDrawable : this.mUnselectedDrawable;
                frame.setBackgroundResource(backgroundResourceId);
                LinearLayout.LayoutParams frameLayoutParams = new LinearLayout.LayoutParams(0, this.mContainerHeightPixels, 1.0f);
                int margin = this.mContainerMarginPixels;
                frameLayoutParams.setMargins(margin, margin, margin, margin);
                frame.setLayoutParams(frameLayoutParams);
                frame.setId(((Integer) option.second).intValue());
                frame.setOnClickListener(this.mOnClickListener);
                ImageView image = new ImageView(getContext());
                FrameLayout.LayoutParams imageLayoutParams = new FrameLayout.LayoutParams(this.mIconWidthHeightPixels, this.mIconWidthHeightPixels, 17);
                image.setLayoutParams(imageLayoutParams);
                image.setImageResource(((Integer) option.first).intValue());
                frame.addView(image);
                addView(frame);
                i++;
            }
        }
    }

    private int getDimension(int dimensionResourceId) {
        return (int) getResources().getDimension(dimensionResourceId);
    }
}
