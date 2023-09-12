package com.google.android.gms.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.dw;
import com.google.android.gms.plus.PlusShare;
import com.google.android.gms.plus.model.moments.ItemScope;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutSummary;
import com.microsoft.kapp.telephony.MmsColumns;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
/* loaded from: classes.dex */
public final class fq extends dw implements SafeParcelable, ItemScope {
    public static final fr CREATOR = new fr();
    private static final HashMap<String, dw.a<?, ?>> rH = new HashMap<>();
    private String hN;
    private final int iM;
    private String mName;
    private String mo;
    private double oE;
    private double oF;
    private final Set<Integer> rI;
    private fq rJ;
    private List<String> rK;
    private fq rL;
    private String rM;
    private String rN;
    private String rO;
    private List<fq> rP;
    private int rQ;
    private List<fq> rR;
    private fq rS;
    private List<fq> rT;
    private String rU;
    private String rV;
    private fq rW;
    private String rX;
    private String rY;
    private String rZ;
    private fq sA;
    private String sB;
    private String sC;
    private String sD;
    private String sE;
    private String sF;
    private List<fq> sa;
    private String sb;
    private String sc;
    private String sd;
    private String se;
    private String sf;
    private String sg;
    private String sh;
    private String si;
    private fq sj;
    private String sk;
    private String sl;
    private String sm;
    private String sn;
    private fq so;
    private fq sp;
    private fq sq;
    private List<fq> sr;
    private String ss;
    private String st;
    private String su;
    private String sv;
    private fq sw;
    private String sx;
    private String sy;
    private String sz;

    static {
        rH.put("about", dw.a.a("about", 2, fq.class));
        rH.put("additionalName", dw.a.h("additionalName", 3));
        rH.put("address", dw.a.a("address", 4, fq.class));
        rH.put("addressCountry", dw.a.g("addressCountry", 5));
        rH.put("addressLocality", dw.a.g("addressLocality", 6));
        rH.put("addressRegion", dw.a.g("addressRegion", 7));
        rH.put("associated_media", dw.a.b("associated_media", 8, fq.class));
        rH.put("attendeeCount", dw.a.d("attendeeCount", 9));
        rH.put("attendees", dw.a.b("attendees", 10, fq.class));
        rH.put("audio", dw.a.a("audio", 11, fq.class));
        rH.put("author", dw.a.b("author", 12, fq.class));
        rH.put("bestRating", dw.a.g("bestRating", 13));
        rH.put("birthDate", dw.a.g("birthDate", 14));
        rH.put("byArtist", dw.a.a("byArtist", 15, fq.class));
        rH.put("caption", dw.a.g("caption", 16));
        rH.put("contentSize", dw.a.g("contentSize", 17));
        rH.put("contentUrl", dw.a.g("contentUrl", 18));
        rH.put("contributor", dw.a.b("contributor", 19, fq.class));
        rH.put("dateCreated", dw.a.g("dateCreated", 20));
        rH.put("dateModified", dw.a.g("dateModified", 21));
        rH.put("datePublished", dw.a.g("datePublished", 22));
        rH.put(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_DESCRIPTION, dw.a.g(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_DESCRIPTION, 23));
        rH.put("duration", dw.a.g("duration", 24));
        rH.put("embedUrl", dw.a.g("embedUrl", 25));
        rH.put("endDate", dw.a.g("endDate", 26));
        rH.put("familyName", dw.a.g("familyName", 27));
        rH.put(WorkoutSummary.GENDER, dw.a.g(WorkoutSummary.GENDER, 28));
        rH.put("geo", dw.a.a("geo", 29, fq.class));
        rH.put("givenName", dw.a.g("givenName", 30));
        rH.put("height", dw.a.g("height", 31));
        rH.put("id", dw.a.g("id", 32));
        rH.put(WorkoutSummary.IMAGE, dw.a.g(WorkoutSummary.IMAGE, 33));
        rH.put("inAlbum", dw.a.a("inAlbum", 34, fq.class));
        rH.put("latitude", dw.a.e("latitude", 36));
        rH.put("location", dw.a.a("location", 37, fq.class));
        rH.put("longitude", dw.a.e("longitude", 38));
        rH.put(WorkoutSummary.NAME, dw.a.g(WorkoutSummary.NAME, 39));
        rH.put("partOfTVSeries", dw.a.a("partOfTVSeries", 40, fq.class));
        rH.put("performers", dw.a.b("performers", 41, fq.class));
        rH.put("playerType", dw.a.g("playerType", 42));
        rH.put("postOfficeBoxNumber", dw.a.g("postOfficeBoxNumber", 43));
        rH.put("postalCode", dw.a.g("postalCode", 44));
        rH.put("ratingValue", dw.a.g("ratingValue", 45));
        rH.put("reviewRating", dw.a.a("reviewRating", 46, fq.class));
        rH.put("startDate", dw.a.g("startDate", 47));
        rH.put("streetAddress", dw.a.g("streetAddress", 48));
        rH.put(MmsColumns.MMS_PART_TEXT, dw.a.g(MmsColumns.MMS_PART_TEXT, 49));
        rH.put("thumbnail", dw.a.a("thumbnail", 50, fq.class));
        rH.put(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_THUMBNAIL_URL, dw.a.g(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_THUMBNAIL_URL, 51));
        rH.put("tickerSymbol", dw.a.g("tickerSymbol", 52));
        rH.put("type", dw.a.g("type", 53));
        rH.put("url", dw.a.g("url", 54));
        rH.put("width", dw.a.g("width", 55));
        rH.put("worstRating", dw.a.g("worstRating", 56));
    }

