package com.shinobicontrols.kcompanionapp.charts;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.utils.Constants;
import com.shinobicontrols.charts.AnnotationsManager;
import com.shinobicontrols.charts.Axis;
import com.shinobicontrols.charts.ChartView;
import com.shinobicontrols.charts.ShinobiChart;
import com.shinobicontrols.kcompanionapp.charts.internal.AxisMapping;
import com.shinobicontrols.kcompanionapp.charts.internal.BaseChartFragment;
import com.shinobicontrols.kcompanionapp.charts.internal.ChartManager;
import com.shinobicontrols.kcompanionapp.charts.internal.RangeAndFrequencySettingStrategy;
import com.shinobicontrols.kcompanionapp.properties.ChartDataProperties;
import com.shinobicontrols.kcompanionapp.providers.XAxisProvider;
import com.shinobicontrols.kcompanionapp.providers.XAxisStrategyProvider;
import java.util.List;
/* loaded from: classes.dex */
public class ChartFragment extends BaseChartFragment implements ShinobiChart.OnInternalLayoutListener {
    private static final String ARG_IS_METRIC = "is_metric";
    private ChartManager.OnAxisMappingSelectedListener mAxisSelectedListener = new ChartManager.OnAxisMappingSelectedListener() { // from class: com.shinobicontrols.kcompanionapp.charts.ChartFragment.1
        @Override // com.shinobicontrols.kcompanionapp.charts.internal.ChartManager.OnAxisMappingSelectedListener
        public void onAxisMappingSelected(AxisMapping axisMapping) {
            Drawable legend = axisMapping.getIconLegend();
            if (legend != null) {
                ChartFragment.this.mIconLegend.setBackground(legend);
                ChartFragment.this.mIconLegend.setVisibility(0);
                ChartFragment.this.mIconText.setText(axisMapping.getText());
                ChartFragment.this.mIconText.setTextColor(ChartFragment.this.getResources().getColor(R.color.blackHigh));
                return;
            }
            ChartFragment.this.mIconText.setText(axisMapping.getText());
            ChartFragment.this.mIconText.setTextColor(ChartFragment.this.getResources().getColor(R.color.PrimaryColor));
            ChartFragment.this.mIconText.setTextSize(ChartFragment.this.getResources().getDimension(R.dimen.shinobicharts_icon_text_size) / ChartFragment.this.getResources().getDisplayMetrics().density);
            ChartFragment.this.mIconLegend.setVisibility(8);
        }
    };
    private ChartDataProperties mChartProperties;
    private List<IChartView> mCharts;
    private FrameLayout mIconLegend;
    private TextView mIconText;
    private boolean mIsInitialized;
    private boolean mIsMetricUnits;
    private Axis<Double, Double> mXAxis;
    private XAxisProvider mXAxisProvider;
    private RangeAndFrequencySettingStrategy mXAxisRangeAndFrequencySettingStrategy;
    private XAxisStrategyProvider mXAxisStrategyProvider;

    public ChartFragment() {
        this.mIsInitialized = false;
        this.mIsInitialized = false;
    }

    public static ChartFragment newInstance(List<IChartView> charts, XAxisProvider xAxisProvider, XAxisStrategyProvider xAxisStrategyProvider, ChartDataProperties properties, boolean isMetric) {
        ChartFragment fragment = new ChartFragment();
        fragment.setCharts(charts);
        fragment.mIsMetricUnits = isMetric;
        fragment.mXAxisProvider = xAxisProvider;
        fragment.mChartProperties = properties;
        fragment.mXAxisStrategyProvider = xAxisStrategyProvider;
        fragment.mIsInitialized = true;
        return fragment;
    }

    @Override // android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            this.mIsMetricUnits = savedInstanceState.getBoolean(ARG_IS_METRIC);
        }
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.internal.BaseChartFragment, android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        if (this.mIsInitialized) {
            ViewGroup iconGroup = (ViewGroup) rootView.findViewById(R.id.icon_group);
            ChartView chartView = (ChartView) rootView.findViewById(R.id.chart);
            this.mIconText = (TextView) rootView.findViewById(R.id.icon_text);
            this.mIconLegend = (FrameLayout) rootView.findViewById(R.id.icon_legend);
            ShinobiChart chart = chartView.getShinobiChart();
            if (this.mXAxisProvider != null) {
                this.mXAxis = this.mXAxisProvider.getAxis(getActivity(), getChartThemeCache(), this.mIsMetricUnits);
                chart.setXAxis(this.mXAxis);
                this.mXAxisRangeAndFrequencySettingStrategy = this.mXAxisStrategyProvider.getRangeAndFrequencyStrategy(getActivity(), this.mXAxis, Constants.SPLITS_ACCURACY, Constants.SPLITS_ACCURACY, this.mChartProperties);
                this.mXAxisRangeAndFrequencySettingStrategy.setRangeAndFrequency();
                FrameLayout chartHolder = (FrameLayout) rootView.findViewById(R.id.chart_holder);
                ChartManager chartManager = getChartManager();
                for (IChartView currentChart : this.mCharts) {
                    currentChart.onChartViewCreated(chart);
                    currentChart.setChartThemeCache(getChartThemeCache());
                    iconGroup.addView(currentChart.createIcon());
                    chart.addYAxis(currentChart.getYAxis());
                    currentChart.createSeries();
                    currentChart.createAnnotations();
                    currentChart.createAdditionalViews(chartHolder);
                    chartManager.addMapping(currentChart.getAxisMapping());
                }
                chartManager.setOnSelectedListener(this.mAxisSelectedListener);
                chart.setOnInternalLayoutListener(this);
            }
        }
        return rootView;
    }

    public void setCharts(List<IChartView> charts) {
        this.mCharts = charts;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.internal.Provideable
    public void reload() {
        if (isReadyForReload()) {
            AnnotationsManager annotationsManager = this.mXAxis.getChart().getAnnotationsManager();
            annotationsManager.removeAllAnnotations();
            for (IChartView chartView : this.mCharts) {
                chartView.reload(this.mXAxis);
            }
        }
    }

    @Override // com.shinobicontrols.charts.ShinobiChart.OnInternalLayoutListener
    public void onInternalLayout(ShinobiChart chart) {
        if (this.mXAxis != null) {
            for (IChartView chartView : this.mCharts) {
                chartView.onInternalLayout(chart, this.mXAxis);
            }
        }
    }
}
