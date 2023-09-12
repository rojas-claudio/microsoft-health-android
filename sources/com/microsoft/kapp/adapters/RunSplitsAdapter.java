package com.microsoft.kapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.models.RunSplitSummary;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.views.CustomGlyphView;
import java.util.List;
/* loaded from: classes.dex */
public class RunSplitsAdapter extends ArrayAdapter<RunSplitSummary> {
    public RunSplitsAdapter(Context context, List<RunSplitSummary> objects) {
        super(context, 0, objects);
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.run_splits_list_row, parent, false);
            viewHolder.mGlyph = (CustomGlyphView) ViewUtils.getValidView(convertView, R.id.run_split_glyph, CustomGlyphView.class);
            viewHolder.mDistance = (TextView) ViewUtils.getValidView(convertView, R.id.run_split_distance, TextView.class);
            viewHolder.mDuration = (TextView) ViewUtils.getValidView(convertView, R.id.run_split_duration, TextView.class);
            viewHolder.mAvgBpm = (TextView) ViewUtils.getValidView(convertView, R.id.run_split_bpm, TextView.class);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        RunSplitSummary split = getItem(position);
        viewHolder.mDistance.setText(split.getDistance());
        viewHolder.mDuration.setText(split.getPace());
        viewHolder.mAvgBpm.setText(split.getHeartRate());
        switch (split.getSplitType()) {
            case SLOWEST:
                viewHolder.mGlyph.setGlyph(R.string.glyph_slow);
                viewHolder.mGlyph.setVisibility(0);
                break;
            case FASTEST:
                viewHolder.mGlyph.setGlyph(R.string.glyph_fast);
                viewHolder.mGlyph.setVisibility(0);
                break;
            default:
                viewHolder.mGlyph.setVisibility(4);
                break;
        }
        return convertView;
    }

    /* loaded from: classes.dex */
    private static class ViewHolder {
        TextView mAvgBpm;
        TextView mDistance;
        TextView mDuration;
        CustomGlyphView mGlyph;

        private ViewHolder() {
        }
    }
}
