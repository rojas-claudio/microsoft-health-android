package com.microsoft.kapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.personalization.DeviceTheme;
import com.microsoft.kapp.utils.ViewUtils;
/* loaded from: classes.dex */
public class DeviceColorsAdapter extends ArrayAdapter<DeviceTheme> {
    private int mSelectedColorIndex;

    public DeviceColorsAdapter(Context context, DeviceTheme[] deviceThemes, int selectedColorIndex) {
        super(context, (int) R.layout.device_color_grid_item, deviceThemes);
        this.mSelectedColorIndex = selectedColorIndex;
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public DeviceTheme getItem(int position) {
        if (position < 0 || position >= getCount()) {
            return null;
        }
        return (DeviceTheme) super.getItem(position);
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewholder;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            convertView = inflater.inflate(R.layout.device_color_grid_item, parent, false);
            viewholder = new ViewHolder();
            viewholder.baseColor = (ImageView) ViewUtils.getValidView(convertView, R.id.base_color_container, ImageView.class);
            viewholder.highlightColor = (ImageView) ViewUtils.getValidView(convertView, R.id.highlight_color_container, ImageView.class);
            viewholder.container = convertView.findViewById(R.id.color_container);
            convertView.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) convertView.getTag();
        }
        DeviceTheme currentItem = getItem(position);
        if (currentItem.getThemeId() == this.mSelectedColorIndex) {
            viewholder.container.setBackgroundResource(R.drawable.selected_grid_item);
        }
        viewholder.baseColor.setBackgroundColor(currentItem.getBase());
        viewholder.highlightColor.setBackgroundColor(currentItem.getHighlight());
        return convertView;
    }

    /* loaded from: classes.dex */
    private static class ViewHolder {
        public ImageView baseColor;
        public View container;
        public ImageView highlightColor;

        private ViewHolder() {
        }
    }
}
