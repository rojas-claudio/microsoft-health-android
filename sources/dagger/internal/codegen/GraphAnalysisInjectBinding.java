package dagger.internal.codegen;

import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
/* loaded from: classes.dex */
final class GraphAnalysisInjectBinding extends Binding<Object> {
    private final Binding<?>[] bindings;
    private final List<String> keys;
    private final String supertypeKey;
    private final TypeElement type;

    private GraphAnalysisInjectBinding(String provideKey, String membersKey, TypeElement type, List<String> keys, String supertypeKey) {
        super(provideKey, membersKey, type.getAnnotation(Singleton.class) != null, type.getQualifiedName().toString());
        this.type = type;
        this.keys = keys;
        this.bindings = new Binding[keys.size()];
        this.supertypeKey = supertypeKey;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static GraphAnalysisInjectBinding create(TypeElement type, boolean mustHaveInjections) {
        List<String> requiredKeys = new ArrayList<>();
        boolean hasInjectConstructor = false;
        boolean hasNoArgsConstructor = false;
        for (VariableElement variableElement : type.getEnclosedElements()) {
            switch (AnonymousClass1.$SwitchMap$javax$lang$model$element$ElementKind[variableElement.getKind().ordinal()]) {
                case 1:
                    if (hasAtInject(variableElement) && !variableElement.getModifiers().contains(Modifier.STATIC)) {
                        requiredKeys.add(GeneratorKeys.get(variableElement));
                        break;
                    }
                    break;
                case 2:
                    ExecutableElement constructor = (ExecutableElement) variableElement;
                    List<? extends VariableElement> parameters = constructor.getParameters();
                    if (hasAtInject(variableElement)) {
                        if (hasAtSingleton(variableElement)) {
                            throw new IllegalArgumentException("Singleton annotations have no effect on constructors. Did you mean to annotate the class? " + type.getQualifiedName().toString());
                        }
                        if (hasInjectConstructor) {
                            throw new IllegalArgumentException("Too many injectable constructors on " + type.getQualifiedName().toString());
                        }
                        hasInjectConstructor = true;
                        for (VariableElement parameter : parameters) {
                            requiredKeys.add(GeneratorKeys.get(parameter));
                        }
                        break;
                    } else if (parameters.isEmpty()) {
                        hasNoArgsConstructor = true;
                        break;
                    } else {
                        break;
                    }
                default:
                    if (!hasAtInject(variableElement)) {
                        break;
                    } else {
                        throw new IllegalArgumentException("Unexpected @Inject annotation on " + variableElement);
                    }
            }
        }
        if (!hasInjectConstructor && requiredKeys.isEmpty() && mustHaveInjections) {
            throw new IllegalArgumentException("No injectable members on " + type.getQualifiedName().toString() + ". Do you want to add an injectable constructor?");
        }
        TypeMirror supertype = Util.getApplicationSupertype(type);
        String supertypeKey = supertype != null ? GeneratorKeys.rawMembersKey(supertype) : null;
        String provideKey = (hasInjectConstructor || (hasNoArgsConstructor && !requiredKeys.isEmpty())) ? GeneratorKeys.get(type.asType()) : null;
        String membersKey = GeneratorKeys.rawMembersKey(type.asType());
        return new GraphAnalysisInjectBinding(provideKey, membersKey, type, requiredKeys, supertypeKey);
    }

    /* renamed from: dagger.internal.codegen.GraphAnalysisInjectBinding$1  reason: invalid class name */
    /* loaded from: classes.dex */
    static /* synthetic */ class AnonymousClass1 {
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

    private static boolean hasAtInject(Element enclosed) {
        return enclosed.getAnnotation(Inject.class) != null;
    }

    private static boolean hasAtSingleton(Element enclosed) {
        return enclosed.getAnnotation(Singleton.class) != null;
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        String requiredBy = this.type.getQualifiedName().toString();
        for (int i = 0; i < this.keys.size(); i++) {
            this.bindings[i] = linker.requestBinding(this.keys.get(i), requiredBy, getClass().getClassLoader());
        }
        if (this.supertypeKey != null) {
            linker.requestBinding(this.supertypeKey, requiredBy, getClass().getClassLoader(), false, true);
        }
    }

    @Override // dagger.internal.Binding, javax.inject.Provider
    public Object get() {
        throw new AssertionError("Compile-time binding should never be called to inject.");
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(Object t) {
        throw new AssertionError("Compile-time binding should never be called to inject.");
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> get, Set<Binding<?>> injectMembers) {
        Collections.addAll(get, this.bindings);
    }
}
