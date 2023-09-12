package com.google.android.gms.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.dw;
import com.google.android.gms.plus.PlusShare;
import com.google.android.gms.plus.model.people.Person;
import com.microsoft.kapp.logging.LogConstants;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutSummary;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
/* loaded from: classes.dex */
public final class fv extends dw implements SafeParcelable, Person {
    public static final fw CREATOR = new fw();
    private static final HashMap<String, dw.a<?, ?>> rH = new HashMap<>();
    private int dI;
    private String hN;
    private final int iM;
    private String ml;
    private final Set<Integer> rI;
    private String sJ;
    private a sK;
    private String sL;
    private String sM;
    private int sN;
    private b sO;
    private String sP;
    private c sQ;
    private boolean sR;
    private String sS;
    private d sT;
    private String sU;
    private int sV;
    private List<f> sW;
    private List<g> sX;
    private int sY;
    private int sZ;
    private String sm;
    private String ta;
    private List<h> tb;
    private boolean tc;

    /* loaded from: classes.dex */
    public static final class a extends dw implements SafeParcelable, Person.AgeRange {
        public static final fx CREATOR = new fx();
        private static final HashMap<String, dw.a<?, ?>> rH = new HashMap<>();
        private final int iM;
        private final Set<Integer> rI;
        private int td;
        private int te;

        static {
            rH.put("max", dw.a.d("max", 2));
            rH.put("min", dw.a.d("min", 3));
        }

