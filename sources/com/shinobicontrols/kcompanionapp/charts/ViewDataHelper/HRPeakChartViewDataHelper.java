package com.shinobicontrols.kcompanionapp.charts.ViewDataHelper;

import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.krestsdk.models.GolfEvent;
import com.microsoft.krestsdk.models.GolfEventHoleSequence;
import com.shinobicontrols.charts.DataAdapter;
import com.shinobicontrols.charts.DataPoint;
import com.shinobicontrols.charts.SimpleDataAdapter;
import com.shinobicontrols.kcompanionapp.properties.ChartDataProperties;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class HRPeakChartViewDataHelper extends ChartDataProperties {
    private GolfEvent mGolfEvent;
    private boolean mHasDataToPlot;
    private int mTotalLineSeries;
    private List<DataAdapter<Integer, Integer>> mAllLineSeriesData = new ArrayList();
    private DataAdapter<Integer, Integer> mPointSeriesData = new SimpleDataAdapter();
    private List<DataPoint<Integer, Integer>> mPointSeriesDPs = new ArrayList();
    private List<List<DataPoint<Integer, Integer>>> mAllLineSeriesDPs = new ArrayList();

    public HRPeakChartViewDataHelper(GolfEvent golfEvent) {
        Validate.notNull(golfEvent, "golfEvent");
        this.mGolfEvent = golfEvent;
        setDataPorperties();
    }

    public int getTotalLineSeries() {
        return this.mTotalLineSeries;
    }

    public List<DataAdapter<Integer, Integer>> getAllSeriesData() {
        return this.mAllLineSeriesData;
    }

    public DataAdapter<Integer, Integer> getPointSeriesData() {
        return this.mPointSeriesData;
    }

    public List<List<DataPoint<Integer, Integer>>> getAllLineSeriesDPs() {
        return this.mAllLineSeriesDPs;
    }

    public List<DataPoint<Integer, Integer>> getPointSeriesDPs() {
        return this.mPointSeriesDPs;
    }

    public boolean hasDataToPlot() {
        return this.mHasDataToPlot;
    }

    private void setDataPorperties() {
        if (this.mGolfEvent.getSequences() != null && this.mGolfEvent.getSequences().length != 0) {
            this.mHasDataToPlot = true;
            GolfEventHoleSequence[] sequences = this.mGolfEvent.getSortedSequences();
            int start = sequences[0].getHoleNumber();
            int end = sequences[0].getHoleNumber();
            int previousLineSeriesHoleNumber = sequences[0].getHoleNumber();
            double maxHRValue = Double.MIN_VALUE;
            double minHRValue = Double.MAX_VALUE;
            for (int i = 0; i < sequences.length; i++) {
                GolfEventHoleSequence sequence = sequences[i];
                int holeNumber = sequence.getHoleNumber();
                int maxPeakHeartRate = sequence.getPeakHeartRate();
                if (holeNumber - previousLineSeriesHoleNumber != 1) {
                    removeStaleLineSeries();
                }
                if (maxPeakHeartRate > 0) {
                    DataPoint<Integer, Integer> dp = new DataPoint<>(Integer.valueOf(holeNumber), Integer.valueOf(maxPeakHeartRate));
                    this.mPointSeriesDPs.add(dp);
                    if (holeNumber - previousLineSeriesHoleNumber == 1) {
                        end++;
                        if (this.mAllLineSeriesDPs.isEmpty()) {
                            this.mAllLineSeriesDPs.add(new ArrayList());
                        }
                        this.mAllLineSeriesDPs.get(this.mAllLineSeriesDPs.size() - 1).add(dp);
                    } else {
                        if (start != end) {
                            removeStaleLineSeries();
                        }
                        if (i != sequences.length - 1) {
                            this.mAllLineSeriesDPs.add(new ArrayList());
                            this.mAllLineSeriesDPs.get(this.mAllLineSeriesDPs.size() - 1).add(dp);
                        }
                        start = holeNumber;
                        end = holeNumber;
                    }
                    previousLineSeriesHoleNumber = holeNumber;
                    maxHRValue = Math.max(maxHRValue, maxPeakHeartRate);
                    minHRValue = Math.min(minHRValue, maxPeakHeartRate);
                }
            }
            setAllLineSeriesData();
            setPointSeriesData();
            setMinMaxValue(maxHRValue, minHRValue);
            this.mTotalLineSeries = this.mAllLineSeriesData.size();
        }
    }

    private void setMinMaxValue(double max, double min) {
        this.mMinValue = min;
        this.mMaxValue = max;
    }

    private void setPointSeriesData() {
        for (DataPoint<Integer, Integer> dataPoint : this.mPointSeriesDPs) {
            this.mPointSeriesData.add(dataPoint);
        }
    }

    private void setAllLineSeriesData() {
        for (List<DataPoint<Integer, Integer>> dps : this.mAllLineSeriesDPs) {
            DataAdapter<Integer, Integer> da = new SimpleDataAdapter<>();
            this.mAllLineSeriesData.add(da);
            for (DataPoint<Integer, Integer> lineDataPoint : dps) {
                da.add(lineDataPoint);
            }
        }
    }

    private void removeStaleLineSeries() {
        if (this.mAllLineSeriesDPs.size() > 0) {
            int totalDataPointsInCurrentIndex = this.mAllLineSeriesDPs.get(this.mAllLineSeriesDPs.size() - 1).size();
            if (totalDataPointsInCurrentIndex == 1) {
                this.mAllLineSeriesDPs.remove(this.mAllLineSeriesDPs.size() - 1);
            }
        }
    }
}
