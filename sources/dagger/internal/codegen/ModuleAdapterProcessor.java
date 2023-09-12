package dagger.internal.codegen;

import com.squareup.javawriter.JavaWriter;
import dagger.Lazy;
import dagger.Module;
import dagger.Provides;
import dagger.internal.Binding;
import dagger.internal.BindingsGroup;
import dagger.internal.Linker;
import dagger.internal.ModuleAdapter;
import dagger.internal.ProvidesBinding;
import dagger.internal.SetBinding;
import dagger.internal.codegen.Util;
import dagger.internal.loaders.GeneratedAdapters;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
@SupportedAnnotationTypes({"*"})
/* loaded from: classes.dex */
public final class ModuleAdapterProcessor extends AbstractProcessor {
    private static final String BINDINGS_MAP = JavaWriter.type(BindingsGroup.class, new String[0]);
    private static final List<String> INVALID_RETURN_TYPES = Arrays.asList(Provider.class.getCanonicalName(), Lazy.class.getCanonicalName());
    private final LinkedHashMap<String, List<ExecutableElement>> remainingTypes = new LinkedHashMap<>();

    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    public boolean process(Set<? extends TypeElement> types, RoundEnvironment env) {
        this.remainingTypes.putAll(providerMethodsByClass(env));
        Iterator<String> i = this.remainingTypes.keySet().iterator();
        while (i.hasNext()) {
            String typeName = i.next();
            Element typeElement = this.processingEnv.getElementUtils().getTypeElement(typeName);
            List<ExecutableElement> providesTypes = this.remainingTypes.get(typeName);
            try {
                Map<String, Object> parsedAnnotation = Util.getAnnotation(Module.class, typeElement);
                StringWriter stringWriter = new StringWriter();
                String adapterName = Util.adapterName(typeElement, GeneratedAdapters.MODULE_ADAPTER_SUFFIX);
                generateModuleAdapter(stringWriter, adapterName, typeElement, parsedAnnotation, providesTypes);
                JavaFileObject sourceFile = this.processingEnv.getFiler().createSourceFile(adapterName, new Element[]{typeElement});
                Writer sourceWriter = sourceFile.openWriter();
                sourceWriter.append((CharSequence) stringWriter.getBuffer());
                sourceWriter.close();
            } catch (Util.CodeGenerationIncompleteException e) {
            } catch (IOException e2) {
                error("Code gen failed: " + e2, typeElement);
            }
            i.remove();
        }
        if (env.processingOver() && this.remainingTypes.size() > 0) {
            this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Could not find types required by provides methods for " + this.remainingTypes.keySet());
            return false;
        }
        return false;
    }

