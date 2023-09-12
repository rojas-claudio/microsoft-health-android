package com.shinobicontrols.charts;
/* loaded from: classes.dex */
public class MultiValueDataPoint<Tx, Tv> extends DataPoint<Tx, Tv> implements Data<Tx, Tv>, MultiValueData<Tv>, SelectableData {
    private final Tv a;
    private final Tv b;
    private final Tv c;
    private final Tv d;

    public MultiValueDataPoint(Tx x, Tv low, Tv high, Tv open, Tv close) {
        super(x, close);
        if (low == null || high == null || open == null || close == null) {
            throw new IllegalArgumentException("You must supply all DataPoint parameter arguments, non-null");
        }
        this.a = low;
        this.b = high;
        this.c = open;
        this.d = close;
    }

    public MultiValueDataPoint(Tx x, Tv low, Tv high, Tv open, Tv close, boolean selected) {
        super(x, close, selected);
        if (low == null || high == null || open == null || close == null) {
            throw new IllegalArgumentException("You must supply all DataPoint parameter arguments, non-null");
        }
        this.a = low;
        this.b = high;
        this.c = open;
        this.d = close;
    }

    public MultiValueDataPoint(Tx x, Tv low, Tv high) {
        super(x, high);
        if (low == null || high == null) {
            throw new IllegalArgumentException("You must supply all DataPoint parameter arguments, non-null");
        }
        this.a = low;
        this.b = high;
        this.c = null;
        this.d = null;
    }

    public MultiValueDataPoint(Tx x, Tv low, Tv high, boolean selected) {
        super(x, high, selected);
        if (low == null || high == null) {
            throw new IllegalArgumentException("You must supply all DataPoint parameter arguments, non-null");
        }
        this.a = low;
        this.b = high;
        this.c = null;
        this.d = null;
    }

    @Override // com.shinobicontrols.charts.MultiValueData
    public Tv getOpen() {
        return this.c;
    }

    @Override // com.shinobicontrols.charts.MultiValueData
    public Tv getHigh() {
        return this.b;
    }

    @Override // com.shinobicontrols.charts.MultiValueData
    public Tv getLow() {
        return this.a;
    }

    @Override // com.shinobicontrols.charts.MultiValueData
    public Tv getClose() {
        return this.d;
    }
}
