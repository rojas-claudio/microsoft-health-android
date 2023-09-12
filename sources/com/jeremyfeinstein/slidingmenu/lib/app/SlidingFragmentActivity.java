package com.jeremyfeinstein.slidingmenu.lib.app;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
/* loaded from: classes.dex */
public class SlidingFragmentActivity extends FragmentActivity implements SlidingActivityBase {
    private SlidingActivityHelper mHelper;

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mHelper = new SlidingActivityHelper(this);
        this.mHelper.onCreate(savedInstanceState);
    }

    @Override // android.app.Activity
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        this.mHelper.onPostCreate(savedInstanceState);
    }

    @Override // android.app.Activity
    public View findViewById(int id) {
        View v = super.findViewById(id);
        return v != null ? v : this.mHelper.findViewById(id);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        this.mHelper.onSaveInstanceState(outState);
    }

    @Override // android.app.Activity
    public void setContentView(int id) {
        setContentView(getLayoutInflater().inflate(id, (ViewGroup) null));
    }

    @Override // android.app.Activity
    public void setContentView(View v) {
        setContentView(v, new ViewGroup.LayoutParams(-1, -1));
    }

    @Override // android.app.Activity
    public void setContentView(View v, ViewGroup.LayoutParams params) {
        super.setContentView(v, params);
        this.mHelper.registerAboveContentView(v, params);
    }

    @Override // com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase
    public void setBehindContentView(int id) {
        setBehindContentView(getLayoutInflater().inflate(id, (ViewGroup) null));
    }

    @Override // com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase
    public void setBehindContentView(View v) {
        setBehindContentView(v, new ViewGroup.LayoutParams(-1, -1));
    }

    @Override // com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase
    public void setBehindContentView(View v, ViewGroup.LayoutParams params) {
        this.mHelper.setBehindContentView(v, params);
    }

    @Override // com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase
    public SlidingMenu getSlidingMenu() {
        return this.mHelper.getSlidingMenu();
    }

    @Override // com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase
    public void toggle() {
        this.mHelper.toggle();
    }

    @Override // com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase
    public void showContent() {
        this.mHelper.showContent();
    }

    @Override // com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase
    public void showMenu() {
        this.mHelper.showMenu();
    }

    @Override // com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase
    public void showSecondaryMenu() {
        this.mHelper.showSecondaryMenu();
    }

    @Override // com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase
    public void setSlidingActionBarEnabled(boolean b) {
        this.mHelper.setSlidingActionBarEnabled(b);
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        boolean b = this.mHelper.onKeyUp(keyCode, event);
        return b ? b : super.onKeyUp(keyCode, event);
    }
}
