package com.jayway.jsonpath.internal.token;

import com.jayway.jsonpath.InvalidPathException;
import com.jayway.jsonpath.PathNotFoundException;
import com.jayway.jsonpath.internal.PathRef;
import com.jayway.jsonpath.internal.Utils;
import com.unnamed.b.atv.model.TreeNode;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/* loaded from: classes.dex */
public class ArrayPathToken extends PathToken {
    private static final Logger logger = LoggerFactory.getLogger(ArrayPathToken.class);
    private final List<Integer> criteria;
    private final boolean isDefinite;
    private final Operation operation;

    /* loaded from: classes.dex */
    public enum Operation {
        CONTEXT_SIZE,
        SLICE_TO,
        SLICE_FROM,
        SLICE_BETWEEN,
        INDEX_SEQUENCE,
        SINGLE_INDEX
    }

    public ArrayPathToken(List<Integer> criteria, Operation operation) {
        this.criteria = criteria;
        this.operation = operation;
        this.isDefinite = Operation.SINGLE_INDEX == operation || Operation.CONTEXT_SIZE == operation;
    }

    @Override // com.jayway.jsonpath.internal.token.PathToken
    public void evaluate(String currentPath, PathRef parent, Object model, EvaluationContextImpl ctx) {
        if (model == null) {
            throw new PathNotFoundException("The path " + currentPath + " is null");
        }
        if (!ctx.jsonProvider().isArray(model)) {
            throw new InvalidPathException(String.format("Filter: %s can only be applied to arrays. Current context is: %s", toString(), model));
        }
        try {
            switch (this.operation) {
                case SINGLE_INDEX:
                    handleArrayIndex(this.criteria.get(0).intValue(), currentPath, model, ctx);
                    return;
                case INDEX_SEQUENCE:
                    for (Integer i : this.criteria) {
                        handleArrayIndex(i.intValue(), currentPath, model, ctx);
                    }
                    return;
                case CONTEXT_SIZE:
                    int idx = ctx.jsonProvider().length(model) + this.criteria.get(0).intValue();
                    handleArrayIndex(idx, currentPath, model, ctx);
                    return;
                case SLICE_FROM:
                    int input = this.criteria.get(0).intValue();
                    int length = ctx.jsonProvider().length(model);
                    int from = input;
                    if (from < 0) {
                        from += length;
                    }
                    int from2 = Math.max(0, from);
                    logger.debug("Slice from index on array with length: {}. From index: {} to: {}. Input: {}", Integer.valueOf(length), Integer.valueOf(from2), Integer.valueOf(length - 1), toString());
                    if (length != 0 && from2 < length) {
                        for (int i2 = from2; i2 < length; i2++) {
                            handleArrayIndex(i2, currentPath, model, ctx);
                        }
                        return;
                    }
                    return;
                case SLICE_TO:
                    int input2 = this.criteria.get(0).intValue();
                    int length2 = ctx.jsonProvider().length(model);
                    int to = input2;
                    if (to < 0) {
                        to += length2;
                    }
                    int to2 = Math.min(length2, to);
                    logger.debug("Slice to index on array with length: {}. From index: 0 to: {}. Input: {}", Integer.valueOf(length2), Integer.valueOf(to2), toString());
                    if (length2 != 0) {
                        for (int i3 = 0; i3 < to2; i3++) {
                            handleArrayIndex(i3, currentPath, model, ctx);
                        }
                        return;
                    }
                    return;
                case SLICE_BETWEEN:
                    int from3 = this.criteria.get(0).intValue();
                    int to3 = this.criteria.get(1).intValue();
                    int length3 = ctx.jsonProvider().length(model);
                    int to4 = Math.min(length3, to3);
                    if (from3 < to4 && length3 != 0) {
                        logger.debug("Slice between indexes on array with length: {}. From index: {} to: {}. Input: {}", Integer.valueOf(length3), Integer.valueOf(from3), Integer.valueOf(to4), toString());
                        for (int i4 = from3; i4 < to4; i4++) {
                            handleArrayIndex(i4, currentPath, model, ctx);
                        }
                        return;
                    }
                    return;
                default:
                    return;
            }
        } catch (IndexOutOfBoundsException e) {
            throw new PathNotFoundException("Index out of bounds when evaluating path " + currentPath);
        }
    }

    @Override // com.jayway.jsonpath.internal.token.PathToken
    public String getPathFragment() {
        StringBuilder sb = new StringBuilder();
        if (Operation.SINGLE_INDEX == this.operation || Operation.INDEX_SEQUENCE == this.operation) {
            sb.append("[").append(Utils.join(",", "", this.criteria)).append("]");
        } else if (Operation.CONTEXT_SIZE == this.operation) {
            sb.append("[@.size()").append(this.criteria.get(0)).append("]");
        } else if (Operation.SLICE_FROM == this.operation) {
            sb.append("[").append(this.criteria.get(0)).append(":]");
        } else if (Operation.SLICE_TO == this.operation) {
            sb.append("[:").append(this.criteria.get(0)).append("]");
        } else if (Operation.SLICE_BETWEEN == this.operation) {
            sb.append("[").append(this.criteria.get(0)).append(TreeNode.NODES_ID_SEPARATOR).append(this.criteria.get(1)).append("]");
        } else {
            sb.append("NOT IMPLEMENTED");
        }
        return sb.toString();
    }

    @Override // com.jayway.jsonpath.internal.token.PathToken
    boolean isTokenDefinite() {
        return this.isDefinite;
    }
}
