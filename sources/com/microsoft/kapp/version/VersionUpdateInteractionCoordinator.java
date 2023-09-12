package com.microsoft.kapp.version;

import android.app.Activity;
import android.content.DialogInterface;
import com.microsoft.kapp.R;
import com.microsoft.kapp.activities.DialogPriority;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.utils.DialogManagerImpl;
import java.util.Date;
import javax.inject.Inject;
import javax.inject.Singleton;
@Singleton
/* loaded from: classes.dex */
public class VersionUpdateInteractionCoordinator {
    private Date mApplicationUpdateLastDetected;
    private final ApplicationUpdateLauncher mLauncher;
    private VersionCheckExecutor mVersionCheckExecutor;

    @Inject
    public VersionUpdateInteractionCoordinator(ApplicationUpdateLauncher launcher) {
        Validate.notNull(launcher, "launcher");
        this.mLauncher = launcher;
    }

    public void setVersionCheckExecutor(VersionCheckExecutor versionCheckExecutor) {
        Validate.notNull(versionCheckExecutor, "versionCheckExecutor");
        this.mVersionCheckExecutor = versionCheckExecutor;
    }

    public void notifyApplicationUpdateAvailable() {
        this.mApplicationUpdateLastDetected = new Date();
    }

    public void requestApplicationVersionUpdateCheck() {
        if (this.mVersionCheckExecutor != null) {
            this.mVersionCheckExecutor.requestApplicationVersionUpdateCheck();
        }
    }

    public void displayApplicationUpdateNotificationIfNecessary(final Activity activity) {
        Validate.notNull(activity, "activity");
        if (this.mApplicationUpdateLastDetected != null) {
            DialogManagerImpl.getDialogManager(activity).showDialog(activity, Integer.valueOf((int) R.string.version_update_interaction_coordinator_application_update_available_title), Integer.valueOf((int) R.string.version_update_interaction_coordinator_application_update_available), R.string.version_update_interaction_coordinator_application_update_update, new DialogInterface.OnClickListener() { // from class: com.microsoft.kapp.version.VersionUpdateInteractionCoordinator.1
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    VersionUpdateInteractionCoordinator.this.mLauncher.launch(activity);
                }
            }, DialogPriority.HIGH);
        }
    }
}
