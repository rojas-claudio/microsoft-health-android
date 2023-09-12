package com.microsoft.kapp.webtiles;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.LightingColorFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.microsoft.band.webtiles.WebTile;
import com.microsoft.band.webtiles.WebTileResource;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.R;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.views.CustomFontTextView;
import com.microsoft.kapp.widgets.ConfirmationBar;
import com.microsoft.kapp.widgets.Interstitial;
import java.net.URL;
import java.util.List;
/* loaded from: classes.dex */
public class WebTileAuthDialog extends Dialog {
    private Activity mActivity;
    private OnWebTileDialogClick mAuthCompletedClickListener;
    private List<WebTileResource> mAuthRequiredResources;
    private TextView mAuthResourceErrorTextView;
    private TextView mAuthResourceIndexTextView;
    private TextView mAuthResourceNameTextView;
    private View.OnClickListener mCancelClickListener;
    private View.OnClickListener mConfirmClickListener;
    private ConfirmationBar mConfirmationBar;
    private int mCurrentAuthedResource;
    private Interstitial mInterstitial;
    private EditText mPasswordEditText;
    private ViewGroup mResourceDetails;
    private ImageView mTileIcon;
    private CustomFontTextView mTitle;
    private EditText mUserNameEditText;
    private WebTile mWebTile;

    static /* synthetic */ int access$108(WebTileAuthDialog x0) {
        int i = x0.mCurrentAuthedResource;
        x0.mCurrentAuthedResource = i + 1;
        return i;
    }

