package com.microsoft.kapp.models.strapp;

import com.microsoft.kapp.KApplicationGraph;
import com.microsoft.kapp.R;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.utils.AppConfigurationManager;
import com.microsoft.kapp.utils.CommonUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class DefaultStrappManager {
    private static final StrappDefinition[] Definitions = {new StrappDefinition(DefaultStrappUUID.STRAPP_MESSAGING, R.string.strapp_messaging, false, true), new StrappDefinition(DefaultStrappUUID.STRAPP_EMAIL, R.string.strapp_email, true, true), new StrappDefinition(DefaultStrappUUID.STRAPP_CALLS, R.string.strapp_calls, false, true), new StrappDefinition(DefaultStrappUUID.STRAPP_CALENDAR, R.string.strapp_calendar, false, true), new StrappDefinition(DefaultStrappUUID.STRAPP_RUN, R.string.strapp_run, false, true), new StrappDefinition(DefaultStrappUUID.STRAPP_BIKE, R.string.strapp_bike, false, true), new StrappDefinition(DefaultStrappUUID.STRAPP_GOLF, R.string.strapp_golf, false, true), new StrappDefinition(DefaultStrappUUID.STRAPP_EXERCISE, R.string.strapp_exercise, false, true), new StrappDefinition(DefaultStrappUUID.STRAPP_SLEEP, R.string.strapp_sleep, false, true), new StrappDefinition(DefaultStrappUUID.STRAPP_ALARM_STOPWATCH, R.string.strapp_alarm_stopwatch, false, true), new StrappDefinition(DefaultStrappUUID.STRAPP_GUIDED_WORKOUTS, R.string.strapp_guided_workouts, false, true), new StrappDefinition(DefaultStrappUUID.STRAPP_BING_WEATHER, (int) R.string.strapp_bing_weather, false), new StrappDefinition(DefaultStrappUUID.STRAPP_BING_FINANCE, (int) R.string.strapp_bing_finance, false), new StrappDefinition(DefaultStrappUUID.STRAPP_UV, R.string.strapp_uv, false, true), new StrappDefinition(DefaultStrappUUID.STRAPP_STARBUCKS, (int) R.string.strapp_starbucks, false), new StrappDefinition(DefaultStrappUUID.STRAPP_FACEBOOK, (int) R.string.strapp_facebook, true), new StrappDefinition(DefaultStrappUUID.STRAPP_FACEBOOK_MESSENGER, (int) R.string.strapp_facebook_messenger, true), new StrappDefinition(DefaultStrappUUID.STRAPP_TWITTER, (int) R.string.strapp_twitter, true), new StrappDefinition(DefaultStrappUUID.STRAPP_NOTIFICATION_CENTER, (int) R.string.strapp_notification_center, true)};
    @Inject
    AppConfigurationManager mAppConfigurationManager;
    private Map<UUID, StrappDefinition> mStrappMap;

    public DefaultStrappManager() {
        KApplicationGraph.getApplicationGraph().inject(this);
        boolean notificationEnabled = CommonUtils.areNotificationsSupported();
        this.mStrappMap = new LinkedHashMap();
        StrappDefinition[] arr$ = Definitions;
        for (StrappDefinition definition : arr$) {
            if (this.mAppConfigurationManager.isStrappEnabled(definition)) {
                if (notificationEnabled) {
                    this.mStrappMap.put(definition.getStrappId(), definition);
                } else if (!definition.isNotificationEnabled()) {
                    this.mStrappMap.put(definition.getStrappId(), definition);
                }
            }
        }
    }

    public StrappDefinition getStrappDefinition(UUID strappId) {
        Validate.notNull(strappId, "strappId");
        return this.mStrappMap.get(strappId);
    }

    public Collection<StrappDefinition> getStrappDefinitions() {
        return this.mStrappMap.values();
    }

    public Collection<StrappDefinition> getOobeStrappDefinitions() {
        Collection<StrappDefinition> initialMap = new ArrayList<>();
        for (StrappDefinition strappDefinition : this.mStrappMap.values()) {
            if (strappDefinition.isEnabledDuringOOBE()) {
                initialMap.add(strappDefinition);
            }
        }
        return initialMap;
    }

    public StrappStateCollection applyAppConfigChanges(StrappStateCollection enabledStrapps) {
        StrappStateCollection configChangesAppliedStrapp = new StrappStateCollection();
        for (UUID key : enabledStrapps.keySet()) {
            StrappState strappState = enabledStrapps.get(key);
            StrappDefinition definition = strappState.getDefinition();
            if (this.mAppConfigurationManager.isStrappEnabled(definition)) {
                configChangesAppliedStrapp.put(strappState);
            }
        }
        return configChangesAppliedStrapp;
    }
}
