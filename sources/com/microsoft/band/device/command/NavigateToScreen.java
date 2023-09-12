package com.microsoft.band.device.command;

import com.microsoft.band.internal.BandDeviceConstants;
import com.microsoft.band.internal.CommandBase;
import com.microsoft.band.internal.util.BufferUtil;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public class NavigateToScreen extends CommandBase {
    private static final int ARG_SIZE = 2;
    private static byte MESSAGE_SIZE = 0;
    private static final long serialVersionUID = 1;
    private byte mScreenId;

    public NavigateToScreen(byte screenId) {
        super(BandDeviceConstants.Command.CargoFireballUINavigateToScreen, 2, MESSAGE_SIZE);
        this.mScreenId = screenId;
    }

    @Override // com.microsoft.band.internal.CommandBase
    public byte[] getExtendedData() {
        ByteBuffer buffer = BufferUtil.allocateLittleEndian(2).putShort(this.mScreenId);
        return buffer.array();
    }

    /* loaded from: classes.dex */
    public enum Screens {
        OOBE_UPDATES_SCREEN(20),
        OOBE_ALMOST_THERE_SCREEN(21),
        OOBE_PRESS_BUTTON_TO_START_SCREEN(22);
        
        private int mId;

        public int getID() {
            return this.mId;
        }

        Screens(int id) {
            this.mId = id;
        }
    }
}
