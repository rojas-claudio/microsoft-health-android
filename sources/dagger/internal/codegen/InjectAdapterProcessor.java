package dagger.internal.codegen;

import com.squareup.javawriter.JavaWriter;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import dagger.internal.StaticInjection;
import dagger.internal.loaders.GeneratedAdapters;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.inject.Inject;
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
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import org.apache.commons.lang3.ClassUtils;
@SupportedAnnotationTypes({"javax.inject.Inject"})
/* loaded from: classes.dex */
public final class InjectAdapterProcessor extends AbstractProcessor {
    private final Set<String> remainingTypeNames = new LinkedHashSet();

    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    public boolean process(Set<? extends TypeElement> types, RoundEnvironment env) {
        this.remainingTypeNames.addAll(findInjectedClassNames(env));
        Iterator<String> i = this.remainingTypeNames.iterator();
        while (i.hasNext()) {
            InjectedClass injectedClass = createInjectedClass(i.next());
            boolean missingDependentClasses = (allTypesExist(injectedClass.fields) && (injectedClass.constructor == null || allTypesExist(injectedClass.constructor.getParameters())) && allTypesExist(injectedClass.staticFields)) ? false : true;
            if (!missingDependentClasses) {
                try {
                    generateInjectionsForClass(injectedClass);
                } catch (IOException e) {
                    error("Code gen failed: " + e, injectedClass.type);
                }
                i.remove();
            }
        }
        if (env.processingOver() && !this.remainingTypeNames.isEmpty()) {
            this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Could not find injection type required by " + this.remainingTypeNames);
        }
        return false;
    }

    private void generateInjectionsForClass(InjectedClass injectedClass) throws IOException {
        if (injectedClass.constructor != null || !injectedClass.fields.isEmpty()) {
            generateInjectAdapter(injectedClass.type, injectedClass.constructor, injectedClass.fields);
        }
        if (!injectedClass.staticFields.isEmpty()) {
            generateStaticInjection(injectedClass.type, injectedClass.staticFields);
        }
    }

    private boolean allTypesExist(Collection<? extends Element> elements) {
        for (Element element : elements) {
            if (element.asType().getKind() == TypeKind.ERROR) {
                return false;
            }
        }
        return true;
    }

    private Set<String> findInjectedClassNames(RoundEnvironment env) {
        Set<String> injectedTypeNames = new LinkedHashSet<>();
        for (Element element : env.getElementsAnnotatedWith(Inject.class)) {
            if (validateInjectable(element)) {
                injectedTypeNames.add(Util.rawTypeToString(element.getEnclosingElement().asType(), ClassUtils.PACKAGE_SEPARATOR_CHAR));
            }
        }
        return injectedTypeNames;
    }

    private boolean validateInjectable(Element injectable) {
        Element injectableType = injectable.getEnclosingElement();
        if (injectable.getKind() == ElementKind.CLASS) {
            error("@Inject is not valid on a class: " + Util.elementToString(injectable), injectable);
            return false;
        } else if (injectable.getKind() == ElementKind.METHOD) {
            error("Method injection is not supported: " + Util.elementToString(injectable), injectable);
            return false;
        } else if (injectable.getKind() == ElementKind.FIELD && injectable.getModifiers().contains(Modifier.FINAL)) {
            error("Can't inject a final field: " + Util.elementToString(injectable), injectable);
            return false;
        } else if (injectable.getKind() == ElementKind.FIELD && injectable.getModifiers().contains(Modifier.PRIVATE)) {
            error("Can't inject a private field: " + Util.elementToString(injectable), injectable);
            return false;
        } else if (injectable.getKind() == ElementKind.CONSTRUCTOR && injectable.getModifiers().contains(Modifier.PRIVATE)) {
            error("Can't inject a private constructor: " + Util.elementToString(injectable), injectable);
            return false;
        } else {
            ElementKind elementKind = injectableType.getEnclosingElement().getKind();
            boolean isClassOrInterface = elementKind.isClass() || elementKind.isInterface();
            boolean isStatic = injectableType.getModifiers().contains(Modifier.STATIC);
            if (!isClassOrInterface || isStatic) {
                return true;
            }
            error("Can't inject a non-static inner class: " + Util.elementToString(injectable), injectableType);
            return false;
        }
    }

