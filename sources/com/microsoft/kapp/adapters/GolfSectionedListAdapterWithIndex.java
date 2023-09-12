package com.microsoft.kapp.adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.microsoft.kapp.views.astickyheader.PinnedSectionListView;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
/* loaded from: classes.dex */
public class GolfSectionedListAdapterWithIndex extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter {
    private ArrayAdapter mBaseAdapter;
    private int mHeaderTextViewResId;
    private LayoutInflater mLayoutInflater;
    private int mSectionResourceId;
    private boolean mValid = true;
    private SparseArray<Section> mSections = new SparseArray<>();

    /* loaded from: classes.dex */
    public static class Section {
        int adjustedPosition;
        int firstPosition;
        CharSequence title;

        public Section(int firstPosition, CharSequence title) {
            this.firstPosition = firstPosition;
            this.title = title;
        }

        public CharSequence getTitle() {
            return this.title;
        }
    }

    public GolfSectionedListAdapterWithIndex(Context context, ArrayAdapter baseAdapter, int sectionResourceId, int headerTextViewResId) {
        this.mLayoutInflater = (LayoutInflater) context.getSystemService("layout_inflater");
        this.mSectionResourceId = sectionResourceId;
        this.mHeaderTextViewResId = headerTextViewResId;
        this.mBaseAdapter = baseAdapter;
        this.mBaseAdapter.registerDataSetObserver(new DataSetObserver() { // from class: com.microsoft.kapp.adapters.GolfSectionedListAdapterWithIndex.1
            @Override // android.database.DataSetObserver
            public void onChanged() {
                GolfSectionedListAdapterWithIndex.this.mValid = !GolfSectionedListAdapterWithIndex.this.mBaseAdapter.isEmpty();
                GolfSectionedListAdapterWithIndex.this.notifyDataSetChanged();
            }

            @Override // android.database.DataSetObserver
            public void onInvalidated() {
                GolfSectionedListAdapterWithIndex.this.mValid = false;
                GolfSectionedListAdapterWithIndex.this.notifyDataSetInvalidated();
            }
        });
    }

    public void setSections(List<Section> sections) {
        this.mSections.clear();
        Collections.sort(sections, new Comparator<Section>() { // from class: com.microsoft.kapp.adapters.GolfSectionedListAdapterWithIndex.2
            @Override // java.util.Comparator
            public int compare(Section o, Section o1) {
                if (o.firstPosition == o1.firstPosition) {
                    return 0;
                }
                return o.firstPosition < o1.firstPosition ? -1 : 1;
            }
        });
        int offset = 0;
        for (Section section : sections) {
            if (section != null) {
                section.adjustedPosition = section.firstPosition + offset;
                this.mSections.append(section.adjustedPosition, section);
                offset++;
            }
        }
        notifyDataSetChanged();
    }

    public int positionToSectionedPosition(int position) {
        int offset = 0;
        for (int i = 0; i < this.mSections.size() && this.mSections.valueAt(i).firstPosition <= position; i++) {
            offset++;
        }
        return position + offset;
    }

    public int sectionedPositionToPosition(int sectionedPosition) {
        if (isSectionHeaderPosition(sectionedPosition)) {
            return -1;
        }
        int offset = 0;
        for (int i = 0; i < this.mSections.size() && this.mSections.valueAt(i).adjustedPosition <= sectionedPosition; i++) {
            offset--;
        }
        return sectionedPosition + offset;
    }

    public boolean isSectionHeaderPosition(int position) {
        return this.mSections.get(position) != null;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        if (this.mValid) {
            return this.mBaseAdapter.getCount() + this.mSections.size();
        }
        return 0;
    }

    @Override // android.widget.Adapter
    public Object getItem(int position) {
        return isSectionHeaderPosition(position) ? this.mSections.get(position) : this.mBaseAdapter.getItem(sectionedPositionToPosition(position));
    }

    @Override // android.widget.Adapter
    public long getItemId(int position) {
        return isSectionHeaderPosition(position) ? Integer.MAX_VALUE - this.mSections.indexOfKey(position) : this.mBaseAdapter.getItemId(sectionedPositionToPosition(position));
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public int getItemViewType(int position) {
        return isSectionHeaderPosition(position) ? getViewTypeCount() - 1 : this.mBaseAdapter.getItemViewType(sectionedPositionToPosition(position));
    }

    @Override // android.widget.BaseAdapter, android.widget.ListAdapter
    public boolean isEnabled(int position) {
        if (isSectionHeaderPosition(position)) {
            return false;
        }
        return this.mBaseAdapter.isEnabled(sectionedPositionToPosition(position));
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public int getViewTypeCount() {
        return this.mBaseAdapter.getViewTypeCount() + 1;
    }

    @Override // android.widget.BaseAdapter, android.widget.ListAdapter
    public boolean areAllItemsEnabled() {
        return this.mBaseAdapter.areAllItemsEnabled();
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public boolean hasStableIds() {
        return this.mBaseAdapter.hasStableIds();
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public boolean isEmpty() {
        return this.mBaseAdapter.isEmpty();
    }

    public ArrayAdapter getAdapter() {
        return this.mBaseAdapter;
    }

    @Override // android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        if (isSectionHeaderPosition(position)) {
            if (convertView == null) {
                convertView = this.mLayoutInflater.inflate(this.mSectionResourceId, parent, false);
            } else if (convertView.findViewById(this.mHeaderTextViewResId) == null) {
                convertView = this.mLayoutInflater.inflate(this.mSectionResourceId, parent, false);
            }
            TextView view = (TextView) convertView.findViewById(this.mHeaderTextViewResId);
            view.setText(this.mSections.get(position).title);
            return convertView;
        }
        return this.mBaseAdapter.getView(sectionedPositionToPosition(position), convertView, parent);
    }

    @Override // com.microsoft.kapp.views.astickyheader.PinnedSectionListView.PinnedSectionListAdapter
    public boolean isItemViewTypePinned(int position) {
        return isSectionHeaderPosition(position);
    }

    @Override // com.microsoft.kapp.views.astickyheader.PinnedSectionListView.PinnedSectionListAdapter
    public String[] getSections() {
        String[] sections = new String[this.mSections.size()];
        for (int i = 0; i < this.mSections.size(); i++) {
            sections[i] = String.valueOf(this.mSections.valueAt(i).title);
        }
        return sections;
    }

    @Override // com.microsoft.kapp.views.astickyheader.PinnedSectionListView.PinnedSectionListAdapter
    public int getPositionForSection(int sectionIndex) {
        return this.mSections.valueAt(sectionIndex).adjustedPosition;
    }
}
