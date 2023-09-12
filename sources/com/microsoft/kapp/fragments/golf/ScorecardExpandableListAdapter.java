package com.microsoft.kapp.fragments.golf;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.golf.ScorecardItem;
import com.microsoft.kapp.models.golf.ScorecardItemDetails;
import com.microsoft.kapp.models.golf.ScorecardModel;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.ConversionUtils;
import com.microsoft.kapp.utils.Formatter;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.views.PicassoImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.UrlConnectionDownloader;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.HashCodeBuilder;
/* loaded from: classes.dex */
public class ScorecardExpandableListAdapter extends BaseExpandableListAdapter {
    private static final int DEFAULT_MAX_REDIRECTS = 5;
    private static final String TAG = ScorecardExpandableListAdapter.class.getName();
    private Context mContext;
    private ScorecardModel mScorecardModel;
    private SettingsProvider mSettingsProvider;
    private Picasso picasso;

    /* loaded from: classes.dex */
    public static class HoleGroupHolder {
        public TextView hole;
        public TextView index;
        public TextView par;
        public TextView score;
        public TextView yards;
    }

    /* loaded from: classes.dex */
    public static class TotalsGroupHolder {
        public TextView name;
        public TextView totalPar;
        public TextView totalScore;
        public TextView totalYards;
    }

    /* loaded from: classes.dex */
    public static class HoleViewHolder implements Target {
        public TextView avgHR;
        public TextView calories;
        public TextView distance;
        public TextView duration;
        public PicassoImageView holeImage;
        public int key = -1;
        public TextView maxHR;
        public TextView minHR;
        public FrameLayout progressBarContainer;
        public TextView steps;

        @Override // com.squareup.picasso.Target
        public void onBitmapFailed(Drawable drawable) {
            if (this.progressBarContainer.getVisibility() == 0) {
                this.progressBarContainer.setVisibility(8);
            }
            if (drawable != null) {
                this.holeImage.setImageDrawable(drawable);
            }
        }

        @Override // com.squareup.picasso.Target
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            if (this.progressBarContainer.getVisibility() == 0) {
                this.progressBarContainer.setVisibility(8);
            }
            this.holeImage.setImageBitmap(bitmap);
        }

        @Override // com.squareup.picasso.Target
        public void onPrepareLoad(Drawable drawable) {
            this.progressBarContainer.setVisibility(0);
        }

        public boolean equals(Object other) {
            if (other instanceof HoleViewHolder) {
                return other == this || this.key == ((HoleViewHolder) other).key;
            }
            return false;
        }

