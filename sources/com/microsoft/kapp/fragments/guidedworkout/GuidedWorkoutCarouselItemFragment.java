package com.microsoft.kapp.fragments.guidedworkout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.picasso.utils.PicassoWrapper;
import com.microsoft.kapp.services.bedrock.BedrockImageServiceUtils;
import com.microsoft.kapp.utils.ActivityUtils;
import com.microsoft.kapp.utils.ViewUtils;
import com.squareup.picasso.Callback;
/* loaded from: classes.dex */
public class GuidedWorkoutCarouselItemFragment extends Fragment {
    private static int mFeaturedWorkoutImageHeight;
    private final String TAG = getClass().getSimpleName();
    private int mCurrentPageNumber;
    private ImageView mFeaturedWorkoutImage;
    private ProgressBar mFeaturedWorkoutProgressBar;
    private TextView mFeaturedWorkoutSubTitle;
    private TextView mFeaturedWorkoutTitle;
    private String mId;
    private String mImageUrl;
    private String mSubTitle;
    private String mTitle;
    private int mTotalPages;
    private View mView;

    /* loaded from: classes.dex */
    public static class BundleInfo {
        public static final String CurrentPageNumber = "currentPageNumber";
        public static final String Id = "id";
        public static final String ImageUrl = "image-url";
        public static final String SubTitle = "sub-title";
        public static final String Title = "title";
    }

    public static GuidedWorkoutCarouselItemFragment newInstance(int pageNumber, String title, String subTitle, String imageUrl, String id) {
        GuidedWorkoutCarouselItemFragment fragment = new GuidedWorkoutCarouselItemFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString(BundleInfo.SubTitle, subTitle);
        args.putString(BundleInfo.ImageUrl, imageUrl);
        args.putString("id", id);
        args.putInt(BundleInfo.CurrentPageNumber, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override // android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mCurrentPageNumber = getArguments().getInt(BundleInfo.CurrentPageNumber);
        this.mTitle = getArguments().getString("title");
        this.mSubTitle = getArguments().getString(BundleInfo.SubTitle);
        this.mImageUrl = getArguments().getString(BundleInfo.ImageUrl);
        this.mId = getArguments().getString("id");
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mView = inflater.inflate(R.layout.guided_workout_favorite_carousel_item, container, false);
        mFeaturedWorkoutImageHeight = getResources().getDimensionPixelSize(R.dimen.guided_workout_featured_workout_image_height);
        loadViewElements();
        populateViewElements();
        return this.mView;
    }

    private void populateViewElements() {
        if (!TextUtils.isEmpty(this.mId)) {
            this.mFeaturedWorkoutImage.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutCarouselItemFragment.1
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("workoutPlanId", GuidedWorkoutCarouselItemFragment.this.mId);
                    ActivityUtils.launchLevelTwoActivity(GuidedWorkoutCarouselItemFragment.this.getActivity(), GuidedWorkoutsBrowsePlanFragment.class, bundle);
                }
            });
        }
        if (!TextUtils.isEmpty(this.mImageUrl)) {
            if (this.mImageUrl.equalsIgnoreCase(GuidedWorkoutCarouselFragment.DEFAULT_IMAGE)) {
                loadDefaultFeaturedWorkoutImage();
            } else {
                PicassoWrapper.with(getActivity()).load(BedrockImageServiceUtils.createSizedImageUrl(this.mImageUrl, mFeaturedWorkoutImageHeight)).fit().centerCrop().into(this.mFeaturedWorkoutImage, new Callback.EmptyCallback() { // from class: com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutCarouselItemFragment.2
                    @Override // com.squareup.picasso.Callback.EmptyCallback, com.squareup.picasso.Callback
                    public void onSuccess() {
                    }

                    @Override // com.squareup.picasso.Callback.EmptyCallback, com.squareup.picasso.Callback
                    public void onError() {
                        KLog.w(GuidedWorkoutCarouselItemFragment.this.TAG, "Picasso failed to download the image!");
                        GuidedWorkoutCarouselItemFragment.this.loadDefaultFeaturedWorkoutImage();
                    }
                });
            }
        } else {
            KLog.e(this.TAG, "no featured workout with a valid image has been found!");
            loadDefaultFeaturedWorkoutImage();
        }
        this.mFeaturedWorkoutTitle.setText(this.mTitle);
        this.mFeaturedWorkoutSubTitle.setText(this.mSubTitle);
        this.mFeaturedWorkoutProgressBar.setVisibility(8);
    }

    private void loadViewElements() {
        this.mFeaturedWorkoutTitle = (TextView) ViewUtils.getValidView(this.mView, R.id.featured_workout_header, TextView.class);
        this.mFeaturedWorkoutSubTitle = (TextView) ViewUtils.getValidView(this.mView, R.id.featured_workout_name, TextView.class);
        this.mFeaturedWorkoutImage = (ImageView) ViewUtils.getValidView(this.mView, R.id.featured_workout_image, ImageView.class);
        this.mFeaturedWorkoutProgressBar = (ProgressBar) ViewUtils.getValidView(this.mView, R.id.featured_workout_load_progress, ProgressBar.class);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadDefaultFeaturedWorkoutImage() {
        PicassoWrapper.with(getActivity()).load(R.drawable.featured_workout_default_image).fit().centerCrop().into(this.mFeaturedWorkoutImage);
        this.mFeaturedWorkoutProgressBar.setVisibility(8);
    }

    public int getPageNumber() {
        return this.mCurrentPageNumber;
    }

    public static Fragment newInstance() {
        return new GuidedWorkoutCarouselItemFragment();
    }
}
