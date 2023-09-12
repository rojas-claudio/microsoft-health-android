package com.microsoft.band.tiles.pages;

import android.os.Parcel;
import android.os.Parcelable;
import com.microsoft.band.internal.util.Validation;
import java.util.HashSet;
/* loaded from: classes.dex */
public final class PageLayout implements Parcelable {
    public static final Parcelable.Creator<PageLayout> CREATOR = new Parcelable.Creator<PageLayout>() { // from class: com.microsoft.band.tiles.pages.PageLayout.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PageLayout createFromParcel(Parcel in) {
            return new PageLayout(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PageLayout[] newArray(int size) {
            return new PageLayout[size];
        }
    };
    private PagePanel mRoot;

    public PageLayout(PagePanel root) {
        setRoot(root);
    }

    public PagePanel getRoot() {
        return this.mRoot;
    }

    public PageLayout setRoot(PagePanel root) {
        Validation.notNull(root, "Root panel cannot be null");
        this.mRoot = root;
        return this;
    }

    public void validateLayout() {
        HashSet<Integer> mIdHashSet = new HashSet<>();
        HashSet<PageElement> mPagePanelSet = new HashSet<>();
        this.mRoot.validateDuplicateIdAndObject(mIdHashSet, mPagePanelSet);
        int nextSequentialID = 1;
        while (mIdHashSet.contains(Integer.valueOf(nextSequentialID))) {
            nextSequentialID++;
        }
        this.mRoot.setIdIfNotPresent(mIdHashSet, nextSequentialID);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        this.mRoot.writeToParcel(dest, flags);
    }

    PageLayout(Parcel in) {
        this.mRoot = (PagePanel) ElementCreator.createFromParcel(in);
    }
}
