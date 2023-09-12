package com.jayway.jsonpath;

import com.j256.ormlite.stmt.query.SimpleComparison;
import com.jayway.jsonpath.Predicate;
import com.jayway.jsonpath.internal.Path;
import com.jayway.jsonpath.internal.PathCompiler;
import com.jayway.jsonpath.internal.Utils;
import com.jayway.jsonpath.internal.token.PredicateContextImpl;
import com.microsoft.kapp.utils.Constants;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/* loaded from: classes.dex */
public class Criteria implements Predicate {
    private final List<Criteria> criteriaChain;
    private CriteriaType criteriaType;
    private Object left;
    private Object right;
    private static final Logger logger = LoggerFactory.getLogger(Criteria.class);
    private static final String[] OPERATORS = {CriteriaType.EQ.toString(), CriteriaType.GTE.toString(), CriteriaType.LTE.toString(), CriteriaType.NE.toString(), CriteriaType.LT.toString(), CriteriaType.GT.toString(), CriteriaType.REGEX.toString()};

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public enum CriteriaType {
        EQ { // from class: com.jayway.jsonpath.Criteria.CriteriaType.1
            @Override // com.jayway.jsonpath.Criteria.CriteriaType
            boolean eval(Object expected, Object model, Predicate.PredicateContext ctx) {
                boolean res = Criteria.safeCompare(expected, model) == 0;
                if (Criteria.logger.isDebugEnabled()) {
                    Criteria.logger.debug("[{}] {} [{}] => {}", model, name(), expected, Boolean.valueOf(res));
                }
                return res;
            }

            @Override // java.lang.Enum
            public String toString() {
                return "==";
            }
        },
        NE { // from class: com.jayway.jsonpath.Criteria.CriteriaType.2
            @Override // com.jayway.jsonpath.Criteria.CriteriaType
            boolean eval(Object expected, Object model, Predicate.PredicateContext ctx) {
                boolean res = Criteria.safeCompare(expected, model) != 0;
                if (Criteria.logger.isDebugEnabled()) {
                    Criteria.logger.debug("[{}] {} [{}] => {}", model, name(), expected, Boolean.valueOf(res));
                }
                return res;
            }

            @Override // java.lang.Enum
            public String toString() {
                return "!=";
            }
        },
        GT { // from class: com.jayway.jsonpath.Criteria.CriteriaType.3
            @Override // com.jayway.jsonpath.Criteria.CriteriaType
            boolean eval(Object expected, Object model, Predicate.PredicateContext ctx) {
                if ((model == null) ^ (expected == null)) {
                    return false;
                }
                boolean res = Criteria.safeCompare(expected, model) < 0;
                if (Criteria.logger.isDebugEnabled()) {
                    Criteria.logger.debug("[{}] {} [{}] => {}", model, name(), expected, Boolean.valueOf(res));
                }
                return res;
            }

            @Override // java.lang.Enum
            public String toString() {
                return SimpleComparison.GREATER_THAN_OPERATION;
            }
        },
        GTE { // from class: com.jayway.jsonpath.Criteria.CriteriaType.4
            @Override // com.jayway.jsonpath.Criteria.CriteriaType
            boolean eval(Object expected, Object model, Predicate.PredicateContext ctx) {
                if ((model == null) ^ (expected == null)) {
                    return false;
                }
                boolean res = Criteria.safeCompare(expected, model) <= 0;
                if (Criteria.logger.isDebugEnabled()) {
                    Criteria.logger.debug("[{}] {} [{}] => {}", model, name(), expected, Boolean.valueOf(res));
                }
                return res;
            }

            @Override // java.lang.Enum
            public String toString() {
                return SimpleComparison.GREATER_THAN_EQUAL_TO_OPERATION;
            }
        },
        LT { // from class: com.jayway.jsonpath.Criteria.CriteriaType.5
            @Override // com.jayway.jsonpath.Criteria.CriteriaType
            boolean eval(Object expected, Object model, Predicate.PredicateContext ctx) {
                if ((model == null) ^ (expected == null)) {
                    return false;
                }
                boolean res = Criteria.safeCompare(expected, model) > 0;
                if (Criteria.logger.isDebugEnabled()) {
                    Criteria.logger.debug("[{}] {} [{}] => {}", model, name(), expected, Boolean.valueOf(res));
                }
                return res;
            }

            @Override // java.lang.Enum
            public String toString() {
                return SimpleComparison.LESS_THAN_OPERATION;
            }
        },
        LTE { // from class: com.jayway.jsonpath.Criteria.CriteriaType.6
            @Override // com.jayway.jsonpath.Criteria.CriteriaType
            boolean eval(Object expected, Object model, Predicate.PredicateContext ctx) {
                if ((model == null) ^ (expected == null)) {
                    return false;
                }
                boolean res = Criteria.safeCompare(expected, model) >= 0;
                if (Criteria.logger.isDebugEnabled()) {
                    Criteria.logger.debug("[{}] {} [{}] => {}", model, name(), expected, Boolean.valueOf(res));
                }
                return res;
            }

            @Override // java.lang.Enum
            public String toString() {
                return SimpleComparison.LESS_THAN_EQUAL_TO_OPERATION;
            }
        },
        IN { // from class: com.jayway.jsonpath.Criteria.CriteriaType.7
            @Override // com.jayway.jsonpath.Criteria.CriteriaType
            boolean eval(Object expected, Object model, Predicate.PredicateContext ctx) {
                boolean res = false;
                Collection exps = (Collection) expected;
                Iterator it = exps.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    Object exp = it.next();
                    if (Criteria.safeCompare(exp, model) == 0) {
                        res = true;
                        break;
                    }
                }
                if (Criteria.logger.isDebugEnabled()) {
                    Criteria.logger.debug("[{}] {} [{}] => {}", model, name(), Utils.join(", ", exps), Boolean.valueOf(res));
                }
                return res;
            }
        },
        NIN { // from class: com.jayway.jsonpath.Criteria.CriteriaType.8
            @Override // com.jayway.jsonpath.Criteria.CriteriaType
            boolean eval(Object expected, Object model, Predicate.PredicateContext ctx) {
                Collection nexps = (Collection) expected;
                boolean res = !nexps.contains(model);
                if (Criteria.logger.isDebugEnabled()) {
                    Criteria.logger.debug("[{}] {} [{}] => {}", model, name(), Utils.join(", ", nexps), Boolean.valueOf(res));
                }
                return res;
            }
        },
        CONTAINS { // from class: com.jayway.jsonpath.Criteria.CriteriaType.9
            @Override // com.jayway.jsonpath.Criteria.CriteriaType
            boolean eval(Object expected, Object model, Predicate.PredicateContext ctx) {
                boolean res = false;
                if (ctx.configuration().jsonProvider().isArray(model)) {
                    Iterator<?> it = ctx.configuration().jsonProvider().toIterable(model).iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        Object o = it.next();
                        if (expected.equals(o)) {
                            res = true;
                            break;
                        }
                    }
                } else if (model instanceof String) {
                    res = (Criteria.isNullish(expected) || !(expected instanceof String)) ? false : ((String) model).contains((String) expected);
                }
                if (Criteria.logger.isDebugEnabled()) {
                    Criteria.logger.debug("[{}] {} [{}] => {}", model, name(), expected, Boolean.valueOf(res));
                }
                return res;
            }
        },
        ALL { // from class: com.jayway.jsonpath.Criteria.CriteriaType.10
            @Override // com.jayway.jsonpath.Criteria.CriteriaType
            boolean eval(Object expected, Object model, Predicate.PredicateContext ctx) {
                boolean res = true;
                Collection exps = (Collection) expected;
                if (ctx.configuration().jsonProvider().isArray(model)) {
                    Iterator it = exps.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        Object exp = it.next();
                        boolean found = false;
                        Iterator<?> it2 = ctx.configuration().jsonProvider().toIterable(model).iterator();
                        while (true) {
                            if (it2.hasNext()) {
                                Object check = it2.next();
                                if (Criteria.safeCompare(exp, check) == 0) {
                                    found = true;
                                    continue;
                                    break;
                                }
                            }
                        }
                        if (!found) {
                            res = false;
                            break;
                        }
                    }
                    if (Criteria.logger.isDebugEnabled()) {
                        Criteria.logger.debug("[{}] {} [{}] => {}", Utils.join(", ", ctx.configuration().jsonProvider().toIterable(model)), name(), Utils.join(", ", exps), Boolean.valueOf(res));
                    }
                } else {
                    res = false;
                    if (Criteria.logger.isDebugEnabled()) {
                        Criteria.logger.debug("[{}] {} [{}] => {}", "<NOT AN ARRAY>", name(), Utils.join(", ", exps), false);
                    }
                }
                return res;
            }
        },
        SIZE { // from class: com.jayway.jsonpath.Criteria.CriteriaType.11
            @Override // com.jayway.jsonpath.Criteria.CriteriaType
            boolean eval(Object expected, Object model, Predicate.PredicateContext ctx) {
                boolean res;
                int size = ((Integer) expected).intValue();
                if (ctx.configuration().jsonProvider().isArray(model)) {
                    int length = ctx.configuration().jsonProvider().length(model);
                    res = length == size;
                    if (Criteria.logger.isDebugEnabled()) {
                        Criteria.logger.debug("Array with size {} {} {} => {}", Integer.valueOf(length), name(), Integer.valueOf(size), Boolean.valueOf(res));
                    }
                } else if (model instanceof String) {
                    int length2 = ((String) model).length();
                    res = length2 == size;
                    if (Criteria.logger.isDebugEnabled()) {
                        Criteria.logger.debug("String with length {} {} {} => {}", Integer.valueOf(length2), name(), Integer.valueOf(size), Boolean.valueOf(res));
                    }
                } else {
                    res = false;
                    if (Criteria.logger.isDebugEnabled()) {
                        Logger logger = Criteria.logger;
                        Object[] objArr = new Object[4];
                        objArr[0] = model == null ? "null" : model.getClass().getName();
                        objArr[1] = name();
                        objArr[2] = Integer.valueOf(size);
                        objArr[3] = false;
                        logger.debug("{} {} {} => {}", objArr);
                    }
                }
                return res;
            }
        },
        EXISTS { // from class: com.jayway.jsonpath.Criteria.CriteriaType.12
            @Override // com.jayway.jsonpath.Criteria.CriteriaType
            boolean eval(Object expected, Object model, Predicate.PredicateContext ctx) {
                throw new UnsupportedOperationException();
            }
        },
        TYPE { // from class: com.jayway.jsonpath.Criteria.CriteriaType.13
            @Override // com.jayway.jsonpath.Criteria.CriteriaType
            boolean eval(Object expected, Object model, Predicate.PredicateContext ctx) {
                Class<?> expType = (Class) expected;
                Class<?> actType = model == null ? null : model.getClass();
                return actType != null && expType.isAssignableFrom(actType);
            }
        },
        REGEX { // from class: com.jayway.jsonpath.Criteria.CriteriaType.14
            @Override // com.jayway.jsonpath.Criteria.CriteriaType
            boolean eval(Object expected, Object model, Predicate.PredicateContext ctx) {
                Pattern pattern;
                Object target;
                boolean res = false;
                if (model instanceof Pattern) {
                    pattern = (Pattern) model;
                    target = expected;
                } else {
                    pattern = (Pattern) expected;
                    target = model;
                }
                if (target != null) {
                    res = pattern.matcher(target.toString()).matches();
                }
                if (Criteria.logger.isDebugEnabled()) {
                    Logger logger = Criteria.logger;
                    Object[] objArr = new Object[4];
                    objArr[0] = model == null ? "null" : model.toString();
                    objArr[1] = name();
                    objArr[2] = expected == null ? "null" : expected.toString();
                    objArr[3] = Boolean.valueOf(res);
                    logger.debug("[{}] {} [{}] => {}", objArr);
                }
                return res;
            }

            @Override // java.lang.Enum
            public String toString() {
                return "=~";
            }
        },
        MATCHES { // from class: com.jayway.jsonpath.Criteria.CriteriaType.15
            @Override // com.jayway.jsonpath.Criteria.CriteriaType
            boolean eval(Object expected, Object model, Predicate.PredicateContext ctx) {
                PredicateContextImpl pci = (PredicateContextImpl) ctx;
                Predicate exp = (Predicate) expected;
                return exp.apply(new PredicateContextImpl(model, ctx.root(), ctx.configuration(), pci.documentPathCache()));
            }
        },
        NOT_EMPTY { // from class: com.jayway.jsonpath.Criteria.CriteriaType.16
            @Override // com.jayway.jsonpath.Criteria.CriteriaType
            boolean eval(Object expected, Object model, Predicate.PredicateContext ctx) {
                boolean res = false;
                if (model != null) {
                    if (ctx.configuration().jsonProvider().isArray(model)) {
                        int len = ctx.configuration().jsonProvider().length(model);
                        res = len != 0;
                        if (Criteria.logger.isDebugEnabled()) {
                            Criteria.logger.debug("array length = {} {} => {}", Integer.valueOf(len), name(), Boolean.valueOf(res));
                        }
                    } else if (model instanceof String) {
                        int len2 = ((String) model).length();
                        res = len2 != 0;
                        if (Criteria.logger.isDebugEnabled()) {
                            Criteria.logger.debug("string length = {} {} => {}", Integer.valueOf(len2), name(), Boolean.valueOf(res));
                        }
                    }
                }
                return res;
            }
        };

        abstract boolean eval(Object obj, Object obj2, Predicate.PredicateContext predicateContext);

        public static CriteriaType parse(String str) {
            if ("==".equals(str)) {
                return EQ;
            }
            if (SimpleComparison.GREATER_THAN_OPERATION.equals(str)) {
                return GT;
            }
            if (SimpleComparison.GREATER_THAN_EQUAL_TO_OPERATION.equals(str)) {
                return GTE;
            }
            if (SimpleComparison.LESS_THAN_OPERATION.equals(str)) {
                return LT;
            }
            if (SimpleComparison.LESS_THAN_EQUAL_TO_OPERATION.equals(str)) {
                return LTE;
            }
            if ("!=".equals(str)) {
                return NE;
            }
            if ("=~".equals(str)) {
                return REGEX;
            }
            throw new UnsupportedOperationException("CriteriaType " + str + " can not be parsed");
        }
    }

    private Criteria(List<Criteria> criteriaChain, Object left) {
        this.left = left;
        this.criteriaChain = criteriaChain;
        this.criteriaChain.add(this);
    }

    private Criteria(Object left) {
        this(new LinkedList(), left);
    }

    private Criteria(Object left, CriteriaType criteriaType, Object right) {
        this(new LinkedList(), left);
        this.criteriaType = criteriaType;
        this.right = right;
    }

    @Override // com.jayway.jsonpath.Predicate
    public boolean apply(Predicate.PredicateContext ctx) {
        for (Criteria criteria : this.criteriaChain) {
            if (!criteria.eval(ctx)) {
                return false;
            }
        }
        return true;
    }

    private Object evaluateIfPath(Object target, Predicate.PredicateContext ctx) {
        Object res = target;
        if (res instanceof Path) {
            Path leftPath = (Path) target;
            if (ctx instanceof PredicateContextImpl) {
                PredicateContextImpl ctxi = (PredicateContextImpl) ctx;
                res = ctxi.evaluate(leftPath);
            } else {
                Object doc = leftPath.isRootPath() ? ctx.root() : ctx.item();
                res = leftPath.evaluate(doc, ctx.root(), ctx.configuration()).getValue();
            }
        }
        if (res == null) {
            return null;
        }
        return ctx.configuration().jsonProvider().unwrap(res);
    }

    private boolean eval(Predicate.PredicateContext ctx) {
        if (CriteriaType.EXISTS == this.criteriaType) {
            boolean exists = ((Boolean) this.right).booleanValue();
            try {
                Configuration c = Configuration.builder().jsonProvider(ctx.configuration().jsonProvider()).options(Option.REQUIRE_PROPERTIES).build();
                Object value = ((Path) this.left).evaluate(ctx.item(), ctx.root(), c).getValue();
                if (exists) {
                    return value != null;
                }
                return value == null;
            } catch (PathNotFoundException e) {
                return !exists;
            }
        }
        try {
            Object leftVal = evaluateIfPath(this.left, ctx);
            Object rightVal = evaluateIfPath(this.right, ctx);
            return this.criteriaType.eval(rightVal, leftVal, ctx);
        } catch (PathNotFoundException e2) {
            return false;
        } catch (ValueCompareException e3) {
            return false;
        }
    }

    public static Criteria where(Path key) {
        return new Criteria(key);
    }

    public static Criteria where(String key) {
        if (!key.startsWith("$") && !key.startsWith(Constants.CHAR_AT)) {
            key = "@." + key;
        }
        return where(PathCompiler.compile(key, new Predicate[0]));
    }

    public Criteria and(String key) {
        if (!key.startsWith("$") && !key.startsWith(Constants.CHAR_AT)) {
            key = "@." + key;
        }
        return new Criteria(this.criteriaChain, PathCompiler.compile(key, new Predicate[0]));
    }

    public Criteria is(Object o) {
        this.criteriaType = CriteriaType.EQ;
        this.right = o;
        return this;
    }

    public Criteria eq(Object o) {
        return is(o);
    }

    public Criteria ne(Object o) {
        this.criteriaType = CriteriaType.NE;
        this.right = o;
        return this;
    }

    public Criteria lt(Object o) {
        this.criteriaType = CriteriaType.LT;
        this.right = o;
        return this;
    }

    public Criteria lte(Object o) {
        this.criteriaType = CriteriaType.LTE;
        this.right = o;
        return this;
    }

    public Criteria gt(Object o) {
        this.criteriaType = CriteriaType.GT;
        this.right = o;
        return this;
    }

    public Criteria gte(Object o) {
        this.criteriaType = CriteriaType.GTE;
        this.right = o;
        return this;
    }

    public Criteria regex(Pattern pattern) {
        Utils.notNull(pattern, "pattern can not be null", new Object[0]);
        this.criteriaType = CriteriaType.REGEX;
        this.right = pattern;
        return this;
    }

    public Criteria in(Object... o) {
        return in(Arrays.asList(o));
    }

    public Criteria in(Collection<?> c) {
        Utils.notNull(c, "collection can not be null", new Object[0]);
        this.criteriaType = CriteriaType.IN;
        this.right = c;
        return this;
    }

    public Criteria contains(Object o) {
        this.criteriaType = CriteriaType.CONTAINS;
        this.right = o;
        return this;
    }

    public Criteria nin(Object... o) {
        return nin(Arrays.asList(o));
    }

    public Criteria nin(Collection<?> c) {
        Utils.notNull(c, "collection can not be null", new Object[0]);
        this.criteriaType = CriteriaType.NIN;
        this.right = c;
        return this;
    }

    public Criteria all(Object... o) {
        return all(Arrays.asList(o));
    }

    public Criteria all(Collection<?> c) {
        Utils.notNull(c, "collection can not be null", new Object[0]);
        this.criteriaType = CriteriaType.ALL;
        this.right = c;
        return this;
    }

    public Criteria size(int size) {
        this.criteriaType = CriteriaType.SIZE;
        this.right = Integer.valueOf(size);
        return this;
    }

    public Criteria exists(boolean b) {
        this.criteriaType = CriteriaType.EXISTS;
        this.right = Boolean.valueOf(b);
        return this;
    }

    public Criteria type(Class<?> t) {
        Utils.notNull(t, "type can not be null", new Object[0]);
        this.criteriaType = CriteriaType.TYPE;
        this.right = t;
        return this;
    }

    public Criteria notEmpty() {
        this.criteriaType = CriteriaType.NOT_EMPTY;
        this.right = null;
        return this;
    }

    public Criteria matches(Predicate p) {
        this.criteriaType = CriteriaType.MATCHES;
        this.right = p;
        return this;
    }

    private static boolean isPath(String string) {
        return string != null && (string.startsWith("$") || string.startsWith(Constants.CHAR_AT) || string.startsWith("!@"));
    }

    private static boolean isString(String string) {
        return string != null && !string.isEmpty() && string.charAt(0) == '\'' && string.charAt(string.length() + (-1)) == '\'';
    }

    private static boolean isPattern(String string) {
        if (string == null || string.isEmpty() || string.charAt(0) != '/') {
            return false;
        }
        return string.charAt(string.length() + (-1)) == '/' || (string.charAt(string.length() + (-2)) == '/' && string.charAt(string.length() + (-1)) == 'i');
    }

    private static Pattern compilePattern(String string) {
        int lastIndex = string.lastIndexOf(47);
        boolean ignoreCase = string.endsWith("i");
        String regex = string.substring(1, lastIndex);
        int flags = ignoreCase ? 2 : 0;
        return Pattern.compile(regex, flags);
    }

    public static Criteria parse(String criteria) {
        String left;
        int operatorIndex = -1;
        String operator = "";
        String right = "";
        int y = 0;
        while (true) {
            if (y >= OPERATORS.length) {
                break;
            }
            operatorIndex = criteria.indexOf(OPERATORS[y]);
            if (operatorIndex == -1) {
                y++;
            } else {
                operator = OPERATORS[y];
                break;
            }
        }
        if (!operator.isEmpty()) {
            left = criteria.substring(0, operatorIndex).trim();
            right = criteria.substring(operator.length() + operatorIndex).trim();
        } else {
            left = criteria.trim();
        }
        return create(left, operator, right);
    }

    public static Criteria create(String left, String operator, String right) {
        Object obj = left;
        Object obj2 = right;
        Path leftPath = null;
        boolean existsCheck = true;
        if (isPath(left)) {
            if (left.charAt(0) == '!') {
                existsCheck = false;
                left = left.substring(1);
            }
            leftPath = PathCompiler.compile(left, new Predicate[0]);
            if (!leftPath.isDefinite()) {
                throw new InvalidPathException("the predicate path: " + left + " is not definite");
            }
            obj = leftPath;
        } else if (isString(left)) {
            obj = left.substring(1, left.length() - 1);
        } else if (isPattern(left)) {
            obj = compilePattern(left);
        }
        if (isPath(right)) {
            if (right.charAt(0) == '!') {
                throw new InvalidPathException("Invalid negation! Can only be used for existence check e.g [?(!@.foo)]");
            }
            Path rightPath = PathCompiler.compile(right, new Predicate[0]);
            if (!rightPath.isDefinite()) {
                throw new InvalidPathException("the predicate path: " + right + " is not definite");
            }
            obj2 = rightPath;
        } else if (isString(right)) {
            obj2 = right.substring(1, right.length() - 1);
        } else if (isPattern(right)) {
            obj2 = compilePattern(right);
        }
        if (leftPath != null && operator.isEmpty()) {
            return where(leftPath).exists(existsCheck);
        }
        return new Criteria(obj, CriteriaType.parse(operator), obj2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int safeCompare(Object left, Object right) throws ValueCompareException {
        if (left == right) {
            return 0;
        }
        boolean leftNullish = isNullish(left);
        boolean rightNullish = isNullish(right);
        if (leftNullish && !rightNullish) {
            return -1;
        }
        if (!leftNullish && rightNullish) {
            return 1;
        }
        if (leftNullish && rightNullish) {
            return 0;
        }
        if ((left instanceof String) && (right instanceof String)) {
            String exp = (String) left;
            if (exp.contains("'")) {
                exp = exp.replace("\\'", "'");
            }
            return exp.compareTo((String) right);
        } else if ((left instanceof Number) && (right instanceof Number)) {
            return new BigDecimal(left.toString()).compareTo(new BigDecimal(right.toString()));
        } else {
            if ((left instanceof String) && (right instanceof Number)) {
                return new BigDecimal(left.toString()).compareTo(new BigDecimal(right.toString()));
            }
            if ((left instanceof String) && (right instanceof Boolean)) {
                Boolean e = Boolean.valueOf((String) left);
                Boolean a = (Boolean) right;
                return e.compareTo(a);
            } else if ((left instanceof Boolean) && (right instanceof Boolean)) {
                Boolean e2 = (Boolean) left;
                Boolean a2 = (Boolean) right;
                return e2.compareTo(a2);
            } else {
                logger.debug("Can not compare a {} with a {}", left.getClass().getName(), right.getClass().getName());
                throw new ValueCompareException();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isNullish(Object o) {
        return o == null || ((o instanceof String) && "null".equals(o));
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.left.toString()).append(this.criteriaType.toString()).append(wrapString(this.right));
        return sb.toString();
    }

    private static String wrapString(Object o) {
        if (o == null) {
            return "null";
        }
        if (o instanceof String) {
            return "'" + o.toString() + "'";
        }
        return o.toString();
    }
}
