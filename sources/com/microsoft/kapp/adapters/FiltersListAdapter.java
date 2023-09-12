package com.microsoft.kapp.adapters;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import com.microsoft.kapp.R;
import com.microsoft.kapp.views.CustomFontTextView;
import java.util.List;
/* loaded from: classes.dex */
public class FiltersListAdapter<T> extends ArrayAdapter<Pair<String, T>> {
    private static final int RESOURCE_ID = 2130903070;

    public FiltersListAdapter(Context context, List<Pair<String, T>> filterTypes) {
        super(context, (int) R.layout.adapter_history_filters_list, filterTypes);
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService("layout_inflater");
            view = inflater.inflate(R.layout.adapter_history_filters_list, (ViewGroup) null);
            holder = new ViewHolder();
            holder.titleText = (CustomFontTextView) view.findViewById(R.id.filter_title);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.titleText.setText((CharSequence) ((Pair) getItem(position)).first);
        return view;
    }

    /* loaded from: classes.dex */
    private static class ViewHolder {
        public CustomFontTextView titleText;

        private ViewHolder() {
        }
    }
}
