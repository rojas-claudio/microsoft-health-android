package com.microsoft.kapp.activities.settings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.microsoft.kapp.R;
import com.microsoft.kapp.activities.BaseFragmentActivity;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.fragments.StockAddFragment;
import com.microsoft.kapp.fragments.StockOverviewFragment;
import com.microsoft.kapp.fragments.StockReorderFragment;
import com.microsoft.kapp.services.finance.StockCompanyInformation;
import com.microsoft.kapp.strappsettings.StrappSettingsManager;
import com.microsoft.kapp.utils.ActivityUtils;
import com.microsoft.kapp.utils.CommonUtils;
import com.microsoft.kapp.utils.DialogManager;
import com.microsoft.kapp.utils.ViewUtils;
import java.util.List;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class FinanceSettingsActivity extends BaseFragmentActivity implements StockAddFragment.OnStockSelectedListener, StockReorderFragment.OnReorderStockPageListener, StockOverviewFragment.OnEditStockListener {
    private List<StockCompanyInformation> mStockCompanies;
    @Inject
    StrappSettingsManager mStrappSettingsManager;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.activities.BaseFragmentActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Validate.notNull(this.mStrappSettingsManager, "mStrappSettingsManager");
        setContentView(R.layout.activity_finance_settings);
        this.mStockCompanies = this.mStrappSettingsManager.getStockCompanies();
        moveToStockOverviewFragment();
    }

    @Override // com.microsoft.kapp.fragments.StockAddFragment.OnStockSelectedListener
    public void onStockAdditionComplete(StockCompanyInformation newStockCompany) {
        if (this.mStockCompanies != null) {
            for (StockCompanyInformation stock : this.mStockCompanies) {
                if (stock.getBingValue().equals(newStockCompany.getBingValue())) {
                    if (Validate.isActivityAlive(this)) {
                        DialogManager.showDialog(this, Integer.valueOf((int) R.string.app_name), Integer.valueOf((int) R.string.settings_finance_stock_already_in_list), DialogManager.Priority.LOW);
                        return;
                    }
                    return;
                }
            }
            this.mStockCompanies.add(newStockCompany);
            closeCurrentFragment();
        }
    }

    @Override // com.microsoft.kapp.fragments.StockAddFragment.OnStockSelectedListener
    public void onStockSelectionComplete() {
        closeCurrentFragment();
    }

    private void closeCurrentFragment() {
        ViewUtils.closeSoftKeyboard(this, findViewById(R.id.current_fragment));
        FragmentManager manager = getSupportFragmentManager();
        manager.popBackStack();
    }

    private void moveToStockOverviewFragment() {
        if (!isDestroyed() && !isFinishing()) {
            StockOverviewFragment stockOverviewFragment = new StockOverviewFragment();
            stockOverviewFragment.setStockCompanies(this.mStrappSettingsManager.getStockCompanies());
            stockOverviewFragment.setReorderStockListener(this);
            FragmentTransaction ft = ActivityUtils.getFragmentTransaction(this, false);
            ft.replace(R.id.current_fragment, stockOverviewFragment);
            ft.commitAllowingStateLoss();
        }
    }

    @Override // com.microsoft.kapp.fragments.StockOverviewFragment.OnEditStockListener
    public void moveToStockReorderFragment() {
        if (!isDestroyed() && !isFinishing()) {
            StockReorderFragment stockReorderFragment = new StockReorderFragment();
            stockReorderFragment.setStockCompanies(this.mStockCompanies);
            stockReorderFragment.setAdjustStockListener(this);
            moveToFragment(stockReorderFragment);
        }
    }

    private void moveToFragment(Fragment fragment) {
        FragmentTransaction ft = ActivityUtils.getFragmentTransaction(this, true);
        ft.replace(R.id.current_fragment, fragment);
        ft.addToBackStack(fragment.getTag());
        ft.commitAllowingStateLoss();
    }

    @Override // com.microsoft.kapp.fragments.StockReorderFragment.OnReorderStockPageListener
    public void moveToStockAddFragment() {
        if (!isDestroyed() && !isFinishing()) {
            if (CommonUtils.isNetworkAvailable(this)) {
                StockAddFragment stockLookupFragment = new StockAddFragment();
                stockLookupFragment.setOnStockSelectedListener(this);
                moveToFragment(stockLookupFragment);
                return;
            }
            DialogManager.showNetworkErrorDialog(this);
        }
    }

    @Override // com.microsoft.kapp.fragments.StockReorderFragment.OnReorderStockPageListener
    public void reorderStocks(List<StockCompanyInformation> stockList) {
        this.mStockCompanies = stockList;
        this.mStrappSettingsManager.setTransactionStockList(this.mStockCompanies);
    }

    public List<StockCompanyInformation> getCurrentStockCompanies() {
        return this.mStockCompanies;
    }

    @Override // com.microsoft.kapp.fragments.StockReorderFragment.OnReorderStockPageListener
    public List<StockCompanyInformation> revertStocks() {
        this.mStockCompanies = this.mStrappSettingsManager.getStockCompanies();
        return this.mStockCompanies;
    }
}