    public fq() {
        this.iM = 1;
        this.rI = new HashSet();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public fq(Set<Integer> set, int i, fq fqVar, List<String> list, fq fqVar2, String str, String str2, String str3, List<fq> list2, int i2, List<fq> list3, fq fqVar3, List<fq> list4, String str4, String str5, fq fqVar4, String str6, String str7, String str8, List<fq> list5, String str9, String str10, String str11, String str12, String str13, String str14, String str15, String str16, String str17, fq fqVar5, String str18, String str19, String str20, String str21, fq fqVar6, double d, fq fqVar7, double d2, String str22, fq fqVar8, List<fq> list6, String str23, String str24, String str25, String str26, fq fqVar9, String str27, String str28, String str29, fq fqVar10, String str30, String str31, String str32, String str33, String str34, String str35) {
        this.rI = set;
        this.iM = i;
        this.rJ = fqVar;
        this.rK = list;
        this.rL = fqVar2;
        this.rM = str;
        this.rN = str2;
        this.rO = str3;
        this.rP = list2;
        this.rQ = i2;
        this.rR = list3;
        this.rS = fqVar3;
        this.rT = list4;
        this.rU = str4;
        this.rV = str5;
        this.rW = fqVar4;
        this.rX = str6;
        this.rY = str7;
        this.rZ = str8;
        this.sa = list5;
        this.sb = str9;
        this.sc = str10;
        this.sd = str11;
        this.mo = str12;
        this.se = str13;
        this.sf = str14;
        this.sg = str15;
        this.sh = str16;
        this.si = str17;
        this.sj = fqVar5;
        this.sk = str18;
        this.sl = str19;
        this.sm = str20;
        this.sn = str21;
        this.so = fqVar6;
        this.oE = d;
        this.sp = fqVar7;
        this.oF = d2;
        this.mName = str22;
        this.sq = fqVar8;
        this.sr = list6;
        this.ss = str23;
        this.st = str24;
        this.su = str25;
        this.sv = str26;
        this.sw = fqVar9;
        this.sx = str27;
        this.sy = str28;
        this.sz = str29;
        this.sA = fqVar10;
        this.sB = str30;
        this.sC = str31;
        this.sD = str32;
        this.hN = str33;
        this.sE = str34;
        this.sF = str35;
    }

    public fq(Set<Integer> set, fq fqVar, List<String> list, fq fqVar2, String str, String str2, String str3, List<fq> list2, int i, List<fq> list3, fq fqVar3, List<fq> list4, String str4, String str5, fq fqVar4, String str6, String str7, String str8, List<fq> list5, String str9, String str10, String str11, String str12, String str13, String str14, String str15, String str16, String str17, fq fqVar5, String str18, String str19, String str20, String str21, fq fqVar6, double d, fq fqVar7, double d2, String str22, fq fqVar8, List<fq> list6, String str23, String str24, String str25, String str26, fq fqVar9, String str27, String str28, String str29, fq fqVar10, String str30, String str31, String str32, String str33, String str34, String str35) {
        this.rI = set;
        this.iM = 1;
        this.rJ = fqVar;
        this.rK = list;
        this.rL = fqVar2;
        this.rM = str;
        this.rN = str2;
        this.rO = str3;
        this.rP = list2;
        this.rQ = i;
        this.rR = list3;
        this.rS = fqVar3;
        this.rT = list4;
        this.rU = str4;
        this.rV = str5;
        this.rW = fqVar4;
        this.rX = str6;
        this.rY = str7;
        this.rZ = str8;
        this.sa = list5;
        this.sb = str9;
        this.sc = str10;
        this.sd = str11;
        this.mo = str12;
        this.se = str13;
        this.sf = str14;
        this.sg = str15;
        this.sh = str16;
        this.si = str17;
        this.sj = fqVar5;
        this.sk = str18;
        this.sl = str19;
        this.sm = str20;
        this.sn = str21;
        this.so = fqVar6;
        this.oE = d;
        this.sp = fqVar7;
        this.oF = d2;
        this.mName = str22;
        this.sq = fqVar8;
        this.sr = list6;
        this.ss = str23;
        this.st = str24;
        this.su = str25;
        this.sv = str26;
        this.sw = fqVar9;
        this.sx = str27;
        this.sy = str28;
        this.sz = str29;
        this.sA = fqVar10;
        this.sB = str30;
        this.sC = str31;
        this.sD = str32;
        this.hN = str33;
        this.sE = str34;
        this.sF = str35;
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
                return this.rJ;
            case 3:
                return this.rK;
            case 4:
                return this.rL;
            case 5:
                return this.rM;
            case 6:
                return this.rN;
            case 7:
                return this.rO;
            case 8:
                return this.rP;
            case 9:
                return Integer.valueOf(this.rQ);
            case 10:
                return this.rR;
            case 11:
                return this.rS;
            case 12:
                return this.rT;
            case 13:
                return this.rU;
            case 14:
                return this.rV;
            case 15:
                return this.rW;
            case 16:
                return this.rX;
            case 17:
                return this.rY;
            case 18:
                return this.rZ;
            case 19:
                return this.sa;
            case 20:
                return this.sb;
            case 21:
                return this.sc;
            case 22:
                return this.sd;
            case 23:
                return this.mo;
            case 24:
                return this.se;
            case 25:
                return this.sf;
            case 26:
                return this.sg;
            case 27:
                return this.sh;
            case 28:
                return this.si;
            case 29:
                return this.sj;
            case 30:
                return this.sk;
            case 31:
                return this.sl;
            case 32:
                return this.sm;
            case 33:
                return this.sn;
            case 34:
                return this.so;
            case 35:
            default:
                throw new IllegalStateException("Unknown safe parcelable id=" + aVar.bw());
            case 36:
                return Double.valueOf(this.oE);
            case 37:
                return this.sp;
            case 38:
                return Double.valueOf(this.oF);
            case 39:
                return this.mName;
            case 40:
                return this.sq;
            case 41:
                return this.sr;
            case 42:
                return this.ss;
            case 43:
                return this.st;
            case 44:
                return this.su;
            case 45:
                return this.sv;
            case 46:
                return this.sw;
            case 47:
                return this.sx;
            case 48:
                return this.sy;
            case 49:
                return this.sz;
            case 50:
                return this.sA;
            case 51:
                return this.sB;
            case 52:
                return this.sC;
            case 53:
                return this.sD;
            case 54:
                return this.hN;
            case 55:
                return this.sE;
            case 56:
                return this.sF;
        }
    }

