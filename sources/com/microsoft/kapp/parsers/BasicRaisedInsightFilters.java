package com.microsoft.kapp.parsers;

import com.microsoft.krestsdk.models.RaisedInsight;
/* loaded from: classes.dex */
public enum BasicRaisedInsightFilters implements RaisedInsightFilter {
    TONE_CAUTION { // from class: com.microsoft.kapp.parsers.BasicRaisedInsightFilters.1
        @Override // com.microsoft.kapp.parsers.RaisedInsightFilter
        public boolean hasBeenFiltered(RaisedInsight insight) {
            return RaisedInsight.Pivot.CAUTION.toString().equalsIgnoreCase(insight.getTonePivot());
        }
    },
    COMPARISION_SELF { // from class: com.microsoft.kapp.parsers.BasicRaisedInsightFilters.2
        @Override // com.microsoft.kapp.parsers.RaisedInsightFilter
        public boolean hasBeenFiltered(RaisedInsight insight) {
            return RaisedInsight.Pivot.SELF.toString().equalsIgnoreCase(insight.getComparisonPivot());
        }
    },
    COMPARISION_PEOPLE_LIKE_YOU { // from class: com.microsoft.kapp.parsers.BasicRaisedInsightFilters.3
        @Override // com.microsoft.kapp.parsers.RaisedInsightFilter
        public boolean hasBeenFiltered(RaisedInsight insight) {
            return RaisedInsight.Pivot.PEOPLE_LIKE_YOU.toString().equalsIgnoreCase(insight.getComparisonPivot());
        }
    }
}
