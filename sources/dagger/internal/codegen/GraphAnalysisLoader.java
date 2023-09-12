package dagger.internal.codegen;

import dagger.internal.Binding;
import dagger.internal.Loader;
import dagger.internal.ModuleAdapter;
import dagger.internal.StaticInjection;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import org.apache.commons.lang3.ClassUtils;
/* loaded from: classes.dex */
public final class GraphAnalysisLoader extends Loader {
    private final ProcessingEnvironment processingEnv;

    public GraphAnalysisLoader(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
    }

    @Override // dagger.internal.Loader
    public Binding<?> getAtInjectBinding(String key, String className, ClassLoader classLoader, boolean mustHaveInjections) {
        String sourceClassName = className.replace('$', ClassUtils.PACKAGE_SEPARATOR_CHAR);
        TypeElement type = this.processingEnv.getElementUtils().getTypeElement(sourceClassName);
        if (type == null || type.getKind() == ElementKind.INTERFACE) {
            return null;
        }
        return GraphAnalysisInjectBinding.create(type, mustHaveInjections);
    }

    @Override // dagger.internal.Loader
    public <T> ModuleAdapter<T> getModuleAdapter(Class<T> moduleClass) {
        throw new UnsupportedOperationException();
    }

    @Override // dagger.internal.Loader
    public StaticInjection getStaticInjection(Class<?> injectedClass) {
        throw new UnsupportedOperationException();
    }
}
