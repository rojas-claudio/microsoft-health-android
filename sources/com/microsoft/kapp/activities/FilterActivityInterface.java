package com.microsoft.kapp.activities;

import java.util.List;
import java.util.Set;
/* loaded from: classes.dex */
public interface FilterActivityInterface {
    void clearAllFiltersSelection();

    Set<String> getAllFiltersDisplayNames();

    String getCurrentFilterSelection();

    List<String> getCurrentFilterValuesList();

    List<String> getFilterCriteriaList(String str);

    String getFilterName(String str);

    List<String> getSingleFilterValuesList(String str);

    void goToMainFilterPage();

    void returnResultAndExit(int i);

    void setSelectedCriteria(List<String> list);

    void startSelectionFragment(String str);
}
