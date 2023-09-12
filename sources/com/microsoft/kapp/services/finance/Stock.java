package com.microsoft.kapp.services.finance;
/* loaded from: classes.dex */
public class Stock {
    private int mChange;
    private String mCompanyName;
    private String mSymbol;
    private int mValue;

    public Stock(String symbol, int value, int change, String companyName) {
        this.mSymbol = symbol;
        this.mValue = value;
        this.mChange = change;
        this.mCompanyName = companyName;
    }

    public int getValue() {
        return this.mValue;
    }

    public void setValue(int value) {
        this.mValue = value;
    }

    public int getChange() {
        return this.mChange;
    }

    public void setChange(int change) {
        this.mChange = change;
    }

    public String getSymbol() {
        return this.mSymbol;
    }

    public void setSymbol(String symbol) {
        this.mSymbol = symbol;
    }

    public void setCompanyName(String companyName) {
        this.mCompanyName = companyName;
    }

    public String getCompanyName() {
        return this.mCompanyName.length() > 8 ? this.mCompanyName.substring(0, 8) : this.mCompanyName;
    }
}
