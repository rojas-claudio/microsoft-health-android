package net.hockeyapp.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.microsoft.kapp.telephony.MmsColumns;
import java.io.File;
import java.io.FileOutputStream;
import net.hockeyapp.android.views.PaintView;
/* loaded from: classes.dex */
public class PaintActivity extends Activity {
    private static final int MENU_CLEAR_ID = 3;
    private static final int MENU_SAVE_ID = 1;
    private static final int MENU_UNDO_ID = 2;
    private String imageName;
    private PaintView paintView;

    @Override // android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        Uri imageUri = (Uri) extras.getParcelable("imageUri");
        this.imageName = determineFilename(imageUri, imageUri.getLastPathSegment());
        int displayWidth = getResources().getDisplayMetrics().widthPixels;
        int displayHeight = getResources().getDisplayMetrics().heightPixels;
        int currentOrientation = displayWidth > displayHeight ? 0 : 1;
        int desiredOrientation = PaintView.determineOrientation(getContentResolver(), imageUri);
        setRequestedOrientation(desiredOrientation);
        if (currentOrientation != desiredOrientation) {
            Log.d("HockeyApp", "Image loading skipped because activity will be destroyed for orientation change.");
            return;
        }
        this.paintView = new PaintView(this, imageUri, displayWidth, displayHeight);
        LinearLayout vLayout = new LinearLayout(this);
        LinearLayout.LayoutParams vParams = new LinearLayout.LayoutParams(-1, -1);
        vLayout.setLayoutParams(vParams);
        vLayout.setGravity(17);
        vLayout.setOrientation(1);
        LinearLayout hLayout = new LinearLayout(this);
        LinearLayout.LayoutParams hParams = new LinearLayout.LayoutParams(-1, -1);
        hLayout.setLayoutParams(hParams);
        hLayout.setGravity(17);
        hLayout.setOrientation(0);
        vLayout.addView(hLayout);
        hLayout.addView(this.paintView);
        setContentView(vLayout);
        Toast toast = Toast.makeText(this, Strings.get(Strings.PAINT_INDICATOR_TOAST_ID), 1000);
        toast.show();
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, 1, 0, Strings.get(Strings.PAINT_MENU_SAVE_ID));
        menu.add(0, 2, 0, Strings.get(Strings.PAINT_MENU_UNDO_ID));
        menu.add(0, 3, 0, Strings.get(Strings.PAINT_MENU_CLEAR_ID));
        return true;
    }

    @Override // android.app.Activity
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                makeResult();
                return true;
            case 2:
                this.paintView.undo();
                return true;
            case 3:
                this.paintView.clearImage();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4 || this.paintView.isClear()) {
            return super.onKeyDown(keyCode, event);
        }
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() { // from class: net.hockeyapp.android.PaintActivity.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case -1:
                        PaintActivity.this.finish();
                        return;
                    default:
                        return;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(Strings.get(Strings.PAINT_DIALOG_MESSAGE_ID)).setPositiveButton(Strings.get(Strings.PAINT_DIALOG_POSITIVE_BUTTON_ID), dialogClickListener).setNegativeButton(Strings.get(Strings.PAINT_DIALOG_NEGATIVE_BUTTON_ID), dialogClickListener).show();
        return true;
    }

    /* JADX WARN: Type inference failed for: r6v5, types: [net.hockeyapp.android.PaintActivity$2] */
    private void makeResult() {
        File hockeyAppCache = new File(getCacheDir(), "HockeyApp");
        hockeyAppCache.mkdir();
        File result = new File(hockeyAppCache, this.imageName);
        int suffix = 1;
        while (result.exists()) {
            result = new File(hockeyAppCache, this.imageName + "_" + suffix);
            suffix++;
        }
        this.paintView.setDrawingCacheEnabled(true);
        final Bitmap bitmap = this.paintView.getDrawingCache();
        new AsyncTask<File, Void, Void>() { // from class: net.hockeyapp.android.PaintActivity.2
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public Void doInBackground(File... args) {
                try {
                    FileOutputStream out = new FileOutputStream(args[0]);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.close();
                    return null;
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("HockeyApp", "Could not save image.", e);
                    return null;
                }
            }
        }.execute(result);
        Intent intent = new Intent();
        Uri uri = Uri.fromFile(result);
        intent.putExtra("imageUri", uri);
        if (getParent() == null) {
            setResult(-1, intent);
        } else {
            getParent().setResult(-1, intent);
        }
        finish();
    }

    private String determineFilename(Uri uri, String fallback) {
        String[] projection = {MmsColumns.MMS_PART_DATA};
        String path = null;
        ContentResolver cr = getApplicationContext().getContentResolver();
        Cursor metaCursor = cr.query(uri, projection, null, null, null);
        if (metaCursor != null) {
            try {
                if (metaCursor.moveToFirst()) {
                    path = metaCursor.getString(0);
                }
            } finally {
                metaCursor.close();
            }
        }
        if (path == null) {
            return fallback;
        }
        String fallback2 = new File(path).getName();
        return fallback2;
    }
}
