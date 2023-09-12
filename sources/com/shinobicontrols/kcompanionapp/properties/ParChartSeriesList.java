package com.shinobicontrols.kcompanionapp.properties;

import com.microsoft.kapp.R;
import com.microsoft.kapp.utils.Constants;
import com.shinobicontrols.charts.DataAdapter;
import com.shinobicontrols.charts.DataPoint;
import com.shinobicontrols.charts.SimpleDataAdapter;
import com.shinobicontrols.kcompanionapp.charts.internal.TypedValueGetter;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class ParChartSeriesList extends ArrayList<DataAdapter<Double, Double>> {
    private static final int INDEX_ABOVE_BASELINE = 1;
    private static final int INDEX_ALBATROSS = 3;
    private static final int INDEX_BASELINE = 0;
    private static final int INDEX_BELOW_BASELINE = 2;
    private static final int INDEX_BIRDIE = 5;
    private static final int INDEX_BOGEY = 7;
    private static final int INDEX_DOUBLE_BOGEY = 8;
    private static final int INDEX_EAGLE = 4;
    private static final int INDEX_PAR = 6;
    private static final int INDEX_SKIPPED = 10;
    private static final int INDEX_TRIPLE_BOGEY = 9;
    private static final int SCORE_ALBATROSS = -3;
    private static final int SCORE_BIRDIE = -1;
    private static final int SCORE_BOGEY = 1;
    private static final int SCORE_DOUBLE_BOGEY = 2;
    private static final int SCORE_EAGLE = -2;
    private static final int SCORE_PAR = 0;
    private static final int SCORE_TRIPLE_BOGEY = 3;
    private static final int SERIES_COUNT = 11;
    private static final long serialVersionUID = -50208351283718766L;
    private double mBaselineOffset;
    private double mParHeight;
    private double mParPadding;

    public ParChartSeriesList(TypedValueGetter valueGetter) {
        super(11);
        for (int i = 0; i < 11; i++) {
            add(i, new SimpleDataAdapter());
        }
        this.mBaselineOffset = Math.abs(valueGetter.getFloat(R.id.shinobicharts_golf_par_zone_max));
        this.mParPadding = Math.abs(valueGetter.getFloat(R.id.shinobicharts_golf_par_padding));
        this.mParHeight = (this.mBaselineOffset * 2.0d) - (this.mParPadding * 2.0d);
    }

    public void addSkippedHole(double hole) {
        get(10).add(new DataPoint(Double.valueOf(hole), Double.valueOf((double) Constants.SPLITS_ACCURACY)));
    }

    public void addHoleData(int hole, int parDelta) {
        if (parDelta < 0) {
            switch (parDelta) {
                case -3:
                    addAlbatross(hole);
                    return;
                case -2:
                    addEagle(hole);
                    return;
                case -1:
                    addBirdie(hole);
                    return;
                default:
                    addAlbatross(hole);
                    return;
            }
        }
        switch (parDelta) {
            case 0:
                addPar(hole);
                return;
            case 1:
                addBogey(hole);
                return;
            case 2:
                addDoubleBogey(hole);
                return;
            default:
                addTripleBogey(hole);
                return;
        }
    }

    private void addAlbatross(double hole) {
        addBelowBaseline(hole);
        get(3).add(new DataPoint(Double.valueOf(hole), Double.valueOf((-3.0d) + this.mBaselineOffset)));
    }

    private void addEagle(double hole) {
        addBelowBaseline(hole);
        get(4).add(new DataPoint(Double.valueOf(hole), Double.valueOf((-2.0d) + this.mBaselineOffset)));
    }

    private void addBirdie(double hole) {
        addBelowBaseline(hole);
        get(5).add(new DataPoint(Double.valueOf(hole), Double.valueOf((-1.0d) + this.mBaselineOffset)));
    }

    private void addPar(double hole) {
        get(0).add(new DataPoint(Double.valueOf(hole), Double.valueOf((-this.mBaselineOffset) + this.mParPadding)));
        get(6).add(new DataPoint(Double.valueOf(hole), Double.valueOf(this.mParHeight)));
    }

    private void addBogey(double hole) {
        addAboveBaseline(hole);
        get(7).add(new DataPoint(Double.valueOf(hole), Double.valueOf(1.0d - this.mBaselineOffset)));
    }

    private void addDoubleBogey(double hole) {
        addAboveBaseline(hole);
        get(8).add(new DataPoint(Double.valueOf(hole), Double.valueOf(2.0d - this.mBaselineOffset)));
    }

    private void addTripleBogey(double hole) {
        addAboveBaseline(hole);
        get(9).add(new DataPoint(Double.valueOf(hole), Double.valueOf(3.0d - this.mBaselineOffset)));
    }

    private void addBelowBaseline(double hole) {
        get(2).add(new DataPoint(Double.valueOf(hole), Double.valueOf(-this.mBaselineOffset)));
    }

    private void addAboveBaseline(double hole) {
        get(1).add(new DataPoint(Double.valueOf(hole), Double.valueOf(this.mBaselineOffset)));
    }

    public DataAdapter<Double, Double> getBaselineData() {
        return get(0);
    }

    public DataAdapter<Double, Double> getAboveBaselineData() {
        return get(1);
    }

    public DataAdapter<Double, Double> getBelowBaselineData() {
        return get(2);
    }

    public DataAdapter<Double, Double> getAlbatrossData() {
        return get(3);
    }

    public DataAdapter<Double, Double> getEagleData() {
        return get(4);
    }

    public DataAdapter<Double, Double> getBirdieData() {
        return get(5);
    }

    public DataAdapter<Double, Double> getParData() {
        return get(6);
    }

    public DataAdapter<Double, Double> getBogeyData() {
        return get(7);
    }

    public DataAdapter<Double, Double> getDoubleBogeyData() {
        return get(8);
    }

    public DataAdapter<Double, Double> getTripleBogeyData() {
        return get(9);
    }

    public DataAdapter<Double, Double> getSkippedData() {
        return get(10);
    }
}
