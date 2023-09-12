package com.microsoft.kapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.models.WorkoutScheduleItem;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.views.CustomGlyphView;
import com.microsoft.krestsdk.models.UserWorkoutStatus;
import java.util.List;
/* loaded from: classes.dex */
public class WorkoutScheduleItemAdapter extends BaseExpandableListAdapter {
    private static final int RESOURCE_HEADER_ID = 2130903083;
    private static final int RESOURCE_ITEM_ID = 2130903084;
    private static String mWeekTextformat;
    private boolean mAllowSync;
    private Context mContext;
    private int mTotalWeeks;
    private List<WorkoutScheduleItem> mWorkouts;

    /* loaded from: classes.dex */
    private static class ViewHolder {
        public TextView name;
        public TextView onBandText;
        public FrameLayout syncContainer;
        public ProgressBar syncIndicator;
        public CustomGlyphView syncedBox;
        public TextView workoutNumber;

        private ViewHolder() {
        }
    }

    /* loaded from: classes.dex */
    private static class HeaderViewHolder {
        public TextView groupIcon;
        public TextView name;

        private HeaderViewHolder() {
        }
    }

    public WorkoutScheduleItemAdapter(Context context, List<WorkoutScheduleItem> workouts, int totalWeeks, boolean allowSync) {
        this.mContext = context;
        this.mWorkouts = workouts;
        this.mTotalWeeks = totalWeeks;
        mWeekTextformat = this.mContext.getResources().getString(R.string.workout_schedule_header_week_text);
        this.mAllowSync = allowSync;
    }

    @Override // android.widget.ExpandableListAdapter
    public int getGroupCount() {
        return this.mTotalWeeks;
    }

    @Override // android.widget.ExpandableListAdapter
    public int getChildrenCount(int groupPosition) {
        int count = groupPosition < this.mTotalWeeks + (-1) ? 7 : this.mWorkouts.size() % 7;
        if (count == 0) {
            return 7;
        }
        return count;
    }

    @Override // android.widget.ExpandableListAdapter
    public Object getGroup(int groupPosition) {
        int startIndex = groupPosition * 7;
        int endIndex = Math.min(((groupPosition + 1) * 7) - 1, this.mWorkouts.size() - startIndex);
        return this.mWorkouts.subList(startIndex, endIndex);
    }

    @Override // android.widget.ExpandableListAdapter
    public Object getChild(int groupPosition, int childPosition) {
        return this.mWorkouts.get((groupPosition * 7) + childPosition);
    }

    @Override // android.widget.ExpandableListAdapter
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override // android.widget.ExpandableListAdapter
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override // android.widget.ExpandableListAdapter
    public boolean hasStableIds() {
        return false;
    }

