package com.microsoft.kapp.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.microsoft.kapp.OnTaskListener;
import com.microsoft.kapp.R;
import com.microsoft.kapp.ScopedAsyncTask;
import com.microsoft.kapp.adapters.FinanceCompanyAddAdapter;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.services.finance.FinanceService;
import com.microsoft.kapp.services.finance.StockCompanyInformation;
import com.microsoft.kapp.utils.CommonUtils;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.views.CustomFontEditText;
import com.microsoft.kapp.views.CustomGlyphView;
import com.microsoft.krestsdk.services.KRestException;
import com.microsoft.krestsdk.services.RestService;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class StockAddFragment extends BaseFragment {
    private CustomGlyphView mCloseButton;
    private FinanceCompanyAddAdapter mCompanyAdapter;
    private ListView mCompanyList;
    private CustomFontEditText mCompanyName;
    private ArrayList<StockCompanyInformation> mDiscoveredCompanies;
    @Inject
    FinanceService mFinanceService;
    private final AtomicBoolean mIsPopUpVisible = new AtomicBoolean(false);
    private String mLastSearchedTerm;
    @Inject
    RestService mRestService;
    @Inject
    SettingsProvider mSettingsProvider;
    private OnStockSelectedListener mStockSelectedListener;

    /* loaded from: classes.dex */
    public interface OnStockSelectedListener {
        void onStockAdditionComplete(StockCompanyInformation stockCompanyInformation);

        void onStockSelectionComplete();
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_stock_settings_add, container, false);
        this.mCompanyList = (ListView) ViewUtils.getValidView(view, R.id.company_search_results, ListView.class);
        this.mCompanyList.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.microsoft.kapp.fragments.StockAddFragment.1
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (StockAddFragment.this.mStockSelectedListener != null && Validate.isNotNullNotEmpty(StockAddFragment.this.mDiscoveredCompanies)) {
                    StockAddFragment.this.mStockSelectedListener.onStockAdditionComplete((StockCompanyInformation) StockAddFragment.this.mDiscoveredCompanies.get(position));
                }
            }
        });
        this.mCompanyName = (CustomFontEditText) ViewUtils.getValidView(view, R.id.company_name, CustomFontEditText.class);
        this.mCompanyName.addTextChangedListener(new TextWatcher() { // from class: com.microsoft.kapp.fragments.StockAddFragment.2
            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence searchTerm, int start, int before, int count) {
                if ((StockAddFragment.this.mLastSearchedTerm == null || !searchTerm.toString().equals(StockAddFragment.this.mLastSearchedTerm)) && searchTerm.length() >= 2) {
                    StockAddFragment.this.searchForCompany(searchTerm);
                    StockAddFragment.this.mLastSearchedTerm = searchTerm.toString();
                }
            }

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable s) {
            }
        });
        this.mCompanyName.setMaxLines(1);
        this.mCloseButton = (CustomGlyphView) ViewUtils.getValidView(view, R.id.close_button_glyph, CustomGlyphView.class);
        this.mCloseButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.StockAddFragment.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (StockAddFragment.this.mStockSelectedListener != null) {
                    StockAddFragment.this.mStockSelectedListener.onStockSelectionComplete();
                }
            }
        });
        return view;
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        Telemetry.logPage(TelemetryConstants.PageViews.SETTINGS_BAND_MANAGE_TILES_CONNECTED_STOCKS_SEARCH);
    }

    @Override // android.support.v4.app.Fragment
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void setOnStockSelectedListener(OnStockSelectedListener stockSelectedListener) {
        this.mStockSelectedListener = stockSelectedListener;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void searchForCompany(CharSequence companyName) {
        DiscoverCompanyTask task = new DiscoverCompanyTask(this);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, companyName.toString());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshDiscoveredCompanyList() {
        if (this.mDiscoveredCompanies != null && this.mDiscoveredCompanies.size() != 0) {
            this.mCompanyAdapter = new FinanceCompanyAddAdapter(getActivity(), this.mDiscoveredCompanies);
            this.mCompanyList.setAdapter((ListAdapter) this.mCompanyAdapter);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class DiscoverCompanyTask extends ScopedAsyncTask<String, Void, ArrayList<StockCompanyInformation>> {
        public DiscoverCompanyTask(OnTaskListener onTaskListener) {
            super(onTaskListener);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.microsoft.kapp.ScopedAsyncTask
        public ArrayList<StockCompanyInformation> doInBackground(String... params) {
            try {
                return StockAddFragment.this.mFinanceService.getStockCompanies(params[0]);
            } catch (KRestException e) {
                setException(e);
                KLog.w(this.TAG, "Could not load stock companies!", e);
                return null;
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.microsoft.kapp.ScopedAsyncTask
        public void onPostExecute(ArrayList<StockCompanyInformation> discoveredCompanies) {
            if (Validate.isNotNullNotEmpty(discoveredCompanies)) {
                StockAddFragment.this.mDiscoveredCompanies = discoveredCompanies;
                StockAddFragment.this.refreshDiscoveredCompanyList();
                return;
            }
            Activity activity = StockAddFragment.this.getActivity();
            if (Validate.isActivityAlive(activity) && discoveredCompanies == null && !CommonUtils.isNetworkAvailable(activity) && StockAddFragment.this.mIsPopUpVisible.compareAndSet(false, true)) {
                StockAddFragment.this.getDialogManager().showNetworkErrorDialogWithCallback(activity, new DialogInterface.OnClickListener() { // from class: com.microsoft.kapp.fragments.StockAddFragment.DiscoverCompanyTask.1
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialog, int which) {
                        StockAddFragment.this.mIsPopUpVisible.set(false);
                    }
                });
            }
        }
    }
}
