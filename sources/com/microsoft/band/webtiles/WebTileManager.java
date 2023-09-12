package com.microsoft.band.webtiles;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import com.microsoft.band.client.CargoException;
import com.microsoft.band.device.CargoStrapp;
import com.microsoft.band.device.StrappLayout;
import com.microsoft.band.internal.BandServiceMessage;
import com.microsoft.band.internal.util.KDKLog;
import com.microsoft.band.internal.util.VersionCheck;
import com.microsoft.band.service.CargoServiceException;
import com.microsoft.band.service.util.FileHelper;
import com.microsoft.band.util.StreamUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.json.JSONException;
/* loaded from: classes.dex */
public class WebTileManager {
    public static final int BUFFER = 1024;
    private static final String TAG = WebTileManager.class.getSimpleName();

    private WebTileManager() {
        throw new UnsupportedOperationException();
    }

    public static WebTile createWebTileFromWebUrl(String url, String basePath) throws JSONException, IOException, WebTileException {
        return createWebTileFromWebUrl(url, basePath, true);
    }

    private static WebTile createWebTileFromWebUrl(String url, String basePath, boolean reattemptOnUnknownHost) throws JSONException, IOException, WebTileException {
        InputStream is = null;
        String host = "";
        try {
            URL androidURL = new URL(url);
            host = androidURL.getHost();
            HttpURLConnection urlConnection = (HttpURLConnection) androidURL.openConnection();
            urlConnection.setConnectTimeout(20000);
            is = urlConnection.getInputStream();
        } catch (UnknownHostException e) {
            if (reattemptOnUnknownHost) {
                if (!TextUtils.isEmpty(url) && TextUtils.isEmpty(host)) {
                    StreamUtils.closeQuietly(null);
                    return createWebTileFromWebUrl(url.replaceFirst(":/", "://"), basePath, false);
                }
            } else {
                StreamUtils.closeQuietly(null);
                throw new WebTileException(e);
            }
        } catch (Exception e2) {
            StreamUtils.closeQuietly(null);
            throw new WebTileException(e2);
        }
        File tempDirectory = unzipWebtile(is, basePath);
        return new WebTile(tempDirectory);
    }

    public static WebTile createWebTileFromFileUrl(Uri uri, String basePath, ContentResolver cResolver) throws FileNotFoundException, IOException, JSONException {
        File tempDirectory = unzipWebtile(cResolver.openInputStream(uri), basePath);
        return new WebTile(tempDirectory);
    }

    public static WebTile createWebTileFromPackagePath(String path) {
        throw new UnsupportedOperationException();
    }

    public static WebTile readWebTileFromPackagePath(String basePath, UUID tileId) throws IllegalArgumentException, JSONException, IOException {
        return new WebTile(getTileLocationForUUID(tileId, basePath), tileId);
    }

