package com.microsoft.kapp.webtiles;

import com.microsoft.band.webtiles.WebTile;
import com.microsoft.band.webtiles.WebTileResource;
import com.microsoft.kapp.diagnostics.Validate;
/* loaded from: classes.dex */
public class WebTileAuthResource {
    private String mPassword;
    private String mUsername;
    private WebTile mWebTile;
    private WebTileResource mWebTileResource;

    public WebTileAuthResource(WebTile webTile, WebTileResource webTileResource, String username, String password) {
        Validate.notNull(webTile, "webTile");
        Validate.notNull(webTileResource, "webTileResource");
        Validate.notNull(username, "username");
        Validate.notNull(password, "password");
        this.mWebTile = webTile;
        this.mWebTileResource = webTileResource;
        this.mUsername = username;
        this.mPassword = password;
    }

    public WebTileResource getResource() {
        return this.mWebTileResource;
    }

    public WebTile getWebTile() {
        return this.mWebTile;
    }

    public String getUsername() {
        return this.mUsername;
    }

    public String getPassword() {
        return this.mPassword;
    }
}
