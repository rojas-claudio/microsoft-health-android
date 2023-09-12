package com.microsoft.kapp.models;
/* loaded from: classes.dex */
public class StatData {
    private int mGlyph;
    private String mLeftSubTitle;
    private String mLeftSubValue;
    private String mRightSubTitle;
    private String mRightSubValue;
    private String mTitle;
    private String mValue;

    public StatData(int glyph, String title, String value) {
        this.mGlyph = glyph;
        this.mTitle = title;
        this.mValue = value;
    }

    public int getmGlyph() {
        return this.mGlyph;
    }

    public String getmTitle() {
        return this.mTitle;
    }

    public String getmValue() {
        return this.mValue;
    }

    public String getmLeftSubTitle() {
        return this.mLeftSubTitle;
    }

    public String getmLeftSubValue() {
        return this.mLeftSubValue;
    }

    public String getmRightSubTitle() {
        return this.mRightSubTitle;
    }

    public String getmRightSubValue() {
        return this.mRightSubValue;
    }

    public void setLeftSubStat(String leftSubTitle, String leftSubValue) {
        this.mLeftSubTitle = leftSubTitle;
        this.mLeftSubValue = leftSubValue;
    }

    public void setRightSubStat(String rightSubTitle, String rightSubValue) {
        this.mRightSubTitle = rightSubTitle;
        this.mRightSubValue = rightSubValue;
    }
}
