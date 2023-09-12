package com.microsoft.kapp.models.golf;
/* loaded from: classes.dex */
public class ScorecardItem {
    private ScorecardItemDetails mDetails;
    private int mDistance;
    private int mHole;
    private String mHoleLabel;
    private int mIndex;
    private String mIndexLabel;
    private ScorecardItemType mItemtype;
    private int mPar;
    private int mScore;
    private ScorecardScoreState mScoreState;

    /* loaded from: classes.dex */
    public enum ScorecardItemType {
        OUT,
        IN,
        TOT,
        HOLE
    }

    /* loaded from: classes.dex */
    public enum ScorecardScoreState {
        UNDER_3,
        UNDER_2,
        UNDER_1,
        PAR,
        OVER_1,
        OVER_2,
        OVER_3
    }

    public ScorecardItemType getItemtype() {
        return this.mItemtype;
    }

    public void setItemtype(ScorecardItemType mItemtype) {
        this.mItemtype = mItemtype;
    }

    public int getHole() {
        return this.mHole;
    }

    public void setHole(int mHole) {
        this.mHole = mHole;
    }

    public int getDistance() {
        return this.mDistance;
    }

    public void setDistance(int mYards) {
        this.mDistance = mYards;
    }

    public int getPar() {
        return this.mPar;
    }

    public void setPar(int mPar) {
        this.mPar = mPar;
    }

    public int getIndex() {
        return this.mIndex;
    }

    public void setIndex(int mIndex) {
        this.mIndex = mIndex;
    }

    public int getScore() {
        return this.mScore;
    }

    public void setScore(int mScore) {
        this.mScore = mScore;
    }

    public ScorecardItemDetails getDetails() {
        return this.mDetails;
    }

    public void setDetails(ScorecardItemDetails mDetails) {
        this.mDetails = mDetails;
    }

    public String getHoleLabel() {
        return this.mHoleLabel;
    }

    public void setHoleLabel(String mHoleLabel) {
        this.mHoleLabel = mHoleLabel;
    }

    public String getIndexLabel() {
        return this.mIndexLabel;
    }

    public void setIndexLabel(String mIndexLabel) {
        this.mIndexLabel = mIndexLabel;
    }

    public ScorecardScoreState getScoreState() {
        return this.mScoreState;
    }

    public void setScoreState(ScorecardScoreState mScoreState) {
        this.mScoreState = mScoreState;
    }
}