    public WebTileAuthDialog(Activity activity, List<WebTileResource> authRequiredResources, OnWebTileDialogClick confirmClickListener, final OnWebTileDialogClick cancelClickListener, Interstitial interstitial, WebTile webTile) {
        super(activity, R.style.WebtileAddTileDialogStyle);
        this.mActivity = activity;
        Validate.notNullOrEmpty(authRequiredResources, "authRequiredResources");
        Validate.notNull(confirmClickListener, "confirmClickListener");
        Validate.notNull(cancelClickListener, "cancelClickListener");
        Validate.notNull(interstitial, "interstitial");
        this.mConfirmClickListener = new View.OnClickListener() { // from class: com.microsoft.kapp.webtiles.WebTileAuthDialog.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                WebTileAuthDialog.this.authWebResource();
            }
        };
        this.mCancelClickListener = new View.OnClickListener() { // from class: com.microsoft.kapp.webtiles.WebTileAuthDialog.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                cancelClickListener.onClick();
                WebTileAuthDialog.this.dismiss();
            }
        };
        this.mWebTile = webTile;
        this.mAuthRequiredResources = authRequiredResources;
        this.mInterstitial = interstitial;
        this.mAuthCompletedClickListener = confirmClickListener;
    }

    @Override // android.app.Dialog
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_webtile_dialog);
        this.mConfirmationBar = (ConfirmationBar) findViewById(R.id.confirmation_bar);
        this.mResourceDetails = (ViewGroup) findViewById(R.id.web_tile_auth_resource_details);
        this.mAuthResourceIndexTextView = (TextView) findViewById(R.id.web_tile_auth_resource_index);
        this.mAuthResourceNameTextView = (TextView) findViewById(R.id.web_tile_auth_resource_name);
        this.mAuthResourceErrorTextView = (TextView) findViewById(R.id.web_tile_auth_resource_error);
        this.mTitle = (CustomFontTextView) findViewById(R.id.add_tile_app_name);
        this.mTileIcon = (ImageView) findViewById(R.id.tile_icon);
        TextView description = (TextView) findViewById(R.id.add_tile_app_description);
        this.mTitle.setText(this.mWebTile.getName());
        if (!TextUtils.isEmpty(this.mWebTile.getDescription())) {
            description.setText(this.mWebTile.getDescription());
        }
        this.mTileIcon.setColorFilter(new LightingColorFilter(0, ViewCompat.MEASURED_SIZE_MASK));
        if (this.mWebTile.getTileIcon() != null) {
            this.mTileIcon.setImageBitmap(this.mWebTile.getTileIcon().getIcon());
        }
        this.mUserNameEditText = (EditText) findViewById(R.id.web_tile_user_name);
        this.mPasswordEditText = (EditText) findViewById(R.id.web_tile_password_value);
        if (this.mAuthRequiredResources.size() == 1) {
            this.mResourceDetails.setVisibility(8);
        } else {
            populateResourceTile();
        }
        this.mPasswordEditText.setOnKeyListener(new View.OnKeyListener() { // from class: com.microsoft.kapp.webtiles.WebTileAuthDialog.3
            @Override // android.view.View.OnKeyListener
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == 0) {
                    switch (keyCode) {
                        case 23:
                        case 66:
                            WebTileAuthDialog.this.authWebResource();
                            return true;
                    }
                }
                return false;
            }
        });
        this.mConfirmationBar.setOnConfirmButtonClickListener(this.mConfirmClickListener);
        this.mConfirmationBar.setOnCancelButtonClickListener(this.mCancelClickListener);
    }

    @Override // android.app.Dialog, android.view.Window.Callback
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == 4) {
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    protected void authWebResource() {
        this.mConfirmationBar.setVisibility(8);
        Callback<Boolean> callback = new Callback<Boolean>() { // from class: com.microsoft.kapp.webtiles.WebTileAuthDialog.4
            @Override // com.microsoft.kapp.Callback
            public void callback(Boolean authenticated) {
                WebTileAuthDialog.this.mInterstitial.setSlide(Interstitial.SLIDE_GONE);
                if (authenticated.booleanValue()) {
                    WebTileAuthDialog.access$108(WebTileAuthDialog.this);
                    if (WebTileAuthDialog.this.mAuthRequiredResources.size() == WebTileAuthDialog.this.mCurrentAuthedResource) {
                        WebTileAuthDialog.this.mAuthCompletedClickListener.onClick();
                        WebTileAuthDialog.this.dismiss();
                    } else {
                        WebTileAuthDialog.this.prepareAuthFields();
                    }
                } else {
                    WebTileAuthDialog.this.showAuthError();
                }
                WebTileAuthDialog.this.mConfirmationBar.setVisibility(0);
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                WebTileAuthDialog.this.mInterstitial.setSlide(Interstitial.SLIDE_GONE);
            }
        };
        String username = this.mUserNameEditText.getText().toString();
        String password = this.mPasswordEditText.getText().toString();
        if (!TextUtils.isEmpty(username)) {
            WebTileAuthResource authResource = new WebTileAuthResource(this.mWebTile, this.mAuthRequiredResources.get(this.mCurrentAuthedResource), username, password);
            WebTileAuthSetTask authSetTask = new WebTileAuthSetTask(this.mActivity, callback);
            this.mInterstitial.setSlide(5000);
            authSetTask.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, authResource);
            return;
        }
        showAuthError();
        this.mConfirmationBar.setVisibility(0);
    }

    protected void showAuthError() {
        this.mAuthResourceErrorTextView.setVisibility(0);
        this.mUserNameEditText.setText("");
        this.mPasswordEditText.setText("");
    }

    protected void prepareAuthFields() {
        this.mAuthResourceErrorTextView.setVisibility(8);
        populateResourceTile();
        this.mUserNameEditText.setText("");
        this.mPasswordEditText.setText("");
    }

    private void populateResourceTile() {
        this.mAuthResourceIndexTextView.setText(this.mActivity.getString(R.string.web_tile_auth_progress, new Object[]{Integer.valueOf(this.mCurrentAuthedResource + 1), Integer.valueOf(this.mAuthRequiredResources.size())}));
        if (this.mAuthRequiredResources.size() > this.mCurrentAuthedResource) {
            WebTileResource resource = this.mAuthRequiredResources.get(this.mCurrentAuthedResource);
            URL url = resource.getUrl();
            if (url != null && !TextUtils.isEmpty(url.toString())) {
                this.mAuthResourceNameTextView.setText(url.toString());
            } else {
                this.mAuthResourceNameTextView.setText(this.mActivity.getString(R.string.no_value));
            }
        }
    }
}