        public int hashCode() {
            HashCodeBuilder hashCodeBuilder = new HashCodeBuilder(11, 3);
            hashCodeBuilder.append(this.key);
            return hashCodeBuilder.hashCode();
        }
    }

    public ScorecardExpandableListAdapter(Context context, SettingsProvider settingsProvider, ScorecardModel model) {
        Validate.notNull(model, "ScorecardModel", new Object[0]);
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO, new Object[0]);
        Validate.notNull(settingsProvider, "settingsProvider", new Object[0]);
        this.mScorecardModel = model;
        this.mContext = context;
        this.mSettingsProvider = settingsProvider;
        buildPicasso();
    }

    @Override // android.widget.ExpandableListAdapter
    public Object getChild(int groupPosition, int childPosition) {
        return this.mScorecardModel.getRow(groupPosition).getDetails();
    }

    @Override // android.widget.ExpandableListAdapter
    public long getChildId(int groupPosition, int childPosition) {
        return (groupPosition * 1000) + 1;
    }

    @Override // android.widget.ExpandableListAdapter
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        HoleViewHolder holder;
        ScorecardItem item = this.mScorecardModel.getRow(groupPosition);
        if (item.getItemtype() != ScorecardItem.ScorecardItemType.HOLE) {
            return null;
        }
        if (convertView == null) {
            holder = new HoleViewHolder();
            holder.key = groupPosition;
            LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService("layout_inflater");
            convertView = inflater.inflate(R.layout.golf_scorecard_list_item, parent, false);
            holder.holeImage = (PicassoImageView) ViewUtils.getValidView(convertView, R.id.scorecard_hole_item_hole_image, PicassoImageView.class);
            holder.progressBarContainer = (FrameLayout) ViewUtils.getValidView(convertView, R.id.scorecard_progress_bar_container, FrameLayout.class);
            holder.calories = (TextView) ViewUtils.getValidView(convertView, R.id.scorecard_hole_item_cals, TextView.class);
            holder.distance = (TextView) ViewUtils.getValidView(convertView, R.id.scorecard_hole_item_distance, TextView.class);
            holder.duration = (TextView) ViewUtils.getValidView(convertView, R.id.scorecard_hole_item_duration, TextView.class);
            holder.steps = (TextView) ViewUtils.getValidView(convertView, R.id.scorecard_hole_item_steps, TextView.class);
            holder.maxHR = (TextView) ViewUtils.getValidView(convertView, R.id.scorecard_hole_item_maxhr, TextView.class);
            holder.minHR = (TextView) ViewUtils.getValidView(convertView, R.id.scorecard_hole_item_minhr, TextView.class);
            holder.avgHR = (TextView) ViewUtils.getValidView(convertView, R.id.scorecard_hole_item_avghr, TextView.class);
            convertView.setTag(holder);
        } else {
            holder = (HoleViewHolder) convertView.getTag();
        }
        boolean itemFilled = false;
        ScorecardItemDetails itemDetails = this.mScorecardModel.getRow(groupPosition).getDetails();
        String imageUri = null;
        if (itemDetails != null) {
            imageUri = itemDetails.getImageURI();
            if (item.getScore() != 0) {
                itemFilled = true;
                holder.calories.setText(Formatter.formatCalories(this.mContext, itemDetails.getCals()));
                holder.distance.setText(Formatter.formatDistance(this.mContext, itemDetails.getDistance(), this.mSettingsProvider.isDistanceHeightMetric()));
                holder.duration.setText(Formatter.formatTimeInHoursAndMin(this.mContext, itemDetails.getDuration()));
                holder.steps.setText(Formatter.formatNumber(itemDetails.getSteps(), this.mContext));
                holder.maxHR.setText(itemDetails.getMaxHR() == 0 ? this.mContext.getResources().getString(R.string.no_value) : Formatter.formatHR(itemDetails.getMaxHR()));
                holder.minHR.setText(itemDetails.getMinHR() == 0 ? this.mContext.getResources().getString(R.string.no_value) : Formatter.formatHR(itemDetails.getMinHR()));
                holder.avgHR.setText(itemDetails.getAvgHR() == 0 ? this.mContext.getResources().getString(R.string.no_value) : Formatter.formatHR(itemDetails.getAvgHR()));
            }
        }
        setHoleImage(holder, imageUri);
        if (!itemFilled) {
            holder.calories.setText(this.mContext.getResources().getString(R.string.no_value));
            holder.distance.setText(this.mContext.getResources().getString(R.string.no_value));
            holder.duration.setText(this.mContext.getResources().getString(R.string.no_value));
            holder.steps.setText(this.mContext.getResources().getString(R.string.no_value));
            holder.maxHR.setText(this.mContext.getResources().getString(R.string.no_value));
            holder.minHR.setText(this.mContext.getResources().getString(R.string.no_value));
            holder.avgHR.setText(this.mContext.getResources().getString(R.string.no_value));
        }
        return convertView;
    }

    @Override // android.widget.ExpandableListAdapter
    public int getChildrenCount(int groupPosition) {
        ScorecardItem item = this.mScorecardModel.getRow(groupPosition);
        return item.getItemtype() == ScorecardItem.ScorecardItemType.HOLE ? 1 : 0;
    }

    @Override // android.widget.ExpandableListAdapter
    public Object getGroup(int groupPosition) {
        return this.mScorecardModel.getRow(groupPosition);
    }

    @Override // android.widget.ExpandableListAdapter
    public int getGroupCount() {
        return this.mScorecardModel.getTotalRows();
    }

    @Override // android.widget.ExpandableListAdapter
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override // android.widget.ExpandableListAdapter
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        this.mScorecardModel.getRow(groupPosition);
        switch (groupItem.getItemtype()) {
            case HOLE:
                return getHoleGroupView(groupPosition, isExpanded, convertView, parent);
            case IN:
            case OUT:
            case TOT:
                return getHoleTotalsView(groupPosition, isExpanded, convertView, parent);
            default:
                return null;
        }
    }

    private void buildPicasso() {
        this.picasso = new Picasso.Builder(this.mContext).downloader(new UrlConnectionDownloader(this.mContext) { // from class: com.microsoft.kapp.fragments.golf.ScorecardExpandableListAdapter.2
            /* JADX WARN: Removed duplicated region for block: B:12:0x004f  */
            /* JADX WARN: Removed duplicated region for block: B:17:0x009b A[RETURN] */
            @Override // com.squareup.picasso.UrlConnectionDownloader, com.squareup.picasso.Downloader
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public com.squareup.picasso.Downloader.Response load(android.net.Uri r15, boolean r16) throws java.io.IOException {
                /*
                    r14 = this;
                    r0 = 0
                    r6 = 0
                    r3 = 0
                    r7 = -1
                    java.lang.String r8 = ""
                L7:
                    r9 = 5
                    if (r3 > r9) goto L34
                    java.net.HttpURLConnection r0 = r14.openConnection(r15)
                    r9 = 1
                    r0.setUseCaches(r9)
                    int r7 = r0.getResponseCode()
                    java.lang.String r8 = r0.getResponseMessage()
                    java.lang.String r9 = com.microsoft.kapp.fragments.golf.ScorecardExpandableListAdapter.access$000()
                    java.lang.String r10 = "Response code while fetching Golf hole image: %1s and response message: %2s"
                    r11 = 2
                    java.lang.Object[] r11 = new java.lang.Object[r11]
                    r12 = 0
                    java.lang.Integer r13 = java.lang.Integer.valueOf(r7)
                    r11[r12] = r13
                    r12 = 1
                    r11[r12] = r8
                    com.microsoft.kapp.logging.KLog.i(r9, r10, r11)
                    switch(r7) {
                        case 301: goto L75;
                        case 302: goto L75;
                        default: goto L34;
                    }
                L34:
                    if (r0 == 0) goto L4d
                    r9 = 200(0xc8, float:2.8E-43)
                    if (r7 != r9) goto L4d
                    java.lang.String r9 = "Content-Length"
                    r10 = -1
                    int r9 = r0.getHeaderFieldInt(r9, r10)
                    long r1 = (long) r9
                    com.squareup.picasso.Downloader$Response r6 = new com.squareup.picasso.Downloader$Response
                    java.io.InputStream r9 = r0.getInputStream()
                    r10 = 0
                    r6.<init>(r9, r10, r1)
                L4d:
                    if (r6 != 0) goto L9b
                    if (r0 == 0) goto L54
                    r0.disconnect()
                L54:
                    java.lang.String r9 = "error while fetching the image with response code: %1s and response message: %2s"
                    r10 = 2
                    java.lang.Object[] r10 = new java.lang.Object[r10]
                    r11 = 0
                    java.lang.Integer r12 = java.lang.Integer.valueOf(r7)
                    r10[r11] = r12
                    r11 = 1
                    r10[r11] = r8
                    java.lang.String r5 = java.lang.String.format(r9, r10)
                    java.lang.String r9 = com.microsoft.kapp.fragments.golf.ScorecardExpandableListAdapter.access$000()
                    com.microsoft.kapp.logging.KLog.w(r9, r5)
                    com.squareup.picasso.Downloader$ResponseException r9 = new com.squareup.picasso.Downloader$ResponseException
                    r9.<init>(r5)
                    throw r9
                L75:
                    java.lang.String r9 = "Location"
                    java.lang.String r4 = r0.getHeaderField(r9)
                    android.net.Uri r15 = android.net.Uri.parse(r4)
                    r0.disconnect()
                    java.lang.String r9 = com.microsoft.kapp.fragments.golf.ScorecardExpandableListAdapter.access$000()
                    java.lang.String r10 = "Redirect count while fetching Golf hole image: %s"
                    r11 = 1
                    java.lang.Object[] r11 = new java.lang.Object[r11]
                    r12 = 0
                    java.lang.Integer r13 = java.lang.Integer.valueOf(r3)
                    r11[r12] = r13
                    com.microsoft.kapp.logging.KLog.i(r9, r10, r11)
                    int r3 = r3 + 1
                    goto L7
                L9b:
                    return r6
                */
                throw new UnsupportedOperationException("Method not decompiled: com.microsoft.kapp.fragments.golf.ScorecardExpandableListAdapter.AnonymousClass2.load(android.net.Uri, boolean):com.squareup.picasso.Downloader$Response");
            }
        }).listener(new Picasso.Listener() { // from class: com.microsoft.kapp.fragments.golf.ScorecardExpandableListAdapter.1
            @Override // com.squareup.picasso.Picasso.Listener
            public void onImageLoadFailed(Picasso pikasso, Uri uri, Exception exception) {
                KLog.e(ScorecardExpandableListAdapter.TAG, "error while loading the image", exception.getMessage());
            }
        }).build();
    }

    private void setHoleImage(HoleViewHolder holeViewHolder, String imageURI) {
        if (!TextUtils.isEmpty(imageURI)) {
            if (holeViewHolder.holeImage.getVisibility() == 8) {
                holeViewHolder.holeImage.setVisibility(0);
            }
            Pair<Integer, Integer> dimensions = ViewUtils.getScreenDimensionInPixes(this.mContext);
            int width = ((Integer) dimensions.first).intValue();
            int lastIndexOf = imageURI.lastIndexOf(".");
            if (lastIndexOf > 0) {
                if (width <= 640) {
                    imageURI = new StringBuilder(imageURI).replace(lastIndexOf, lastIndexOf + 1, "_sm.").toString();
                } else if (width <= 1080) {
                    imageURI = new StringBuilder(imageURI).replace(lastIndexOf, lastIndexOf + 1, "_md.").toString();
                } else {
                    imageURI = new StringBuilder(imageURI).replace(lastIndexOf, lastIndexOf + 1, "_lg.").toString();
                }
            }
            this.picasso.load(imageURI).placeholder(R.drawable.golf_noimage).error(R.drawable.golf_noimage).resize(width, 0).into(holeViewHolder);
            return;
        }
        holeViewHolder.holeImage.setVisibility(8);
    }

    private View getHoleTotalsView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        TotalsGroupHolder holder;
        if (convertView == null) {
            holder = new TotalsGroupHolder();
            LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService("layout_inflater");
            convertView = inflater.inflate(R.layout.golf_scorecard_list_group, parent, false);
            holder.name = (TextView) ViewUtils.getValidView(convertView, R.id.scorecard_totals_text, TextView.class);
            holder.totalYards = (TextView) ViewUtils.getValidView(convertView, R.id.scorecard_totals_yards, TextView.class);
            holder.totalPar = (TextView) ViewUtils.getValidView(convertView, R.id.scorecard_totals_par, TextView.class);
            holder.totalScore = (TextView) ViewUtils.getValidView(convertView, R.id.scorecard_totals_score, TextView.class);
            convertView.setTag(holder);
        } else {
            holder = (TotalsGroupHolder) convertView.getTag();
        }
        ScorecardItem groupItem = this.mScorecardModel.getRow(groupPosition);
        String name = null;
        switch (this.mScorecardModel.getRow(groupPosition).getItemtype()) {
            case IN:
                name = this.mContext.getResources().getString(R.string.golf_in_header);
                break;
            case OUT:
                name = this.mContext.getResources().getString(R.string.golf_out_header);
                break;
            case TOT:
                name = this.mContext.getResources().getString(R.string.golf_tot_header);
                break;
        }
        holder.name.setText(name);
        String distance = getLongestDrive(groupItem.getDistance());
        holder.totalYards.setText(distance);
        holder.totalPar.setText(String.valueOf(groupItem.getPar()));
        holder.totalScore.setText(String.valueOf(groupItem.getScore()));
        convertView.setBackgroundColor(this.mContext.getResources().getColor(R.color.golf_list_background_headers));
        return convertView;
    }

    private View getHoleGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        HoleGroupHolder holder;
        if (convertView == null) {
            holder = new HoleGroupHolder();
            LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService("layout_inflater");
            convertView = inflater.inflate(R.layout.golf_scorecard_list_group_hole, parent, false);
            holder.hole = (TextView) ViewUtils.getValidView(convertView, R.id.scorecard_hole_number, TextView.class);
            holder.yards = (TextView) ViewUtils.getValidView(convertView, R.id.scorecard_hole_yards, TextView.class);
            holder.par = (TextView) ViewUtils.getValidView(convertView, R.id.scorecard_hole_par, TextView.class);
            holder.index = (TextView) ViewUtils.getValidView(convertView, R.id.scorecard_hole_index, TextView.class);
            holder.score = (TextView) ViewUtils.getValidView(convertView, R.id.scorecard_hole_score, TextView.class);
            convertView.setTag(holder);
        } else {
            holder = (HoleGroupHolder) convertView.getTag();
        }
        ScorecardItem groupItem = this.mScorecardModel.getRow(groupPosition);
        holder.hole.setText(String.valueOf(groupItem.getHole()));
        String longestDrive = getLongestDrive(groupItem.getDistance());
        holder.yards.setText(longestDrive);
        holder.par.setText(String.valueOf(groupItem.getPar()));
        holder.index.setText(groupItem.getIndex() == 0 ? this.mContext.getResources().getString(R.string.no_value) : String.valueOf(groupItem.getIndex()));
        if (groupItem.getScore() == 0) {
            holder.score.setText(this.mContext.getResources().getString(R.string.no_value));
            holder.score.setBackgroundResource(R.color.transparent);
        } else {
            holder.score.setText(String.valueOf(groupItem.getScore()));
            setScoreBackground(holder.score, groupItem.getScoreState());
        }
        if (groupPosition % 2 == 0) {
            convertView.setBackgroundColor(this.mContext.getResources().getColor(R.color.golf_list_background_light));
        } else {
            convertView.setBackgroundColor(this.mContext.getResources().getColor(R.color.golf_list_background_dark));
        }
        return convertView;
    }

    private void setScoreBackground(TextView score, ScorecardItem.ScorecardScoreState scoreState) {
        int colorRes;
        switch (scoreState) {
            case OVER_1:
                colorRes = R.color.AboveParLightColor;
                break;
            case OVER_2:
                colorRes = R.color.AboveParMediumColor;
                break;
            case OVER_3:
                colorRes = R.color.AboveParDarkColor;
                break;
            case PAR:
                colorRes = R.color.ParColor;
                break;
            case UNDER_1:
                colorRes = R.color.BelowParLightColor;
                break;
            case UNDER_2:
                colorRes = R.color.BelowParMediumColor;
                break;
            case UNDER_3:
                colorRes = R.color.BelowParDarkColor;
                break;
            default:
                colorRes = R.color.transparent;
                break;
        }
        score.setBackgroundResource(colorRes);
    }

    @Override // android.widget.ExpandableListAdapter
    public boolean hasStableIds() {
        return true;
    }

    @Override // android.widget.ExpandableListAdapter
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override // android.widget.BaseExpandableListAdapter, android.widget.HeterogeneousExpandableList
    public int getGroupTypeCount() {
        return ScorecardItem.ScorecardItemType.values().length;
    }

    @Override // android.widget.BaseExpandableListAdapter, android.widget.HeterogeneousExpandableList
    public int getGroupType(int groupPosition) {
        ScorecardItem item = this.mScorecardModel.getRow(groupPosition);
        if (item != null) {
            return item.getItemtype().ordinal();
        }
        return 0;
    }

    private String getLongestDrive(double distance) {
        String longestDrive = String.valueOf(ConversionUtils.CentimetersToYards(distance));
        if (this.mSettingsProvider.isDistanceHeightMetric()) {
            NumberFormat doubleFormat = new DecimalFormat(this.mContext.getString(R.string.decimal_format_whole_number));
            doubleFormat.setRoundingMode(RoundingMode.DOWN);
            String longestDrive2 = doubleFormat.format(ConversionUtils.CentimetersToMeters(distance));
            return longestDrive2;
        }
        return longestDrive;
    }
}
