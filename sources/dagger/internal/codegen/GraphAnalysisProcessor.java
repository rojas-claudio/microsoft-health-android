package dagger.internal.codegen;

import dagger.Module;
import dagger.Provides;
import dagger.internal.Binding;
import dagger.internal.BindingsGroup;
import dagger.internal.Linker;
import dagger.internal.ProblemDetector;
import dagger.internal.ProvidesBinding;
import dagger.internal.SetBinding;
import dagger.internal.codegen.Util;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.inject.Singleton;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
@SupportedAnnotationTypes({"dagger.Module"})
/* loaded from: classes.dex */
public final class GraphAnalysisProcessor extends AbstractProcessor {
    private static final Set<String> ERROR_NAMES_TO_PROPAGATE = new LinkedHashSet(Arrays.asList("com.sun.tools.javac.code.Symbol$CompletionFailure"));
    private final Set<String> delayedModuleNames = new LinkedHashSet();

    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    public boolean process(Set<? extends TypeElement> types, RoundEnvironment env) {
        if (!env.processingOver()) {
            for (TypeElement typeElement : env.getElementsAnnotatedWith(Module.class)) {
                if (!(typeElement instanceof TypeElement)) {
                    error("@Module applies to a type, " + typeElement.getSimpleName() + " is a " + typeElement.getKind(), typeElement);
                } else {
                    this.delayedModuleNames.add(typeElement.getQualifiedName().toString());
                }
            }
            return false;
        }
        Set<Element> modules = new LinkedHashSet<>();
        for (String moduleName : this.delayedModuleNames) {
            modules.add(elements().getTypeElement(moduleName));
        }
        for (Element element : modules) {
            try {
                Map<String, Object> annotation = Util.getAnnotation(Module.class, element);
                TypeElement moduleType = (TypeElement) element;
                if (annotation == null) {
                    error("Missing @Module annotation.", moduleType);
                } else {
                    if (annotation.get("complete").equals(Boolean.TRUE)) {
                        try {
                            Map<String, Binding<?>> bindings = processCompleteModule(moduleType, false);
                            new ProblemDetector().detectCircularDependencies(bindings.values());
                            try {
                                writeDotFile(moduleType, bindings);
                            } catch (IOException e) {
                                StringWriter sw = new StringWriter();
                                e.printStackTrace(new PrintWriter(sw));
                                this.processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "Graph visualization failed. Please report this as a bug.\n\n" + sw, moduleType);
                            }
                        } catch (Binding.InvalidBindingException e2) {
                            error("Graph validation failed: " + e2.getMessage(), elements().getTypeElement(e2.type));
                        } catch (ModuleValidationException e3) {
                            error("Graph validation failed: " + e3.getMessage(), e3.source);
                        } catch (RuntimeException e4) {
                            if (ERROR_NAMES_TO_PROPAGATE.contains(e4.getClass().getName())) {
                                throw e4;
                            }
                            error("Unknown error " + e4.getClass().getName() + " thrown by javac in graph validation: " + e4.getMessage(), moduleType);
                        }
                    }
                    if (annotation.get("library").equals(Boolean.FALSE)) {
                        try {
                            new ProblemDetector().detectUnusedBinding(processCompleteModule(moduleType, true).values());
                        } catch (IllegalStateException e5) {
                            error("Graph validation failed: " + e5.getMessage(), moduleType);
                        }
                    }
                }
            } catch (Util.CodeGenerationIncompleteException e6) {
            }
        }
        return false;
    }

    private void error(String message, Element element) {
        this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, message, element);
    }

    private Map<String, Binding<?>> processCompleteModule(TypeElement rootModule, boolean ignoreCompletenessErrors) {
        Map<String, Binding<?>> linkAll;
        Map<String, TypeElement> allModules = new LinkedHashMap<>();
        collectIncludesRecursively(rootModule, allModules, new LinkedList());
        ArrayList<GraphAnalysisStaticInjection> staticInjections = new ArrayList<>();
        Linker.ErrorHandler errorHandler = ignoreCompletenessErrors ? Linker.ErrorHandler.NULL : new GraphAnalysisErrorHandler(this.processingEnv, rootModule.getQualifiedName().toString());
        Linker linker = new Linker(null, new GraphAnalysisLoader(this.processingEnv), errorHandler);
        synchronized (linker) {
            BindingsGroup baseBindings = new BindingsGroup() { // from class: dagger.internal.codegen.GraphAnalysisProcessor.1
                @Override // dagger.internal.BindingsGroup
                public Binding<?> contributeSetBinding(String key, SetBinding<?> value) {
                    return super.put(key, value);
                }
            };
            BindingsGroup overrideBindings = new BindingsGroup() { // from class: dagger.internal.codegen.GraphAnalysisProcessor.2
                @Override // dagger.internal.BindingsGroup
                public Binding<?> contributeSetBinding(String key, SetBinding<?> value) {
                    throw new IllegalStateException("Module overrides cannot contribute set bindings.");
                }
            };
            for (TypeElement module : allModules.values()) {
                Map<String, Object> annotation = Util.getAnnotation(Module.class, module);
                boolean overrides = ((Boolean) annotation.get("overrides")).booleanValue();
                boolean library = ((Boolean) annotation.get("library")).booleanValue();
                BindingsGroup addTo = overrides ? overrideBindings : baseBindings;
                Set<String> injectsProvisionKeys = new LinkedHashSet<>();
                Object[] arr$ = (Object[]) annotation.get("injects");
                for (Object injectableTypeObject : arr$) {
                    TypeMirror injectableType = (TypeMirror) injectableTypeObject;
                    String providerKey = GeneratorKeys.get(injectableType);
                    injectsProvisionKeys.add(providerKey);
                    linker.requestBinding(Util.isInterface(injectableType) ? providerKey : GeneratorKeys.rawMembersKey(injectableType), module.getQualifiedName().toString(), getClass().getClassLoader(), false, true);
                }
                Object[] arr$2 = (Object[]) annotation.get("staticInjections");
                for (Object staticInjection : arr$2) {
                    TypeMirror staticInjectionTypeMirror = (TypeMirror) staticInjection;
                    Element element = this.processingEnv.getTypeUtils().asElement(staticInjectionTypeMirror);
                    staticInjections.add(new GraphAnalysisStaticInjection(element));
                }
                for (ExecutableElement executableElement : module.getEnclosedElements()) {
                    Provides provides = (Provides) executableElement.getAnnotation(Provides.class);
                    if (provides != null) {
                        ExecutableElement providerMethod = executableElement;
                        String key = GeneratorKeys.get(providerMethod);
                        ProvidesBinding<?> binding = new ProviderMethodBinding(key, providerMethod, library);
                        Binding<?> previous = addTo.get(key);
                        if (previous != null && ((provides.type() != Provides.Type.SET && provides.type() != Provides.Type.SET_VALUES) || !(previous instanceof SetBinding))) {
                            String message = "Duplicate bindings for " + key;
                            if (overrides) {
                                message = message + " in override module(s) - cannot override an override";
                            }
                            error(message + ":\n    " + previous.requiredBy + "\n    " + binding.requiredBy, providerMethod);
                        }
                        switch (provides.type()) {
                            case UNIQUE:
                                if (injectsProvisionKeys.contains(binding.provideKey)) {
                                    binding.setDependedOn(true);
                                }
                                try {
                                    addTo.contributeProvidesBinding(key, binding);
                                    break;
                                } catch (IllegalStateException ise) {
                                    throw new ModuleValidationException(ise.getMessage(), providerMethod);
                                }
                            case SET:
                                String setKey = GeneratorKeys.getSetKey(providerMethod);
                                SetBinding.add(addTo, setKey, binding);
                                break;
                            case SET_VALUES:
                                SetBinding.add(addTo, key, binding);
                                break;
                            default:
                                throw new AssertionError("Unknown @Provides type " + provides.type());
                        }
                    }
                }
            }
            linker.installBindings(baseBindings);
            linker.installBindings(overrideBindings);
            Iterator i$ = staticInjections.iterator();
            while (i$.hasNext()) {
                GraphAnalysisStaticInjection staticInjection2 = i$.next();
                staticInjection2.attach(linker);
            }
            linkAll = linker.linkAll();
        }
        return linkAll;
    }

    private Elements elements() {
        return this.processingEnv.getElementUtils();
    }

    void collectIncludesRecursively(TypeElement module, Map<String, TypeElement> result, Deque<String> path) {
        Map<String, Object> annotation = Util.getAnnotation(Module.class, module);
        if (annotation == null) {
            throw new ModuleValidationException("No @Module on " + module, module);
        }
        String name = module.getQualifiedName().toString();
        if (path.contains(name)) {
            StringBuilder message = new StringBuilder("Module Inclusion Cycle: ");
            if (path.size() == 1) {
                message.append(name).append(" includes itself directly.");
            } else {
                String includer = name;
                int i = 0;
                while (path.size() > 0) {
                    String current = includer;
                    String includer2 = path.pop();
                    includer = includer2;
                    message.append("\n").append(i).append(". ").append(current).append(" included by ").append(includer);
                    i++;
                }
                message.append("\n0. ").append(name);
            }
            throw new ModuleValidationException(message.toString(), module);
        }
        result.put(name, module);
        Types types = this.processingEnv.getTypeUtils();
        List<Object> seedModules = new ArrayList<>();
        seedModules.addAll(Arrays.asList((Object[]) annotation.get("includes")));
        if (!annotation.get("addsTo").equals(Void.class)) {
            seedModules.add(annotation.get("addsTo"));
        }
        for (Object include : seedModules) {
            if (!(include instanceof TypeMirror)) {
                this.processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "Unexpected value for include: " + include + " in " + module, module);
            } else {
                TypeElement includedModule = (TypeElement) types.asElement((TypeMirror) include);
                path.push(name);
                collectIncludesRecursively(includedModule, result, path);
                path.pop();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class ProviderMethodBinding extends ProvidesBinding<Object> {
        private final ExecutableElement method;
        private final Binding<?>[] parameters;

        protected ProviderMethodBinding(String provideKey, ExecutableElement method, boolean library) {
            super(provideKey, method.getAnnotation(Singleton.class) != null, Util.className(method), method.getSimpleName().toString());
            this.method = method;
            this.parameters = new Binding[method.getParameters().size()];
            setLibrary(library);
        }

        @Override // dagger.internal.Binding
        public void attach(Linker linker) {
            for (int i = 0; i < this.method.getParameters().size(); i++) {
                VariableElement parameter = (VariableElement) this.method.getParameters().get(i);
                String parameterKey = GeneratorKeys.get(parameter);
                this.parameters[i] = linker.requestBinding(parameterKey, this.method.toString(), getClass().getClassLoader());
            }
        }

        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public Object get() {
            throw new AssertionError("Compile-time binding should never be called to inject.");
        }

        @Override // dagger.internal.Binding, dagger.MembersInjector
        public void injectMembers(Object t) {
            throw new AssertionError("Compile-time binding should never be called to inject.");
        }

        @Override // dagger.internal.Binding
        public void getDependencies(Set<Binding<?>> get, Set<Binding<?>> injectMembers) {
            Collections.addAll(get, this.parameters);
        }

        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding
        public String toString() {
            return "ProvidesBinding[key=" + this.provideKey + " method=" + this.moduleClass + "." + this.method.getSimpleName() + "()";
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    void writeDotFile(TypeElement module, Map<String, Binding<?>> bindings) throws IOException {
        StandardLocation standardLocation = StandardLocation.SOURCE_OUTPUT;
        String path = Util.getPackage(module).getQualifiedName().toString();
        String file = module.getQualifiedName().toString().substring(path.length() + 1) + ".dot";
        FileObject resource = this.processingEnv.getFiler().createResource(standardLocation, path, file, new Element[]{module});
        Writer writer = resource.openWriter();
        GraphVizWriter dotWriter = new GraphVizWriter(writer);
        new GraphVisualizer().write(bindings, dotWriter);
        dotWriter.close();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class ModuleValidationException extends IllegalStateException {
        final Element source;

        public ModuleValidationException(String message, Element source) {
            super(message);
            this.source = source;
        }
    }
}
