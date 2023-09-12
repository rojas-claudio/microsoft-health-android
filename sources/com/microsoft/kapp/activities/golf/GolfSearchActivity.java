package com.microsoft.kapp.activities.golf;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.activities.BaseFragmentActivityWithOfflineSupport;
import com.microsoft.kapp.fragments.golf.GolfNearbySearchResultsFragment;
import com.microsoft.kapp.fragments.golf.GolfRecentSearchResultsFragment;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.utils.ActivityUtils;
import com.microsoft.kapp.utils.ViewUtils;
/* loaded from: classes.dex */
public class GolfSearchActivity extends BaseFragmentActivityWithOfflineSupport {
    public static final String GOLF_SEARCH_TYPE = "GolfSearchType";
    public static final int GOLF_SEARCH_TYPE_NEARBY = 2301;
    public static final int GOLF_SEARCH_TYPE_RECENT = 2302;
    private static final String TAG = GolfSearchActivity.class.getSimpleName();
    private TextView mBackButton;
    private TextView mHeaderTileText;
    private int mSearchType;

    @Override // com.microsoft.kapp.activities.BaseFragmentActivityWithOfflineSupport
    protected void onCreate(ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            restoreSavedData(savedInstanceState);
        } else {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                this.mSearchType = extras.getInt(GOLF_SEARCH_TYPE);
            }
        }
        setContentView(R.layout.golf_search_activity);
        View view = getWindow().getDecorView();
        this.mHeaderTileText = (TextView) ViewUtils.getValidView(view, R.id.header_text, TextView.class);
        this.mBackButton = (TextView) ViewUtils.getValidView(view, R.id.back, TextView.class);
        this.mBackButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.activities.golf.GolfSearchActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                GolfSearchActivity.this.onBackPressed();
            }
        });
        loadSearchResults();
    }

    private void loadSearchResults() {
        switch (this.mSearchType) {
            case GOLF_SEARCH_TYPE_NEARBY /* 2301 */:
                this.mHeaderTileText.setText(R.string.golf_nearBy);
                navigateToFragment(new GolfNearbySearchResultsFragment());
                return;
            case GOLF_SEARCH_TYPE_RECENT /* 2302 */:
                this.mHeaderTileText.setText(R.string.golf_recent);
                navigateToFragment(new GolfRecentSearchResultsFragment());
                return;
            default:
                KLog.e(TAG, "Error determining search type");
                setState(1235);
                return;
        }
    }

    private void restoreSavedData(Bundle savedData) {
        if (savedData != null) {
            this.mSearchType = savedData.getInt(GOLF_SEARCH_TYPE);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.activities.BaseFragmentActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(GOLF_SEARCH_TYPE, this.mSearchType);
    }

    private void navigateToFragment(Fragment fragment) {
        FragmentTransaction ft = ActivityUtils.getFragmentTransaction(this, false);
        ft.replace(R.id.main_content, fragment);
        try {
            ft.commit();
            setState(1234);
        } catch (IllegalStateException ex) {
            KLog.w(TAG, "Error during fragment transaction", ex);
            setState(1235);
        }
    }
}
