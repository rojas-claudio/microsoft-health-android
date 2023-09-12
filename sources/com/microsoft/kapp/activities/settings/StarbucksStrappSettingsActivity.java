package com.microsoft.kapp.activities.settings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.microsoft.kapp.R;
import com.microsoft.kapp.activities.BaseFragmentActivity;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.fragments.StarbucksAddCardFragment;
import com.microsoft.kapp.fragments.StarbucksNoCardsOverviewFragment;
import com.microsoft.kapp.fragments.StarbucksOverviewFragment;
import com.microsoft.kapp.strappsettings.StrappSettingsManager;
import com.microsoft.kapp.utils.ActivityUtils;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class StarbucksStrappSettingsActivity extends BaseFragmentActivity implements StarbucksAddCardFragment.AddCardListener, StarbucksNoCardsOverviewFragment.OnAddCardClickListener, StarbucksOverviewFragment.OnRemoveCardListener {
    @Inject
    StrappSettingsManager mStrappSettingsManager;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.activities.BaseFragmentActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Validate.notNull(this.mStrappSettingsManager, "mStrappSettingsManager");
        setContentView(R.layout.activity_finance_settings);
        String starbucksCardNumber = this.mStrappSettingsManager.getStarbucksCardNumber();
        if (starbucksCardNumber == null || starbucksCardNumber.isEmpty()) {
            moveToStarbucksNoCardOverviewFragment();
        } else {
            moveToStarbucksOverviewFragment();
        }
    }

    private void moveToStarbucksAddCardFragment() {
        if (Validate.isActivityAlive(this)) {
            StarbucksAddCardFragment addCardFragment = new StarbucksAddCardFragment();
            addCardFragment.setOnAddCardListener(this);
            moveToFragment(addCardFragment, true);
        }
    }

    private void moveToStarbucksNoCardOverviewFragment() {
        if (Validate.isActivityAlive(this)) {
            StarbucksNoCardsOverviewFragment overviewFragment = new StarbucksNoCardsOverviewFragment();
            overviewFragment.setAddCardClickListener(this);
            moveToFragment(overviewFragment, false);
        }
    }

    private void moveToStarbucksOverviewFragment() {
        if (Validate.isActivityAlive(this)) {
            StarbucksOverviewFragment overviewFragment = new StarbucksOverviewFragment();
            overviewFragment.setRemoveCardListener(this);
            moveToFragment(overviewFragment, false);
        }
    }

    private void moveToFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction ft = ActivityUtils.getFragmentTransaction(this, false);
        ft.replace(R.id.current_fragment, fragment);
        if (addToBackStack) {
            ft.addToBackStack(fragment.getTag());
        }
        ft.commitAllowingStateLoss();
    }

    @Override // com.microsoft.kapp.fragments.StarbucksAddCardFragment.AddCardListener
    public void onAddCard() {
        FragmentManager manager = getSupportFragmentManager();
        manager.popBackStack();
        moveToStarbucksOverviewFragment();
    }

    @Override // com.microsoft.kapp.fragments.StarbucksAddCardFragment.AddCardListener
    public void onCancelCard() {
        FragmentManager manager = getSupportFragmentManager();
        manager.popBackStack();
        moveToStarbucksNoCardOverviewFragment();
    }

    @Override // com.microsoft.kapp.fragments.StarbucksNoCardsOverviewFragment.OnAddCardClickListener
    public void moveToManageCardsFragment() {
        moveToStarbucksAddCardFragment();
    }

    @Override // com.microsoft.kapp.fragments.StarbucksOverviewFragment.OnRemoveCardListener
    public void moveToNoCardFragment() {
        moveToStarbucksNoCardOverviewFragment();
    }
}
