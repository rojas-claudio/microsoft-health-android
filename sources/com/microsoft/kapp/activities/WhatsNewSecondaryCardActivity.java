package com.microsoft.kapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import com.microsoft.kapp.R;
import com.microsoft.kapp.fragments.whatsnew.WhatsNewSecondaryCardFragment;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.whatsnew.WhatsNewSecondaryCardDataModel;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.views.CustomGlyphView;
/* loaded from: classes.dex */
public class WhatsNewSecondaryCardActivity extends BaseFragmentActivity {
    private static final String TAG = WhatsNewSecondaryCardActivity.class.getSimpleName();

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.activities.BaseFragmentActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whatsnew_secondary_card);
        Intent intent = getIntent();
        WhatsNewSecondaryCardDataModel secondaryCardDataModel = (WhatsNewSecondaryCardDataModel) intent.getParcelableExtra(Constants.WHATSNEW_SECONDARY_MODEL);
        CustomGlyphView backButton = (CustomGlyphView) findViewById(R.id.whatsnew_secondary_card_back);
        backButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.activities.WhatsNewSecondaryCardActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                WhatsNewSecondaryCardActivity.this.finish();
            }
        });
        if (isSafeToCommitFragmentTransactions()) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.whatsnew_secondary_card_main_content, WhatsNewSecondaryCardFragment.newInstance(secondaryCardDataModel));
            fragmentTransaction.commit();
            return;
        }
        KLog.w(TAG, "Fragment Transaction was blocked to prevent a state loss");
    }
}