    private InjectedClass createInjectedClass(String injectedClassName) {
        TypeElement type = this.processingEnv.getElementUtils().getTypeElement(injectedClassName);
        boolean isAbstract = type.getModifiers().contains(Modifier.ABSTRACT);
        List<Element> staticFields = new ArrayList<>();
        ExecutableElement constructor = null;
        List<Element> fields = new ArrayList<>();
        for (Element member : type.getEnclosedElements()) {
            if (member.getAnnotation(Inject.class) != null) {
                switch (AnonymousClass1.$SwitchMap$javax$lang$model$element$ElementKind[member.getKind().ordinal()]) {
                    case 1:
                        if (member.getModifiers().contains(Modifier.STATIC)) {
                            staticFields.add(member);
                            break;
                        } else {
                            fields.add(member);
                            continue;
                        }
                    case 2:
                        if (constructor != null) {
                            error("Too many injectable constructors on " + type.getQualifiedName(), member);
                        } else if (isAbstract) {
                            error("Abstract class " + type.getQualifiedName() + " must not have an @Inject-annotated constructor.", member);
                        }
                        constructor = (ExecutableElement) member;
                        continue;
                    default:
                        error("Cannot inject " + Util.elementToString(member), member);
                        continue;
                }
            }
        }
        if (constructor == null && !isAbstract && (constructor = Util.getNoArgsConstructor(type)) != null && !Util.isCallableConstructor(constructor)) {
            constructor = null;
        }
        return new InjectedClass(type, staticFields, constructor, fields);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: dagger.internal.codegen.InjectAdapterProcessor$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$javax$lang$model$element$ElementKind = new int[ElementKind.values().length];

        static {
            try {
                $SwitchMap$javax$lang$model$element$ElementKind[ElementKind.FIELD.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$javax$lang$model$element$ElementKind[ElementKind.CONSTRUCTOR.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void generateInjectAdapter(TypeElement type, ExecutableElement constructor, List<Element> fields) throws IOException {
        String packageName = Util.getPackage(type).getQualifiedName().toString();
        String strippedTypeName = strippedTypeName(type.getQualifiedName().toString(), packageName);
        TypeMirror supertype = Util.getApplicationSupertype(type);
        String adapterName = Util.adapterName(type, GeneratedAdapters.INJECT_ADAPTER_SUFFIX);
        JavaFileObject sourceFile = this.processingEnv.getFiler().createSourceFile(adapterName, new Element[]{type});
        JavaWriter writer = new JavaWriter(sourceFile.openWriter());
        boolean isAbstract = type.getModifiers().contains(Modifier.ABSTRACT);
        boolean injectMembers = (fields.isEmpty() && supertype == null) ? false : true;
        boolean disambiguateFields = (fields.isEmpty() || constructor == null || constructor.getParameters().isEmpty()) ? false : true;
        boolean dependent = injectMembers || !(constructor == null || constructor.getParameters().isEmpty());
        writer.emitSingleLineComment("Code generated by dagger-compiler.  Do not edit.", new Object[0]);
        writer.emitPackage(packageName);
        writer.emitImports(findImports(dependent, injectMembers, constructor != null));
        writer.emitEmptyLine();
        writer.emitJavadoc(AdapterJavadocs.bindingTypeDocs(strippedTypeName, isAbstract, injectMembers, dependent), new Object[0]);
        writer.beginType(adapterName, "class", EnumSet.of(Modifier.PUBLIC, Modifier.FINAL), JavaWriter.type(Binding.class, strippedTypeName), implementedInterfaces(strippedTypeName, injectMembers, constructor != null));
        writeMemberBindingsFields(writer, fields, disambiguateFields);
        if (constructor != null) {
            writeParameterBindingsFields(writer, constructor, disambiguateFields);
        }
        if (supertype != null) {
            writeSupertypeInjectorField(writer, supertype);
        }
        writer.emitEmptyLine();
        writeInjectAdapterConstructor(writer, constructor, type, strippedTypeName, adapterName);
        if (dependent) {
            writeAttachMethod(writer, constructor, fields, disambiguateFields, strippedTypeName, supertype, true);
            writeGetDependenciesMethod(writer, constructor, fields, disambiguateFields, supertype, true);
        }
        if (constructor != null) {
            writeGetMethod(writer, constructor, disambiguateFields, injectMembers, strippedTypeName);
        }
        if (injectMembers) {
            writeMembersInjectMethod(writer, fields, disambiguateFields, strippedTypeName, supertype);
        }
        writer.endType();
        writer.close();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void generateStaticInjection(TypeElement type, List<Element> fields) throws IOException {
        String typeName = type.getQualifiedName().toString();
        String adapterName = Util.adapterName(type, GeneratedAdapters.STATIC_INJECTION_SUFFIX);
        JavaFileObject sourceFile = this.processingEnv.getFiler().createSourceFile(adapterName, new Element[]{type});
        JavaWriter writer = new JavaWriter(sourceFile.openWriter());
        writer.emitSingleLineComment("Code generated by dagger-compiler.  Do not edit.", new Object[0]);
        writer.emitPackage(Util.getPackage(type).getQualifiedName().toString());
        writer.emitImports(Arrays.asList(StaticInjection.class.getName(), Binding.class.getName(), Linker.class.getName()));
        writer.emitEmptyLine();
        writer.emitJavadoc("A manager for {@code %s}'s injections into static fields.", type.getSimpleName());
        writer.beginType(adapterName, "class", EnumSet.of(Modifier.PUBLIC, Modifier.FINAL), StaticInjection.class.getSimpleName(), new String[0]);
        writeMemberBindingsFields(writer, fields, false);
        writer.emitEmptyLine();
        writeAttachMethod(writer, null, fields, false, typeName, null, true);
        writeStaticInjectMethod(writer, fields, typeName);
        writer.endType();
        writer.close();
    }

    private void writeMemberBindingsFields(JavaWriter writer, List<Element> fields, boolean disambiguateFields) throws IOException {
        for (Element field : fields) {
            writer.emitField(JavaWriter.type(Binding.class, Util.typeToString(field.asType())), fieldName(disambiguateFields, field), EnumSet.of(Modifier.PRIVATE));
        }
    }

    private void writeParameterBindingsFields(JavaWriter writer, ExecutableElement constructor, boolean disambiguateFields) throws IOException {
        for (VariableElement parameter : constructor.getParameters()) {
            writer.emitField(JavaWriter.type(Binding.class, Util.typeToString(parameter.asType())), parameterName(disambiguateFields, parameter), EnumSet.of(Modifier.PRIVATE));
        }
    }

    private void writeSupertypeInjectorField(JavaWriter writer, TypeMirror supertype) throws IOException {
        writer.emitField(JavaWriter.type(Binding.class, Util.rawTypeToString(supertype, ClassUtils.PACKAGE_SEPARATOR_CHAR)), "supertype", EnumSet.of(Modifier.PRIVATE));
    }

    private void writeInjectAdapterConstructor(JavaWriter writer, ExecutableElement constructor, TypeElement type, String strippedTypeName, String adapterName) throws IOException {
        writer.beginMethod((String) null, adapterName, EnumSet.of(Modifier.PUBLIC), new String[0]);
        String key = constructor != null ? JavaWriter.stringLiteral(GeneratorKeys.get(type.asType())) : null;
        String membersKey = JavaWriter.stringLiteral(GeneratorKeys.rawMembersKey(type.asType()));
        boolean singleton = type.getAnnotation(Singleton.class) != null;
        Object[] objArr = new Object[4];
        objArr[0] = key;
        objArr[1] = membersKey;
        objArr[2] = singleton ? "IS_SINGLETON" : "NOT_SINGLETON";
        objArr[3] = strippedTypeName;
        writer.emitStatement("super(%s, %s, %s, %s.class)", objArr);
        writer.endMethod();
        writer.emitEmptyLine();
    }

    private void writeAttachMethod(JavaWriter writer, ExecutableElement constructor, List<Element> fields, boolean disambiguateFields, String typeName, TypeMirror supertype, boolean extendsBinding) throws IOException {
        writer.emitJavadoc("Used internally to link bindings/providers together at run time\naccording to their dependency graph.", new Object[0]);
        if (extendsBinding) {
            writer.emitAnnotation(Override.class);
        }
        writer.emitAnnotation(SuppressWarnings.class, JavaWriter.stringLiteral("unchecked"));
        writer.beginMethod("void", "attach", EnumSet.of(Modifier.PUBLIC), Linker.class.getCanonicalName(), "linker");
        if (constructor != null) {
            for (VariableElement parameter : constructor.getParameters()) {
                writer.emitStatement("%s = (%s) linker.requestBinding(%s, %s.class, getClass().getClassLoader())", parameterName(disambiguateFields, parameter), writer.compressType(JavaWriter.type(Binding.class, Util.typeToString(parameter.asType()))), JavaWriter.stringLiteral(GeneratorKeys.get(parameter)), typeName);
            }
        }
        Iterator i$ = fields.iterator();
        while (i$.hasNext()) {
            VariableElement variableElement = (Element) i$.next();
            writer.emitStatement("%s = (%s) linker.requestBinding(%s, %s.class, getClass().getClassLoader())", fieldName(disambiguateFields, variableElement), writer.compressType(JavaWriter.type(Binding.class, Util.typeToString(variableElement.asType()))), JavaWriter.stringLiteral(GeneratorKeys.get(variableElement)), typeName);
        }
        if (supertype != null) {
            writer.emitStatement("%s = (%s) linker.requestBinding(%s, %s.class, getClass().getClassLoader(), false, true)", "supertype", writer.compressType(JavaWriter.type(Binding.class, Util.rawTypeToString(supertype, ClassUtils.PACKAGE_SEPARATOR_CHAR))), JavaWriter.stringLiteral(GeneratorKeys.rawMembersKey(supertype)), typeName);
        }
        writer.endMethod();
        writer.emitEmptyLine();
    }

    private void writeGetDependenciesMethod(JavaWriter writer, ExecutableElement constructor, List<Element> fields, boolean disambiguateFields, TypeMirror supertype, boolean extendsBinding) throws IOException {
        writer.emitJavadoc("Used internally obtain dependency information, such as for cyclical\ngraph detection.", new Object[0]);
        if (extendsBinding) {
            writer.emitAnnotation(Override.class);
        }
        String setOfBindings = JavaWriter.type(Set.class, "Binding<?>");
        writer.beginMethod("void", "getDependencies", EnumSet.of(Modifier.PUBLIC), setOfBindings, "getBindings", setOfBindings, "injectMembersBindings");
        if (constructor != null) {
            for (Element parameter : constructor.getParameters()) {
                writer.emitStatement("getBindings.add(%s)", parameterName(disambiguateFields, parameter));
            }
        }
        for (Element field : fields) {
            writer.emitStatement("injectMembersBindings.add(%s)", fieldName(disambiguateFields, field));
        }
        if (supertype != null) {
            writer.emitStatement("injectMembersBindings.add(%s)", "supertype");
        }
        writer.endMethod();
        writer.emitEmptyLine();
    }

    private void writeGetMethod(JavaWriter writer, ExecutableElement constructor, boolean disambiguateFields, boolean injectMembers, String strippedTypeName) throws IOException {
        writer.emitJavadoc("Returns the fully provisioned instance satisfying the contract for\n{@code Provider<%s>}.", strippedTypeName);
        writer.emitAnnotation(Override.class);
        writer.beginMethod(strippedTypeName, "get", EnumSet.of(Modifier.PUBLIC), new String[0]);
        StringBuilder newInstance = new StringBuilder();
        newInstance.append(strippedTypeName).append(" result = new ");
        newInstance.append(strippedTypeName).append('(');
        boolean first = true;
        for (VariableElement parameter : constructor.getParameters()) {
            if (first) {
                first = false;
            } else {
                newInstance.append(", ");
            }
            newInstance.append(parameterName(disambiguateFields, parameter)).append(".get()");
        }
        newInstance.append(')');
        writer.emitStatement(newInstance.toString(), new Object[0]);
        if (injectMembers) {
            writer.emitStatement("injectMembers(result)", new Object[0]);
        }
        writer.emitStatement("return result", new Object[0]);
        writer.endMethod();
        writer.emitEmptyLine();
    }

    private void writeMembersInjectMethod(JavaWriter writer, List<Element> fields, boolean disambiguateFields, String strippedTypeName, TypeMirror supertype) throws IOException {
        writer.emitJavadoc("Injects any {@code @Inject} annotated fields in the given instance,\nsatisfying the contract for {@code Provider<%s>}.", strippedTypeName);
        writer.emitAnnotation(Override.class);
        writer.beginMethod("void", "injectMembers", EnumSet.of(Modifier.PUBLIC), strippedTypeName, "object");
        for (Element field : fields) {
            writer.emitStatement("object.%s = %s.get()", field.getSimpleName(), fieldName(disambiguateFields, field));
        }
        if (supertype != null) {
            writer.emitStatement("supertype.injectMembers(object)", new Object[0]);
        }
        writer.endMethod();
        writer.emitEmptyLine();
    }

    private void writeStaticInjectMethod(JavaWriter writer, List<Element> fields, String typeName) throws IOException {
        writer.emitEmptyLine();
        writer.emitJavadoc("Performs the injections of dependencies into static fields when requested by\nthe {@code dagger.ObjectGraph}.", new Object[0]);
        writer.emitAnnotation(Override.class);
        writer.beginMethod("void", "inject", EnumSet.of(Modifier.PUBLIC), new String[0]);
        for (Element field : fields) {
            writer.emitStatement("%s.%s = %s.get()", writer.compressType(typeName), field.getSimpleName().toString(), fieldName(false, field));
        }
        writer.endMethod();
        writer.emitEmptyLine();
    }

    private Set<String> findImports(boolean dependent, boolean injectMembers, boolean isProvider) {
        Set<String> imports = new LinkedHashSet<>();
        imports.add(Binding.class.getCanonicalName());
        if (dependent) {
            imports.add(Linker.class.getCanonicalName());
            imports.add(Set.class.getCanonicalName());
        }
        if (injectMembers) {
            imports.add(MembersInjector.class.getCanonicalName());
        }
        if (isProvider) {
            imports.add(Provider.class.getCanonicalName());
        }
        return imports;
    }

    private String[] implementedInterfaces(String strippedTypeName, boolean hasFields, boolean isProvider) {
        List<String> interfaces = new ArrayList<>();
        if (isProvider) {
            interfaces.add(JavaWriter.type(Provider.class, strippedTypeName));
        }
        if (hasFields) {
            interfaces.add(JavaWriter.type(MembersInjector.class, strippedTypeName));
        }
        return (String[]) interfaces.toArray(new String[interfaces.size()]);
    }

    private String strippedTypeName(String type, String packageName) {
        return type.substring(packageName.isEmpty() ? 0 : packageName.length() + 1);
    }

    private String fieldName(boolean disambiguateFields, Element field) {
        return (disambiguateFields ? "field_" : "") + field.getSimpleName().toString();
    }

    private String parameterName(boolean disambiguateFields, Element parameter) {
        return (disambiguateFields ? "parameter_" : "") + parameter.getSimpleName().toString();
    }

    private void error(String msg, Element element) {
        this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, msg, element);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class InjectedClass {
        final ExecutableElement constructor;
        final List<Element> fields;
        final List<Element> staticFields;
        final TypeElement type;

        InjectedClass(TypeElement type, List<Element> staticFields, ExecutableElement constructor, List<Element> fields) {
            this.type = type;
            this.staticFields = staticFields;
            this.constructor = constructor;
            this.fields = fields;
        }
    }
}
