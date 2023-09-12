package com.microsoft.kapp.parsers;

import android.util.SparseArray;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.krestsdk.models.InsightMetadata;
import com.microsoft.krestsdk.models.InsightTracker;
import com.microsoft.krestsdk.models.RaisedInsight;
import java.util.List;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class InsightUtils {
    public static final int INSIGHT_GROUP_DAILY_WALK = 1;
    public static final int INSIGHT_GROUP_WEEKLY_WALK = 2;
    private static final String TAG = InsightUtils.class.getSimpleName();

    public static RaisedInsight getInsight(List<RaisedInsight> raisedInsights, List<InsightMetadata> insightMetadataList, InsightTracker insightType, int insightGroupId) {
        if (raisedInsights == null || insightMetadataList == null) {
            return null;
        }
        RaisedInsight selectedInsight = null;
        try {
            SparseArray<InsightMetadata> insightsMetadataMap = new SparseArray<>();
            for (InsightMetadata insightMetadata : insightMetadataList) {
                insightsMetadataMap.put(insightMetadata.getInsightID(), insightMetadata);
            }
            for (RaisedInsight raisedInsight : raisedInsights) {
                InsightMetadata insightMetadata2 = insightsMetadataMap.get(raisedInsight.getInsightId());
                DateTime effectiveUntil = raisedInsight.getEffectiveDT().plusMinutes(insightMetadata2.getTimeFrame());
                if (insightMetadata2.getTracker() == insightType && insightMetadata2.getGroupId() == insightGroupId && effectiveUntil.isAfter(DateTime.now()) && insightMetadata2.getEnabled() && !insightMetadata2.getOptOut()) {
                    selectedInsight = raisedInsight;
                }
            }
            return selectedInsight;
        } catch (Exception e) {
            KLog.d(TAG, "Insights parsing error", e);
            return selectedInsight;
        }
    }
}
