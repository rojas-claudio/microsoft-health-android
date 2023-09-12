package com.squareup.javawriter;

import com.j256.ormlite.stmt.query.SimpleComparison;
import com.microsoft.kapp.style.utils.MetricSpannerUtils;
import com.microsoft.kapp.utils.Constants;
import java.io.Closeable;
import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.lang.model.element.Modifier;
/* loaded from: classes.dex */
public final class JavaWriter implements Closeable {
    private static final String INDENT = "  ";
    private static final Pattern TYPE_PATTERN = Pattern.compile("(?:[\\w$]+\\.)*([\\w\\.*$]+)");
    private final Writer out;
    private String packagePrefix;
    private final Map<String, String> importedTypes = new LinkedHashMap();
    private final List<Scope> scopes = new ArrayList();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public enum Scope {
        TYPE_DECLARATION,
        ABSTRACT_METHOD,
        NON_ABSTRACT_METHOD,
        CONTROL_FLOW,
        ANNOTATION_ATTRIBUTE,
        ANNOTATION_ARRAY_VALUE,
        INITIALIZER
    }

    public JavaWriter(Writer out) {
        this.out = out;
    }

    public JavaWriter emitPackage(String packageName) throws IOException {
        if (this.packagePrefix != null) {
            throw new IllegalStateException();
        }
        if (packageName.isEmpty()) {
            this.packagePrefix = "";
        } else {
            this.out.write("package ");
            this.out.write(packageName);
            this.out.write(";\n\n");
            this.packagePrefix = packageName + ".";
        }
        return this;
    }

    public JavaWriter emitImports(String... types) throws IOException {
        return emitImports(Arrays.asList(types));
    }

    public JavaWriter emitImports(Collection<String> types) throws IOException {
        Iterator i$ = new TreeSet(types).iterator();
        while (i$.hasNext()) {
            String type = (String) i$.next();
            Matcher matcher = TYPE_PATTERN.matcher(type);
            if (!matcher.matches()) {
                throw new IllegalArgumentException(type);
            }
            if (this.importedTypes.put(type, matcher.group(1)) != null) {
                throw new IllegalArgumentException(type);
            }
            this.out.write("import ");
            this.out.write(type);
            this.out.write(";\n");
        }
        return this;
    }

    public JavaWriter emitStaticImports(String... types) throws IOException {
        return emitStaticImports(Arrays.asList(types));
    }

    public JavaWriter emitStaticImports(Collection<String> types) throws IOException {
        Iterator i$ = new TreeSet(types).iterator();
        while (i$.hasNext()) {
            String type = (String) i$.next();
            Matcher matcher = TYPE_PATTERN.matcher(type);
            if (!matcher.matches()) {
                throw new IllegalArgumentException(type);
            }
            if (this.importedTypes.put(type, matcher.group(1)) != null) {
                throw new IllegalArgumentException(type);
            }
            this.out.write("import static ");
            this.out.write(type);
            this.out.write(";\n");
        }
        return this;
    }

    private JavaWriter emitType(String type) throws IOException {
        this.out.write(compressType(type));
        return this;
    }

    public String compressType(String type) {
        StringBuilder sb = new StringBuilder();
        if (this.packagePrefix == null) {
            throw new IllegalStateException();
        }
        Matcher m = TYPE_PATTERN.matcher(type);
        int pos = 0;
        while (true) {
            boolean found = m.find(pos);
            int typeStart = found ? m.start() : type.length();
            sb.append((CharSequence) type, pos, typeStart);
            if (found) {
                String name = m.group(0);
                String imported = this.importedTypes.get(name);
                if (imported != null) {
                    sb.append(imported);
                } else if (isClassInPackage(name)) {
                    String compressed = name.substring(this.packagePrefix.length());
                    if (isAmbiguous(compressed)) {
                        sb.append(name);
                    } else {
                        sb.append(compressed);
                    }
                } else if (name.startsWith("java.lang.")) {
                    sb.append(name.substring("java.lang.".length()));
                } else {
                    sb.append(name);
                }
                pos = m.end();
            } else {
                return sb.toString();
            }
        }
    }

