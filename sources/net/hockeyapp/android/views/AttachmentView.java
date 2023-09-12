package net.hockeyapp.android.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import java.io.File;
import net.hockeyapp.android.Constants;
import net.hockeyapp.android.objects.FeedbackAttachment;
import net.hockeyapp.android.utils.ImageUtils;
/* loaded from: classes.dex */
public class AttachmentView extends FrameLayout {
    private static final int IMAGES_PER_ROW_LANDSCAPE = 2;
    private static final int IMAGES_PER_ROW_PORTRAIT = 3;
    private final FeedbackAttachment attachment;
    private final Uri attachmentUri;
    private final Context context;
    private final String filename;
    private int gap;
    private ImageView imageView;
    private int maxHeightLandscape;
    private int maxHeightPortrait;
    private int orientation;
    private final ViewGroup parent;
    private TextView textView;
    private int widthLandscape;
    private int widthPortrait;

    /* JADX WARN: Type inference failed for: r0v4, types: [net.hockeyapp.android.views.AttachmentView$1] */
    public AttachmentView(Context context, ViewGroup parent, Uri attachmentUri, boolean removable) {
        super(context);
        this.context = context;
        this.parent = parent;
        this.attachment = null;
        this.attachmentUri = attachmentUri;
        this.filename = attachmentUri.getLastPathSegment();
        calculateDimensions(20);
        initializeView(context, removable);
        this.textView.setText(this.filename);
        new AsyncTask<Void, Void, Bitmap>() { // from class: net.hockeyapp.android.views.AttachmentView.1
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public Bitmap doInBackground(Void... args) {
                Bitmap bitmap = AttachmentView.this.loadImageThumbnail();
                return bitmap;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public void onPostExecute(Bitmap bitmap) {
                if (bitmap != null) {
                    AttachmentView.this.configureViewForThumbnail(bitmap, false);
                } else {
                    AttachmentView.this.configureViewForPlaceholder(false);
                }
            }
        }.execute(new Void[0]);
    }

    public AttachmentView(Context context, ViewGroup parent, FeedbackAttachment attachment, boolean removable) {
        super(context);
        this.context = context;
        this.parent = parent;
        this.attachment = attachment;
        this.attachmentUri = Uri.fromFile(new File(Constants.getHockeyAppStorageDir(), attachment.getCacheId()));
        this.filename = attachment.getFilename();
        calculateDimensions(30);
        initializeView(context, removable);
        this.orientation = 0;
        this.textView.setText("Loading...");
        configureViewForPlaceholder(false);
    }

    public FeedbackAttachment getAttachment() {
        return this.attachment;
    }

    public Uri getAttachmentUri() {
        return this.attachmentUri;
    }

    public int getWidthPortrait() {
        return this.widthPortrait;
    }

    public int getMaxHeightPortrait() {
        return this.maxHeightPortrait;
    }

    public int getWidthLandscape() {
        return this.widthLandscape;
    }

    public int getMaxHeightLandscape() {
        return this.maxHeightLandscape;
    }

    public int getGap() {
        return this.gap;
    }

    public int getEffectiveMaxHeight() {
        return this.orientation == 1 ? this.maxHeightLandscape : this.maxHeightPortrait;
    }

    public void remove() {
        this.parent.removeView(this);
    }

    public void setImage(Bitmap bitmap, int orientation) {
        this.textView.setText(this.filename);
        this.orientation = orientation;
        if (bitmap == null) {
            configureViewForPlaceholder(true);
        } else {
            configureViewForThumbnail(bitmap, true);
        }
    }

    public void signalImageLoadingError() {
        this.textView.setText(TelemetryConstants.Events.Error.Dimensions.LOG_TYPE_ERROR);
    }

    private void calculateDimensions(int marginDip) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        this.gap = Math.round(TypedValue.applyDimension(1, 10.0f, metrics));
        int layoutMargin = Math.round(TypedValue.applyDimension(1, marginDip, metrics));
        int displayWidth = metrics.widthPixels;
        int parentWidthPortrait = (displayWidth - (layoutMargin * 2)) - (this.gap * 2);
        int parentWidthLandscape = (displayWidth - (layoutMargin * 2)) - (this.gap * 1);
        this.widthPortrait = parentWidthPortrait / 3;
        this.widthLandscape = parentWidthLandscape / 2;
        this.maxHeightPortrait = this.widthPortrait * 2;
        this.maxHeightLandscape = this.widthLandscape;
    }

