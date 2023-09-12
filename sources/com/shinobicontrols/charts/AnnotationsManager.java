package com.shinobicontrols.charts;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.shinobicontrols.charts.Annotation;
import com.shinobicontrols.charts.d;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/* loaded from: classes.dex */
public class AnnotationsManager {
    private final v a;
    private final Map<Annotation, am> c = new HashMap();
    private final a d = new a();
    private final List<Annotation> b = new ArrayList();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class a implements d.a {
        private a() {
        }

        @Override // com.shinobicontrols.charts.d.a
        public void a(Annotation annotation) {
            AnnotationsManager.this.h(annotation);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AnnotationsManager(v chart) {
        this.a = chart;
    }

    public List<Annotation> getAnnotations() {
        return Collections.unmodifiableList(this.b);
    }

    public void removeAnnotation(Annotation annotation) {
        a(annotation);
    }

    public void removeAllAnnotations() {
        a(new ArrayList(this.b));
    }

    public void removeAllAnnotations(Axis<?, ?> axis) {
        ArrayList arrayList = new ArrayList();
        for (Annotation annotation : this.b) {
            if (annotation.getXAxis() == axis || annotation.getYAxis() == axis) {
                arrayList.add(annotation);
            }
        }
        a(arrayList);
    }

    private void a(Annotation annotation) {
        this.a.b.b(annotation.getView(), annotation.getPosition());
        this.b.remove(annotation);
        b(annotation);
    }

    private void a(List<Annotation> list) {
        for (Annotation annotation : list) {
            a(annotation);
        }
    }

    private void b(Annotation annotation) {
        am amVar = this.c.get(annotation);
        if (amVar != null) {
            amVar.a();
            this.c.remove(annotation);
        }
    }

    public Annotation addTextAnnotation(String text, Object xValue, Object yValue, Axis<?, ?> xAxis, Axis<?, ?> yAxis) {
        this.a.o();
        TextView textView = new TextView(this.a.getContext());
        textView.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
        int a2 = at.a(this.a.getResources().getDisplayMetrics().density, 6.0f);
        textView.setPadding(a2, a2, a2, a2);
        textView.setText(text);
        Annotation annotation = new Annotation(textView, xAxis, yAxis, e.a);
        annotation.setXValue(xValue);
        annotation.setYValue(yValue);
        c(annotation);
        return annotation;
    }

    public Annotation addViewAnnotation(View view, Object xValue, Object yValue, Axis<?, ?> xAxis, Axis<?, ?> yAxis) {
        this.a.o();
        if (view == null) {
            throw new IllegalArgumentException("Annotation cannot have a null View.");
        }
        if (view.getLayoutParams() == null) {
            view.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
        }
        Annotation annotation = new Annotation(view, xAxis, yAxis, e.c);
        annotation.setXValue(xValue);
        annotation.setYValue(yValue);
        c(annotation);
        return annotation;
    }

    public Annotation addHorizontalLineAnnotation(Object yValue, float thickness, Axis<?, ?> xAxis, Axis<?, ?> yAxis) {
        this.a.o();
        int a2 = at.a(this.a.getResources().getDisplayMetrics().density, thickness);
        View view = new View(this.a.getContext());
        view.setLayoutParams(new ViewGroup.LayoutParams(-1, a2));
        Annotation annotation = new Annotation(view, xAxis, yAxis, e.b);
        annotation.setYValue(yValue);
        c(annotation);
        return annotation;
    }

    public Annotation addVerticalLineAnnotation(Object xValue, float thickness, Axis<?, ?> xAxis, Axis<?, ?> yAxis) {
        this.a.o();
        int a2 = at.a(this.a.getResources().getDisplayMetrics().density, thickness);
        View view = new View(this.a.getContext());
        view.setLayoutParams(new ViewGroup.LayoutParams(a2, -1));
        Annotation annotation = new Annotation(view, xAxis, yAxis, e.b);
        annotation.setXValue(xValue);
        c(annotation);
        return annotation;
    }

    public Annotation addHorizontalBandAnnotation(Range<?> yRange, Axis<?, ?> xAxis, Axis<?, ?> yAxis) {
        this.a.o();
        View view = new View(this.a.getContext());
        view.setLayoutParams(new ViewGroup.LayoutParams(-1, 0));
        Annotation annotation = new Annotation(view, xAxis, yAxis, e.b);
        annotation.setYRange(yRange);
        c(annotation);
        return annotation;
    }

    public Annotation addVerticalBandAnnotation(Range<?> xRange, Axis<?, ?> xAxis, Axis<?, ?> yAxis) {
        this.a.o();
        View view = new View(this.a.getContext());
        view.setLayoutParams(new ViewGroup.LayoutParams(0, -1));
        Annotation annotation = new Annotation(view, xAxis, yAxis, e.b);
        annotation.setXRange(xRange);
        c(annotation);
        return annotation;
    }

    public Annotation addBoxAnnotation(Range<?> xRange, Range<?> yRange, Axis<?, ?> xAxis, Axis<?, ?> yAxis) {
        this.a.o();
        View view = new View(this.a.getContext());
        view.setLayoutParams(new ViewGroup.LayoutParams(0, 0));
        Annotation annotation = new Annotation(view, xAxis, yAxis, e.b);
        annotation.setXRange(xRange);
        annotation.setYRange(yRange);
        c(annotation);
        return annotation;
    }

    private AnnotationStyle c() {
        AnnotationStyle annotationStyle = new AnnotationStyle();
        annotationStyle.a(this.a.p().g());
        return annotationStyle;
    }

    private void c(Annotation annotation) {
        annotation.setStyle(c());
        annotation.b();
        d(annotation);
        this.b.add(annotation);
        this.a.b.a(annotation.getView(), annotation.getPosition());
    }

    private void d(Annotation annotation) {
        this.c.put(annotation, annotation.a(this.d));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(int i, int i2, Annotation.Position position) {
        int size = this.b.size();
        for (int i3 = 0; i3 < size; i3++) {
            Annotation annotation = this.b.get(i3);
            if (annotation.getPosition() == position) {
                ViewGroup.LayoutParams layoutParams = annotation.getView().getLayoutParams();
                annotation.a(ViewGroup.getChildMeasureSpec(i, 0, a(layoutParams, annotation)), ViewGroup.getChildMeasureSpec(i2, 0, b(layoutParams, annotation)));
            }
        }
    }

    private int a(ViewGroup.LayoutParams layoutParams, Annotation annotation) {
        return layoutParams.width == 0 ? e(annotation) : layoutParams.width;
    }

    private int b(ViewGroup.LayoutParams layoutParams, Annotation annotation) {
        return layoutParams.height == 0 ? f(annotation) : layoutParams.height;
    }

    private int e(Annotation annotation) {
        if (annotation.getXRange() != null) {
            double translatePoint = annotation.getXAxis().translatePoint(annotation.getXRange().getMinimum());
            double translatePoint2 = annotation.getXAxis().translatePoint(annotation.getXRange().getMaximum());
            return (int) Math.round(annotation.getXAxis().n.a(translatePoint2, this.a.b.g, this.a.b.h) - annotation.getXAxis().n.a(translatePoint, this.a.b.g, this.a.b.h));
        }
        return 0;
    }

    private int f(Annotation annotation) {
        if (annotation.getYRange() != null) {
            return (int) Math.round(annotation.getYAxis().n.a(annotation.getYAxis().translatePoint(annotation.getYRange().getMinimum()), this.a.b.g, this.a.b.h) - annotation.getYAxis().n.a(annotation.getYAxis().translatePoint(annotation.getYRange().getMaximum()), this.a.b.g, this.a.b.h));
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(int i, int i2, int i3, int i4, Annotation.Position position) {
        int size = this.b.size();
        for (int i5 = 0; i5 < size; i5++) {
            Annotation annotation = this.b.get(i5);
            if (annotation.getPosition() == position) {
                g(annotation);
                annotation.a(i, i2, i3, i4);
            }
        }
    }

    private void g(Annotation annotation) {
        if (annotation.getXAxis().b != this.a || annotation.getYAxis().b != this.a) {
            throw new IllegalStateException(annotation.getView().getContext().getString(R.string.AnnotationMustBeOnSameChart));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a() {
        if (this.b.size() > 0) {
            this.a.b.g();
            this.a.b.h();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void h(Annotation annotation) {
        this.a.b.b(annotation.getView(), annotation.getPosition() == Annotation.Position.IN_FRONT_OF_DATA ? Annotation.Position.BEHIND_DATA : Annotation.Position.IN_FRONT_OF_DATA);
        this.a.b.a(annotation.getView(), annotation.getPosition());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b() {
        int size = this.b.size();
        for (int i = 0; i < size; i++) {
            this.b.get(i).b();
        }
    }
}
