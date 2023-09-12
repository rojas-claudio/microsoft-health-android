package com.microsoft.kapp.services.finance;

import android.content.Context;
import android.text.TextUtils;
import com.google.gson.annotations.SerializedName;
import com.microsoft.kapp.R;
import com.microsoft.kapp.style.utils.MetricSpannerUtils;
/* loaded from: classes.dex */
public class StockCompanyInformation {
    @SerializedName("FullInstrument")
    private String mBingValue;
    @SerializedName("OS01W")
    private String mCompanyName;
    @SerializedName("DisplayName")
    private String mDisplayName;
    @SerializedName("AC040")
    private String mExchange;
    @SerializedName("OS001")
    private String mTickerSymbol;
    @SerializedName("OS010")
    private String mType;

    public StockCompanyInformation() {
    }

    public StockCompanyInformation(String companyName, String tickerSymbol, String exchange, String type, String bingValue) {
        this.mCompanyName = companyName;
        this.mTickerSymbol = tickerSymbol;
        this.mExchange = exchange;
        this.mType = type;
        this.mBingValue = bingValue;
    }

    public StockCompanyInformation(String tickerSymbol, String bingValue) {
        this.mTickerSymbol = tickerSymbol;
        this.mBingValue = bingValue;
    }

    public void setCompanyName(String companyName) {
        this.mCompanyName = companyName;
    }

    public String getCompanyName() {
        return this.mCompanyName;
    }

    public String getTickerSymbol() {
        return (TextUtils.isEmpty(this.mType) || !this.mType.equals("XI")) ? this.mTickerSymbol : this.mDisplayName;
    }

    public String getExchange() {
        return this.mExchange;
    }

    public String getSubtitle(Context context) {
        if (this.mType != null) {
            if (this.mType.equals("ST")) {
                return context.getResources().getString(R.string.settings_finance_stock_name);
            }
            if (this.mType.equals("FO")) {
                return context.getResources().getString(R.string.settings_finance_mutual_fund_name);
            }
            if (this.mType.equals("FE")) {
                return context.getResources().getString(R.string.settings_finance_etf_name);
            }
            if (this.mType.equals("XI")) {
                return context.getResources().getString(R.string.settings_finance_index_name);
            }
        }
        return MetricSpannerUtils.METRIC_UNITS_SEPARATOR_SPACE;
    }

    public String getBingValue() {
        return this.mBingValue;
    }
}
