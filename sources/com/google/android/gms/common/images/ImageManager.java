package com.google.android.gms.common.images;

import android.app.ActivityManager;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.os.ResultReceiver;
import android.util.Log;
import android.widget.ImageView;
import com.google.android.gms.common.images.a;
import com.google.android.gms.internal.db;
import com.google.android.gms.internal.dq;
import com.google.android.gms.internal.ek;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/* loaded from: classes.dex */
public final class ImageManager {
    private static final Object jC = new Object();
    private static HashSet<Uri> jD = new HashSet<>();
    private static ImageManager jE;
    private static ImageManager jF;
    private final b jH;
    private final Map<com.google.android.gms.common.images.a, ImageReceiver> jI;
    private final Map<Uri, ImageReceiver> jJ;
    private final Context mContext;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private final ExecutorService jG = Executors.newFixedThreadPool(4);

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public final class ImageReceiver extends ResultReceiver {
        private final ArrayList<com.google.android.gms.common.images.a> jK;
        boolean jL;
        private final Uri mUri;

        ImageReceiver(Uri uri) {
            super(new Handler(Looper.getMainLooper()));
            this.jL = false;
            this.mUri = uri;
            this.jK = new ArrayList<>();
        }

        public void aR() {
            Intent intent = new Intent("com.google.android.gms.common.images.LOAD_IMAGE");
            intent.putExtra("com.google.android.gms.extras.uri", this.mUri);
            intent.putExtra("com.google.android.gms.extras.resultReceiver", this);
            intent.putExtra("com.google.android.gms.extras.priority", 3);
            ImageManager.this.mContext.sendBroadcast(intent);
        }

        public void c(com.google.android.gms.common.images.a aVar) {
            db.a(!this.jL, "Cannot add an ImageRequest when mHandlingRequests is true");
            db.w("ImageReceiver.addImageRequest() must be called in the main thread");
            this.jK.add(aVar);
        }

        public void d(com.google.android.gms.common.images.a aVar) {
            db.a(!this.jL, "Cannot remove an ImageRequest when mHandlingRequests is true");
            db.w("ImageReceiver.removeImageRequest() must be called in the main thread");
            this.jK.remove(aVar);
        }

        @Override // android.os.ResultReceiver
        public void onReceiveResult(int resultCode, Bundle resultData) {
            ImageManager.this.jG.execute(new c(this.mUri, (ParcelFileDescriptor) resultData.getParcelable("com.google.android.gms.extra.fileDescriptor")));
        }
    }

