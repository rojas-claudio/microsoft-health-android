package com.microsoft.kapp.fragments.whatsnew;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.fragments.BaseFragment;
import com.microsoft.kapp.models.whatsnew.WhatsNewSecondaryCardDataModel;
import com.microsoft.kapp.utils.ViewUtils;
/* loaded from: classes.dex */
public class WhatsNewSecondaryCardFragment extends BaseFragment {
    private TextView mWhatsNewSubtitle;
    private TextView mWhatsNewTitle;

    /* loaded from: classes.dex */
    public static class BundleInfo {
        public static final String SubTitle = "subtitle";
        public static final String Title = "title";
    }

    public static WhatsNewSecondaryCardFragment newInstance(WhatsNewSecondaryCardDataModel dataModel) {
        WhatsNewSecondaryCardFragment fragment = new WhatsNewSecondaryCardFragment();
        Bundle args = new Bundle();
        args.putString("title", dataModel.getTitle());
        args.putString(BundleInfo.SubTitle, dataModel.getSubtitle());
        fragment.setArguments(args);
        return fragment;
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_whatsnew_carousel_secondary_card, container, false);
        Bundle bundle = getArguments();
        String title = bundle.getString("title", "");
        String subtitle = bundle.getString(BundleInfo.SubTitle, "");
        this.mWhatsNewTitle = (TextView) ViewUtils.getValidView(rootView, R.id.whatsnew_secondary_card_title, TextView.class);
        this.mWhatsNewSubtitle = (TextView) ViewUtils.getValidView(rootView, R.id.whatsnew_secondary_card_subtitle, TextView.class);
        this.mWhatsNewTitle.setText(title);
        this.mWhatsNewSubtitle.setText(subtitle);
        return rootView;
    }
}
