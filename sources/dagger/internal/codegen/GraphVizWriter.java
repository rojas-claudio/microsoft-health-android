package dagger.internal.codegen;

import com.google.android.gms.plus.PlusShare;
import com.j256.ormlite.stmt.query.SimpleComparison;
import com.squareup.javawriter.JavaWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.Map;
/* loaded from: classes.dex */
public final class GraphVizWriter implements Closeable {
    private static final String INDENT = "  ";
    private final Writer out;
    private int indent = 0;
    private int nextName = 1;
    private final Map<String, String> generatedNames = new LinkedHashMap();

    public GraphVizWriter(Writer out) {
        this.out = out;
    }

    public void beginGraph(String... attributes) throws IOException {
        indent();
        String type = this.indent == 0 ? "digraph " : "subgraph ";
        String name = nextName(this.indent == 0 ? "G" : "cluster");
        this.out.write(type);
        this.out.write(name);
        this.out.write(" {\n");
        this.indent++;
        attributes(attributes);
    }

    public void endGraph() throws IOException {
        this.indent--;
        indent();
        this.out.write("}\n");
    }

    public void node(String name, String... attributes) throws IOException {
        String name2 = nodeName(name);
        indent();
        this.out.write(name2);
        inlineAttributes(attributes);
        this.out.write(";\n");
    }

    public void edge(String source, String target, String... attributes) throws IOException {
        String source2 = nodeName(source);
        String target2 = nodeName(target);
        indent();
        this.out.write(source2);
        this.out.write(" -> ");
        this.out.write(target2);
        inlineAttributes(attributes);
        this.out.write(";\n");
    }

    public void nodeDefaults(String... attributes) throws IOException {
        if (attributes.length != 0) {
            indent();
            this.out.write("node");
            inlineAttributes(attributes);
            this.out.write(";\n");
        }
    }

    public void edgeDefaults(String... attributes) throws IOException {
        if (attributes.length != 0) {
            indent();
            this.out.write("edge");
            inlineAttributes(attributes);
            this.out.write(";\n");
        }
    }

    private void attributes(String[] attributes) throws IOException {
        if (attributes.length != 0) {
            if (attributes.length % 2 != 0) {
                throw new IllegalArgumentException();
            }
            for (int i = 0; i < attributes.length; i += 2) {
                indent();
                this.out.write(attributes[i]);
                this.out.write(" = ");
                this.out.write(literal(attributes[i + 1]));
                this.out.write(";\n");
            }
        }
    }

    private void inlineAttributes(String[] attributes) throws IOException {
        if (attributes.length != 0) {
            if (attributes.length % 2 != 0) {
                throw new IllegalArgumentException();
            }
            this.out.write(" [");
            for (int i = 0; i < attributes.length; i += 2) {
                if (i != 0) {
                    this.out.write(";");
                }
                this.out.write(attributes[i]);
                this.out.write(SimpleComparison.EQUAL_TO_OPERATION);
                this.out.write(literal(attributes[i + 1]));
            }
            this.out.write("]");
        }
    }

    private String nodeName(String name) throws IOException {
        if (name.matches("\\w+")) {
            return name;
        }
        String generatedName = this.generatedNames.get(name);
        if (generatedName == null) {
            String generatedName2 = nextName("n");
            this.generatedNames.put(name, generatedName2);
            node(generatedName2, PlusShare.KEY_CALL_TO_ACTION_LABEL, name);
            return generatedName2;
        }
        return generatedName;
    }

    private String literal(String raw) {
        return raw.matches("\\w+") ? raw : JavaWriter.stringLiteral(raw);
    }

    private void indent() throws IOException {
        for (int i = 0; i < this.indent; i++) {
            this.out.write(INDENT);
        }
    }

    private String nextName(String prefix) {
        StringBuilder append = new StringBuilder().append(prefix);
        int i = this.nextName;
        this.nextName = i + 1;
        return append.append(i).toString();
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.out.close();
    }
}
