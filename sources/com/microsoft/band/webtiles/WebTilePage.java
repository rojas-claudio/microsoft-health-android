package com.microsoft.band.webtiles;

import android.os.Parcel;
import android.os.Parcelable;
import com.microsoft.band.internal.util.Validation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes.dex */
public class WebTilePage implements Parcelable {
    public static final Parcelable.Creator<WebTilePage> CREATOR = new Parcelable.Creator<WebTilePage>() { // from class: com.microsoft.band.webtiles.WebTilePage.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WebTilePage createFromParcel(Parcel in) {
            return new WebTilePage(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WebTilePage[] newArray(int size) {
            return new WebTilePage[size];
        }
    };
    private static final String JSON_KEY_PAGE_CONDITION = "condition";
    private static final String JSON_KEY_PAGE_ICON_BINDING = "iconBindings";
    private static final String JSON_KEY_PAGE_LAYOUT = "layout";
    private static final String JSON_KEY_PAGE_TEXT_BINDING = "textBindings";
    private String mCondition;
    private List<Integer> mElementIDs;
    private List<WebTileIconBinding> mIconBindings;
    private PageLayoutStyle mPageLayoutStyle;
    private List<WebTileTextBinding> mTextBindings;

    public WebTilePage(JSONObject json) throws JSONException {
        this.mElementIDs = new ArrayList();
        this.mIconBindings = new ArrayList();
        this.mTextBindings = new ArrayList();
        if (json.has(JSON_KEY_PAGE_LAYOUT)) {
            setPageLayoutStyle(json.getString(JSON_KEY_PAGE_LAYOUT));
            if (json.has(JSON_KEY_PAGE_CONDITION)) {
                setCondition(json.getString(JSON_KEY_PAGE_CONDITION));
            }
            if (json.has(JSON_KEY_PAGE_ICON_BINDING)) {
                setIconBindings(json.getJSONArray(JSON_KEY_PAGE_ICON_BINDING));
            }
            if (json.has(JSON_KEY_PAGE_TEXT_BINDING)) {
                setTextBindings(json.getJSONArray(JSON_KEY_PAGE_TEXT_BINDING));
                return;
            }
            return;
        }
        throw new IllegalArgumentException("Layout is required for WebTile pages.");
    }

    public PageLayoutStyle getPageLayoutStyle() {
        return this.mPageLayoutStyle;
    }

    public void setPageLayoutStyle(String layout) {
        if (layout == null || layout.length() == 0) {
            throw new IllegalArgumentException("Invalid layout provided");
        }
        PageLayoutStyle layoutStyle = PageLayoutStyle.lookup(layout);
        if (layoutStyle != null) {
            this.mPageLayoutStyle = layoutStyle;
            return;
        }
        throw new IllegalArgumentException("Invalid layout provided");
    }

    public String getCondition() {
        return this.mCondition;
    }

    public void setCondition(String condition) {
        Validation.validateNullParameter(condition, "Page Condition");
        Validation.validateStringEmptyOrWhiteSpace(condition, "Page Condition");
        this.mCondition = condition;
    }

    public List<WebTileTextBinding> getTextBindings() {
        return this.mTextBindings;
    }

    public void setTextBindings(JSONArray textJSONArray) throws JSONException {
        int size = textJSONArray.length();
        for (int i = 0; i < size; i++) {
            WebTileTextBinding textBinding = new WebTileTextBinding((JSONObject) textJSONArray.get(i));
            int id = textBinding.getId();
            if (!this.mPageLayoutStyle.hasTextElementId(id)) {
                throw new IllegalArgumentException(String.format("Text element ID %d is not found in layout %s", Integer.valueOf(id), this.mPageLayoutStyle.getName()));
            }
            if (this.mElementIDs.contains(Integer.valueOf(id))) {
                throw new IllegalArgumentException(String.format("Duplicate page element ID %d", Integer.valueOf(id)));
            }
            this.mElementIDs.add(Integer.valueOf(textBinding.getId()));
            this.mTextBindings.add(textBinding);
        }
    }

    public List<WebTileIconBinding> getIconBindings() {
        return this.mIconBindings;
    }

    public void setIconBindings(JSONArray iconJSONArray) throws JSONException {
        int size = iconJSONArray.length();
        for (int i = 0; i < size; i++) {
            WebTileIconBinding iconBinding = new WebTileIconBinding((JSONObject) iconJSONArray.get(i));
            int id = iconBinding.getId();
            if (!this.mPageLayoutStyle.hasIconElementId(id)) {
                throw new IllegalArgumentException(String.format("Icon element ID %d is not found in layout %s", Integer.valueOf(id), this.mPageLayoutStyle.getName()));
            }
            if (this.mElementIDs.contains(Integer.valueOf(id))) {
                throw new IllegalArgumentException(String.format("Duplicate page element ID %d", Integer.valueOf(id)));
            }
            this.mElementIDs.add(Integer.valueOf(iconBinding.getId()));
            this.mIconBindings.add(iconBinding);
        }
    }

    public Map<Integer, String> getIconData(Map<String, String> contentMap) {
        Map<Integer, String> iconData = new HashMap<>();
        for (WebTileIconBinding iconBinding : getIconBindings()) {
            String iconName = iconBinding.getIconBindingData(contentMap);
            if (iconName != null) {
                iconData.put(Integer.valueOf(iconBinding.getId()), iconName);
            }
        }
        return iconData;
    }

    public Map<Integer, String> getTextData(Map<String, String> contentMap) {
        Map<Integer, String> textData = new HashMap<>();
        for (WebTileTextBinding textBinding : getTextBindings()) {
            textData.put(Integer.valueOf(textBinding.getId()), WebTile.resolveDataBindingExpression(textBinding.getValue(), contentMap));
        }
        return textData;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    WebTilePage(Parcel in) {
        this.mElementIDs = new ArrayList();
        this.mPageLayoutStyle = (PageLayoutStyle) in.readSerializable();
        this.mCondition = in.readString();
        this.mIconBindings = new ArrayList();
        in.readList(this.mIconBindings, WebTileIconBinding.class.getClassLoader());
        this.mTextBindings = new ArrayList();
        in.readList(this.mTextBindings, WebTileTextBinding.class.getClassLoader());
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.mPageLayoutStyle);
        dest.writeString(this.mCondition);
        dest.writeList(this.mIconBindings);
        dest.writeList(this.mTextBindings);
    }
}
