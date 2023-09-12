package com.microsoft.kapp.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.services.finance.StockCompanyInformation;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class FinanceCompanyAddAdapter extends ArrayAdapter<StockCompanyInformation> {
    private static final int RESOURCE_ID = 2130903076;
    private final ArrayList<StockCompanyInformation> data;
    private final Activity mActivity;

    /* loaded from: classes.dex */
    static class ViewHolder {
        TextView companyExchange;
        TextView companyName;
        TextView companyTickerSymbol;
        TextView companyType;

        ViewHolder() {
        }
    }

    public FinanceCompanyAddAdapter(Activity activity, ArrayList<StockCompanyInformation> data) {
        super(activity, (int) R.layout.adapter_settings_stock_lookup, data);
        this.mActivity = activity;
        this.data = data;
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = this.mActivity.getLayoutInflater();
            row = inflater.inflate(R.layout.adapter_settings_stock_lookup, parent, false);
            holder = new ViewHolder();
            holder.companyName = (TextView) row.findViewById(R.id.stock_company_name);
            holder.companyType = (TextView) row.findViewById(R.id.stock_company_type);
            holder.companyExchange = (TextView) row.findViewById(R.id.stock_company_exchange);
            holder.companyTickerSymbol = (TextView) row.findViewById(R.id.stock_company_ticker_symbol);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        StockCompanyInformation company = this.data.get(position);
        holder.companyName.setText(company.getCompanyName());
        holder.companyType.setText(company.getSubtitle(getContext()));
        holder.companyExchange.setText(company.getExchange());
        holder.companyTickerSymbol.setText(company.getTickerSymbol());
        return row;
    }
}
