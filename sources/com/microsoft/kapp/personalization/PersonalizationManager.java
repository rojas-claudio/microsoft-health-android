package com.microsoft.kapp.personalization;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.util.Pair;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
/* loaded from: classes.dex */
public abstract class PersonalizationManager {
    private static final String APP_PACKAGE_NAME = "com.microsoft.kapp";
    public static final int HIGH_BITS = -65536;
    public static final int INVALID_WALLPAPER_OR_THEME_ID = -1;
    public static final int LOW_BITS = 65535;
    private static final int RESOURCE_NOT_FOUND = 0;
    protected List<Pair<Integer, String>> WALLPAPER_PATTERN_NAMES = new ArrayList();
    protected Resources mResources;
    protected Map<Integer, DeviceTheme> mThemes;
    private DeviceTheme[] mThemesArray;

    public abstract int getDefaultWallpaper();

    private int getColor(int colorResId) {
        return this.mResources.getColor(colorResId);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void addTheme(int themeId, String themeName, int baseColorResId, int highlightColorResId, int lowlightColorResId, int secondaryTextColorResId, int highContrastColorResId, int mutedColorResId) {
        Map<Integer, DeviceWallpaper> wallpapers = getWallpapersForTheme(themeId, themeName);
        DeviceWallpaper defaultWallpaper = wallpapers.get(Integer.valueOf(getDefaultWallpaper() & 65535));
        if (!wallpapers.isEmpty()) {
            DeviceTheme deviceTheme = new DeviceTheme(themeId, themeName, getColor(baseColorResId), getColor(highlightColorResId), getColor(lowlightColorResId), getColor(secondaryTextColorResId), getColor(highContrastColorResId), getColor(mutedColorResId), wallpapers, defaultWallpaper);
            this.mThemes.put(Integer.valueOf(themeId), deviceTheme);
        }
    }

    @SuppressLint({"UseSparseArrays"})
    private Map<Integer, DeviceWallpaper> getWallpapersForTheme(int themeId, String themeName) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (Pair<Integer, String> wallpaperPattern : this.WALLPAPER_PATTERN_NAMES) {
            int resId = this.mResources.getIdentifier(themeName + ((String) wallpaperPattern.second), "drawable", "com.microsoft.kapp");
            if (resId != 0) {
                int wallpaperId = ((Integer) wallpaperPattern.first).intValue() | themeId;
                linkedHashMap.put(wallpaperPattern.first, new DeviceWallpaper(wallpaperId, resId));
            }
        }
        return linkedHashMap;
    }

    public synchronized DeviceTheme[] getThemes() {
        DeviceTheme[] deviceThemeArr;
        if (this.mThemesArray != null) {
            deviceThemeArr = this.mThemesArray;
        } else {
            this.mThemesArray = new DeviceTheme[this.mThemes.size()];
            this.mThemes.values().toArray(this.mThemesArray);
            deviceThemeArr = this.mThemesArray;
        }
        return deviceThemeArr;
    }

    public DeviceTheme getThemeById(int themeId) {
        DeviceTheme theme = this.mThemes.get(Integer.valueOf(themeId & (-65536)));
        return theme != null ? theme : this.mThemes.get(Integer.valueOf(getDefaultWallpaper() & (-65536)));
    }

    public int getWallpaperFromId(int wallpaperPatternId) {
        DeviceTheme deviceTheme = getThemeById(wallpaperPatternId);
        DeviceWallpaper deviceWallpaper = deviceTheme.getWallpaperById(wallpaperPatternId);
        return deviceWallpaper.getResId();
    }
}
