package com.microsoft.kapp.activities;

import com.microsoft.krestsdk.models.DisplaySubType;
import java.util.List;
import java.util.Map;
/* loaded from: classes.dex */
public interface WorkoutPlanDiscoveryActivityController {
    public static final int FITNESS_BRANDS_PAGE = 4;
    public static final int FITNESS_START_PAGE = 0;
    public static final int FITNESS_TYPES_PAGE = 5;

    Map<String, String> getAllFiltersNamesIdsMapping();

    Map<String, String> getAllHnFBrands();

    List<String> getAllHnFTypes();

    Map<String, String> getFitnessPlanFilterSelection();

    String getMappingTypeName(DisplaySubType displaySubType);

    int getPlanType();

    String getSelectedBrandName();

    String getSelectedType();

    void nextPage(int i);

    void setHeaderText(int i);

    void setHeaderText(String str);

    void setSelectedBrandName(String str);

    void setSelectedTypeName(String str);
}
