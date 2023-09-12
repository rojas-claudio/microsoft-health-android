package com.microsoft.kapp.models.home;

import com.microsoft.kapp.KApplicationGraph;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.krestsdk.services.RestService;
import javax.inject.Inject;
/* loaded from: classes.dex */
public abstract class BaseDataFetcher implements DataFetcher {
    protected HomeData mHomeData;
    @Inject
    protected RestService mService;
    @Inject
    protected SettingsProvider mSettingsProvider;

    public BaseDataFetcher(HomeData homeData) {
        KApplicationGraph.getApplicationGraph().inject(this);
        this.mHomeData = homeData;
    }
}
