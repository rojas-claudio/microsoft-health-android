package com.microsoft.kapp.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.microsoft.kapp.OnTaskListener;
import com.microsoft.kapp.diagnostics.Validate;
/* loaded from: classes.dex */
public class BaseActivity extends Activity implements OnTaskListener, DialogManagerProvider {
    private BaseActivityAdapter mAdapter = new BaseActivityAdapter(this);

    @Override // com.microsoft.kapp.activities.DialogManagerProvider
    public DialogManager getDialogManager() {
        return this.mAdapter.getDialogManager();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mAdapter.onCreate(savedInstanceState);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onResume() {
        super.onResume();
        this.mAdapter.onResume();
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return this.mAdapter.onCreateOptionsMenu(menu);
    }

    @Override // android.app.Activity
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return this.mAdapter.onPrepareOptionsMenu(menu);
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        return this.mAdapter.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override // com.microsoft.kapp.OnTaskListener
    public boolean isWaitingForResult() {
        return Validate.isActivityAlive(this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onPause() {
        super.onPause();
        this.mAdapter.onPause();
    }
}
