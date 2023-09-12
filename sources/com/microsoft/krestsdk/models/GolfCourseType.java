package com.microsoft.krestsdk.models;

import android.content.Context;
import com.microsoft.kapp.R;
/* loaded from: classes.dex */
public enum GolfCourseType {
    UNKNOWN,
    PUBLIC,
    PRIVATE;

    public static String getDisplayValue(Context context, GolfCourseType courseType) {
        String courseTypeString = String.valueOf(courseType);
        if (courseType != null) {
            switch (courseType) {
                case PUBLIC:
                    return context.getString(R.string.golf_course_type_public_locked);
                case PRIVATE:
                    return context.getString(R.string.golf_course_type_private_locked);
                default:
                    return courseTypeString;
            }
        }
        return courseTypeString;
    }
}
