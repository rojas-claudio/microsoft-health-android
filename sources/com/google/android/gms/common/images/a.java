package com.google.android.gms.common.images;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.common.images.ImageManager;
import com.google.android.gms.internal.cz;
import com.google.android.gms.internal.da;
import com.google.android.gms.internal.db;
import com.google.android.gms.internal.dl;
import com.google.android.gms.internal.ek;
import com.microsoft.kapp.utils.Constants;
import java.lang.ref.WeakReference;
/* loaded from: classes.dex */
public final class a {
    final C0001a jS;
    private int jT;
    private int jU;
    int jV;
    private int jW;
    private WeakReference<ImageManager.OnImageLoadedListener> jX;
    private WeakReference<ImageView> jY;
    private WeakReference<TextView> jZ;
    private int ka;
    private boolean kb;
    private boolean kc;

    /* renamed from: com.google.android.gms.common.images.a$a  reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public static final class C0001a {
        public final Uri uri;

        public C0001a(Uri uri) {
            this.uri = uri;
        }

        public boolean equals(Object obj) {
            if (obj instanceof C0001a) {
                return this == obj || ((C0001a) obj).hashCode() == hashCode();
            }
            return false;
        }

        public int hashCode() {
            return dl.hashCode(this.uri);
        }
    }

    public a(int i) {
        this.jT = 0;
        this.jU = 0;
        this.ka = -1;
        this.kb = true;
        this.kc = false;
        this.jS = new C0001a(null);
        this.jU = i;
    }

    public a(Uri uri) {
        this.jT = 0;
        this.jU = 0;
        this.ka = -1;
        this.kb = true;
        this.kc = false;
        this.jS = new C0001a(uri);
        this.jU = 0;
    }

    private cz a(Drawable drawable, Drawable drawable2) {
        if (drawable == null) {
            drawable = null;
        } else if (drawable instanceof cz) {
            drawable = ((cz) drawable).aS();
        }
        return new cz(drawable, drawable2);
    }

    private void a(Drawable drawable, boolean z, boolean z2, boolean z3) {
        ImageManager.OnImageLoadedListener onImageLoadedListener;
        switch (this.jV) {
            case 1:
                if (z2 || (onImageLoadedListener = this.jX.get()) == null) {
                    return;
                }
                onImageLoadedListener.onImageLoaded(this.jS.uri, drawable);
                return;
            case 2:
                ImageView imageView = this.jY.get();
                if (imageView != null) {
                    a(imageView, drawable, z, z2, z3);
                    return;
                }
                return;
            case 3:
                TextView textView = this.jZ.get();
                if (textView != null) {
                    a(textView, this.ka, drawable, z, z2);
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void a(ImageView imageView, Drawable drawable, boolean z, boolean z2, boolean z3) {
        boolean z4 = (z2 || z3) ? false : true;
        if (z4 && (imageView instanceof da)) {
            int aU = ((da) imageView).aU();
            if (this.jU != 0 && aU == this.jU) {
                return;
            }
        }
        boolean a = a(z, z2);
        Drawable a2 = a ? a(imageView.getDrawable(), drawable) : drawable;
        imageView.setImageDrawable(a2);
        if (imageView instanceof da) {
            da daVar = (da) imageView;
            daVar.d(z3 ? this.jS.uri : null);
            daVar.w(z4 ? this.jU : 0);
        }
        if (a) {
            ((cz) a2).startTransition(Constants.FILTER_TYPE_ALL);
        }
    }

    private void a(TextView textView, int i, Drawable drawable, boolean z, boolean z2) {
        boolean a = a(z, z2);
        Drawable[] compoundDrawablesRelative = ek.bO() ? textView.getCompoundDrawablesRelative() : textView.getCompoundDrawables();
        Drawable a2 = a ? a(compoundDrawablesRelative[i], drawable) : drawable;
        Drawable drawable2 = i == 0 ? a2 : compoundDrawablesRelative[0];
        Drawable drawable3 = i == 1 ? a2 : compoundDrawablesRelative[1];
        Drawable drawable4 = i == 2 ? a2 : compoundDrawablesRelative[2];
        Drawable drawable5 = i == 3 ? a2 : compoundDrawablesRelative[3];
        if (ek.bO()) {
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable2, drawable3, drawable4, drawable5);
        } else {
            textView.setCompoundDrawablesWithIntrinsicBounds(drawable2, drawable3, drawable4, drawable5);
        }
        if (a) {
            ((cz) a2).startTransition(Constants.FILTER_TYPE_ALL);
        }
    }

    private boolean a(boolean z, boolean z2) {
        return this.kb && !z2 && (!z || this.kc);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(Context context, Bitmap bitmap, boolean z) {
        db.c(bitmap);
        a(new BitmapDrawable(context.getResources(), bitmap), z, false, true);
    }

    public void a(ImageView imageView) {
        db.c(imageView);
        this.jX = null;
        this.jY = new WeakReference<>(imageView);
        this.jZ = null;
        this.ka = -1;
        this.jV = 2;
        this.jW = imageView.hashCode();
    }

    public void a(ImageManager.OnImageLoadedListener onImageLoadedListener) {
        db.c(onImageLoadedListener);
        this.jX = new WeakReference<>(onImageLoadedListener);
        this.jY = null;
        this.jZ = null;
        this.ka = -1;
        this.jV = 1;
        this.jW = dl.hashCode(onImageLoadedListener, this.jS);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b(Context context, boolean z) {
        a(this.jU != 0 ? context.getResources().getDrawable(this.jU) : null, z, false, false);
    }

    public boolean equals(Object obj) {
        if (obj instanceof a) {
            return this == obj || ((a) obj).hashCode() == hashCode();
        }
        return false;
    }

    public int hashCode() {
        return this.jW;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void r(Context context) {
        a(this.jT != 0 ? context.getResources().getDrawable(this.jT) : null, false, true, false);
    }

    public void v(int i) {
        this.jU = i;
    }
}
