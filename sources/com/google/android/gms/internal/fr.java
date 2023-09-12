package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
import com.microsoft.kapp.utils.Constants;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
/* loaded from: classes.dex */
public class fr implements Parcelable.Creator<fq> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(fq fqVar, Parcel parcel, int i) {
        int k = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        Set<Integer> di = fqVar.di();
        if (di.contains(1)) {
            com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, fqVar.getVersionCode());
        }
        if (di.contains(2)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, (Parcelable) fqVar.dj(), i, true);
        }
        if (di.contains(3)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, fqVar.getAdditionalName(), true);
        }
        if (di.contains(4)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 4, (Parcelable) fqVar.dk(), i, true);
        }
        if (di.contains(5)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 5, fqVar.getAddressCountry(), true);
        }
        if (di.contains(6)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 6, fqVar.getAddressLocality(), true);
        }
        if (di.contains(7)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 7, fqVar.getAddressRegion(), true);
        }
        if (di.contains(8)) {
            com.google.android.gms.common.internal.safeparcel.b.b(parcel, 8, fqVar.dl(), true);
        }
        if (di.contains(9)) {
            com.google.android.gms.common.internal.safeparcel.b.c(parcel, 9, fqVar.getAttendeeCount());
        }
        if (di.contains(10)) {
            com.google.android.gms.common.internal.safeparcel.b.b(parcel, 10, fqVar.dm(), true);
        }
        if (di.contains(11)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 11, (Parcelable) fqVar.dn(), i, true);
        }
        if (di.contains(12)) {
            com.google.android.gms.common.internal.safeparcel.b.b(parcel, 12, fqVar.m6do(), true);
        }
        if (di.contains(13)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 13, fqVar.getBestRating(), true);
        }
        if (di.contains(14)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 14, fqVar.getBirthDate(), true);
        }
        if (di.contains(15)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 15, (Parcelable) fqVar.dp(), i, true);
        }
        if (di.contains(17)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 17, fqVar.getContentSize(), true);
        }
        if (di.contains(16)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 16, fqVar.getCaption(), true);
        }
        if (di.contains(19)) {
            com.google.android.gms.common.internal.safeparcel.b.b(parcel, 19, fqVar.dq(), true);
        }
        if (di.contains(18)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 18, fqVar.getContentUrl(), true);
        }
        if (di.contains(21)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 21, fqVar.getDateModified(), true);
        }
        if (di.contains(20)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 20, fqVar.getDateCreated(), true);
        }
        if (di.contains(23)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 23, fqVar.getDescription(), true);
        }
        if (di.contains(22)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 22, fqVar.getDatePublished(), true);
        }
        if (di.contains(25)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 25, fqVar.getEmbedUrl(), true);
        }
        if (di.contains(24)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 24, fqVar.getDuration(), true);
        }
        if (di.contains(27)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 27, fqVar.getFamilyName(), true);
        }
        if (di.contains(26)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 26, fqVar.getEndDate(), true);
        }
        if (di.contains(29)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 29, (Parcelable) fqVar.dr(), i, true);
        }
        if (di.contains(28)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 28, fqVar.getGender(), true);
        }
        if (di.contains(31)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 31, fqVar.getHeight(), true);
        }
        if (di.contains(30)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 30, fqVar.getGivenName(), true);
        }
        if (di.contains(34)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 34, (Parcelable) fqVar.ds(), i, true);
        }
        if (di.contains(32)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 32, fqVar.getId(), true);
        }
        if (di.contains(33)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 33, fqVar.getImage(), true);
        }
        if (di.contains(38)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 38, fqVar.getLongitude());
        }
        if (di.contains(39)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 39, fqVar.getName(), true);
        }
        if (di.contains(36)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 36, fqVar.getLatitude());
        }
        if (di.contains(37)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 37, (Parcelable) fqVar.dt(), i, true);
        }
        if (di.contains(42)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 42, fqVar.getPlayerType(), true);
        }
        if (di.contains(43)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 43, fqVar.getPostOfficeBoxNumber(), true);
        }
        if (di.contains(40)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 40, (Parcelable) fqVar.du(), i, true);
        }
        if (di.contains(41)) {
            com.google.android.gms.common.internal.safeparcel.b.b(parcel, 41, fqVar.dv(), true);
        }
        if (di.contains(46)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 46, (Parcelable) fqVar.dw(), i, true);
        }
        if (di.contains(47)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 47, fqVar.getStartDate(), true);
        }
        if (di.contains(44)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 44, fqVar.getPostalCode(), true);
        }
        if (di.contains(45)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 45, fqVar.getRatingValue(), true);
        }
        if (di.contains(51)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 51, fqVar.getThumbnailUrl(), true);
        }
        if (di.contains(50)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 50, (Parcelable) fqVar.dx(), i, true);
        }
        if (di.contains(49)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 49, fqVar.getText(), true);
        }
        if (di.contains(48)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 48, fqVar.getStreetAddress(), true);
        }
        if (di.contains(55)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 55, fqVar.getWidth(), true);
        }
        if (di.contains(54)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 54, fqVar.getUrl(), true);
        }
        if (di.contains(53)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 53, fqVar.getType(), true);
        }
        if (di.contains(52)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 52, fqVar.getTickerSymbol(), true);
        }
        if (di.contains(56)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 56, fqVar.getWorstRating(), true);
        }
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, k);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: B */
    public fq createFromParcel(Parcel parcel) {
        int j = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        HashSet hashSet = new HashSet();
        int i = 0;
        fq fqVar = null;
        ArrayList<String> arrayList = null;
        fq fqVar2 = null;
        String str = null;
        String str2 = null;
        String str3 = null;
        ArrayList arrayList2 = null;
        int i2 = 0;
        ArrayList arrayList3 = null;
        fq fqVar3 = null;
        ArrayList arrayList4 = null;
        String str4 = null;
        String str5 = null;
        fq fqVar4 = null;
        String str6 = null;
        String str7 = null;
        String str8 = null;
        ArrayList arrayList5 = null;
        String str9 = null;
        String str10 = null;
        String str11 = null;
        String str12 = null;
        String str13 = null;
        String str14 = null;
        String str15 = null;
        String str16 = null;
        String str17 = null;
        fq fqVar5 = null;
        String str18 = null;
        String str19 = null;
        String str20 = null;
        String str21 = null;
        fq fqVar6 = null;
        double d = Constants.SPLITS_ACCURACY;
        fq fqVar7 = null;
        double d2 = Constants.SPLITS_ACCURACY;
        String str22 = null;
        fq fqVar8 = null;
        ArrayList arrayList6 = null;
        String str23 = null;
        String str24 = null;
        String str25 = null;
        String str26 = null;
        fq fqVar9 = null;
        String str27 = null;
        String str28 = null;
        String str29 = null;
        fq fqVar10 = null;
        String str30 = null;
        String str31 = null;
        String str32 = null;
        String str33 = null;
        String str34 = null;
        String str35 = null;
        while (parcel.dataPosition() < j) {
            int i3 = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i3)) {
                case 1:
                    i = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i3);
                    hashSet.add(1);
                    break;
                case 2:
                    hashSet.add(2);
                    fqVar = (fq) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i3, fq.CREATOR);
                    break;
                case 3:
                    arrayList = com.google.android.gms.common.internal.safeparcel.a.x(parcel, i3);
                    hashSet.add(3);
                    break;
                case 4:
                    hashSet.add(4);
                    fqVar2 = (fq) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i3, fq.CREATOR);
                    break;
                case 5:
                    str = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i3);
                    hashSet.add(5);
                    break;
                case 6:
                    str2 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i3);
                    hashSet.add(6);
                    break;
                case 7:
                    str3 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i3);
                    hashSet.add(7);
                    break;
                case 8:
                    arrayList2 = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i3, fq.CREATOR);
                    hashSet.add(8);
                    break;
                case 9:
                    i2 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i3);
                    hashSet.add(9);
                    break;
                case 10:
                    arrayList3 = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i3, fq.CREATOR);
                    hashSet.add(10);
                    break;
                case 11:
                    hashSet.add(11);
                    fqVar3 = (fq) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i3, fq.CREATOR);
                    break;
                case 12:
                    arrayList4 = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i3, fq.CREATOR);
                    hashSet.add(12);
                    break;
                case 13:
                    str4 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i3);
                    hashSet.add(13);
                    break;
                case 14:
                    str5 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i3);
                    hashSet.add(14);
                    break;
                case 15:
                    hashSet.add(15);
                    fqVar4 = (fq) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i3, fq.CREATOR);
                    break;
                case 16:
                    str6 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i3);
                    hashSet.add(16);
                    break;
                case 17:
                    str7 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i3);
                    hashSet.add(17);
                    break;
                case 18:
                    str8 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i3);
                    hashSet.add(18);
                    break;
                case 19:
                    arrayList5 = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i3, fq.CREATOR);
                    hashSet.add(19);
                    break;
                case 20:
                    str9 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i3);
                    hashSet.add(20);
                    break;
                case 21:
                    str10 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i3);
                    hashSet.add(21);
                    break;
                case 22:
                    str11 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i3);
                    hashSet.add(22);
                    break;
                case 23:
                    str12 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i3);
                    hashSet.add(23);
                    break;
                case 24:
                    str13 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i3);
                    hashSet.add(24);
                    break;
                case 25:
                    str14 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i3);
                    hashSet.add(25);
                    break;
                case 26:
                    str15 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i3);
                    hashSet.add(26);
                    break;
                case 27:
                    str16 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i3);
                    hashSet.add(27);
                    break;
                case 28:
                    str17 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i3);
                    hashSet.add(28);
                    break;
                case 29:
                    hashSet.add(29);
                    fqVar5 = (fq) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i3, fq.CREATOR);
                    break;
                case 30:
                    str18 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i3);
                    hashSet.add(30);
                    break;
                case 31:
                    str19 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i3);
                    hashSet.add(31);
                    break;
                case 32:
                    str20 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i3);
                    hashSet.add(32);
                    break;
                case 33:
                    str21 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i3);
                    hashSet.add(33);
                    break;
                case 34:
                    hashSet.add(34);
                    fqVar6 = (fq) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i3, fq.CREATOR);
                    break;
                case 35:
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i3);
                    break;
                case 36:
                    d = com.google.android.gms.common.internal.safeparcel.a.j(parcel, i3);
                    hashSet.add(36);
                    break;
                case 37:
                    hashSet.add(37);
                    fqVar7 = (fq) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i3, fq.CREATOR);
                    break;
                case 38:
                    d2 = com.google.android.gms.common.internal.safeparcel.a.j(parcel, i3);
                    hashSet.add(38);
                    break;
                case 39:
                    str22 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i3);
                    hashSet.add(39);
                    break;
                case 40:
                    hashSet.add(40);
                    fqVar8 = (fq) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i3, fq.CREATOR);
                    break;
                case 41:
                    arrayList6 = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i3, fq.CREATOR);
                    hashSet.add(41);
                    break;
                case 42:
                    str23 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i3);
                    hashSet.add(42);
                    break;
                case 43:
                    str24 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i3);
                    hashSet.add(43);
                    break;
                case 44:
                    str25 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i3);
                    hashSet.add(44);
                    break;
                case 45:
                    str26 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i3);
                    hashSet.add(45);
                    break;
                case 46:
                    hashSet.add(46);
                    fqVar9 = (fq) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i3, fq.CREATOR);
                    break;
                case 47:
                    str27 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i3);
                    hashSet.add(47);
                    break;
                case 48:
                    str28 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i3);
                    hashSet.add(48);
                    break;
                case 49:
                    str29 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i3);
                    hashSet.add(49);
                    break;
                case 50:
                    hashSet.add(50);
                    fqVar10 = (fq) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i3, fq.CREATOR);
                    break;
                case 51:
                    str30 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i3);
                    hashSet.add(51);
                    break;
                case 52:
                    str31 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i3);
                    hashSet.add(52);
                    break;
                case 53:
                    str32 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i3);
                    hashSet.add(53);
                    break;
                case 54:
                    str33 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i3);
                    hashSet.add(54);
                    break;
                case 55:
                    str34 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i3);
                    hashSet.add(55);
                    break;
                case 56:
                    str35 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i3);
                    hashSet.add(56);
                    break;
            }
        }
        if (parcel.dataPosition() != j) {
            throw new a.C0002a("Overread allowed size end=" + j, parcel);
        }
        return new fq(hashSet, i, fqVar, arrayList, fqVar2, str, str2, str3, arrayList2, i2, arrayList3, fqVar3, arrayList4, str4, str5, fqVar4, str6, str7, str8, arrayList5, str9, str10, str11, str12, str13, str14, str15, str16, str17, fqVar5, str18, str19, str20, str21, fqVar6, d, fqVar7, d2, str22, fqVar8, arrayList6, str23, str24, str25, str26, fqVar9, str27, str28, str29, fqVar10, str30, str31, str32, str33, str34, str35);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: ag */
    public fq[] newArray(int i) {
        return new fq[i];
    }
}
