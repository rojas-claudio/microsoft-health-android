package com.microsoft.band.webtiles;

import com.microsoft.band.device.NotificationGenericDialog;
import com.microsoft.band.internal.util.Validation;
import java.util.Map;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes.dex */
public final class WebTileNotification {
    private static final String JSON_KEY_NOTIFICATIONE_TITLE = "title";
    private static final String JSON_KEY_NOTIFICATION_BODY = "body";
    private static final String JSON_KEY_NOTIFICATION_CONDITION = "condition";
    private String mBody;
    private WebTileCondition mCondition;
    private String mTitle;

    public WebTileNotification(JSONObject json) throws JSONException {
        if (json.has(JSON_KEY_NOTIFICATION_CONDITION)) {
            setCondition(json.getString(JSON_KEY_NOTIFICATION_CONDITION));
            if (json.has("title")) {
                setTitle(json.getString("title"));
                if (json.has("body")) {
                    setBody(json.getString("body"));
                    return;
                }
                return;
            }
            throw new IllegalArgumentException("Title is required for WebTile notifications.");
        }
        throw new IllegalArgumentException("Condition is required for WebTile notifications.");
    }

    private void setCondition(String condition) {
        Validation.validateNullParameter(condition, "Condition");
        Validation.validateStringEmptyOrWhiteSpace(condition, "Condition");
        this.mCondition = new WebTileCondition(condition);
    }

    public WebTileCondition getCondition() {
        return this.mCondition;
    }

    private void setTitle(String title) {
        Validation.validateNullParameter(title, "Title");
        Validation.validateStringEmptyOrWhiteSpace(title, "Title");
        this.mTitle = title;
    }

    public String getTitle() {
        return this.mTitle;
    }

    private void setBody(String body) {
        if (body != null && !body.matches("\\s*")) {
            this.mBody = body;
        }
    }

    public String getBody() {
        return this.mBody;
    }

    public NotificationGenericDialog getNotificationWithResource(Map<String, String> contentMap) throws WebTileException {
        if (this.mCondition.evaluateWithResources(contentMap)) {
            String mTitleValue = WebTile.resolveDataBindingExpression(this.mTitle, contentMap);
            String mBodyValue = this.mBody != null ? WebTile.resolveDataBindingExpression(this.mBody, contentMap) : null;
            return new NotificationGenericDialog(mTitleValue, mBodyValue);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Set<String> getNotificationContentKeys() {
        Set<String> keys = this.mCondition.getConditionVariables();
        keys.addAll(WebTile.findContentKey(getTitle()));
        if (this.mBody != null) {
            keys.addAll(WebTile.findContentKey(this.mBody));
        }
        return keys;
    }
}
