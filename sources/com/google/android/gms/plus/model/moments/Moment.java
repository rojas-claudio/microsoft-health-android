package com.google.android.gms.plus.model.moments;

import com.google.android.gms.common.data.Freezable;
import com.google.android.gms.internal.fq;
import com.google.android.gms.internal.fs;
import java.util.HashSet;
import java.util.Set;
/* loaded from: classes.dex */
public interface Moment extends Freezable<Moment> {

    /* loaded from: classes.dex */
    public static class Builder {
        private final Set<Integer> rI = new HashSet();
        private String sD;
        private fq sG;
        private fq sH;
        private String sm;
        private String sx;

        public Moment build() {
            return new fs(this.rI, this.sm, this.sG, this.sx, this.sH, this.sD);
        }

        public Builder setId(String id) {
            this.sm = id;
            this.rI.add(2);
            return this;
        }

        public Builder setResult(ItemScope result) {
            this.sG = (fq) result;
            this.rI.add(4);
            return this;
        }

        public Builder setStartDate(String startDate) {
            this.sx = startDate;
            this.rI.add(5);
            return this;
        }

        public Builder setTarget(ItemScope target) {
            this.sH = (fq) target;
            this.rI.add(6);
            return this;
        }

        public Builder setType(String type) {
            this.sD = type;
            this.rI.add(7);
            return this;
        }
    }

    String getId();

    ItemScope getResult();

    String getStartDate();

    ItemScope getTarget();

    String getType();

    boolean hasId();

    boolean hasResult();

    boolean hasStartDate();

    boolean hasTarget();

    boolean hasType();
}
