package com.google.android.gms.internal;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.mediation.MediationAdRequest;
import java.util.Date;
import java.util.HashSet;
/* loaded from: classes.dex */
public final class bb {
    public static int a(AdRequest.ErrorCode errorCode) {
        switch (errorCode) {
            case INVALID_REQUEST:
                return 1;
            case NETWORK_ERROR:
                return 2;
            case NO_FILL:
                return 3;
            default:
                return 0;
        }
    }

    public static int a(AdRequest.Gender gender) {
        switch (gender) {
            case FEMALE:
                return 2;
            case MALE:
                return 1;
            default:
                return 0;
        }
    }

    public static AdSize a(x xVar) {
        return new AdSize(new com.google.android.gms.ads.AdSize(xVar.width, xVar.height, xVar.ew));
    }

    public static AdRequest.Gender e(int i) {
        switch (i) {
            case 1:
                return AdRequest.Gender.MALE;
            case 2:
                return AdRequest.Gender.FEMALE;
            default:
                return AdRequest.Gender.UNKNOWN;
        }
    }

    public static MediationAdRequest e(v vVar) {
        return new MediationAdRequest(new Date(vVar.es), e(vVar.et), vVar.eu != null ? new HashSet(vVar.eu) : null, vVar.ev);
    }

    public static final AdRequest.ErrorCode f(int i) {
        switch (i) {
            case 1:
                return AdRequest.ErrorCode.INVALID_REQUEST;
            case 2:
                return AdRequest.ErrorCode.NETWORK_ERROR;
            case 3:
                return AdRequest.ErrorCode.NO_FILL;
            default:
                return AdRequest.ErrorCode.INTERNAL_ERROR;
        }
    }
}
