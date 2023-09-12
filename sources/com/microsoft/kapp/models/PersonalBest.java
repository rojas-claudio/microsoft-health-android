package com.microsoft.kapp.models;

import android.text.Spannable;
/* loaded from: classes.dex */
public class PersonalBest {
    private boolean mHasBestValue;
    private String mId;
    private String mPersonalBestDate;
    private Spannable mPersonalBestValue;

    public PersonalBest(String personalBestKey, Spannable personalBestValue, String value) {
        this(personalBestKey, personalBestValue, true, value);
    }

    public PersonalBest(String personalBestKey, Spannable personalBestValue, boolean hasBestValue, String value) {
        this.mPersonalBestDate = personalBestKey;
        this.mPersonalBestValue = personalBestValue;
        this.mHasBestValue = hasBestValue;
        this.mId = value;
    }

    public Spannable getPersonalBestValue() {
        return this.mPersonalBestValue;
    }

    public void setPersonalBestValue(Spannable personalBestValue) {
        this.mPersonalBestValue = personalBestValue;
    }

    public String getPersonalBestDate() {
        return this.mPersonalBestDate;
    }

    public void setPersonalBestDate(String personalBestDate) {
        this.mPersonalBestDate = personalBestDate;
    }

    public String getId() {
        return this.mId;
    }

    public void setId(String value) {
        this.mId = value;
    }

    public boolean hasBestValue() {
        return this.mHasBestValue;
    }
}
