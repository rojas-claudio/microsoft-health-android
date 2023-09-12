package com.google.android.gms.plus.model.people;

import com.google.android.gms.common.data.DataBuffer;
import com.google.android.gms.common.data.c;
import com.google.android.gms.common.data.d;
import com.google.android.gms.internal.fv;
import com.google.android.gms.internal.gg;
/* loaded from: classes.dex */
public final class PersonBuffer extends DataBuffer<Person> {
    private final c<fv> tt;

    public PersonBuffer(d dataHolder) {
        super(dataHolder);
        if (dataHolder.aM() == null || !dataHolder.aM().getBoolean("com.google.android.gms.plus.IsSafeParcelable", false)) {
            this.tt = null;
        } else {
            this.tt = new c<>(dataHolder, fv.CREATOR);
        }
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.google.android.gms.common.data.DataBuffer
    public Person get(int position) {
        return this.tt != null ? this.tt.get(position) : new gg(this.jf, position);
    }
}
