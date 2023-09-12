package com.google.android.gms.ads;

import android.content.Context;
import com.google.android.gms.ads.mediation.NetworkExtras;
import com.google.android.gms.internal.cm;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
/* loaded from: classes.dex */
public final class AdRequest {
    public static final String DEVICE_ID_EMULATOR = cm.l("emulator");
    public static final int ERROR_CODE_INTERNAL_ERROR = 0;
    public static final int ERROR_CODE_INVALID_REQUEST = 1;
    public static final int ERROR_CODE_NETWORK_ERROR = 2;
    public static final int ERROR_CODE_NO_FILL = 3;
    public static final int GENDER_FEMALE = 2;
    public static final int GENDER_MALE = 1;
    public static final int GENDER_UNKNOWN = 0;
    private final Date d;
    private final int dI;
    private final Map<Class<? extends NetworkExtras>, NetworkExtras> dJ;
    private final int dK;
    private final Set<String> dL;
    private final Set<String> f;

    /* loaded from: classes.dex */
    public static final class Builder {
        private Date d;
        private final HashSet<String> dM = new HashSet<>();
        private final HashMap<Class<? extends NetworkExtras>, NetworkExtras> dN = new HashMap<>();
        private final HashSet<String> dO = new HashSet<>();
        private int dI = -1;
        private int dK = -1;

        public Builder addKeyword(String keyword) {
            this.dM.add(keyword);
            return this;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public Builder addNetworkExtras(NetworkExtras networkExtras) {
            this.dN.put(networkExtras.getClass(), networkExtras);
            return this;
        }

        public Builder addTestDevice(String deviceId) {
            this.dO.add(deviceId);
            return this;
        }

        public AdRequest build() {
            return new AdRequest(this);
        }

        public Builder setBirthday(Date birthday) {
            this.d = birthday;
            return this;
        }

        public Builder setGender(int gender) {
            this.dI = gender;
            return this;
        }

        public Builder tagForChildDirectedTreatment(boolean tagForChildDirectedTreatment) {
            this.dK = tagForChildDirectedTreatment ? 1 : 0;
            return this;
        }
    }

    private AdRequest(Builder builder) {
        this.d = builder.d;
        this.dI = builder.dI;
        this.f = Collections.unmodifiableSet(builder.dM);
        this.dJ = Collections.unmodifiableMap(builder.dN);
        this.dK = builder.dK;
        this.dL = Collections.unmodifiableSet(builder.dO);
    }

    public Date getBirthday() {
        return this.d;
    }

    public int getGender() {
        return this.dI;
    }

    public Set<String> getKeywords() {
        return this.f;
    }

    public <T extends NetworkExtras> T getNetworkExtras(Class<T> networkExtrasClass) {
        return (T) this.dJ.get(networkExtrasClass);
    }

    public boolean isTestDevice(Context context) {
        return this.dL.contains(cm.l(context));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Map<Class<? extends NetworkExtras>, NetworkExtras> v() {
        return this.dJ;
    }

    public int w() {
        return this.dK;
    }
}
