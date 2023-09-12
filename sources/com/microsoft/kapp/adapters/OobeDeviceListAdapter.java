package com.microsoft.kapp.adapters;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.microsoft.kapp.R;
import java.util.LinkedHashMap;
/* loaded from: classes.dex */
public class OobeDeviceListAdapter extends BaseAdapter {
    private final Activity mActivity;
    private final LinkedHashMap<String, BluetoothDevice> mDevices;
    private int mPairingItem = -1;

    /* loaded from: classes.dex */
    static class ViewHolder {
        TextView deviceName;

        ViewHolder() {
        }
    }

    public OobeDeviceListAdapter(Activity activity, LinkedHashMap<String, BluetoothDevice> devices) {
        this.mActivity = activity;
        this.mDevices = devices;
    }

    @Override // android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = this.mActivity.getLayoutInflater();
            row = inflater.inflate(R.layout.adapter_oobe_devices_list, parent, false);
            holder = new ViewHolder();
            holder.deviceName = (TextView) row.findViewById(16908308);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        if (this.mPairingItem == position) {
            holder.deviceName.setBackgroundColor(this.mActivity.getResources().getColor(R.color.GreyLightColor));
        } else {
            holder.deviceName.setBackgroundColor(this.mActivity.getResources().getColor(position % 2 == 0 ? R.color.GreyXLightColor : R.color.transparent));
        }
        holder.deviceName.setText(((BluetoothDevice) this.mDevices.values().toArray()[position]).getName());
        return row;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.mDevices.size();
    }

    @Override // android.widget.Adapter
    public Object getItem(int position) {
        return this.mDevices.values().toArray()[position];
    }

    @Override // android.widget.Adapter
    public long getItemId(int position) {
        return position;
    }

    public void setPairingItem(int position) {
        this.mPairingItem = position;
    }
}
