package com.google.android.gms.internal;

import android.content.Context;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.VideoView;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes.dex */
public final class bj extends FrameLayout implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener {
    private final cq fG;
    private final MediaController gb;
    private final a gc;
    private final VideoView gd;
    private long ge;
    private String gf;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class a {
        private final Runnable el;
        private volatile boolean gg = false;

        public a(final bj bjVar) {
            this.el = new Runnable() { // from class: com.google.android.gms.internal.bj.a.1
                private final WeakReference<bj> gh;

                {
                    this.gh = new WeakReference<>(bjVar);
                }

                @Override // java.lang.Runnable
                public void run() {
                    bj bjVar2 = this.gh.get();
                    if (a.this.gg || bjVar2 == null) {
                        return;
                    }
                    bjVar2.aa();
                    a.this.ab();
                }
            };
        }

        public void ab() {
            cm.hO.postDelayed(this.el, 250L);
        }

        public void cancel() {
            this.gg = true;
            cm.hO.removeCallbacks(this.el);
        }
    }

    public bj(Context context, cq cqVar) {
        super(context);
        this.fG = cqVar;
        this.gd = new VideoView(context);
        addView(this.gd, new FrameLayout.LayoutParams(-1, -1, 17));
        this.gb = new MediaController(context);
        this.gc = new a(this);
        this.gc.ab();
        this.gd.setOnCompletionListener(this);
        this.gd.setOnPreparedListener(this);
        this.gd.setOnErrorListener(this);
    }

    private static void a(cq cqVar, String str) {
        a(cqVar, str, new HashMap(1));
    }

    public static void a(cq cqVar, String str, String str2) {
        boolean z = str2 == null;
        HashMap hashMap = new HashMap(z ? 2 : 3);
        hashMap.put("what", str);
        if (!z) {
            hashMap.put("extra", str2);
        }
        a(cqVar, "error", hashMap);
    }

    private static void a(cq cqVar, String str, String str2, String str3) {
        HashMap hashMap = new HashMap(2);
        hashMap.put(str2, str3);
        a(cqVar, str, hashMap);
    }

    private static void a(cq cqVar, String str, Map<String, String> map) {
        map.put("event", str);
        cqVar.a("onVideoEvent", map);
    }

    public void Z() {
        if (TextUtils.isEmpty(this.gf)) {
            a(this.fG, "no_src", (String) null);
        } else {
            this.gd.setVideoPath(this.gf);
        }
    }

    public void aa() {
        long currentPosition = this.gd.getCurrentPosition();
        if (this.ge != currentPosition) {
            a(this.fG, "timeupdate", "time", String.valueOf(((float) currentPosition) / 1000.0f));
            this.ge = currentPosition;
        }
    }

    public void b(MotionEvent motionEvent) {
        this.gd.dispatchTouchEvent(motionEvent);
    }

    public void destroy() {
        this.gc.cancel();
        this.gd.stopPlayback();
    }

    public void f(boolean z) {
        if (z) {
            this.gd.setMediaController(this.gb);
            return;
        }
        this.gb.hide();
        this.gd.setMediaController(null);
    }

    public void i(String str) {
        this.gf = str;
    }

    @Override // android.media.MediaPlayer.OnCompletionListener
    public void onCompletion(MediaPlayer mediaPlayer) {
        a(this.fG, "ended");
    }

    @Override // android.media.MediaPlayer.OnErrorListener
    public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
        a(this.fG, String.valueOf(what), String.valueOf(extra));
        return true;
    }

    @Override // android.media.MediaPlayer.OnPreparedListener
    public void onPrepared(MediaPlayer mediaPlayer) {
        a(this.fG, "canplaythrough", "duration", String.valueOf(this.gd.getDuration() / 1000.0f));
    }

    public void pause() {
        this.gd.pause();
    }

    public void play() {
        this.gd.start();
    }

    public void seekTo(int timeInMilliseconds) {
        this.gd.seekTo(timeInMilliseconds);
    }
}
