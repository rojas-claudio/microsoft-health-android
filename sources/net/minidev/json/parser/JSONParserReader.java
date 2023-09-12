package net.minidev.json.parser;

import java.io.IOException;
import java.io.Reader;
import net.minidev.json.JSONValue;
import net.minidev.json.writer.JsonReaderI;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class JSONParserReader extends JSONParserStream {
    private Reader in;

    public JSONParserReader(int permissiveMode) {
        super(permissiveMode);
    }

    public Object parse(Reader in) throws ParseException {
        return parse(in, JSONValue.defaultReader.DEFAULT);
    }

    public <T> T parse(Reader in, JsonReaderI<T> mapper) throws ParseException {
        this.base = mapper.base;
        this.in = in;
        return (T) super.parse(mapper);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // net.minidev.json.parser.JSONParserBase
    public void read() throws IOException {
        int i = this.in.read();
        this.c = i == -1 ? (char) 26 : (char) i;
        this.pos++;
    }

    @Override // net.minidev.json.parser.JSONParserBase
    protected void readS() throws IOException {
        this.sb.append(this.c);
        int i = this.in.read();
        if (i == -1) {
            this.c = (char) 26;
            return;
        }
        this.c = (char) i;
        this.pos++;
    }

    @Override // net.minidev.json.parser.JSONParserBase
    protected void readNoEnd() throws ParseException, IOException {
        int i = this.in.read();
        if (i == -1) {
            throw new ParseException(this.pos - 1, 3, "EOF");
        }
        this.c = (char) i;
    }
}
