package com.jayway.jsonpath.internal;

import com.jayway.jsonpath.Filter;
import com.jayway.jsonpath.InvalidPathException;
import com.jayway.jsonpath.Predicate;
import com.jayway.jsonpath.internal.token.ArrayPathToken;
import com.jayway.jsonpath.internal.token.PathToken;
import com.jayway.jsonpath.internal.token.PredicatePathToken;
import com.jayway.jsonpath.internal.token.PropertyPathToken;
import com.jayway.jsonpath.internal.token.RootPathToken;
import com.jayway.jsonpath.internal.token.ScanPathToken;
import com.jayway.jsonpath.internal.token.WildcardPathToken;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/* loaded from: classes.dex */
public class PathCompiler {
    private static final char ANY = '*';
    private static final char BRACKET_CLOSE = ']';
    private static final char BRACKET_OPEN = '[';
    private static final char DOCUMENT = '$';
    private static final char PERIOD = '.';
    private static final String PROPERTY_CLOSE = "']";
    private static final String PROPERTY_OPEN = "['";
    private static final char SPACE = ' ';
    private static final Logger logger = LoggerFactory.getLogger(PathCompiler.class);
    private static final Cache cache = new Cache(200);

    /* JADX WARN: Removed duplicated region for block: B:41:0x012d A[Catch: Exception -> 0x001f, TryCatch #0 {Exception -> 0x001f, blocks: (B:3:0x0009, B:5:0x0016, B:6:0x001e, B:10:0x0026, B:12:0x0038, B:14:0x0041, B:15:0x0055, B:18:0x005f, B:20:0x0068, B:21:0x0081, B:23:0x0088, B:25:0x0091, B:27:0x009a, B:28:0x00b3, B:30:0x00b6, B:32:0x00d7, B:34:0x00df, B:37:0x00fe, B:38:0x0102, B:39:0x0105, B:41:0x012d, B:42:0x0133, B:44:0x0139, B:62:0x01bc, B:45:0x0145, B:46:0x014d, B:47:0x014e, B:48:0x0154, B:49:0x0160, B:51:0x016a, B:52:0x0170, B:56:0x0179, B:58:0x0181, B:59:0x0189, B:60:0x018b, B:61:0x01b0), top: B:64:0x0009 }] */
    /* JADX WARN: Removed duplicated region for block: B:62:0x01bc A[Catch: Exception -> 0x001f, TRY_LEAVE, TryCatch #0 {Exception -> 0x001f, blocks: (B:3:0x0009, B:5:0x0016, B:6:0x001e, B:10:0x0026, B:12:0x0038, B:14:0x0041, B:15:0x0055, B:18:0x005f, B:20:0x0068, B:21:0x0081, B:23:0x0088, B:25:0x0091, B:27:0x009a, B:28:0x00b3, B:30:0x00b6, B:32:0x00d7, B:34:0x00df, B:37:0x00fe, B:38:0x0102, B:39:0x0105, B:41:0x012d, B:42:0x0133, B:44:0x0139, B:62:0x01bc, B:45:0x0145, B:46:0x014d, B:47:0x014e, B:48:0x0154, B:49:0x0160, B:51:0x016a, B:52:0x0170, B:56:0x0179, B:58:0x0181, B:59:0x0189, B:60:0x018b, B:61:0x01b0), top: B:64:0x0009 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static com.jayway.jsonpath.internal.Path compile(java.lang.String r14, com.jayway.jsonpath.Predicate... r15) {
        /*
            Method dump skipped, instructions count: 476
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jayway.jsonpath.internal.PathCompiler.compile(java.lang.String, com.jayway.jsonpath.Predicate[]):com.jayway.jsonpath.internal.Path");
    }

    private static void assertValidFieldChars(String s, int start, int positions) {
    }

    private static int fastForward(String s, int index) {
        char current;
        int skipCount = 0;
        while (index < s.length() && (current = s.charAt(index)) != '.' && current != '[' && current != ' ') {
            index++;
            skipCount++;
        }
        return skipCount;
    }

    private static int fastForwardUntilClosed(String s, int index) {
        int nestedBrackets = 0;
        int index2 = index + 1;
        int skipCount = 0 + 1;
        while (index2 < s.length()) {
            char current = s.charAt(index2);
            index2++;
            skipCount++;
            if (current == ']' && nestedBrackets == 0) {
                break;
            }
            if (current == '[') {
                nestedBrackets++;
            }
            if (current == ']') {
                nestedBrackets--;
            }
        }
        return skipCount;
    }

    /* loaded from: classes.dex */
    static class PathComponentAnalyzer {
        static final /* synthetic */ boolean $assertionsDisabled;
        private static final Pattern FILTER_PATTERN;
        private char current;
        private final LinkedList<Predicate> filterList;
        private int i;
        private final String pathFragment;

