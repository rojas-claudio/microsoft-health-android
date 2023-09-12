package com.microsoft.kapp.personalization;

import com.microsoft.band.device.StrappColorPalette;
import java.util.Map;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class DeviceTheme {
    private final int mBaseColor;
    private DeviceWallpaper mDefaultWallpaper;
    private final int mHighContrastColor;
    private final int mHighlightColor;
    private final int mLowlightColor;
    private final int mMutedColor;
    @Inject
    PersonalizationManagerFactory mPersonalizationManagerFactory;
    private final int mSecondaryTextColor;
    private final int mThemeId;
    private final String mThemeName;
    private DeviceWallpaper[] mWallpaperArray;
    private final Map<Integer, DeviceWallpaper> mWallpapers;

    public DeviceTheme(int themeId, String themeName, int baseColor, int highlightColor, int lowlightColor, int secondaryTextColor, int highContrastColor, int mutedColor, Map<Integer, DeviceWallpaper> wallpapers, DeviceWallpaper defaultWallpaper) {
        this.mThemeId = themeId;
        this.mThemeName = themeName;
        this.mBaseColor = baseColor;
        this.mHighlightColor = highlightColor;
        this.mLowlightColor = lowlightColor;
        this.mSecondaryTextColor = secondaryTextColor;
        this.mHighContrastColor = highContrastColor;
        this.mMutedColor = mutedColor;
        this.mWallpapers = wallpapers;
        this.mDefaultWallpaper = defaultWallpaper;
    }

    public StrappColorPalette getColorPalette() {
        return new StrappColorPalette(this.mBaseColor, this.mHighlightColor, this.mLowlightColor, this.mSecondaryTextColor, this.mHighContrastColor, this.mMutedColor);
    }

    public int getThemeId() {
        return this.mThemeId;
    }

    public String getThemeName() {
        return this.mThemeName;
    }

    public int getBase() {
        return this.mBaseColor;
    }

    public int getHighlight() {
        return this.mHighlightColor;
    }

    public int getLowlight() {
        return this.mLowlightColor;
    }

    public int getSecondaryText() {
        return this.mSecondaryTextColor;
    }

    public int getHighContrast() {
        return this.mHighContrastColor;
    }

    public int getMuted() {
        return this.mMutedColor;
    }

    public synchronized DeviceWallpaper[] getWallpapers() {
        DeviceWallpaper[] deviceWallpaperArr;
        if (this.mWallpaperArray != null) {
            deviceWallpaperArr = this.mWallpaperArray;
        } else {
            this.mWallpaperArray = new DeviceWallpaper[this.mWallpapers.size()];
            this.mWallpapers.values().toArray(this.mWallpaperArray);
            deviceWallpaperArr = this.mWallpaperArray;
        }
        return deviceWallpaperArr;
    }

    public DeviceWallpaper getWallpaperById(int wallpaperId) {
        DeviceWallpaper wallpaper = this.mWallpapers.get(Integer.valueOf(65535 & wallpaperId));
        return wallpaper != null ? wallpaper : this.mDefaultWallpaper;
    }
}
