package com.microsoft.kapp.models;

import com.microsoft.band.build.BranchInfo;
import java.util.Locale;
/* loaded from: classes.dex */
public enum CloudEnvironment {
    KMAIN("https://kmain-kds-eus2-0.cloudapp.net/", "kmain-kds-eus2-0.cloudapp.net"),
    PROD("https://prodkds.dns-cargo.com/", "prodkds.dns-cargo.com"),
    STG("https://stgkds.dns-cargo.com/", "stgkds.dns-cargo.com"),
    INT("https://intkds.dns-cargo.com/", "intkds.dns-cargo.com"),
    KTEST1("https://ktest1-kds-eus2-0.cloudapp.net", "ktest1-kds-eus2-0.cloudapp.net"),
    KTEST2("https://ktest1-kds-eus2-0.cloudapp.net", "ktest1-kds-eus2-0.cloudapp.net");
    
    private String mRealm;
    private String mUrl;

    CloudEnvironment(String url, String realm) {
        this.mUrl = url;
        this.mRealm = realm;
    }

    public String getUrl() {
        return this.mUrl;
    }

    public String getRealm() {
        return this.mRealm;
    }

    public static CloudEnvironment getDefault() {
        String enumName = BranchInfo.DefaultCloudEnvironment.toUpperCase(Locale.getDefault());
        try {
            return valueOf(enumName);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Could not resolve default cloud environment " + BranchInfo.DefaultCloudEnvironment + ". Make sure the name is valid and that there is an entry for that environment.", e);
        }
    }
}
