package net.hockeyapp.android.tasks;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import net.hockeyapp.android.Strings;
import net.hockeyapp.android.UpdateActivity;
import net.hockeyapp.android.UpdateFragment;
import net.hockeyapp.android.UpdateManagerListener;
import net.hockeyapp.android.utils.Util;
import net.hockeyapp.android.utils.VersionCache;
import org.json.JSONArray;
/* loaded from: classes.dex */
public class CheckUpdateTaskWithUI extends CheckUpdateTask {
    private Activity activity;
    private AlertDialog dialog;
    protected boolean isDialogRequired;

    public CheckUpdateTaskWithUI(WeakReference<Activity> weakActivity, String urlString, String appIdentifier, UpdateManagerListener listener, boolean isDialogRequired) {
        super(weakActivity, urlString, appIdentifier, listener);
        this.activity = null;
        this.dialog = null;
        this.isDialogRequired = false;
        if (weakActivity != null) {
            this.activity = weakActivity.get();
        }
        this.isDialogRequired = isDialogRequired;
    }

    @Override // net.hockeyapp.android.tasks.CheckUpdateTask
    public void detach() {
        super.detach();
        this.activity = null;
        if (this.dialog != null) {
            this.dialog.dismiss();
            this.dialog = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // net.hockeyapp.android.tasks.CheckUpdateTask, android.os.AsyncTask
    public void onPostExecute(JSONArray updateInfo) {
        super.onPostExecute(updateInfo);
        if (updateInfo != null && this.isDialogRequired) {
            showDialog(updateInfo);
        }
    }

    @TargetApi(11)
    private void showDialog(final JSONArray updateInfo) {
        if (getCachingEnabled()) {
            VersionCache.setVersionInfo(this.activity, updateInfo.toString());
        }
        if (this.activity != null && !this.activity.isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this.activity);
            builder.setTitle(Strings.get(this.listener, Strings.UPDATE_DIALOG_TITLE_ID));
            if (!this.mandatory.booleanValue()) {
                builder.setMessage(Strings.get(this.listener, Strings.UPDATE_DIALOG_MESSAGE_ID));
                builder.setNegativeButton(Strings.get(this.listener, Strings.UPDATE_DIALOG_NEGATIVE_BUTTON_ID), new DialogInterface.OnClickListener() { // from class: net.hockeyapp.android.tasks.CheckUpdateTaskWithUI.1
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialog, int which) {
                        CheckUpdateTaskWithUI.this.cleanUp();
                    }
                });
                builder.setPositiveButton(Strings.get(this.listener, Strings.UPDATE_DIALOG_POSITIVE_BUTTON_ID), new DialogInterface.OnClickListener() { // from class: net.hockeyapp.android.tasks.CheckUpdateTaskWithUI.2
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialog, int which) {
                        if (CheckUpdateTaskWithUI.this.getCachingEnabled()) {
                            VersionCache.setVersionInfo(CheckUpdateTaskWithUI.this.activity, "[]");
                        }
                        WeakReference<Activity> weakActivity = new WeakReference<>(CheckUpdateTaskWithUI.this.activity);
                        if (!Util.fragmentsSupported().booleanValue() || !Util.runsOnTablet(weakActivity).booleanValue()) {
                            CheckUpdateTaskWithUI.this.startUpdateIntent(updateInfo, false);
                        } else {
                            CheckUpdateTaskWithUI.this.showUpdateFragment(updateInfo);
                        }
                    }
                });
                this.dialog = builder.create();
                this.dialog.show();
                return;
            }
            Toast.makeText(this.activity, Strings.get(this.listener, 512), 1).show();
            startUpdateIntent(updateInfo, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @TargetApi(11)
    public void showUpdateFragment(JSONArray updateInfo) {
        if (this.activity != null) {
            FragmentTransaction fragmentTransaction = this.activity.getFragmentManager().beginTransaction();
            fragmentTransaction.setTransition(4097);
            Fragment existingFragment = this.activity.getFragmentManager().findFragmentByTag("hockey_update_dialog");
            if (existingFragment != null) {
                fragmentTransaction.remove(existingFragment);
            }
            fragmentTransaction.addToBackStack(null);
            Class<? extends UpdateFragment> fragmentClass = UpdateFragment.class;
            if (this.listener != null) {
                fragmentClass = this.listener.getUpdateFragmentClass();
            }
            try {
                Method method = fragmentClass.getMethod("newInstance", JSONArray.class, String.class);
                DialogFragment updateFragment = (DialogFragment) method.invoke(null, updateInfo, getURLString("apk"));
                updateFragment.show(fragmentTransaction, "hockey_update_dialog");
            } catch (Exception e) {
                Log.d("HockeyApp", "An exception happened while showing the update fragment:");
                e.printStackTrace();
                Log.d("HockeyApp", "Showing update activity instead.");
                startUpdateIntent(updateInfo, false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @TargetApi(11)
    public void startUpdateIntent(JSONArray updateInfo, Boolean finish) {
        Class<?> activityClass = null;
        if (this.listener != null) {
            activityClass = this.listener.getUpdateActivityClass();
        }
        if (activityClass == null) {
            activityClass = UpdateActivity.class;
        }
        if (this.activity != null) {
            Intent intent = new Intent();
            intent.setClass(this.activity, activityClass);
            intent.putExtra("json", updateInfo.toString());
            intent.putExtra("url", getURLString("apk"));
            this.activity.startActivity(intent);
            if (finish.booleanValue()) {
                this.activity.finish();
            }
        }
        cleanUp();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // net.hockeyapp.android.tasks.CheckUpdateTask
    public void cleanUp() {
        super.cleanUp();
        this.activity = null;
        this.dialog = null;
    }
}
