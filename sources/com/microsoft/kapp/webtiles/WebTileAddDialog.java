package com.microsoft.kapp.webtiles;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.microsoft.band.webtiles.WebTile;
import com.microsoft.band.webtiles.WebTileResource;
import com.microsoft.kapp.R;
import com.microsoft.kapp.views.CustomFontTextView;
import com.microsoft.kapp.widgets.ConfirmationBar;
import java.util.List;
/* loaded from: classes.dex */
public class WebTileAddDialog extends Dialog {
    private Activity mActivity;
    private View.OnClickListener mCancelClickListener;
    private View.OnClickListener mConfirmClickListener;
    private WebTile mWebTile;

    public WebTileAddDialog(Activity activity, final OnWebTileDialogClick confirmClickListener, final OnWebTileDialogClick cancelClickListener, WebTile webTile) {
        super(activity, R.style.WebtileAddTileDialogStyle);
        this.mActivity = activity;
        this.mConfirmClickListener = new View.OnClickListener() { // from class: com.microsoft.kapp.webtiles.WebTileAddDialog.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                WebTileAddDialog.this.dismiss();
                confirmClickListener.onClick();
            }
        };
        this.mCancelClickListener = new View.OnClickListener() { // from class: com.microsoft.kapp.webtiles.WebTileAddDialog.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                WebTileAddDialog.this.dismiss();
                cancelClickListener.onClick();
            }
        };
        this.mWebTile = webTile;
    }

    @Override // android.app.Dialog
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_webtile_dialog);
        ConfirmationBar confirmationBar = (ConfirmationBar) findViewById(R.id.confirmation_bar);
        confirmationBar.setOnConfirmButtonClickListener(this.mConfirmClickListener);
        confirmationBar.setOnCancelButtonClickListener(this.mCancelClickListener);
        CustomFontTextView addTileQuestion = (CustomFontTextView) findViewById(R.id.add_tile_question);
        addTileQuestion.setText(this.mActivity.getString(R.string.webtile_add_tile_to_band));
        TextView description = (TextView) findViewById(R.id.add_tile_app_description);
        TextView author = (TextView) findViewById(R.id.web_tile_author_value);
        TextView org2 = (TextView) findViewById(R.id.web_tile_org_value);
        TextView version = (TextView) findViewById(R.id.web_tile_version_value);
        TextView textView = (TextView) findViewById(R.id.web_tile_data_source);
        TextView dataSourceValue = (TextView) findViewById(R.id.web_title_data_source_value);
        CustomFontTextView addTileName = (CustomFontTextView) findViewById(R.id.add_tile_app_name);
        addTileName.setText(this.mWebTile.getName());
        ImageView tileIcon = (ImageView) findViewById(R.id.tile_icon);
        tileIcon.setColorFilter(new LightingColorFilter(0, ViewCompat.MEASURED_SIZE_MASK));
        if (this.mWebTile.getTileIcon() != null) {
            tileIcon.setImageBitmap(this.mWebTile.getTileIcon().getIcon());
        }
        if (!TextUtils.isEmpty(this.mWebTile.getDescription())) {
            description.setText(this.mWebTile.getDescription());
        }
        String authorValue = ifEmptyThenNoValue(this.mWebTile.getAuthor());
        author.setText(authorValue);
        String organizationValue = ifEmptyThenNoValue(this.mWebTile.getOrganization());
        org2.setText(organizationValue);
        String versionValue = ifEmptyThenNoValue(this.mWebTile.getVersionString());
        version.setText(versionValue);
        StringBuilder dataSourceBuilder = new StringBuilder();
        List<WebTileResource> resources = this.mWebTile.getResources();
        if (resources != null) {
            for (int i = 0; i < resources.size(); i++) {
                WebTileResource tileResource = resources.get(i);
                if (tileResource.getUrl() != null && !TextUtils.isEmpty(tileResource.getUrl().toString())) {
                    dataSourceBuilder.append(tileResource.getUrl().toString());
                    dataSourceBuilder.append(System.lineSeparator());
                }
            }
        }
        dataSourceValue.setText(ifEmptyThenNoValue(dataSourceBuilder.toString()));
    }

    @Override // android.app.Dialog, android.view.Window.Callback
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == 4) {
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    private String ifEmptyThenNoValue(String value) {
        return TextUtils.isEmpty(value) ? this.mActivity.getString(R.string.no_value) : value;
    }
}
