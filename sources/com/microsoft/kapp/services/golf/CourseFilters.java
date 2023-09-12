package com.microsoft.kapp.services.golf;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import com.microsoft.kapp.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
/* loaded from: classes.dex */
public class CourseFilters implements Parcelable {
    private static final String RequestCourseTypeFilterString = "course_type";
    private static final String RequestHoleFilterString = "hole_count";
    private static final String RequestHoleTypePrivate = "private";
    private static final String RequestHoleTypePublic = "public";
    private static final String RequestHoleValue18Filter = "18";
    private static final String RequestHoleValue9Filter = "9";
    private LinkedHashMap<String, List<String>> selectedCourseFilters;
    public static String CourseTypeFilterString = "Course type";
    public static String HoleFilterString = "# of holes";
    public static String HoleValue9Filter = "9 holes";
    public static String HoleValue18Filter = "18 holes";
    public static String HoleTypePrivate = "Private";
    public static String HoleTypePublic = "Public";
    public static final Parcelable.Creator<CourseFilters> CREATOR = new Parcelable.Creator<CourseFilters>() { // from class: com.microsoft.kapp.services.golf.CourseFilters.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CourseFilters createFromParcel(Parcel in) {
            return new CourseFilters(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CourseFilters[] newArray(int size) {
            return new CourseFilters[size];
        }
    };

    /* loaded from: classes.dex */
    public enum AvailableCourseType {
        PUBLIC(0),
        PRIVATE(1),
        NOT_SET(2);
        
        public int value;

        AvailableCourseType(int value) {
            this.value = value;
        }

        public static AvailableCourseType getCourseType(int code) {
            AvailableCourseType[] arr$ = values();
            for (AvailableCourseType type : arr$) {
                if (type.value == code) {
                    return type;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    /* loaded from: classes.dex */
    public enum AvailableHoleType {
        HOLE_9(0),
        HOLE_18(1),
        NOT_SET(2);
        
        public int value;

        AvailableHoleType(int value) {
            this.value = value;
        }
    }

    public CourseFilters(Context context) {
        this.selectedCourseFilters = new LinkedHashMap<>();
        if (context != null) {
            CourseTypeFilterString = context.getResources().getString(R.string.golf_filter_course);
            HoleFilterString = context.getResources().getString(R.string.golf_filter_holes);
            HoleValue9Filter = context.getResources().getString(R.string.golf_filter_course_private);
            HoleValue18Filter = context.getResources().getString(R.string.golf_filter_course_public);
            HoleTypePrivate = context.getResources().getString(R.string.golf_filter_holes_9);
            HoleTypePublic = context.getResources().getString(R.string.golf_filter_holes_18);
        }
    }

    public AvailableCourseType getCourseTypeFilter() {
        ArrayList typeFilters = (ArrayList) this.selectedCourseFilters.get(CourseTypeFilterString);
        if (typeFilters == null) {
            return AvailableCourseType.NOT_SET;
        }
        if (typeFilters.contains(HoleTypePrivate) && typeFilters.contains(HoleTypePublic)) {
            return AvailableCourseType.NOT_SET;
        }
        if (typeFilters.contains(HoleTypePrivate)) {
            return AvailableCourseType.PRIVATE;
        }
        if (typeFilters.contains(HoleTypePublic)) {
            return AvailableCourseType.PUBLIC;
        }
        return AvailableCourseType.NOT_SET;
    }

    public AvailableHoleType getHoleTypeFilter() {
        ArrayList holeFilters = (ArrayList) this.selectedCourseFilters.get(HoleFilterString);
        if (holeFilters == null) {
            return AvailableHoleType.NOT_SET;
        }
        if (holeFilters.contains(HoleValue9Filter) && holeFilters.contains(HoleValue18Filter)) {
            return AvailableHoleType.NOT_SET;
        }
        if (holeFilters.contains(HoleValue9Filter)) {
            return AvailableHoleType.HOLE_9;
        }
        if (holeFilters.contains(HoleValue18Filter)) {
            return AvailableHoleType.HOLE_18;
        }
        return AvailableHoleType.NOT_SET;
    }

    public Map<String, String> getCloudFilterList() {
        Map<String, String> cloudFilterList = new HashMap<>();
        AvailableCourseType courseType = getCourseTypeFilter();
        if (courseType != AvailableCourseType.NOT_SET) {
            cloudFilterList.put(RequestCourseTypeFilterString, getRequestCourseTypeString(courseType));
        }
        AvailableHoleType numberOfHoles = getHoleTypeFilter();
        if (numberOfHoles != AvailableHoleType.NOT_SET) {
            cloudFilterList.put(RequestHoleFilterString, getRequestHoleTypeString(numberOfHoles));
        }
        return cloudFilterList;
    }

    public HashMap<String, List<String>> getFullCriteriaMap() {
        LinkedHashMap<String, List<String>> criteria = new LinkedHashMap<>();
        ArrayList<String> options = new ArrayList<>();
        options.add(HoleTypePublic);
        options.add(HoleTypePrivate);
        criteria.put(CourseTypeFilterString, options);
        ArrayList<String> options2 = new ArrayList<>();
        options2.add(HoleValue9Filter);
        options2.add(HoleValue18Filter);
        criteria.put(HoleFilterString, options2);
        return criteria;
    }

    public HashMap<String, List<String>> getSelectedCriteriaMap() {
        return this.selectedCourseFilters;
    }

    public String getHoleTypeString(AvailableHoleType holeType) {
        switch (holeType) {
            case HOLE_18:
                return HoleValue18Filter;
            case HOLE_9:
                return HoleValue9Filter;
            default:
                return "";
        }
    }

    public String getRequestHoleTypeString(AvailableHoleType holeType) {
        switch (holeType) {
            case HOLE_18:
                return RequestHoleValue18Filter;
            case HOLE_9:
                return RequestHoleValue9Filter;
            default:
                return "";
        }
    }

    public String getCourseTypeString(AvailableCourseType courseType) {
        switch (courseType) {
            case PRIVATE:
                return HoleTypePrivate;
            case PUBLIC:
                return HoleTypePublic;
            default:
                return "";
        }
    }

    public String getRequestCourseTypeString(AvailableCourseType courseType) {
        switch (courseType) {
            case PRIVATE:
                return RequestHoleTypePrivate;
            case PUBLIC:
                return RequestHoleTypePublic;
            default:
                return "";
        }
    }

    public void setFilters(HashMap<String, List<String>> filterCriteria) {
        this.selectedCourseFilters = new LinkedHashMap<>();
        for (Map.Entry<String, List<String>> entry : filterCriteria.entrySet()) {
            ArrayList<String> filterList = new ArrayList<>();
            for (String setFilter : entry.getValue()) {
                filterList.add(setFilter);
            }
            this.selectedCourseFilters.put(entry.getKey(), filterList);
        }
    }

    protected CourseFilters(Parcel in) {
        this.selectedCourseFilters = new LinkedHashMap<>();
        this.selectedCourseFilters = (LinkedHashMap) in.readSerializable();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.selectedCourseFilters);
    }
}
