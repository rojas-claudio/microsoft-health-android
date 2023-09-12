package com.microsoft.kapp.comparators;

import com.microsoft.krestsdk.models.UserActivity;
import java.util.Comparator;
import org.joda.time.ReadableInstant;
/* loaded from: classes.dex */
public class UserActivityComparator implements Comparator<UserActivity> {
    @Override // java.util.Comparator
    public int compare(UserActivity activity1, UserActivity activity2) {
        return activity1.getTimeOfDay().compareTo((ReadableInstant) activity2.getTimeOfDay());
    }
}
