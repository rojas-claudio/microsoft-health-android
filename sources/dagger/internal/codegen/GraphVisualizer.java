package dagger.internal.codegen;

import com.facebook.internal.ServerProtocol;
import dagger.internal.Binding;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/* loaded from: classes.dex */
public final class GraphVisualizer {
    private static final Pattern KEY_PATTERN = Pattern.compile("(?:@(?:[\\w$]+\\.)*([\\w$]+)(?:\\(.*\\))?/)?(?:members/)?(?:[\\w$]+\\.)*([\\w$]+)(\\<[^/]+\\>)?((\\[\\])*)");

    public void write(Map<String, Binding<?>> bindings, GraphVizWriter writer) throws IOException {
        Map<Binding<?>, String> namesIndex = buildNamesIndex(bindings);
        writer.beginGraph("concentrate", ServerProtocol.DIALOG_RETURN_SCOPES_TRUE);
        for (Map.Entry<Binding<?>, String> entry : namesIndex.entrySet()) {
            Binding<?> sourceBinding = entry.getKey();
            String sourceName = entry.getValue();
            Set<Binding<?>> dependencies = new TreeSet<>(new BindingComparator());
            sourceBinding.getDependencies(dependencies, dependencies);
            for (Binding<?> targetBinding : dependencies) {
                String targetName = namesIndex.get(targetBinding);
                if (targetName == null) {
                    targetName = "Unbound:" + targetBinding.provideKey;
                }
                writer.edge(sourceName, targetName, new String[0]);
            }
        }
        writer.endGraph();
    }

    private Map<Binding<?>, String> buildNamesIndex(Map<String, Binding<?>> bindings) {
        Map<String, Binding<?>> shortNameToBinding = new TreeMap<>();
        Set<Binding<?>> collisions = new HashSet<>();
        for (Map.Entry<String, Binding<?>> entry : bindings.entrySet()) {
            Binding<?> binding = entry.getValue();
            String shortName = shortName(entry.getKey());
            Binding<?> collision = shortNameToBinding.put(shortName, binding);
            if (collision != null && collision != binding) {
                collisions.add(binding);
                collisions.add(collision);
            }
        }
        for (Map.Entry<String, Binding<?>> entry2 : bindings.entrySet()) {
            Binding<?> binding2 = entry2.getValue();
            if (collisions.contains(binding2)) {
                String key = entry2.getKey();
                String shortName2 = shortName(key);
                shortNameToBinding.remove(shortName2);
                shortNameToBinding.put(key, binding2);
            }
        }
        Map<Binding<?>, String> bindingToName = new LinkedHashMap<>();
        for (Map.Entry<String, Binding<?>> entry3 : shortNameToBinding.entrySet()) {
            bindingToName.put(entry3.getValue(), entry3.getKey());
        }
        return bindingToName;
    }

    String shortName(String key) {
        Matcher matcher = KEY_PATTERN.matcher(key);
        if (matcher.matches()) {
            StringBuilder result = new StringBuilder();
            String annotationSimpleName = matcher.group(1);
            if (annotationSimpleName != null) {
                result.append('@').append(annotationSimpleName).append(' ');
            }
            String simpleName = matcher.group(2);
            result.append(simpleName);
            String typeParameters = matcher.group(3);
            if (typeParameters != null) {
                result.append(typeParameters);
            }
            String arrays = matcher.group(4);
            if (arrays != null) {
                result.append(arrays);
            }
            return result.toString();
        }
        throw new IllegalArgumentException("Unexpected key: " + key);
    }

    /* loaded from: classes.dex */
    private static class BindingComparator implements Comparator<Binding<?>> {
        private BindingComparator() {
        }

        @Override // java.util.Comparator
        public int compare(Binding<?> left, Binding<?> right) {
            return getStringForBinding(left).compareTo(getStringForBinding(right));
        }

        private String getStringForBinding(Binding<?> binding) {
            return binding == null ? "" : binding.toString();
        }
    }
}
