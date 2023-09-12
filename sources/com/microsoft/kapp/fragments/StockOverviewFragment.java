package com.microsoft.kapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.adapters.FinanceCompanyOverviewAdapter;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.services.finance.StockCompanyInformation;
import com.microsoft.kapp.strappsettings.StrappSettingsManager;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.views.CustomFontButton;
import com.microsoft.kapp.views.CustomGlyphView;
import java.util.List;
import javax.inject.Inject;
import org.apache.commons.lang3.Validate;
/* loaded from: classes.dex */
public class StockOverviewFragment extends BaseFragment {
    private CustomGlyphView mCloseButton;
    private ListView mCurrentStocksList;
    private CustomFontButton mEditButton;
    private OnEditStockListener mEditStockListener;
    @Inject
    StrappSettingsManager mStrappSettingsManager;

    /* loaded from: classes.dex */
    public interface OnEditStockListener {
        void moveToStockReorderFragment();
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Validate.notNull(this.mStrappSettingsManager, "mStrappSettingsManager", new Object[0]);
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_stock_settings_overview, container, false);
        this.mCurrentStocksList = (ListView) ViewUtils.getValidView(view, R.id.company_current_watched_stocks, ListView.class);
        this.mCloseButton = (CustomGlyphView) ViewUtils.getValidView(view, R.id.company_stock_settings_back_button, CustomGlyphView.class);
        this.mCloseButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.StockOverviewFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                StockOverviewFragment.this.getActivity().finish();
            }
        });
        View footer = inflater.inflate(R.layout.finance_edit_company_list_footer, (ViewGroup) this.mCurrentStocksList, false);
        this.mEditButton = (CustomFontButton) ViewUtils.getValidView(footer, R.id.finance_edit_companies, CustomFontButton.class);
        this.mEditButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.StockOverviewFragment.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (StockOverviewFragment.this.mEditStockListener != null) {
                    StockOverviewFragment.this.mEditStockListener.moveToStockReorderFragment();
                }
            }
        });
        this.mCurrentStocksList.addFooterView(footer);
        return view;
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        Telemetry.logPage(TelemetryConstants.PageViews.SETTINGS_BAND_MANAGE_TILES_CONNECTED_STOCKS);
        if (isAdded()) {
            setStockCompanies(this.mStrappSettingsManager.getStockCompanies());
        }
    }

    public void setStockCompanies(List<StockCompanyInformation> stockCompanies) {
        if (isAdded()) {
            FinanceCompanyOverviewAdapter financeAdapter = new FinanceCompanyOverviewAdapter(getActivity(), stockCompanies);
            this.mCurrentStocksList.setAdapter((ListAdapter) financeAdapter);
        }
    }

    public void setReorderStockListener(OnEditStockListener editStockListener) {
        this.mEditStockListener = editStockListener;
    }
}
