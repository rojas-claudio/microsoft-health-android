package com.google.android.gms.internal;

import android.os.Parcel;
import com.google.android.gms.plus.model.moments.ItemScope;
import com.google.android.gms.plus.model.moments.Moment;
/* loaded from: classes.dex */
public final class fu extends com.google.android.gms.common.data.b implements Moment {
    private fs sI;

    public fu(com.google.android.gms.common.data.d dVar, int i) {
        super(dVar, i);
    }

    private fs dC() {
        synchronized (this) {
            if (this.sI == null) {
                byte[] byteArray = getByteArray("momentImpl");
                Parcel obtain = Parcel.obtain();
                obtain.unmarshall(byteArray, 0, byteArray.length);
                obtain.setDataPosition(0);
                this.sI = fs.CREATOR.createFromParcel(obtain);
                obtain.recycle();
            }
        }
        return this.sI;
    }

    @Override // com.google.android.gms.common.data.Freezable
    /* renamed from: dB */
    public fs freeze() {
        return dC();
    }

    @Override // com.google.android.gms.plus.model.moments.Moment
    public String getId() {
        return dC().getId();
    }

    @Override // com.google.android.gms.plus.model.moments.Moment
    public ItemScope getResult() {
        return dC().getResult();
    }

    @Override // com.google.android.gms.plus.model.moments.Moment
    public String getStartDate() {
        return dC().getStartDate();
    }

    @Override // com.google.android.gms.plus.model.moments.Moment
    public ItemScope getTarget() {
        return dC().getTarget();
    }

    @Override // com.google.android.gms.plus.model.moments.Moment
    public String getType() {
        return dC().getType();
    }

    @Override // com.google.android.gms.plus.model.moments.Moment
    public boolean hasId() {
        return dC().hasId();
    }

    @Override // com.google.android.gms.plus.model.moments.Moment
    public boolean hasResult() {
        return dC().hasId();
    }

    @Override // com.google.android.gms.plus.model.moments.Moment
    public boolean hasStartDate() {
        return dC().hasStartDate();
    }

    @Override // com.google.android.gms.plus.model.moments.Moment
    public boolean hasTarget() {
        return dC().hasTarget();
    }

    @Override // com.google.android.gms.plus.model.moments.Moment
    public boolean hasType() {
        return dC().hasType();
    }
}