        static {
            $assertionsDisabled = !PathCompiler.class.desiredAssertionStatus();
            FILTER_PATTERN = Pattern.compile("^\\[\\s*\\?\\s*[,\\s*\\?]*?\\s*]$");
        }

        PathComponentAnalyzer(String pathFragment, LinkedList<Predicate> filterList) {
            this.pathFragment = pathFragment;
            this.filterList = filterList;
        }

        static PathToken analyze(String pathFragment, LinkedList<Predicate> filterList) {
            return new PathComponentAnalyzer(pathFragment, filterList).analyze();
        }

        public PathToken analyze() {
            if ("$".equals(this.pathFragment)) {
                return new RootPathToken();
            }
            if ("..".equals(this.pathFragment)) {
                return new ScanPathToken();
            }
            if (!"[*]".equals(this.pathFragment) && !".*".equals(this.pathFragment)) {
                if ("[?]".equals(this.pathFragment)) {
                    return new PredicatePathToken(this.filterList.poll());
                }
                if (FILTER_PATTERN.matcher(this.pathFragment).matches()) {
                    int criteriaCount = Utils.countMatches(this.pathFragment, "?");
                    List<Predicate> filters = new ArrayList<>(criteriaCount);
                    for (int i = 0; i < criteriaCount; i++) {
                        filters.add(this.filterList.poll());
                    }
                    return new PredicatePathToken(filters);
                }
                this.i = 0;
                do {
                    this.current = this.pathFragment.charAt(this.i);
                    switch (this.current) {
                        case '\'':
                            return analyzeProperty();
                        case '?':
                            return analyzeCriteriaSequence4();
                        default:
                            if (Character.isDigit(this.current) || this.current == ':' || this.current == '-' || this.current == '@') {
                                return analyzeArraySequence();
                            }
                            this.i++;
                            break;
                            break;
                    }
                } while (this.i < this.pathFragment.length());
                throw new InvalidPathException("Could not analyze path component: " + this.pathFragment);
            }
            return new WildcardPathToken();
        }

        public PathToken analyzeCriteriaSequence4() {
            int[] bounds = findFilterBounds();
            this.i = bounds[1];
            return new PredicatePathToken(Filter.parse(this.pathFragment.substring(bounds[0], bounds[1])));
        }

