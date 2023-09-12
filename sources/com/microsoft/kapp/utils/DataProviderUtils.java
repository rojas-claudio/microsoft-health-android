package com.microsoft.kapp.utils;

import com.microsoft.krestsdk.models.BikeEvent;
import com.microsoft.krestsdk.models.ExerciseEventBase;
import com.microsoft.krestsdk.models.GolfEvent;
import com.microsoft.krestsdk.models.RunEvent;
import com.microsoft.krestsdk.models.SleepEvent;
import com.microsoft.krestsdk.models.UserActivity;
import com.microsoft.krestsdk.models.UserDailySummary;
import com.shinobicontrols.kcompanionapp.charts.DataProvider;
/* loaded from: classes.dex */
public final class DataProviderUtils {
    public static DataProvider createBikeEventDataProvider(final BikeEvent event) {
        return new BaseDataProvider() { // from class: com.microsoft.kapp.utils.DataProviderUtils.1
            {
                super();
            }

            @Override // com.microsoft.kapp.utils.DataProviderUtils.BaseDataProvider, com.shinobicontrols.kcompanionapp.charts.DataProvider
            public BikeEvent getBikeEvent() {
                return BikeEvent.this;
            }
        };
    }

    public static DataProvider createExerciseEventDataProvider(final ExerciseEventBase event) {
        return new BaseDataProvider() { // from class: com.microsoft.kapp.utils.DataProviderUtils.2
            {
                super();
            }

            @Override // com.microsoft.kapp.utils.DataProviderUtils.BaseDataProvider, com.shinobicontrols.kcompanionapp.charts.DataProvider
            public ExerciseEventBase getExerciseEvent() {
                return ExerciseEventBase.this;
            }
        };
    }

    public static DataProvider createRunEventDataProvider(final RunEvent event) {
        return new BaseDataProvider() { // from class: com.microsoft.kapp.utils.DataProviderUtils.3
            {
                super();
            }

            @Override // com.microsoft.kapp.utils.DataProviderUtils.BaseDataProvider, com.shinobicontrols.kcompanionapp.charts.DataProvider
            public RunEvent getRunEvent() {
                return RunEvent.this;
            }
        };
    }

    public static DataProvider createGolfEventDataProvider(final GolfEvent event) {
        return new BaseDataProvider() { // from class: com.microsoft.kapp.utils.DataProviderUtils.4
            {
                super();
            }

            @Override // com.microsoft.kapp.utils.DataProviderUtils.BaseDataProvider, com.shinobicontrols.kcompanionapp.charts.DataProvider
            public GolfEvent getGolfEvent() {
                return GolfEvent.this;
            }
        };
    }

    /* loaded from: classes.dex */
    private static class BaseDataProvider implements DataProvider {
        private BaseDataProvider() {
        }

        @Override // com.shinobicontrols.kcompanionapp.charts.DataProvider
        public UserActivity[] getHourlyUserActivitiesForDay() {
            return null;
        }

        @Override // com.shinobicontrols.kcompanionapp.charts.DataProvider
        public UserDailySummary[] getDailySummariesForWeek() {
            return null;
        }

        @Override // com.shinobicontrols.kcompanionapp.charts.DataProvider
        public RunEvent getRunEvent() {
            return null;
        }

        @Override // com.shinobicontrols.kcompanionapp.charts.DataProvider
        public SleepEvent getSleepEvent() {
            return null;
        }

        @Override // com.shinobicontrols.kcompanionapp.charts.DataProvider
        public BikeEvent getBikeEvent() {
            return null;
        }

        @Override // com.shinobicontrols.kcompanionapp.charts.DataProvider
        public ExerciseEventBase getExerciseEvent() {
            return null;
        }

        @Override // com.shinobicontrols.kcompanionapp.charts.DataProvider
        public GolfEvent getGolfEvent() {
            return null;
        }
    }
}