    private boolean isClassInPackage(String name) {
        if (name.startsWith(this.packagePrefix)) {
            if (name.indexOf(46, this.packagePrefix.length()) == -1) {
                return true;
            }
            int index = name.indexOf(46);
            if (name.substring(index + 1, index + 2).matches("[A-Z]")) {
                return true;
            }
        }
        return false;
    }

    private boolean isAmbiguous(String compressed) {
        return this.importedTypes.values().contains(compressed);
    }

    public JavaWriter beginInitializer(boolean isStatic) throws IOException {
        indent();
        if (isStatic) {
            this.out.write("static");
            this.out.write(" {\n");
        } else {
            this.out.write("{\n");
        }
        pushScope(Scope.INITIALIZER);
        return this;
    }

    public JavaWriter endInitializer() throws IOException {
        popScope(Scope.INITIALIZER);
        indent();
        this.out.write("}\n");
        return this;
    }

    public JavaWriter beginType(String type, String kind) throws IOException {
        return beginType(type, kind, EnumSet.noneOf(Modifier.class), (String) null, new String[0]);
    }

    @Deprecated
    public JavaWriter beginType(String type, String kind, int modifiers) throws IOException {
        return beginType(type, kind, modifiersAsSet(modifiers), (String) null, new String[0]);
    }

    public JavaWriter beginType(String type, String kind, Set<Modifier> modifiers) throws IOException {
        return beginType(type, kind, modifiers, (String) null, new String[0]);
    }

    @Deprecated
    public JavaWriter beginType(String type, String kind, int modifiers, String extendsType, String... implementsTypes) throws IOException {
        return beginType(type, kind, modifiersAsSet(modifiers), extendsType, implementsTypes);
    }

    public JavaWriter beginType(String type, String kind, Set<Modifier> modifiers, String extendsType, String... implementsTypes) throws IOException {
        indent();
        emitModifiers(modifiers);
        this.out.write(kind);
        this.out.write(MetricSpannerUtils.METRIC_UNITS_SEPARATOR_SPACE);
        emitType(type);
        if (extendsType != null) {
            this.out.write(" extends ");
            emitType(extendsType);
        }
        if (implementsTypes.length > 0) {
            this.out.write("\n");
            indent();
            this.out.write("    implements ");
            for (int i = 0; i < implementsTypes.length; i++) {
                if (i != 0) {
                    this.out.write(", ");
                }
                emitType(implementsTypes[i]);
            }
        }
        this.out.write(" {\n");
        pushScope(Scope.TYPE_DECLARATION);
        return this;
    }

    public JavaWriter endType() throws IOException {
        popScope(Scope.TYPE_DECLARATION);
        indent();
        this.out.write("}\n");
        return this;
    }

    public JavaWriter emitField(String type, String name) throws IOException {
        return emitField(type, name, EnumSet.noneOf(Modifier.class), (String) null);
    }

    @Deprecated
    public JavaWriter emitField(String type, String name, int modifiers) throws IOException {
        return emitField(type, name, modifiersAsSet(modifiers), (String) null);
    }

    public JavaWriter emitField(String type, String name, Set<Modifier> modifiers) throws IOException {
        return emitField(type, name, modifiers, (String) null);
    }

    @Deprecated
    public JavaWriter emitField(String type, String name, int modifiers, String initialValue) throws IOException {
        return emitField(type, name, modifiersAsSet(modifiers), initialValue);
    }

    public JavaWriter emitField(String type, String name, Set<Modifier> modifiers, String initialValue) throws IOException {
        indent();
        emitModifiers(modifiers);
        emitType(type);
        this.out.write(MetricSpannerUtils.METRIC_UNITS_SEPARATOR_SPACE);
        this.out.write(name);
        if (initialValue != null) {
            this.out.write(" = ");
            this.out.write(initialValue);
        }
        this.out.write(";\n");
        return this;
    }

