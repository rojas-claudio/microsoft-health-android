package com.microsoft.kapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import com.microsoft.kapp.R;
import com.microsoft.kapp.services.finance.StockCompanyInformation;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.views.CustomFontTextView;
import java.util.List;
/* loaded from: classes.dex */
public class FinanceCompanyOverviewAdapter extends ArrayAdapter<StockCompanyInformation> {
    private static final int RESOURCE_ID = 2130903077;

    /* loaded from: classes.dex */
    private static class ViewHolder {
        private CustomFontTextView mNameTextView;
        private CustomFontTextView mTickerSymbolTextView;

        private ViewHolder() {
        }
    }

    public FinanceCompanyOverviewAdapter(Context context, List<StockCompanyInformation> stockCompanies) {
        super(context, (int) R.layout.adapter_settings_stock_overview, stockCompanies);
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService("layout_inflater");
            convertView = inflater.inflate(R.layout.adapter_settings_stock_overview, (ViewGroup) null);
            holder = new ViewHolder();
            holder.mNameTextView = (CustomFontTextView) ViewUtils.getValidView(convertView, R.id.company_name, CustomFontTextView.class);
            holder.mTickerSymbolTextView = (CustomFontTextView) ViewUtils.getValidView(convertView, R.id.company_symbol, CustomFontTextView.class);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        StockCompanyInformation stockCompany = getItem(position);
        holder.mNameTextView.setText(stockCompany.getCompanyName());
        holder.mTickerSymbolTextView.setText(stockCompany.getTickerSymbol());
        return convertView;
    }
}
