package com.microsoft.band.tiles;

import android.graphics.Bitmap;
import com.microsoft.band.BandTheme;
import com.microsoft.band.client.CargoException;
import com.microsoft.band.device.CargoStrapp;
import com.microsoft.band.device.StrappLayout;
import com.microsoft.band.tiles.BandTile;
import com.microsoft.band.tiles.pages.LayoutTemplate;
import com.microsoft.band.tiles.pages.PageLayout;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public final class TileUtils {
    private TileUtils() {
        throw new UnsupportedOperationException();
    }

    public static CargoStrapp tileToStrapp(BandTile tile, byte[] appId) throws CargoException {
        List<Bitmap> images = new ArrayList<>();
        short tileIconIndex = 0;
        short tileSmallIconIndex = 0;
        if (tile.getTileIcon() != null) {
            images.add(tile.getTileIcon().getIcon());
            tileIconIndex = (short) (images.size() - 1);
        }
        if (tile.getTileSmallIcon() != null) {
            images.add(tile.getTileSmallIcon().getIcon());
            tileSmallIconIndex = (short) (images.size() - 1);
        } else {
            images.add(tile.getTileIcon().getIcon());
        }
        if (tile.getPageIcons() != null) {
            for (BandIcon icon : tile.getPageIcons()) {
                images.add(icon.getIcon());
            }
        }
        int mask = 0;
        if (tile.isBadgingEnabled()) {
            mask = 0 | 2;
        }
        if (tile.isScreenTimeoutDisabled()) {
            mask |= 32;
        }
        List<StrappLayout> layouts = new ArrayList<>();
        for (int i = 0; i < tile.getPageLayouts().size(); i++) {
            LayoutTemplate template = new LayoutTemplate(tile.getPageLayouts().get(i));
            layouts.add(new StrappLayout(template.toBytes()));
        }
        return new CargoStrapp(tile.getTileId(), tile.getTileName(), mask, tile.getTheme() != null ? tile.getTheme().getBaseColor() : 0L, images, tileIconIndex, layouts, tileSmallIconIndex, appId);
    }

    public static BandTile strappToTile(CargoStrapp strapp, BandTheme theme, List<PageLayout> layouts) {
        Bitmap icon = strapp.getTileImage();
        BandTile.Builder tileBuilder = new BandTile.Builder(strapp.getId(), strapp.getName(), new BandIcon(Bitmap.createScaledBitmap(icon, 46, 46, false)));
        tileBuilder.setBadgingEnabled(strapp.isBadgingEnabled());
        tileBuilder.setScreenTimeoutDisabled(strapp.isScreenTimeoutDisabled());
        tileBuilder.setPageLayouts((PageLayout[]) layouts.toArray(new PageLayout[layouts.size()]));
        return tileBuilder.build();
    }
}
