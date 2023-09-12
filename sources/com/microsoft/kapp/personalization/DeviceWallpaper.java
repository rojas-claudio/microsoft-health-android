package com.microsoft.kapp.personalization;
/* loaded from: classes.dex */
public class DeviceWallpaper {
    private int mWallpaperId;
    private int mWallpaperResId;

    public DeviceWallpaper(int wallpaperId, int wallpaperResId) {
        this.mWallpaperId = wallpaperId;
        this.mWallpaperResId = wallpaperResId;
    }

    public int getId() {
        return this.mWallpaperId;
    }

    public int getResId() {
        return this.mWallpaperResId;
    }

    public boolean isPatternEqual(int wallpaperId) {
        return (this.mWallpaperId & 65535) == (65535 & wallpaperId);
    }
}
