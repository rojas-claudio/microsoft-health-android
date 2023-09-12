package com.jeremyfeinstein.slidingmenu.lib;

import android.graphics.Canvas;
import android.view.animation.Interpolator;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
/* loaded from: classes.dex */
public class CanvasTransformerBuilder {
    private static Interpolator lin = new Interpolator() { // from class: com.jeremyfeinstein.slidingmenu.lib.CanvasTransformerBuilder.1
        @Override // android.animation.TimeInterpolator
        public float getInterpolation(float t) {
            return t;
        }
    };
    private SlidingMenu.CanvasTransformer mTrans;

    private void initTransformer() {
        if (this.mTrans == null) {
            this.mTrans = new SlidingMenu.CanvasTransformer() { // from class: com.jeremyfeinstein.slidingmenu.lib.CanvasTransformerBuilder.2
                @Override // com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.CanvasTransformer
                public void transformCanvas(Canvas canvas, float percentOpen) {
                }
            };
        }
    }

    public SlidingMenu.CanvasTransformer zoom(int openedX, int closedX, int openedY, int closedY, int px, int py) {
        return zoom(openedX, closedX, openedY, closedY, px, py, lin);
    }

    public SlidingMenu.CanvasTransformer zoom(final int openedX, final int closedX, final int openedY, final int closedY, final int px, final int py, final Interpolator interp) {
        initTransformer();
        this.mTrans = new SlidingMenu.CanvasTransformer() { // from class: com.jeremyfeinstein.slidingmenu.lib.CanvasTransformerBuilder.3
            @Override // com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.CanvasTransformer
            public void transformCanvas(Canvas canvas, float percentOpen) {
                CanvasTransformerBuilder.this.mTrans.transformCanvas(canvas, percentOpen);
                float f = interp.getInterpolation(percentOpen);
                canvas.scale(((openedX - closedX) * f) + closedX, ((openedY - closedY) * f) + closedY, px, py);
            }
        };
        return this.mTrans;
    }

    public SlidingMenu.CanvasTransformer rotate(int openedDeg, int closedDeg, int px, int py) {
        return rotate(openedDeg, closedDeg, px, py, lin);
    }

    public SlidingMenu.CanvasTransformer rotate(final int openedDeg, final int closedDeg, final int px, final int py, final Interpolator interp) {
        initTransformer();
        this.mTrans = new SlidingMenu.CanvasTransformer() { // from class: com.jeremyfeinstein.slidingmenu.lib.CanvasTransformerBuilder.4
            @Override // com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.CanvasTransformer
            public void transformCanvas(Canvas canvas, float percentOpen) {
                CanvasTransformerBuilder.this.mTrans.transformCanvas(canvas, percentOpen);
                float f = interp.getInterpolation(percentOpen);
                canvas.rotate(((openedDeg - closedDeg) * f) + closedDeg, px, py);
            }
        };
        return this.mTrans;
    }

    public SlidingMenu.CanvasTransformer translate(int openedX, int closedX, int openedY, int closedY) {
        return translate(openedX, closedX, openedY, closedY, lin);
    }

    public SlidingMenu.CanvasTransformer translate(final int openedX, final int closedX, final int openedY, final int closedY, final Interpolator interp) {
        initTransformer();
        this.mTrans = new SlidingMenu.CanvasTransformer() { // from class: com.jeremyfeinstein.slidingmenu.lib.CanvasTransformerBuilder.5
            @Override // com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.CanvasTransformer
            public void transformCanvas(Canvas canvas, float percentOpen) {
                CanvasTransformerBuilder.this.mTrans.transformCanvas(canvas, percentOpen);
                float f = interp.getInterpolation(percentOpen);
                canvas.translate(((openedX - closedX) * f) + closedX, ((openedY - closedY) * f) + closedY);
            }
        };
        return this.mTrans;
    }

    public SlidingMenu.CanvasTransformer concatTransformer(final SlidingMenu.CanvasTransformer t) {
        initTransformer();
        this.mTrans = new SlidingMenu.CanvasTransformer() { // from class: com.jeremyfeinstein.slidingmenu.lib.CanvasTransformerBuilder.6
            @Override // com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.CanvasTransformer
            public void transformCanvas(Canvas canvas, float percentOpen) {
                CanvasTransformerBuilder.this.mTrans.transformCanvas(canvas, percentOpen);
                t.transformCanvas(canvas, percentOpen);
            }
        };
        return this.mTrans;
    }
}
