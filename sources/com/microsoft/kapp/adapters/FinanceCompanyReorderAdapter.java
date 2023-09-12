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
import com.microsoft.kapp.views.CustomGlyphView;
import java.util.HashMap;
import java.util.List;
/* loaded from: classes.dex */
public class FinanceCompanyReorderAdapter extends ArrayAdapter<StockCompanyInformation> {
    private static final int RESOURCE_ID = 2130903078;
    final int INVALID_ID;
    private HashMap<StockCompanyInformation, Integer> mCompanyIDs;
    private RemoveCompanyListener mRemoveCompanyListener;

    /* loaded from: classes.dex */
    public interface RemoveCompanyListener {
        void removeCompany(StockCompanyInformation stockCompanyInformation);
    }

    /* loaded from: classes.dex */
    private static class ViewHolder {
        private CustomFontTextView mNameTextView;
        private CustomGlyphView mRemoveIcon;
        private CustomFontTextView mTickerSymbolTextView;

        private ViewHolder() {
        }
    }

    public FinanceCompanyReorderAdapter(Context context, List<StockCompanyInformation> stockCompanies) {
        super(context, (int) R.layout.adapter_settings_stock_reorder, stockCompanies);
        this.INVALID_ID = -1;
        this.mCompanyIDs = new HashMap<>();
        for (int i = 0; i < stockCompanies.size(); i++) {
            this.mCompanyIDs.put(stockCompanies.get(i), Integer.valueOf(i));
        }
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public long getItemId(int position) {
        if (position < 0 || position >= this.mCompanyIDs.size()) {
            return -1L;
        }
        StockCompanyInformation item = getItem(position);
        return this.mCompanyIDs.get(item).intValue();
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public boolean hasStableIds() {
        return true;
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService("layout_inflater");
            convertView = inflater.inflate(R.layout.adapter_settings_stock_reorder, (ViewGroup) null);
            holder = new ViewHolder();
            holder.mNameTextView = (CustomFontTextView) ViewUtils.getValidView(convertView, R.id.company_name, CustomFontTextView.class);
            holder.mTickerSymbolTextView = (CustomFontTextView) ViewUtils.getValidView(convertView, R.id.company_ticker_symbol, CustomFontTextView.class);
            holder.mRemoveIcon = (CustomGlyphView) ViewUtils.getValidView(convertView, R.id.company_remove_icon, CustomGlyphView.class);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final StockCompanyInformation stockCompany = getItem(position);
        holder.mNameTextView.setText(stockCompany.getCompanyName());
        holder.mTickerSymbolTextView.setText(stockCompany.getTickerSymbol());
        holder.mRemoveIcon.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.adapters.FinanceCompanyReorderAdapter.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (FinanceCompanyReorderAdapter.this.mRemoveCompanyListener != null) {
                    FinanceCompanyReorderAdapter.this.mRemoveCompanyListener.removeCompany(stockCompany);
                }
            }
        });
        return convertView;
    }

    public void setOnRemoveCompanyListener(RemoveCompanyListener listener) {
        this.mRemoveCompanyListener = listener;
    }
}
