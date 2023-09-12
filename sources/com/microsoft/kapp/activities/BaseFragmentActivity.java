package com.microsoft.kapp.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import com.microsoft.kapp.CargoConnection;
import com.microsoft.kapp.OnTaskListener;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.services.SettingsProvider;
import javax.inject.Inject;
/* loaded from: classes.dex */
public abstract class BaseFragmentActivity extends FragmentActivity implements OnTaskListener, DialogManagerProvider {
    @Inject
    CargoConnection mCargoConnection;
    @Inject
    SettingsProvider mSettingsProvider;
    private BaseActivityAdapter mAdapter = new BaseActivityAdapter(this);
    private boolean mIsSafeToCommitFragmentTransaction = false;

    @Override // com.microsoft.kapp.activities.DialogManagerProvider
    public DialogManager getDialogManager() {
        return this.mAdapter.getDialogManager();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        this.mIsSafeToCommitFragmentTransaction = true;
        super.onCreate(savedInstanceState);
        this.mAdapter.onCreate(savedInstanceState);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onResume() {
        this.mIsSafeToCommitFragmentTransaction = false;
        super.onResume();
        this.mAdapter.onResume();
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        this.mAdapter.onCreateOptionsMenu(menu);
        return true;
    }

    @Override // android.app.Activity
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        this.mAdapter.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override // android.app.Activity
    protected void onRestart() {
        super.onRestart();
        this.mIsSafeToCommitFragmentTransaction = true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onPause() {
        this.mIsSafeToCommitFragmentTransaction = false;
        super.onPause();
        this.mAdapter.onPause();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.support.v4.app.FragmentActivity
    public void onResumeFragments() {
        this.mIsSafeToCommitFragmentTransaction = true;
        super.onResumeFragments();
    }

    public boolean isSafeToCommitFragmentTransactions() {
        return this.mIsSafeToCommitFragmentTransaction;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onSaveInstanceState(Bundle outState) {
        this.mIsSafeToCommitFragmentTransaction = false;
        super.onSaveInstanceState(outState);
    }

    @Override // com.microsoft.kapp.OnTaskListener
    public boolean isWaitingForResult() {
        return Validate.isActivityAlive(this);
    }
}
