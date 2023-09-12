package com.microsoft.band.personalization;

import android.graphics.Bitmap;
import com.microsoft.band.BandIOException;
import com.microsoft.band.BandPendingResult;
import com.microsoft.band.BandTheme;
/* loaded from: classes.dex */
public interface BandPersonalizationManager {
    BandPendingResult<Bitmap> getMeTileImage() throws BandIOException;

    BandPendingResult<BandTheme> getTheme() throws BandIOException;

    BandPendingResult<Void> setMeTileImage(Bitmap bitmap) throws BandIOException;

    BandPendingResult<Void> setTheme(BandTheme bandTheme) throws BandIOException;
}