    /* loaded from: classes.dex */
    public interface OnImageLoadedListener {
        void onImageLoaded(Uri uri, Drawable drawable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class a {
        static int a(ActivityManager activityManager) {
            return activityManager.getLargeMemoryClass();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class b extends dq<a.C0001a, Bitmap> {
        public b(Context context) {
            super(q(context));
        }

        private static int q(Context context) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
            return (int) (((((context.getApplicationInfo().flags & 1048576) != 0) && ek.bJ()) ? a.a(activityManager) : activityManager.getMemoryClass()) * 1048576 * 0.33f);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.android.gms.internal.dq
        /* renamed from: a */
        public int sizeOf(a.C0001a c0001a, Bitmap bitmap) {
            return bitmap.getHeight() * bitmap.getRowBytes();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.android.gms.internal.dq
        /* renamed from: a */
        public void entryRemoved(boolean z, a.C0001a c0001a, Bitmap bitmap, Bitmap bitmap2) {
            super.entryRemoved(z, c0001a, bitmap, bitmap2);
        }
    }

    /* loaded from: classes.dex */
    private final class c implements Runnable {
        private final ParcelFileDescriptor jN;
        private final Uri mUri;

        public c(Uri uri, ParcelFileDescriptor parcelFileDescriptor) {
            this.mUri = uri;
            this.jN = parcelFileDescriptor;
        }

        @Override // java.lang.Runnable
        public void run() {
            db.x("LoadBitmapFromDiskRunnable can't be executed in the main thread");
            boolean z = false;
            Bitmap bitmap = null;
            if (this.jN != null) {
                try {
                    bitmap = BitmapFactory.decodeFileDescriptor(this.jN.getFileDescriptor());
                } catch (OutOfMemoryError e) {
                    Log.e("ImageManager", "OOM while loading bitmap for uri: " + this.mUri, e);
                    z = true;
                }
                try {
                    this.jN.close();
                } catch (IOException e2) {
                    Log.e("ImageManager", "closed failed", e2);
                }
            }
            CountDownLatch countDownLatch = new CountDownLatch(1);
            ImageManager.this.mHandler.post(new f(this.mUri, bitmap, z, countDownLatch));
            try {
                countDownLatch.await();
            } catch (InterruptedException e3) {
                Log.w("ImageManager", "Latch interrupted while posting " + this.mUri);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public final class d implements Runnable {
        private final com.google.android.gms.common.images.a jO;

        public d(com.google.android.gms.common.images.a aVar) {
            this.jO = aVar;
        }

        @Override // java.lang.Runnable
        public void run() {
            db.w("LoadImageRunnable must be executed on the main thread");
            ImageManager.this.b(this.jO);
            a.C0001a c0001a = this.jO.jS;
            if (c0001a.uri == null) {
                this.jO.b(ImageManager.this.mContext, true);
                return;
            }
            Bitmap a = ImageManager.this.a(c0001a);
            if (a != null) {
                this.jO.a(ImageManager.this.mContext, a, true);
                return;
            }
            this.jO.r(ImageManager.this.mContext);
            ImageReceiver imageReceiver = (ImageReceiver) ImageManager.this.jJ.get(c0001a.uri);
            if (imageReceiver == null) {
                imageReceiver = new ImageReceiver(c0001a.uri);
                ImageManager.this.jJ.put(c0001a.uri, imageReceiver);
            }
            imageReceiver.c(this.jO);
            if (this.jO.jV != 1) {
                ImageManager.this.jI.put(this.jO, imageReceiver);
            }
            synchronized (ImageManager.jC) {
                if (!ImageManager.jD.contains(c0001a.uri)) {
                    ImageManager.jD.add(c0001a.uri);
                    imageReceiver.aR();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class e implements ComponentCallbacks2 {
        private final b jH;

        public e(b bVar) {
            this.jH = bVar;
        }

        @Override // android.content.ComponentCallbacks
        public void onConfigurationChanged(Configuration newConfig) {
        }

        @Override // android.content.ComponentCallbacks
        public void onLowMemory() {
            this.jH.evictAll();
        }

        @Override // android.content.ComponentCallbacks2
        public void onTrimMemory(int level) {
            if (level >= 60) {
                this.jH.evictAll();
            } else if (level >= 20) {
                this.jH.trimToSize(this.jH.size() / 2);
            }
        }
    }

    /* loaded from: classes.dex */
    private final class f implements Runnable {
        private final Bitmap jP;
        private final CountDownLatch jQ;
        private boolean jR;
        private final Uri mUri;

        public f(Uri uri, Bitmap bitmap, boolean z, CountDownLatch countDownLatch) {
            this.mUri = uri;
            this.jP = bitmap;
            this.jR = z;
            this.jQ = countDownLatch;
        }

        private void a(ImageReceiver imageReceiver, boolean z) {
            imageReceiver.jL = true;
            ArrayList arrayList = imageReceiver.jK;
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                com.google.android.gms.common.images.a aVar = (com.google.android.gms.common.images.a) arrayList.get(i);
                if (z) {
                    aVar.a(ImageManager.this.mContext, this.jP, false);
                } else {
                    aVar.b(ImageManager.this.mContext, false);
                }
                if (aVar.jV != 1) {
                    ImageManager.this.jI.remove(aVar);
                }
            }
            imageReceiver.jL = false;
        }

        @Override // java.lang.Runnable
        public void run() {
            db.w("OnBitmapLoadedRunnable must be executed in the main thread");
            boolean z = this.jP != null;
            if (ImageManager.this.jH != null) {
                if (this.jR) {
                    ImageManager.this.jH.evictAll();
                    System.gc();
                    this.jR = false;
                    ImageManager.this.mHandler.post(this);
                    return;
                } else if (z) {
                    ImageManager.this.jH.put(new a.C0001a(this.mUri), this.jP);
                }
            }
            ImageReceiver imageReceiver = (ImageReceiver) ImageManager.this.jJ.remove(this.mUri);
            if (imageReceiver != null) {
                a(imageReceiver, z);
            }
            this.jQ.countDown();
            synchronized (ImageManager.jC) {
                ImageManager.jD.remove(this.mUri);
            }
        }
    }

    private ImageManager(Context context, boolean withMemoryCache) {
        this.mContext = context.getApplicationContext();
        if (withMemoryCache) {
            this.jH = new b(this.mContext);
            if (ek.bM()) {
                aO();
            }
        } else {
            this.jH = null;
        }
        this.jI = new HashMap();
        this.jJ = new HashMap();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Bitmap a(a.C0001a c0001a) {
        if (this.jH == null) {
            return null;
        }
        return this.jH.get(c0001a);
    }

    public static ImageManager a(Context context, boolean z) {
        if (z) {
            if (jF == null) {
                jF = new ImageManager(context, true);
            }
            return jF;
        }
        if (jE == null) {
            jE = new ImageManager(context, false);
        }
        return jE;
    }

    private void aO() {
        this.mContext.registerComponentCallbacks(new e(this.jH));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean b(com.google.android.gms.common.images.a aVar) {
        ImageReceiver imageReceiver;
        db.w("ImageManager.cleanupHashMaps() must be called in the main thread");
        if (aVar.jV != 1 && (imageReceiver = this.jI.get(aVar)) != null) {
            if (imageReceiver.jL) {
                return false;
            }
            this.jI.remove(aVar);
            imageReceiver.d(aVar);
            return true;
        }
        return true;
    }

    public static ImageManager create(Context context) {
        return a(context, false);
    }

    public void a(com.google.android.gms.common.images.a aVar) {
        db.w("ImageManager.loadImage() must be called in the main thread");
        boolean b2 = b(aVar);
        d dVar = new d(aVar);
        if (b2) {
            dVar.run();
        } else {
            this.mHandler.post(dVar);
        }
    }

    public void loadImage(ImageView imageView, int resId) {
        com.google.android.gms.common.images.a aVar = new com.google.android.gms.common.images.a(resId);
        aVar.a(imageView);
        a(aVar);
    }

    public void loadImage(ImageView imageView, Uri uri) {
        com.google.android.gms.common.images.a aVar = new com.google.android.gms.common.images.a(uri);
        aVar.a(imageView);
        a(aVar);
    }

    public void loadImage(ImageView imageView, Uri uri, int defaultResId) {
        com.google.android.gms.common.images.a aVar = new com.google.android.gms.common.images.a(uri);
        aVar.v(defaultResId);
        aVar.a(imageView);
        a(aVar);
    }

    public void loadImage(OnImageLoadedListener listener, Uri uri) {
        com.google.android.gms.common.images.a aVar = new com.google.android.gms.common.images.a(uri);
        aVar.a(listener);
        a(aVar);
    }

    public void loadImage(OnImageLoadedListener listener, Uri uri, int defaultResId) {
        com.google.android.gms.common.images.a aVar = new com.google.android.gms.common.images.a(uri);
        aVar.v(defaultResId);
        aVar.a(listener);
        a(aVar);
    }
}
