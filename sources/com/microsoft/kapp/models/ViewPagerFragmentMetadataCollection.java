package com.microsoft.kapp.models;

import android.support.v4.app.Fragment;
import com.microsoft.kapp.diagnostics.Validate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/* loaded from: classes.dex */
public class ViewPagerFragmentMetadataCollection implements Collection<ViewPagerFragmentMetadata> {
    private final List<ViewPagerFragmentMetadata> mList = new ArrayList();
    private final Map<Class<?>, Integer> mClassToIndexMap = new HashMap();

    @Override // java.util.Collection
    public boolean add(ViewPagerFragmentMetadata object) {
        this.mList.add(object);
        this.mClassToIndexMap.put(object.getFragmentClass(), Integer.valueOf(this.mList.size() - 1));
        return true;
    }

    public boolean add(int titleResourceId, int iconResourceId, Class<? extends Fragment> fragmentClass) {
        ViewPagerFragmentMetadata context = new ViewPagerFragmentMetadata(titleResourceId, iconResourceId, fragmentClass);
        return add(context);
    }

    @Override // java.util.Collection
    public boolean addAll(Collection<? extends ViewPagerFragmentMetadata> collection) {
        for (ViewPagerFragmentMetadata context : collection) {
            add(context);
        }
        return true;
    }

    @Override // java.util.Collection
    public void clear() {
        this.mList.clear();
        this.mClassToIndexMap.clear();
    }

    @Override // java.util.Collection
    public boolean contains(Object object) {
        return this.mList.contains(object);
    }

    @Override // java.util.Collection
    public boolean containsAll(Collection<?> collection) {
        return this.mList.containsAll(collection);
    }

    @Override // java.util.Collection
    public boolean isEmpty() {
        return this.mList.isEmpty();
    }

    @Override // java.util.Collection, java.lang.Iterable
    public Iterator<ViewPagerFragmentMetadata> iterator() {
        return this.mList.iterator();
    }

    @Override // java.util.Collection
    public boolean remove(Object object) {
        Validate.isTrue(object instanceof ViewPagerFragmentMetadata, "object is not an instance of ViewPagerFragmentMetadata");
        ViewPagerFragmentMetadata context = (ViewPagerFragmentMetadata) object;
        boolean result = this.mList.remove(context);
        if (result) {
            this.mClassToIndexMap.remove(context.getFragmentClass());
        }
        return result;
    }

    @Override // java.util.Collection
    public boolean removeAll(Collection<?> collection) {
        boolean result = false;
        for (Object object : collection) {
            result |= remove(object);
        }
        return result;
    }

    @Override // java.util.Collection
    public boolean retainAll(Collection<?> collection) {
        throw new UnsupportedOperationException("retainAll is not supported.");
    }

    @Override // java.util.Collection
    public int size() {
        return this.mList.size();
    }

    @Override // java.util.Collection
    public Object[] toArray() {
        return this.mList.toArray();
    }

    @Override // java.util.Collection
    public <T> T[] toArray(T[] array) {
        return (T[]) this.mList.toArray(array);
    }

    public ViewPagerFragmentMetadata get(int index) {
        if (index < 0 || index >= this.mList.size()) {
            throw new IndexOutOfBoundsException("location is outside of the bounds.");
        }
        return this.mList.get(index);
    }

    public int findIndexOfFragment(Class<? extends Fragment> fragment) {
        Validate.notNull(fragment, "fragment");
        Integer index = this.mClassToIndexMap.get(fragment);
        if (index == null) {
            String message = String.format("The specified fragment %s does not exist", fragment.getSimpleName());
            throw new IllegalStateException(message);
        }
        return index.intValue();
    }
}
