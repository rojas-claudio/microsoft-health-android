package com.jayway.jsonpath;

import com.jayway.jsonpath.Predicate;
import com.jayway.jsonpath.internal.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Stack;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/* loaded from: classes.dex */
public abstract class Filter implements Predicate {
    private static final String AND = "&&";
    private static final String OR = "||";
    private static final Logger logger = LoggerFactory.getLogger(Filter.class);
    private static final Pattern OPERATOR_SPLIT = Pattern.compile("((?<=&&|\\|\\|)|(?=&&|\\|\\|))");

    @Override // com.jayway.jsonpath.Predicate
    public abstract boolean apply(Predicate.PredicateContext predicateContext);

    public static Filter filter(Predicate predicate) {
        return new SingleFilter(predicate);
    }

    public static Filter filter(Collection<Predicate> predicates) {
        return new AndFilter(predicates);
    }

    public Filter or(Predicate other) {
        return new OrFilter(this, other);
    }

    public Filter and(Predicate other) {
        return new AndFilter(this, other);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class SingleFilter extends Filter {
        private final Predicate predicate;

        private SingleFilter(Predicate predicate) {
            this.predicate = predicate;
        }

        @Override // com.jayway.jsonpath.Filter, com.jayway.jsonpath.Predicate
        public boolean apply(Predicate.PredicateContext ctx) {
            return this.predicate.apply(ctx);
        }

        public String toString() {
            return this.predicate.toString();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class AndFilter extends Filter {
        private final Collection<Predicate> predicates;

        private AndFilter(Collection<Predicate> predicates) {
            this.predicates = predicates;
        }

        private AndFilter(Predicate left, Predicate right) {
            this(Arrays.asList(left, right));
        }

        @Override // com.jayway.jsonpath.Filter
        public Filter and(Predicate other) {
            Collection<Predicate> newPredicates = new ArrayList<>(this.predicates);
            newPredicates.add(other);
            return new AndFilter(newPredicates);
        }

        @Override // com.jayway.jsonpath.Filter, com.jayway.jsonpath.Predicate
        public boolean apply(Predicate.PredicateContext ctx) {
            for (Predicate predicate : this.predicates) {
                if (!predicate.apply(ctx)) {
                    return false;
                }
            }
            return true;
        }

        public String toString() {
            return "(" + Utils.join(" && ", this.predicates) + ")";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class OrFilter extends Filter {
        private final Predicate left;
        private final Predicate right;

        private OrFilter(Predicate left, Predicate right) {
            this.left = left;
            this.right = right;
        }

        @Override // com.jayway.jsonpath.Filter
        public Filter and(Predicate other) {
            return new OrFilter(this.left, new AndFilter(this.right, other));
        }

        @Override // com.jayway.jsonpath.Filter, com.jayway.jsonpath.Predicate
        public boolean apply(Predicate.PredicateContext ctx) {
            boolean a = this.left.apply(ctx);
            return a || this.right.apply(ctx);
        }

        public String toString() {
            return "(" + this.left.toString() + " || " + this.right.toString() + ")";
        }
    }

    public static Filter parse(String filter) {
        String filter2 = filter.trim();
        if (!filter2.startsWith("[") || !filter2.endsWith("]")) {
            throw new InvalidPathException("Filter must start with '[' and end with ']'. " + filter2);
        }
        String filter3 = filter2.substring(1, filter2.length() - 1).trim();
        if (!filter3.startsWith("?")) {
            throw new InvalidPathException("Filter must start with '[?' and end with ']'. " + filter3);
        }
        String filter4 = filter3.substring(1).trim();
        if (!filter4.startsWith("(") || !filter4.endsWith(")")) {
            throw new InvalidPathException("Filter must start with '[?(' and end with ')]'. " + filter4);
        }
        String filter5 = filter4.substring(1, filter4.length() - 1).trim();
        String[] split = OPERATOR_SPLIT.split(filter5);
        Stack<String> operators = new Stack<>();
        Stack<Criteria> criteria = new Stack<>();
        for (String exp : split) {
            String exp2 = exp.trim();
            if (AND.equals(exp2) || OR.equals(exp2)) {
                operators.push(exp2);
            } else {
                criteria.push(Criteria.parse(cleanCriteria(exp2)));
            }
        }
        Filter root = new SingleFilter(criteria.pop());
        while (!operators.isEmpty()) {
            String operator = operators.pop();
            if (AND.equals(operator)) {
                root = root.and(criteria.pop());
            } else if (criteria.isEmpty()) {
                throw new InvalidPathException("Invalid operators " + filter5);
            } else {
                root = root.or(criteria.pop());
            }
        }
        if (!operators.isEmpty() || !criteria.isEmpty()) {
            throw new InvalidPathException("Invalid operators " + filter5);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Parsed filter: " + root.toString());
        }
        return root;
    }

    private static String cleanCriteria(String filter) {
        int begin = 0;
        int end = filter.length() - 1;
        char c = filter.charAt(0);
        while (true) {
            if (c != '[' && c != '?' && c != '(' && c != ' ') {
                break;
            }
            begin++;
            c = filter.charAt(begin);
        }
        char c2 = filter.charAt(end);
        while (true) {
            if (c2 == ')' || c2 == ' ') {
                end--;
                c2 = filter.charAt(end);
            } else {
                return filter.substring(begin, end + 1);
            }
        }
    }
}
