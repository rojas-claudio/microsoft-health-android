package com.microsoft.kapp.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.activities.HomeTileStateChangedNotifier;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.views.CustomGlyphView;
import com.microsoft.kapp.widgets.HomeTile;
/* loaded from: classes.dex */
public class MiniHomeTile extends HomeTile implements HomeTileStateChangedNotifier {
    protected MiniTileRefreshListener mListener;
    protected String mNonSynchedText;
    protected String mSyncedText;
    protected String mTelemetryName;

    /* loaded from: classes.dex */
    public interface MiniTileRefreshListener {
        void onRefresh(MiniHomeTile miniHomeTile);
    }

    public MiniHomeTile(Context context) {
        super(context);
    }

    public MiniHomeTile(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.confirmationBarStyle);
    }

    public MiniHomeTile(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override // com.microsoft.kapp.widgets.HomeTile
    protected View initializeViews(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View rootView = inflater.inflate(R.layout.mini_home_tile, (ViewGroup) this, false);
        this.mContainer = (RelativeLayout) ViewUtils.getValidView(rootView, R.id.mini_home_tile_container, RelativeLayout.class);
        this.mGlyphView = (CustomGlyphView) ViewUtils.getValidView(rootView, R.id.mini_home_tile_glyph, CustomGlyphView.class);
        this.mTileEventName = (TextView) ViewUtils.getValidView(rootView, R.id.mini_home_tile_event_name, TextView.class);
        this.mTileDivider = (View) ViewUtils.getValidView(rootView, R.id.mini_home_tile_divider, View.class);
        setState(HomeTile.TileState.NOT_VIEWED);
        setGlyphResourceId(R.string.glyph_chevron);
        return rootView;
    }

    @Override // com.microsoft.kapp.widgets.HomeTile
    protected void setAttributes(AttributeSet attributes) {
        if (attributes != null) {
            TypedArray typedAttributes = getContext().obtainStyledAttributes(attributes, R.styleable.HomeTile);
            try {
                int glyphResourceId = typedAttributes.getResourceId(0, 0);
                if (glyphResourceId > 0) {
                    this.mGlyphView.setText(glyphResourceId);
                }
                displayText();
            } finally {
                typedAttributes.recycle();
            }
        }
    }

    public void setText(String text) {
        this.mEventName = text;
        displayText();
    }

    private void displayText() {
        this.mTileEventName.setText(this.mEventName);
        this.mTileEventName.setTextColor(this.mThemeManager.getTileTextColor(getState()));
        invalidate();
    }

    @Override // com.microsoft.kapp.widgets.HomeTile
    public void setState(HomeTile.TileState tileState) {
        this.mTileState = tileState;
        this.mBackgroundColor = this.mThemeManager.getTileBackgroundColor(tileState);
        this.mContainer.setBackgroundColor(this.mBackgroundColor);
        this.mTileEventName.setTextColor(this.mThemeManager.getTileTextColor(tileState));
    }

    @Override // com.microsoft.kapp.widgets.HomeTile
    public boolean isMiniTile() {
        return true;
    }

    @Override // com.microsoft.kapp.widgets.BaseTile
    public String getTelemetryName() {
        return this.mTelemetryName;
    }

    public void refresh() {
        if (this.mListener != null) {
            this.mListener.onRefresh(this);
        }
    }

    public void setRefreshListener(MiniTileRefreshListener listener) {
        this.mListener = listener;
    }

    public void setTelemetryString(String telemetryName) {
        this.mTelemetryName = telemetryName;
    }
}
