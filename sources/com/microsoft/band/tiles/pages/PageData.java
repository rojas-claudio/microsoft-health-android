package com.microsoft.band.tiles.pages;

import android.os.Parcel;
import android.os.Parcelable;
import com.microsoft.band.internal.util.Validation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
/* loaded from: classes.dex */
public final class PageData implements Parcelable {
    public static final Parcelable.Creator<PageData> CREATOR = new Parcelable.Creator<PageData>() { // from class: com.microsoft.band.tiles.pages.PageData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PageData createFromParcel(Parcel source) {
            return new PageData(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PageData[] newArray(int size) {
            return new PageData[size];
        }
    };
    private Set<Integer> mIds;
    private UUID mPageId;
    private int mPageLayoutIndex;
    private List<PageElementData> mValues;

    public PageData(UUID pageId, int layoutId) {
        Validation.notNull(pageId, "Page ID cannot be null");
        Validation.validateInRange("Layout id", layoutId, 0, 4);
        this.mPageId = pageId;
        this.mPageLayoutIndex = layoutId;
        this.mValues = new ArrayList();
        this.mIds = new HashSet();
    }

    public PageData update(PageElementData el) {
        Validation.notNull(el, "Element data cannot be null");
        int id = el.getId();
        if (this.mIds.contains(Integer.valueOf(id))) {
            throw new IllegalArgumentException(String.format("PageElement with id = %d already defined", Integer.valueOf(id)));
        }
        this.mIds.add(Integer.valueOf(id));
        this.mValues.add(el);
        return this;
    }

    private PageData(Parcel source) {
        this.mPageId = (UUID) source.readSerializable();
        this.mPageLayoutIndex = source.readInt();
        this.mValues = new ArrayList();
        source.readList(this.mValues, PageElementData.class.getClassLoader());
        this.mIds = new HashSet();
        for (PageElementData data : this.mValues) {
            this.mIds.add(Integer.valueOf(data.getId()));
        }
    }

    public UUID getPageId() {
        return this.mPageId;
    }

    public int getPageLayoutIndex() {
        return this.mPageLayoutIndex;
    }

    public List<PageElementData> getValues() {
        return Collections.unmodifiableList(this.mValues);
    }

    public void validate(int hardwareVersion) {
        for (PageElementData data : this.mValues) {
            data.validate(hardwareVersion);
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.mPageId);
        dest.writeInt(this.mPageLayoutIndex);
        dest.writeList(this.mValues);
    }
}
