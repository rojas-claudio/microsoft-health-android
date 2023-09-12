package net.minidev.json.parser;

import net.minidev.json.JSONValue;
import net.minidev.json.writer.JsonReaderI;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class JSONParserString extends JSONParserMemory {
    private String in;

    public JSONParserString(int permissiveMode) {
        super(permissiveMode);
    }

    public Object parse(String in) throws ParseException {
        return parse(in, JSONValue.defaultReader.DEFAULT);
    }

    public <T> T parse(String in, JsonReaderI<T> mapper) throws ParseException {
        this.base = mapper.base;
        this.in = in;
        this.len = in.length();
        return (T) parse(mapper);
    }

    @Override // net.minidev.json.parser.JSONParserMemory
    protected void extractString(int beginIndex, int endIndex) {
        this.xs = this.in.substring(beginIndex, endIndex);
    }

    @Override // net.minidev.json.parser.JSONParserMemory
    protected void extractStringTrim(int start, int stop) {
        extractString(start, stop);
        this.xs = this.xs.trim();
    }

    @Override // net.minidev.json.parser.JSONParserMemory
    protected int indexOf(char c, int pos) {
        return this.in.indexOf(c, pos);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // net.minidev.json.parser.JSONParserBase
    public void read() {
        int i = this.pos + 1;
        this.pos = i;
        if (i >= this.len) {
            this.c = (char) 26;
        } else {
            this.c = this.in.charAt(this.pos);
        }
    }

    @Override // net.minidev.json.parser.JSONParserBase
    protected void readS() {
        int i = this.pos + 1;
        this.pos = i;
        if (i >= this.len) {
            this.c = (char) 26;
        } else {
            this.c = this.in.charAt(this.pos);
        }
    }

    @Override // net.minidev.json.parser.JSONParserBase
    protected void readNoEnd() throws ParseException {
        int i = this.pos + 1;
        this.pos = i;
        if (i >= this.len) {
            this.c = (char) 26;
            throw new ParseException(this.pos - 1, 3, "EOF");
        } else {
            this.c = this.in.charAt(this.pos);
        }
    }
}