        public a() {
            this.iM = 1;
            this.rI = new HashSet();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public a(Set<Integer> set, int i, int i2, int i3) {
            this.rI = set;
            this.iM = i;
            this.td = i2;
            this.te = i3;
        }

        @Override // com.google.android.gms.internal.dw
        protected Object D(String str) {
            return null;
        }

        @Override // com.google.android.gms.internal.dw
        protected boolean E(String str) {
            return false;
        }

        @Override // com.google.android.gms.internal.dw
        protected boolean a(dw.a aVar) {
            return this.rI.contains(Integer.valueOf(aVar.bw()));
        }

        @Override // com.google.android.gms.internal.dw
        protected Object b(dw.a aVar) {
            switch (aVar.bw()) {
                case 2:
                    return Integer.valueOf(this.td);
                case 3:
                    return Integer.valueOf(this.te);
                default:
                    throw new IllegalStateException("Unknown safe parcelable id=" + aVar.bw());
            }
        }

        @Override // com.google.android.gms.internal.dw
        public HashMap<String, dw.a<?, ?>> bp() {
            return rH;
        }

        @Override // com.google.android.gms.common.data.Freezable
        /* renamed from: dL */
        public a freeze() {
            return this;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            fx fxVar = CREATOR;
            return 0;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Set<Integer> di() {
            return this.rI;
        }

        public boolean equals(Object obj) {
            if (obj instanceof a) {
                if (this == obj) {
                    return true;
                }
                a aVar = (a) obj;
                for (dw.a<?, ?> aVar2 : rH.values()) {
                    if (a(aVar2)) {
                        if (aVar.a(aVar2) && b(aVar2).equals(aVar.b(aVar2))) {
                        }
                        return false;
                    } else if (aVar.a(aVar2)) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }

        @Override // com.google.android.gms.plus.model.people.Person.AgeRange
        public int getMax() {
            return this.td;
        }

        @Override // com.google.android.gms.plus.model.people.Person.AgeRange
        public int getMin() {
            return this.te;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public int getVersionCode() {
            return this.iM;
        }

        @Override // com.google.android.gms.plus.model.people.Person.AgeRange
        public boolean hasMax() {
            return this.rI.contains(2);
        }

        @Override // com.google.android.gms.plus.model.people.Person.AgeRange
        public boolean hasMin() {
            return this.rI.contains(3);
        }

        public int hashCode() {
            int i = 0;
            Iterator<dw.a<?, ?>> it = rH.values().iterator();
            while (true) {
                int i2 = i;
                if (!it.hasNext()) {
                    return i2;
                }
                dw.a<?, ?> next = it.next();
                if (a(next)) {
                    i = b(next).hashCode() + i2 + next.bw();
                } else {
                    i = i2;
                }
            }
        }

        @Override // com.google.android.gms.common.data.Freezable
        public boolean isDataValid() {
            return true;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel out, int flags) {
            fx fxVar = CREATOR;
            fx.a(this, out, flags);
        }
    }

    /* loaded from: classes.dex */
    public static final class b extends dw implements SafeParcelable, Person.Cover {
        public static final fy CREATOR = new fy();
        private static final HashMap<String, dw.a<?, ?>> rH = new HashMap<>();
        private final int iM;
        private final Set<Integer> rI;
        private a tf;
        private C0033b tg;
        private int th;

        /* loaded from: classes.dex */
        public static final class a extends dw implements SafeParcelable, Person.Cover.CoverInfo {
            public static final fz CREATOR = new fz();
            private static final HashMap<String, dw.a<?, ?>> rH = new HashMap<>();
            private final int iM;
            private final Set<Integer> rI;
            private int ti;
            private int tj;

            static {
                rH.put("leftImageOffset", dw.a.d("leftImageOffset", 2));
                rH.put("topImageOffset", dw.a.d("topImageOffset", 3));
            }

            public a() {
                this.iM = 1;
                this.rI = new HashSet();
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            public a(Set<Integer> set, int i, int i2, int i3) {
                this.rI = set;
                this.iM = i;
                this.ti = i2;
                this.tj = i3;
            }

            @Override // com.google.android.gms.internal.dw
            protected Object D(String str) {
                return null;
            }

            @Override // com.google.android.gms.internal.dw
            protected boolean E(String str) {
                return false;
            }

            @Override // com.google.android.gms.internal.dw
            protected boolean a(dw.a aVar) {
                return this.rI.contains(Integer.valueOf(aVar.bw()));
            }

            @Override // com.google.android.gms.internal.dw
            protected Object b(dw.a aVar) {
                switch (aVar.bw()) {
                    case 2:
                        return Integer.valueOf(this.ti);
                    case 3:
                        return Integer.valueOf(this.tj);
                    default:
                        throw new IllegalStateException("Unknown safe parcelable id=" + aVar.bw());
                }
            }

            @Override // com.google.android.gms.internal.dw
            public HashMap<String, dw.a<?, ?>> bp() {
                return rH;
            }

            @Override // com.google.android.gms.common.data.Freezable
            /* renamed from: dP */
            public a freeze() {
                return this;
            }

            @Override // android.os.Parcelable
            public int describeContents() {
                fz fzVar = CREATOR;
                return 0;
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            public Set<Integer> di() {
                return this.rI;
            }

            public boolean equals(Object obj) {
                if (obj instanceof a) {
                    if (this == obj) {
                        return true;
                    }
                    a aVar = (a) obj;
                    for (dw.a<?, ?> aVar2 : rH.values()) {
                        if (a(aVar2)) {
                            if (aVar.a(aVar2) && b(aVar2).equals(aVar.b(aVar2))) {
                            }
                            return false;
                        } else if (aVar.a(aVar2)) {
                            return false;
                        }
                    }
                    return true;
                }
                return false;
            }

            @Override // com.google.android.gms.plus.model.people.Person.Cover.CoverInfo
            public int getLeftImageOffset() {
                return this.ti;
            }

            @Override // com.google.android.gms.plus.model.people.Person.Cover.CoverInfo
            public int getTopImageOffset() {
                return this.tj;
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            public int getVersionCode() {
                return this.iM;
            }

            @Override // com.google.android.gms.plus.model.people.Person.Cover.CoverInfo
            public boolean hasLeftImageOffset() {
                return this.rI.contains(2);
            }

            @Override // com.google.android.gms.plus.model.people.Person.Cover.CoverInfo
            public boolean hasTopImageOffset() {
                return this.rI.contains(3);
            }

            public int hashCode() {
                int i = 0;
                Iterator<dw.a<?, ?>> it = rH.values().iterator();
                while (true) {
                    int i2 = i;
                    if (!it.hasNext()) {
                        return i2;
                    }
                    dw.a<?, ?> next = it.next();
                    if (a(next)) {
                        i = b(next).hashCode() + i2 + next.bw();
                    } else {
                        i = i2;
                    }
                }
            }

            @Override // com.google.android.gms.common.data.Freezable
            public boolean isDataValid() {
                return true;
            }

            @Override // android.os.Parcelable
            public void writeToParcel(Parcel out, int flags) {
                fz fzVar = CREATOR;
                fz.a(this, out, flags);
            }
        }

        /* renamed from: com.google.android.gms.internal.fv$b$b  reason: collision with other inner class name */
        /* loaded from: classes.dex */
        public static final class C0033b extends dw implements SafeParcelable, Person.Cover.CoverPhoto {
            public static final ga CREATOR = new ga();
            private static final HashMap<String, dw.a<?, ?>> rH = new HashMap<>();
            private int dP;
            private int dQ;
            private String hN;
            private final int iM;
            private final Set<Integer> rI;

            static {
                rH.put("height", dw.a.d("height", 2));
                rH.put("url", dw.a.g("url", 3));
                rH.put("width", dw.a.d("width", 4));
            }

            public C0033b() {
                this.iM = 1;
                this.rI = new HashSet();
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            public C0033b(Set<Integer> set, int i, int i2, String str, int i3) {
                this.rI = set;
                this.iM = i;
                this.dQ = i2;
                this.hN = str;
                this.dP = i3;
            }

            @Override // com.google.android.gms.internal.dw
            protected Object D(String str) {
                return null;
            }

            @Override // com.google.android.gms.internal.dw
            protected boolean E(String str) {
                return false;
            }

            @Override // com.google.android.gms.internal.dw
            protected boolean a(dw.a aVar) {
                return this.rI.contains(Integer.valueOf(aVar.bw()));
            }

            @Override // com.google.android.gms.internal.dw
            protected Object b(dw.a aVar) {
                switch (aVar.bw()) {
                    case 2:
                        return Integer.valueOf(this.dQ);
                    case 3:
                        return this.hN;
                    case 4:
                        return Integer.valueOf(this.dP);
                    default:
                        throw new IllegalStateException("Unknown safe parcelable id=" + aVar.bw());
                }
            }

            @Override // com.google.android.gms.internal.dw
            public HashMap<String, dw.a<?, ?>> bp() {
                return rH;
            }

            @Override // com.google.android.gms.common.data.Freezable
            /* renamed from: dQ */
            public C0033b freeze() {
                return this;
            }

            @Override // android.os.Parcelable
            public int describeContents() {
                ga gaVar = CREATOR;
                return 0;
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            public Set<Integer> di() {
                return this.rI;
            }

            public boolean equals(Object obj) {
                if (obj instanceof C0033b) {
                    if (this == obj) {
                        return true;
                    }
                    C0033b c0033b = (C0033b) obj;
                    for (dw.a<?, ?> aVar : rH.values()) {
                        if (a(aVar)) {
                            if (c0033b.a(aVar) && b(aVar).equals(c0033b.b(aVar))) {
                            }
                            return false;
                        } else if (c0033b.a(aVar)) {
                            return false;
                        }
                    }
                    return true;
                }
                return false;
            }

            @Override // com.google.android.gms.plus.model.people.Person.Cover.CoverPhoto
            public int getHeight() {
                return this.dQ;
            }

            @Override // com.google.android.gms.plus.model.people.Person.Cover.CoverPhoto
            public String getUrl() {
                return this.hN;
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            public int getVersionCode() {
                return this.iM;
            }

            @Override // com.google.android.gms.plus.model.people.Person.Cover.CoverPhoto
            public int getWidth() {
                return this.dP;
            }

            @Override // com.google.android.gms.plus.model.people.Person.Cover.CoverPhoto
            public boolean hasHeight() {
                return this.rI.contains(2);
            }

            @Override // com.google.android.gms.plus.model.people.Person.Cover.CoverPhoto
            public boolean hasUrl() {
                return this.rI.contains(3);
            }

            @Override // com.google.android.gms.plus.model.people.Person.Cover.CoverPhoto
            public boolean hasWidth() {
                return this.rI.contains(4);
            }

            public int hashCode() {
                int i = 0;
                Iterator<dw.a<?, ?>> it = rH.values().iterator();
                while (true) {
                    int i2 = i;
                    if (!it.hasNext()) {
                        return i2;
                    }
                    dw.a<?, ?> next = it.next();
                    if (a(next)) {
                        i = b(next).hashCode() + i2 + next.bw();
                    } else {
                        i = i2;
                    }
                }
            }

            @Override // com.google.android.gms.common.data.Freezable
            public boolean isDataValid() {
                return true;
            }

            @Override // android.os.Parcelable
            public void writeToParcel(Parcel out, int flags) {
                ga gaVar = CREATOR;
                ga.a(this, out, flags);
            }
        }

        static {
            rH.put("coverInfo", dw.a.a("coverInfo", 2, a.class));
            rH.put("coverPhoto", dw.a.a("coverPhoto", 3, C0033b.class));
            rH.put("layout", dw.a.a("layout", 4, new dt().c("banner", 0), false));
        }

        public b() {
            this.iM = 1;
            this.rI = new HashSet();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public b(Set<Integer> set, int i, a aVar, C0033b c0033b, int i2) {
            this.rI = set;
            this.iM = i;
            this.tf = aVar;
            this.tg = c0033b;
            this.th = i2;
        }

        @Override // com.google.android.gms.internal.dw
        protected Object D(String str) {
            return null;
        }

        @Override // com.google.android.gms.internal.dw
        protected boolean E(String str) {
            return false;
        }

        @Override // com.google.android.gms.internal.dw
        protected boolean a(dw.a aVar) {
            return this.rI.contains(Integer.valueOf(aVar.bw()));
        }

        @Override // com.google.android.gms.internal.dw
        protected Object b(dw.a aVar) {
            switch (aVar.bw()) {
                case 2:
                    return this.tf;
                case 3:
                    return this.tg;
                case 4:
                    return Integer.valueOf(this.th);
                default:
                    throw new IllegalStateException("Unknown safe parcelable id=" + aVar.bw());
            }
        }

        @Override // com.google.android.gms.internal.dw
        public HashMap<String, dw.a<?, ?>> bp() {
            return rH;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public a dM() {
            return this.tf;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public C0033b dN() {
            return this.tg;
        }

        @Override // com.google.android.gms.common.data.Freezable
        /* renamed from: dO */
        public b freeze() {
            return this;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            fy fyVar = CREATOR;
            return 0;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Set<Integer> di() {
            return this.rI;
        }

        public boolean equals(Object obj) {
            if (obj instanceof b) {
                if (this == obj) {
                    return true;
                }
                b bVar = (b) obj;
                for (dw.a<?, ?> aVar : rH.values()) {
                    if (a(aVar)) {
                        if (bVar.a(aVar) && b(aVar).equals(bVar.b(aVar))) {
                        }
                        return false;
                    } else if (bVar.a(aVar)) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }

        @Override // com.google.android.gms.plus.model.people.Person.Cover
        public Person.Cover.CoverInfo getCoverInfo() {
            return this.tf;
        }

        @Override // com.google.android.gms.plus.model.people.Person.Cover
        public Person.Cover.CoverPhoto getCoverPhoto() {
            return this.tg;
        }

        @Override // com.google.android.gms.plus.model.people.Person.Cover
        public int getLayout() {
            return this.th;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public int getVersionCode() {
            return this.iM;
        }

        @Override // com.google.android.gms.plus.model.people.Person.Cover
        public boolean hasCoverInfo() {
            return this.rI.contains(2);
        }

        @Override // com.google.android.gms.plus.model.people.Person.Cover
        public boolean hasCoverPhoto() {
            return this.rI.contains(3);
        }

        @Override // com.google.android.gms.plus.model.people.Person.Cover
        public boolean hasLayout() {
            return this.rI.contains(4);
        }

        public int hashCode() {
            int i = 0;
            Iterator<dw.a<?, ?>> it = rH.values().iterator();
            while (true) {
                int i2 = i;
                if (!it.hasNext()) {
                    return i2;
                }
                dw.a<?, ?> next = it.next();
                if (a(next)) {
                    i = b(next).hashCode() + i2 + next.bw();
                } else {
                    i = i2;
                }
            }
        }

        @Override // com.google.android.gms.common.data.Freezable
        public boolean isDataValid() {
            return true;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel out, int flags) {
            fy fyVar = CREATOR;
            fy.a(this, out, flags);
        }
    }

    /* loaded from: classes.dex */
    public static final class c extends dw implements SafeParcelable, Person.Image {
        public static final gb CREATOR = new gb();
        private static final HashMap<String, dw.a<?, ?>> rH = new HashMap<>();
        private String hN;
        private final int iM;
        private final Set<Integer> rI;

        static {
            rH.put("url", dw.a.g("url", 2));
        }

        public c() {
            this.iM = 1;
            this.rI = new HashSet();
        }

        public c(String str) {
            this.rI = new HashSet();
            this.iM = 1;
            this.hN = str;
            this.rI.add(2);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public c(Set<Integer> set, int i, String str) {
            this.rI = set;
            this.iM = i;
            this.hN = str;
        }

        @Override // com.google.android.gms.internal.dw
        protected Object D(String str) {
            return null;
        }

        @Override // com.google.android.gms.internal.dw
        protected boolean E(String str) {
            return false;
        }

        @Override // com.google.android.gms.internal.dw
        protected boolean a(dw.a aVar) {
            return this.rI.contains(Integer.valueOf(aVar.bw()));
        }

        @Override // com.google.android.gms.internal.dw
        protected Object b(dw.a aVar) {
            switch (aVar.bw()) {
                case 2:
                    return this.hN;
                default:
                    throw new IllegalStateException("Unknown safe parcelable id=" + aVar.bw());
            }
        }

        @Override // com.google.android.gms.internal.dw
        public HashMap<String, dw.a<?, ?>> bp() {
            return rH;
        }

        @Override // com.google.android.gms.common.data.Freezable
        /* renamed from: dR */
        public c freeze() {
            return this;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            gb gbVar = CREATOR;
            return 0;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Set<Integer> di() {
            return this.rI;
        }

        public boolean equals(Object obj) {
            if (obj instanceof c) {
                if (this == obj) {
                    return true;
                }
                c cVar = (c) obj;
                for (dw.a<?, ?> aVar : rH.values()) {
                    if (a(aVar)) {
                        if (cVar.a(aVar) && b(aVar).equals(cVar.b(aVar))) {
                        }
                        return false;
                    } else if (cVar.a(aVar)) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }

        @Override // com.google.android.gms.plus.model.people.Person.Image
        public String getUrl() {
            return this.hN;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public int getVersionCode() {
            return this.iM;
        }

        @Override // com.google.android.gms.plus.model.people.Person.Image
        public boolean hasUrl() {
            return this.rI.contains(2);
        }

        public int hashCode() {
            int i = 0;
            Iterator<dw.a<?, ?>> it = rH.values().iterator();
            while (true) {
                int i2 = i;
                if (!it.hasNext()) {
                    return i2;
                }
                dw.a<?, ?> next = it.next();
                if (a(next)) {
                    i = b(next).hashCode() + i2 + next.bw();
                } else {
                    i = i2;
                }
            }
        }

        @Override // com.google.android.gms.common.data.Freezable
        public boolean isDataValid() {
            return true;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel out, int flags) {
            gb gbVar = CREATOR;
            gb.a(this, out, flags);
        }
    }

    /* loaded from: classes.dex */
    public static final class d extends dw implements SafeParcelable, Person.Name {
        public static final gc CREATOR = new gc();
        private static final HashMap<String, dw.a<?, ?>> rH = new HashMap<>();
        private final int iM;
        private final Set<Integer> rI;
        private String sh;
        private String sk;
        private String tk;
        private String tl;
        private String tm;
        private String tn;

        static {
            rH.put("familyName", dw.a.g("familyName", 2));
            rH.put("formatted", dw.a.g("formatted", 3));
            rH.put("givenName", dw.a.g("givenName", 4));
            rH.put("honorificPrefix", dw.a.g("honorificPrefix", 5));
            rH.put("honorificSuffix", dw.a.g("honorificSuffix", 6));
            rH.put("middleName", dw.a.g("middleName", 7));
        }

        public d() {
            this.iM = 1;
            this.rI = new HashSet();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public d(Set<Integer> set, int i, String str, String str2, String str3, String str4, String str5, String str6) {
            this.rI = set;
            this.iM = i;
            this.sh = str;
            this.tk = str2;
            this.sk = str3;
            this.tl = str4;
            this.tm = str5;
            this.tn = str6;
        }

        @Override // com.google.android.gms.internal.dw
        protected Object D(String str) {
            return null;
        }

        @Override // com.google.android.gms.internal.dw
        protected boolean E(String str) {
            return false;
        }

        @Override // com.google.android.gms.internal.dw
        protected boolean a(dw.a aVar) {
            return this.rI.contains(Integer.valueOf(aVar.bw()));
        }

        @Override // com.google.android.gms.internal.dw
        protected Object b(dw.a aVar) {
            switch (aVar.bw()) {
                case 2:
                    return this.sh;
                case 3:
                    return this.tk;
                case 4:
                    return this.sk;
                case 5:
                    return this.tl;
                case 6:
                    return this.tm;
                case 7:
                    return this.tn;
                default:
                    throw new IllegalStateException("Unknown safe parcelable id=" + aVar.bw());
            }
        }

        @Override // com.google.android.gms.internal.dw
        public HashMap<String, dw.a<?, ?>> bp() {
            return rH;
        }

        @Override // com.google.android.gms.common.data.Freezable
        /* renamed from: dS */
        public d freeze() {
            return this;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            gc gcVar = CREATOR;
            return 0;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Set<Integer> di() {
            return this.rI;
        }

        public boolean equals(Object obj) {
            if (obj instanceof d) {
                if (this == obj) {
                    return true;
                }
                d dVar = (d) obj;
                for (dw.a<?, ?> aVar : rH.values()) {
                    if (a(aVar)) {
                        if (dVar.a(aVar) && b(aVar).equals(dVar.b(aVar))) {
                        }
                        return false;
                    } else if (dVar.a(aVar)) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }

        @Override // com.google.android.gms.plus.model.people.Person.Name
        public String getFamilyName() {
            return this.sh;
        }

        @Override // com.google.android.gms.plus.model.people.Person.Name
        public String getFormatted() {
            return this.tk;
        }

        @Override // com.google.android.gms.plus.model.people.Person.Name
        public String getGivenName() {
            return this.sk;
        }

        @Override // com.google.android.gms.plus.model.people.Person.Name
        public String getHonorificPrefix() {
            return this.tl;
        }

        @Override // com.google.android.gms.plus.model.people.Person.Name
        public String getHonorificSuffix() {
            return this.tm;
        }

        @Override // com.google.android.gms.plus.model.people.Person.Name
        public String getMiddleName() {
            return this.tn;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public int getVersionCode() {
            return this.iM;
        }

        @Override // com.google.android.gms.plus.model.people.Person.Name
        public boolean hasFamilyName() {
            return this.rI.contains(2);
        }

        @Override // com.google.android.gms.plus.model.people.Person.Name
        public boolean hasFormatted() {
            return this.rI.contains(3);
        }

        @Override // com.google.android.gms.plus.model.people.Person.Name
        public boolean hasGivenName() {
            return this.rI.contains(4);
        }

        @Override // com.google.android.gms.plus.model.people.Person.Name
        public boolean hasHonorificPrefix() {
            return this.rI.contains(5);
        }

        @Override // com.google.android.gms.plus.model.people.Person.Name
        public boolean hasHonorificSuffix() {
            return this.rI.contains(6);
        }

        @Override // com.google.android.gms.plus.model.people.Person.Name
        public boolean hasMiddleName() {
            return this.rI.contains(7);
        }

        public int hashCode() {
            int i = 0;
            Iterator<dw.a<?, ?>> it = rH.values().iterator();
            while (true) {
                int i2 = i;
                if (!it.hasNext()) {
                    return i2;
                }
                dw.a<?, ?> next = it.next();
                if (a(next)) {
                    i = b(next).hashCode() + i2 + next.bw();
                } else {
                    i = i2;
                }
            }
        }

        @Override // com.google.android.gms.common.data.Freezable
        public boolean isDataValid() {
            return true;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel out, int flags) {
            gc gcVar = CREATOR;
            gc.a(this, out, flags);
        }
    }

    /* loaded from: classes.dex */
    public static class e {
        public static int aa(String str) {
            if (str.equals("person")) {
                return 0;
            }
            if (str.equals("page")) {
                return 1;
            }
            throw new IllegalArgumentException("Unknown objectType string: " + str);
        }
    }

    /* loaded from: classes.dex */
    public static final class f extends dw implements SafeParcelable, Person.Organizations {
        public static final gd CREATOR = new gd();
        private static final HashMap<String, dw.a<?, ?>> rH = new HashMap<>();
        private final int iM;
        private int jV;
        private String mName;
        private String mo;
        private String qB;
        private final Set<Integer> rI;
        private String sg;
        private String sx;
        private String to;
        private String tp;
        private boolean tq;

        static {
            rH.put("department", dw.a.g("department", 2));
            rH.put(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_DESCRIPTION, dw.a.g(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_DESCRIPTION, 3));
            rH.put("endDate", dw.a.g("endDate", 4));
            rH.put("location", dw.a.g("location", 5));
            rH.put(WorkoutSummary.NAME, dw.a.g(WorkoutSummary.NAME, 6));
            rH.put("primary", dw.a.f("primary", 7));
            rH.put("startDate", dw.a.g("startDate", 8));
            rH.put("title", dw.a.g("title", 9));
            rH.put("type", dw.a.a("type", 10, new dt().c("work", 0).c("school", 1), false));
        }

        public f() {
            this.iM = 1;
            this.rI = new HashSet();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public f(Set<Integer> set, int i, String str, String str2, String str3, String str4, String str5, boolean z, String str6, String str7, int i2) {
            this.rI = set;
            this.iM = i;
            this.to = str;
            this.mo = str2;
            this.sg = str3;
            this.tp = str4;
            this.mName = str5;
            this.tq = z;
            this.sx = str6;
            this.qB = str7;
            this.jV = i2;
        }

        @Override // com.google.android.gms.internal.dw
        protected Object D(String str) {
            return null;
        }

        @Override // com.google.android.gms.internal.dw
        protected boolean E(String str) {
            return false;
        }

        @Override // com.google.android.gms.internal.dw
        protected boolean a(dw.a aVar) {
            return this.rI.contains(Integer.valueOf(aVar.bw()));
        }

        @Override // com.google.android.gms.internal.dw
        protected Object b(dw.a aVar) {
            switch (aVar.bw()) {
                case 2:
                    return this.to;
                case 3:
                    return this.mo;
                case 4:
                    return this.sg;
                case 5:
                    return this.tp;
                case 6:
                    return this.mName;
                case 7:
                    return Boolean.valueOf(this.tq);
                case 8:
                    return this.sx;
                case 9:
                    return this.qB;
                case 10:
                    return Integer.valueOf(this.jV);
                default:
                    throw new IllegalStateException("Unknown safe parcelable id=" + aVar.bw());
            }
        }

        @Override // com.google.android.gms.internal.dw
        public HashMap<String, dw.a<?, ?>> bp() {
            return rH;
        }

        @Override // com.google.android.gms.common.data.Freezable
        /* renamed from: dT */
        public f freeze() {
            return this;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            gd gdVar = CREATOR;
            return 0;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Set<Integer> di() {
            return this.rI;
        }

        public boolean equals(Object obj) {
            if (obj instanceof f) {
                if (this == obj) {
                    return true;
                }
                f fVar = (f) obj;
                for (dw.a<?, ?> aVar : rH.values()) {
                    if (a(aVar)) {
                        if (fVar.a(aVar) && b(aVar).equals(fVar.b(aVar))) {
                        }
                        return false;
                    } else if (fVar.a(aVar)) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }

        @Override // com.google.android.gms.plus.model.people.Person.Organizations
        public String getDepartment() {
            return this.to;
        }

        @Override // com.google.android.gms.plus.model.people.Person.Organizations
        public String getDescription() {
            return this.mo;
        }

        @Override // com.google.android.gms.plus.model.people.Person.Organizations
        public String getEndDate() {
            return this.sg;
        }

        @Override // com.google.android.gms.plus.model.people.Person.Organizations
        public String getLocation() {
            return this.tp;
        }

        @Override // com.google.android.gms.plus.model.people.Person.Organizations
        public String getName() {
            return this.mName;
        }

        @Override // com.google.android.gms.plus.model.people.Person.Organizations
        public String getStartDate() {
            return this.sx;
        }

        @Override // com.google.android.gms.plus.model.people.Person.Organizations
        public String getTitle() {
            return this.qB;
        }

        @Override // com.google.android.gms.plus.model.people.Person.Organizations
        public int getType() {
            return this.jV;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public int getVersionCode() {
            return this.iM;
        }

        @Override // com.google.android.gms.plus.model.people.Person.Organizations
        public boolean hasDepartment() {
            return this.rI.contains(2);
        }

        @Override // com.google.android.gms.plus.model.people.Person.Organizations
        public boolean hasDescription() {
            return this.rI.contains(3);
        }

        @Override // com.google.android.gms.plus.model.people.Person.Organizations
        public boolean hasEndDate() {
            return this.rI.contains(4);
        }

        @Override // com.google.android.gms.plus.model.people.Person.Organizations
        public boolean hasLocation() {
            return this.rI.contains(5);
        }

        @Override // com.google.android.gms.plus.model.people.Person.Organizations
        public boolean hasName() {
            return this.rI.contains(6);
        }

        @Override // com.google.android.gms.plus.model.people.Person.Organizations
        public boolean hasPrimary() {
            return this.rI.contains(7);
        }

        @Override // com.google.android.gms.plus.model.people.Person.Organizations
        public boolean hasStartDate() {
            return this.rI.contains(8);
        }

        @Override // com.google.android.gms.plus.model.people.Person.Organizations
        public boolean hasTitle() {
            return this.rI.contains(9);
        }

        @Override // com.google.android.gms.plus.model.people.Person.Organizations
        public boolean hasType() {
            return this.rI.contains(10);
        }

        public int hashCode() {
            int i = 0;
            Iterator<dw.a<?, ?>> it = rH.values().iterator();
            while (true) {
                int i2 = i;
                if (!it.hasNext()) {
                    return i2;
                }
                dw.a<?, ?> next = it.next();
                if (a(next)) {
                    i = b(next).hashCode() + i2 + next.bw();
                } else {
                    i = i2;
                }
            }
        }

        @Override // com.google.android.gms.common.data.Freezable
        public boolean isDataValid() {
            return true;
        }

        @Override // com.google.android.gms.plus.model.people.Person.Organizations
        public boolean isPrimary() {
            return this.tq;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel out, int flags) {
            gd gdVar = CREATOR;
            gd.a(this, out, flags);
        }
    }

    /* loaded from: classes.dex */
    public static final class g extends dw implements SafeParcelable, Person.PlacesLived {
        public static final ge CREATOR = new ge();
        private static final HashMap<String, dw.a<?, ?>> rH = new HashMap<>();
        private final int iM;
        private String mValue;
        private final Set<Integer> rI;
        private boolean tq;

        static {
            rH.put("primary", dw.a.f("primary", 2));
            rH.put("value", dw.a.g("value", 3));
        }

        public g() {
            this.iM = 1;
            this.rI = new HashSet();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public g(Set<Integer> set, int i, boolean z, String str) {
            this.rI = set;
            this.iM = i;
            this.tq = z;
            this.mValue = str;
        }

        @Override // com.google.android.gms.internal.dw
        protected Object D(String str) {
            return null;
        }

        @Override // com.google.android.gms.internal.dw
        protected boolean E(String str) {
            return false;
        }

        @Override // com.google.android.gms.internal.dw
        protected boolean a(dw.a aVar) {
            return this.rI.contains(Integer.valueOf(aVar.bw()));
        }

        @Override // com.google.android.gms.internal.dw
        protected Object b(dw.a aVar) {
            switch (aVar.bw()) {
                case 2:
                    return Boolean.valueOf(this.tq);
                case 3:
                    return this.mValue;
                default:
                    throw new IllegalStateException("Unknown safe parcelable id=" + aVar.bw());
            }
        }

        @Override // com.google.android.gms.internal.dw
        public HashMap<String, dw.a<?, ?>> bp() {
            return rH;
        }

        @Override // com.google.android.gms.common.data.Freezable
        /* renamed from: dU */
        public g freeze() {
            return this;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            ge geVar = CREATOR;
            return 0;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Set<Integer> di() {
            return this.rI;
        }

        public boolean equals(Object obj) {
            if (obj instanceof g) {
                if (this == obj) {
                    return true;
                }
                g gVar = (g) obj;
                for (dw.a<?, ?> aVar : rH.values()) {
                    if (a(aVar)) {
                        if (gVar.a(aVar) && b(aVar).equals(gVar.b(aVar))) {
                        }
                        return false;
                    } else if (gVar.a(aVar)) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }

        @Override // com.google.android.gms.plus.model.people.Person.PlacesLived
        public String getValue() {
            return this.mValue;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public int getVersionCode() {
            return this.iM;
        }

        @Override // com.google.android.gms.plus.model.people.Person.PlacesLived
        public boolean hasPrimary() {
            return this.rI.contains(2);
        }

        @Override // com.google.android.gms.plus.model.people.Person.PlacesLived
        public boolean hasValue() {
            return this.rI.contains(3);
        }

        public int hashCode() {
            int i = 0;
            Iterator<dw.a<?, ?>> it = rH.values().iterator();
            while (true) {
                int i2 = i;
                if (!it.hasNext()) {
                    return i2;
                }
                dw.a<?, ?> next = it.next();
                if (a(next)) {
                    i = b(next).hashCode() + i2 + next.bw();
                } else {
                    i = i2;
                }
            }
        }

        @Override // com.google.android.gms.common.data.Freezable
        public boolean isDataValid() {
            return true;
        }

        @Override // com.google.android.gms.plus.model.people.Person.PlacesLived
        public boolean isPrimary() {
            return this.tq;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel out, int flags) {
            ge geVar = CREATOR;
            ge.a(this, out, flags);
        }
    }

    /* loaded from: classes.dex */
    public static final class h extends dw implements SafeParcelable, Person.Urls {
        public static final gf CREATOR = new gf();
        private static final HashMap<String, dw.a<?, ?>> rH = new HashMap<>();
        private final int iM;
        private int jV;
        private String mValue;
        private final Set<Integer> rI;
        private String tr;
        private final int ts;

        static {
            rH.put(PlusShare.KEY_CALL_TO_ACTION_LABEL, dw.a.g(PlusShare.KEY_CALL_TO_ACTION_LABEL, 5));
            rH.put("type", dw.a.a("type", 6, new dt().c("home", 0).c("work", 1).c("blog", 2).c("profile", 3).c(LogConstants.OTHER_LOGS_FOLDER, 4).c("otherProfile", 5).c("contributor", 6).c("website", 7), false));
            rH.put("value", dw.a.g("value", 4));
        }

        public h() {
            this.ts = 4;
            this.iM = 2;
            this.rI = new HashSet();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public h(Set<Integer> set, int i, String str, int i2, String str2, int i3) {
            this.ts = 4;
            this.rI = set;
            this.iM = i;
            this.tr = str;
            this.jV = i2;
            this.mValue = str2;
        }

        @Override // com.google.android.gms.internal.dw
        protected Object D(String str) {
            return null;
        }

        @Override // com.google.android.gms.internal.dw
        protected boolean E(String str) {
            return false;
        }

        @Override // com.google.android.gms.internal.dw
        protected boolean a(dw.a aVar) {
            return this.rI.contains(Integer.valueOf(aVar.bw()));
        }

        @Override // com.google.android.gms.internal.dw
        protected Object b(dw.a aVar) {
            switch (aVar.bw()) {
                case 4:
                    return this.mValue;
                case 5:
                    return this.tr;
                case 6:
                    return Integer.valueOf(this.jV);
                default:
                    throw new IllegalStateException("Unknown safe parcelable id=" + aVar.bw());
            }
        }

        @Override // com.google.android.gms.internal.dw
        public HashMap<String, dw.a<?, ?>> bp() {
            return rH;
        }

        @Deprecated
        public int dV() {
            return 4;
        }

        @Override // com.google.android.gms.common.data.Freezable
        /* renamed from: dW */
        public h freeze() {
            return this;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            gf gfVar = CREATOR;
            return 0;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Set<Integer> di() {
            return this.rI;
        }

        public boolean equals(Object obj) {
            if (obj instanceof h) {
                if (this == obj) {
                    return true;
                }
                h hVar = (h) obj;
                for (dw.a<?, ?> aVar : rH.values()) {
                    if (a(aVar)) {
                        if (hVar.a(aVar) && b(aVar).equals(hVar.b(aVar))) {
                        }
                        return false;
                    } else if (hVar.a(aVar)) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }

        @Override // com.google.android.gms.plus.model.people.Person.Urls
        public String getLabel() {
            return this.tr;
        }

        @Override // com.google.android.gms.plus.model.people.Person.Urls
        public int getType() {
            return this.jV;
        }

        @Override // com.google.android.gms.plus.model.people.Person.Urls
        public String getValue() {
            return this.mValue;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public int getVersionCode() {
            return this.iM;
        }

        @Override // com.google.android.gms.plus.model.people.Person.Urls
        public boolean hasLabel() {
            return this.rI.contains(5);
        }

        @Override // com.google.android.gms.plus.model.people.Person.Urls
        public boolean hasType() {
            return this.rI.contains(6);
        }

        @Override // com.google.android.gms.plus.model.people.Person.Urls
        public boolean hasValue() {
            return this.rI.contains(4);
        }

        public int hashCode() {
            int i = 0;
            Iterator<dw.a<?, ?>> it = rH.values().iterator();
            while (true) {
                int i2 = i;
                if (!it.hasNext()) {
                    return i2;
                }
                dw.a<?, ?> next = it.next();
                if (a(next)) {
                    i = b(next).hashCode() + i2 + next.bw();
                } else {
                    i = i2;
                }
            }
        }

        @Override // com.google.android.gms.common.data.Freezable
        public boolean isDataValid() {
            return true;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel out, int flags) {
            gf gfVar = CREATOR;
            gf.a(this, out, flags);
        }
    }

    static {
        rH.put("aboutMe", dw.a.g("aboutMe", 2));
        rH.put("ageRange", dw.a.a("ageRange", 3, a.class));
        rH.put("birthday", dw.a.g("birthday", 4));
        rH.put("braggingRights", dw.a.g("braggingRights", 5));
        rH.put("circledByCount", dw.a.d("circledByCount", 6));
        rH.put("cover", dw.a.a("cover", 7, b.class));
        rH.put("currentLocation", dw.a.g("currentLocation", 8));
        rH.put("displayName", dw.a.g("displayName", 9));
        rH.put(WorkoutSummary.GENDER, dw.a.a(WorkoutSummary.GENDER, 12, new dt().c("male", 0).c("female", 1).c(LogConstants.OTHER_LOGS_FOLDER, 2), false));
        rH.put("id", dw.a.g("id", 14));
        rH.put(WorkoutSummary.IMAGE, dw.a.a(WorkoutSummary.IMAGE, 15, c.class));
        rH.put("isPlusUser", dw.a.f("isPlusUser", 16));
        rH.put("language", dw.a.g("language", 18));
        rH.put(WorkoutSummary.NAME, dw.a.a(WorkoutSummary.NAME, 19, d.class));
        rH.put("nickname", dw.a.g("nickname", 20));
        rH.put("objectType", dw.a.a("objectType", 21, new dt().c("person", 0).c("page", 1), false));
        rH.put("organizations", dw.a.b("organizations", 22, f.class));
        rH.put("placesLived", dw.a.b("placesLived", 23, g.class));
        rH.put("plusOneCount", dw.a.d("plusOneCount", 24));
        rH.put("relationshipStatus", dw.a.a("relationshipStatus", 25, new dt().c("single", 0).c("in_a_relationship", 1).c("engaged", 2).c("married", 3).c("its_complicated", 4).c("open_relationship", 5).c("widowed", 6).c("in_domestic_partnership", 7).c("in_civil_union", 8), false));
        rH.put("tagline", dw.a.g("tagline", 26));
        rH.put("url", dw.a.g("url", 27));
        rH.put("urls", dw.a.b("urls", 28, h.class));
        rH.put("verified", dw.a.f("verified", 29));
    }

    public fv() {
        this.iM = 2;
        this.rI = new HashSet();
    }

    public fv(String str, String str2, c cVar, int i, String str3) {
        this.iM = 2;
        this.rI = new HashSet();
        this.ml = str;
        this.rI.add(9);
        this.sm = str2;
        this.rI.add(14);
        this.sQ = cVar;
        this.rI.add(15);
        this.sV = i;
        this.rI.add(21);
        this.hN = str3;
        this.rI.add(27);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public fv(Set<Integer> set, int i, String str, a aVar, String str2, String str3, int i2, b bVar, String str4, String str5, int i3, String str6, c cVar, boolean z, String str7, d dVar, String str8, int i4, List<f> list, List<g> list2, int i5, int i6, String str9, String str10, List<h> list3, boolean z2) {
        this.rI = set;
        this.iM = i;
        this.sJ = str;
        this.sK = aVar;
        this.sL = str2;
        this.sM = str3;
        this.sN = i2;
        this.sO = bVar;
        this.sP = str4;
        this.ml = str5;
        this.dI = i3;
        this.sm = str6;
        this.sQ = cVar;
        this.sR = z;
        this.sS = str7;
        this.sT = dVar;
        this.sU = str8;
        this.sV = i4;
        this.sW = list;
        this.sX = list2;
        this.sY = i5;
        this.sZ = i6;
        this.ta = str9;
        this.hN = str10;
        this.tb = list3;
        this.tc = z2;
    }

    public static fv e(byte[] bArr) {
        Parcel obtain = Parcel.obtain();
        obtain.unmarshall(bArr, 0, bArr.length);
        obtain.setDataPosition(0);
        fv createFromParcel = CREATOR.createFromParcel(obtain);
        obtain.recycle();
        return createFromParcel;
    }

    @Override // com.google.android.gms.internal.dw
    protected Object D(String str) {
        return null;
    }

    @Override // com.google.android.gms.internal.dw
    protected boolean E(String str) {
        return false;
    }

    @Override // com.google.android.gms.internal.dw
    protected boolean a(dw.a aVar) {
        return this.rI.contains(Integer.valueOf(aVar.bw()));
    }

    @Override // com.google.android.gms.internal.dw
    protected Object b(dw.a aVar) {
        switch (aVar.bw()) {
            case 2:
                return this.sJ;
            case 3:
                return this.sK;
            case 4:
                return this.sL;
            case 5:
                return this.sM;
            case 6:
                return Integer.valueOf(this.sN);
            case 7:
                return this.sO;
            case 8:
                return this.sP;
            case 9:
                return this.ml;
            case 10:
            case 11:
            case 13:
            case 17:
            default:
                throw new IllegalStateException("Unknown safe parcelable id=" + aVar.bw());
            case 12:
                return Integer.valueOf(this.dI);
            case 14:
                return this.sm;
            case 15:
                return this.sQ;
            case 16:
                return Boolean.valueOf(this.sR);
            case 18:
                return this.sS;
            case 19:
                return this.sT;
            case 20:
                return this.sU;
            case 21:
                return Integer.valueOf(this.sV);
            case 22:
                return this.sW;
            case 23:
                return this.sX;
            case 24:
                return Integer.valueOf(this.sY);
            case 25:
                return Integer.valueOf(this.sZ);
            case 26:
                return this.ta;
            case 27:
                return this.hN;
            case 28:
                return this.tb;
            case 29:
                return Boolean.valueOf(this.tc);
        }
    }

    @Override // com.google.android.gms.internal.dw
    public HashMap<String, dw.a<?, ?>> bp() {
        return rH;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public a dD() {
        return this.sK;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public b dE() {
        return this.sO;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public c dF() {
        return this.sQ;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public d dG() {
        return this.sT;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<f> dH() {
        return this.sW;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<g> dI() {
        return this.sX;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<h> dJ() {
        return this.tb;
    }

    @Override // com.google.android.gms.common.data.Freezable
    /* renamed from: dK */
    public fv freeze() {
        return this;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        fw fwVar = CREATOR;
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Set<Integer> di() {
        return this.rI;
    }

    public boolean equals(Object obj) {
        if (obj instanceof fv) {
            if (this == obj) {
                return true;
            }
            fv fvVar = (fv) obj;
            for (dw.a<?, ?> aVar : rH.values()) {
                if (a(aVar)) {
                    if (fvVar.a(aVar) && b(aVar).equals(fvVar.b(aVar))) {
                    }
                    return false;
                } else if (fvVar.a(aVar)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override // com.google.android.gms.plus.model.people.Person
    public String getAboutMe() {
        return this.sJ;
    }

    @Override // com.google.android.gms.plus.model.people.Person
    public Person.AgeRange getAgeRange() {
        return this.sK;
    }

    @Override // com.google.android.gms.plus.model.people.Person
    public String getBirthday() {
        return this.sL;
    }

    @Override // com.google.android.gms.plus.model.people.Person
    public String getBraggingRights() {
        return this.sM;
    }

    @Override // com.google.android.gms.plus.model.people.Person
    public int getCircledByCount() {
        return this.sN;
    }

    @Override // com.google.android.gms.plus.model.people.Person
    public Person.Cover getCover() {
        return this.sO;
    }

    @Override // com.google.android.gms.plus.model.people.Person
    public String getCurrentLocation() {
        return this.sP;
    }

    @Override // com.google.android.gms.plus.model.people.Person
    public String getDisplayName() {
        return this.ml;
    }

    @Override // com.google.android.gms.plus.model.people.Person
    public int getGender() {
        return this.dI;
    }

    @Override // com.google.android.gms.plus.model.people.Person
    public String getId() {
        return this.sm;
    }

    @Override // com.google.android.gms.plus.model.people.Person
    public Person.Image getImage() {
        return this.sQ;
    }

    @Override // com.google.android.gms.plus.model.people.Person
    public String getLanguage() {
        return this.sS;
    }

    @Override // com.google.android.gms.plus.model.people.Person
    public Person.Name getName() {
        return this.sT;
    }

    @Override // com.google.android.gms.plus.model.people.Person
    public String getNickname() {
        return this.sU;
    }

    @Override // com.google.android.gms.plus.model.people.Person
    public int getObjectType() {
        return this.sV;
    }

    @Override // com.google.android.gms.plus.model.people.Person
    public List<Person.Organizations> getOrganizations() {
        return (ArrayList) this.sW;
    }

    @Override // com.google.android.gms.plus.model.people.Person
    public List<Person.PlacesLived> getPlacesLived() {
        return (ArrayList) this.sX;
    }

    @Override // com.google.android.gms.plus.model.people.Person
    public int getPlusOneCount() {
        return this.sY;
    }

    @Override // com.google.android.gms.plus.model.people.Person
    public int getRelationshipStatus() {
        return this.sZ;
    }

    @Override // com.google.android.gms.plus.model.people.Person
    public String getTagline() {
        return this.ta;
    }

    @Override // com.google.android.gms.plus.model.people.Person
    public String getUrl() {
        return this.hN;
    }

    @Override // com.google.android.gms.plus.model.people.Person
    public List<Person.Urls> getUrls() {
        return (ArrayList) this.tb;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getVersionCode() {
        return this.iM;
    }

    @Override // com.google.android.gms.plus.model.people.Person
    public boolean hasAboutMe() {
        return this.rI.contains(2);
    }

    @Override // com.google.android.gms.plus.model.people.Person
    public boolean hasAgeRange() {
        return this.rI.contains(3);
    }

    @Override // com.google.android.gms.plus.model.people.Person
    public boolean hasBirthday() {
        return this.rI.contains(4);
    }

    @Override // com.google.android.gms.plus.model.people.Person
    public boolean hasBraggingRights() {
        return this.rI.contains(5);
    }

    @Override // com.google.android.gms.plus.model.people.Person
    public boolean hasCircledByCount() {
        return this.rI.contains(6);
    }

    @Override // com.google.android.gms.plus.model.people.Person
    public boolean hasCover() {
        return this.rI.contains(7);
    }

    @Override // com.google.android.gms.plus.model.people.Person
    public boolean hasCurrentLocation() {
        return this.rI.contains(8);
    }

    @Override // com.google.android.gms.plus.model.people.Person
    public boolean hasDisplayName() {
        return this.rI.contains(9);
    }

    @Override // com.google.android.gms.plus.model.people.Person
    public boolean hasGender() {
        return this.rI.contains(12);
    }

    @Override // com.google.android.gms.plus.model.people.Person
    public boolean hasId() {
        return this.rI.contains(14);
    }

    @Override // com.google.android.gms.plus.model.people.Person
    public boolean hasImage() {
        return this.rI.contains(15);
    }

    @Override // com.google.android.gms.plus.model.people.Person
    public boolean hasIsPlusUser() {
        return this.rI.contains(16);
    }

    @Override // com.google.android.gms.plus.model.people.Person
    public boolean hasLanguage() {
        return this.rI.contains(18);
    }

    @Override // com.google.android.gms.plus.model.people.Person
    public boolean hasName() {
        return this.rI.contains(19);
    }

    @Override // com.google.android.gms.plus.model.people.Person
    public boolean hasNickname() {
        return this.rI.contains(20);
    }

    @Override // com.google.android.gms.plus.model.people.Person
    public boolean hasObjectType() {
        return this.rI.contains(21);
    }

    @Override // com.google.android.gms.plus.model.people.Person
    public boolean hasOrganizations() {
        return this.rI.contains(22);
    }

    @Override // com.google.android.gms.plus.model.people.Person
    public boolean hasPlacesLived() {
        return this.rI.contains(23);
    }

    @Override // com.google.android.gms.plus.model.people.Person
    public boolean hasPlusOneCount() {
        return this.rI.contains(24);
    }

    @Override // com.google.android.gms.plus.model.people.Person
    public boolean hasRelationshipStatus() {
        return this.rI.contains(25);
    }

    @Override // com.google.android.gms.plus.model.people.Person
    public boolean hasTagline() {
        return this.rI.contains(26);
    }

    @Override // com.google.android.gms.plus.model.people.Person
    public boolean hasUrl() {
        return this.rI.contains(27);
    }

    @Override // com.google.android.gms.plus.model.people.Person
    public boolean hasUrls() {
        return this.rI.contains(28);
    }

    @Override // com.google.android.gms.plus.model.people.Person
    public boolean hasVerified() {
        return this.rI.contains(29);
    }

    public int hashCode() {
        int i = 0;
        Iterator<dw.a<?, ?>> it = rH.values().iterator();
        while (true) {
            int i2 = i;
            if (!it.hasNext()) {
                return i2;
            }
            dw.a<?, ?> next = it.next();
            if (a(next)) {
                i = b(next).hashCode() + i2 + next.bw();
            } else {
                i = i2;
            }
        }
    }

    @Override // com.google.android.gms.common.data.Freezable
    public boolean isDataValid() {
        return true;
    }

    @Override // com.google.android.gms.plus.model.people.Person
    public boolean isPlusUser() {
        return this.sR;
    }

    @Override // com.google.android.gms.plus.model.people.Person
    public boolean isVerified() {
        return this.tc;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        fw fwVar = CREATOR;
        fw.a(this, out, flags);
    }
}
