package com.google.android.gms.games;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
/* loaded from: classes.dex */
public class a implements Parcelable.Creator<GameEntity> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(GameEntity gameEntity, Parcel parcel, int i) {
        int k = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 1, gameEntity.getApplicationId(), false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, gameEntity.getDisplayName(), false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, gameEntity.getPrimaryCategory(), false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 4, gameEntity.getSecondaryCategory(), false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 5, gameEntity.getDescription(), false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 6, gameEntity.getDeveloperName(), false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 7, (Parcelable) gameEntity.getIconImageUri(), i, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 8, (Parcelable) gameEntity.getHiResImageUri(), i, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 9, (Parcelable) gameEntity.getFeaturedImageUri(), i, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 10, gameEntity.isPlayEnabledGame());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 11, gameEntity.isInstanceInstalled());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 12, gameEntity.getInstancePackageName(), false);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 13, gameEntity.getGameplayAclStatus());
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 14, gameEntity.getAchievementTotalCount());
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 15, gameEntity.getLeaderboardCount());
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1000, gameEntity.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, k);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: K */
    public GameEntity[] newArray(int i) {
        return new GameEntity[i];
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: t */
    public GameEntity createFromParcel(Parcel parcel) {
        int j = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        int i = 0;
        String str = null;
        String str2 = null;
        String str3 = null;
        String str4 = null;
        String str5 = null;
        String str6 = null;
        Uri uri = null;
        Uri uri2 = null;
        Uri uri3 = null;
        boolean z = false;
        boolean z2 = false;
        String str7 = null;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        while (parcel.dataPosition() < j) {
            int i5 = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i5)) {
                case 1:
                    str = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i5);
                    break;
                case 2:
                    str2 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i5);
                    break;
                case 3:
                    str3 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i5);
                    break;
                case 4:
                    str4 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i5);
                    break;
                case 5:
                    str5 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i5);
                    break;
                case 6:
                    str6 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i5);
                    break;
                case 7:
                    uri = (Uri) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i5, Uri.CREATOR);
                    break;
                case 8:
                    uri2 = (Uri) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i5, Uri.CREATOR);
                    break;
                case 9:
                    uri3 = (Uri) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i5, Uri.CREATOR);
                    break;
                case 10:
                    z = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i5);
                    break;
                case 11:
                    z2 = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i5);
                    break;
                case 12:
                    str7 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i5);
                    break;
                case 13:
                    i2 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i5);
                    break;
                case 14:
                    i3 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i5);
                    break;
                case 15:
                    i4 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i5);
                    break;
                case 1000:
                    i = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i5);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i5);
                    break;
            }
        }
        if (parcel.dataPosition() != j) {
            throw new a.C0002a("Overread allowed size end=" + j, parcel);
        }
        return new GameEntity(i, str, str2, str3, str4, str5, str6, uri, uri2, uri3, z, z2, str7, i2, i3, i4);
    }
}
