package com.google.android.gms.maps;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.google.android.gms.maps.model.CameraPosition;
/* loaded from: classes.dex */
public class GoogleMapOptionsCreator implements Parcelable.Creator<GoogleMapOptions> {
    public static final int CONTENT_DESCRIPTION = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(GoogleMapOptions googleMapOptions, Parcel parcel, int i) {
        int k = b.k(parcel);
        b.c(parcel, 1, googleMapOptions.getVersionCode());
        b.a(parcel, 2, googleMapOptions.cv());
        b.a(parcel, 3, googleMapOptions.cw());
        b.c(parcel, 4, googleMapOptions.getMapType());
        b.a(parcel, 5, (Parcelable) googleMapOptions.getCamera(), i, false);
        b.a(parcel, 6, googleMapOptions.cx());
        b.a(parcel, 7, googleMapOptions.cy());
        b.a(parcel, 8, googleMapOptions.cz());
        b.a(parcel, 9, googleMapOptions.cA());
        b.a(parcel, 10, googleMapOptions.cB());
        b.a(parcel, 11, googleMapOptions.cC());
        b.C(parcel, k);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public GoogleMapOptions createFromParcel(Parcel parcel) {
        byte b = 0;
        int j = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        CameraPosition cameraPosition = null;
        byte b2 = 0;
        byte b3 = 0;
        byte b4 = 0;
        byte b5 = 0;
        byte b6 = 0;
        int i = 0;
        byte b7 = 0;
        byte b8 = 0;
        int i2 = 0;
        while (parcel.dataPosition() < j) {
            int i3 = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i3)) {
                case 1:
                    i2 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i3);
                    break;
                case 2:
                    b8 = com.google.android.gms.common.internal.safeparcel.a.d(parcel, i3);
                    break;
                case 3:
                    b7 = com.google.android.gms.common.internal.safeparcel.a.d(parcel, i3);
                    break;
                case 4:
                    i = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i3);
                    break;
                case 5:
                    cameraPosition = (CameraPosition) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i3, CameraPosition.CREATOR);
                    break;
                case 6:
                    b6 = com.google.android.gms.common.internal.safeparcel.a.d(parcel, i3);
                    break;
                case 7:
                    b5 = com.google.android.gms.common.internal.safeparcel.a.d(parcel, i3);
                    break;
                case 8:
                    b4 = com.google.android.gms.common.internal.safeparcel.a.d(parcel, i3);
                    break;
                case 9:
                    b3 = com.google.android.gms.common.internal.safeparcel.a.d(parcel, i3);
                    break;
                case 10:
                    b2 = com.google.android.gms.common.internal.safeparcel.a.d(parcel, i3);
                    break;
                case 11:
                    b = com.google.android.gms.common.internal.safeparcel.a.d(parcel, i3);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i3);
                    break;
            }
        }
        if (parcel.dataPosition() != j) {
            throw new a.C0002a("Overread allowed size end=" + j, parcel);
        }
        return new GoogleMapOptions(i2, b8, b7, i, cameraPosition, b6, b5, b4, b3, b2, b);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public GoogleMapOptions[] newArray(int size) {
        return new GoogleMapOptions[size];
    }
}
