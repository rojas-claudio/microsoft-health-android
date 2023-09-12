package com.google.ads.mediation;

import android.location.Location;
import com.google.ads.AdRequest;
import java.util.Date;
import java.util.Set;
@Deprecated
/* loaded from: classes.dex */
public final class MediationAdRequest {
    private final Date d;
    private final AdRequest.Gender e;
    private final Set<String> f;
    private final boolean g;

    public MediationAdRequest(Date birthday, AdRequest.Gender gender, Set<String> keywords, boolean isTesting) {
        this.d = birthday;
        this.e = gender;
        this.f = keywords;
        this.g = isTesting;
    }

    public Integer getAgeInYears() {
        return null;
    }

    public Date getBirthday() {
        return this.d;
    }

    public AdRequest.Gender getGender() {
        return this.e;
    }

    public Set<String> getKeywords() {
        return this.f;
    }

    public Location getLocation() {
        return null;
    }

    public boolean isTesting() {
        return this.g;
    }
}
