package com.google.android.gms.games.multiplayer.realtime;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.games.multiplayer.ParticipantEntity;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class b implements Parcelable.Creator<RoomEntity> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(RoomEntity roomEntity, Parcel parcel, int i) {
        int k = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 1, roomEntity.getRoomId(), false);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1000, roomEntity.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, roomEntity.getCreatorId(), false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, roomEntity.getCreationTimestamp());
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 4, roomEntity.getStatus());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 5, roomEntity.getDescription(), false);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 6, roomEntity.getVariant());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 7, roomEntity.getAutoMatchCriteria(), false);
        com.google.android.gms.common.internal.safeparcel.b.b(parcel, 8, roomEntity.getParticipants(), false);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 9, roomEntity.getAutoMatchWaitEstimateSeconds());
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, k);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: V */
    public RoomEntity[] newArray(int i) {
        return new RoomEntity[i];
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: y */
    public RoomEntity createFromParcel(Parcel parcel) {
        int i = 0;
        ArrayList arrayList = null;
        int j = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        long j2 = 0;
        Bundle bundle = null;
        int i2 = 0;
        String str = null;
        int i3 = 0;
        String str2 = null;
        String str3 = null;
        int i4 = 0;
        while (parcel.dataPosition() < j) {
            int i5 = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i5)) {
                case 1:
                    str3 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i5);
                    break;
                case 2:
                    str2 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i5);
                    break;
                case 3:
                    j2 = com.google.android.gms.common.internal.safeparcel.a.g(parcel, i5);
                    break;
                case 4:
                    i3 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i5);
                    break;
                case 5:
                    str = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i5);
                    break;
                case 6:
                    i2 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i5);
                    break;
                case 7:
                    bundle = com.google.android.gms.common.internal.safeparcel.a.n(parcel, i5);
                    break;
                case 8:
                    arrayList = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i5, ParticipantEntity.CREATOR);
                    break;
                case 9:
                    i = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i5);
                    break;
                case 1000:
                    i4 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i5);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i5);
                    break;
            }
        }
        if (parcel.dataPosition() != j) {
            throw new a.C0002a("Overread allowed size end=" + j, parcel);
        }
        return new RoomEntity(i4, str3, str2, j2, i3, str, i2, bundle, arrayList, i);
    }
}
