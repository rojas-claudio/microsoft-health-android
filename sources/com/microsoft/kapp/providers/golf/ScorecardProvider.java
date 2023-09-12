package com.microsoft.kapp.providers.golf;

import com.microsoft.kapp.Callback;
import com.microsoft.kapp.models.golf.ScorecardModel;
/* loaded from: classes.dex */
public interface ScorecardProvider {
    void getScoreCardForEvent(String str, Callback<ScorecardModel> callback);
}