    @Deprecated
    public JavaWriter beginMethod(String returnType, String name, int modifiers, String... parameters) throws IOException {
        return beginMethod(returnType, name, modifiersAsSet(modifiers), Arrays.asList(parameters), (List<String>) null);
    }

    public JavaWriter beginMethod(String returnType, String name, Set<Modifier> modifiers, String... parameters) throws IOException {
        return beginMethod(returnType, name, modifiers, Arrays.asList(parameters), (List<String>) null);
    }

    @Deprecated
    public JavaWriter beginMethod(String returnType, String name, int modifiers, List<String> parameters, List<String> throwsTypes) throws IOException {
        return beginMethod(returnType, name, modifiersAsSet(modifiers), parameters, throwsTypes);
    }

    public JavaWriter beginMethod(String returnType, String name, Set<Modifier> modifiers, List<String> parameters, List<String> throwsTypes) throws IOException {
        indent();
        emitModifiers(modifiers);
        if (returnType != null) {
            emitType(returnType);
            this.out.write(MetricSpannerUtils.METRIC_UNITS_SEPARATOR_SPACE);
            this.out.write(name);
        } else {
            emitType(name);
        }
        this.out.write("(");
        if (parameters != null) {
            int p = 0;
            while (p < parameters.size()) {
                if (p != 0) {
                    this.out.write(", ");
                }
                int p2 = p + 1;
                emitType(parameters.get(p));
                this.out.write(MetricSpannerUtils.METRIC_UNITS_SEPARATOR_SPACE);
                p = p2 + 1;
                emitType(parameters.get(p2));
            }
        }
        this.out.write(")");
        if (throwsTypes != null && throwsTypes.size() > 0) {
            this.out.write("\n");
            indent();
            this.out.write("    throws ");
            for (int i = 0; i < throwsTypes.size(); i++) {
                if (i != 0) {
                    this.out.write(", ");
                }
                emitType(throwsTypes.get(i));
            }
        }
        if (modifiers.contains(Modifier.ABSTRACT)) {
            this.out.write(";\n");
            pushScope(Scope.ABSTRACT_METHOD);
        } else {
            this.out.write(" {\n");
            pushScope(Scope.NON_ABSTRACT_METHOD);
        }
        return this;
    }

    public JavaWriter emitJavadoc(String javadoc, Object... params) throws IOException {
        String formatted = String.format(javadoc, params);
        indent();
        this.out.write("/**\n");
        String[] arr$ = formatted.split("\n");
        for (String line : arr$) {
            indent();
            this.out.write(" * ");
            this.out.write(line);
            this.out.write("\n");
        }
        indent();
        this.out.write(" */\n");
        return this;
    }

    public JavaWriter emitSingleLineComment(String comment, Object... args) throws IOException {
        indent();
        this.out.write("// ");
        this.out.write(String.format(comment, args));
        this.out.write("\n");
        return this;
    }

    public JavaWriter emitEmptyLine() throws IOException {
        this.out.write("\n");
        return this;
    }

    public JavaWriter emitEnumValue(String name) throws IOException {
        indent();
        this.out.write(name);
        this.out.write(",\n");
        return this;
    }

    public JavaWriter emitAnnotation(String annotation) throws IOException {
        return emitAnnotation(annotation, Collections.emptyMap());
    }

    public JavaWriter emitAnnotation(Class<? extends Annotation> annotationType) throws IOException {
        return emitAnnotation(type(annotationType, new String[0]), Collections.emptyMap());
    }

    public JavaWriter emitAnnotation(Class<? extends Annotation> annotationType, Object value) throws IOException {
        return emitAnnotation(type(annotationType, new String[0]), value);
    }

    public JavaWriter emitAnnotation(String annotation, Object value) throws IOException {
        indent();
        this.out.write(Constants.CHAR_AT);
        emitType(annotation);
        this.out.write("(");
        emitAnnotationValue(value);
        this.out.write(")");
        this.out.write("\n");
        return this;
    }

