package com.microsoft.band.webtiles;

import android.os.Parcel;
import android.os.Parcelable;
import com.facebook.internal.ServerProtocol;
import com.j256.ormlite.stmt.query.SimpleComparison;
import com.microsoft.band.internal.util.KDKLog;
import com.microsoft.band.internal.util.Validation;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/* loaded from: classes.dex */
public class WebTileCondition implements Parcelable {
    private static final String MESSAGE_FORMAT = "Invalid WebTileCondition: [%s]  **ERROR: %s.";
    private static final int NUMBER_PRECISION = 16;
    private static final String PATTERN_NUMBER = "^([+-]?\\d*\\.?\\d*([Ee][-+]?\\d+)?)";
    private static final String PATTERN_OPERATOR = "^(==|>=|<=|!=|contains|>|<)";
    private static final String PATTERN_STRING = "^(\".*\"|'.*')";
    public static final String PATTERN_VARIABLE = "\\{\\{[a-zA-Z_]\\w*\\}\\}";
    private boolean mAlwaysTrue;
    private String mLeft;
    private OperandType mLeftType;
    private String mOp;
    private String mRight;
    private OperandType mRightType;
    private static final String TAG = WebTileCondition.class.getSimpleName();
    public static final Parcelable.Creator<WebTileCondition> CREATOR = new Parcelable.Creator<WebTileCondition>() { // from class: com.microsoft.band.webtiles.WebTileCondition.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WebTileCondition createFromParcel(Parcel in) {
            return new WebTileCondition(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WebTileCondition[] newArray(int size) {
            return new WebTileCondition[size];
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public enum OperandType {
        VARIABLE,
        STRING,
        NUMBER
    }

    public WebTileCondition(String conditionExpression) {
        Validation.validateNullParameter(conditionExpression, "WebTile condition expression");
        String expression = conditionExpression.trim();
        if (ServerProtocol.DIALOG_RETURN_SCOPES_TRUE.equals(expression)) {
            this.mAlwaysTrue = true;
            return;
        }
        parseOperand(expression, true);
        String expression2 = expression.substring(this.mLeft.length()).trim();
        parseOperator(expression2);
        String expression3 = expression2.substring(this.mOp.length()).trim();
        parseOperand(expression3, false);
        if (expression3.substring(this.mRight.length()).trim().length() > 0) {
            throw new IllegalArgumentException(String.format(MESSAGE_FORMAT, conditionExpression, "extra content after right operand [" + this.mRight + "]"));
        }
        if (OperandType.VARIABLE != this.mLeftType && OperandType.VARIABLE != this.mRightType) {
            throw new IllegalArgumentException(String.format(MESSAGE_FORMAT, conditionExpression, "missing variable"));
        }
        if ("contains".equalsIgnoreCase(this.mOp)) {
            if (OperandType.NUMBER == this.mLeftType || OperandType.NUMBER == this.mRightType) {
                throw new IllegalArgumentException(String.format(MESSAGE_FORMAT, conditionExpression, "[contains] is NOT a valid operator for number"));
            }
        }
    }

    private void parseOperand(String expr, boolean isLeft) {
        Pattern PATTERN = Pattern.compile("^\\{\\{[a-zA-Z_]\\w*\\}\\}");
        Matcher matcher = PATTERN.matcher(expr);
        if (matcher.find()) {
            setOperand(isLeft, matcher.group(), OperandType.VARIABLE);
            return;
        }
        Pattern PATTERN2 = Pattern.compile(PATTERN_STRING);
        Matcher matcher2 = PATTERN2.matcher(expr);
        if (matcher2.find()) {
            setOperand(isLeft, matcher2.group(), OperandType.STRING);
            return;
        }
        Pattern PATTERN3 = Pattern.compile(PATTERN_NUMBER);
        Matcher matcher3 = PATTERN3.matcher(expr);
        if (matcher3.find()) {
            String result = matcher3.group();
            if (!result.isEmpty()) {
                if (!isNumber(result)) {
                    Object[] objArr = new Object[2];
                    objArr[0] = isLeft ? "left" : "right";
                    objArr[1] = expr;
                    throw new IllegalArgumentException(String.format("Invalid %s operand found in [%s] with wrong number format", objArr));
                }
                setOperand(isLeft, result, OperandType.NUMBER);
                return;
            }
        }
        Object[] objArr2 = new Object[2];
        objArr2[0] = isLeft ? "left" : "right";
        objArr2[1] = expr;
        throw new IllegalArgumentException(String.format("Invalid %s operand found in [%s]", objArr2));
    }

    private void setOperand(boolean isLeft, String result, OperandType type) {
        if (isLeft) {
            this.mLeftType = type;
            this.mLeft = result;
            return;
        }
        this.mRightType = type;
        this.mRight = result;
    }

    private boolean isNumber(String expr) {
        try {
            Double.parseDouble(expr);
            return true;
        } catch (NumberFormatException e) {
            KDKLog.i(TAG, e.getMessage());
            return false;
        }
    }

    private void parseOperator(String expr) {
        Pattern PATTERN = Pattern.compile(PATTERN_OPERATOR);
        Matcher matcher = PATTERN.matcher(expr);
        if (!matcher.find()) {
            throw new IllegalArgumentException(String.format("Invalid operator found in [%s]", expr));
        }
        this.mOp = matcher.group();
    }

    public boolean evaluateWithResources(Map<String, String> contentMap) throws WebTileException {
        String lOperand;
        String rOperand;
        if (this.mAlwaysTrue) {
            return true;
        }
        if (OperandType.VARIABLE == this.mLeftType) {
            lOperand = WebTile.resolveDataBindingExpression(this.mLeft, contentMap);
        } else {
            lOperand = stripString(this.mLeft);
        }
        if (OperandType.VARIABLE == this.mRightType) {
            rOperand = WebTile.resolveDataBindingExpression(this.mRight, contentMap);
        } else {
            rOperand = stripString(this.mRight);
        }
        if (OperandType.VARIABLE == this.mLeftType && OperandType.VARIABLE == this.mRightType) {
            if ("contains".equalsIgnoreCase(this.mOp) || (!isNumber(lOperand) && !isNumber(rOperand))) {
                return evaluateString(lOperand, this.mOp, rOperand);
            }
            if (isNumber(lOperand) && isNumber(rOperand)) {
                return evaluateNumber(Double.valueOf(parsingNumber(lOperand)), this.mOp, Double.valueOf(parsingNumber(rOperand)));
            }
            return "!=".equals(this.mOp);
        } else if (OperandType.NUMBER == this.mLeftType || OperandType.NUMBER == this.mRightType) {
            try {
                return evaluateNumber(Double.valueOf(parsingNumber(lOperand)), this.mOp, Double.valueOf(parsingNumber(rOperand)));
            } catch (Exception e) {
                String msg = String.format("Invalid WebTileCondition: [%s %s %s]  with error %s", lOperand, this.mOp, rOperand, e.getMessage());
                KDKLog.e(TAG, msg);
                throw new WebTileException(msg, WebTileErrorType.INVALID_CONDITION);
            }
        } else {
            return evaluateString(lOperand, this.mOp, rOperand);
        }
    }

    private String stripString(String expression) {
        if (expression.startsWith("\"") || expression.startsWith("'")) {
            return expression.substring(1, expression.length() - 1);
        }
        return expression;
    }

    private double parsingNumber(String input) {
        if (input.length() > 16) {
            input = new BigDecimal(input, new MathContext(16)).toString();
        }
        return Double.parseDouble(input);
    }

    private boolean evaluateString(String left, String op, String right) throws WebTileException {
        if ("==".equals(op)) {
            return left.equals(right);
        }
        if ("!=".equals(op)) {
            return !left.equals(right);
        } else if ("contains".equals(op)) {
            return left.contains(right);
        } else {
            if (SimpleComparison.GREATER_THAN_EQUAL_TO_OPERATION.equals(op)) {
                return left.compareTo(right) >= 0;
            } else if (SimpleComparison.LESS_THAN_EQUAL_TO_OPERATION.equals(op)) {
                return left.compareTo(right) <= 0;
            } else if (SimpleComparison.GREATER_THAN_OPERATION.equals(op)) {
                return left.compareTo(right) > 0;
            } else if (SimpleComparison.LESS_THAN_OPERATION.equals(op)) {
                return left.compareTo(right) < 0;
            } else {
                throw new WebTileException(String.format("Invalid operator [%s] for String", op));
            }
        }
    }

    private boolean evaluateNumber(Double left, String op, Double right) throws WebTileException {
        if ("==".equals(op)) {
            return Double.compare(left.doubleValue(), right.doubleValue()) == 0;
        } else if ("!=".equals(op)) {
            return Double.compare(left.doubleValue(), right.doubleValue()) != 0;
        } else if (SimpleComparison.GREATER_THAN_EQUAL_TO_OPERATION.equals(op)) {
            return Double.compare(left.doubleValue(), right.doubleValue()) >= 0;
        } else if (SimpleComparison.LESS_THAN_EQUAL_TO_OPERATION.equals(op)) {
            return Double.compare(left.doubleValue(), right.doubleValue()) <= 0;
        } else if (SimpleComparison.GREATER_THAN_OPERATION.equals(op)) {
            return Double.compare(left.doubleValue(), right.doubleValue()) > 0;
        } else if (SimpleComparison.LESS_THAN_OPERATION.equals(op)) {
            return Double.compare(left.doubleValue(), right.doubleValue()) < 0;
        } else {
            throw new WebTileException(String.format("Invalid operator [%s] for number", op));
        }
    }

    public String getLeft() {
        return this.mLeft;
    }

    public String getRight() {
        return this.mRight;
    }

    public String getOp() {
        return this.mOp;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Set<String> getConditionVariables() {
        Set<String> keys = new HashSet<>();
        if (OperandType.VARIABLE == this.mLeftType) {
            keys.addAll(WebTile.findContentKey(this.mLeft));
        }
        if (OperandType.VARIABLE == this.mRightType) {
            keys.addAll(WebTile.findContentKey(this.mRight));
        }
        return keys;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mLeft);
        dest.writeString(this.mOp);
        dest.writeString(this.mRight);
        dest.writeByte((byte) (this.mAlwaysTrue ? 1 : 0));
        dest.writeSerializable(this.mLeftType);
        dest.writeSerializable(this.mRightType);
    }

    WebTileCondition(Parcel in) {
        this.mLeft = in.readString();
        this.mOp = in.readString();
        this.mRight = in.readString();
        this.mAlwaysTrue = in.readByte() != 0;
        this.mLeftType = (OperandType) in.readSerializable();
        this.mRightType = (OperandType) in.readSerializable();
    }
}
