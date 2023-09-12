package com.microsoft.kapp.fragments.whatsnew;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.activities.WhatsNewSecondaryCardActivity;
import com.microsoft.kapp.activities.WorkoutPlanDiscoveryActivity;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.fragments.BaseFragment;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.whatsnew.WhatsNewCardDataModel;
import com.microsoft.kapp.navigations.FragmentNavigationCommandV1;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.krestsdk.models.DisplaySubType;
import com.squareup.picasso.Picasso;
import java.util.HashMap;
/* loaded from: classes.dex */
public class WhatsNewCardFragment extends BaseFragment {
    private static String DATA_MODEL = "DataModel";
    private WhatsNewCardDataModel mDataModel;
    private ImageView mWhatsNewImage;
    private TextView mWhatsNewLink;
    private RelativeLayout mWhatsNewPage;
    private RelativeLayout mWhatsNewPageButtonLayout;
    private TextView mWhatsNewSubtitle;
    private TextView mWhatsNewTitle;

    public static WhatsNewCardFragment newInstance(WhatsNewCardDataModel dataModel) {
        WhatsNewCardFragment fragment = new WhatsNewCardFragment();
        Bundle args = new Bundle();
        args.putParcelable(DATA_MODEL, dataModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_whatsnew_carousel_card, container, false);
        Bundle bundle = getArguments();
        this.mDataModel = (WhatsNewCardDataModel) bundle.getParcelable(DATA_MODEL);
        this.mWhatsNewPage = (RelativeLayout) ViewUtils.getValidView(rootView, R.id.whatsnew_card_image_text_overlay, RelativeLayout.class);
        this.mWhatsNewTitle = (TextView) ViewUtils.getValidView(rootView, R.id.whatsnew_card_title, TextView.class);
        this.mWhatsNewSubtitle = (TextView) ViewUtils.getValidView(rootView, R.id.whatsnew_card_subtitle, TextView.class);
        this.mWhatsNewImage = (ImageView) ViewUtils.getValidView(rootView, R.id.whatsnew_card_image, ImageView.class);
        this.mWhatsNewLink = (TextView) ViewUtils.getValidView(rootView, R.id.whatsnew_card_button, TextView.class);
        this.mWhatsNewPageButtonLayout = (RelativeLayout) ViewUtils.getValidView(rootView, R.id.whatsnew_card_button_layout, RelativeLayout.class);
        updateCardContent();
        return rootView;
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        setTopMenuBarColor(getResources().getColor(R.color.PrimaryMediumColor));
    }

    private void updateCardContent() {
        if (this.mDataModel != null) {
            this.mWhatsNewTitle.setText(this.mDataModel.getTitle());
            this.mWhatsNewSubtitle.setText(this.mDataModel.getSubtitle());
            if (this.mDataModel.getImageID() != 0) {
                Picasso.with(getActivity()).load(this.mDataModel.getImageID()).into(this.mWhatsNewImage);
            }
            if (this.mDataModel.getBackgroundColor() != 0) {
                this.mWhatsNewPage.setBackgroundColor(this.mDataModel.getBackgroundColor());
            }
            updateButton();
        }
    }

    private void updateButton() {
        final WhatsNewCardDataModel.ButtonType buttonType = this.mDataModel.getButtonType();
        if (buttonType == null || buttonType == WhatsNewCardDataModel.ButtonType.NONE) {
            this.mWhatsNewLink.setVisibility(4);
        } else if (!TextUtils.isEmpty(this.mDataModel.getButtonText())) {
            this.mWhatsNewPageButtonLayout.setVisibility(0);
            this.mWhatsNewLink.setText(this.mDataModel.getButtonText());
            this.mWhatsNewLink.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.whatsnew.WhatsNewCardFragment.1
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    String telemetryName = WhatsNewCardFragment.this.mDataModel.getCardType();
                    if (!TextUtils.isEmpty(telemetryName)) {
                        HashMap<String, String> properties = new HashMap<>();
                        properties.put("Learn more", telemetryName);
                        Telemetry.logEvent(TelemetryConstants.Events.WhatsNew.LearnMore.LEARN_MORE, properties, null);
                    }
                    if (buttonType == WhatsNewCardDataModel.ButtonType.SECONDARY) {
                        WhatsNewCardFragment.this.navigateToSecondaryCard();
                    } else if (buttonType != WhatsNewCardDataModel.ButtonType.WEB) {
                        if (!TextUtils.isEmpty(WhatsNewCardFragment.this.mDataModel.getFeatureClass())) {
                            Class<?> featureClass = WhatsNewCardFragment.this.getClassObjectFromString(WhatsNewCardFragment.this.mDataModel.getFeatureClass());
                            WhatsNewCardFragment.this.navigateToFragment(featureClass);
                        }
                    } else {
                        Intent intent = new Intent("android.intent.action.VIEW");
                        intent.setData(Uri.parse(WhatsNewCardFragment.this.mDataModel.getUrl()));
                        WhatsNewCardFragment.this.startActivity(intent);
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void navigateToSecondaryCard() {
        Intent intent = new Intent(getActivity(), WhatsNewSecondaryCardActivity.class);
        intent.putExtra(Constants.WHATSNEW_SECONDARY_MODEL, this.mDataModel.getSecondCardModel());
        startActivity(intent);
    }

    private void navigateToGuidedWorkoutPageWithType(DisplaySubType mDisplaySubType) {
        Activity activity = getActivity();
        if (Validate.isActivityAlive(activity)) {
            Intent intent = new Intent(activity, WorkoutPlanDiscoveryActivity.class);
            intent.putExtra(Constants.LOAD_FROM_WHATSNEW, true);
            intent.putExtra(Constants.LOAD_FROM_WHATSNEW_TYPE, mDisplaySubType);
            startActivityForResult(intent, 10004);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void navigateToFragment(Class<?> featureClass) {
        if (featureClass != null) {
            FragmentNavigationCommandV1 navi = new FragmentNavigationCommandV1(getActivity(), R.string.navigation_item_title_history, 0, true, featureClass);
            navi.navigate(getActivity(), false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Class<?> getClassObjectFromString(String className) {
        if (TextUtils.isEmpty(className)) {
            return null;
        }
        try {
            Class<?> featureClass = Class.forName(className);
            return featureClass;
        } catch (ClassNotFoundException e) {
            KLog.e(this.TAG, "get Class from String failed", e);
            return null;
        }
    }

    public WhatsNewCardDataModel getDataModel() {
        return this.mDataModel;
    }

    public void setDataModel(WhatsNewCardDataModel mDataModel) {
        this.mDataModel = mDataModel;
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment
    protected int getTopMenuDividerVisibility() {
        return 0;
    }
}