    private void initializeView(Context context, boolean removable) {
        setLayoutParams(new FrameLayout.LayoutParams(-2, -2, 80));
        setPadding(0, this.gap, 0, 0);
        this.imageView = new ImageView(context);
        LinearLayout bottomView = new LinearLayout(context);
        bottomView.setLayoutParams(new FrameLayout.LayoutParams(-1, -2, 80));
        bottomView.setGravity(3);
        bottomView.setOrientation(1);
        bottomView.setBackgroundColor(Color.parseColor("#80262626"));
        this.textView = new TextView(context);
        this.textView.setLayoutParams(new FrameLayout.LayoutParams(-1, -2, 17));
        this.textView.setGravity(17);
        this.textView.setTextColor(Color.parseColor("#FFFFFF"));
        this.textView.setSingleLine();
        this.textView.setEllipsize(TextUtils.TruncateAt.MIDDLE);
        if (removable) {
            ImageButton imageButton = new ImageButton(context);
            imageButton.setLayoutParams(new FrameLayout.LayoutParams(-1, -2, 80));
            imageButton.setAdjustViewBounds(true);
            imageButton.setImageDrawable(getSystemIcon("ic_menu_delete"));
            imageButton.setBackgroundResource(0);
            imageButton.setOnClickListener(new View.OnClickListener() { // from class: net.hockeyapp.android.views.AttachmentView.2
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    AttachmentView.this.remove();
                }
            });
            bottomView.addView(imageButton);
        }
        bottomView.addView(this.textView);
        addView(this.imageView);
        addView(bottomView);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void configureViewForThumbnail(Bitmap bitmap, final boolean openOnClick) {
        int width = this.orientation == 1 ? this.widthLandscape : this.widthPortrait;
        int height = this.orientation == 1 ? this.maxHeightLandscape : this.maxHeightPortrait;
        this.textView.setMaxWidth(width);
        this.textView.setMinWidth(width);
        this.imageView.setLayoutParams(new FrameLayout.LayoutParams(-2, -2));
        this.imageView.setAdjustViewBounds(true);
        this.imageView.setMinimumWidth(width);
        this.imageView.setMaxWidth(width);
        this.imageView.setMaxHeight(height);
        this.imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        this.imageView.setImageBitmap(bitmap);
        this.imageView.setOnClickListener(new View.OnClickListener() { // from class: net.hockeyapp.android.views.AttachmentView.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (openOnClick) {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    intent.setDataAndType(AttachmentView.this.attachmentUri, "image/*");
                    AttachmentView.this.context.startActivity(intent);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void configureViewForPlaceholder(final boolean openOnClick) {
        this.textView.setMaxWidth(this.widthPortrait);
        this.textView.setMinWidth(this.widthPortrait);
        this.imageView.setLayoutParams(new FrameLayout.LayoutParams(-2, -2));
        this.imageView.setAdjustViewBounds(false);
        this.imageView.setBackgroundColor(Color.parseColor("#eeeeee"));
        this.imageView.setMinimumHeight((int) (this.widthPortrait * 1.2f));
        this.imageView.setMinimumWidth(this.widthPortrait);
        this.imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        this.imageView.setImageDrawable(getSystemIcon("ic_menu_attachment"));
        this.imageView.setOnClickListener(new View.OnClickListener() { // from class: net.hockeyapp.android.views.AttachmentView.4
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (openOnClick) {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    intent.setDataAndType(AttachmentView.this.attachmentUri, "*/*");
                    AttachmentView.this.context.startActivity(intent);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Bitmap loadImageThumbnail() {
        try {
            this.orientation = ImageUtils.determineOrientation(this.context, this.attachmentUri);
            int width = this.orientation == 1 ? this.widthLandscape : this.widthPortrait;
            int height = this.orientation == 1 ? this.maxHeightLandscape : this.maxHeightPortrait;
            return ImageUtils.decodeSampledBitmap(this.context, this.attachmentUri, width, height);
        } catch (Throwable th) {
            return null;
        }
    }

    private Drawable getSystemIcon(String name) {
        return getResources().getDrawable(getResources().getIdentifier(name, "drawable", "android"));
    }
}
