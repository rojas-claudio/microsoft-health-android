package com.microsoft.kapp.widgets;

import com.microsoft.kapp.widgets.HomeTile;
/* loaded from: classes.dex */
public class HomeTileData {
    private String mTileDate;
    private int mTileMetric;
    private String mTileName;
    private HomeTile.TileState mTileState;
    private int mTileUnit;

    public HomeTile.TileState getTileState() {
        return this.mTileState;
    }

    public void setTileState(HomeTile.TileState tileState) {
        this.mTileState = tileState;
    }

    public String getTileDate() {
        return this.mTileDate;
    }

    public void setTileDate(String tileDate) {
        this.mTileDate = tileDate;
    }

    public String getTileName() {
        return this.mTileName;
    }

    public void setTileName(String tileName) {
        this.mTileName = tileName;
    }

    public int getTileMetric() {
        return this.mTileMetric;
    }

    public void setTileMetric(int tileMetric) {
        this.mTileMetric = tileMetric;
    }

    public int getTileUnit() {
        return this.mTileUnit;
    }

    public void setTileUnit(int tileUnit) {
        this.mTileUnit = tileUnit;
    }
}
