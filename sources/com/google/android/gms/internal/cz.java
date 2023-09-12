package com.google.android.gms.internal;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
/* loaded from: classes.dex */
public final class cz extends Drawable implements Drawable.Callback {
    private boolean kb;
    private int kd;
    private long ke;
    private int kf;
    private int kg;
    private int kh;
    private int ki;
    private int kj;
    private boolean kk;
    private b kl;
    private Drawable km;
    private Drawable kn;
    private boolean ko;
    private boolean kp;
    private boolean kq;
    private int kr;

    /* loaded from: classes.dex */
    private static final class a extends Drawable {
        private static final a ks = new a();
        private static final C0018a kt = new C0018a();

        /* renamed from: com.google.android.gms.internal.cz$a$a  reason: collision with other inner class name */
        /* loaded from: classes.dex */
        private static final class C0018a extends Drawable.ConstantState {
            private C0018a() {
            }

            @Override // android.graphics.drawable.Drawable.ConstantState
            public int getChangingConfigurations() {
                return 0;
            }

            @Override // android.graphics.drawable.Drawable.ConstantState
            public Drawable newDrawable() {
                return a.ks;
            }
        }

        private a() {
        }

        @Override // android.graphics.drawable.Drawable
        public void draw(Canvas canvas) {
        }

        @Override // android.graphics.drawable.Drawable
        public Drawable.ConstantState getConstantState() {
            return kt;
        }

        @Override // android.graphics.drawable.Drawable
        public int getOpacity() {
            return -2;
        }

        @Override // android.graphics.drawable.Drawable
        public void setAlpha(int alpha) {
        }

        @Override // android.graphics.drawable.Drawable
        public void setColorFilter(ColorFilter cf) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class b extends Drawable.ConstantState {
        int ku;
        int kv;

        b(b bVar) {
            if (bVar != null) {
                this.ku = bVar.ku;
                this.kv = bVar.kv;
            }
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public int getChangingConfigurations() {
            return this.ku;
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable() {
            return new cz(this);
        }
    }

    public cz(Drawable drawable, Drawable drawable2) {
        this(null);
        drawable = drawable == null ? a.ks : drawable;
        this.km = drawable;
        drawable.setCallback(this);
        this.kl.kv |= drawable.getChangingConfigurations();
        drawable2 = drawable2 == null ? a.ks : drawable2;
        this.kn = drawable2;
        drawable2.setCallback(this);
        this.kl.kv |= drawable2.getChangingConfigurations();
    }

    cz(b bVar) {
        this.kd = 0;
        this.kh = 255;
        this.kj = 0;
        this.kb = true;
        this.kl = new b(bVar);
    }

    public Drawable aS() {
        return this.kn;
    }

    public boolean canConstantState() {
        if (!this.ko) {
            this.kp = (this.km.getConstantState() == null || this.kn.getConstantState() == null) ? false : true;
            this.ko = true;
        }
        return this.kp;
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        boolean z = false;
        switch (this.kd) {
            case 1:
                this.ke = SystemClock.uptimeMillis();
                this.kd = 2;
                break;
            case 2:
                if (this.ke >= 0) {
                    float uptimeMillis = ((float) (SystemClock.uptimeMillis() - this.ke)) / this.ki;
                    r1 = uptimeMillis >= 1.0f;
                    if (r1) {
                        this.kd = 0;
                    }
                    this.kj = (int) ((Math.min(uptimeMillis, 1.0f) * (this.kg - this.kf)) + this.kf);
                }
            default:
                z = r1;
                break;
        }
        int i = this.kj;
        boolean z2 = this.kb;
        Drawable drawable = this.km;
        Drawable drawable2 = this.kn;
        if (z) {
            if (!z2 || i == 0) {
                drawable.draw(canvas);
            }
            if (i == this.kh) {
                drawable2.setAlpha(this.kh);
                drawable2.draw(canvas);
                return;
            }
            return;
        }
        if (z2) {
            drawable.setAlpha(this.kh - i);
        }
        drawable.draw(canvas);
        if (z2) {
            drawable.setAlpha(this.kh);
        }
        if (i > 0) {
            drawable2.setAlpha(i);
            drawable2.draw(canvas);
            drawable2.setAlpha(this.kh);
        }
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public int getChangingConfigurations() {
        return super.getChangingConfigurations() | this.kl.ku | this.kl.kv;
    }

    @Override // android.graphics.drawable.Drawable
    public Drawable.ConstantState getConstantState() {
        if (canConstantState()) {
            this.kl.ku = getChangingConfigurations();
            return this.kl;
        }
        return null;
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        return Math.max(this.km.getIntrinsicHeight(), this.kn.getIntrinsicHeight());
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        return Math.max(this.km.getIntrinsicWidth(), this.kn.getIntrinsicWidth());
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        if (!this.kq) {
            this.kr = Drawable.resolveOpacity(this.km.getOpacity(), this.kn.getOpacity());
            this.kq = true;
        }
        return this.kr;
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void invalidateDrawable(Drawable who) {
        Drawable.Callback callback;
        if (!ek.bJ() || (callback = getCallback()) == null) {
            return;
        }
        callback.invalidateDrawable(this);
    }

    @Override // android.graphics.drawable.Drawable
    public Drawable mutate() {
        if (!this.kk && super.mutate() == this) {
            if (!canConstantState()) {
                throw new IllegalStateException("One or more children of this LayerDrawable does not have constant state; this drawable cannot be mutated.");
            }
            this.km.mutate();
            this.kn.mutate();
            this.kk = true;
        }
        return this;
    }

    @Override // android.graphics.drawable.Drawable
    protected void onBoundsChange(Rect bounds) {
        this.km.setBounds(bounds);
        this.kn.setBounds(bounds);
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void scheduleDrawable(Drawable who, Runnable what, long when) {
        Drawable.Callback callback;
        if (!ek.bJ() || (callback = getCallback()) == null) {
            return;
        }
        callback.scheduleDrawable(this, what, when);
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int alpha) {
        if (this.kj == this.kh) {
            this.kj = alpha;
        }
        this.kh = alpha;
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter cf) {
        this.km.setColorFilter(cf);
        this.kn.setColorFilter(cf);
    }

    public void startTransition(int durationMillis) {
        this.kf = 0;
        this.kg = this.kh;
        this.kj = 0;
        this.ki = durationMillis;
        this.kd = 1;
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void unscheduleDrawable(Drawable who, Runnable what) {
        Drawable.Callback callback;
        if (!ek.bJ() || (callback = getCallback()) == null) {
            return;
        }
        callback.unscheduleDrawable(this, what);
    }
}
