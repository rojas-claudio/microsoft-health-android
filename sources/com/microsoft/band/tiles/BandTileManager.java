package com.microsoft.band.tiles;

import android.app.Activity;
import com.microsoft.band.BandIOException;
import com.microsoft.band.BandPendingResult;
import com.microsoft.band.tiles.pages.PageData;
import java.util.List;
import java.util.UUID;
/* loaded from: classes.dex */
public interface BandTileManager {
    BandPendingResult<Boolean> addTile(Activity activity, BandTile bandTile) throws BandIOException;

    BandPendingResult<Integer> getRemainingTileCapacity() throws BandIOException;

    BandPendingResult<List<BandTile>> getTiles() throws BandIOException;

    BandPendingResult<Boolean> removePages(UUID uuid) throws BandIOException;

    BandPendingResult<Boolean> removeTile(BandTile bandTile) throws BandIOException;

    BandPendingResult<Boolean> removeTile(UUID uuid) throws BandIOException;

    BandPendingResult<Boolean> setPages(UUID uuid, PageData... pageDataArr) throws BandIOException;
}
