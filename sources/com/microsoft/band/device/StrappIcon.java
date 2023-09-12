package com.microsoft.band.device;

import android.graphics.Bitmap;
import android.graphics.Color;
import com.microsoft.band.internal.util.BitHelper;
import com.microsoft.band.internal.util.BufferUtil;
import com.microsoft.band.internal.util.IconUtils;
import com.microsoft.band.internal.util.Validation;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
/* loaded from: classes.dex */
public class StrappIcon implements Serializable {
    private static final long serialVersionUID = 1;
    private byte[] mIconData;
    private int mIconDataSize;
    private int mIconHeight;
    private int mIconWidth;

    public StrappIcon(Bitmap image) {
        Validation.validateNullParameter(image, "Image must be specified.");
        this.mIconWidth = image.getWidth();
        this.mIconHeight = image.getHeight();
        this.mIconData = IconUtils.bitmapToByteArray(image);
        this.mIconDataSize = this.mIconData.length;
    }

    public StrappIcon(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        this.mIconWidth = getIntFrom2Byte(buffer.get(), buffer.get());
        this.mIconHeight = getIntFrom2Byte(buffer.get(), buffer.get());
        this.mIconDataSize = getIntFrom2Byte(buffer.get(), buffer.get()) - 6;
        if (validateIconDataSize()) {
            this.mIconData = new byte[this.mIconDataSize];
            buffer.get(this.mIconData);
            return;
        }
        this.mIconData = null;
    }

    private int getIntFrom2Byte(byte bHigh, byte bLow) {
        return (BitHelper.unsignedByteToInteger(bHigh) << 8) | BitHelper.unsignedByteToInteger(bLow);
    }

    public byte[] toByte() {
        if (validateIconDataSize()) {
            ByteBuffer buffer = BufferUtil.allocateLittleEndian(1024);
            buffer.put((byte) (this.mIconWidth >> 8)).put((byte) this.mIconWidth);
            buffer.put((byte) (this.mIconHeight >> 8)).put((byte) this.mIconHeight);
            int size = this.mIconDataSize + 6;
            buffer.put((byte) (size >> 8)).put((byte) size);
            buffer.put(this.mIconData);
            return buffer.array();
        }
        return null;
    }

    public Bitmap getImage() {
        if (this.mIconHeight <= 0 || this.mIconHeight > 102 || this.mIconWidth <= 0 || this.mIconWidth > 102 || !validateIconDataSize()) {
            return null;
        }
        int expandDataSize = this.mIconWidth * this.mIconHeight;
        int[] pixels = new int[expandDataSize];
        int pixelIndex = 0;
        int iconDataIndex = 0;
        while (iconDataIndex < this.mIconDataSize) {
            short compressData = BitHelper.unsignedByteToShort(this.mIconData[iconDataIndex]);
            int runLength = compressData >> 4;
            int runValue = (compressData & 15) << 4;
            int pixel = Color.argb(runValue, 255, 255, 255);
            int i = 0;
            int pixelIndex2 = pixelIndex;
            while (i < runLength) {
                pixels[pixelIndex2] = pixel;
                i++;
                pixelIndex2++;
            }
            iconDataIndex++;
            pixelIndex = pixelIndex2;
        }
        return Bitmap.createBitmap(pixels, this.mIconWidth, this.mIconHeight, Bitmap.Config.ARGB_8888);
    }

    private boolean validateIconDataSize() {
        return this.mIconDataSize > 0 && this.mIconDataSize <= 1018;
    }
}
