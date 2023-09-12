package com.microsoft.band.webtiles;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.util.Pair;
import android.util.Patterns;
import com.microsoft.band.BandTheme;
import com.microsoft.band.device.DeviceConstants;
import com.microsoft.band.device.NotificationGenericDialog;
import com.microsoft.band.internal.util.IconUtils;
import com.microsoft.band.internal.util.KDKLog;
import com.microsoft.band.internal.util.StringUtil;
import com.microsoft.band.internal.util.Validation;
import com.microsoft.band.service.crypto.CryptoException;
import com.microsoft.band.service.crypto.CryptoProvider;
import com.microsoft.band.service.crypto.CryptoProviderImpl;
import com.microsoft.band.service.util.FileHelper;
import com.microsoft.band.tiles.BandIcon;
import com.microsoft.band.tiles.pages.IconData;
import com.microsoft.band.tiles.pages.PageData;
import com.microsoft.band.tiles.pages.TextBlockData;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.logging.LogConstants;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes.dex */
public class WebTile implements Parcelable {
    private static final String JSON_KEY_AUTHOR = "author";
    private static final String JSON_KEY_BADGE_ICON = "badgeIcon";
    private static final String JSON_KEY_BADGE_ICON_SIZE = "24";
    private static final String JSON_KEY_CONTACT_EMAIL = "contactEmail";
    private static final String JSON_KEY_CRYPT_VERSION = "crypt_version";
    private static final String JSON_KEY_DESCRIPTION = "description";
    private static final String JSON_KEY_ICONS = "icons";
    private static final String JSON_KEY_LAST_UPDATED_ERROR = "last_updated_error";
    private static final String JSON_KEY_MANIFEST_VERSION = "manifestVersion";
    private static final String JSON_KEY_NAME = "name";
    private static final String JSON_KEY_NOTIFICATIONS = "notifications";
    private static final String JSON_KEY_ORGANIZATION = "organization";
    private static final String JSON_KEY_PAGES = "pages";
    private static final String JSON_KEY_REFRESH_INTERVAL_MINUTES = "refreshIntervalMinutes";
    private static final String JSON_KEY_RESOURCES = "resources";
    private static final String JSON_KEY_RESOURCE_CACHE_INFOS = "resource_cache_infos";
    private static final String JSON_KEY_TILE_ICON = "tileIcon";
    private static final String JSON_KEY_TILE_ICON_SIZE = "46";
    private static final String JSON_KEY_TILE_THEME = "tileTheme";
    private static final String JSON_KEY_VARIABLE_MAPPINGS = "variable_mappings";
    private static final String JSON_KEY_VERSION = "version";
    private static final String JSON_KEY_VERSION_STRING = "versionString";
    private static final String JSON_TILE_THEME_BASE = "base";
    private static final String JSON_TILE_THEME_HIGHLIGHT = "highlight";
    private static final String JSON_TILE_THEME_HIGH_CONTRAST = "highContrast";
    private static final String JSON_TILE_THEME_LOWLIGHT = "lowlight";
    private static final String JSON_TILE_THEME_MUTED = "muted";
    private static final String JSON_TILE_THEME_SECONDARY = "secondary";
    private static final String MANIFEST_FILE_NAME = "manifest.json";
    private LinkedHashMap<String, Pair<String, Integer>> mAdditionalIconsMap;
    private Map<String, String> mAuthenticationMap;
    private String mAuthor;
    private String mBadgeIconPath;
    private List<String> mConsumedContentKeys;
    private String mContactEmail;
    private String mDescription;
    private File mDirectory;
    private boolean mLastUpdateError;
    private LinkedHashMap<PageLayoutStyle, Integer> mLayouts;
    private int mManifestVersion;
    private String mName;
    private List<WebTileNotification> mNotifications;
    private String mOrganization;
    private int mRefreshIntervalMinutes;
    private List<String> mResourceContentMappings;
    private List<WebTileResource> mResources;
    private String mTileIconPath;
    private UUID mTileId;
    private BandTheme mTileTheme;
    private boolean mValidatingEnable;
    private Map<String, String> mVariableMappings;
    private int mVersion;
    private String mVersionString;
    private List<WebTilePage> mWebTilePages;
    private static final String TAG = WebTile.class.getSimpleName();
    private static String SYNC_TIME_FILE_NAME = "synctime";
    private static String AUTHENTICATION_FILE_NAME = "authentication";
    private static String CACHED_DATA_FILE_NAME = "cachedData";
    public static final Parcelable.Creator<WebTile> CREATOR = new Parcelable.Creator<WebTile>() { // from class: com.microsoft.band.webtiles.WebTile.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WebTile createFromParcel(Parcel in) {
            return new WebTile(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WebTile[] newArray(int size) {
            return new WebTile[size];
        }
    };

    public WebTile(File directory) throws JSONException, IOException, IllegalArgumentException {
        this(directory, UUID.randomUUID(), true);
    }

    public WebTile(File directory, UUID tileId) throws IllegalArgumentException, JSONException, IOException {
        this(directory, tileId, false);
    }

    private WebTile(File directory, UUID tileId, boolean withValidate) throws IllegalArgumentException, JSONException, IOException {
        this.mVersion = 1;
        this.mVersionString = LogConstants.METADATA_VERSION;
        this.mRefreshIntervalMinutes = 30;
        this.mResourceContentMappings = new ArrayList();
        this.mConsumedContentKeys = new ArrayList();
        this.mAuthenticationMap = new HashMap();
        Validation.validateNullParameter(directory, "WebTile package directory");
        Validation.validateNullParameter(tileId, "WebTile Id");
        this.mDirectory = directory;
        this.mTileId = tileId;
        this.mValidatingEnable = withValidate;
        this.mAdditionalIconsMap = new LinkedHashMap<>();
        this.mResources = new ArrayList();
        this.mWebTilePages = new ArrayList();
        this.mLayouts = new LinkedHashMap<>();
        this.mNotifications = new ArrayList();
        this.mVariableMappings = new HashMap();
        initWithJSONObject(new JSONObject(FileHelper.readStringFromFile(new File(this.mDirectory, MANIFEST_FILE_NAME))));
    }

    private void initWithJSONObject(JSONObject json) throws IllegalArgumentException, IOException {
        try {
            if (json.has(JSON_KEY_MANIFEST_VERSION)) {
                setManifestVersion(json.getInt(JSON_KEY_MANIFEST_VERSION));
                if (json.has("name")) {
                    setName(json.getString("name"));
                    if (json.has("description")) {
                        setDescription(json.getString("description"));
                    }
                    if (json.has("version")) {
                        setVersion(json.getInt("version"));
                    }
                    if (json.has(JSON_KEY_VERSION_STRING)) {
                        setVersionString(json.getString(JSON_KEY_VERSION_STRING));
                    } else {
                        setVersionString(JSONObject.numberToString(Integer.valueOf(getVersion())));
                    }
                    if (json.has(JSON_KEY_AUTHOR)) {
                        setAuthor(json.getString(JSON_KEY_AUTHOR));
                    }
                    if (json.has(JSON_KEY_ORGANIZATION)) {
                        setOrganization(json.getString(JSON_KEY_ORGANIZATION));
                    }
                    if (json.has(JSON_KEY_CONTACT_EMAIL)) {
                        setContactEmail(json.getString(JSON_KEY_CONTACT_EMAIL));
                    }
                    if (json.has(JSON_KEY_TILE_ICON)) {
                        setTileIconPath(json.getJSONObject(JSON_KEY_TILE_ICON));
                        if (json.has(JSON_KEY_BADGE_ICON)) {
                            setBadgeIconPath(json.optJSONObject(JSON_KEY_BADGE_ICON));
                        }
                        if (json.has(JSON_KEY_ICONS)) {
                            setAdditionalIconsMap(json.optJSONObject(JSON_KEY_ICONS));
                        }
                        if (json.has(JSON_KEY_TILE_THEME)) {
                            setTileTheme(json.optJSONObject(JSON_KEY_TILE_THEME));
                        }
                        if (json.has(JSON_KEY_REFRESH_INTERVAL_MINUTES)) {
                            setRefreshIntervalMinutes(json.getInt(JSON_KEY_REFRESH_INTERVAL_MINUTES));
                        }
                        if (json.has(JSON_KEY_RESOURCES)) {
                            setResources(json.getJSONArray(JSON_KEY_RESOURCES));
                            if (json.has(JSON_KEY_NOTIFICATIONS)) {
                                setWebTileNotifications(json.getJSONArray(JSON_KEY_NOTIFICATIONS));
                            }
                            if (json.has(JSON_KEY_PAGES)) {
                                setWebTilePages(json.getJSONArray(JSON_KEY_PAGES));
                                return;
                            }
                            throw new IllegalArgumentException("Pages is required for WebTile.");
                        }
                        throw new IllegalArgumentException("Resources is required for WebTile.");
                    }
                    throw new IllegalArgumentException("TileIcon is required for WebTile.");
                }
                throw new IllegalArgumentException("Name is required for WebTile.");
            }
            throw new IllegalArgumentException("ManifestVersion is required for WebTile.");
        } catch (CryptoException e) {
            throw new IllegalArgumentException("Cannot read crypto authentication: " + e.getMessage(), e);
        } catch (JSONException e2) {
            throw new IllegalArgumentException("Cannot read JSON Object: " + e2.getMessage(), e2);
        }
    }

    public File getDirectory() {
        return this.mDirectory;
    }

    public void setDirectory(File mDirectory) {
        this.mDirectory = mDirectory;
    }

    public List<PageData> getWebPageData() {
        return null;
    }

    public int getManifestVersion() {
        return this.mManifestVersion;
    }

    public void setManifestVersion(int manifestVersion) {
        Validation.validateNullParameter(Integer.valueOf(manifestVersion), "Manifest Version");
        Validation.validateValueMatchesExpected(manifestVersion, 1, "Manifest Version");
        this.mManifestVersion = manifestVersion;
    }

    public String getName() {
        return this.mName;
    }

    public void setName(String name) {
        Validation.validateNullParameter(name, TelemetryConstants.TimedEvents.Cloud.Golf.SEARCH_TYPE_NAME);
        Validation.validateStringEmptyOrWhiteSpace(name, TelemetryConstants.TimedEvents.Cloud.Golf.SEARCH_TYPE_NAME);
        Validation.lengthLessOrEq(name, 21, TelemetryConstants.TimedEvents.Cloud.Golf.SEARCH_TYPE_NAME);
        this.mName = name;
    }

    public UUID getTileId() {
        return this.mTileId;
    }

    public String getDescription() {
        return this.mDescription;
    }

    public void setDescription(String description) {
        if (description != null && !description.matches("\\s*")) {
            Validation.lengthLessOrEq(description, 100, TelemetryConstants.Events.SendFeedbackComplete.Dimensions.DESCRIPTION);
            this.mDescription = description;
        }
    }

    public int getVersion() {
        return this.mVersion;
    }

    public void setVersion(int version) {
        Validation.validateNotNegative(version, "Version");
        this.mVersion = version;
    }

    public String getAuthor() {
        return this.mAuthor;
    }

    public void setAuthor(String author) {
        if (author != null && !author.matches("\\s*")) {
            Validation.lengthLessOrEq(author, 50, "Author");
            this.mAuthor = author;
        }
    }

    public String getVersionString() {
        return this.mVersionString;
    }

    public void setVersionString(String versionString) {
        if (versionString != null && !versionString.matches("\\s*")) {
            Validation.lengthLessOrEq(versionString, 10, "Version String");
            this.mVersionString = versionString;
        }
    }

    public String getOrganization() {
        return this.mOrganization;
    }

    public void setOrganization(String organization) {
        if (organization != null && !organization.matches("\\s*")) {
            Validation.lengthLessOrEq(organization, 100, "Organization");
            this.mOrganization = organization;
        }
    }

    public String getContactEmail() {
        return this.mContactEmail;
    }

    public void setContactEmail(String contactEmail) {
        if (contactEmail != null && !contactEmail.matches("\\s*")) {
            if (Patterns.EMAIL_ADDRESS.matcher(contactEmail).matches()) {
                Validation.lengthLessOrEq(contactEmail, 100, "Contact Email");
                this.mContactEmail = contactEmail;
                return;
            }
            throw new IllegalArgumentException("Invalid contact email address provided");
        }
    }

    public int getRefreshIntervalMinutes() {
        return this.mRefreshIntervalMinutes;
    }

    public void setRefreshIntervalMinutes(int refreshIntervalMinutes) {
        Validation.validateInRange(JSON_KEY_REFRESH_INTERVAL_MINUTES, refreshIntervalMinutes, 15, DeviceConstants.MAX_REFRESH_INTERVAL);
        this.mRefreshIntervalMinutes = refreshIntervalMinutes;
    }

    public BandTheme getTileTheme() {
        return this.mTileTheme;
    }

    public void setTileTheme(JSONObject theme) throws JSONException {
        if (theme != null) {
            try {
                if (theme.has(JSON_TILE_THEME_BASE) && theme.has(JSON_TILE_THEME_HIGHLIGHT) && theme.has(JSON_TILE_THEME_LOWLIGHT) && theme.has(JSON_TILE_THEME_SECONDARY) && theme.has(JSON_TILE_THEME_HIGH_CONTRAST) && theme.has(JSON_TILE_THEME_MUTED)) {
                    int base = (int) Long.parseLong(theme.getString(JSON_TILE_THEME_BASE), 16);
                    int highlight = (int) Long.parseLong(theme.getString(JSON_TILE_THEME_HIGHLIGHT), 16);
                    int lowlight = (int) Long.parseLong(theme.getString(JSON_TILE_THEME_LOWLIGHT), 16);
                    int secondary = (int) Long.parseLong(theme.getString(JSON_TILE_THEME_SECONDARY), 16);
                    int highContrast = (int) Long.parseLong(theme.getString(JSON_TILE_THEME_HIGH_CONTRAST), 16);
                    int muted = (int) Long.parseLong(theme.getString(JSON_TILE_THEME_MUTED), 16);
                    this.mTileTheme = new BandTheme(base, highlight, lowlight, secondary, highContrast, muted);
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Cannot parse theme color: " + e.getMessage());
            }
        }
    }

    public BandIcon getTileIcon() {
        if (this.mTileIconPath != null) {
            return new BandIcon(BitmapFactory.decodeFile(new File(this.mDirectory, this.mTileIconPath).getAbsolutePath()));
        }
        return null;
    }

    public String getTileIconPath() {
        return this.mTileIconPath;
    }

    public void setTileIconPath(JSONObject tileIconJson) throws JSONException {
        if (tileIconJson.has(JSON_KEY_TILE_ICON_SIZE)) {
            String path = tileIconJson.getString(JSON_KEY_TILE_ICON_SIZE);
            validateIcon(path, 46);
            this.mTileIconPath = path;
            return;
        }
        throw new IllegalArgumentException(String.format("TileIcon is required to be %d x %d pixels.", 46, 46));
    }

    private void validateIcon(String path, int icon_size) {
        if (this.mValidatingEnable) {
            File file = new File(this.mDirectory, path);
            if (!file.exists()) {
                throw new IllegalArgumentException(String.format("Icon file path does not exist: %s", path));
            }
            Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath());
            if (icon_size > 0) {
                Validation.verifyIconPixelSize(bm, icon_size);
            }
            IconUtils.bitmapToByteArray(bm);
        }
    }

    public BandIcon getBadgeIcon() {
        if (this.mBadgeIconPath != null) {
            return new BandIcon(BitmapFactory.decodeFile(new File(this.mDirectory, this.mBadgeIconPath).getAbsolutePath()));
        }
        return null;
    }

    public String getBadgeIconPath() {
        return this.mBadgeIconPath;
    }

    public void setBadgeIconPath(JSONObject tileIconJson) throws JSONException {
        if (tileIconJson != null) {
            if (tileIconJson.has(JSON_KEY_BADGE_ICON_SIZE)) {
                String path = tileIconJson.getString(JSON_KEY_BADGE_ICON_SIZE);
                validateIcon(path, 24);
                this.mBadgeIconPath = path;
                return;
            }
            throw new IllegalArgumentException(String.format("BadgeIcon is required to be %d x %d pixels.", 24, 24));
        }
    }

    public Map<String, Pair<String, Integer>> getAdditionalIconMap() {
        return Collections.unmodifiableMap(this.mAdditionalIconsMap);
    }

    int getIconIndex(String name) {
        Pair<String, Integer> iconPair = this.mAdditionalIconsMap.get(name);
        if (iconPair != null) {
            return iconPair.second.intValue();
        }
        return -1;
    }

    public List<Bitmap> getIcons() {
        List<Bitmap> images = new ArrayList<>();
        Bitmap tileIcon = getTileIcon().getIcon();
        images.add(tileIcon);
        if (this.mBadgeIconPath != null) {
            images.add(getBadgeIcon().getIcon());
        } else {
            images.add(null);
        }
        if (this.mAdditionalIconsMap != null) {
            for (Pair<String, Integer> iconValue : this.mAdditionalIconsMap.values()) {
                if (iconValue.second.intValue() >= images.size()) {
                    images.add(BitmapFactory.decodeFile(new File(this.mDirectory, iconValue.first).getAbsolutePath()));
                }
            }
        }
        return images;
    }

    public void setAdditionalIconsMap(JSONObject tileIconJson) throws JSONException {
        Map<String, Integer> iconPathKeyMap = new HashMap<>();
        if (tileIconJson != null) {
            Iterator<String> keys = tileIconJson.keys();
            iconPathKeyMap.put(this.mTileIconPath, 0);
            if (this.mBadgeIconPath != null) {
                iconPathKeyMap.put(this.mBadgeIconPath, 1);
            }
            int index = 2;
            if (tileIconJson.length() <= 8) {
                while (keys.hasNext()) {
                    String key = keys.next();
                    String path = tileIconJson.getString(key);
                    if (this.mAdditionalIconsMap.keySet().contains(key)) {
                        throw new IllegalArgumentException(String.format("Icon [%s] is already addded.", key));
                    }
                    if (iconPathKeyMap.containsKey(path)) {
                        this.mAdditionalIconsMap.put(key, new Pair<>(path, iconPathKeyMap.get(path)));
                    } else {
                        validateIcon(path, 0);
                        iconPathKeyMap.put(path, Integer.valueOf(index));
                        this.mAdditionalIconsMap.put(key, new Pair<>(path, Integer.valueOf(index)));
                        index++;
                    }
                }
                return;
            }
            throw new IllegalArgumentException(String.format("Only %d icons are allowed", 8));
        }
    }

    public List<WebTileResource> getResources() {
        return Collections.unmodifiableList(this.mResources);
    }

    public void setResources(JSONArray resourceJSONArray) throws JSONException, IOException, CryptoException {
        int size = resourceJSONArray.length();
        if (size == 0) {
            throw new IllegalArgumentException("WebTile Resources array cannot be empty");
        }
        if (!this.mValidatingEnable) {
            this.mAuthenticationMap = readResourceAuthentication();
        }
        for (int i = 0; i < size; i++) {
            WebTileResource resource = new WebTileResource((JSONObject) resourceJSONArray.get(i));
            for (WebTileContentMapping contentMapping : resource.getContentMapping()) {
                if (this.mResourceContentMappings.contains(contentMapping.getTemplatePattern())) {
                    throw new IllegalArgumentException(String.format("WebTile Resources cannot have the same template patterns. Template pattern = %s", contentMapping.getTemplatePattern()));
                }
                this.mResourceContentMappings.add(contentMapping.getTemplatePattern());
            }
            String header = this.mAuthenticationMap.get(resource.getUrl().toString());
            if (header != null) {
                resource.setAuthenticationHeader(header);
            }
            this.mResources.add(resource);
        }
    }

    public List<WebTilePage> getWebTilePages() {
        return Collections.unmodifiableList(this.mWebTilePages);
    }

    public void setWebTilePages(JSONArray pageJSONArray) throws JSONException {
        int index;
        int size = pageJSONArray.length();
        boolean hasErrorPageLayout = false;
        if (size == 0) {
            throw new IllegalArgumentException("Must provide at lease one page");
        }
        if (size <= 7) {
            int index2 = 0 + 1;
            this.mLayouts.put(DeviceConstants.ERROR_PAGE_LAYOUT, 0);
            int mLayoutSize = 1;
            int i = 0;
            while (i < size) {
                WebTilePage page = new WebTilePage((JSONObject) pageJSONArray.get(i));
                for (WebTileTextBinding textBinding : page.getTextBindings()) {
                    Set<String> keys = findContentKey(textBinding.getValue());
                    for (String key : keys) {
                        if (this.mResourceContentMappings.contains(key)) {
                            this.mConsumedContentKeys.add(key);
                        } else {
                            throw new IllegalArgumentException(String.format("Text binding [%s] is not defined in the content mappings", key));
                        }
                    }
                }
                for (WebTileIconBinding iconBinding : page.getIconBindings()) {
                    for (WebTileIconBindingCondition iconBindingCondition : iconBinding.getConditions()) {
                        if (!this.mAdditionalIconsMap.containsKey(iconBindingCondition.getIconName())) {
                            throw new IllegalArgumentException(String.format("Icon [%s] is missing in icons definitions", iconBindingCondition.getIconName()));
                        }
                    }
                }
                PageLayoutStyle layoutStyle = page.getPageLayoutStyle();
                if (!hasErrorPageLayout && DeviceConstants.ERROR_PAGE_LAYOUT == layoutStyle) {
                    hasErrorPageLayout = true;
                }
                if (this.mLayouts.containsKey(layoutStyle)) {
                    index = index2;
                } else {
                    index = index2 + 1;
                    this.mLayouts.put(layoutStyle, Integer.valueOf(index2));
                    mLayoutSize++;
                }
                if ((hasErrorPageLayout && mLayoutSize < 5) || (!hasErrorPageLayout && mLayoutSize <= 5)) {
                    this.mWebTilePages.add(page);
                    i++;
                    index2 = index;
                } else {
                    throw new IllegalArgumentException(String.format("Cannot add WebTile with more than %d Layouts", 4));
                }
            }
            for (String s : this.mResourceContentMappings) {
                if (!this.mConsumedContentKeys.contains(s)) {
                    KDKLog.w(TAG, "Content Mapping [%s] is not used in the resource data bindings.", s);
                }
            }
            return;
        }
        throw new IllegalArgumentException(String.format("Only %d pages allowed", 7));
    }

    public Map<PageLayoutStyle, Integer> getLayouts() {
        return Collections.unmodifiableMap(this.mLayouts);
    }

    public WebTileRefreshResult refresh() {
        WebTileRefreshResult result = new WebTileRefreshResult();
        try {
            readCachedData();
            if (WebTileResourceStyle.FEED == this.mResources.get(0).getStyle()) {
                if (this.mResources.size() != 1) {
                    throw new WebTileException("Invalid local webtile resources.", WebTileErrorType.INVALID_RESOURCE);
                }
                if (this.mWebTilePages.size() != 1) {
                    throw new WebTileException("Invalid local webtile pages.", WebTileErrorType.INVALID_RESOURCE);
                }
                result.setSendAsMessage(PageLayoutStyle.SCROLLING_TEXT == this.mWebTilePages.get(0).getPageLayoutStyle());
                List<Map<String, String>> feedContentMaps = this.mResources.get(0).resolveFeedContentMappings();
                for (int i = feedContentMaps.size() - 1; i >= 0; i--) {
                    result.addPageData(getPageData(-1, feedContentMaps.get(i)));
                    if (result.getDialog() == null) {
                        result.setDialog(getNotificationDialog(feedContentMaps.get(i)));
                    }
                }
            } else {
                boolean resourcesChanged = resolveContentMappings(this.mVariableMappings);
                if (resourcesChanged || this.mLastUpdateError) {
                    result.setPageDatas(getPageDatas(this.mVariableMappings));
                }
                if (resourcesChanged) {
                    result.setDialog(getNotificationDialog(this.mVariableMappings));
                }
                result.setClearPage(this.mLastUpdateError);
                this.mLastUpdateError = false;
            }
        } catch (Exception e) {
            KDKLog.e(TAG, e.getMessage(), e);
            result.addPageData(createErrorPage());
            this.mLastUpdateError = true;
        }
        return result;
    }

    private List<PageData> getPageDatas(Map<String, String> resource) throws WebTileException {
        List<PageData> pageDatas = new ArrayList<>();
        for (int pageIndex = this.mWebTilePages.size() - 1; pageIndex >= 0; pageIndex--) {
            pageDatas.add(getPageData(pageIndex, resource));
        }
        return pageDatas;
    }

    private PageData getPageData(int pageIndex, Map<String, String> resource) throws WebTileException {
        WebTilePage page;
        UUID pageId;
        TextBlockData textBlockData;
        if (pageIndex == -1) {
            page = this.mWebTilePages.get(0);
            pageId = UUID.randomUUID();
        } else {
            page = this.mWebTilePages.get(pageIndex);
            pageId = UUID.fromString(DeviceConstants.GUID_ID_WEBTILE_PAGE_PREFIX + pageIndex);
        }
        int layoutIndex = this.mLayouts.get(page.getPageLayoutStyle()).intValue();
        PageData pageData = new PageData(pageId, layoutIndex);
        if (page.getPageLayoutStyle() == PageLayoutStyle.SINGLE_METRIC_WITH_SECONDARY) {
            pageData.update(new TextBlockData(13, "l"));
        }
        try {
            for (Map.Entry<Integer, String> entry : page.getTextData(resource).entrySet()) {
                int id = entry.getKey().intValue();
                String text = entry.getValue();
                if (PageLayoutStyle.SCROLLING_TEXT == page.getPageLayoutStyle() && id == 2) {
                    textBlockData = new TextBlockData(id, StringUtil.truncateString(text, 160));
                } else {
                    textBlockData = new TextBlockData(id, StringUtil.truncateString(text, 20));
                }
                pageData.update(textBlockData);
            }
            for (Map.Entry<Integer, String> entry2 : page.getIconData(resource).entrySet()) {
                int index = getIconIndex(entry2.getValue());
                if (index == -1) {
                    throw new WebTileException(String.format("Icon %s not defined yet", entry2.getValue()), WebTileErrorType.ICON_NOT_DEFINED);
                }
                pageData.update(new IconData(entry2.getKey().intValue(), index));
            }
            return pageData;
        } catch (WebTileException e) {
            KDKLog.e(TAG, e.getMessage());
            throw e;
        } catch (Exception e2) {
            KDKLog.e(TAG, e2.getMessage());
            throw new WebTileException(String.format("Cannot create PageData with ", e2.getMessage()), WebTileErrorType.PAGE_DATA_ERROR);
        }
    }

    private PageData createErrorPage() {
        UUID pageId = UUID.fromString(DeviceConstants.GUID_ID_WEBTILE_ERROR_PAGE);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return new PageData(pageId, 0).update(new TextBlockData(1, "Data fetch Error")).update(new TextBlockData(2, "There seems to be something wrong with the data for this tile...check back in a few.")).update(new TextBlockData(3, dateFormat.format(new Date())));
    }

    public static String resolveDataBindingExpression(String expression, Map<String, String> resource) {
        Set<String> variables = findContentKey(expression);
        for (String key : variables) {
            if (resource.containsKey(key)) {
                expression = expression.replace("{{" + key + "}}", resource.get(key));
            }
        }
        return expression;
    }

    public static Set<String> findContentKey(String expression) {
        Set<String> result = new HashSet<>();
        Matcher matcher = Pattern.compile(WebTileCondition.PATTERN_VARIABLE).matcher(expression);
        while (matcher.find()) {
            String key = matcher.group();
            result.add(key.substring(2, key.length() - 2));
        }
        return result;
    }

    public boolean resolveContentMappings(Map<String, String> mapping) throws WebTileException {
        boolean resourceChanged = false;
        for (WebTileResource resource : this.mResources) {
            if (resource.resolveContentMappings(mapping) && !resourceChanged) {
                resourceChanged = true;
            }
        }
        return resourceChanged;
    }

    public void setWebTileNotifications(JSONArray notificationJSONArray) throws JSONException {
        for (int i = 0; i < notificationJSONArray.length(); i++) {
            WebTileNotification notification = new WebTileNotification((JSONObject) notificationJSONArray.get(i));
            Set<String> notificationContentKeys = notification.getNotificationContentKeys();
            for (String key : notificationContentKeys) {
                if (!this.mResourceContentMappings.contains(key)) {
                    throw new IllegalArgumentException(String.format("Notification contains [%s] which is not defined in the content mappings", key));
                }
            }
            this.mConsumedContentKeys.addAll(notificationContentKeys);
            this.mNotifications.add(notification);
        }
    }

    public List<WebTileNotification> getWebTileNotifications() {
        return Collections.unmodifiableList(this.mNotifications);
    }

    private NotificationGenericDialog getNotificationDialog(Map<String, String> contentMap) {
        NotificationGenericDialog dialog = null;
        for (WebTileNotification notification : this.mNotifications) {
            try {
                dialog = notification.getNotificationWithResource(contentMap);
            } catch (WebTileException e) {
                KDKLog.w(TAG, e, "WT %s : Notification evaluation is failed with %s", this.mName, e.getMessage());
            }
            if (dialog != null) {
                return dialog;
            }
        }
        return dialog;
    }

    public WebTile(Parcel in) {
        this.mVersion = 1;
        this.mVersionString = LogConstants.METADATA_VERSION;
        this.mRefreshIntervalMinutes = 30;
        this.mResourceContentMappings = new ArrayList();
        this.mConsumedContentKeys = new ArrayList();
        this.mAuthenticationMap = new HashMap();
        this.mDirectory = new File(in.readString());
        this.mTileId = (UUID) in.readValue(UUID.class.getClassLoader());
        this.mName = in.readString();
        this.mDescription = in.readString();
        this.mVersion = in.readInt();
        this.mAuthor = in.readString();
        this.mVersionString = in.readString();
        this.mOrganization = in.readString();
        this.mContactEmail = in.readString();
        this.mTileTheme = (BandTheme) in.readValue(BandTheme.class.getClassLoader());
        this.mManifestVersion = in.readInt();
        this.mRefreshIntervalMinutes = in.readInt();
        this.mWebTilePages = new ArrayList();
        in.readList(this.mWebTilePages, WebTilePage.class.getClassLoader());
        this.mResources = new ArrayList();
        in.readList(this.mResources, WebTileResource.class.getClassLoader());
        this.mTileIconPath = in.readString();
        this.mBadgeIconPath = in.readString();
        this.mAdditionalIconsMap = new LinkedHashMap<>();
        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            this.mAdditionalIconsMap.put(in.readString(), new Pair<>(in.readString(), Integer.valueOf(in.readInt())));
        }
        this.mLayouts = new LinkedHashMap<>();
        int size2 = in.readInt();
        for (int i2 = 0; i2 < size2; i2++) {
            this.mLayouts.put((PageLayoutStyle) in.readValue(PageLayoutStyle.class.getClassLoader()), Integer.valueOf(in.readInt()));
        }
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mDirectory.getAbsolutePath());
        dest.writeValue(this.mTileId);
        dest.writeString(this.mName);
        dest.writeString(this.mDescription);
        dest.writeInt(this.mVersion);
        dest.writeString(this.mAuthor);
        dest.writeString(this.mVersionString);
        dest.writeString(this.mOrganization);
        dest.writeString(this.mContactEmail);
        dest.writeValue(this.mTileTheme);
        dest.writeInt(this.mManifestVersion);
        dest.writeInt(this.mRefreshIntervalMinutes);
        dest.writeList(this.mWebTilePages);
        dest.writeList(this.mResources);
        dest.writeString(this.mTileIconPath);
        dest.writeString(this.mBadgeIconPath);
        dest.writeInt(this.mAdditionalIconsMap.size());
        for (Map.Entry<String, Pair<String, Integer>> entry : this.mAdditionalIconsMap.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeString(entry.getValue().first);
            dest.writeInt(entry.getValue().second.intValue());
        }
        dest.writeInt(this.mLayouts.size());
        for (Map.Entry<PageLayoutStyle, Integer> style : this.mLayouts.entrySet()) {
            dest.writeValue(style.getKey());
            dest.writeInt(style.getValue().intValue());
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean hasRefreshIntervalElapsed(long currentTime) {
        long lastSyncTime = FileHelper.readLongFromFile(getFileInDataFolder(SYNC_TIME_FILE_NAME));
        return currentTime - lastSyncTime > ((long) ((this.mRefreshIntervalMinutes * 60) * 1000));
    }

    public void saveLastSync(long syncCheckTime) throws IOException, JSONException {
        FileHelper.overwriteLongToFile(syncCheckTime, getFileInDataFolder(SYNC_TIME_FILE_NAME));
        saveCachedData();
    }

    private File getFileInDataFolder(String fileName) {
        File dataDirectory = FileHelper.directoryAtPath(this.mDirectory.getParentFile().getAbsolutePath() + FileHelper.DATA_PATH);
        return new File(dataDirectory, fileName);
    }

    Map<String, String> getAuthenticationMap() {
        return Collections.unmodifiableMap(this.mAuthenticationMap);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void saveResourceAuthentication() throws IOException, JSONException, CryptoException {
        saveResourceAuthenticationMap(this.mAuthenticationMap);
    }

    void saveResourceAuthenticationMap(Map<String, String> authenticationMap) throws JSONException, CryptoException, IOException {
        if (authenticationMap.size() > 0) {
            JSONObject json = new JSONObject();
            json.put(JSON_KEY_CRYPT_VERSION, 0);
            CryptoProvider cryptoProvider = new CryptoProviderImpl(this.mDirectory);
            for (Map.Entry<String, String> entry : authenticationMap.entrySet()) {
                json.put(entry.getKey(), cryptoProvider.encrypt(entry.getValue(), 0));
            }
            FileHelper.writeStringToFile(json.toString(), getFileInDataFolder(AUTHENTICATION_FILE_NAME));
        }
    }

    Map<String, String> readResourceAuthentication() throws IOException, JSONException, CryptoException {
        Map<String, String> authenticationMap = new HashMap<>();
        String data = FileHelper.readStringFromFile(getFileInDataFolder(AUTHENTICATION_FILE_NAME));
        if (data != null) {
            CryptoProvider cryptoProvider = new CryptoProviderImpl(this.mDirectory);
            JSONObject json = new JSONObject(data);
            int cryptVersion = 0;
            if (json.has(JSON_KEY_CRYPT_VERSION)) {
                cryptVersion = json.getInt(JSON_KEY_CRYPT_VERSION);
            }
            Iterator<String> keys = json.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                if (!JSON_KEY_CRYPT_VERSION.equals(key)) {
                    authenticationMap.put(key, cryptoProvider.decrypt(json.getString(key), cryptVersion));
                }
            }
        }
        return authenticationMap;
    }

    public boolean authenticateResource(WebTileResource resource) throws WebTileException {
        Validation.validateNullParameter(resource, "Resource");
        String url = resource.getUrl().toString();
        if (this.mAuthenticationMap.containsKey(url)) {
            return true;
        }
        boolean result = resource.authenticate();
        String header = resource.getAuthenticationHeader();
        if (result && header != null) {
            this.mAuthenticationMap.put(url, header);
            return result;
        }
        return result;
    }

    public void setAuthenticationHeader(WebTileResource resource, String username, String password) {
        resource.setAuthenticationHeader(username, password);
    }

    void saveCachedData() throws IOException, JSONException {
        JSONObject json = new JSONObject();
        JSONObject jsonResource = new JSONObject();
        for (WebTileResource resource : this.mResources) {
            String resourceCache = resource.getResourceCacheInfo().toJsonString();
            if (resourceCache != null) {
                jsonResource.put(resource.getUrl().toString(), resourceCache);
            }
        }
        json.put(JSON_KEY_RESOURCE_CACHE_INFOS, jsonResource);
        if (this.mVariableMappings.size() > 0) {
            json.put(JSON_KEY_VARIABLE_MAPPINGS, new JSONObject(this.mVariableMappings));
        }
        json.put(JSON_KEY_LAST_UPDATED_ERROR, this.mLastUpdateError);
        synchronized (this) {
            FileHelper.writeStringToFile(json.toString(), getFileInDataFolder(CACHED_DATA_FILE_NAME));
        }
    }

    void readCachedData() throws IOException, JSONException {
        synchronized (this) {
            String data = FileHelper.readStringFromFile(getFileInDataFolder(CACHED_DATA_FILE_NAME));
            if (data != null) {
                JSONObject json = new JSONObject(data);
                if (json.has(JSON_KEY_RESOURCE_CACHE_INFOS)) {
                    JSONObject jsonCacheInfo = json.optJSONObject(JSON_KEY_RESOURCE_CACHE_INFOS);
                    Iterator<String> keys = jsonCacheInfo.keys();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        JSONObject jsonResourceCacheInfo = new JSONObject(jsonCacheInfo.getString(key));
                        if (jsonResourceCacheInfo != null && jsonResourceCacheInfo.length() != 0) {
                            WebTileResourceCacheInfo resourceCacheInfo = new WebTileResourceCacheInfo(jsonResourceCacheInfo);
                            for (WebTileResource resource : this.mResources) {
                                if (key.equals(resource.getUrl().toString())) {
                                    resource.setResourceCacheInfo(resourceCacheInfo);
                                }
                            }
                        }
                    }
                }
                if (json.has(JSON_KEY_VARIABLE_MAPPINGS)) {
                    JSONObject jsonVariable = json.optJSONObject(JSON_KEY_VARIABLE_MAPPINGS);
                    Iterator<String> keys2 = jsonVariable.keys();
                    while (keys2.hasNext()) {
                        String key2 = keys2.next();
                        this.mVariableMappings.put(key2, jsonVariable.getString(key2));
                    }
                }
                if (json.has(JSON_KEY_LAST_UPDATED_ERROR)) {
                    this.mLastUpdateError = json.getBoolean(JSON_KEY_LAST_UPDATED_ERROR);
                }
            }
        }
    }

    Map<String, String> getVariableMappings() {
        return Collections.unmodifiableMap(this.mVariableMappings);
    }

    void setVariableMappings(Map<String, String> variableMappings) {
        this.mVariableMappings = variableMappings;
    }

    boolean isLastUpdateError() {
        return this.mLastUpdateError;
    }

    void setLastUpdateError(boolean lastUpdateError) {
        this.mLastUpdateError = lastUpdateError;
    }
}
