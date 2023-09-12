package com.microsoft.kapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.models.strapp.StrappState;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.StrappUtils;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.views.CustomFontTextView;
import com.microsoft.kapp.views.CustomGlyphView;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class StrappStateAdapter extends BaseExpandableListAdapter {
    private static final int CHILD_RESOURCE_ID = 2130903080;
    private static final int GROUP_RESOURCE_ID = 2130903079;
    private static final int MAX_STRAPPS_IN_GROUP = 200;
    private Context mContext;
    private ArrayList<ArrayList<StrappState>> mStrapps = new ArrayList<>();
    private View.OnClickListener mSwitchOnClickListener;

    /* loaded from: classes.dex */
    private static class ViewHolder {
        private CustomGlyphView mEditableIcon;
        private Switch mEnabledSwitch;
        private CustomFontTextView mNameTextView;
        private CustomGlyphView mTileBackgroundIcon;
        private CustomGlyphView mTileGlyph;
        private ImageView mTileIcon;

        private ViewHolder() {
        }
    }

    /* loaded from: classes.dex */
    private static class GroupViewHolder {
        private TextView mGroupNameTextView;

        private GroupViewHolder() {
        }
    }

    public StrappStateAdapter(Context context, ArrayList<StrappState> firstPartyStrapps, ArrayList<StrappState> thirdPartyStrapps) {
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO);
        Validate.notNull(firstPartyStrapps, "firstPartyStrapps");
        this.mContext = context;
        this.mStrapps.add(firstPartyStrapps);
        this.mStrapps.add(thirdPartyStrapps);
    }

    public void setOnSwitchClickListener(View.OnClickListener listener) {
        this.mSwitchOnClickListener = listener;
    }

    @Override // android.widget.ExpandableListAdapter
    public int getGroupCount() {
        return this.mStrapps.size();
    }

    @Override // android.widget.ExpandableListAdapter
    public int getChildrenCount(int groupPosition) {
        if (this.mStrapps.get(groupPosition) != null) {
            return this.mStrapps.get(groupPosition).size();
        }
        return 0;
    }

    @Override // android.widget.ExpandableListAdapter
    public Object getGroup(int groupPosition) {
        return Integer.valueOf(groupPosition);
    }

    @Override // android.widget.ExpandableListAdapter
    public Object getChild(int groupPosition, int childPosition) {
        return this.mStrapps.get(groupPosition).get(childPosition);
    }

    @Override // android.widget.ExpandableListAdapter
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override // android.widget.ExpandableListAdapter
    public long getChildId(int groupPosition, int childPosition) {
        return ((groupPosition + 1) * MAX_STRAPPS_IN_GROUP) + childPosition;
    }

    @Override // android.widget.ExpandableListAdapter
    public boolean hasStableIds() {
        return true;
    }

    @Override // android.widget.ExpandableListAdapter
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService("layout_inflater");
            convertView = inflater.inflate(R.layout.adapter_strapp_state_group, parent, false);
            holder = new GroupViewHolder();
            holder.mGroupNameTextView = (TextView) ViewUtils.getValidView(convertView, R.id.group_text_view, TextView.class);
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        if (groupPosition == 0) {
            holder.mGroupNameTextView.setVisibility(8);
        } else {
            holder.mGroupNameTextView.setVisibility(0);
            holder.mGroupNameTextView.setText(this.mContext.getResources().getString(R.string.strapps_third_party_tiles));
        }
        ExpandableListView expandableListView = (ExpandableListView) parent;
        expandableListView.expandGroup(groupPosition);
        return convertView;
    }

    @Override // android.widget.ExpandableListAdapter
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService("layout_inflater");
            convertView = inflater.inflate(R.layout.adapter_strapp_state_v1, parent, false);
            holder = new ViewHolder();
            holder.mTileGlyph = (CustomGlyphView) ViewUtils.getValidView(convertView, R.id.manage_strapps_tile_glyph, CustomGlyphView.class);
            holder.mTileIcon = (ImageView) ViewUtils.getValidView(convertView, R.id.manage_strapps_tile_icon, ImageView.class);
            holder.mTileBackgroundIcon = (CustomGlyphView) ViewUtils.getValidView(convertView, R.id.manage_strapps_tile_icon_background, CustomGlyphView.class);
            holder.mNameTextView = (CustomFontTextView) ViewUtils.getValidView(convertView, R.id.name_text_view, CustomFontTextView.class);
            holder.mEditableIcon = (CustomGlyphView) ViewUtils.getValidView(convertView, R.id.manage_strapps_editable_icon, CustomGlyphView.class);
            holder.mEnabledSwitch = (Switch) ViewUtils.getValidView(convertView, R.id.enabled_switch, Switch.class);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.mEnabledSwitch.setOnCheckedChangeListener(null);
        }
        final StrappState strappState = (StrappState) getChild(groupPosition, childPosition);
        String foregroundGlyph = StrappUtils.lookupGlyphForUuid(strappState.getDefinition().getStrappId(), this.mContext);
        if (foregroundGlyph != null) {
            holder.mTileGlyph.setText(foregroundGlyph);
            holder.mTileGlyph.setVisibility(0);
            holder.mTileIcon.setVisibility(4);
        } else if (strappState.getDefinition().isThirdPartyStrapp()) {
            holder.mTileGlyph.setVisibility(4);
            holder.mTileIcon.setVisibility(0);
            holder.mTileIcon.setImageBitmap(strappState.getDefinition().getIcon());
        } else {
            holder.mTileGlyph.setText("");
            holder.mTileGlyph.setVisibility(0);
            holder.mTileIcon.setVisibility(4);
        }
        String backgroundGlyph = StrappUtils.lookupBackGlyphForUuid(strappState.getDefinition().getStrappId(), this.mContext);
        if (backgroundGlyph == null || backgroundGlyph.isEmpty()) {
            holder.mTileBackgroundIcon.setText("");
        } else {
            int backgroundColor = StrappUtils.lookupGlyhpBackgroundColorForUUID(strappState.getDefinition().getStrappId(), this.mContext);
            holder.mTileBackgroundIcon.setText(backgroundGlyph);
            holder.mTileBackgroundIcon.setTextColor(backgroundColor);
        }
        String strappName = strappState.getDefinition().getName(this.mContext);
        holder.mNameTextView.setText(strappName);
        if (StrappUtils.isSettingsEnabledStrapp(strappState.getDefinition().getStrappId())) {
            holder.mEditableIcon.setVisibility(0);
        } else {
            holder.mEditableIcon.setVisibility(8);
        }
        Switch enabledSwitch = holder.mEnabledSwitch;
        enabledSwitch.setTag(strappState.getDefinition().getStrappId());
        enabledSwitch.setChecked(strappState.isEnabled());
        enabledSwitch.setEnabled(strappState.isStateChangeAllowed());
        if (holder.mEnabledSwitch.isChecked()) {
            holder.mEditableIcon.setTextColor(this.mContext.getResources().getColorStateList(R.color.button_feedback));
        } else {
            holder.mEditableIcon.setTextColor(this.mContext.getResources().getColor(R.color.greyHigh));
        }
        enabledSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.microsoft.kapp.adapters.StrappStateAdapter.1
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                strappState.setIsEnabled(isChecked);
                if (StrappStateAdapter.this.mSwitchOnClickListener != null) {
                    StrappStateAdapter.this.mSwitchOnClickListener.onClick(buttonView);
                }
            }
        });
        return convertView;
    }

    @Override // android.widget.ExpandableListAdapter
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
