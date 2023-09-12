package com.shinobicontrols.charts;

import com.shinobicontrols.charts.di;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
/* loaded from: classes.dex */
public abstract class DataAdapter<Tx, Ty> implements Iterable<Data<Tx, Ty>> {
    private final al a = new al();
    private final List<Data<Tx, Ty>> b = new bo();

    public boolean add(Data<Tx, Ty> dataPoint) {
        return this.b.add(dataPoint);
    }

    public void add(int location, Data<Tx, Ty> dataPoint) {
        this.b.add(location, dataPoint);
    }

    public boolean addAll(Collection<? extends Data<Tx, Ty>> dataPoints) {
        return this.b.addAll(dataPoints);
    }

    public boolean addAll(int location, Collection<? extends Data<Tx, Ty>> dataPoints) {
        return this.b.addAll(location, dataPoints);
    }

    public void clear() {
        this.b.clear();
    }

    public boolean contains(Object object) {
        return this.b.contains(object);
    }

    public boolean containsAll(Collection<?> collection) {
        return this.b.containsAll(collection);
    }

    public Data<Tx, Ty> get(int location) {
        return this.b.get(location);
    }

    public int hashCode() {
        return this.b.hashCode();
    }

    public int indexOf(Object object) {
        return this.b.indexOf(object);
    }

    public boolean isEmpty() {
        return this.b.isEmpty();
    }

    @Override // java.lang.Iterable
    public Iterator<Data<Tx, Ty>> iterator() {
        return this.b.iterator();
    }

    public int lastIndexOf(Object object) {
        return this.b.lastIndexOf(object);
    }

    public Data<Tx, Ty> remove(int location) {
        return this.b.remove(location);
    }

    public boolean remove(Object object) {
        return this.b.remove(object);
    }

    public boolean removeAll(Collection<?> collection) {
        return this.b.removeAll(collection);
    }

    public boolean retainAll(Collection<?> collection) {
        return this.b.retainAll(collection);
    }

    public Data<Tx, Ty> set(int location, Data<Tx, Ty> dataPoint) {
        return this.b.set(location, dataPoint);
    }

    public int size() {
        return this.b.size();
    }

    public Object[] toArray() {
        return this.b.toArray();
    }

    public List<Data<Tx, Ty>> getDataPointsForDisplay() {
        return this.b;
    }

    public <T> T[] toArray(T[] array) {
        return (T[]) this.b.toArray(array);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void fireUpdateHandler() {
        this.a.a(new di());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final am a(di.a aVar) {
        return this.a.a(di.a, aVar);
    }
}
