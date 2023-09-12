package com.microsoft.kapp.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import com.microsoft.kapp.R;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.views.CustomFontTextView;
import com.microsoft.kapp.views.CustomGlyphView;
/* loaded from: classes.dex */
public class NoHistoryWidget extends FrameLayout {
    private Context mContext;
    private GridView mGlyphGridView;
    private int mGlyphSize;
    private int mGridSize;
    private LinearLayout.LayoutParams mGridViewLayoutParams;
    private CustomFontTextView mNoFilteredHistoryTextView;
    private CustomFontTextView mNoHistorySubTitleTextView;
    private CustomFontTextView mNoHistoryTitleTextView;
    private AbsListView.LayoutParams mStrappImageLayoutParams;
    private int mStrappSize;

    public NoHistoryWidget(Context context) {
        super(context);
        this.mStrappSize = 0;
        initializeViews(context);
    }

    public NoHistoryWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mStrappSize = 0;
        initializeAttributes(attrs, context);
        initializeViews(context);
    }

    public NoHistoryWidget(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mStrappSize = 0;
        initializeAttributes(attrs, context);
        initializeViews(context);
    }

    private void initializeViews(Context context) {
        this.mContext = context;
        inflate(context, R.layout.widget_no_history, this);
        this.mGlyphGridView = (GridView) ViewUtils.getValidView(this, R.id.glyph_image_grid_view, GridView.class);
        this.mGlyphGridView.setAdapter((ListAdapter) new ImageAdapter());
        this.mGlyphGridView.setLayoutParams(this.mGridViewLayoutParams);
        this.mNoFilteredHistoryTextView = (CustomFontTextView) ViewUtils.getValidView(this, R.id.no_filtered_history_title, CustomFontTextView.class);
        this.mNoHistoryTitleTextView = (CustomFontTextView) ViewUtils.getValidView(this, R.id.no_history_title, CustomFontTextView.class);
        this.mNoHistorySubTitleTextView = (CustomFontTextView) ViewUtils.getValidView(this, R.id.no_history_subtitle, CustomFontTextView.class);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class ImageAdapter extends BaseAdapter {
        private Integer[] imageIds;

        private ImageAdapter() {
            this.imageIds = new Integer[]{Integer.valueOf((int) R.string.glyph_sleep), Integer.valueOf((int) R.string.glyph_run), Integer.valueOf((int) R.string.glyph_guided_workout), Integer.valueOf((int) R.string.glyph_workout)};
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return this.imageIds.length;
        }

        @Override // android.widget.Adapter
        public Object getItem(int position) {
            return this.imageIds[position];
        }

        @Override // android.widget.Adapter
        public long getItemId(int position) {
            return 0L;
        }

        @Override // android.widget.Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            CustomGlyphView strappImage = new CustomGlyphView(NoHistoryWidget.this.mContext, null);
            strappImage.setLayoutParams(NoHistoryWidget.this.mStrappImageLayoutParams);
            strappImage.setBackgroundResource(R.color.graphite);
            strappImage.setTextSize(NoHistoryWidget.this.mGlyphSize);
            strappImage.setTextColor(-1);
            strappImage.setGravity(17);
            String glyph = NoHistoryWidget.this.mContext.getResources().getString(this.imageIds[position].intValue());
            strappImage.setText(glyph);
            return strappImage;
        }
    }

    private void initializeAttributes(AttributeSet attrs, Context context) {
        TypedArray typedAttributes = context.obtainStyledAttributes(attrs, R.styleable.DefaultPageForNoHistoryWidget);
        try {
            this.mStrappSize = typedAttributes.getDimensionPixelSize(0, context.getResources().getDimensionPixelSize(R.dimen.default_no_histry_strapp_size));
            this.mGridSize = typedAttributes.getDimensionPixelSize(1, context.getResources().getDimensionPixelSize(R.dimen.default_no_histry_grid_size));
            this.mGridViewLayoutParams = new LinearLayout.LayoutParams(this.mGridSize, this.mGridSize);
            this.mGridViewLayoutParams.gravity = 17;
            this.mGlyphSize = typedAttributes.getInteger(2, context.getResources().getDimensionPixelSize(R.dimen.default_no_histry_glyph_size));
            this.mStrappImageLayoutParams = new AbsListView.LayoutParams(this.mStrappSize, this.mStrappSize);
        } finally {
            typedAttributes.recycle();
        }
    }

    public void showNoHistoryMessage(boolean isFilteredHistory, String eventType) {
        if (isFilteredHistory) {
            String noFilterHistoryText = String.format(this.mContext.getResources().getString(R.string.no_filtered_history_text), eventType);
            this.mNoFilteredHistoryTextView.setText(noFilterHistoryText);
            this.mNoFilteredHistoryTextView.setVisibility(0);
            this.mGlyphGridView.setVisibility(8);
            this.mNoHistoryTitleTextView.setVisibility(8);
            this.mNoHistorySubTitleTextView.setVisibility(8);
        } else {
            this.mNoFilteredHistoryTextView.setVisibility(8);
            this.mGlyphGridView.setVisibility(0);
            this.mNoHistoryTitleTextView.setVisibility(0);
            this.mNoHistorySubTitleTextView.setVisibility(0);
        }
        setVisibility(0);
    }
}
