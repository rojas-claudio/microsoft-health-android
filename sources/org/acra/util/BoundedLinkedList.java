package org.acra.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
/* loaded from: classes.dex */
public class BoundedLinkedList<E> extends LinkedList<E> {
    private final int maxSize;

    public BoundedLinkedList(int maxSize) {
        this.maxSize = maxSize;
    }

    @Override // java.util.LinkedList, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List, java.util.Deque, java.util.Queue
    public boolean add(E object) {
        if (size() == this.maxSize) {
            removeFirst();
        }
        return super.add(object);
    }

    @Override // java.util.LinkedList, java.util.AbstractSequentialList, java.util.AbstractList, java.util.List
    public void add(int location, E object) {
        if (size() == this.maxSize) {
            removeFirst();
        }
        super.add(location, object);
    }

    @Override // java.util.LinkedList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean addAll(Collection<? extends E> collection) {
        int totalNeededSize = size() + collection.size();
        int overhead = totalNeededSize - this.maxSize;
        if (overhead > 0) {
            removeRange(0, overhead);
        }
        return super.addAll(collection);
    }

    @Override // java.util.LinkedList, java.util.AbstractSequentialList, java.util.AbstractList, java.util.List
    public boolean addAll(int location, Collection<? extends E> collection) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.LinkedList, java.util.Deque
    public void addFirst(E object) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.LinkedList, java.util.Deque
    public void addLast(E object) {
        add(object);
    }

    @Override // java.util.AbstractCollection
    public String toString() {
        StringBuilder result = new StringBuilder();
        Iterator i$ = iterator();
        while (i$.hasNext()) {
            result.append(i$.next().toString());
        }
        return result.toString();
    }
}
