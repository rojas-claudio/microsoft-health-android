package com.shinobicontrols.charts;

import java.util.ArrayList;
import java.util.Collection;
/* loaded from: classes.dex */
class bo<E> extends ArrayList<E> {
    private static final long serialVersionUID = 8109538979144194735L;

    @Override // java.util.ArrayList, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean add(E object) {
        a(object == null);
        return super.add(object);
    }

    @Override // java.util.ArrayList, java.util.AbstractList, java.util.List
    public void add(int index, E object) {
        a(object == null);
        super.add(index, object);
    }

    @Override // java.util.ArrayList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean addAll(Collection<? extends E> collection) {
        a(collection.contains(null));
        return super.addAll(collection);
    }

    @Override // java.util.ArrayList, java.util.AbstractList, java.util.List
    public boolean addAll(int index, Collection<? extends E> collection) {
        a(collection.contains(null));
        return super.addAll(index, collection);
    }

    @Override // java.util.ArrayList, java.util.AbstractList, java.util.List
    public E set(int index, E object) {
        a(object == null);
        return (E) super.set(index, object);
    }

    private void a(boolean z) {
        if (z) {
            throw new NullPointerException("Cannot add null elements.");
        }
    }
}
