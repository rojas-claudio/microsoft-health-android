package com.microsoft.band.device;

import com.microsoft.band.internal.util.BitHelper;
import com.microsoft.band.internal.util.BufferUtil;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class StrappListFromDevice implements Serializable {
    public static final int STRAPP_LIST_NO_IMAGE_STRUCTURE_SIZE = 1324;
    public static final int STRAPP_LIST_STRUCTURE_SIZE = 16684;
    private static final long serialVersionUID = 1;
    private int mCount;
    private StrappData[] mDataArray;
    private StrappIcon[] mTileIcons;
    private boolean mWithImage;

    public StrappListFromDevice(byte[] data, boolean withImage) {
        this.mWithImage = withImage;
        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        this.mDataArray = new StrappData[15];
        this.mTileIcons = null;
        if (withImage) {
            this.mTileIcons = new StrappIcon[15];
            for (int i = 0; i < this.mTileIcons.length; i++) {
                byte[] iconData = new byte[1024];
                buffer.get(iconData);
                this.mTileIcons[i] = new StrappIcon(iconData);
            }
        }
        this.mCount = (int) BitHelper.unsignedIntegerToLong(buffer.getInt());
        for (int i2 = 0; i2 < this.mCount; i2++) {
            byte[] appData = new byte[88];
            buffer.get(appData);
            this.mDataArray[i2] = new StrappData(appData);
        }
    }

    public byte[] toBytes() {
        ByteBuffer dataBuffer = BufferUtil.allocateLittleEndian(1320);
        ByteBuffer iconBuffer = BufferUtil.allocateLittleEndian(15360);
        for (int i = 0; i < this.mCount; i++) {
            dataBuffer.put(this.mDataArray[i].toByte());
        }
        for (int i2 = 0; i2 < this.mCount; i2++) {
            iconBuffer.put(this.mTileIcons[i2].toByte());
        }
        ByteBuffer buffer = BufferUtil.allocateLittleEndian(STRAPP_LIST_STRUCTURE_SIZE);
        buffer.put(iconBuffer.array());
        int emptyAppSize = 15 - this.mCount;
        if (emptyAppSize > 0) {
            buffer.position(emptyAppSize * 1024);
        }
        return buffer.put(iconBuffer.array()).putInt(this.mCount).put(dataBuffer.array()).array();
    }

    public int getCount() {
        return this.mCount;
    }

    public StrappData getStrappData(int index) {
        return this.mDataArray[index];
    }

    public StrappIcon getTileIcon(int index) {
        if (this.mWithImage) {
            return this.mTileIcons[index];
        }
        return null;
    }

    private CargoStrapp getCargoStrapp(int index) {
        StrappData strappData = getStrappData(index);
        if (strappData != null) {
            return new CargoStrapp(getStrappData(index), getTileIcon(index));
        }
        return null;
    }

    public List<CargoStrapp> getStrappList() {
        List<CargoStrapp> list = new ArrayList<>();
        for (int i = 0; i < getCount(); i++) {
            list.add(getCargoStrapp(i));
        }
        return list;
    }
}
