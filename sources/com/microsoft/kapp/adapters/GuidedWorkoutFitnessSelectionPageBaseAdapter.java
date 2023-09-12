package com.microsoft.kapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.utils.ViewUtils;
import java.util.List;
/* loaded from: classes.dex */
public class GuidedWorkoutFitnessSelectionPageBaseAdapter extends ArrayAdapter<String> {
    private static final int RESOURCE_ID = 2130903067;

    /* loaded from: classes.dex */
    public static class ViewHolder {
        public TextView name;
    }

    public GuidedWorkoutFitnessSelectionPageBaseAdapter(Context context, List<String> goalsList) {
        super(context, (int) R.layout.adapter_fitness_selection_list_single_item, goalsList);
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService("layout_inflater");
            convertView = inflater.inflate(R.layout.adapter_fitness_selection_list_single_item, (ViewGroup) null);
            holder = new ViewHolder();
            holder.name = (TextView) ViewUtils.getValidView(convertView, R.id.name, TextView.class);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String levelName = getItem(position);
        holder.name.setText(levelName);
        return convertView;
    }
}