    @Override // android.widget.ExpandableListAdapter
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            HeaderViewHolder holder = new HeaderViewHolder();
            LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService("layout_inflater");
            convertView = inflater.inflate(R.layout.adapter_workout_schedule_header, parent, false);
            holder.name = (TextView) ViewUtils.getValidView(convertView, R.id.workout_schedule_header_name, TextView.class);
            holder.groupIcon = (TextView) ViewUtils.getValidView(convertView, R.id.group_icon, TextView.class);
            convertView.setTag(holder);
        }
        HeaderViewHolder holder2 = (HeaderViewHolder) convertView.getTag();
        holder2.name.setText(String.format(mWeekTextformat, Integer.valueOf(groupPosition + 1)));
        if (isExpanded) {
            holder2.groupIcon.setText(R.string.glyph_triangle_down);
        } else {
            holder2.groupIcon.setText(R.string.glyph_triangle_right);
        }
        return convertView;
    }

    @Override // android.widget.ExpandableListAdapter
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder holder;
        int nameColor;
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService("layout_inflater");
            view = inflater.inflate(R.layout.adapter_workout_schedule_item, (ViewGroup) null);
            holder = new ViewHolder();
            holder.name = (TextView) ViewUtils.getValidView(view, R.id.workout_schedule_item_name, TextView.class);
            holder.workoutNumber = (TextView) ViewUtils.getValidView(view, R.id.workout_schedule_item_number, TextView.class);
            holder.syncedBox = (CustomGlyphView) ViewUtils.getValidView(view, R.id.workout_schedule_item_synced, CustomGlyphView.class);
            holder.onBandText = (TextView) ViewUtils.getValidView(view, R.id.workout_schedule_item_on_band, TextView.class);
            holder.syncIndicator = (ProgressBar) ViewUtils.getValidView(view, R.id.workout_schedule_item_sync_progress, ProgressBar.class);
            holder.syncContainer = (FrameLayout) ViewUtils.getValidView(view, R.id.container_workout_sync, FrameLayout.class);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        int currentPosition = (groupPosition * 7) + childPosition;
        final WorkoutScheduleItem scheduleItem = this.mWorkouts.get(currentPosition);
        holder.workoutNumber.setText(String.valueOf(childPosition + 1));
        holder.onBandText.setVisibility(8);
        if (!this.mAllowSync) {
            holder.syncContainer.setVisibility(8);
        }
        if (scheduleItem.isRest()) {
            holder.name.setText(this.mContext.getResources().getString(R.string.workout_exercise_rest_step));
            holder.syncIndicator.setVisibility(8);
            holder.syncedBox.setVisibility(8);
            if (scheduleItem.getCompletionStatus() == UserWorkoutStatus.SKIPPED) {
                holder.syncedBox.setVisibility(0);
                holder.syncedBox.setText(this.mContext.getResources().getString(R.string.glyph_check));
            }
        } else {
            holder.name.setText(scheduleItem.getName());
            if (scheduleItem.getSyncStatus() == null) {
                holder.syncedBox.setVisibility(8);
                holder.syncIndicator.setVisibility(0);
            } else {
                holder.syncIndicator.setVisibility(8);
                if (scheduleItem.getCompletionStatus() == UserWorkoutStatus.COMPLETED || scheduleItem.getCompletionStatus() == UserWorkoutStatus.ABORTED) {
                    holder.syncedBox.setVisibility(0);
                    holder.syncedBox.setText(this.mContext.getResources().getString(R.string.glyph_check));
                } else if (scheduleItem.getSyncStatus().booleanValue()) {
                    holder.onBandText.setVisibility(0);
                    holder.syncedBox.setVisibility(8);
                } else {
                    holder.syncedBox.setVisibility(0);
                    holder.syncedBox.setText(this.mContext.getResources().getString(R.string.glyph_download_circle));
                }
            }
        }
        if (scheduleItem.getWorkoutClickedListener() != null) {
            holder.name.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.adapters.WorkoutScheduleItemAdapter.1
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    scheduleItem.getWorkoutClickedListener().onWorkoutClicked(scheduleItem);
                }
            });
        }
        if (scheduleItem.getWorkoutToggledListener() != null) {
            holder.syncedBox.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.adapters.WorkoutScheduleItemAdapter.2
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    if (scheduleItem.isEnabled()) {
                        boolean isChecked = !scheduleItem.getSyncStatus().booleanValue();
                        WorkoutScheduleItem.OnWorkoutToggledListener listener = scheduleItem.getWorkoutToggledListener();
                        if (listener != null) {
                            listener.onWorkoutToggled(scheduleItem, isChecked);
                        }
                    }
                }
            });
        }
        if (scheduleItem.isRest()) {
            nameColor = this.mContext.getResources().getColor(R.color.greyHigh);
        } else if (scheduleItem.getSyncStatus() != null && scheduleItem.getSyncStatus().booleanValue()) {
            nameColor = this.mContext.getResources().getColor(R.color.themeHigh);
        } else {
            nameColor = this.mContext.getResources().getColor(R.color.workout_step_normal_text);
        }
        holder.name.setTextColor(nameColor);
        return view;
    }

    @Override // android.widget.ExpandableListAdapter
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