    public JavaWriter emitAnnotation(Class<? extends Annotation> annotationType, Map<String, ?> attributes) throws IOException {
        return emitAnnotation(type(annotationType, new String[0]), attributes);
    }

    public JavaWriter emitAnnotation(String annotation, Map<String, ?> attributes) throws IOException {
        indent();
        this.out.write(Constants.CHAR_AT);
        emitType(annotation);
        switch (attributes.size()) {
            case 0:
                break;
            case 1:
                Map.Entry<String, ?> onlyEntry = attributes.entrySet().iterator().next();
                if ("value".equals(onlyEntry.getKey())) {
                    this.out.write("(");
                    emitAnnotationValue(onlyEntry.getValue());
                    this.out.write(")");
                    break;
                }
            default:
                this.out.write("(");
                pushScope(Scope.ANNOTATION_ATTRIBUTE);
                boolean firstAttribute = true;
                for (Map.Entry<String, ?> entry : attributes.entrySet()) {
                    if (firstAttribute) {
                        firstAttribute = false;
                        this.out.write("\n");
                    } else {
                        this.out.write(",\n");
                    }
                    indent();
                    this.out.write(entry.getKey());
                    this.out.write(" = ");
                    Object value = entry.getValue();
                    emitAnnotationValue(value);
                }
                popScope(Scope.ANNOTATION_ATTRIBUTE);
                this.out.write("\n");
                indent();
                this.out.write(")");
                break;
        }
        this.out.write("\n");
        return this;
    }

    private JavaWriter emitAnnotationValue(Object value) throws IOException {
        if (value instanceof Object[]) {
            this.out.write("{");
            boolean firstValue = true;
            pushScope(Scope.ANNOTATION_ARRAY_VALUE);
            Object[] arr$ = (Object[]) value;
            for (Object o : arr$) {
                if (firstValue) {
                    firstValue = false;
                    this.out.write("\n");
                } else {
                    this.out.write(",\n");
                }
                indent();
                this.out.write(o.toString());
            }
            popScope(Scope.ANNOTATION_ARRAY_VALUE);
            this.out.write("\n");
            indent();
            this.out.write("}");
        } else {
            this.out.write(value.toString());
        }
        return this;
    }

    public JavaWriter emitStatement(String pattern, Object... args) throws IOException {
        checkInMethod();
        String[] lines = String.format(pattern, args).split("\n", -1);
        indent();
        this.out.write(lines[0]);
        for (int i = 1; i < lines.length; i++) {
            this.out.write("\n");
            hangingIndent();
            this.out.write(lines[i]);
        }
        this.out.write(";\n");
        return this;
    }

    public JavaWriter beginControlFlow(String controlFlow) throws IOException {
        checkInMethod();
        indent();
        this.out.write(controlFlow);
        this.out.write(" {\n");
        pushScope(Scope.CONTROL_FLOW);
        return this;
    }

    public JavaWriter nextControlFlow(String controlFlow) throws IOException {
        popScope(Scope.CONTROL_FLOW);
        indent();
        pushScope(Scope.CONTROL_FLOW);
        this.out.write("} ");
        this.out.write(controlFlow);
        this.out.write(" {\n");
        return this;
    }

    public JavaWriter endControlFlow() throws IOException {
        return endControlFlow(null);
    }

    public JavaWriter endControlFlow(String controlFlow) throws IOException {
        popScope(Scope.CONTROL_FLOW);
        indent();
        if (controlFlow != null) {
            this.out.write("} ");
            this.out.write(controlFlow);
            this.out.write(";\n");
        } else {
            this.out.write("}\n");
        }
        return this;
    }

    public JavaWriter endMethod() throws IOException {
        Scope popped = popScope();
        if (popped == Scope.NON_ABSTRACT_METHOD) {
            indent();
            this.out.write("}\n");
        } else if (popped != Scope.ABSTRACT_METHOD) {
            throw new IllegalStateException();
        }
        return this;
    }

