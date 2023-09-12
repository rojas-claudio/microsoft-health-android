package net.minidev.json.parser;

import java.io.IOException;
/* loaded from: classes.dex */
abstract class JSONParserMemory extends JSONParserBase {
    protected int len;

    protected abstract void extractString(int i, int i2);

    protected abstract void extractStringTrim(int i, int i2);

    protected abstract int indexOf(char c, int i);

    public JSONParserMemory(int permissiveMode) {
        super(permissiveMode);
    }

    @Override // net.minidev.json.parser.JSONParserBase
    protected void readNQString(boolean[] stop) throws IOException {
        int start = this.pos;
        skipNQString(stop);
        extractStringTrim(start, this.pos);
    }

    @Override // net.minidev.json.parser.JSONParserBase
    protected Object readNumber(boolean[] stop) throws ParseException, IOException {
        int start = this.pos;
        read();
        skipDigits();
        if (this.c != '.' && this.c != 'E' && this.c != 'e') {
            skipSpace();
            if (this.c >= 0 && this.c < '~' && !stop[this.c] && this.c != 26) {
                skipNQString(stop);
                extractStringTrim(start, this.pos);
                if (!this.acceptNonQuote) {
                    throw new ParseException(this.pos, 1, this.xs);
                }
                return this.xs;
            }
            extractStringTrim(start, this.pos);
            return parseNumber(this.xs);
        }
        if (this.c == '.') {
            read();
            skipDigits();
        }
        if (this.c != 'E' && this.c != 'e') {
            skipSpace();
            if (this.c >= 0 && this.c < '~' && !stop[this.c] && this.c != 26) {
                skipNQString(stop);
                extractStringTrim(start, this.pos);
                if (!this.acceptNonQuote) {
                    throw new ParseException(this.pos, 1, this.xs);
                }
                return this.xs;
            }
            extractStringTrim(start, this.pos);
            return extractFloat();
        }
        this.sb.append('E');
        read();
        if (this.c == '+' || this.c == '-' || (this.c >= '0' && this.c <= '9')) {
            this.sb.append(this.c);
            read();
            skipDigits();
            skipSpace();
            if (this.c >= 0 && this.c < '~' && !stop[this.c] && this.c != 26) {
                skipNQString(stop);
                extractStringTrim(start, this.pos);
                if (!this.acceptNonQuote) {
                    throw new ParseException(this.pos, 1, this.xs);
                }
                return this.xs;
            }
            extractStringTrim(start, this.pos);
            return extractFloat();
        }
        skipNQString(stop);
        extractStringTrim(start, this.pos);
        if (!this.acceptNonQuote) {
            throw new ParseException(this.pos, 1, this.xs);
        }
        if (!this.acceptLeadinZero) {
            checkLeadinZero();
        }
        return this.xs;
    }

    @Override // net.minidev.json.parser.JSONParserBase
    protected void readString() throws ParseException, IOException {
        if (!this.acceptSimpleQuote && this.c == '\'') {
            if (this.acceptNonQuote) {
                readNQString(stopAll);
                return;
            }
            throw new ParseException(this.pos, 0, Character.valueOf(this.c));
        }
        int tmpP = indexOf(this.c, this.pos + 1);
        if (tmpP == -1) {
            throw new ParseException(this.len, 3, null);
        }
        extractString(this.pos + 1, tmpP);
        if (this.xs.indexOf(92) == -1) {
            checkControleChar();
            this.pos = tmpP;
            read();
            return;
        }
        this.sb.clear();
        readString2();
    }
}
