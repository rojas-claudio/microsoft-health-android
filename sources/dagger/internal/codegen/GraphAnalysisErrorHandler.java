package dagger.internal.codegen;

import dagger.internal.Linker;
import java.util.List;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
/* loaded from: classes.dex */
final class GraphAnalysisErrorHandler implements Linker.ErrorHandler {
    private final String moduleName;
    private final ProcessingEnvironment processingEnv;

    /* JADX INFO: Access modifiers changed from: package-private */
    public GraphAnalysisErrorHandler(ProcessingEnvironment processingEnv, String moduleName) {
        this.processingEnv = processingEnv;
        this.moduleName = moduleName;
    }

    @Override // dagger.internal.Linker.ErrorHandler
    public void handleErrors(List<String> errors) {
        TypeElement module = this.processingEnv.getElementUtils().getTypeElement(this.moduleName);
        for (String error : errors) {
            this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, error + " for " + this.moduleName, module);
        }
    }
}