    private void error(String msg, Element element) {
        this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, msg, element);
    }

    private Map<String, List<ExecutableElement>> providerMethodsByClass(RoundEnvironment env) {
        Elements elementUtils = this.processingEnv.getElementUtils();
        Types types = this.processingEnv.getTypeUtils();
        Map<String, List<ExecutableElement>> result = new HashMap<>();
        Iterator<? extends Element> it = findProvidesMethods(env).iterator();
        while (it.hasNext()) {
            ExecutableElement executableElement = (Element) it.next();
            switch (AnonymousClass1.$SwitchMap$javax$lang$model$element$ElementKind[executableElement.getEnclosingElement().getKind().ordinal()]) {
                case 1:
                    TypeElement type = executableElement.getEnclosingElement();
                    Set<Modifier> typeModifiers = type.getModifiers();
                    if (typeModifiers.contains(Modifier.PRIVATE) || typeModifiers.contains(Modifier.ABSTRACT)) {
                        error("Classes declaring @Provides methods must not be private or abstract: " + type.getQualifiedName(), type);
                        break;
                    } else {
                        Set<Modifier> methodModifiers = executableElement.getModifiers();
                        if (methodModifiers.contains(Modifier.PRIVATE) || methodModifiers.contains(Modifier.ABSTRACT) || methodModifiers.contains(Modifier.STATIC)) {
                            error("@Provides methods must not be private, abstract or static: " + type.getQualifiedName() + "." + executableElement, executableElement);
                            break;
                        } else {
                            ExecutableElement providerMethodAsExecutable = executableElement;
                            if (!providerMethodAsExecutable.getThrownTypes().isEmpty()) {
                                error("@Provides methods must not have a throws clause: " + type.getQualifiedName() + "." + executableElement, executableElement);
                                break;
                            } else {
                                TypeMirror returnType = types.erasure(providerMethodAsExecutable.getReturnType());
                                if (!returnType.getKind().equals(TypeKind.ERROR)) {
                                    for (String invalidTypeName : INVALID_RETURN_TYPES) {
                                        TypeElement invalidTypeElement = elementUtils.getTypeElement(invalidTypeName);
                                        if (invalidTypeElement != null && types.isSameType(returnType, types.erasure(invalidTypeElement.asType()))) {
                                            error(String.format("@Provides method must not return %s directly: %s.%s", invalidTypeElement, type.getQualifiedName(), executableElement), executableElement);
                                            break;
                                        }
                                    }
                                }
                                List<ExecutableElement> methods = result.get(type.getQualifiedName().toString());
                                if (methods == null) {
                                    methods = new ArrayList<>();
                                    result.put(type.getQualifiedName().toString(), methods);
                                }
                                methods.add(providerMethodAsExecutable);
                                break;
                            }
                        }
                    }
                    break;
                default:
                    error("Unexpected @Provides on " + Util.elementToString(executableElement), executableElement);
                    break;
            }
        }
        TypeMirror objectType = elementUtils.getTypeElement("java.lang.Object").asType();
        for (TypeElement typeElement : env.getElementsAnnotatedWith(Module.class)) {
            if (!typeElement.getKind().equals(ElementKind.CLASS)) {
                error("Modules must be classes: " + Util.elementToString(typeElement), typeElement);
            } else {
                TypeElement moduleType = typeElement;
                if (!moduleType.getSuperclass().equals(objectType)) {
                    error("Modules must not extend from other classes: " + Util.elementToString(typeElement), typeElement);
                }
                String moduleName = moduleType.getQualifiedName().toString();
                if (!result.containsKey(moduleName)) {
                    result.put(moduleName, new ArrayList<>());
                }
            }
        }
        return result;
    }

    private Set<? extends Element> findProvidesMethods(RoundEnvironment env) {
        Set<Element> result = new LinkedHashSet<>();
        result.addAll(env.getElementsAnnotatedWith(Provides.class));
        return result;
    }

    private void generateModuleAdapter(Writer ioWriter, String adapterName, TypeElement type, Map<String, Object> module, List<ExecutableElement> providerMethods) throws IOException {
        if (module == null) {
            error(type + " has @Provides methods but no @Module annotation", type);
            return;
        }
        Object[] staticInjections = (Object[]) module.get("staticInjections");
        Object[] injects = (Object[]) module.get("injects");
        Object[] includes = (Object[]) module.get("includes");
        boolean overrides = ((Boolean) module.get("overrides")).booleanValue();
        boolean complete = ((Boolean) module.get("complete")).booleanValue();
        boolean library = ((Boolean) module.get("library")).booleanValue();
        JavaWriter writer = new JavaWriter(ioWriter);
        boolean multibindings = checkForMultibindings(providerMethods);
        boolean providerMethodDependencies = checkForDependencies(providerMethods);
        writer.emitSingleLineComment("Code generated by dagger-compiler.  Do not edit.", new Object[0]);
        writer.emitPackage(Util.getPackage(type).getQualifiedName().toString());
        writer.emitImports(findImports(multibindings, !providerMethods.isEmpty(), providerMethodDependencies));
        String typeName = type.getQualifiedName().toString();
        writer.emitEmptyLine();
        writer.emitJavadoc("A manager of modules and provides adapters allowing for proper linking and\ninstance provision of types served by {@code @Provides} methods.", new Object[0]);
        writer.beginType(adapterName, "class", EnumSet.of(Modifier.PUBLIC, Modifier.FINAL), JavaWriter.type(ModuleAdapter.class, typeName), new String[0]);
        StringBuilder injectsField = new StringBuilder().append("{ ");
        for (Object injectableType : injects) {
            TypeMirror typeMirror = (TypeMirror) injectableType;
            String key = Util.isInterface(typeMirror) ? GeneratorKeys.get(typeMirror) : GeneratorKeys.rawMembersKey(typeMirror);
            injectsField.append(JavaWriter.stringLiteral(key)).append(", ");
        }
        injectsField.append("}");
        writer.emitField("String[]", "INJECTS", EnumSet.of(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL), injectsField.toString());
        StringBuilder staticInjectionsField = new StringBuilder().append("{ ");
        for (Object staticInjection : staticInjections) {
            TypeMirror typeMirror2 = (TypeMirror) staticInjection;
            staticInjectionsField.append(Util.typeToString(typeMirror2)).append(".class, ");
        }
        staticInjectionsField.append("}");
        writer.emitField("Class<?>[]", "STATIC_INJECTIONS", EnumSet.of(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL), staticInjectionsField.toString());
        StringBuilder includesField = new StringBuilder().append("{ ");
        for (Object include : includes) {
            if (!(include instanceof TypeMirror)) {
                this.processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "Unexpected value: " + include + " in includes of " + type, type);
            } else {
                TypeMirror typeMirror3 = (TypeMirror) include;
                includesField.append(Util.typeToString(typeMirror3)).append(".class, ");
            }
        }
        includesField.append("}");
        writer.emitField("Class<?>[]", "INCLUDES", EnumSet.of(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL), includesField.toString());
        writer.emitEmptyLine();
        writer.beginMethod((String) null, adapterName, EnumSet.of(Modifier.PUBLIC), new String[0]);
        writer.emitStatement("super(%s.class, INJECTS, STATIC_INJECTIONS, %s /*overrides*/, INCLUDES, %s /*complete*/, %s /*library*/)", typeName, Boolean.valueOf(overrides), Boolean.valueOf(complete), Boolean.valueOf(library));
        writer.endMethod();
        ExecutableElement noArgsConstructor = Util.getNoArgsConstructor(type);
        if (noArgsConstructor != null && Util.isCallableConstructor(noArgsConstructor)) {
            writer.emitEmptyLine();
            writer.emitAnnotation(Override.class);
            writer.beginMethod(typeName, "newModule", EnumSet.of(Modifier.PUBLIC), new String[0]);
            writer.emitStatement("return new %s()", typeName);
            writer.endMethod();
        }
        Map<ExecutableElement, String> methodToClassName = new LinkedHashMap<>();
        Map<String, AtomicInteger> methodNameToNextId = new LinkedHashMap<>();
        if (!providerMethods.isEmpty()) {
            writer.emitEmptyLine();
            writer.emitJavadoc("Used internally obtain dependency information, such as for cyclical\ngraph detection.", new Object[0]);
            writer.emitAnnotation(Override.class);
            writer.beginMethod("void", "getBindings", EnumSet.of(Modifier.PUBLIC), BINDINGS_MAP, "bindings", typeName, "module");
            for (ExecutableElement providerMethod : providerMethods) {
                Provides provides = (Provides) providerMethod.getAnnotation(Provides.class);
                switch (provides.type()) {
                    case UNIQUE:
                        String key2 = GeneratorKeys.get(providerMethod);
                        writer.emitStatement("bindings.contributeProvidesBinding(%s, new %s(module))", JavaWriter.stringLiteral(key2), bindingClassName(providerMethod, methodToClassName, methodNameToNextId));
                        break;
                    case SET:
                        String key3 = GeneratorKeys.getSetKey(providerMethod);
                        writer.emitStatement("SetBinding.add(bindings, %s, new %s(module))", JavaWriter.stringLiteral(key3), bindingClassName(providerMethod, methodToClassName, methodNameToNextId));
                        break;
                    case SET_VALUES:
                        String key4 = GeneratorKeys.get(providerMethod);
                        writer.emitStatement("SetBinding.add(bindings, %s, new %s(module))", JavaWriter.stringLiteral(key4), bindingClassName(providerMethod, methodToClassName, methodNameToNextId));
                        break;
                    default:
                        throw new AssertionError("Unknown @Provides type " + provides.type());
                }
            }
            writer.endMethod();
        }
        for (ExecutableElement providerMethod2 : providerMethods) {
            generateProvidesAdapter(writer, providerMethod2, methodToClassName, methodNameToNextId, library);
        }
        writer.endType();
        writer.close();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: dagger.internal.codegen.ModuleAdapterProcessor$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$javax$lang$model$element$ElementKind;

        static {
            try {
                $SwitchMap$dagger$Provides$Type[Provides.Type.UNIQUE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$dagger$Provides$Type[Provides.Type.SET.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$dagger$Provides$Type[Provides.Type.SET_VALUES.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            $SwitchMap$javax$lang$model$element$ElementKind = new int[ElementKind.values().length];
            try {
                $SwitchMap$javax$lang$model$element$ElementKind[ElementKind.CLASS.ordinal()] = 1;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    private Set<String> findImports(boolean multibindings, boolean providers, boolean dependencies) {
        Set<String> imports = new LinkedHashSet<>();
        imports.add(ModuleAdapter.class.getCanonicalName());
        if (providers) {
            imports.add(BindingsGroup.class.getCanonicalName());
            imports.add(Provider.class.getCanonicalName());
            imports.add(ProvidesBinding.class.getCanonicalName());
        }
        if (dependencies) {
            imports.add(Linker.class.getCanonicalName());
            imports.add(Set.class.getCanonicalName());
            imports.add(Binding.class.getCanonicalName());
        }
        if (multibindings) {
            imports.add(SetBinding.class.getCanonicalName());
        }
        return imports;
    }

    private boolean checkForDependencies(List<ExecutableElement> providerMethods) {
        for (ExecutableElement element : providerMethods) {
            if (!element.getParameters().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:5:0x000a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean checkForMultibindings(java.util.List<javax.lang.model.element.ExecutableElement> r5) {
        /*
            r4 = this;
            java.util.Iterator r1 = r5.iterator()
        L4:
            boolean r3 = r1.hasNext()
            if (r3 == 0) goto L26
            java.lang.Object r0 = r1.next()
            javax.lang.model.element.ExecutableElement r0 = (javax.lang.model.element.ExecutableElement) r0
            java.lang.Class<dagger.Provides> r3 = dagger.Provides.class
            java.lang.annotation.Annotation r3 = r0.getAnnotation(r3)
            dagger.Provides r3 = (dagger.Provides) r3
            dagger.Provides$Type r2 = r3.type()
            dagger.Provides$Type r3 = dagger.Provides.Type.SET
            if (r2 == r3) goto L24
            dagger.Provides$Type r3 = dagger.Provides.Type.SET_VALUES
            if (r2 != r3) goto L4
        L24:
            r3 = 1
        L25:
            return r3
        L26:
            r3 = 0
            goto L25
        */
        throw new UnsupportedOperationException("Method not decompiled: dagger.internal.codegen.ModuleAdapterProcessor.checkForMultibindings(java.util.List):boolean");
    }

    private String bindingClassName(ExecutableElement providerMethod, Map<ExecutableElement, String> methodToClassName, Map<String, AtomicInteger> methodNameToNextId) {
        String className = methodToClassName.get(providerMethod);
        if (className != null) {
            return className;
        }
        String methodName = providerMethod.getSimpleName().toString();
        String suffix = "";
        AtomicInteger id = methodNameToNextId.get(methodName);
        if (id == null) {
            methodNameToNextId.put(methodName, new AtomicInteger(2));
        } else {
            suffix = id.toString();
            id.incrementAndGet();
        }
        String uppercaseMethodName = Character.toUpperCase(methodName.charAt(0)) + methodName.substring(1);
        String className2 = uppercaseMethodName + "ProvidesAdapter" + suffix;
        methodToClassName.put(providerMethod, className2);
        return className2;
    }

    private void generateProvidesAdapter(JavaWriter writer, ExecutableElement providerMethod, Map<ExecutableElement, String> methodToClassName, Map<String, AtomicInteger> methodNameToNextId, boolean library) throws IOException {
        String methodName = providerMethod.getSimpleName().toString();
        String moduleType = Util.typeToString(providerMethod.getEnclosingElement().asType());
        String className = bindingClassName(providerMethod, methodToClassName, methodNameToNextId);
        String returnType = Util.typeToString(providerMethod.getReturnType());
        List<? extends VariableElement> parameters = providerMethod.getParameters();
        boolean dependent = !parameters.isEmpty();
        writer.emitEmptyLine();
        writer.emitJavadoc(AdapterJavadocs.bindingTypeDocs(returnType, false, false, dependent), new Object[0]);
        writer.beginType(className, "class", EnumSet.of(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL), JavaWriter.type(ProvidesBinding.class, returnType), JavaWriter.type(Provider.class, returnType));
        writer.emitField(moduleType, "module", EnumSet.of(Modifier.PRIVATE, Modifier.FINAL));
        Iterator i$ = parameters.iterator();
        while (i$.hasNext()) {
            Element parameter = (Element) i$.next();
            TypeMirror parameterType = parameter.asType();
            writer.emitField(JavaWriter.type(Binding.class, Util.typeToString(parameterType)), parameterName(parameter), EnumSet.of(Modifier.PRIVATE));
        }
        writer.emitEmptyLine();
        writer.beginMethod((String) null, className, EnumSet.of(Modifier.PUBLIC), moduleType, "module");
        boolean singleton = providerMethod.getAnnotation(Singleton.class) != null;
        String key = JavaWriter.stringLiteral(GeneratorKeys.get(providerMethod));
        Object[] objArr = new Object[4];
        objArr[0] = key;
        objArr[1] = singleton ? "IS_SINGLETON" : "NOT_SINGLETON";
        objArr[2] = JavaWriter.stringLiteral(moduleType);
        objArr[3] = JavaWriter.stringLiteral(methodName);
        writer.emitStatement("super(%s, %s, %s, %s)", objArr);
        writer.emitStatement("this.module = module", new Object[0]);
        writer.emitStatement("setLibrary(%s)", Boolean.valueOf(library));
        writer.endMethod();
        if (dependent) {
            writer.emitEmptyLine();
            writer.emitJavadoc("Used internally to link bindings/providers together at run time\naccording to their dependency graph.", new Object[0]);
            writer.emitAnnotation(Override.class);
            writer.emitAnnotation(SuppressWarnings.class, JavaWriter.stringLiteral("unchecked"));
            writer.beginMethod("void", "attach", EnumSet.of(Modifier.PUBLIC), Linker.class.getCanonicalName(), "linker");
            for (VariableElement parameter2 : parameters) {
                String parameterKey = GeneratorKeys.get(parameter2);
                writer.emitStatement("%s = (%s) linker.requestBinding(%s, %s.class, getClass().getClassLoader())", parameterName(parameter2), writer.compressType(JavaWriter.type(Binding.class, Util.typeToString(parameter2.asType()))), JavaWriter.stringLiteral(parameterKey), writer.compressType(moduleType));
            }
            writer.endMethod();
            writer.emitEmptyLine();
            writer.emitJavadoc("Used internally obtain dependency information, such as for cyclical\ngraph detection.", new Object[0]);
            writer.emitAnnotation(Override.class);
            String setOfBindings = JavaWriter.type(Set.class, "Binding<?>");
            writer.beginMethod("void", "getDependencies", EnumSet.of(Modifier.PUBLIC), setOfBindings, "getBindings", setOfBindings, "injectMembersBindings");
            Iterator i$2 = parameters.iterator();
            while (i$2.hasNext()) {
                Element parameter3 = (Element) i$2.next();
                writer.emitStatement("getBindings.add(%s)", parameterName(parameter3));
            }
            writer.endMethod();
        }
        writer.emitEmptyLine();
        writer.emitJavadoc("Returns the fully provisioned instance satisfying the contract for\n{@code Provider<%s>}.", returnType);
        writer.emitAnnotation(Override.class);
        writer.beginMethod(returnType, "get", EnumSet.of(Modifier.PUBLIC), new String[0]);
        StringBuilder args = new StringBuilder();
        boolean first = true;
        Iterator i$3 = parameters.iterator();
        while (i$3.hasNext()) {
            Element parameter4 = (Element) i$3.next();
            if (first) {
                first = false;
            } else {
                args.append(", ");
            }
            args.append(String.format("%s.get()", parameterName(parameter4)));
        }
        writer.emitStatement("return module.%s(%s)", methodName, args.toString());
        writer.endMethod();
        writer.endType();
    }

    private String parameterName(Element parameter) {
        return parameter.getSimpleName().contentEquals("module") ? "parameter_" + parameter.getSimpleName().toString() : parameter.getSimpleName().toString();
    }
}
