package com.microsoft.band.device;

import android.os.Parcel;
import android.os.Parcelable;
import com.microsoft.band.client.CargoException;
import com.microsoft.band.internal.BandServiceMessage;
import com.microsoft.band.internal.util.BufferUtil;
import com.microsoft.band.internal.util.Validation;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
/* loaded from: classes.dex */
public class StartStrip implements Parcelable {
    public static final Parcelable.Creator<StartStrip> CREATOR = new Parcelable.Creator<StartStrip>() { // from class: com.microsoft.band.device.StartStrip.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public StartStrip createFromParcel(Parcel in) {
            return new StartStrip(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public StartStrip[] newArray(int size) {
            return new StartStrip[size];
        }
    };
    public static final int START_STRIP_STRUCTURE_SIZE = 1324;
    private List<CargoStrapp> mAppList;

    public StartStrip(StrappListFromDevice list) throws CargoException {
        this(list.getStrappList());
    }

    public StartStrip(List<CargoStrapp> strapps) throws CargoException {
        Validation.validateNullParameter(strapps, "Start Strip: ");
        this.mAppList = new ArrayList();
        for (CargoStrapp app : strapps) {
            add(app);
        }
    }

    public boolean contains(UUID appId) {
        for (CargoStrapp strapp : this.mAppList) {
            if (strapp.getId().equals(appId)) {
                return true;
            }
        }
        return false;
    }

    public boolean contains(CargoStrapp item) {
        return contains(item.getId());
    }

    public boolean remove(UUID appId) {
        for (int i = 0; i < this.mAppList.size(); i++) {
            if (this.mAppList.get(i).getId().equals(appId)) {
                this.mAppList.remove(i);
                return true;
            }
        }
        return false;
    }

    public boolean remove(CargoStrapp item) {
        return remove(item.getId());
    }

    public void removeAt(int index) throws CargoException {
        if (getCount() == 0) {
            throw new CargoException("StartStrip: no strapp on device", BandServiceMessage.Response.TILE_NOT_FOUND_ERROR);
        }
        Validation.validateInRange("StartStrip: removeAt index", index, 0, getCount());
        this.mAppList.remove(index);
    }

    public int indexOf(UUID appId) {
        for (int i = 0; i < this.mAppList.size(); i++) {
            if (this.mAppList.get(i).getId().equals(appId)) {
                return i;
            }
        }
        return -1;
    }

    public int indexOf(CargoStrapp item) {
        return indexOf(item.getId());
    }

    public void add(CargoStrapp app) throws CargoException {
        if (canAddStrapp(app)) {
            this.mAppList.add(app);
        }
    }

    public void insert(int index, CargoStrapp item) throws CargoException {
        if (getCount() > 0) {
            Validation.validateInRange("StartStrip: removeAt index", index, 0, getCount());
        } else if (index != 0) {
            index = 0;
        }
        if (canAddStrapp(item)) {
            this.mAppList.add(index, item);
        }
    }

    private boolean canAddStrapp(CargoStrapp item) throws CargoException {
        Validation.validateNullParameter(item, "StartStrip: Add strapp");
        if (this.mAppList.size() >= 13) {
            throw new CargoException("StartStrip: app count cannot exceed 13", BandServiceMessage.Response.BAND_IS_FULL_ERROR);
        }
        if (contains(item)) {
            throw new CargoException("StartStrip: cannot add repeat strapp for " + item.getName(), BandServiceMessage.Response.TILE_ALREADY_EXISTS_ERROR);
        }
        return true;
    }

    public int getCount() {
        return this.mAppList.size();
    }

    public List<CargoStrapp> getAppList() {
        return this.mAppList;
    }

    public byte[] toBytesToDevice() {
        int count = getCount();
        int size = (count * 88) + 4;
        ByteBuffer dataBuffer = BufferUtil.allocateLittleEndian(size).putInt(count);
        for (int i = 0; i < count; i++) {
            StrappData data = this.mAppList.get(i).getStrappData();
            data.setStartStripOrder(i);
            dataBuffer.put(data.toByte());
        }
        return dataBuffer.array();
    }

    public String toString() {
        int count = getCount();
        StringBuilder result = new StringBuilder();
        result.append(String.format("Start Strip: %d %s", Integer.valueOf(count), System.getProperty("line.separator")));
        for (int i = 0; i < count; i++) {
            result.append(String.format("===== %d %s", Integer.valueOf(i), System.getProperty("line.separator")));
            result.append(this.mAppList.get(i).toString());
        }
        return result.toString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.mAppList);
    }

    StartStrip(Parcel in) {
        this.mAppList = new ArrayList();
        in.readList(this.mAppList, CargoStrapp.class.getClassLoader());
    }
}
