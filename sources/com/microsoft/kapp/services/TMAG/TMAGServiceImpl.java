package com.microsoft.kapp.services.TMAG;

import com.microsoft.kapp.services.SettingsProvider;
/* loaded from: classes.dex */
public class TMAGServiceImpl implements TMAGService {
    private SettingsProvider mSettingsProvider;

    public TMAGServiceImpl(SettingsProvider settingsProvider) {
        this.mSettingsProvider = settingsProvider;
    }

    @Override // com.microsoft.kapp.services.TMAG.TMAGService
    public boolean isConnected() {
        return this.mSettingsProvider.isTMAGConnectionEnabled();
    }
}
