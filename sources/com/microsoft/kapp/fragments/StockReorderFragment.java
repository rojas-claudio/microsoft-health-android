package com.microsoft.kapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import com.microsoft.kapp.R;
import com.microsoft.kapp.activities.settings.FinanceSettingsActivity;
import com.microsoft.kapp.adapters.FinanceCompanyReorderAdapter;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.services.finance.StockCompanyInformation;
import com.microsoft.kapp.strappsettings.StrappSettingsManager;
import com.microsoft.kapp.utils.ActivityUtils;
import com.microsoft.kapp.utils.DialogManager;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.views.CustomFontButton;
import com.microsoft.kapp.views.SortableFinanceListview;
import com.microsoft.kapp.widgets.ConfirmationBar;
import java.util.List;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class StockReorderFragment extends BaseFragment implements FinanceCompanyReorderAdapter.RemoveCompanyListener {
    private static final int TOTAL_COMPANIES_ALLOWED = 7;
    private CustomFontButton mAddButton;
    private ConfirmationBar mConfirmationBar;
    private SortableFinanceListview mCurrentStocksListView;
    private SortableFinanceListview.OnReorderListener mReorderListener;
    private OnReorderStockPageListener mReorderStockPageListener;
    private List<StockCompanyInformation> mStockCompanies;
    @Inject
    StrappSettingsManager mStrappSettingsManager;

    /* loaded from: classes.dex */
    public interface OnReorderStockPageListener {
        void moveToStockAddFragment();

        void reorderStocks(List<StockCompanyInformation> list);

        List<StockCompanyInformation> revertStocks();
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Validate.notNull(this.mStrappSettingsManager, "mStrappSettingsManager");
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_stock_settings_reorder, container, false);
        this.mCurrentStocksListView = (SortableFinanceListview) ViewUtils.getValidView(view, R.id.company_reorder, SortableFinanceListview.class);
        this.mConfirmationBar = (ConfirmationBar) ViewUtils.getValidView(view, R.id.confirmation_bar, ConfirmationBar.class);
        this.mConfirmationBar.setVisibility(0);
        View footer = inflater.inflate(R.layout.finance_add_company_list_footer, (ViewGroup) this.mCurrentStocksListView, false);
        this.mAddButton = (CustomFontButton) ViewUtils.getValidView(footer, R.id.finance_add_companies, CustomFontButton.class);
        this.mAddButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.StockReorderFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (StockReorderFragment.this.mReorderStockPageListener != null) {
                    if (StockReorderFragment.this.mStockCompanies.size() < 7) {
                        StockReorderFragment.this.mReorderStockPageListener.moveToStockAddFragment();
                    } else {
                        DialogManager.showDialog(StockReorderFragment.this.getActivity(), Integer.valueOf((int) R.string.app_name), Integer.valueOf((int) R.string.settings_finance_reorder_error), DialogManager.Priority.LOW);
                    }
                }
            }
        });
        this.mCurrentStocksListView.addFooterView(footer);
        if (this.mCurrentStocksListView != null) {
            setStockCompanies(this.mStockCompanies);
        }
        this.mReorderListener = new SortableFinanceListview.OnReorderListener() { // from class: com.microsoft.kapp.fragments.StockReorderFragment.2
            @Override // com.microsoft.kapp.views.SortableFinanceListview.OnReorderListener
            public void onReorder(List<StockCompanyInformation> newOrder) {
                StockReorderFragment.this.onOrderChanged(newOrder);
            }
        };
        this.mCurrentStocksListView.setOnReorderListener(this.mReorderListener);
        this.mConfirmationBar.setOnConfirmButtonClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.StockReorderFragment.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                StockReorderFragment.this.mReorderStockPageListener.reorderStocks(StockReorderFragment.this.mStockCompanies);
                ActivityUtils.performBackButton(StockReorderFragment.this.getActivity());
            }
        });
        this.mConfirmationBar.setOnCancelButtonClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.StockReorderFragment.4
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                StockReorderFragment.this.mStockCompanies = StockReorderFragment.this.mReorderStockPageListener.revertStocks();
                StockReorderFragment.this.setStockCompanies(StockReorderFragment.this.mStockCompanies);
                ActivityUtils.performBackButton(StockReorderFragment.this.getActivity());
            }
        });
        return view;
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        Telemetry.logPage(TelemetryConstants.PageViews.SETTINGS_BAND_MANAGE_TILES_CONNECTED_STOCKS_WATCHLIST);
        if (isAdded()) {
            setStockCompanies(((FinanceSettingsActivity) getActivity()).getCurrentStockCompanies());
        }
    }

    public void setAdjustStockListener(OnReorderStockPageListener addStockListener) {
        this.mReorderStockPageListener = addStockListener;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onOrderChanged(List<StockCompanyInformation> stockCompanies) {
        this.mStockCompanies = stockCompanies;
    }

    public void setStockCompanies(List<StockCompanyInformation> stockCompanies) {
        if (isAdded()) {
            this.mStockCompanies = stockCompanies;
            if (this.mStockCompanies != null) {
                this.mCurrentStocksListView.setVisibility(0);
                FinanceCompanyReorderAdapter financeAdapter = new FinanceCompanyReorderAdapter(getActivity(), this.mStockCompanies);
                financeAdapter.setOnRemoveCompanyListener(this);
                this.mCurrentStocksListView.setStockCompaniesDisplayed(this.mStockCompanies);
                this.mCurrentStocksListView.setAdapter((ListAdapter) financeAdapter);
                return;
            }
            this.mCurrentStocksListView.setVisibility(8);
        }
    }

    @Override // com.microsoft.kapp.adapters.FinanceCompanyReorderAdapter.RemoveCompanyListener
    public void removeCompany(StockCompanyInformation stockCompany) {
        if (this.mStockCompanies != null && this.mStockCompanies.size() > 1) {
            if (this.mStockCompanies != null && this.mStockCompanies.contains(stockCompany)) {
                this.mStockCompanies.remove(stockCompany);
            }
            onOrderChanged(this.mStockCompanies);
            setStockCompanies(this.mStockCompanies);
            return;
        }
        DialogManager.showDialog(getActivity(), Integer.valueOf((int) R.string.app_name), Integer.valueOf((int) R.string.settings_finance_remove_all_error), DialogManager.Priority.LOW);
    }
}
