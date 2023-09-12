package com.microsoft.kapp.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.Spannable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.microsoft.kapp.KApplicationGraph;
import com.microsoft.kapp.R;
import com.microsoft.kapp.activities.HomeTileStateChangedListener;
import com.microsoft.kapp.activities.HomeTileStateChangedNotifier;
import com.microsoft.kapp.style.utils.MetricSpannerUtils;
import com.microsoft.kapp.utils.Formatter;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.views.CustomGlyphView;
import java.lang.ref.WeakReference;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public abstract class HomeTile extends BaseTile implements HomeTileStateChangedNotifier {
    protected int mBackgroundColor;
    protected RelativeLayout mContainer;
    protected String mEventDate;
    private CharSequence mEventMetric;
    protected String mEventName;
    protected CustomGlyphView mGlyphView;
    protected WeakReference<HomeTileStateChangedListener> mHomeTileStateChangedListenerWeakRef;
    protected int mOldContentThreshold;
    protected CustomGlyphView mPersonalBestState;
    protected View mTileDivider;
    protected TextView mTileEventDate;
    protected TextView mTileEventMetric;
    protected ViewGroup mTileEventMetricLayout;
    protected TextView mTileEventName;
    protected TileState mTileState;

    /* loaded from: classes.dex */
    public enum TileState {
        NOT_VIEWED,
        VIEWED
    }

    public HomeTile(Context context) {
        this(context, null);
    }

    public HomeTile(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.confirmationBarStyle);
    }

    public HomeTile(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mTileState = TileState.NOT_VIEWED;
        KApplicationGraph.getApplicationGraph().inject(this);
        View rootView = initializeViews(context);
        setAttributes(attrs);
        addView(rootView);
    }

    protected View initializeViews(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View rootView = inflater.inflate(R.layout.home_tile, (ViewGroup) this, false);
        this.mContainer = (RelativeLayout) ViewUtils.getValidView(rootView, R.id.home_tile_container, RelativeLayout.class);
        this.mGlyphView = (CustomGlyphView) ViewUtils.getValidView(rootView, R.id.home_tile_glyph, CustomGlyphView.class);
        this.mPersonalBestState = (CustomGlyphView) ViewUtils.getValidView(rootView, R.id.home_tile_personal_best_icon, CustomGlyphView.class);
        this.mTileEventDate = (TextView) ViewUtils.getValidView(rootView, R.id.home_tile_event_date, TextView.class);
        this.mTileEventName = (TextView) ViewUtils.getValidView(rootView, R.id.home_tile_event_name, TextView.class);
        this.mTileEventMetric = (TextView) ViewUtils.getValidView(rootView, R.id.home_tile_event_metric, TextView.class);
        this.mTileEventMetricLayout = (ViewGroup) ViewUtils.getValidView(rootView, R.id.home_tile_event_metric_layout, ViewGroup.class);
        this.mTileDivider = (View) ViewUtils.getValidView(rootView, R.id.home_tile_divider, View.class);
        setState(TileState.NOT_VIEWED);
        return rootView;
    }

    protected void setAttributes(AttributeSet attributes) {
        if (attributes != null) {
            TypedArray typedAttributes = getContext().obtainStyledAttributes(attributes, R.styleable.HomeTile);
            try {
                int glyphResourceId = typedAttributes.getResourceId(0, 0);
                if (glyphResourceId > 0) {
                    this.mGlyphView.setText(glyphResourceId);
                }
                this.mEventDate = typedAttributes.getString(2);
                this.mEventMetric = typedAttributes.getString(3);
                displayText();
            } finally {
                typedAttributes.recycle();
            }
        }
    }

    public void setTileData(TileState tileState, String tileDate, String tileName, CharSequence tileMetric) {
        boolean hasChanged = (this.mTileState == tileState && compareStrings(this.mEventDate, tileDate) && compareStrings(this.mEventName, tileName) && compareStrings(this.mEventMetric, tileMetric)) ? false : true;
        if (hasChanged) {
            setState(tileState);
            this.mEventDate = tileDate;
            this.mEventName = tileName;
            this.mEventMetric = tileMetric;
            displayText();
            invalidate();
        }
    }

    private boolean compareStrings(CharSequence first, CharSequence second) {
        if (first == null && second == null) {
            return true;
        }
        if ((first == null && second != null) || first == null || second == null) {
            return false;
        }
        return first.toString().equals(second.toString());
    }

    public void setTileName(String text) {
        this.mEventName = text;
        displayText();
    }

    protected Spannable getTileMetricWithSubtextUnits(String value, String units) {
        StringBuilder metric = new StringBuilder(value);
        int metricLength = metric.length();
        metric.append(MetricSpannerUtils.METRIC_UNITS_SEPARATOR_SPACE);
        metric.append(units);
        return Formatter.getSubtextSpannable(getContext(), metric.toString(), metricLength);
    }

    public void setPersonalBestStatus(boolean show) {
        this.mPersonalBestState.setVisibility(show ? 0 : 8);
    }

    public void setDividerVisibility(int visibility) {
        if (this.mTileDivider != null) {
            this.mTileDivider.setVisibility(visibility);
        }
    }

    public RelativeLayout getContainerView() {
        return this.mContainer;
    }

    private void displayText() {
        int i = 0;
        this.mTileEventDate.setText(this.mEventDate);
        this.mTileEventDate.setTextColor(this.mThemeManager.getTileTextColor(getState()));
        this.mTileEventDate.setVisibility((this.mEventDate == null || this.mEventDate.isEmpty()) ? 8 : 0);
        this.mTileEventName.setText(this.mEventName);
        this.mTileEventName.setTextColor(this.mThemeManager.getTileTextColor(getState()));
        this.mTileEventName.setVisibility((this.mEventName == null || this.mEventName.isEmpty()) ? 8 : 0);
        this.mTileEventMetric.setText(this.mEventMetric);
        ViewGroup viewGroup = this.mTileEventMetricLayout;
        if (this.mEventMetric == null || this.mEventMetric.length() <= 0) {
            i = 8;
        }
        viewGroup.setVisibility(i);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setGlyphResourceId(int id) {
        this.mGlyphView.setText(id);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setGlyphResourceContentDesc(String contentDesc) {
        this.mGlyphView.setContentDescription(contentDesc);
    }

    @Override // com.microsoft.kapp.widgets.BaseTile
    public int getBackgroundColor() {
        return this.mBackgroundColor;
    }

    public void setTilebackgroundColor(int color) {
        this.mBackgroundColor = color;
        this.mContainer.setBackgroundColor(this.mBackgroundColor);
    }

    protected void setTileBackgroundColorForState(TileState state) {
        setTilebackgroundColor(this.mThemeManager.getTileBackgroundColor(state));
    }

    public TileState getState() {
        return this.mTileState;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setState(TileState tileState) {
        this.mTileState = tileState;
        setTileBackgroundColorForState(tileState);
        this.mTileEventName.setTextColor(this.mThemeManager.getTileTextColor(tileState));
        this.mTileEventDate.setTextColor(this.mThemeManager.getTileTextColor(tileState));
    }

    public boolean isOldContent(DateTime runTime) {
        DateTime now = DateTime.now();
        return runTime.isBefore(now.minusDays(this.mOldContentThreshold));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setTileEventNameContentDesc(String contentDesc) {
        this.mTileEventName.setContentDescription(contentDesc);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setTileEventDateContentDesc(String contentDesc) {
        this.mTileEventDate.setContentDescription(contentDesc);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setTileEventMetricContentDesc(String contentDesc) {
        this.mTileEventMetric.setContentDescription(contentDesc);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setHomeTileContainerContentDesc(String contentDesc) {
        this.mContainer.setContentDescription(contentDesc);
    }

    public void setHistoryBackgroundColor() {
        setTileBackgroundColorForState(TileState.VIEWED);
    }

    @Override // com.microsoft.kapp.activities.HomeTileStateChangedNotifier
    public void setHomeTileStateChangedListenerWeakRef(HomeTileStateChangedListener listener) {
        this.mHomeTileStateChangedListenerWeakRef = new WeakReference<>(listener);
    }

    protected void dimText(boolean dimIt) {
        if (this.mGlyphView != null) {
            this.mGlyphView.setAlpha((float) (dimIt ? 0.6d : 1.0d));
        }
        if (this.mPersonalBestState != null) {
            this.mPersonalBestState.setAlpha((float) (dimIt ? 0.6d : 1.0d));
        }
        if (this.mTileEventDate != null) {
            this.mTileEventDate.setAlpha((float) (dimIt ? 0.6d : 1.0d));
        }
        if (this.mTileEventName != null) {
            this.mTileEventName.setAlpha((float) (dimIt ? 0.6d : 1.0d));
        }
        if (this.mTileEventMetric != null) {
            this.mTileEventMetric.setAlpha((float) (dimIt ? 0.6d : 1.0d));
        }
    }

    @Override // android.view.View
    @SuppressLint({"ClickableViewAccessibility"})
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case 0:
                dimText(true);
                break;
            case 1:
                dimText(false);
                performClick();
                break;
            case 3:
                dimText(false);
                break;
        }
        return true;
    }

    public boolean isMiniTile() {
        return false;
    }
}