    public static boolean saveWebTileFromTemp(WebTile tileFromTemp) {
        try {
            File tempDirectory = tileFromTemp.getDirectory();
            File saveDirectory = getTileLocationForUUID(tileFromTemp.getTileId(), tileFromTemp.getDirectory().getParent());
            FileHelper.renameFileTo(tempDirectory, saveDirectory);
            tileFromTemp.setDirectory(saveDirectory);
            FileHelper.deleteDirectory(tempDirectory, true);
            tileFromTemp.saveResourceAuthentication();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean deleteWebTile(UUID tileId) {
        throw new UnsupportedOperationException();
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x0055, code lost:
        com.microsoft.band.internal.util.KDKLog.e(com.microsoft.band.webtiles.WebTileManager.TAG, "Failed to create " + r5.getName());
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x007a, code lost:
        throw new java.io.IOException("Problem with creating temporary webtile directory");
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.io.File unzipWebtile(java.io.InputStream r14, java.lang.String r15) throws java.io.IOException {
        /*
            java.util.zip.ZipInputStream r8 = new java.util.zip.ZipInputStream
            r8.<init>(r14)
            java.io.File r3 = getTempFolder(r15)     // Catch: java.lang.Throwable -> L7b
            deleteAllFilesInDirectory(r3)     // Catch: java.lang.Throwable -> L7b
            r4 = 0
        Ld:
            java.util.zip.ZipEntry r4 = r8.getNextEntry()     // Catch: java.lang.Throwable -> L7b
            if (r4 == 0) goto Lbb
            java.lang.String r6 = r4.getName()     // Catch: java.lang.Throwable -> L7b
            java.lang.String r9 = com.microsoft.band.webtiles.WebTileManager.TAG     // Catch: java.lang.Throwable -> L7b
            com.microsoft.band.internal.util.KDKLog.i(r9, r6)     // Catch: java.lang.Throwable -> L7b
            java.lang.String r9 = "\\"
            boolean r9 = r6.contains(r9)     // Catch: java.lang.Throwable -> L7b
            if (r9 == 0) goto L33
            java.util.zip.ZipEntry r4 = new java.util.zip.ZipEntry     // Catch: java.lang.Throwable -> L7b
            java.lang.String r9 = "\\"
            java.lang.String r10 = java.io.File.separator     // Catch: java.lang.Throwable -> L7b
            java.lang.String r9 = r6.replace(r9, r10)     // Catch: java.lang.Throwable -> L7b
            r4.<init>(r9)     // Catch: java.lang.Throwable -> L7b
        L33:
            java.io.File r5 = new java.io.File     // Catch: java.lang.Throwable -> L7b
            java.lang.String r9 = r4.getName()     // Catch: java.lang.Throwable -> L7b
            r5.<init>(r3, r9)     // Catch: java.lang.Throwable -> L7b
            java.io.File r7 = r5.getParentFile()     // Catch: java.lang.Throwable -> L7b
            r7.mkdirs()     // Catch: java.lang.Throwable -> L7b
            boolean r9 = r4.isDirectory()     // Catch: java.lang.Throwable -> L7b
            if (r9 == 0) goto L83
            boolean r9 = r5.exists()     // Catch: java.lang.Throwable -> L7b
            if (r9 != 0) goto Ld
            boolean r9 = r5.mkdirs()     // Catch: java.lang.Throwable -> L7b
            if (r9 != 0) goto Ld
            java.lang.String r9 = com.microsoft.band.webtiles.WebTileManager.TAG     // Catch: java.lang.Throwable -> L7b
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L7b
            r10.<init>()     // Catch: java.lang.Throwable -> L7b
            java.lang.String r11 = "Failed to create "
            java.lang.StringBuilder r10 = r10.append(r11)     // Catch: java.lang.Throwable -> L7b
            java.lang.String r11 = r5.getName()     // Catch: java.lang.Throwable -> L7b
            java.lang.StringBuilder r10 = r10.append(r11)     // Catch: java.lang.Throwable -> L7b
            java.lang.String r10 = r10.toString()     // Catch: java.lang.Throwable -> L7b
            com.microsoft.band.internal.util.KDKLog.e(r9, r10)     // Catch: java.lang.Throwable -> L7b
            java.io.IOException r9 = new java.io.IOException     // Catch: java.lang.Throwable -> L7b
            java.lang.String r10 = "Problem with creating temporary webtile directory"
            r9.<init>(r10)     // Catch: java.lang.Throwable -> L7b
            throw r9     // Catch: java.lang.Throwable -> L7b
        L7b:
            r9 = move-exception
            r8.close()
            r14.close()
            throw r9
        L83:
            java.io.FileOutputStream r2 = new java.io.FileOutputStream     // Catch: java.lang.Throwable -> L7b
            r2.<init>(r5)     // Catch: java.lang.Throwable -> L7b
            r9 = 1024(0x400, float:1.435E-42)
            byte[] r0 = new byte[r9]     // Catch: java.lang.Throwable -> L7b
        L8c:
            r9 = 0
            int r10 = r0.length     // Catch: java.lang.Throwable -> L7b
            int r1 = r8.read(r0, r9, r10)     // Catch: java.lang.Throwable -> L7b
            r9 = -1
            if (r1 == r9) goto L9a
            r9 = 0
            r2.write(r0, r9, r1)     // Catch: java.lang.Throwable -> L7b
            goto L8c
        L9a:
            java.lang.String r9 = com.microsoft.band.webtiles.WebTileManager.TAG     // Catch: java.lang.Throwable -> L7b
            java.lang.String r10 = "Wrote file %s"
            r11 = 1
            java.lang.Object[] r11 = new java.lang.Object[r11]     // Catch: java.lang.Throwable -> L7b
            r12 = 0
            java.lang.String r13 = r5.getName()     // Catch: java.lang.Throwable -> L7b
            r11[r12] = r13     // Catch: java.lang.Throwable -> L7b
            java.lang.String r10 = java.lang.String.format(r10, r11)     // Catch: java.lang.Throwable -> L7b
            com.microsoft.band.internal.util.KDKLog.i(r9, r10)     // Catch: java.lang.Throwable -> L7b
            r2.flush()     // Catch: java.lang.Throwable -> L7b
            r2.close()     // Catch: java.lang.Throwable -> L7b
            r8.closeEntry()     // Catch: java.lang.Throwable -> L7b
            goto Ld
        Lbb:
            r8.close()
            r14.close()
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.microsoft.band.webtiles.WebTileManager.unzipWebtile(java.io.InputStream, java.lang.String):java.io.File");
    }

    public static CargoStrapp webTileToCargoStrapp(WebTile tile, Context context, byte[] webTileHash, int hardwareVersion) throws CargoException {
        int mask = 0;
        List<Bitmap> images = tile.getIcons();
        if (tile.getBadgeIconPath() == null) {
            images.set(1, tile.getTileIcon().getIcon());
        } else {
            mask = 0 | 2;
        }
        List<StrappLayout> layouts = new ArrayList<>();
        for (Map.Entry<PageLayoutStyle, Integer> entry : tile.getLayouts().entrySet()) {
            layouts.add(getPreDefinedLBLOB(entry.getKey(), context, hardwareVersion));
        }
        return new CargoStrapp(tile.getTileId(), tile.getName(), mask, tile.getTileTheme() != null ? tile.getTileTheme().getBaseColor() : 0L, images, (short) 0, layouts, 1, webTileHash);
    }

    private static StrappLayout getPreDefinedLBLOB(PageLayoutStyle layoutStyle, Context context, int hardwareVersion) throws CargoException {
        Resources resource = context.getResources();
        InputStream stream = null;
        try {
            try {
                String lblobName = layoutStyle.getLBlobName();
                if (VersionCheck.isV2DeviceOrGreater(hardwareVersion)) {
                    lblobName = lblobName + "_v2";
                }
                int rID = resource.getIdentifier(lblobName, "raw", context.getPackageName());
                stream = resource.openRawResource(rID);
                return new StrappLayout(FileHelper.readBytesFromStream(stream));
            } catch (Resources.NotFoundException e) {
                String message = "Cannot find preDefined LBLOB " + layoutStyle.getName();
                KDKLog.e(TAG, message);
                throw new CargoException(message, BandServiceMessage.Response.WEB_TILE_LBLOB_ERROR);
            } catch (IOException e2) {
                String message2 = "Cannot read preDefined LBLOB " + layoutStyle.getName();
                KDKLog.e(TAG, message2);
                throw new CargoException(message2, BandServiceMessage.Response.WEB_TILE_LBLOB_ERROR);
            }
        } finally {
            StreamUtils.closeQuietly(stream);
        }
    }

    private static File getTileLocationForUUID(UUID uuid, String absolutePath) {
        return FileHelper.directoryAtPath(absolutePath + FileHelper.WEBTILE_DIR + File.separator + uuid.toString() + FileHelper.PACKAGE_PATH);
    }

    private static File getTempFolder(String absolutePath) {
        return FileHelper.directoryAtPath(absolutePath + FileHelper.TEMP_WEBTILE_DIR);
    }

    private static void deleteAllFilesInDirectory(File directory) throws IOException {
        try {
            FileHelper.deleteAllFilesInDirectory(directory, true);
        } catch (CargoServiceException e) {
            throw new IOException(e.getMessage());
        }
    }

    public static List<UUID> getInstalledWebTileIds(String absolutePath) {
        List<UUID> webtileIds = new ArrayList<>();
        File webtileDirectory = FileHelper.directoryAtPath(absolutePath + FileHelper.WEBTILE_DIR);
        String[] filesList = webtileDirectory.list();
        if (filesList != null && filesList.length > 0) {
            for (String file : filesList) {
                try {
                    UUID id = UUID.fromString(file);
                    webtileIds.add(id);
                } catch (IllegalArgumentException e) {
                    KDKLog.e(TAG, "File %s is not a UUID", file);
                }
            }
        }
        return webtileIds;
    }
}
