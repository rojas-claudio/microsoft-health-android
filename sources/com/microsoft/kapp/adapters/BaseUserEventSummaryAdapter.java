package com.microsoft.kapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.models.BaseUserEventSummary;
import com.microsoft.krestsdk.models.EventType;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
/* loaded from: classes.dex */
public class BaseUserEventSummaryAdapter extends ArrayAdapter<BaseUserEventSummary> {
    private static final int RESOURCE_ID = 2130903065;
    private int mBackgroundResource;
    private int mDetailsTextColor;
    private int mHeaderTextColor;

    /* loaded from: classes.dex */
    private static class ViewHolder {
        public LinearLayout container;
        public TextView detailsText;
        public TextView headerText;

        private ViewHolder() {
        }
    }

    public BaseUserEventSummaryAdapter(Context context, List<BaseUserEventSummary> exercises, int headerTextColor, int detailsTextColor, int backgroundResource) {
        super(context, (int) R.layout.adapter_exercise_summary, exercises);
        this.mHeaderTextColor = headerTextColor;
        this.mDetailsTextColor = detailsTextColor;
        this.mBackgroundResource = backgroundResource;
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        String header;
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService("layout_inflater");
            view = inflater.inflate(R.layout.adapter_exercise_summary, (ViewGroup) null);
            holder = new ViewHolder();
            holder.container = (LinearLayout) view.findViewById(R.id.exercise_summary_container);
            holder.headerText = (TextView) view.findViewById(R.id.exercise_summary_header);
            holder.detailsText = (TextView) view.findViewById(R.id.exercise_summary_details);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        BaseUserEventSummary summary = getItem(position);
        String name = summary.getName();
        DateTime date = summary.getUserEvent().getEventType() == EventType.Sleeping ? summary.getDate().minusHours(5) : summary.getDate();
        String dateString = date.toString(DateTimeFormat.shortDate());
        if (name == null) {
            header = dateString;
        } else {
            header = getContext().getString(R.string.exercise_summary_header_format, name, dateString);
        }
        holder.container.setBackgroundResource(this.mBackgroundResource);
        holder.headerText.setTextColor(this.mHeaderTextColor);
        holder.detailsText.setTextColor(this.mDetailsTextColor);
        holder.headerText.setText(header);
        holder.detailsText.setText(summary.getDetails());
        return view;
    }
}
