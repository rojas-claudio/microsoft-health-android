package com.microsoft.band.tiles.pages;

import android.os.Parcel;
import com.microsoft.band.internal.util.Validation;
import com.microsoft.band.tiles.pages.PagePanel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
/* loaded from: classes.dex */
public abstract class PagePanel<T extends PagePanel<T>> extends PageElement<T> {
    List<PageElement> mElements;

    /* JADX INFO: Access modifiers changed from: package-private */
    public PagePanel(int originX, int originY, int width, int height) {
        super(originX, originY, width, height);
        this.mElements = new ArrayList();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PagePanel(PageRect bound) {
        super(bound);
        this.mElements = new ArrayList();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PagePanel(int originX, int originY, int width, int height, PageElement... elements) {
        super(originX, originY, width, height);
        this.mElements = new ArrayList();
        addElements(elements);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PagePanel(PageRect bound, PageElement... elements) {
        super(bound);
        this.mElements = new ArrayList();
        addElements(elements);
    }

    public List<PageElement> getElements() {
        return this.mElements;
    }

    public T addElements(PageElement... elements) {
        Validation.notNull(elements, "Must add Elements");
        for (PageElement element : elements) {
            Validation.notNull(element, "Elements cannot be null");
            getElements().add(element);
        }
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PagePanel(Parcel in) {
        super(in);
        this.mElements = new ArrayList();
        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            this.mElements.add(ElementCreator.createFromParcel(in));
        }
    }

    @Override // com.microsoft.band.tiles.pages.PageElement, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.mElements.size());
        for (PageElement element : this.mElements) {
            element.writeToParcel(dest, flags);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void validateDuplicateIdAndObject(HashSet<Integer> inUseIds, HashSet<PageElement> elementSet) {
        super.getInUseIds(inUseIds);
        if (!elementSet.add(this)) {
            throw new IllegalArgumentException("The panels in the layout form a loop. This configuration is not supported.");
        }
        for (PageElement element : this.mElements) {
            if (element instanceof PagePanel) {
                ((PagePanel) element).validateDuplicateIdAndObject(inUseIds, elementSet);
            } else {
                element.getInUseIds(inUseIds);
                if (!elementSet.add(element)) {
                    throw new IllegalArgumentException("Duplicate page element is not supported.");
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.microsoft.band.tiles.pages.PageElement
    public int setIdIfNotPresent(HashSet<Integer> inUseIds, int nextSequentialID) {
        int nextSequentialID2 = super.setIdIfNotPresent(inUseIds, nextSequentialID);
        for (PageElement element : this.mElements) {
            nextSequentialID2 = element.setIdIfNotPresent(inUseIds, nextSequentialID2);
        }
        return nextSequentialID2;
    }
}
