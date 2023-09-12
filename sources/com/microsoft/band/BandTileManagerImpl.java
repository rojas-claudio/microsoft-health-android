package com.microsoft.band;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.InstrumentationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.microsoft.band.internal.BandDeviceConstants;
import com.microsoft.band.internal.InternalBandConstants;
import com.microsoft.band.internal.ServiceCommand;
import com.microsoft.band.internal.util.KDKLog;
import com.microsoft.band.internal.util.Validation;
import com.microsoft.band.tiles.BandTile;
import com.microsoft.band.tiles.BandTileManager;
import com.microsoft.band.tiles.pages.PageData;
import com.microsoft.kapp.utils.StrappConstants;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class BandTileManagerImpl implements BandTileManager {
    private static final int DIALOG_TOP_BOTTOM_PADDING_DIP = 65;
    private static final int IMAGE_BACKGROUND_SIZE_DIP = 40;
    private static final int LEFT_DIALOG_PADDING_DIP = 10;
    private static final int SCALED_IMAGE_SIZE_DIP = 30;
    private static final String TAG = BandTileManagerImpl.class.getSimpleName();
    private static final int TILE_NAME_LEFT_PADDING_DIP = 18;
    private static final int TOP_ICON_DIALOG_PADDING_DIP = 35;
    private AtomicBoolean mAddAllowed = new AtomicBoolean(false);
    private final BandServiceConnection mServiceConnection;

    /* JADX INFO: Access modifiers changed from: package-private */
    public BandTileManagerImpl(BandServiceConnection connection) {
        this.mServiceConnection = connection;
    }

    @Override // com.microsoft.band.tiles.BandTileManager
    public BandPendingResult<List<BandTile>> getTiles() throws BandIOException {
        ServiceCommand cmdGetTiles = new ServiceCommand(BandDeviceConstants.Command.BandTileGetTiles.getCode(), (Bundle) null);
        return this.mServiceConnection.send(new TileWaitingCommandTask<List<BandTile>, ServiceCommand>(cmdGetTiles) { // from class: com.microsoft.band.BandTileManagerImpl.1
            @Override // com.microsoft.band.TileWaitingCommandTask
            public List<BandTile> toTileResult(ServiceCommand command, boolean isTimeOut) {
                return command.getBundle().getParcelableArrayList(InternalBandConstants.EXTRA_COMMAND_PAYLOAD);
            }
        });
    }

    @Override // com.microsoft.band.tiles.BandTileManager
    public BandPendingResult<Integer> getRemainingTileCapacity() throws BandIOException {
        ServiceCommand cmdGetRemainingTileCapacity = new ServiceCommand(BandDeviceConstants.Command.BandTileGetRemainingCapacity.getCode(), (Bundle) null);
        return this.mServiceConnection.send(new TileWaitingCommandTask<Integer, ServiceCommand>(cmdGetRemainingTileCapacity) { // from class: com.microsoft.band.BandTileManagerImpl.2
            @Override // com.microsoft.band.TileWaitingCommandTask
            public Integer toTileResult(ServiceCommand command, boolean isTimeOut) {
                return Integer.valueOf(command.getBundle().getInt(InternalBandConstants.EXTRA_COMMAND_PAYLOAD));
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public BandPendingResult<Boolean> addTileInternal(BandTile tile) throws BandIOException {
        Bundle cmdParams = new Bundle();
        cmdParams.putParcelable(InternalBandConstants.EXTRA_COMMAND_DATA, tile);
        ServiceCommand cmdAddTile = new ServiceCommand(BandDeviceConstants.Command.BandTileAddTile.getCode(), cmdParams);
        return this.mServiceConnection.send(new TileWaitingCommandTask<Boolean, ServiceCommand>(cmdAddTile) { // from class: com.microsoft.band.BandTileManagerImpl.3
            @Override // com.microsoft.band.TileWaitingCommandTask
            public Boolean toTileResult(ServiceCommand command, boolean isTimeOut) {
                return Boolean.valueOf(command.getBundle().getBoolean(InternalBandConstants.EXTRA_COMMAND_PAYLOAD, false));
            }
        });
    }

    @Override // com.microsoft.band.tiles.BandTileManager
    public BandPendingResult<Boolean> addTile(final Activity activity, final BandTile tile) throws BandIOException {
        Validation.notNull(tile, "Tile cannot be null");
        ConnectionState state = this.mServiceConnection.getConnectionState();
        if (state.ordinal() < ConnectionState.BOUND.ordinal()) {
            if (state == ConnectionState.INVALID_SDK_VERSION) {
                throw new BandIOException("Microsoft Health BandService doesn't support your SDK Version. Please update to latest SDK.", BandErrorType.UNSUPPORTED_SDK_VERSION_ERROR);
            }
            throw new BandIOException("Microsoft Health BandService is not bound. Please make sure Microsoft Health is installed and that you have connected to it with the correct permissions.", BandErrorType.SERVICE_ERROR);
        }
        try {
            Validation.validateInRange("The number of icons", tile.getPageIcons().size(), 0, InternalBandConstants.getAdditionalMaxIconsPerTile(this.mServiceConnection.getHardwareVersion()));
            ComplexTask<Boolean> complexTask = new ComplexNonUIAwaitableTask<Boolean>() { // from class: com.microsoft.band.BandTileManagerImpl.4
                @Override // com.microsoft.band.ComplexTask
                public Boolean tasks() throws BandException, InterruptedException {
                    if (tile.isBadgingEnabled() && tile.getTileSmallIcon() == null) {
                        KDKLog.d(BandTileManagerImpl.TAG, "Must provide a small icon when badging is enabled");
                        throw new IllegalArgumentException("A small icon must be provided when badging is enabled");
                    } else if (BandTileManagerImpl.this.getRemainingTileCapacity().await().intValue() > 0) {
                        if (BandTileManagerImpl.this.didUserAllowAdd(activity, tile)) {
                            return (Boolean) BandTileManagerImpl.this.addTileInternal(tile).await();
                        }
                        return false;
                    } else {
                        KDKLog.d(BandTileManagerImpl.TAG, "The band is full. Cannot add a new tile.");
                        throw new BandException("The band is full. Cannot add a new tile.", BandErrorType.BAND_FULL_ERROR);
                    }
                }
            };
            complexTask.start();
            return complexTask;
        } catch (BandException e) {
            return new ErrorBandPendingResult(e, Boolean.FALSE);
        } catch (InterruptedException e2) {
            return new ErrorBandPendingResult(e2, Boolean.FALSE);
        }
    }

    private boolean checkTestProject() {
        Context context = this.mServiceConnection.getContext();
        String packageName = context.getPackageName();
        if ("com.microsoft.band.sdk".equals(packageName) || StrappConstants.NOTIFICATION_SERVICE_MICROSOFT_HEALTH.equals(packageName)) {
            PackageManager pm = context.getPackageManager();
            List<InstrumentationInfo> infos = pm.queryInstrumentation(null, 0);
            if (infos == null || infos.size() <= 0) {
                return false;
            }
            for (InstrumentationInfo info : infos) {
                if ("android.test.InstrumentationTestRunner".equals(info.name)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    @Override // com.microsoft.band.tiles.BandTileManager
    public BandPendingResult<Boolean> removeTile(UUID tileId) throws BandIOException {
        Validation.notNull(tileId, "Tile ID cannot be null");
        Bundle cmdParams = new Bundle();
        cmdParams.putSerializable(InternalBandConstants.EXTRA_COMMAND_DATA, tileId);
        ServiceCommand cmdRemoveTile = new ServiceCommand(BandDeviceConstants.Command.BandTileRemoveTile.getCode(), cmdParams);
        return this.mServiceConnection.send(new TileWaitingCommandTask<Boolean, ServiceCommand>(cmdRemoveTile) { // from class: com.microsoft.band.BandTileManagerImpl.5
            @Override // com.microsoft.band.TileWaitingCommandTask
            public Boolean toTileResult(ServiceCommand command, boolean isTimeOut) {
                return Boolean.valueOf(command.getBundle().getBoolean(InternalBandConstants.EXTRA_COMMAND_PAYLOAD, false));
            }
        });
    }

    @Override // com.microsoft.band.tiles.BandTileManager
    public BandPendingResult<Boolean> removeTile(BandTile tile) throws BandIOException {
        Validation.notNull(tile, "Tile cannot be null");
        return removeTile(tile.getTileId());
    }

    @Override // com.microsoft.band.tiles.BandTileManager
    public BandPendingResult<Boolean> removePages(UUID tileId) throws BandIOException {
        Validation.notNull(tileId, "Tile ID cannot be null");
        Bundle cmdParams = new Bundle();
        cmdParams.putSerializable(InternalBandConstants.EXTRA_COMMAND_DATA, tileId);
        ServiceCommand cmdRemovePages = new ServiceCommand(BandDeviceConstants.Command.BandTileRemovePages.getCode(), cmdParams);
        return this.mServiceConnection.send(new TileWaitingCommandTask<Boolean, ServiceCommand>(cmdRemovePages) { // from class: com.microsoft.band.BandTileManagerImpl.6
            @Override // com.microsoft.band.TileWaitingCommandTask
            public Boolean toTileResult(ServiceCommand command, boolean isTimeOut) throws BandException {
                return Boolean.valueOf(command.getBundle().getBoolean(InternalBandConstants.EXTRA_COMMAND_PAYLOAD));
            }
        });
    }

    @Override // com.microsoft.band.tiles.BandTileManager
    public BandPendingResult<Boolean> setPages(UUID tileId, PageData... info) throws BandIOException {
        Validation.notNull(tileId, "Tile ID cannot be null");
        Validation.notNull(info, "Page data cannot be null");
        Validation.validateInRange("The number of page data objects", info.length, 0, 8);
        try {
            int hardwareVersion = this.mServiceConnection.getHardwareVersion();
            for (PageData data : info) {
                data.validate(hardwareVersion);
            }
            Bundle cmdParams = new Bundle();
            cmdParams.putSerializable(InternalBandConstants.EXTRA_COMMAND_DATA, tileId);
            cmdParams.putParcelableArray(InternalBandConstants.PAGE_DATA, info);
            ServiceCommand cmdSetPages = new ServiceCommand(BandDeviceConstants.Command.BandTileSetPages.getCode(), cmdParams);
            return this.mServiceConnection.send(new TileWaitingCommandTask<Boolean, ServiceCommand>(cmdSetPages) { // from class: com.microsoft.band.BandTileManagerImpl.7
                @Override // com.microsoft.band.TileWaitingCommandTask
                public Boolean toTileResult(ServiceCommand command, boolean isTimeOut) throws BandException {
                    return Boolean.valueOf(command.getBundle().getBoolean(InternalBandConstants.EXTRA_COMMAND_PAYLOAD, false));
                }
            });
        } catch (BandException e) {
            return new ErrorBandPendingResult(e, Boolean.FALSE);
        } catch (InterruptedException e2) {
            return new ErrorBandPendingResult(e2, Boolean.FALSE);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @SuppressLint({"RtlHardcoded"})
    public boolean didUserAllowAdd(Activity activity, BandTile tile) throws InterruptedException {
        Resources r = activity.getResources();
        Validation.notNull(activity, "Activity cannot be null");
        Validation.notNull(tile, "Tile cannot be null");
        this.mAddAllowed.set(false);
        LinearLayout mainLayout = new LinearLayout(activity);
        LinearLayout.LayoutParams mainLayoutParams = new LinearLayout.LayoutParams(-1, -1);
        mainLayoutParams.setMargins(0, 0, 0, 0);
        mainLayout.setPadding(0, convertDipTpPixels(r, 65), 0, convertDipTpPixels(r, 65));
        mainLayout.setLayoutParams(mainLayoutParams);
        mainLayout.setOrientation(1);
        mainLayout.setBackgroundColor(-16777216);
        TextView addTileQuestionView = new TextView(activity);
        addTileQuestionView.setTextColor(-1);
        addTileQuestionView.setText("Add this Tile to your Band?");
        addTileQuestionView.setTextSize(2, 20.0f);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(-2, -1);
        params1.gravity = 19;
        addTileQuestionView.setPadding(convertDipTpPixels(r, 10), 0, 0, 0);
        addTileQuestionView.setLayoutParams(params1);
        View line = new View(activity);
        LinearLayout.LayoutParams lineLayoutParams = new LinearLayout.LayoutParams(-1, 1);
        line.setLayoutParams(lineLayoutParams);
        line.setBackgroundColor(-1);
        LinearLayout iconDialogLayout = new LinearLayout(activity);
        LinearLayout.LayoutParams iconDialogLayoutParams = new LinearLayout.LayoutParams(-2, -1);
        iconDialogLayout.setOrientation(0);
        iconDialogLayout.setPadding(convertDipTpPixels(r, 10), convertDipTpPixels(r, 35), 0, 0);
        iconDialogLayout.setLayoutParams(iconDialogLayoutParams);
        Bitmap tileImage = tile.getTileIcon().getIcon();
        ImageView tileIconView = new ImageView(activity);
        LinearLayout.LayoutParams tileIconViewlayoutParams = new LinearLayout.LayoutParams(convertDipTpPixels(r, 40), convertDipTpPixels(r, 40));
        tileIconViewlayoutParams.gravity = 19;
        tileIconView.setLayoutParams(tileIconViewlayoutParams);
        tileIconView.setColorFilter(new LightingColorFilter(0, ViewCompat.MEASURED_SIZE_MASK));
        tileIconView.setImageBitmap(Bitmap.createScaledBitmap(tileImage, convertDipTpPixels(r, 30), convertDipTpPixels(r, 30), false));
        TextView tileNameTextView = new TextView(activity);
        tileNameTextView.setTextColor(-1);
        tileNameTextView.setText(tile.getTileName());
        tileNameTextView.setTextSize(2, 20.0f);
        LinearLayout.LayoutParams tileNameParams = new LinearLayout.LayoutParams(-2, convertDipTpPixels(r, 40));
        tileNameTextView.setPadding(convertDipTpPixels(r, 18), 0, 0, 0);
        tileNameTextView.setLayoutParams(tileNameParams);
        tileNameTextView.setGravity(16);
        iconDialogLayout.addView(tileIconView);
        iconDialogLayout.addView(tileNameTextView);
        mainLayout.addView(addTileQuestionView);
        mainLayout.addView(line);
        mainLayout.addView(iconDialogLayout);
        final CountDownLatch latch = new CountDownLatch(1);
        final AlertDialog.Builder alert = new AlertDialog.Builder(activity, 3);
        alert.setView(mainLayout);
        alert.setCancelable(false);
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() { // from class: com.microsoft.band.BandTileManagerImpl.8
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                BandTileManagerImpl.this.mAddAllowed.set(true);
                latch.countDown();
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() { // from class: com.microsoft.band.BandTileManagerImpl.9
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                BandTileManagerImpl.this.mAddAllowed.set(false);
                latch.countDown();
            }
        });
        Runnable dialogAction = new Runnable() { // from class: com.microsoft.band.BandTileManagerImpl.10
            @Override // java.lang.Runnable
            public void run() {
                AlertDialog alertDialog = alert.create();
                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.microsoft.band.BandTileManagerImpl.10.1
                    @Override // android.content.DialogInterface.OnShowListener
                    public void onShow(DialogInterface dialog) {
                        Button positiveButton = ((AlertDialog) dialog).getButton(-1);
                        positiveButton.setBackgroundColor(-16777216);
                        positiveButton.setTextColor(-1);
                        Button negativeButton = ((AlertDialog) dialog).getButton(-2);
                        negativeButton.setBackgroundColor(-16777216);
                        negativeButton.setTextColor(-1);
                    }
                });
                alertDialog.show();
            }
        };
        activity.runOnUiThread(dialogAction);
        latch.await();
        return this.mAddAllowed.get();
    }

    public static int convertDipTpPixels(Resources r, int dips) {
        return r == null ? dips : (int) TypedValue.applyDimension(1, dips, r.getDisplayMetrics());
    }
}
