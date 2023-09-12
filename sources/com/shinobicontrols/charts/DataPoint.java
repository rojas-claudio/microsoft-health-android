package com.shinobicontrols.charts;
/* loaded from: classes.dex */
public class DataPoint<Tx, Ty> implements Data<Tx, Ty>, SelectableData {
    private final Tx a;
    private final Ty b;
    private boolean c;

    public DataPoint(Tx x, Ty y) {
        if (x == null || y == null) {
            throw new IllegalArgumentException("You must supply all DataPoint parameter arguments, non-null");
        }
        this.a = x;
        this.b = y;
    }

    public DataPoint(Tx x, Ty y, boolean selected) {
        if (x == null || y == null) {
            throw new IllegalArgumentException("You must supply all DataPoint parameter arguments, non-null");
        }
        this.a = x;
        this.b = y;
        this.c = selected;
    }

    @Override // com.shinobicontrols.charts.Data
    public Tx getX() {
        return this.a;
    }

    @Override // com.shinobicontrols.charts.Data
    public Ty getY() {
        return this.b;
    }

    @Override // com.shinobicontrols.charts.SelectableData
    public boolean getSelected() {
        return this.c;
    }
}
