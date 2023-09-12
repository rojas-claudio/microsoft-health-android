package org.apache.commons.lang3.mutable;

import java.io.Serializable;
/* loaded from: classes.dex */
public class MutableObject<T> implements Mutable<T>, Serializable {
    private static final long serialVersionUID = 86241875189L;
    private T value;

    public MutableObject() {
    }

    public MutableObject(T value) {
        this.value = value;
    }

    @Override // org.apache.commons.lang3.mutable.Mutable
    public T getValue() {
        return this.value;
    }

    @Override // org.apache.commons.lang3.mutable.Mutable
    public void setValue(T value) {
        this.value = value;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (getClass() == obj.getClass()) {
            MutableObject<?> that = (MutableObject) obj;
            return this.value.equals(that.value);
        }
        return false;
    }

    public int hashCode() {
        if (this.value == null) {
            return 0;
        }
        return this.value.hashCode();
    }

    public String toString() {
        return this.value == null ? "null" : this.value.toString();
    }
}
