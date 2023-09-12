package com.microsoft.kapp.webtiles;

import android.app.Activity;
import com.microsoft.band.webtiles.WebTile;
import com.microsoft.band.webtiles.WebTileResource;
import com.microsoft.kapp.ActivityScopedCallbackAsyncTask;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.logging.KLog;
import java.util.LinkedList;
import java.util.List;
/* loaded from: classes.dex */
public class WebTileAuthTask extends ActivityScopedCallbackAsyncTask<WebTile, Void, List<WebTileResource>> {
    public WebTileAuthTask(Activity activity, Callback<List<WebTileResource>> callback) {
        super(activity, callback);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.ScopedAsyncTask
    public List<WebTileResource> doInBackground(WebTile... webTiles) {
        if (webTiles != null && webTiles.length == 1) {
            WebTile webTile = webTiles[0];
            List<WebTileResource> webResources = null;
            for (WebTileResource resource : webTile.getResources()) {
                try {
                    boolean authenticated = webTile.authenticateResource(resource);
                    if (!authenticated) {
                        if (webResources == null) {
                            List<WebTileResource> webResources2 = new LinkedList<>();
                            webResources = webResources2;
                        }
                        webResources.add(resource);
                    }
                } catch (Exception ex) {
                    KLog.e(this.TAG, "Exception while getting web tile auth required resources", ex);
                    setException(ex);
                }
            }
            return webResources;
        }
        return null;
    }
}
