package com.google.android.gms.dynamic;

import android.os.IBinder;
import com.google.android.gms.dynamic.b;
import java.lang.reflect.Field;
/* loaded from: classes.dex */
public final class c<T> extends b.a {
    private final T mh;

    private c(T t) {
        this.mh = t;
    }

    public static <T> T b(b bVar) {
        if (bVar instanceof c) {
            return ((c) bVar).mh;
        }
        IBinder asBinder = bVar.asBinder();
        Field[] declaredFields = asBinder.getClass().getDeclaredFields();
        if (declaredFields.length == 1) {
            Field field = declaredFields[0];
            if (field.isAccessible()) {
                throw new IllegalArgumentException("The concrete class implementing IObjectWrapper must have exactly one declared *private* field for the wrapped object. Preferably, this is an instance of the ObjectWrapper<T> class.");
            }
            field.setAccessible(true);
            try {
                return (T) field.get(asBinder);
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException("Could not access the field in remoteBinder.", e);
            } catch (IllegalArgumentException e2) {
                throw new IllegalArgumentException("remoteBinder is the wrong class.", e2);
            } catch (NullPointerException e3) {
                throw new IllegalArgumentException("Binder object is null.", e3);
            }
        }
        throw new IllegalArgumentException("The concrete class implementing IObjectWrapper must have exactly *one* declared private field for the wrapped object.  Preferably, this is an instance of the ObjectWrapper<T> class.");
    }

    public static <T> b g(T t) {
        return new c(t);
    }
}
