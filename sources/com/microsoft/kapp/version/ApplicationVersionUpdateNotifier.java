package com.microsoft.kapp.version;

import android.content.Context;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.utils.Constants;
import javax.inject.Inject;
import javax.inject.Singleton;
@Singleton
/* loaded from: classes.dex */
public class ApplicationVersionUpdateNotifier extends VersionUpdateNotifier<ApplicationVersionRetriever> {
    private final String TAG;
    private Version mApplicationVersion;

    @Inject
    public ApplicationVersionUpdateNotifier(Context context, DefaultApplicationVersionRetriever retriever) {
        super(retriever);
        this.TAG = ApplicationVersionUpdateNotifier.class.getSimpleName();
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO);
        try {
            this.mApplicationVersion = Version.parse(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName);
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to get the manifest version.", ex);
        }
    }

    @Override // com.microsoft.kapp.version.VersionUpdateNotifier, java.lang.Runnable
    public void run() {
        KLog.v(this.TAG, "Checking for application version update.");
        try {
            VersionUpdate versionUpdate = getRetriever().getLatestVersion();
            if (versionUpdate == null) {
                KLog.w(this.TAG, "getLatestVersion() should not returned null.");
            } else {
                Version version = versionUpdate.getVersion();
                if (this.mApplicationVersion.compareTo(version) < 0) {
                    KLog.v(this.TAG, "Application update detected. Old version: %s. New version: %s.", this.mApplicationVersion, version);
                    this.mApplicationVersion = version;
                    notifyVersionUpdateDetected(versionUpdate);
                }
            }
        } catch (Exception ex) {
            KLog.d(this.TAG, "Exception occurred while checking the version.", ex);
            notifyVersionUpdateCheckFailed(ex);
        }
    }
}
