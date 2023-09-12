package com.microsoft.kapp.parsers;

import com.microsoft.krestsdk.models.RaisedInsight;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.DateTimeZone;
import org.joda.time.ReadableInstant;
/* loaded from: classes.dex */
public class InsightsDisplayFilter {
    public static RaisedInsight getRaisedInsight(List<RaisedInsight> insights, RaisedInsightFilter... filters) {
        boolean hasExpired;
        if (insights == null || insights.isEmpty()) {
            return null;
        }
        Collections.sort(insights, new Comparator<RaisedInsight>() { // from class: com.microsoft.kapp.parsers.InsightsDisplayFilter.1
            @Override // java.util.Comparator
            public int compare(RaisedInsight lhs, RaisedInsight rhs) {
                DateTime lhsDT = lhs.getEffectiveDT();
                DateTime rhsDT = rhs.getEffectiveDT();
                return rhsDT.compareTo((ReadableInstant) lhsDT);
            }
        });
        RaisedInsight firstNonWorseRaisedInsight = null;
        HashMap<String, RaisedInsight> seperatedRaisedInsights = new HashMap<>();
        DateTime todayUTC = DateTime.now().toDateTime(DateTimeZone.UTC);
        for (RaisedInsight raisedInsight : insights) {
            String tonePivot = raisedInsight.getTonePivot();
            DateTime expirationDT = raisedInsight.getExpirationDT();
            if (expirationDT == null) {
                hasExpired = false;
            } else {
                hasExpired = DateTimeComparator.getDateOnlyInstance().compare(expirationDT, todayUTC) < 0;
            }
            if (!RaisedInsight.Pivot.WORSE.toString().equalsIgnoreCase(tonePivot) && !hasExpired) {
                for (RaisedInsightFilter raisedInsightFilter : filters) {
                    if (raisedInsightFilter.hasBeenFiltered(raisedInsight) && !seperatedRaisedInsights.containsKey(raisedInsightFilter.toString())) {
                        seperatedRaisedInsights.put(raisedInsightFilter.toString(), raisedInsight);
                    }
                }
                if (firstNonWorseRaisedInsight == null) {
                    firstNonWorseRaisedInsight = raisedInsight;
                }
            }
        }
        for (RaisedInsightFilter raisedInsightFilter2 : filters) {
            if (seperatedRaisedInsights.containsKey(raisedInsightFilter2.toString())) {
                return seperatedRaisedInsights.get(raisedInsightFilter2.toString());
            }
        }
        return firstNonWorseRaisedInsight;
    }
}
