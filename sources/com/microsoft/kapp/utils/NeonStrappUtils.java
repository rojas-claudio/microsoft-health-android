package com.microsoft.kapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.microsoft.kapp.R;
import com.microsoft.kapp.models.strapp.DefaultStrappUUID;
import java.util.UUID;
/* loaded from: classes.dex */
public class NeonStrappUtils extends StrappUtils {
    private static final String TAG = NeonStrappUtils.class.getSimpleName();

    @Override // com.microsoft.kapp.utils.StrappUtils
    protected Bitmap getWeatherTileIcon(Context context) {
        return BitmapFactory.decodeStream(context.getResources().openRawResource(R.raw.neon_weather_tile));
    }

    @Override // com.microsoft.kapp.utils.StrappUtils
    protected int getFinanceDownLayout() {
        return R.raw.neon_finance_down_1;
    }

    @Override // com.microsoft.kapp.utils.StrappUtils
    protected int getFinanceUpLayout() {
        return R.raw.neon_finance_up_2;
    }

    @Override // com.microsoft.kapp.utils.StrappUtils
    protected int getFinanceUpIcon() {
        return R.raw.neon_finance_up_icon;
    }

    @Override // com.microsoft.kapp.utils.StrappUtils
    protected int getFinanceDownIcon() {
        return R.raw.neon_finance_down_icon;
    }

    @Override // com.microsoft.kapp.utils.StrappUtils
    protected int getWeatherLayout() {
        return R.raw.neon_weather_1;
    }

    @Override // com.microsoft.kapp.utils.StrappUtils
    protected int getStarbucksLayout() {
        return R.raw.neon_starbucks_card_barcode_layout;
    }

    @Override // com.microsoft.kapp.utils.StrappUtils
    protected Bitmap getFinanceTileIcon(Context context) {
        return BitmapFactory.decodeStream(context.getResources().openRawResource(R.raw.neon_finance_tile));
    }

    @Override // com.microsoft.kapp.utils.StrappUtils
    public int getTileIconForTile(UUID strappId) {
        if (strappId.equals(DefaultStrappUUID.STRAPP_FACEBOOK)) {
            return R.raw.neon_facebook_tile;
        }
        if (strappId.equals(DefaultStrappUUID.STRAPP_FACEBOOK_MESSENGER)) {
            return R.raw.neon_fb_messenger_tile;
        }
        if (strappId.equals(DefaultStrappUUID.STRAPP_NOTIFICATION_CENTER)) {
            return R.raw.neon_notification_center_tile;
        }
        if (strappId.equals(DefaultStrappUUID.STRAPP_EMAIL)) {
            return R.raw.neon_email_tile;
        }
        if (strappId.equals(DefaultStrappUUID.STRAPP_TWITTER)) {
            return R.raw.neon_twitter_tile;
        }
        return 0;
    }

    @Override // com.microsoft.kapp.utils.StrappUtils
    public int getBadgingIconForTile(UUID strappId) {
        if (strappId.equals(DefaultStrappUUID.STRAPP_FACEBOOK)) {
            return R.raw.neon_facebook_badging;
        }
        if (strappId.equals(DefaultStrappUUID.STRAPP_FACEBOOK_MESSENGER)) {
            return R.raw.neon_fb_messenger_badging;
        }
        if (strappId.equals(DefaultStrappUUID.STRAPP_NOTIFICATION_CENTER)) {
            return R.raw.neon_notification_center_badging;
        }
        if (strappId.equals(DefaultStrappUUID.STRAPP_EMAIL)) {
            return R.raw.neon_email_badging;
        }
        if (strappId.equals(DefaultStrappUUID.STRAPP_TWITTER)) {
            return R.raw.neon_twitter_badging;
        }
        return 0;
    }

    @Override // com.microsoft.kapp.utils.StrappUtils
    public int getNotificiationIconForTile(UUID strappId) {
        if (strappId.equals(DefaultStrappUUID.STRAPP_FACEBOOK)) {
            return R.raw.neon_facebook_notification;
        }
        if (strappId.equals(DefaultStrappUUID.STRAPP_FACEBOOK_MESSENGER)) {
            return R.raw.neon_fb_messenger_notification;
        }
        if (strappId.equals(DefaultStrappUUID.STRAPP_NOTIFICATION_CENTER)) {
            return R.raw.neon_notification_center_notification;
        }
        if (strappId.equals(DefaultStrappUUID.STRAPP_EMAIL)) {
            return R.raw.neon_email_notification;
        }
        if (strappId.equals(DefaultStrappUUID.STRAPP_TWITTER)) {
            return R.raw.neon_twitter_notification;
        }
        return 0;
    }
}
