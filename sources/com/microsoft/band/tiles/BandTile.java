package com.microsoft.band.tiles;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import com.microsoft.band.BandTheme;
import com.microsoft.band.internal.util.IconUtils;
import com.microsoft.band.internal.util.Validation;
import com.microsoft.band.tiles.pages.PageLayout;
import com.microsoft.band.tiles.pages.PagePanel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
/* loaded from: classes.dex */
public final class BandTile implements Parcelable {
    public static final Parcelable.Creator<BandTile> CREATOR = new Parcelable.Creator<BandTile>() { // from class: com.microsoft.band.tiles.BandTile.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public BandTile createFromParcel(Parcel in) {
            return new BandTile(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public BandTile[] newArray(int size) {
            return new BandTile[size];
        }
    };
    private boolean mIsBadgingEnabled;
    private boolean mIsScreenTimeoutDisabled;
    private String mName;
    private List<BandIcon> mPageIcons;
    private List<PageLayout> mPageLayouts;
    private BandIcon mSmallIcon;
    private BandTheme mTheme;
    private BandIcon mTileIcon;
    private final UUID mTileId;

    /* loaded from: classes.dex */
    public static class Builder {
        private BandTile mTile;

        public Builder(UUID id, String name, Bitmap icon) {
            this(id, name, new BandIcon(icon));
        }

        public Builder(UUID id, String name, BandIcon icon) {
            Validation.notNull(id, "Tile ID cannot be null");
            this.mTile = new BandTile(id);
            setTileName(name);
            setTileIcon(icon);
        }

        private void setTileName(String name) {
            Validation.notNull(name, "Tile name cannot be null");
            Validation.validateStringEmptyOrWhiteSpace(name, "Tile name");
            Validation.lengthLessOrEq(name, 21, "Tile name");
            this.mTile.mName = name;
        }

        private void setTileIcon(BandIcon icon) {
            Validation.notNull(icon, "Icon cannot be null");
            IconUtils.bitmapToByteArray(icon.getIcon());
            this.mTile.mTileIcon = icon;
        }

        public Builder setTileSmallIcon(BandIcon icon) {
            return setTileSmallIcon(icon, true);
        }

        public Builder setTileSmallIcon(BandIcon icon, boolean badgingEnabled) {
            Validation.notNull(icon, "Icon cannot be null");
            return setTileSmallIcon(icon.getIcon(), badgingEnabled);
        }

        public Builder setTileSmallIcon(Bitmap icon) {
            return setTileSmallIcon(icon, true);
        }

        public Builder setTileSmallIcon(Bitmap icon, boolean badgingEnabled) {
            IconUtils.bitmapToByteArray(icon);
            this.mTile.mSmallIcon = new BandIcon(icon);
            return setBadgingEnabled(badgingEnabled);
        }

        public Builder setTheme(BandTheme theme) {
            Validation.notNull(theme, "Theme cannot be null");
            this.mTile.mTheme = theme;
            return this;
        }

        public Builder setPageLayouts(PageLayout... pageLayouts) {
            Validation.notNull(pageLayouts, "Must add Layouts");
            Validation.validateInRange("The number of layouts", pageLayouts.length, 0, 5);
            this.mTile.mPageLayouts.clear();
            for (PageLayout page : pageLayouts) {
                addPageLayout(page);
            }
            return this;
        }

        public Builder addPageLayout(PagePanel pagePanel) {
            Validation.notNull(pagePanel, "Panel cannot be null");
            return addPageLayout(new PageLayout(pagePanel));
        }

        public Builder addPageLayout(PageLayout pageLayout) {
            Validation.notNull(pageLayout, "Layout cannot be null");
            if (this.mTile.getPageLayouts().size() == 5) {
                throw new IllegalArgumentException(String.format("BandTile cannot contain more than %d page layouts.", 5));
            }
            pageLayout.validateLayout();
            this.mTile.mPageLayouts.add(pageLayout);
            return this;
        }

        public Builder setPageIcons(BandIcon... pageIcons) {
            Validation.notNull(pageIcons, "Must add Icons");
            this.mTile.mPageIcons.clear();
            for (BandIcon icon : pageIcons) {
                Validation.notNull(icon, "Icons cannot be null");
                IconUtils.bitmapToByteArray(icon.getIcon());
                this.mTile.mPageIcons.add(icon);
            }
            return this;
        }

        public Builder setPageIcons(Bitmap... pageIconBitmaps) {
            Validation.notNull(pageIconBitmaps, "Must add icon bitmaps");
            this.mTile.mPageIcons.clear();
            for (Bitmap icon : pageIconBitmaps) {
                Validation.notNull(icon, "Icon bitmaps cannot be null");
                IconUtils.bitmapToByteArray(icon);
                this.mTile.mPageIcons.add(new BandIcon(icon));
            }
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder setBadgingEnabled(boolean badgingEnabled) {
            this.mTile.mIsBadgingEnabled = badgingEnabled;
            return this;
        }

        public Builder setScreenTimeoutDisabled(boolean screenTimeoutDisabled) {
            this.mTile.mIsScreenTimeoutDisabled = screenTimeoutDisabled;
            return this;
        }

        public BandTile build() {
            BandTile tile = this.mTile;
            this.mTile = null;
            return tile;
        }
    }

    private BandTile(UUID id) {
        this.mIsBadgingEnabled = false;
        this.mIsScreenTimeoutDisabled = false;
        this.mTileId = id;
        this.mPageLayouts = new ArrayList();
        this.mPageIcons = new ArrayList();
    }

    public UUID getTileId() {
        return this.mTileId;
    }

    public String getTileName() {
        return this.mName;
    }

    public BandIcon getTileIcon() {
        return this.mTileIcon;
    }

    public BandIcon getTileSmallIcon() {
        return this.mSmallIcon;
    }

    public boolean isBadgingEnabled() {
        return this.mIsBadgingEnabled;
    }

    public boolean isScreenTimeoutDisabled() {
        return this.mIsScreenTimeoutDisabled;
    }

    public BandTheme getTheme() {
        return this.mTheme;
    }

    public List<PageLayout> getPageLayouts() {
        return Collections.unmodifiableList(this.mPageLayouts);
    }

    public List<BandIcon> getPageIcons() {
        return Collections.unmodifiableList(this.mPageIcons);
    }

    BandTile(Parcel in) {
        this.mIsBadgingEnabled = false;
        this.mIsScreenTimeoutDisabled = false;
        this.mTileId = (UUID) in.readValue(UUID.class.getClassLoader());
        this.mName = in.readString();
        this.mTileIcon = (BandIcon) in.readValue(BandIcon.class.getClassLoader());
        this.mSmallIcon = (BandIcon) in.readValue(BandIcon.class.getClassLoader());
        this.mTheme = (BandTheme) in.readValue(BandTheme.class.getClassLoader());
        this.mPageLayouts = new ArrayList();
        in.readList(this.mPageLayouts, PageLayout.class.getClassLoader());
        this.mPageIcons = new ArrayList();
        in.readList(this.mPageIcons, BandIcon.class.getClassLoader());
        byte mask = in.readByte();
        this.mIsBadgingEnabled = (mask & 1) == 1;
        this.mIsScreenTimeoutDisabled = (mask & 2) == 2;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.mTileId);
        dest.writeString(this.mName);
        dest.writeValue(this.mTileIcon);
        dest.writeValue(this.mSmallIcon);
        dest.writeValue(this.mTheme);
        dest.writeList(this.mPageLayouts);
        dest.writeList(this.mPageIcons);
        byte mask = (byte) (this.mIsBadgingEnabled ? 1 : 0);
        dest.writeByte((byte) (mask | (this.mIsScreenTimeoutDisabled ? (byte) 2 : (byte) 0)));
    }
}
