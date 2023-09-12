package com.microsoft.kapp.services.golf;

import com.microsoft.kapp.models.golf.GolfRegion;
/* loaded from: classes.dex */
public interface GolfFindCourseByRegionListener {
    void onRegionSelected(GolfRegion golfRegion);

    void onStateSelected(GolfRegion golfRegion);

    void setPageTitle(String str);
}
