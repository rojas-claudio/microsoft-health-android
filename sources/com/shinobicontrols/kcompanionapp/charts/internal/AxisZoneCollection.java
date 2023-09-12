package com.shinobicontrols.kcompanionapp.charts.internal;

import android.content.res.Resources;
import com.microsoft.kapp.R;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/* loaded from: classes.dex */
public class AxisZoneCollection implements Iterable<AxisZone> {
    private static final float Epsilon = 1.0E-4f;
    private final List<AxisZone> axisZones = new ArrayList();

    public List<Double> getTickMarkValues() {
        List<Double> tickMarkValues = new ArrayList<>();
        for (AxisZone axisZone : this.axisZones) {
            tickMarkValues.add(Double.valueOf(axisZone.getValue()));
        }
        return tickMarkValues;
    }

    public void add(AxisZone axisZone) {
        this.axisZones.add(axisZone);
    }

    @Override // java.lang.Iterable
    public Iterator<AxisZone> iterator() {
        return this.axisZones.iterator();
    }

    /* loaded from: classes.dex */
    public static class HeartRateAxisZoneCollection extends AxisZoneCollection {
        private final int maxHeartRateForUser;
        private final TypedValueGetter valueGetter;

        public HeartRateAxisZoneCollection(Resources resources, int userMaxHr, int userAge) {
            this.valueGetter = new TypedValueGetter(resources);
            if (userMaxHr == 0) {
                this.maxHeartRateForUser = ((int) this.valueGetter.getFloat(R.id.shinobicharts_run_heart_rate_max)) - userAge;
            } else {
                this.maxHeartRateForUser = userMaxHr;
            }
            double initialValue = getZoneValue(R.id.shinobicharts_heart_rate_initial_zone_proportion);
            add(new AxisZone(initialValue, "", true, "", ""));
            double veryLightValue = getZoneValue(R.id.shinobicharts_heart_rate_very_light_zone_proportion);
            String veryLightText = getZoneRangeText(resources, R.id.shinobicharts_heart_rate_initial_zone_proportion, R.id.shinobicharts_heart_rate_very_light_zone_proportion);
            String veryLightTitle = resources.getString(R.string.shinobicharts_verylight);
            add(new AxisZone(veryLightValue, veryLightTitle, true, "", veryLightText));
            double lightValue = getZoneValue(R.id.shinobicharts_heart_rate_light_zone_proportion);
            String lightText = getZoneRangeText(resources, R.id.shinobicharts_heart_rate_very_light_zone_proportion, R.id.shinobicharts_heart_rate_light_zone_proportion);
            String lightTitle = resources.getString(R.string.shinobicharts_light);
            add(new AxisZone(lightValue, lightTitle, true, "", lightText));
            double moderateValue = getZoneValue(R.id.shinobicharts_heart_rate_moderate_zone_proportion);
            String moderateText = getZoneRangeText(resources, R.id.shinobicharts_heart_rate_light_zone_proportion, R.id.shinobicharts_heart_rate_moderate_zone_proportion);
            String moderateTitle = resources.getString(R.string.shinobicharts_moderate);
            add(new AxisZone(moderateValue, moderateTitle, true, "", moderateText));
            double hardValue = getZoneValue(R.id.shinobicharts_heart_rate_hard_zone_proportion);
            String hardText = getZoneRangeText(resources, R.id.shinobicharts_heart_rate_moderate_zone_proportion, R.id.shinobicharts_heart_rate_hard_zone_proportion);
            String hardTitle = resources.getString(R.string.shinobicharts_hard);
            add(new AxisZone(hardValue, hardTitle, true, "", hardText));
            double veryHardValue = getZoneValue(R.id.shinobicharts_heart_rate_chart_max_zone_proportion);
            String veryHardTitle = resources.getString(R.string.shinobicharts_veryhard);
            String veryhardText = resources.getString(R.string.shinobicharts_heart_rate_range_suffix_text, Integer.valueOf((int) getZoneValue(R.id.shinobicharts_heart_rate_very_hard_zone_proportion)));
            add(new AxisZone(veryHardValue, veryHardTitle, true, "", veryhardText));
        }

        public double getZoneValue(int resId) {
            return this.valueGetter.getFloat(resId) * this.maxHeartRateForUser;
        }

        private String getZoneRangeText(Resources resources, int rangeStartResId, int rangeEndResId) {
            return resources.getString(R.string.shinobicharts_heart_rate_range_text, Integer.valueOf((int) Math.ceil(getZoneValue(rangeStartResId) + 9.999999747378752E-5d)), Integer.valueOf((int) getZoneValue(rangeEndResId)));
        }
    }
}
