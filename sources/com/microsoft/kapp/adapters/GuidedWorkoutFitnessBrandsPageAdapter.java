package com.microsoft.kapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.picasso.utils.PicassoWrapper;
import com.microsoft.kapp.services.bedrock.BedrockImageServiceUtils;
import com.microsoft.kapp.utils.ViewUtils;
import com.squareup.picasso.Callback;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
/* loaded from: classes.dex */
public class GuidedWorkoutFitnessBrandsPageAdapter extends ArrayAdapter<String> {
    private static final int RESOURCE_ID = 2130903066;
    private static int mLogoHeight;
    private List<String> mBrandsNamesList;
    private final int mLastBrandsNamesListIndex;
    private static final int[] mImagesResArray = {R.drawable.android_mens_fitness, R.drawable.android_muscle_fitness, R.drawable.android_shape};
    private static final int IMAGE_RES_SIZE = mImagesResArray.length;

    /* loaded from: classes.dex */
    public static class ViewHolder {
        public ImageView image;
        public ImageView logo;
        public TextView name;
    }

    public GuidedWorkoutFitnessBrandsPageAdapter(Context context, List<String> goalsList, List<String> brandsNamesList) {
        super(context, (int) R.layout.adapter_fitness_brands_list_single_item, goalsList);
        this.mBrandsNamesList = brandsNamesList;
        this.mLastBrandsNamesListIndex = brandsNamesList.size();
        mLogoHeight = context.getResources().getDimensionPixelSize(R.dimen.guided_workout_brand_logo_height);
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService("layout_inflater");
            convertView = inflater.inflate(R.layout.adapter_fitness_brands_list_single_item, (ViewGroup) null);
            holder = new ViewHolder();
            holder.image = (ImageView) ViewUtils.getValidView(convertView, R.id.background_image, ImageView.class);
            holder.logo = (ImageView) ViewUtils.getValidView(convertView, R.id.logo, ImageView.class);
            holder.name = (TextView) ViewUtils.getValidView(convertView, R.id.brand_name, TextView.class);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        int imageResourceId = mImagesResArray[position % IMAGE_RES_SIZE];
        PicassoWrapper.with(getContext()).load(imageResourceId).fit().into(holder.image);
        String logoUrl = getItem(position);
        if (StringUtils.isBlank(logoUrl)) {
            holder.name.setText(this.mBrandsNamesList.get(position));
        } else {
            PicassoWrapper.with(getContext()).load(BedrockImageServiceUtils.createSizedImageUrl(logoUrl, mLogoHeight)).fit().centerInside().into(holder.logo, new Callback() { // from class: com.microsoft.kapp.adapters.GuidedWorkoutFitnessBrandsPageAdapter.1
                @Override // com.squareup.picasso.Callback
                public void onError() {
                    if (position <= GuidedWorkoutFitnessBrandsPageAdapter.this.mLastBrandsNamesListIndex) {
                        holder.name.setVisibility(0);
                        holder.name.setText((CharSequence) GuidedWorkoutFitnessBrandsPageAdapter.this.mBrandsNamesList.get(position));
                    }
                }

                @Override // com.squareup.picasso.Callback
                public void onSuccess() {
                    holder.name.setVisibility(8);
                }
            });
        }
        return convertView;
    }
}
