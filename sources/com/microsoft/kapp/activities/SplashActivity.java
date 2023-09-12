package com.microsoft.kapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.microsoft.kapp.KApplication;
import com.microsoft.kapp.R;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.fragments.SplashFragment;
import com.microsoft.kapp.logging.http.FiddlerLogger;
import com.microsoft.kapp.models.FreStatus;
import com.microsoft.kapp.models.whatsnew.WhatsNewCards;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.FirmwareUpdateUtils;
import com.microsoft.kapp.utils.FreUtils;
import com.microsoft.kapp.utils.UpgradeUtils;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class SplashActivity extends FragmentActivity implements SplashFragment.SplashFragmentCalls {
    private static final int CUSTOM_ENTER_ANIMATION = 2130968577;
    private static final int CUSTOM_EXIT_ANIMATION = 2130968578;
    private static final String SPLASH_FRAGMENT = "splash_fragment";
    @Inject
    FiddlerLogger mFiddlerLogger;
    private boolean mIsMiniOobe;
    @Inject
    SettingsProvider mSettingsProvider;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(1);
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_splash);
        Validate.notNull(findViewById(R.id.start_fragment_container), "start_fragment_container");
        KApplication application = (KApplication) getApplication();
        application.inject(this);
        Validate.notNull(this.mSettingsProvider, "mSettingsProvider");
        if (savedInstanceState == null) {
            this.mIsMiniOobe = getIntent().getBooleanExtra(Constants.MINI_OOBE_FLAG, false);
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).setTransitionStyle(4097).add(R.id.start_fragment_container, new SplashFragment(), SPLASH_FRAGMENT).commit();
            if (!this.mIsMiniOobe) {
                boolean appJustUpgraded = UpgradeUtils.isAppJustUpgraded(this.mSettingsProvider, getApplicationContext());
                if (appJustUpgraded) {
                    FirmwareUpdateUtils.clearCachedFirmwareUpdateInfo(this.mSettingsProvider);
                }
                if (appJustUpgraded && WhatsNewCards.isCardsVersionChanged(this.mSettingsProvider)) {
                    this.mSettingsProvider.setNotificationWhatsNewEnabled(true);
                    this.mFiddlerLogger.cleanup();
                }
                WhatsNewCards.updateCardVersionInSetting(this.mSettingsProvider);
                int appSessionCount = this.mSettingsProvider.getAppSessionCountBeforeClickWhatsNew();
                boolean isNotificationWhatsNewEnabled = this.mSettingsProvider.isNotificationWhatsNewEnabled();
                if (isNotificationWhatsNewEnabled) {
                    this.mSettingsProvider.setAppSessionCountBeforeClickWhatsNew(appSessionCount + 1);
                }
            }
        }
    }

    @Override // com.microsoft.kapp.fragments.SplashFragment.SplashFragmentCalls
    public void onSplashFragmentComplete() {
        if ((!this.mIsMiniOobe && FreUtils.isOOBEComplete(this.mSettingsProvider)) || !FreUtils.freRedirect(this, this.mSettingsProvider)) {
            this.mSettingsProvider.setFreStatus(FreStatus.SHOWN);
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(32768);
            intent.addFlags(268435456);
            startActivity(intent);
            overridePendingTransition(0, 0);
        }
        finish();
    }
}
