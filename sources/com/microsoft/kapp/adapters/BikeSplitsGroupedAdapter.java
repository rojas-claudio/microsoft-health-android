package com.microsoft.kapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.models.BikeSplitSummary;
import com.microsoft.kapp.models.BikeSplitSummaryGroup;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.views.CustomGlyphView;
import java.util.List;
import org.apache.commons.lang3.Validate;
/* loaded from: classes.dex */
public class BikeSplitsGroupedAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<BikeSplitSummaryGroup> mGroups;

    public BikeSplitsGroupedAdapter(Context context, List<BikeSplitSummaryGroup> splitGroups) {
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO, new Object[0]);
        this.mContext = context;
        this.mGroups = splitGroups;
    }

    /* loaded from: classes.dex */
    private static class ViewHolder {
        CustomGlyphView mArrow;
        TextView mDistance;
        View mDivider;
        TextView mElevation;
        CustomGlyphView mGlyph;
        TextView mSpeed;

        private ViewHolder() {
        }
    }

    @Override // android.widget.ExpandableListAdapter
    public int getGroupCount() {
        return this.mGroups.size();
    }

    @Override // android.widget.ExpandableListAdapter
    public int getChildrenCount(int groupPosition) {
        return this.mGroups.get(groupPosition).getSplitCount();
    }

    @Override // android.widget.ExpandableListAdapter
    public Object getGroup(int groupPosition) {
        return this.mGroups.get(groupPosition);
    }

    @Override // android.widget.ExpandableListAdapter
    public Object getChild(int groupPosition, int childPosition) {
        return this.mGroups.get(groupPosition).getSplitAt(childPosition);
    }

    @Override // android.widget.ExpandableListAdapter
    public long getGroupId(int groupPosition) {
        return 0L;
    }

    @Override // android.widget.ExpandableListAdapter
    public long getChildId(int groupPosition, int childPosition) {
        return 0L;
    }

    @Override // android.widget.ExpandableListAdapter
    public boolean hasStableIds() {
        return false;
    }

    @Override // android.widget.ExpandableListAdapter
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.bike_splits_list_group, parent, false);
            viewHolder.mArrow = (CustomGlyphView) ViewUtils.getValidView(convertView, R.id.bike_split_glyph_expand_arrow, CustomGlyphView.class);
            viewHolder.mGlyph = (CustomGlyphView) ViewUtils.getValidView(convertView, R.id.bike_split_header_glyph, CustomGlyphView.class);
            viewHolder.mDistance = (TextView) ViewUtils.getValidView(convertView, R.id.bike_split_group_distance, TextView.class);
            viewHolder.mSpeed = (TextView) ViewUtils.getValidView(convertView, R.id.bike_split_group_average_speed, TextView.class);
            viewHolder.mElevation = (TextView) ViewUtils.getValidView(convertView, R.id.bike_split_group_elevation, TextView.class);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        BikeSplitSummaryGroup group = this.mGroups.get(groupPosition);
        viewHolder.mDistance.setText(group.getDistance());
        viewHolder.mSpeed.setText(group.getSpeed());
        viewHolder.mElevation.setText(group.getElevation());
        if (isExpanded) {
            viewHolder.mArrow.setGlyph(R.string.glyph_triangle_down);
        } else {
            viewHolder.mArrow.setGlyph(R.string.glyph_triangle_right);
        }
        switch (group.getSplitType()) {
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

    @Override // android.widget.ExpandableListAdapter
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.bike_splits_list_row, parent, false);
            viewHolder.mGlyph = (CustomGlyphView) ViewUtils.getValidView(convertView, R.id.bike_split_glyph, CustomGlyphView.class);
            viewHolder.mDistance = (TextView) ViewUtils.getValidView(convertView, R.id.bike_split_distance, TextView.class);
            viewHolder.mSpeed = (TextView) ViewUtils.getValidView(convertView, R.id.bike_split_speed, TextView.class);
            viewHolder.mElevation = (TextView) ViewUtils.getValidView(convertView, R.id.bike_split_elevation, TextView.class);
            viewHolder.mDivider = (View) ViewUtils.getValidView(convertView, R.id.bike_split_divider, View.class);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        BikeSplitSummary split = this.mGroups.get(groupPosition).getSplitAt(childPosition);
        viewHolder.mDistance.setText(split.getDistance());
        viewHolder.mSpeed.setText(split.getSpeed());
        viewHolder.mElevation.setText(split.getElevation());
        viewHolder.mDivider.setVisibility(isLastChild ? 4 : 0);
        return convertView;
    }

    @Override // android.widget.ExpandableListAdapter
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
