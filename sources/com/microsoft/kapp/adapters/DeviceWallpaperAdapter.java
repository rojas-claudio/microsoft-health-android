package com.microsoft.kapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.personalization.DeviceWallpaper;
import com.microsoft.kapp.utils.ViewUtils;
/* loaded from: classes.dex */
public class DeviceWallpaperAdapter extends ArrayAdapter<DeviceWallpaper> {
    private int mSelectedWallpaperId;

    public DeviceWallpaperAdapter(Context context, DeviceWallpaper[] wallpaperResId, int selectedWallpaperId) {
        super(context, (int) R.layout.device_wallpaper_grid_item, wallpaperResId);
        this.mSelectedWallpaperId = selectedWallpaperId;
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public DeviceWallpaper getItem(int position) {
        if (position < 0 || position >= getCount()) {
            return null;
        }
        return (DeviceWallpaper) super.getItem(position);
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            convertView = inflater.inflate(R.layout.device_wallpaper_grid_item, parent, false);
            holder = new ViewHolder();
            holder.mWallpaperContainer = (ImageView) ViewUtils.getValidView(convertView, R.id.wallpaper_container, ImageView.class);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        DeviceWallpaper currentItem = getItem(position);
        if (currentItem.isPatternEqual(this.mSelectedWallpaperId)) {
            holder.mWallpaperContainer.setBackgroundResource(R.drawable.selected_grid_item);
        }
        holder.mWallpaperContainer.setImageResource(currentItem.getResId());
        return convertView;
    }

    /* loaded from: classes.dex */
    private class ViewHolder {
        ImageView mWallpaperContainer;

        private ViewHolder() {
        }
    }
}
