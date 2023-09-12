package com.microsoft.kapp.personalization;

import android.content.Context;
import com.microsoft.kapp.CargoConnection;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.krestsdk.models.BandVersion;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes.dex */
public class PersonalizationManagerFactory {
    public static final String BAND_VERSION_ID = "bandVersion";
    private static Map<BandVersion, PersonalizationManager> mPersonalizationManagers = new HashMap();
    CargoConnection mCargoConnection;
    Context mContext;
    SettingsProvider mSettingsProvider;

    public PersonalizationManagerFactory(Context context, SettingsProvider settingsProvider, CargoConnection cargoConnection) {
        this.mSettingsProvider = settingsProvider;
        this.mCargoConnection = cargoConnection;
        this.mContext = context;
    }

    public PersonalizationManager getPersonalizationManager(BandVersion bandVersion) {
        switch (bandVersion) {
            case NEON:
                if (!mPersonalizationManagers.containsKey(BandVersion.NEON)) {
                    PersonalizationManager manager = new NeonPersonalizationManager(this.mContext.getResources());
                    mPersonalizationManagers.put(BandVersion.NEON, manager);
                    return manager;
                }
                return mPersonalizationManagers.get(BandVersion.NEON);
            case CARGO:
                if (!mPersonalizationManagers.containsKey(BandVersion.CARGO)) {
                    PersonalizationManager manager2 = new CargoPersonalizationManager(this.mContext.getResources());
                    mPersonalizationManagers.put(BandVersion.CARGO, manager2);
                    return manager2;
                }
                return mPersonalizationManagers.get(BandVersion.CARGO);
            case UNKNOWN:
                KLog.w(getClass().getSimpleName(), "Unknown band connected to the device");
                return null;
            default:
                return null;
        }
    }
}
