package com.microsoft.kapp.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Pair;
import com.microsoft.kapp.R;
import com.microsoft.kapp.fragments.CredentialsFragment;
import com.microsoft.kapp.fragments.DebugActionsFragment;
import com.microsoft.kapp.fragments.DebugCalendarFragment;
import com.microsoft.kapp.fragments.DebugSettingsFragment;
import com.microsoft.kapp.fragments.debug.DebugSensorFragment;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class DebugActivity extends BaseFragmentActivity {
    private ArrayList<Pair<Fragment, String>> fragments;
    private ViewPager mViewPager;

    @Override // com.microsoft.kapp.activities.BaseFragmentActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
        if (this.mViewPager == null) {
            if (this.fragments == null) {
                this.fragments = new ArrayList<>();
                this.fragments.add(new Pair<>(new CredentialsFragment(), getString(R.string.title_activity_settings_device_fragment)));
                this.fragments.add(new Pair<>(new DebugSensorFragment(), getString(R.string.title_activity_debug_sensor_fragment)));
                this.fragments.add(new Pair<>(new DebugSettingsFragment(), getString(R.string.title_activity_debug_settings_fragment)));
                this.fragments.add(new Pair<>(new DebugActionsFragment(), getString(R.string.title_activity_debug_actions_fragment)));
                this.fragments.add(new Pair<>(new DebugCalendarFragment(), getString(R.string.title_activity_debug_calendar_fragment)));
            }
            this.mViewPager = (ViewPager) findViewById(R.id.debug_pager);
            this.mViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) { // from class: com.microsoft.kapp.activities.DebugActivity.1
                @Override // android.support.v4.view.PagerAdapter
                public int getCount() {
                    return DebugActivity.this.fragments.size();
                }

                @Override // android.support.v4.app.FragmentStatePagerAdapter
                public Fragment getItem(int position) {
                    return (Fragment) ((Pair) DebugActivity.this.fragments.get(position)).first;
                }

                @Override // android.support.v4.view.PagerAdapter
                public CharSequence getPageTitle(int position) {
                    return (CharSequence) ((Pair) DebugActivity.this.fragments.get(position)).second;
                }
            });
        }
    }
}
