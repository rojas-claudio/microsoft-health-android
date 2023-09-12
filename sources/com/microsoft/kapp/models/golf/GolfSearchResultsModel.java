package com.microsoft.kapp.models.golf;

import com.microsoft.krestsdk.models.GolfCourse;
import com.microsoft.krestsdk.models.GolfCourseSearchResults;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/* loaded from: classes.dex */
public class GolfSearchResultsModel {
    private List<GolfSearchResultItem> mGolfResultItems;

    public GolfSearchResultsModel() {
        this.mGolfResultItems = new ArrayList();
    }

    public GolfSearchResultsModel(List<GolfSearchResultItem> searchResults) {
        this.mGolfResultItems = searchResults;
    }

    public void addSearchResults(GolfCourseSearchResults courses, boolean hasDistance) {
        if (courses != null && courses.getFacilities() != null) {
            addCourseResultsForSet(courses.getFacilities(), hasDistance);
        }
    }

    private void addCourseResultsForSet(List<GolfCourse> facilities, boolean hasDistance) {
        for (GolfCourse course : facilities) {
            GolfSearchResultItem item = new GolfSearchResultItem();
            item.setCity(course.getCity());
            item.setDistance((long) course.getDistance());
            item.setName(course.getName());
            item.setNumberOfHoles(course.getNumberOfHoles());
            item.setIsDistanceAvailable(hasDistance);
            item.setCourseType(course.getCourseType());
            item.setCourseId(course.getCourseId());
            this.mGolfResultItems.add(item);
        }
    }

    public void addAndSortResults(GolfCourseSearchResults mCourses) {
        List<GolfCourse> facilities = mCourses.getFacilities();
        Collections.sort(facilities);
        addCourseResultsForSet(facilities, false);
    }

    public List<GolfSearchResultItem> getItems() {
        return this.mGolfResultItems;
    }
}
