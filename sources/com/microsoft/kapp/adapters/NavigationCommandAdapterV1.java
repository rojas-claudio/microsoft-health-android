package com.microsoft.kapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.navigations.NavigationCommandV1;
import com.microsoft.kapp.navigations.NavigationHeaderCommand;
import com.microsoft.kapp.navigations.NavigationSeparator;
import com.microsoft.kapp.utils.ViewUtils;
import java.util.List;
/* loaded from: classes.dex */
public class NavigationCommandAdapterV1 extends ArrayAdapter<NavigationCommandV1> {
    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_NAV_ITEM = 2;
    private static final int VIEW_TYPE_SEPARATOR = 1;

    public NavigationCommandAdapterV1(Context context, int resourceId, List<NavigationCommandV1> items) {
        super(context, resourceId, items);
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public int getViewTypeCount() {
        return 3;
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public int getItemViewType(int position) {
        NavigationCommandV1 command = getItem(position);
        if (command instanceof NavigationHeaderCommand) {
            return 0;
        }
        if (command instanceof NavigationSeparator) {
            return 1;
        }
        return 2;
    }

    @Override // android.widget.BaseAdapter, android.widget.ListAdapter
    public boolean isEnabled(int position) {
        return getItemViewType(position) == 2;
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        NavigationItemViewHolder viewHolder;
        HeaderViewHolder viewHolder2;
        NavigationCommandV1 command = getItem(position);
        switch (getItemViewType(position)) {
            case 0:
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.navigation_drawer_category_header, (ViewGroup) null);
                    viewHolder2 = new HeaderViewHolder();
                    viewHolder2.headerTitle = (TextView) ViewUtils.getValidView(convertView, R.id.textview_header_title, TextView.class);
                    convertView.setTag(viewHolder2);
                } else {
                    viewHolder2 = (HeaderViewHolder) convertView.getTag();
                }
                viewHolder2.headerTitle.setText(command.getTitle());
                return convertView;
            case 1:
                if (convertView == null) {
                    return LayoutInflater.from(getContext()).inflate(R.layout.navigation_drawer_separator, (ViewGroup) null);
                }
                return convertView;
            case 2:
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.navigation_drawer_navigation_item, (ViewGroup) null);
                    viewHolder = new NavigationItemViewHolder();
                    viewHolder.itemIcon = (TextView) ViewUtils.getValidView(convertView, R.id.textview_navigation_item_icon, TextView.class);
                    viewHolder.itemTitle = (TextView) ViewUtils.getValidView(convertView, R.id.textview_navigation_item_title, TextView.class);
                    viewHolder.itemNotification = (ImageView) ViewUtils.getValidView(convertView, R.id.navigation_notification_dot, ImageView.class);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (NavigationItemViewHolder) convertView.getTag();
                }
                viewHolder.itemTitle.setText(command.getTitle());
                viewHolder.itemIcon.setText(getContext().getString(command.getIconResourceId()));
                if (command.getNotification()) {
                    viewHolder.itemNotification.setVisibility(0);
                    return convertView;
                }
                viewHolder.itemNotification.setVisibility(8);
                return convertView;
            default:
                return convertView;
        }
    }

    /* loaded from: classes.dex */
    private static class NavigationItemViewHolder {
        public TextView itemIcon;
        public ImageView itemNotification;
        public TextView itemTitle;

        private NavigationItemViewHolder() {
        }
    }

    /* loaded from: classes.dex */
    private static class HeaderViewHolder {
        public TextView headerTitle;

        private HeaderViewHolder() {
        }
    }
}
