package com.microsoft.band.device;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import com.microsoft.band.client.CargoException;
import com.microsoft.band.internal.BandServiceMessage;
import com.microsoft.band.internal.util.IconUtils;
import com.microsoft.band.internal.util.Validation;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
/* loaded from: classes.dex */
public class CargoStrapp implements Parcelable {
    public static final int CARGO_STRAPP_STRUCTURE_SIZE = 1112;
    public static final Parcelable.Creator<CargoStrapp> CREATOR = new Parcelable.Creator<CargoStrapp>() { // from class: com.microsoft.band.device.CargoStrapp.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CargoStrapp createFromParcel(Parcel in) {
            return new CargoStrapp(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CargoStrapp[] newArray(int size) {
            return new CargoStrapp[size];
        }
    };
    private int mBadgeImageIndex;
    private byte[] mHashedAppId;
    private UUID mId;
    private List<Bitmap> mImages;
    private Map<Integer, StrappLayout> mLayouts;
    private List<Integer> mLayoutsToRemove;
    private String mName;
    private int mNotificationImageIndex;
    private int mSettingMask;
    private long mThemeColor;
    private Bitmap mTileImage;
    private int mTileImageIndex;

    public CargoStrapp(UUID id, String name, int strappSettings, long themeColor, List<Bitmap> images, short tileImageIndex, List<StrappLayout> layouts) throws CargoException {
        this(id, name, strappSettings, themeColor, images, tileImageIndex, layouts, tileImageIndex + 1);
    }

    public CargoStrapp(UUID id, String name, int strappSettings, long themeColor, List<Bitmap> images, short tileImageIndex, List<StrappLayout> layouts, int badgeImageIndex) throws CargoException {
        this.mHashedAppId = null;
        Validation.validateNullParameter(id, "Strapp id");
        this.mId = id;
        setName(name);
        setImageList(images, tileImageIndex, badgeImageIndex);
        setThemeColor(themeColor);
        if ((strappSettings & 2) == 2) {
            if (images.size() < 2) {
                throw new CargoException("Badging Requires Multiple Images", BandServiceMessage.Response.TILE_ICON_ERROR);
            }
            if (tileImageIndex == images.size() - 1) {
                throw new CargoException("Badging Enabled Display Icon Cannot Be The Last", BandServiceMessage.Response.TILE_ICON_ERROR);
            }
        }
        setSettings(strappSettings);
        this.mLayoutsToRemove = new ArrayList();
        this.mLayouts = new HashMap();
        if (layouts != null) {
            Validation.validateInRange("Strapp Layout num", layouts.size(), 0, 5);
            for (int i = 0; i < layouts.size(); i++) {
                this.mLayouts.put(Integer.valueOf(i), layouts.get(i));
            }
        }
    }

    public CargoStrapp(UUID id, String name, int strappSettings, long themeColor, List<Bitmap> images, short tileImageIndex, List<StrappLayout> layouts, int badgeImageIndex, byte[] appId) throws CargoException {
        this(id, name, strappSettings, themeColor, images, tileImageIndex, layouts, badgeImageIndex);
        this.mHashedAppId = appId;
    }

    public CargoStrapp(byte[] data, boolean withImage) {
        this.mHashedAppId = null;
        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        StrappIcon strappIcon = null;
        if (withImage) {
            byte[] strappIconByte = new byte[1024];
            buffer.get(strappIconByte);
            strappIcon = new StrappIcon(strappIconByte);
        }
        byte[] strappDataByte = new byte[88];
        buffer.get(strappDataByte);
        StrappData strappData = new StrappData(strappDataByte);
        createStrapp(strappData, strappIcon);
    }

    public CargoStrapp(StrappData data, StrappIcon icon) {
        this.mHashedAppId = null;
        createStrapp(data, icon);
    }

    private void createStrapp(StrappData data, StrappIcon icon) {
        this.mId = data.getAppId();
        this.mName = data.getFriendlyName();
        this.mThemeColor = data.getThemeColor();
        this.mSettingMask = data.getSettingMask();
        this.mImages = new ArrayList();
        this.mTileImageIndex = 0;
        this.mBadgeImageIndex = 0;
        this.mNotificationImageIndex = 0;
        if (icon != null) {
            this.mTileImage = icon.getImage();
        } else {
            this.mTileImage = null;
        }
        this.mLayouts = new HashMap();
        this.mLayoutsToRemove = new ArrayList();
        if (data.getAppHashId() != null) {
            this.mHashedAppId = data.getAppHashId();
        }
    }

    public UUID getId() {
        return this.mId;
    }

    public List<Bitmap> getImages() {
        return this.mImages;
    }

    public int getTileImageIndex() {
        return this.mTileImageIndex;
    }

    public boolean isBadgingEnabled() {
        return (this.mSettingMask & 2) == 2;
    }

    public boolean isScreenTimeoutDisabled() {
        return (this.mSettingMask & 32) == 32;
    }

    public boolean isCustomThemeEnabled() {
        return (this.mSettingMask & 4) == 4;
    }

    public Map<Integer, StrappLayout> getLayouts() {
        return this.mLayouts;
    }

    public List<Integer> getLayoutsToRemove() {
        return this.mLayoutsToRemove;
    }

    public String getName() {
        return this.mName;
    }

    public long getThemeColor() {
        return this.mThemeColor;
    }

    public int getSettingMask() {
        return this.mSettingMask;
    }

    public Bitmap getTileImage() {
        return this.mTileImage;
    }

    public int getBadgeImageIndex() {
        return this.mBadgeImageIndex;
    }

    public int getNotificationImageIndex() {
        return this.mNotificationImageIndex;
    }

    public byte[] getHashedAppId() {
        return this.mHashedAppId;
    }

    public void setBadgeImageIndex(int badgeImageIndex) {
        Validation.validateInRange("Strapp badgeImageIndex", badgeImageIndex, 0, this.mImages.size() - 1);
        this.mBadgeImageIndex = badgeImageIndex;
    }

    public void setNotificationImageIndex(int notificationImageIndex) {
        Validation.validateInRange("Strapp notificationImageIndex", notificationImageIndex, 0, this.mImages.size() - 1);
        this.mNotificationImageIndex = notificationImageIndex;
    }

    public void setName(String name) {
        Validation.validStringNullAndLength(name, 30, "Strapp Name");
        this.mName = name;
    }

    public void setSettings(int settings) {
        this.mSettingMask = settings;
    }

    public void setThemeColor(long themeColor) {
        this.mThemeColor = themeColor;
    }

    public void setImageList(List<Bitmap> images, int tileImageIndex) {
        Validation.validateInRange("Strapp tileImageIndex", tileImageIndex, 0, images.size() - 2);
        setImageList(images, tileImageIndex, tileImageIndex + 1);
    }

    public void setImageList(List<Bitmap> images, int tileImageIndex, int badgeImageIndex) {
        setImageList(images, tileImageIndex, badgeImageIndex, badgeImageIndex);
    }

    public void setImageList(List<Bitmap> images, int tileImageIndex, int badgeImageIndex, int notificationImageIndex) {
        Validation.validateNullParameter(images, "Strapp Image");
        Validation.validateInRange("The number of images for the strapp", images.size(), 1, 15);
        Validation.validateInRange("Strapp tileImageIndex", tileImageIndex, 0, images.size() - 1);
        for (Bitmap icon : images) {
            IconUtils.bitmapToByteArray(icon);
        }
        this.mImages = images;
        this.mTileImageIndex = tileImageIndex;
        this.mTileImage = images.get(tileImageIndex);
        setBadgeImageIndex(badgeImageIndex);
        setNotificationImageIndex(notificationImageIndex);
    }

    public void setLayout(int registeredIndex, StrappLayout layout) {
        Validation.validateNullParameter(layout, "Strapp layout");
        this.mLayoutsToRemove.remove(Integer.valueOf(registeredIndex));
        this.mLayouts.put(Integer.valueOf(registeredIndex), layout);
    }

    public void removeLayout(int registeredIndex) {
        if (this.mLayouts.containsKey(Integer.valueOf(registeredIndex))) {
            this.mLayouts.remove(Integer.valueOf(registeredIndex));
        }
        if (!this.mLayoutsToRemove.contains(Integer.valueOf(registeredIndex))) {
            this.mLayoutsToRemove.add(Integer.valueOf(registeredIndex));
        }
    }

    public StrappData getStrappData() {
        return new StrappData(this.mId, 0L, this.mThemeColor, this.mSettingMask, this.mName, this.mHashedAppId);
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(String.format("Strapp:%s", System.getProperty("line.separator")));
        result.append(String.format("     |--Name = %s %s", this.mName, System.getProperty("line.separator")));
        result.append(String.format("     |--ThemeColor = %d %s", Long.valueOf(this.mThemeColor), System.getProperty("line.separator")));
        result.append(String.format("     |--TileImageIndex = %d %s", Integer.valueOf(this.mTileImageIndex), System.getProperty("line.separator")));
        result.append(String.format("     |--SettingMask = %d %s", Integer.valueOf(this.mSettingMask), System.getProperty("line.separator")));
        if (this.mImages != null) {
            result.append(String.format("     |--Images Num = %d %s", Integer.valueOf(this.mImages.size()), System.getProperty("line.separator")));
        }
        if (this.mLayouts.size() > 0) {
            result.append(String.format("     |--Layout Num = %d %s", Integer.valueOf(this.mLayouts.size()), System.getProperty("line.separator")));
        }
        result.append(String.format("     |--BadgeImageIndex = %d %s", Integer.valueOf(this.mBadgeImageIndex), System.getProperty("line.separator")));
        result.append(String.format("     |--NotificationImageIndex = %d %s", Integer.valueOf(this.mNotificationImageIndex), System.getProperty("line.separator")));
        return result.toString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.mId);
        dest.writeString(this.mName);
        dest.writeLong(this.mThemeColor);
        dest.writeList(this.mImages);
        dest.writeInt(this.mTileImageIndex);
        dest.writeInt(this.mBadgeImageIndex);
        dest.writeInt(this.mNotificationImageIndex);
        dest.writeValue(this.mTileImage);
        dest.writeInt(this.mSettingMask);
        dest.writeInt(this.mLayouts.size());
        for (Map.Entry<Integer, StrappLayout> entry : this.mLayouts.entrySet()) {
            dest.writeInt(entry.getKey().intValue());
            dest.writeValue(entry.getValue());
        }
        dest.writeList(this.mLayoutsToRemove);
        if (this.mHashedAppId != null) {
            dest.writeInt(this.mHashedAppId.length);
            dest.writeByteArray(this.mHashedAppId);
            return;
        }
        dest.writeInt(0);
    }

    CargoStrapp(Parcel in) {
        this.mHashedAppId = null;
        this.mId = (UUID) in.readValue(UUID.class.getClassLoader());
        this.mName = in.readString();
        this.mThemeColor = in.readLong();
        this.mImages = new ArrayList();
        in.readList(this.mImages, Bitmap.class.getClassLoader());
        this.mTileImageIndex = in.readInt();
        this.mBadgeImageIndex = in.readInt();
        this.mNotificationImageIndex = in.readInt();
        this.mTileImage = (Bitmap) in.readValue(Bitmap.class.getClassLoader());
        this.mSettingMask = in.readInt();
        this.mLayouts = new HashMap();
        int size = in.readInt();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                int key = in.readInt();
                StrappLayout value = (StrappLayout) in.readValue(StrappLayout.class.getClassLoader());
                this.mLayouts.put(Integer.valueOf(key), value);
            }
        }
        this.mLayoutsToRemove = new ArrayList();
        in.readList(this.mLayoutsToRemove, null);
        if (in.readInt() > 0) {
            this.mHashedAppId = new byte[16];
            in.readByteArray(this.mHashedAppId);
        }
    }
}
