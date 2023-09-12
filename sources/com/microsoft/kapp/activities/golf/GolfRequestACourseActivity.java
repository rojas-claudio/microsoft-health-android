package com.microsoft.kapp.activities.golf;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.activities.BaseActivity;
import com.microsoft.kapp.utils.Constants;
/* loaded from: classes.dex */
public class GolfRequestACourseActivity extends BaseActivity {
    private static final String TAG = GolfRequestACourseActivity.class.getSimpleName();
    private TextView mBackButton;
    private TextView mHeaderTileText;
    private TextView mSuggestACourseText;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.activities.BaseActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.golf_recommend_a_course_activity);
        this.mHeaderTileText = (TextView) findViewById(R.id.header_text);
        this.mHeaderTileText.setText(getResources().getString(R.string.golf_request_a_course_lowercase_locked));
        this.mBackButton = (TextView) findViewById(R.id.back);
        this.mBackButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.activities.golf.GolfRequestACourseActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                GolfRequestACourseActivity.this.onBackPressed();
            }
        });
        this.mSuggestACourseText = (TextView) findViewById(R.id.request_a_course);
        this.mSuggestACourseText.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.activities.golf.GolfRequestACourseActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse(Constants.TMAG_FEEDBACK_URL));
                GolfRequestACourseActivity.this.startActivity(viewIntent);
            }
        });
    }
}
