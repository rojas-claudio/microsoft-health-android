package com.microsoft.kapp;

import android.content.Context;
import com.microsoft.kapp.widgets.HomeTile;
/* loaded from: classes.dex */
public class DefaultThemeManager implements ThemeManager {
    private final Context mContext;

    public DefaultThemeManager(Context context) {
        this.mContext = context;
    }

    @Override // com.microsoft.kapp.ThemeManager
    public int getTileTextColor(HomeTile.TileState tileState) {
        switch (tileState) {
            case NOT_VIEWED:
                int color = this.mContext.getResources().getColor(R.color.tile_text_not_viewed);
                return color;
            case VIEWED:
                int color2 = this.mContext.getResources().getColor(R.color.tile_text_viewed);
                return color2;
            default:
                int color3 = this.mContext.getResources().getColor(R.color.tile_text_not_viewed);
                return color3;
        }
    }

    @Override // com.microsoft.kapp.ThemeManager
    public int getTileBackgroundColor(HomeTile.TileState tileState) {
        switch (tileState) {
            case NOT_VIEWED:
                int color = this.mContext.getResources().getColor(R.color.tile_not_viewed);
                return color;
            case VIEWED:
                int color2 = this.mContext.getResources().getColor(R.color.tile_viewed);
                return color2;
            default:
                int color3 = this.mContext.getResources().getColor(R.color.tile_not_viewed);
                return color3;
        }
    }
}
