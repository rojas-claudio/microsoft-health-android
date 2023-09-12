package com.microsoft.kapp.models.strapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutSummary;
import java.io.ByteArrayOutputStream;
import java.util.UUID;
/* loaded from: classes.dex */
public class StrappDefinition {
    private static final int MAX_IMAGE_SIZE = 50;
    private final boolean isThirdPartyStrapp;
    private byte[] mBitmapdata;
    private transient Bitmap mIcon;
    private boolean mIsEnabledDuringOOBE;
    private boolean mIsNotificationsEnabled;
    private final int mNameResourceId;
    private final UUID mStrappId;
    private String mStrappName;

    public StrappDefinition(UUID strappId, int nameResourceId, boolean isNotificationEnabled, boolean isEnabledDuringOOBE) {
        this.mIcon = null;
        this.mStrappName = null;
        this.mIsNotificationsEnabled = false;
        this.mIsEnabledDuringOOBE = false;
        Validate.notNull(strappId, "strappId");
        this.mStrappId = strappId;
        this.mNameResourceId = nameResourceId;
        this.isThirdPartyStrapp = false;
        this.mIsNotificationsEnabled = isNotificationEnabled;
        this.mIsEnabledDuringOOBE = isEnabledDuringOOBE;
    }

    public StrappDefinition(UUID strappId, int nameResourceId, boolean isNotificationEnabled) {
        this(strappId, nameResourceId, isNotificationEnabled, false);
    }

    public StrappDefinition(UUID strappId, String name, Bitmap icon) {
        this.mIcon = null;
        this.mStrappName = null;
        this.mIsNotificationsEnabled = false;
        this.mIsEnabledDuringOOBE = false;
        Validate.notNull(strappId, "strappId");
        Validate.notNull(name, WorkoutSummary.NAME);
        Validate.notNull(icon, "icon");
        this.mNameResourceId = 0;
        this.mStrappId = strappId;
        this.mStrappName = name;
        this.isThirdPartyStrapp = true;
        this.mIcon = icon;
        ByteArrayOutputStream blob = new ByteArrayOutputStream();
        icon.compress(Bitmap.CompressFormat.PNG, 0, blob);
        this.mBitmapdata = blob.toByteArray();
    }

    public UUID getStrappId() {
        return this.mStrappId;
    }

    public String getName(Context context) {
        return !isThirdPartyStrapp() ? context.getResources().getString(this.mNameResourceId) : getThirdPartyName();
    }

    public String getThirdPartyName() {
        return this.mStrappName;
    }

    public Bitmap getIcon() {
        if (this.mIcon != null) {
            Bitmap bitmap = Bitmap.createBitmap(this.mIcon.getWidth(), this.mIcon.getHeight(), this.mIcon.getConfig());
            return bitmap;
        } else if (this.mBitmapdata == null) {
            return null;
        } else {
            Bitmap bitmap2 = BitmapFactory.decodeByteArray(this.mBitmapdata, 0, this.mBitmapdata.length);
            return bitmap2;
        }
    }

    public boolean isThirdPartyStrapp() {
        return this.isThirdPartyStrapp;
    }

    public boolean isNotificationEnabled() {
        return this.mIsNotificationsEnabled;
    }

    public boolean isEnabledDuringOOBE() {
        return this.mIsEnabledDuringOOBE;
    }

    public void setName(String name) {
        this.mStrappName = name;
    }
}