    public static String stringLiteral(String data) {
        StringBuilder result = new StringBuilder();
        result.append('\"');
        for (int i = 0; i < data.length(); i++) {
            char c = data.charAt(i);
            switch (c) {
                case '\b':
                    result.append("\\b");
                    break;
                case '\t':
                    result.append("\\t");
                    break;
                case '\n':
                    result.append("\\n");
                    break;
                case '\f':
                    result.append("\\f");
                    break;
                case '\r':
                    result.append("\\r");
                    break;
                case '\"':
                    result.append("\\\"");
                    break;
                case '\\':
                    result.append("\\\\");
                    break;
                default:
                    if (Character.isISOControl(c)) {
                        result.append(String.format("\\u%04x", Integer.valueOf(c)));
                        break;
                    } else {
                        result.append(c);
                        break;
                    }
            }
        }
        result.append('\"');
        return result.toString();
    }

    public static String type(Class<?> raw, String... parameters) {
        if (parameters.length == 0) {
            return raw.getCanonicalName();
        }
        if (raw.getTypeParameters().length != parameters.length) {
            throw new IllegalArgumentException();
        }
        StringBuilder result = new StringBuilder();
        result.append(raw.getCanonicalName());
        result.append(SimpleComparison.LESS_THAN_OPERATION);
        result.append(parameters[0]);
        for (int i = 1; i < parameters.length; i++) {
            result.append(", ");
            result.append(parameters[i]);
        }
        result.append(SimpleComparison.GREATER_THAN_OPERATION);
        return result.toString();
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.out.close();
    }

    private void emitModifiers(Set<Modifier> modifiers) throws IOException {
        if (!(modifiers instanceof EnumSet)) {
            modifiers = EnumSet.copyOf((Collection) modifiers);
        }
        for (Modifier modifier : modifiers) {
            this.out.append((CharSequence) modifier.toString()).append(' ');
        }
    }

    private static EnumSet<Modifier> modifiersAsSet(int modifiers) {
        EnumSet<Modifier> modifierSet = EnumSet.noneOf(Modifier.class);
        if ((modifiers & 1) != 0) {
            modifierSet.add(Modifier.PUBLIC);
        }
        if ((modifiers & 2) != 0) {
            modifierSet.add(Modifier.PRIVATE);
        }
        if ((modifiers & 4) != 0) {
            modifierSet.add(Modifier.PROTECTED);
        }
        if ((modifiers & 8) != 0) {
            modifierSet.add(Modifier.STATIC);
        }
        if ((modifiers & 16) != 0) {
            modifierSet.add(Modifier.FINAL);
        }
        if ((modifiers & 1024) != 0) {
            modifierSet.add(Modifier.ABSTRACT);
        }
        if ((modifiers & 32) != 0) {
            modifierSet.add(Modifier.SYNCHRONIZED);
        }
        if ((modifiers & 128) != 0) {
            modifierSet.add(Modifier.TRANSIENT);
        }
        if ((modifiers & 64) != 0) {
            modifierSet.add(Modifier.VOLATILE);
        }
        return modifierSet;
    }

    private void indent() throws IOException {
        int count = this.scopes.size();
        for (int i = 0; i < count; i++) {
            this.out.write(INDENT);
        }
    }

    private void hangingIndent() throws IOException {
        int count = this.scopes.size() + 2;
        for (int i = 0; i < count; i++) {
            this.out.write(INDENT);
        }
    }

    private void checkInMethod() {
        Scope scope = peekScope();
        if (scope != Scope.NON_ABSTRACT_METHOD && scope != Scope.CONTROL_FLOW && scope != Scope.INITIALIZER) {
            throw new IllegalArgumentException();
        }
    }

    private void pushScope(Scope pushed) {
        this.scopes.add(pushed);
    }

    private Scope peekScope() {
        return this.scopes.get(this.scopes.size() - 1);
    }

    private Scope popScope() {
        return this.scopes.remove(this.scopes.size() - 1);
    }

    private void popScope(Scope expected) {
        if (this.scopes.remove(this.scopes.size() - 1) != expected) {
            throw new IllegalStateException();
        }
    }
}
