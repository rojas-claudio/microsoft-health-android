package com.microsoft.kapp.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.adapters.NavigationCommandAdapterV1;
import com.microsoft.kapp.navigations.NavigationManagerV1;
import com.microsoft.kapp.utils.Constants;
/* loaded from: classes.dex */
public abstract class BaseNavigationFragment extends BaseFragment {
    protected Context mContext;
    protected ListView mNavigationList;
    protected NavigationManagerV1 mNavigationManager;
    private BroadcastReceiver mUserProfileUpdatedReceiver = new BroadcastReceiver() { // from class: com.microsoft.kapp.fragments.BaseNavigationFragment.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            BaseNavigationFragment.this.initialize();
        }
    };

    protected abstract NavigationManagerV1 createNavigationManager();

    protected void initialize() {
        if (isAdded()) {
            this.mNavigationManager = createNavigationManager();
            this.mNavigationList.setAdapter((ListAdapter) new NavigationCommandAdapterV1(this.mContext, R.layout.navigation_drawer_navigation_item, this.mNavigationManager.getCommands()));
            this.mNavigationList.setOnItemClickListener(new DrawerItemClickListener());
        }
    }

    @Override // android.support.v4.app.Fragment
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mContext = activity;
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.INTENT_USER_PROFILE_UPDATED);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(this.mUserProfileUpdatedReceiver, filter);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override // android.support.v4.app.Fragment
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize();
    }

    @Override // android.support.v4.app.Fragment
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(this.mUserProfileUpdatedReceiver);
        super.onDestroy();
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment
    protected void setTopMenuDividerVisibility() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void refreshNavigationList() {
        HeaderViewListAdapter rootAdapter = (HeaderViewListAdapter) this.mNavigationList.getAdapter();
        NavigationCommandAdapterV1 mNavigationCommandAdapterV1 = (NavigationCommandAdapterV1) rootAdapter.getWrappedAdapter();
        mNavigationCommandAdapterV1.notifyDataSetChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class DrawerItemClickListener implements AdapterView.OnItemClickListener {
        private DrawerItemClickListener() {
        }

        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            int position2 = position - BaseNavigationFragment.this.mNavigationList.getHeaderViewsCount();
            BaseNavigationFragment.this.mNavigationList.setItemChecked(position2, true);
            BaseNavigationFragment.this.mNavigationManager.navigate(position2);
        }
    }
}
