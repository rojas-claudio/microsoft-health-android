package com.google.android.gms.games.multiplayer;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.games.GameEntity;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class a implements Parcelable.Creator<InvitationEntity> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(InvitationEntity invitationEntity, Parcel parcel, int i) {
        int k = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 1, (Parcelable) invitationEntity.getGame(), i, false);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1000, invitationEntity.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, invitationEntity.getInvitationId(), false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, invitationEntity.getCreationTimestamp());
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 4, invitationEntity.ch());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 5, (Parcelable) invitationEntity.getInviter(), i, false);
        com.google.android.gms.common.internal.safeparcel.b.b(parcel, 6, invitationEntity.getParticipants(), false);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 7, invitationEntity.getVariant());
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, k);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: S */
    public InvitationEntity[] newArray(int i) {
        return new InvitationEntity[i];
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: v */
    public InvitationEntity createFromParcel(Parcel parcel) {
        int i = 0;
        ArrayList arrayList = null;
        int j = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        long j2 = 0;
        ParticipantEntity participantEntity = null;
        int i2 = 0;
        String str = null;
        GameEntity gameEntity = null;
        int i3 = 0;
        while (parcel.dataPosition() < j) {
            int i4 = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i4)) {
                case 1:
                    gameEntity = (GameEntity) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i4, GameEntity.CREATOR);
                    break;
                case 2:
                    str = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i4);
                    break;
                case 3:
                    j2 = com.google.android.gms.common.internal.safeparcel.a.g(parcel, i4);
                    break;
                case 4:
                    i2 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i4);
                    break;
                case 5:
                    participantEntity = (ParticipantEntity) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i4, ParticipantEntity.CREATOR);
                    break;
                case 6:
                    arrayList = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i4, ParticipantEntity.CREATOR);
                    break;
                case 7:
                    i = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i4);
                    break;
                case 1000:
                    i3 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i4);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i4);
                    break;
            }
        }
        if (parcel.dataPosition() != j) {
            throw new a.C0002a("Overread allowed size end=" + j, parcel);
        }
        return new InvitationEntity(i3, gameEntity, str, j2, i2, participantEntity, arrayList, i);
    }
}
