package com.shinobicontrols.charts;

import java.util.Collection;
/* loaded from: classes.dex */
public class SimpleDataAdapter<Tx, Ty> extends DataAdapter<Tx, Ty> {
    @Override // com.shinobicontrols.charts.DataAdapter
    public boolean add(Data<Tx, Ty> dataPoint) {
        boolean add = super.add(dataPoint);
        if (add) {
            fireUpdateHandler();
        }
        return add;
    }

    @Override // com.shinobicontrols.charts.DataAdapter
    public void add(int location, Data<Tx, Ty> dataPoint) {
        super.add(location, dataPoint);
        fireUpdateHandler();
    }

    @Override // com.shinobicontrols.charts.DataAdapter
    public boolean addAll(Collection<? extends Data<Tx, Ty>> dataPoints) {
        boolean addAll = super.addAll(dataPoints);
        if (addAll) {
            fireUpdateHandler();
        }
        return addAll;
    }

    @Override // com.shinobicontrols.charts.DataAdapter
    public boolean addAll(int location, Collection<? extends Data<Tx, Ty>> dataPoints) {
        boolean addAll = super.addAll(location, dataPoints);
        if (addAll) {
            fireUpdateHandler();
        }
        return addAll;
    }

    @Override // com.shinobicontrols.charts.DataAdapter
    public void clear() {
        int size = size();
        super.clear();
        if (size > 0) {
            fireUpdateHandler();
        }
    }

    @Override // com.shinobicontrols.charts.DataAdapter
    public Data<Tx, Ty> remove(int location) {
        Data<Tx, Ty> remove = super.remove(location);
        fireUpdateHandler();
        return remove;
    }

    @Override // com.shinobicontrols.charts.DataAdapter
    public boolean remove(Object object) {
        boolean remove = super.remove(object);
        if (remove) {
            fireUpdateHandler();
        }
        return remove;
    }

    @Override // com.shinobicontrols.charts.DataAdapter
    public boolean removeAll(Collection<?> collection) {
        boolean removeAll = super.removeAll(collection);
        if (removeAll) {
            fireUpdateHandler();
        }
        return removeAll;
    }

    @Override // com.shinobicontrols.charts.DataAdapter
    public boolean retainAll(Collection<?> collection) {
        boolean retainAll = super.retainAll(collection);
        if (retainAll) {
            fireUpdateHandler();
        }
        return retainAll;
    }

    @Override // com.shinobicontrols.charts.DataAdapter
    public Data<Tx, Ty> set(int location, Data<Tx, Ty> dataPoint) {
        Data<Tx, Ty> data = super.set(location, dataPoint);
        fireUpdateHandler();
        return data;
    }
}
