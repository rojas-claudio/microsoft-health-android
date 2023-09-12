package com.microsoft.band.tiles.pages;

import android.os.Parcel;
import android.os.Parcelable;
import com.microsoft.band.internal.util.Validation;
/* loaded from: classes.dex */
public final class PageRect implements Parcelable {
    public static final Parcelable.Creator<PageRect> CREATOR = new Parcelable.Creator<PageRect>() { // from class: com.microsoft.band.tiles.pages.PageRect.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PageRect createFromParcel(Parcel in) {
            return new PageRect(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PageRect[] newArray(int size) {
            return new PageRect[size];
        }
    };
    private int mHeight;
    private int mOriginX;
    private int mOriginY;
    private int mWidth;

    public PageRect(int originX, int originY, int width, int height) {
        setOriginX(originX);
        setOriginY(originY);
        setWidth(width);
        setHeight(height);
    }

    public int getOriginX() {
        return this.mOriginX;
    }

    public PageRect setOriginX(int originX) {
        Validation.validateNotNegative(originX, "X origin cannot be negative");
        this.mOriginX = originX;
        return this;
    }

    public int getOriginY() {
        return this.mOriginY;
    }

    public PageRect setOriginY(int originY) {
        Validation.validateNotNegative(originY, "Y origin cannot be negative");
        this.mOriginY = originY;
        return this;
    }

    public int getWidth() {
        return this.mWidth;
    }

    public PageRect setWidth(int width) {
        Validation.validateNotNegative(width, "Width cannot be negative");
        this.mWidth = width;
        return this;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public PageRect setHeight(int height) {
        Validation.validateNotNegative(height, "Height cannot be negative");
        this.mHeight = height;
        return this;
    }

    PageRect(Parcel in) {
        this.mOriginX = in.readInt();
        this.mOriginY = in.readInt();
        this.mWidth = in.readInt();
        this.mHeight = in.readInt();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mOriginX);
        dest.writeInt(this.mOriginY);
        dest.writeInt(this.mWidth);
        dest.writeInt(this.mHeight);
    }
}