    @Override // com.google.android.gms.internal.dw
    public HashMap<String, dw.a<?, ?>> bp() {
        return rH;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        fr frVar = CREATOR;
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Set<Integer> di() {
        return this.rI;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public fq dj() {
        return this.rJ;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public fq dk() {
        return this.rL;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<fq> dl() {
        return this.rP;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<fq> dm() {
        return this.rR;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public fq dn() {
        return this.rS;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: do  reason: not valid java name */
    public List<fq> m6do() {
        return this.rT;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public fq dp() {
        return this.rW;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<fq> dq() {
        return this.sa;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public fq dr() {
        return this.sj;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public fq ds() {
        return this.so;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public fq dt() {
        return this.sp;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public fq du() {
        return this.sq;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<fq> dv() {
        return this.sr;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public fq dw() {
        return this.sw;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public fq dx() {
        return this.sA;
    }

    @Override // com.google.android.gms.common.data.Freezable
    /* renamed from: dy */
    public fq freeze() {
        return this;
    }

    public boolean equals(Object obj) {
        if (obj instanceof fq) {
            if (this == obj) {
                return true;
            }
            fq fqVar = (fq) obj;
            for (dw.a<?, ?> aVar : rH.values()) {
                if (a(aVar)) {
                    if (fqVar.a(aVar) && b(aVar).equals(fqVar.b(aVar))) {
                    }
                    return false;
                } else if (fqVar.a(aVar)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public ItemScope getAbout() {
        return this.rJ;
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public List<String> getAdditionalName() {
        return this.rK;
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public ItemScope getAddress() {
        return this.rL;
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public String getAddressCountry() {
        return this.rM;
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public String getAddressLocality() {
        return this.rN;
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public String getAddressRegion() {
        return this.rO;
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public List<ItemScope> getAssociated_media() {
        return (ArrayList) this.rP;
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public int getAttendeeCount() {
        return this.rQ;
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public List<ItemScope> getAttendees() {
        return (ArrayList) this.rR;
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public ItemScope getAudio() {
        return this.rS;
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public List<ItemScope> getAuthor() {
        return (ArrayList) this.rT;
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public String getBestRating() {
        return this.rU;
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public String getBirthDate() {
        return this.rV;
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public ItemScope getByArtist() {
        return this.rW;
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public String getCaption() {
        return this.rX;
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public String getContentSize() {
        return this.rY;
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public String getContentUrl() {
        return this.rZ;
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public List<ItemScope> getContributor() {
        return (ArrayList) this.sa;
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public String getDateCreated() {
        return this.sb;
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public String getDateModified() {
        return this.sc;
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public String getDatePublished() {
        return this.sd;
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public String getDescription() {
        return this.mo;
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public String getDuration() {
        return this.se;
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public String getEmbedUrl() {
        return this.sf;
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public String getEndDate() {
        return this.sg;
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public String getFamilyName() {
        return this.sh;
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public String getGender() {
        return this.si;
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public ItemScope getGeo() {
        return this.sj;
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public String getGivenName() {
        return this.sk;
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public String getHeight() {
        return this.sl;
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public String getId() {
        return this.sm;
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public String getImage() {
        return this.sn;
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public ItemScope getInAlbum() {
        return this.so;
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public double getLatitude() {
        return this.oE;
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public ItemScope getLocation() {
        return this.sp;
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public double getLongitude() {
        return this.oF;
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public String getName() {
        return this.mName;
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public ItemScope getPartOfTVSeries() {
        return this.sq;
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public List<ItemScope> getPerformers() {
        return (ArrayList) this.sr;
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public String getPlayerType() {
        return this.ss;
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public String getPostOfficeBoxNumber() {
        return this.st;
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public String getPostalCode() {
        return this.su;
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public String getRatingValue() {
        return this.sv;
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public ItemScope getReviewRating() {
        return this.sw;
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public String getStartDate() {
        return this.sx;
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public String getStreetAddress() {
        return this.sy;
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public String getText() {
        return this.sz;
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public ItemScope getThumbnail() {
        return this.sA;
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public String getThumbnailUrl() {
        return this.sB;
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public String getTickerSymbol() {
        return this.sC;
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public String getType() {
        return this.sD;
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public String getUrl() {
        return this.hN;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getVersionCode() {
        return this.iM;
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public String getWidth() {
        return this.sE;
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public String getWorstRating() {
        return this.sF;
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public boolean hasAbout() {
        return this.rI.contains(2);
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public boolean hasAdditionalName() {
        return this.rI.contains(3);
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public boolean hasAddress() {
        return this.rI.contains(4);
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public boolean hasAddressCountry() {
        return this.rI.contains(5);
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public boolean hasAddressLocality() {
        return this.rI.contains(6);
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public boolean hasAddressRegion() {
        return this.rI.contains(7);
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public boolean hasAssociated_media() {
        return this.rI.contains(8);
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public boolean hasAttendeeCount() {
        return this.rI.contains(9);
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public boolean hasAttendees() {
        return this.rI.contains(10);
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public boolean hasAudio() {
        return this.rI.contains(11);
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public boolean hasAuthor() {
        return this.rI.contains(12);
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public boolean hasBestRating() {
        return this.rI.contains(13);
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public boolean hasBirthDate() {
        return this.rI.contains(14);
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public boolean hasByArtist() {
        return this.rI.contains(15);
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public boolean hasCaption() {
        return this.rI.contains(16);
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public boolean hasContentSize() {
        return this.rI.contains(17);
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public boolean hasContentUrl() {
        return this.rI.contains(18);
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public boolean hasContributor() {
        return this.rI.contains(19);
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public boolean hasDateCreated() {
        return this.rI.contains(20);
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public boolean hasDateModified() {
        return this.rI.contains(21);
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public boolean hasDatePublished() {
        return this.rI.contains(22);
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public boolean hasDescription() {
        return this.rI.contains(23);
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public boolean hasDuration() {
        return this.rI.contains(24);
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public boolean hasEmbedUrl() {
        return this.rI.contains(25);
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public boolean hasEndDate() {
        return this.rI.contains(26);
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public boolean hasFamilyName() {
        return this.rI.contains(27);
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public boolean hasGender() {
        return this.rI.contains(28);
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public boolean hasGeo() {
        return this.rI.contains(29);
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public boolean hasGivenName() {
        return this.rI.contains(30);
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public boolean hasHeight() {
        return this.rI.contains(31);
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public boolean hasId() {
        return this.rI.contains(32);
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public boolean hasImage() {
        return this.rI.contains(33);
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public boolean hasInAlbum() {
        return this.rI.contains(34);
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public boolean hasLatitude() {
        return this.rI.contains(36);
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public boolean hasLocation() {
        return this.rI.contains(37);
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public boolean hasLongitude() {
        return this.rI.contains(38);
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public boolean hasName() {
        return this.rI.contains(39);
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public boolean hasPartOfTVSeries() {
        return this.rI.contains(40);
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public boolean hasPerformers() {
        return this.rI.contains(41);
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public boolean hasPlayerType() {
        return this.rI.contains(42);
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public boolean hasPostOfficeBoxNumber() {
        return this.rI.contains(43);
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public boolean hasPostalCode() {
        return this.rI.contains(44);
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public boolean hasRatingValue() {
        return this.rI.contains(45);
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public boolean hasReviewRating() {
        return this.rI.contains(46);
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public boolean hasStartDate() {
        return this.rI.contains(47);
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public boolean hasStreetAddress() {
        return this.rI.contains(48);
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public boolean hasText() {
        return this.rI.contains(49);
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public boolean hasThumbnail() {
        return this.rI.contains(50);
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public boolean hasThumbnailUrl() {
        return this.rI.contains(51);
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public boolean hasTickerSymbol() {
        return this.rI.contains(52);
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public boolean hasType() {
        return this.rI.contains(53);
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public boolean hasUrl() {
        return this.rI.contains(54);
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public boolean hasWidth() {
        return this.rI.contains(55);
    }

    @Override // com.google.android.gms.plus.model.moments.ItemScope
    public boolean hasWorstRating() {
        return this.rI.contains(56);
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
        fr frVar = CREATOR;
        fr.a(this, out, flags);
    }
}
