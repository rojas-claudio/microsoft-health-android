package com.microsoft.kapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.guidedworkout.WorkoutPlanSummaryDetails;
import com.microsoft.kapp.picasso.utils.PicassoWrapper;
import com.microsoft.kapp.services.bedrock.BedrockImageServiceUtils;
import com.microsoft.kapp.utils.LockedStringUtils;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.krestsdk.models.WorkoutPlanBrowseDetails;
import java.util.List;
import java.util.Locale;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
/* loaded from: classes.dex */
public class GuidedWorkoutPlanBrowseDetailsListAdapter extends ArrayAdapter<WorkoutPlanSummaryDetails> {
    private static final int IMAGE_HEIGHT = 100;
    private static final int IMAGE_WIDTH = 100;
    private static final int RESOURCE_ID = 2130903069;

    /* loaded from: classes.dex */
    public static class ViewHolder {
        public TextView favoriteIcon;
        public ImageView image;
        public TextView subtitle;
        public TextView title;
    }

    public GuidedWorkoutPlanBrowseDetailsListAdapter(Context context, List<WorkoutPlanSummaryDetails> workoutPlanSummaryDetailsList) {
        super(context, (int) R.layout.adapter_guided_workout_browse_summary, workoutPlanSummaryDetailsList);
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        String subtitle;
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService("layout_inflater");
            view = inflater.inflate(R.layout.adapter_guided_workout_browse_summary, (ViewGroup) null);
            holder = new ViewHolder();
            holder.image = (ImageView) ViewUtils.getValidView(view, R.id.workout_browse_summary_image, ImageView.class);
            holder.title = (TextView) ViewUtils.getValidView(view, R.id.workout_browse_summary_title, TextView.class);
            holder.subtitle = (TextView) ViewUtils.getValidView(view, R.id.workout_browse_summary_subtitle, TextView.class);
            holder.favoriteIcon = (TextView) ViewUtils.getValidView(view, R.id.favorite_icon, TextView.class);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        WorkoutPlanSummaryDetails browseDetails = getItem(position);
        if (browseDetails == null) {
            WorkoutPlanBrowseDetails workoutPlanBrowseDetails = new WorkoutPlanBrowseDetails();
            workoutPlanBrowseDetails.setName("");
            workoutPlanBrowseDetails.setPath(null);
            workoutPlanBrowseDetails.setLevel("");
            workoutPlanBrowseDetails.setDuration(0);
            workoutPlanBrowseDetails.setIsCustom(false);
            browseDetails = new WorkoutPlanSummaryDetails(null, workoutPlanBrowseDetails);
        }
        String imageUrl = browseDetails.getPath();
        if (imageUrl != null) {
            imageUrl = BedrockImageServiceUtils.createSizedImageUrl(imageUrl, 100, 100);
        }
        String title = browseDetails.getName();
        if (browseDetails.getIsCustom()) {
            DateTimeFormatter dateFormat = DateTimeFormat.shortDate().withLocale(Locale.getDefault());
            Context context = getContext();
            Object[] objArr = new Object[3];
            objArr[0] = browseDetails.getPublishDate() != null ? dateFormat.print(browseDetails.getPublishDate()) : "";
            objArr[1] = browseDetails.getLevel();
            objArr[2] = LockedStringUtils.unitMins(browseDetails.getDuration().intValue(), getContext().getResources());
            subtitle = context.getString(R.string.guided_workout_summary_subtitle_custom, objArr);
        } else {
            subtitle = getContext().getString(R.string.guided_workout_summary_subtitle, browseDetails.getLevel(), LockedStringUtils.unitMins(browseDetails.getDuration().intValue(), getContext().getResources()));
        }
        PicassoWrapper.with(getContext()).load(imageUrl).placeholder(R.drawable.workout_thumbnail).into(holder.image);
        holder.title.setText(title);
        holder.subtitle.setText(subtitle);
        if (browseDetails.getIsFavorite()) {
            holder.favoriteIcon.setVisibility(0);
        } else {
            holder.favoriteIcon.setVisibility(8);
        }
        return view;
    }
}
