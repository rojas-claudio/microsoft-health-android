package com.microsoft.kapp.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.models.UserEventSummary;
import com.microsoft.kapp.utils.KAppDateFormatter;
import com.microsoft.kapp.views.CustomGlyphView;
import com.microsoft.krestsdk.models.EventType;
import java.util.List;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class UserEventSummaryAdapter extends ArrayAdapter<UserEventSummary> {
    private static final int RESOURCE_ID = 2130903082;

    /* loaded from: classes.dex */
    private static class ViewHolder {
        public TextView detailsText;
        public TextView headerText;
        public CustomGlyphView mBestsTrophyIcon;
        public TextView metricsText;

        private ViewHolder() {
        }
    }

    public UserEventSummaryAdapter(Context context, List<UserEventSummary> exercises) {
        super(context, (int) R.layout.adapter_user_event_summary, exercises);
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService("layout_inflater");
            view = inflater.inflate(R.layout.adapter_user_event_summary, parent, false);
            holder = new ViewHolder();
            holder.headerText = (TextView) view.findViewById(R.id.user_event_summary_header);
            holder.detailsText = (TextView) view.findViewById(R.id.user_event_summary_details);
            holder.metricsText = (TextView) view.findViewById(R.id.user_event_metrics);
            holder.mBestsTrophyIcon = (CustomGlyphView) view.findViewById(R.id.bests_trophy_icon);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        UserEventSummary summary = getItem(position);
        String name = summary.getName();
        DateTime date = summary.getUserEvent().getEventType() == EventType.Sleeping ? summary.getDate().minusHours(5) : summary.getDate();
        String dateString = KAppDateFormatter.formatToMonthDay(date);
        if (TextUtils.isEmpty(name)) {
            name = summary.getDetails();
        }
        holder.headerText.setText(dateString);
        holder.detailsText.setText(name);
        holder.metricsText.setText(summary.getMetric());
        if (summary.getPersonalBests() != null && !summary.getPersonalBests().isEmpty()) {
            holder.mBestsTrophyIcon.setVisibility(0);
        } else {
            holder.mBestsTrophyIcon.setVisibility(8);
        }
        return view;
    }
}