        int[] findFilterBounds() {
            int end = 0;
            int start = this.i;
            while (this.pathFragment.charAt(start) != '[') {
                start--;
            }
            int mem = 32;
            int curr = start;
            boolean inProp = false;
            int openSquareBracket = 0;
            int openBrackets = 0;
            while (end == 0) {
                int c = this.pathFragment.charAt(curr);
                switch (c) {
                    case 39:
                        if (mem != 92) {
                            if (!inProp) {
                                inProp = true;
                                break;
                            } else {
                                inProp = false;
                                break;
                            }
                        } else {
                            break;
                        }
                    case 40:
                        if (!inProp) {
                            openBrackets++;
                            break;
                        } else {
                            break;
                        }
                    case 41:
                        if (!inProp) {
                            openBrackets--;
                            break;
                        } else {
                            break;
                        }
                    case 91:
                        if (!inProp) {
                            openSquareBracket++;
                            break;
                        } else {
                            break;
                        }
                    case 93:
                        if (!inProp) {
                            openSquareBracket--;
                            if (openBrackets != 0) {
                                break;
                            } else {
                                end = curr + 1;
                                break;
                            }
                        } else {
                            break;
                        }
                }
                mem = c;
                curr++;
            }
            if (openBrackets == 0 && openSquareBracket == 0) {
                return new int[]{start, end};
            }
            throw new InvalidPathException("Filter brackets are not balanced");
        }

        private PathToken analyzeProperty() {
            List<String> properties = new ArrayList<>();
            StringBuilder buffer = new StringBuilder();
            boolean propertyIsOpen = false;
            while (this.current != ']') {
                switch (this.current) {
                    case '\'':
                        if (propertyIsOpen) {
                            properties.add(buffer.toString());
                            buffer.setLength(0);
                            propertyIsOpen = false;
                            break;
                        } else {
                            propertyIsOpen = true;
                            break;
                        }
                    default:
                        if (!propertyIsOpen) {
                            break;
                        } else {
                            buffer.append(this.current);
                            break;
                        }
                }
                String str = this.pathFragment;
                int i = this.i + 1;
                this.i = i;
                this.current = str.charAt(i);
            }
            return new PropertyPathToken(properties);
        }

