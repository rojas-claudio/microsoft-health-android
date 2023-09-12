package com.microsoft.band.device.command;

import com.microsoft.band.internal.BandDeviceConstants;
/* loaded from: classes.dex */
public enum CargoFileName {
    CRASHDUMP(BandDeviceConstants.Command.CargoCrashDumpGetFileSize, BandDeviceConstants.Command.CargoCrashDumpReadAndDeleteFile, null),
    EPHEMERIS(null, null, BandDeviceConstants.Command.CargoSystemSettingsEphemerisFileWrite),
    INSTRUMENTATION(BandDeviceConstants.Command.CargoInstFileGetSize, BandDeviceConstants.Command.CargoInstFileRead, null),
    TIME_ZONE(null, null, BandDeviceConstants.Command.CargoTimeUpdateTimezoneFile),
    FITNESS_PLANS(BandDeviceConstants.Command.CargoFitnessPlanFileMaxSize, null, BandDeviceConstants.Command.CargoFitnessPlansFileWrite),
    GOLF_COURSE(BandDeviceConstants.Command.CargoGolfCourseFileGetMaxSize, null, BandDeviceConstants.Command.CargoGolfCourseFileWrite);
    
    protected final BandDeviceConstants.Command mCmdGetSize;
    protected final BandDeviceConstants.Command mCmdReadFile;
    protected final BandDeviceConstants.Command mCmdWriteFile;

    CargoFileName(BandDeviceConstants.Command cmdGetSize, BandDeviceConstants.Command cmdReadFile, BandDeviceConstants.Command cmdWriteFile) {
        this.mCmdGetSize = cmdGetSize;
        this.mCmdReadFile = cmdReadFile;
        this.mCmdWriteFile = cmdWriteFile;
    }
}
