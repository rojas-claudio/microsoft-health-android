package dagger.internal.codegen;

import dagger.internal.Linker;
import dagger.internal.StaticInjection;
import javax.inject.Inject;
import javax.lang.model.element.Element;
/* loaded from: classes.dex */
public final class GraphAnalysisStaticInjection extends StaticInjection {
    private final Element enclosingClass;

    public GraphAnalysisStaticInjection(Element enclosingClass) {
        this.enclosingClass = enclosingClass;
    }

    @Override // dagger.internal.StaticInjection
    public void attach(Linker linker) {
        for (Element enclosedElement : this.enclosingClass.getEnclosedElements()) {
            if (enclosedElement.getKind().isField() && Util.isStatic(enclosedElement)) {
                Inject injectAnnotation = (Inject) enclosedElement.getAnnotation(Inject.class);
                if (injectAnnotation != null) {
                    String key = GeneratorKeys.get(enclosedElement.asType());
                    linker.requestBinding(key, this.enclosingClass.toString(), getClass().getClassLoader());
                }
            }
        }
    }

    @Override // dagger.internal.StaticInjection
    public void inject() {
        throw new UnsupportedOperationException();
    }
}
