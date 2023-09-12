package com.microsoft.kapp.webtiles;

import android.app.Activity;
import com.microsoft.band.webtiles.WebTile;
import com.microsoft.band.webtiles.WebTileResource;
import com.microsoft.kapp.ActivityScopedCallbackAsyncTask;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.logging.KLog;
/* loaded from: classes.dex */
public class WebTileAuthSetTask extends ActivityScopedCallbackAsyncTask<WebTileAuthResource, Void, Boolean> {
    public WebTileAuthSetTask(Activity activity, Callback<Boolean> callback) {
        super(activity, callback);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.ScopedAsyncTask
    public Boolean doInBackground(WebTileAuthResource... tileAuthResource) {
        boolean authenticated = false;
        if (tileAuthResource != null && tileAuthResource.length == 1) {
            WebTileAuthResource authResource = tileAuthResource[0];
            WebTile webTile = authResource.getWebTile();
            WebTileResource resource = authResource.getResource();
            String username = authResource.getUsername();
            String password = authResource.getPassword();
            webTile.setAuthenticationHeader(resource, username, password);
            try {
                authenticated = webTile.authenticateResource(resource);
            } catch (Exception e) {
                KLog.e(this.TAG, String.format("exception while setting auth for resource: %s", resource.getUrl()), e);
                setException(e);
            }
        } else {
            KLog.e(this.TAG, "Can't set auth for multiple web tile resources");
        }
        return Boolean.valueOf(authenticated);
    }
}
