package com.microsoft.kapp.factories;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import com.facebook.model.OpenGraphAction;
import com.facebook.widget.FacebookDialog;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.telephony.MmsColumns;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/* loaded from: classes.dex */
public class ShareObject {
    public static final String TAG = ShareObject.class.getSimpleName();
    private FacebookDialog mFacebookDialog;
    private Intent mIntent;

    private ShareObject(Intent intent) {
        this.mIntent = intent;
    }

    private ShareObject(FacebookDialog facebookDialog) {
        this.mFacebookDialog = facebookDialog;
    }

    public void share(Context context) {
        if (this.mFacebookDialog != null) {
            this.mFacebookDialog.present();
        } else if (this.mIntent != null && context != null) {
            context.startActivity(this.mIntent);
        } else {
            KLog.i(TAG, "Unable to post to either Facebook or Send intent");
        }
    }

    /* loaded from: classes.dex */
    public static class Builder {
        private String mChooserTitle = "";
        private String mTitle = null;
        private String mSubject = null;
        private ArrayList<Uri> mImages = null;
        private boolean mSimilarTypes = true;
        private String mImagesType = null;
        private OpenGraphAction mOgAction = null;
        private String mOgPreviewProperty = null;

        public ShareObject build(Activity activity) {
            if (FacebookDialog.canPresentShareDialog(activity, FacebookDialog.ShareDialogFeature.PHOTOS)) {
                if (Validate.isNotNullNotEmpty(this.mImages)) {
                    FacebookDialog.PhotoShareDialogBuilder builder = new FacebookDialog.PhotoShareDialogBuilder(activity);
                    List<File> images = new ArrayList<>();
                    Iterator i$ = this.mImages.iterator();
                    while (i$.hasNext()) {
                        Uri imageUri = i$.next();
                        images.add(new File(imageUri.getPath()));
                    }
                    builder.addPhotoFiles(images);
                    return new ShareObject(builder.build());
                }
                FacebookDialog.OpenGraphActionDialogBuilder actionBuilder = new FacebookDialog.OpenGraphActionDialogBuilder(activity, this.mOgAction, this.mOgPreviewProperty);
                return new ShareObject(actionBuilder.build());
            }
            Intent shareIntent = new Intent();
            if (Validate.isNotNullNotEmpty(this.mImages)) {
                if (this.mImages.size() == 1) {
                    shareIntent.setAction("android.intent.action.SEND");
                    shareIntent.putExtra("android.intent.extra.STREAM", this.mImages.get(0));
                } else {
                    shareIntent.setAction("android.intent.action.SEND_MULTIPLE");
                    shareIntent.putParcelableArrayListExtra("android.intent.extra.STREAM", this.mImages);
                }
                shareIntent.setType(this.mSimilarTypes ? MmsColumns.MMS_PART_TYPE_IMAGE_PREFIX + this.mImagesType : "image/*");
            } else {
                shareIntent.setAction("android.intent.action.SEND");
                if (this.mTitle != null) {
                    shareIntent.putExtra("android.intent.extra.TEXT", this.mTitle);
                }
                if (this.mSubject != null) {
                    shareIntent.putExtra("android.intent.extra.SUBJECT", this.mSubject);
                }
                shareIntent.setType(MmsColumns.MMS_PART_TYPE_TEXT);
            }
            shareIntent.addFlags(1);
            Intent chooser = Intent.createChooser(shareIntent, this.mChooserTitle);
            return new ShareObject(chooser);
        }

        public Builder setChooserTitle(String chooserTitle) {
            this.mChooserTitle = chooserTitle;
            return this;
        }

        public Builder setTitle(String title) {
            this.mTitle = title;
            return this;
        }

        public Builder setSubject(String subject) {
            this.mSubject = subject;
            return this;
        }

        public Builder setOpenGraph(OpenGraphAction ogAction, String previewProperty) {
            if (ogAction != null && previewProperty != null && !previewProperty.isEmpty()) {
                this.mOgAction = ogAction;
                this.mOgPreviewProperty = previewProperty;
            }
            return this;
        }

        public Builder addImage(Uri imageUri) {
            if (this.mImages == null) {
                this.mImages = new ArrayList<>();
            }
            if (checkForSpecificImageType(MimeTypeMap.getFileExtensionFromUrl(imageUri.toString()))) {
                this.mImages.add(imageUri);
            }
            return this;
        }

        private boolean checkForSpecificImageType(String imageType) {
            if (imageType.isEmpty()) {
                return false;
            }
            if (this.mSimilarTypes) {
                if (this.mImagesType != null) {
                    if (!this.mImagesType.equals(imageType)) {
                        this.mSimilarTypes = false;
                    }
                } else {
                    this.mImagesType = imageType;
                }
            }
            return true;
        }
    }
}
