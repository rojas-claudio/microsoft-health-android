package com.microsoft.kapp.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.microsoft.kapp.R;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.widgets.HomeTile;
/* loaded from: classes.dex */
public class ManageStrappsTile extends HomeTile {
    private RelativeLayout mContainer;

    public ManageStrappsTile(Context context) {
        this(context, null);
    }

    public ManageStrappsTile(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.confirmationBarStyle);
    }

    public ManageStrappsTile(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        View rootView = initializeViews(context);
        addView(rootView);
    }

    @Override // com.microsoft.kapp.widgets.HomeTile
    protected View initializeViews(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View rootView = inflater.inflate(R.layout.manage_strapps_tile, (ViewGroup) this, false);
        this.mContainer = (RelativeLayout) ViewUtils.getValidView(rootView, R.id.manage_strapps_tile_container, RelativeLayout.class);
        this.mContainer.setBackgroundColor(this.mThemeManager.getTileBackgroundColor(HomeTile.TileState.VIEWED));
        return rootView;
    }

    @Override // com.microsoft.kapp.widgets.BaseTile
    public String getTelemetryName() {
        return TelemetryConstants.Events.LogHomeTileTap.Dimensions.TILE_NAME_MANAGE_TILES;
    }
}
