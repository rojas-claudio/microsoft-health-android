package com.microsoft.band.tiles.pages;

import android.os.Parcel;
import android.os.Parcelable;
import com.microsoft.band.internal.util.Validation;
import com.microsoft.band.tiles.pages.PageElement;
import java.util.HashSet;
/* loaded from: classes.dex */
public abstract class PageElement<T extends PageElement<T>> implements Parcelable {
    int mColor;
    ElementColorSource mColorSource;
    private HorizontalAlignment mHorizontalAlignment;
    private int mId;
    private Margins mMargins;
    private PageRect mPageRect;
    private VerticalAlignment mVerticalAlignment;
    private boolean mVisible;

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract ElementType getType();

    /* JADX INFO: Access modifiers changed from: package-private */
    public PageElement(int originX, int originY, int width, int height) {
        this(new PageRect(originX, originY, width, height));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PageElement(PageRect bound) {
        this.mId = -1;
        this.mMargins = new Margins(0, 0, 0, 0);
        this.mColor = -16777216;
        this.mColorSource = ElementColorSource.CUSTOM;
        this.mHorizontalAlignment = HorizontalAlignment.LEFT;
        this.mVerticalAlignment = VerticalAlignment.TOP;
        this.mVisible = true;
        setBounds(bound);
    }

    public PageRect getRect() {
        return this.mPageRect;
    }

    public HorizontalAlignment getHorizontalAlignment() {
        return this.mHorizontalAlignment;
    }

    public int getId() {
        return this.mId;
    }

    public Margins getMargins() {
        return this.mMargins;
    }

    public VerticalAlignment getVerticalAlignment() {
        return this.mVerticalAlignment;
    }

    public boolean isVisible() {
        return this.mVisible;
    }

    public T setBounds(PageRect bounds) {
        Validation.notNull(bounds, "Bounds cannot be null");
        this.mPageRect = bounds;
        return this;
    }

    public T setBounds(int originX, int originY, int width, int height) {
        return setBounds(new PageRect(originX, originY, width, height));
    }

    public T setHorizontalAlignment(HorizontalAlignment horizontalAlignment) {
        Validation.notNull(horizontalAlignment, "Horizontal alignment cannot be null");
        this.mHorizontalAlignment = horizontalAlignment;
        return this;
    }

    public T setId(int id) {
        Validation.validateInRange("ID", id, 1, 65534);
        this.mId = id;
        return this;
    }

    public T setMargins(Margins margins) {
        if (margins != null) {
            this.mMargins = margins;
        }
        return this;
    }

    public T setMargins(int left, int top, int right, int bottom) {
        return setMargins(new Margins(left, top, right, bottom));
    }

    public T setVerticalAlignment(VerticalAlignment verticalAlignment) {
        Validation.notNull(verticalAlignment, "Vertical alignment cannot be null");
        this.mVerticalAlignment = verticalAlignment;
        return this;
    }

    public T setVisible(boolean visible) {
        this.mVisible = visible;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PageElement(Parcel in) {
        this.mId = -1;
        this.mMargins = new Margins(0, 0, 0, 0);
        this.mColor = -16777216;
        this.mColorSource = ElementColorSource.CUSTOM;
        this.mHorizontalAlignment = HorizontalAlignment.LEFT;
        this.mVerticalAlignment = VerticalAlignment.TOP;
        this.mVisible = true;
        setId(in.readInt());
        setBounds((PageRect) in.readValue(PageRect.class.getClassLoader()));
        setMargins((Margins) in.readValue(Margins.class.getClassLoader()));
        this.mColorSource = (ElementColorSource) in.readSerializable();
        this.mColor = in.readInt();
        setHorizontalAlignment((HorizontalAlignment) in.readSerializable());
        setVerticalAlignment((VerticalAlignment) in.readSerializable());
        setVisible(in.readByte() != 0);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getType().ordinal());
        dest.writeInt(this.mId);
        dest.writeValue(this.mPageRect);
        dest.writeValue(this.mMargins);
        dest.writeSerializable(this.mColorSource);
        dest.writeInt(this.mColor);
        dest.writeSerializable(this.mHorizontalAlignment);
        dest.writeSerializable(this.mVerticalAlignment);
        dest.writeByte((byte) (this.mVisible ? 1 : 0));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void getInUseIds(HashSet<Integer> inUseIds) {
        if (this.mId > 0 && !inUseIds.add(Integer.valueOf(this.mId))) {
            throw new IllegalArgumentException("Element with ID " + this.mId + " already exists.");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int setIdIfNotPresent(HashSet<Integer> inUseIds, int nextSequentialID) {
        if (this.mId < 0) {
            while (inUseIds.contains(Integer.valueOf(nextSequentialID))) {
                nextSequentialID++;
            }
            this.mId = nextSequentialID;
            inUseIds.add(Integer.valueOf(nextSequentialID));
            return nextSequentialID + 1;
        }
        return nextSequentialID;
    }
}
