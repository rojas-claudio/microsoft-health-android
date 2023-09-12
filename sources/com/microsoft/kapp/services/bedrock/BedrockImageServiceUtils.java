package com.microsoft.kapp.services.bedrock;

import com.microsoft.kapp.logging.KLog;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
/* loaded from: classes.dex */
public class BedrockImageServiceUtils {
    public static final String TAG = BedrockImageServiceUtils.class.getSimpleName();

    public static String createSizedImageUrl(String imageUrl, int width, int height) {
        if (StringUtils.isBlank(imageUrl)) {
            KLog.e(TAG, "imageUrl cannot be blank.");
            return imageUrl;
        } else if (width <= 0) {
            throw new IllegalArgumentException("width must be positive.");
        } else {
            if (height <= 0) {
                throw new IllegalArgumentException("height must be positive.");
            }
            String imageUrl2 = getImageUrlWithoutResize(imageUrl);
            int dotIndex = imageUrl2.lastIndexOf(46);
            if (dotIndex != -1) {
                String base = imageUrl2.substring(0, dotIndex);
                String sizeExtension = "_m4_w" + width + "_h" + height;
                String imageExtension = imageUrl2.substring(dotIndex, imageUrl2.length());
                return base + sizeExtension + imageExtension;
            }
            return imageUrl2;
        }
    }

    public static String getImageUrlWithoutResize(String imageUrl) {
        if (StringUtils.isBlank(imageUrl)) {
            KLog.e(TAG, "imageUrl cannot be blank.");
            return imageUrl;
        }
        Pattern pattern = Pattern.compile("\\/?_.+\\.");
        return pattern.matcher(imageUrl).replaceFirst(".");
    }

    public static String createSizedImageUrl(String imageUrl, int height) {
        if (StringUtils.isBlank(imageUrl)) {
            KLog.e(TAG, "imageUrl cannot be blank.");
            return imageUrl;
        } else if (height <= 0) {
            throw new IllegalArgumentException("height must be positive.");
        } else {
            String imageUrl2 = getImageUrlWithoutResize(imageUrl);
            int dotIndex = imageUrl2.lastIndexOf(46);
            if (dotIndex != -1) {
                String base = imageUrl2.substring(0, dotIndex);
                String sizeExtension = "_h" + height;
                String imageExtension = imageUrl2.substring(dotIndex, imageUrl2.length());
                return base + sizeExtension + imageExtension;
            }
            return imageUrl2;
        }
    }
}
