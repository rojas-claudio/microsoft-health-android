package com.microsoft.kapp.utils;

import com.microsoft.kapp.models.CloudEnvironment;
/* loaded from: classes.dex */
public final class EnvironmentUtils {
    public static final String GALLERY_URL_INT = "http://go.microsoft.com/fwlink/?LinkID=619404";
    public static final String GALLERY_URL_PROD = "http://go.microsoft.com/fwlink/?LinkID=619406";
    public static final String GALLERY_URL_STG = "http://go.microsoft.com/fwlink/?LinkID=619405";

    public static String getTileGalleryUrl() {
        if (CloudEnvironment.getDefault() == CloudEnvironment.PROD) {
            return GALLERY_URL_PROD;
        }
        if (CloudEnvironment.getDefault() == CloudEnvironment.STG) {
            return GALLERY_URL_STG;
        }
        return GALLERY_URL_INT;
    }
}
