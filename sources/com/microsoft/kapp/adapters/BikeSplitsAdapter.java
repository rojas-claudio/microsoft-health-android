package com.microsoft.kapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.models.BikeSplitSummary;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.views.CustomGlyphView;
import java.util.List;
/* loaded from: classes.dex */
public class BikeSplitsAdapter extends BaseAdapter {
    private Context mContext;
    private List<BikeSplitSummary> mSplits;

    public BikeSplitsAdapter(Context context, List<BikeSplitSummary> splits) {
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO);
        this.mContext = context;
        this.mSplits = splits;
    }

    /* loaded from: classes.dex */
    private static class ViewHolder {
        TextView mDistance;
        TextView mElevation;
        CustomGlyphView mGlyph;
        TextView mSpeed;

        private ViewHolder() {
        }
    }

    @Override // android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.bike_splits_list_row, parent, false);
            viewHolder.mGlyph = (CustomGlyphView) ViewUtils.getValidView(convertView, R.id.bike_split_glyph, CustomGlyphView.class);
            viewHolder.mDistance = (TextView) ViewUtils.getValidView(convertView, R.id.bike_split_distance, TextView.class);
            viewHolder.mSpeed = (TextView) ViewUtils.getValidView(convertView, R.id.bike_split_speed, TextView.class);
            viewHolder.mElevation = (TextView) ViewUtils.getValidView(convertView, R.id.bike_split_elevation, TextView.class);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        BikeSplitSummary split = (BikeSplitSummary) getItem(position);
        viewHolder.mDistance.setText(split.getDistance());
        viewHolder.mSpeed.setText(split.getSpeed());
        viewHolder.mElevation.setText(split.getElevation());
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

    @Override // android.widget.Adapter
    public int getCount() {
        return this.mSplits.size();
    }

    @Override // android.widget.Adapter
    public Object getItem(int position) {
        return this.mSplits.get(position);
    }

    @Override // android.widget.Adapter
    public long getItemId(int position) {
        return 0L;
    }

    @Override // android.widget.BaseAdapter, android.widget.ListAdapter
    public boolean isEnabled(int position) {
        return false;
    }
}
