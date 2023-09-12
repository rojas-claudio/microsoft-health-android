package com.shinobicontrols.charts;

import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
class c extends Animation {
    private final List<Animation> a = new ArrayList();
    private Animation[] b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(Animation animation) {
        this.a.add(animation);
    }

    @Override // com.shinobicontrols.charts.Animation
    public float getDuration() {
        float f = 0.0f;
        int size = this.a.size();
        a(size);
        Animation[] animationArr = (Animation[]) this.a.toArray(this.b);
        int i = 0;
        while (i < size) {
            float duration = animationArr[i].getDuration();
            if (duration <= f) {
                duration = f;
            }
            i++;
            f = duration;
        }
        return f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Animation
    public void a(float f) {
        int size = this.a.size();
        a(size);
        Animation[] animationArr = (Animation[]) this.a.toArray(this.b);
        for (int i = 0; i < size; i++) {
            animationArr[i].a(f);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Animation
    public boolean c() {
        int size = this.a.size();
        a(size);
        Animation[] animationArr = (Animation[]) this.a.toArray(this.b);
        boolean z = true;
        for (int i = 0; i < size; i++) {
            z = z && animationArr[i].c();
        }
        return z;
    }

    void a(int i) {
        if (this.b == null || this.b.length != i) {
            this.b = new Animation[i];
        }
    }
}
