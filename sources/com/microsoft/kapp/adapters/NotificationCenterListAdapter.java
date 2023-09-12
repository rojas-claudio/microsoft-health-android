package com.microsoft.kapp.adapters;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.Switch;
import com.microsoft.kapp.R;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.views.CustomFontTextView;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
/* loaded from: classes.dex */
public class NotificationCenterListAdapter extends ArrayAdapter<ApplicationInfo> {
    private static final int RESOURCE_ID = 2130903064;
    private TreeMap<CharSequence, ApplicationInfo> mAllApps;
    private Context mContext;
    private ArrayList<String> mEnabledApps;
    private Filter mFilter;
    private TreeMap<CharSequence, ApplicationInfo> mFilteredApps;
    private ArrayList<ApplicationInfo> mFilteredAppsOrdered;
    private View.OnClickListener mSwitchOnClickListener;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class ViewHolder {
        private Switch mEnabledSwitch;
        private ImageView mIconImageView;
        private CustomFontTextView mNameTextView;

        private ViewHolder() {
        }
    }

    public NotificationCenterListAdapter(Context context, TreeMap<CharSequence, ApplicationInfo> allApps, ArrayList<String> enabledApps) {
        super(context, (int) R.layout.adapter_enable_notification_center, new ArrayList(allApps.values()));
        this.mContext = context;
        this.mEnabledApps = enabledApps;
        this.mAllApps = allApps;
        this.mFilteredApps = allApps;
        this.mFilteredAppsOrdered = new ArrayList<>(allApps.values());
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService("layout_inflater");
            convertView = inflater.inflate(R.layout.adapter_enable_notification_center, (ViewGroup) null);
            holder = new ViewHolder();
            holder.mIconImageView = (ImageView) ViewUtils.getValidView(convertView, R.id.notification_center_app_icon, ImageView.class);
            holder.mNameTextView = (CustomFontTextView) ViewUtils.getValidView(convertView, R.id.name_text_view, CustomFontTextView.class);
            holder.mEnabledSwitch = (Switch) ViewUtils.getValidView(convertView, R.id.enabled_switch, Switch.class);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.mEnabledSwitch.setOnCheckedChangeListener(null);
        }
        ApplicationInfo applicationInfo = getItem(position);
        holder.mIconImageView.setTag(Integer.valueOf(position));
        holder.mIconImageView.setVisibility(4);
        holder.mNameTextView.setText(getAppName(applicationInfo));
        new LoadImageTask(holder.mIconImageView, this.mContext, applicationInfo).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Object[0]);
        boolean isEnabled = this.mEnabledApps.contains(applicationInfo.packageName);
        holder.mEnabledSwitch.setTag(applicationInfo.packageName);
        holder.mEnabledSwitch.setChecked(isEnabled);
        setTextAlpha(holder, isEnabled);
        holder.mEnabledSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.microsoft.kapp.adapters.NotificationCenterListAdapter.1
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (NotificationCenterListAdapter.this.mSwitchOnClickListener != null) {
                    NotificationCenterListAdapter.this.mSwitchOnClickListener.onClick(buttonView);
                }
                NotificationCenterListAdapter.this.setTextAlpha(holder, isChecked);
            }
        });
        return convertView;
    }

    /* loaded from: classes.dex */
    class LoadImageTask extends AsyncTask<Object, Void, Drawable> {
        private ApplicationInfo mApplicationInfo;
        private ImageView mImage;
        private String mTag;
        private Context mTaskContext;

        public LoadImageTask(ImageView imv, Context context, ApplicationInfo applicationInfo) {
            this.mImage = imv;
            this.mTag = imv.getTag().toString();
            this.mTaskContext = context;
            this.mApplicationInfo = applicationInfo;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.AsyncTask
        public Drawable doInBackground(Object... params) {
            if (this.mTaskContext != null) {
                return this.mTaskContext.getPackageManager().getApplicationIcon(this.mApplicationInfo);
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Drawable result) {
            if (this.mImage != null && result != null && this.mImage.getTag().toString().equals(this.mTag)) {
                this.mImage.setImageDrawable(result);
                this.mImage.setVisibility(0);
            }
        }
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public int getCount() {
        return this.mFilteredApps.size();
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public ApplicationInfo getItem(int position) {
        return this.mFilteredAppsOrdered.get(position);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setTextAlpha(ViewHolder holder, boolean isEnabled) {
        if (!isEnabled) {
            holder.mNameTextView.setTextColor(-2130706433);
        } else {
            holder.mNameTextView.setTextColor(-1);
        }
    }

    private CharSequence getAppName(ApplicationInfo applicationInfo) {
        for (Map.Entry<CharSequence, ApplicationInfo> entry : this.mFilteredApps.entrySet()) {
            if (entry.getValue().equals(applicationInfo)) {
                return entry.getKey();
            }
        }
        return null;
    }

    @Override // android.widget.ArrayAdapter, android.widget.Filterable
    public Filter getFilter() {
        if (this.mFilter == null) {
            this.mFilter = new NotificationTileFilter();
        }
        return this.mFilter;
    }

    public void setOnToggleAppListener(View.OnClickListener listener) {
        this.mSwitchOnClickListener = listener;
    }

    /* loaded from: classes.dex */
    private class NotificationTileFilter extends Filter {
        private NotificationTileFilter() {
        }

        @Override // android.widget.Filter
        protected Filter.FilterResults performFiltering(CharSequence constraint) {
            Filter.FilterResults results = new Filter.FilterResults();
            if (constraint == null || constraint.length() == 0) {
                TreeMap<CharSequence, ApplicationInfo> newValues = new TreeMap<>((SortedMap<CharSequence, ? extends ApplicationInfo>) NotificationCenterListAdapter.this.mAllApps);
                results.values = newValues;
            } else {
                TreeMap treeMap = new TreeMap();
                for (CharSequence item : NotificationCenterListAdapter.this.mAllApps.keySet()) {
                    if (item.toString().toLowerCase(Locale.US).contains(constraint.toString().toLowerCase(Locale.US))) {
                        treeMap.put(item, NotificationCenterListAdapter.this.mAllApps.get(item));
                    }
                }
                results.values = treeMap;
            }
            return results;
        }

        @Override // android.widget.Filter
        protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
            NotificationCenterListAdapter.this.mFilteredApps = (TreeMap) results.values;
            NotificationCenterListAdapter.this.mFilteredAppsOrdered = new ArrayList(NotificationCenterListAdapter.this.mFilteredApps.values());
            NotificationCenterListAdapter.this.notifyDataSetChanged();
        }
    }
}