        private PathToken analyzeArraySequence() {
            StringBuilder buffer = new StringBuilder();
            List<Integer> numbers = new ArrayList<>();
            boolean contextSize = this.current == '@';
            boolean sliceTo = false;
            boolean sliceFrom = false;
            boolean sliceBetween = false;
            boolean indexSequence = false;
            if (contextSize) {
                String str = this.pathFragment;
                int i = this.i + 1;
                this.i = i;
                this.current = str.charAt(i);
                String str2 = this.pathFragment;
                int i2 = this.i + 1;
                this.i = i2;
                this.current = str2.charAt(i2);
                while (this.current != '-') {
                    if (this.current == ' ' || this.current == '(' || this.current == ')') {
                        String str3 = this.pathFragment;
                        int i3 = this.i + 1;
                        this.i = i3;
                        this.current = str3.charAt(i3);
                    } else {
                        buffer.append(this.current);
                        String str4 = this.pathFragment;
                        int i4 = this.i + 1;
                        this.i = i4;
                        this.current = str4.charAt(i4);
                    }
                }
                String function = buffer.toString();
                buffer.setLength(0);
                if (!function.equals("size") && !function.equals("length")) {
                    throw new InvalidPathException("Invalid function: @." + function + ". Supported functions are: [(@.length - n)] and [(@.size() - n)]");
                }
                while (this.current != ')') {
                    if (this.current == ' ') {
                        String str5 = this.pathFragment;
                        int i5 = this.i + 1;
                        this.i = i5;
                        this.current = str5.charAt(i5);
                    } else {
                        buffer.append(this.current);
                        String str6 = this.pathFragment;
                        int i6 = this.i + 1;
                        this.i = i6;
                        this.current = str6.charAt(i6);
                    }
                }
            } else {
                while (true) {
                    if (Character.isDigit(this.current) || this.current == ',' || this.current == ' ' || this.current == ':' || this.current == '-') {
                        switch (this.current) {
                            case ' ':
                                break;
                            case ',':
                                numbers.add(Integer.valueOf(Integer.parseInt(buffer.toString())));
                                buffer.setLength(0);
                                indexSequence = true;
                                break;
                            case ':':
                                if (buffer.length() == 0) {
                                    sliceTo = true;
                                    String str7 = this.pathFragment;
                                    int i7 = this.i + 1;
                                    this.i = i7;
                                    this.current = str7.charAt(i7);
                                    while (true) {
                                        if (Character.isDigit(this.current) || this.current == ' ' || this.current == '-') {
                                            if (this.current != ' ') {
                                                buffer.append(this.current);
                                            }
                                            String str8 = this.pathFragment;
                                            int i8 = this.i + 1;
                                            this.i = i8;
                                            this.current = str8.charAt(i8);
                                        } else {
                                            numbers.add(Integer.valueOf(Integer.parseInt(buffer.toString())));
                                            buffer.setLength(0);
                                            break;
                                        }
                                    }
                                } else {
                                    numbers.add(Integer.valueOf(Integer.parseInt(buffer.toString())));
                                    buffer.setLength(0);
                                    String str9 = this.pathFragment;
                                    int i9 = this.i + 1;
                                    this.i = i9;
                                    this.current = str9.charAt(i9);
                                    while (true) {
                                        if (Character.isDigit(this.current) || this.current == ' ' || this.current == '-') {
                                            if (this.current != ' ') {
                                                buffer.append(this.current);
                                            }
                                            String str10 = this.pathFragment;
                                            int i10 = this.i + 1;
                                            this.i = i10;
                                            this.current = str10.charAt(i10);
                                        } else if (buffer.length() == 0) {
                                            sliceFrom = true;
                                            break;
                                        } else {
                                            sliceBetween = true;
                                            numbers.add(Integer.valueOf(Integer.parseInt(buffer.toString())));
                                            buffer.setLength(0);
                                            break;
                                        }
                                    }
                                }
                                break;
                            default:
                                buffer.append(this.current);
                                break;
                        }
                        if (this.current != ']') {
                            String str11 = this.pathFragment;
                            int i11 = this.i + 1;
                            this.i = i11;
                            this.current = str11.charAt(i11);
                        }
                    }
                }
            }
            if (buffer.length() > 0) {
                numbers.add(Integer.valueOf(Integer.parseInt(buffer.toString())));
            }
            boolean singleIndex = (numbers.size() != 1 || sliceTo || sliceFrom || contextSize) ? false : true;
            if (PathCompiler.logger.isTraceEnabled()) {
                PathCompiler.logger.debug("numbers are                : {}", numbers.toString());
                PathCompiler.logger.debug("sequence is singleNumber   : {}", Boolean.valueOf(singleIndex));
                PathCompiler.logger.debug("sequence is numberSequence : {}", Boolean.valueOf(indexSequence));
                PathCompiler.logger.debug("sequence is sliceFrom      : {}", Boolean.valueOf(sliceFrom));
                PathCompiler.logger.debug("sequence is sliceTo        : {}", Boolean.valueOf(sliceTo));
                PathCompiler.logger.debug("sequence is sliceBetween   : {}", Boolean.valueOf(sliceBetween));
                PathCompiler.logger.debug("sequence is contextFetch   : {}", Boolean.valueOf(contextSize));
                PathCompiler.logger.debug("---------------------------------------------");
            }
            ArrayPathToken.Operation operation = null;
            if (singleIndex) {
                operation = ArrayPathToken.Operation.SINGLE_INDEX;
            } else if (indexSequence) {
                operation = ArrayPathToken.Operation.INDEX_SEQUENCE;
            } else if (sliceFrom) {
                operation = ArrayPathToken.Operation.SLICE_FROM;
            } else if (sliceTo) {
                operation = ArrayPathToken.Operation.SLICE_TO;
            } else if (sliceBetween) {
                operation = ArrayPathToken.Operation.SLICE_BETWEEN;
            } else if (contextSize) {
                operation = ArrayPathToken.Operation.CONTEXT_SIZE;
            }
            if ($assertionsDisabled || operation != null) {
                return new ArrayPathToken(numbers, operation);
            }
            throw new AssertionError();
        }
    }
}
