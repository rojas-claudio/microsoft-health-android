package com.shinobicontrols.charts;

import android.widget.TextView;
/* loaded from: classes.dex */
abstract class e {
    static e a = new e() { // from class: com.shinobicontrols.charts.e.1
        @Override // com.shinobicontrols.charts.e
        void a(Annotation annotation) {
            AnnotationStyle style = annotation.getStyle();
            if (style != null) {
                TextView textView = (TextView) annotation.getView();
                textView.setBackgroundColor(style.d.a.intValue());
                textView.setTextColor(style.a.a.intValue());
                textView.setTextSize(style.b.a.floatValue());
                textView.setTypeface(style.c.a);
            }
        }
    };
    static e b = new e() { // from class: com.shinobicontrols.charts.e.2
        @Override // com.shinobicontrols.charts.e
        void a(Annotation annotation) {
            AnnotationStyle style = annotation.getStyle();
            if (style != null) {
                annotation.getView().setBackgroundColor(style.d.a.intValue());
            }
        }
    };
    static e c = new e() { // from class: com.shinobicontrols.charts.e.3
        @Override // com.shinobicontrols.charts.e
        void a(Annotation annotation) {
        }
    };

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void a(Annotation annotation);

    e() {
    }
}
