package com.microsoft.band.tiles.pages;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes.dex */
public final class Margins implements Parcelable {
    public static final Parcelable.Creator<Margins> CREATOR = new Parcelable.Creator<Margins>() { // from class: com.microsoft.band.tiles.pages.Margins.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Margins createFromParcel(Parcel in) {
            return new Margins(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Margins[] newArray(int size) {
            return new Margins[size];
        }
    };
    private int mBottom;
    private int mLeft;
    private int mRight;
    private int mTop;

    public Margins(int left, int top, int right, int bottom) {
        setLeft((short) left);
        setTop((short) top);
        setRight((short) right);
        setBottom((short) bottom);
    }

    public int getLeft() {
        return this.mLeft;
    }

    public Margins setLeft(int left) {
        this.mLeft = left;
        return this;
    }

    public int getTop() {
        return this.mTop;
    }

    public Margins setTop(int top) {
        this.mTop = top;
        return this;
    }

    public int getRight() {
        return this.mRight;
    }

    public Margins setRight(int right) {
        this.mRight = right;
        return this;
    }

    public int getBottom() {
        return this.mBottom;
    }

    public Margins setBottom(int bottom) {
        this.mBottom = bottom;
        return this;
    }

    Margins(Parcel in) {
        this.mLeft = (short) in.readInt();
        this.mTop = (short) in.readInt();
        this.mRight = (short) in.readInt();
        this.mBottom = (short) in.readInt();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mLeft);
        dest.writeInt(this.mTop);
        dest.writeInt(this.mRight);
        dest.writeInt(this.mBottom);
    }
}
