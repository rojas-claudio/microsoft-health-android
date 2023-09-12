package com.microsoft.kapp.fragments.golf;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.models.golf.GolfSearchResultItem;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.utils.Formatter;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.krestsdk.models.GolfCourseType;
import java.util.List;
/* loaded from: classes.dex */
public class GolfSearchResultsAdapter extends ArrayAdapter<GolfSearchResultItem> {
    private Context mContext;
    private SettingsProvider mSettingsProvider;

    /* loaded from: classes.dex */
    public static class GolfCourseItemViewHolder {
        public TextView city;
        public String courseId;
        public TextView distance;
        public View distanceContainer;
        public TextView holes;
        public boolean isDistanceAvailable;
        public TextView name;
        public TextView publicPrivate;
    }

    public GolfSearchResultsAdapter(Context context, SettingsProvider settingsProvider, List<GolfSearchResultItem> objects) {
        super(context, 0, objects);
        this.mContext = context;
        this.mSettingsProvider = settingsProvider;
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        GolfCourseItemViewHolder holder;
        if (convertView == null) {
            holder = new GolfCourseItemViewHolder();
            LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService("layout_inflater");
            convertView = inflater.inflate(R.layout.golf_search_listitem, parent, false);
            holder.name = (TextView) ViewUtils.getValidView(convertView, R.id.golf_search_item_course_name, TextView.class);
            holder.distanceContainer = (View) ViewUtils.getValidView(convertView, R.id.golf_search_item_distance_container, LinearLayout.class);
            holder.distance = (TextView) ViewUtils.getValidView(convertView, R.id.golf_search_item_course_distance, TextView.class);
            holder.publicPrivate = (TextView) ViewUtils.getValidView(convertView, R.id.golf_search_item_course_public_private, TextView.class);
            holder.holes = (TextView) ViewUtils.getValidView(convertView, R.id.golf_search_item_course_holes, TextView.class);
            holder.city = (TextView) ViewUtils.getValidView(convertView, R.id.golf_search_item_course_city, TextView.class);
            convertView.setTag(holder);
        } else {
            holder = (GolfCourseItemViewHolder) convertView.getTag();
        }
        GolfSearchResultItem searchItem = getItem(position);
        holder.name.setText(searchItem.getName());
        holder.distance.setText(Formatter.formatDistanceToOneDecimalUnitLocked(this.mContext, searchItem.getDistance(), this.mSettingsProvider.isDistanceHeightMetric()));
        holder.publicPrivate.setText(GolfCourseType.getDisplayValue(this.mContext, searchItem.getCourseType()));
        holder.holes.setText(String.valueOf(searchItem.getNumberOfHoles()));
        holder.city.setText(searchItem.getCity());
        holder.courseId = searchItem.getCourseId();
        holder.isDistanceAvailable = searchItem.isDistanceAvailable();
        if (holder.isDistanceAvailable) {
            holder.distanceContainer.setVisibility(0);
        } else {
            holder.distanceContainer.setVisibility(8);
        }
        return convertView;
    }
}
