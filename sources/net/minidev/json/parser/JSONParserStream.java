package net.minidev.json.parser;

import java.io.IOException;
/* loaded from: classes.dex */
abstract class JSONParserStream extends JSONParserBase {
    public JSONParserStream(int permissiveMode) {
        super(permissiveMode);
    }

    @Override // net.minidev.json.parser.JSONParserBase
    protected void readNQString(boolean[] stop) throws IOException {
        this.sb.clear();
        skipNQString(stop);
        this.xs = this.sb.toString().trim();
    }

    @Override // net.minidev.json.parser.JSONParserBase
    protected Object readNumber(boolean[] stop) throws ParseException, IOException {
        this.sb.clear();
        this.sb.append(this.c);
        read();
        skipDigits();
        if (this.c != '.' && this.c != 'E' && this.c != 'e') {
            skipSpace();
            if (this.c >= 0 && this.c < '~' && !stop[this.c] && this.c != 26) {
                skipNQString(stop);
                this.xs = this.sb.toString().trim();
                if (!this.acceptNonQuote) {
                    throw new ParseException(this.pos, 1, this.xs);
                }
                return this.xs;
            }
            this.xs = this.sb.toString().trim();
            return parseNumber(this.xs);
        }
        if (this.c == '.') {
            this.sb.append(this.c);
            read();
            skipDigits();
        }
        if (this.c != 'E' && this.c != 'e') {
            skipSpace();
            if (this.c >= 0 && this.c < '~' && !stop[this.c] && this.c != 26) {
                skipNQString(stop);
                this.xs = this.sb.toString().trim();
                if (!this.acceptNonQuote) {
                    throw new ParseException(this.pos, 1, this.xs);
                }
                return this.xs;
            }
            this.xs = this.sb.toString().trim();
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
                this.xs = this.sb.toString().trim();
                if (!this.acceptNonQuote) {
                    throw new ParseException(this.pos, 1, this.xs);
                }
                return this.xs;
            }
            this.xs = this.sb.toString().trim();
            return extractFloat();
        }
        skipNQString(stop);
        this.xs = this.sb.toString().trim();
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
        this.sb.clear();
        readString2();
    }
}
