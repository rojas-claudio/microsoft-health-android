package com.microsoft.band.webtiles;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.Html;
import android.util.Base64;
import com.google.android.gms.plus.PlusShare;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.jayway.jsonpath.Predicate;
import com.microsoft.band.internal.util.KDKLog;
import com.microsoft.band.internal.util.StringUtil;
import com.microsoft.band.internal.util.Validation;
import com.microsoft.kapp.utils.Constants;
import com.unnamed.b.atv.model.TreeNode;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
/* loaded from: classes.dex */
public class WebTileResource implements Parcelable {
    private static final String ATOM_NAMESPACE = "http://www.w3.org/2005/Atom";
    private static final String CONTENT_NOT_FOUND = "--";
    private static final int HTTP_CONNECTION_TIMEOUT = 2000;
    private static final int HTTP_SO_TIMEOUT = 60000;
    private static final int JSON = 1;
    private static final String JSON_KEY_RESOURCE_URL = "url";
    private static final String JSON_KEY_RESOURCE_content = "content";
    private static final String JSON_KEY_RESOURCE_style = "style";
    public static final int MAX_FEED = 8;
    private static final int XML = 0;
    private String mAuthenticationHeader;
    private List<WebTileContentMapping> mContentMapping;
    private WebTileResourceCacheInfo mResourceCacheInfo;
    private WebTileResourceStyle mStyle;
    private int mURLType;
    private URL mUrl;
    private static final String TAG = WebTileResource.class.getSimpleName();
    public static final Parcelable.Creator<WebTileResource> CREATOR = new Parcelable.Creator<WebTileResource>() { // from class: com.microsoft.band.webtiles.WebTileResource.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WebTileResource createFromParcel(Parcel in) {
            return new WebTileResource(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WebTileResource[] newArray(int size) {
            return new WebTileResource[size];
        }
    };

    public WebTileResource(JSONObject json) throws JSONException {
        this.mStyle = WebTileResourceStyle.SIMPLE;
        this.mURLType = -1;
        this.mAuthenticationHeader = null;
        this.mContentMapping = new ArrayList();
        if (json.has("url")) {
            setUrl(json.getString("url"));
            if (json.has("style")) {
                setStyle(json.getString("style"));
            }
            if (json.has(JSON_KEY_RESOURCE_content)) {
                setContentMapping(json.getJSONObject(JSON_KEY_RESOURCE_content));
                this.mResourceCacheInfo = new WebTileResourceCacheInfo();
                return;
            }
            throw new IllegalArgumentException("Content is required for WebTile resource.");
        }
        throw new IllegalArgumentException("URL is required for WebTile resource.");
    }

    public List<WebTileContentMapping> getContentMapping() {
        return this.mContentMapping;
    }

    public void setContentMapping(JSONObject json) throws JSONException {
        Iterator<String> keys = json.keys();
        if (!keys.hasNext()) {
            throw new IllegalArgumentException("Empty content mapping is not supported for WebTile resource.");
        }
        while (keys.hasNext()) {
            String key = keys.next();
            Validation.validateVariableName(key, "Resource content name");
            String content = json.getString(key);
            this.mContentMapping.add(new WebTileContentMapping(key, content));
        }
    }

    public WebTileResourceStyle getStyle() {
        return this.mStyle;
    }

    public void setStyle(String style) {
        if ("simple".equalsIgnoreCase(style)) {
            this.mStyle = WebTileResourceStyle.SIMPLE;
        } else if ("feed".equalsIgnoreCase(style)) {
            this.mStyle = WebTileResourceStyle.FEED;
        } else {
            throw new IllegalArgumentException("Only Simple and Feed Styles are allowed");
        }
    }

    public URL getUrl() {
        return this.mUrl;
    }

    public void setUrl(String urlString) {
        try {
            this.mUrl = new URL(urlString);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("MalformedURL :" + e.getMessage());
        }
    }

    public Object downloadResource(boolean requiresXml) throws WebTileException {
        String uri = getUrl().toString();
        HttpResponse response = getResourceResponse();
        int statusCode = response.getStatusLine().getStatusCode();
        if (304 == statusCode) {
            KDKLog.i(TAG, "No new data for resource [%s]", uri);
            return null;
        } else if (200 != statusCode) {
            throw new WebTileException(String.format("Failed to connect url %s with status code = %d", uri, Integer.valueOf(statusCode)));
        } else {
            Header etag = response.getFirstHeader("ETag");
            if (etag != null) {
                this.mResourceCacheInfo.setETag(etag.getValue());
            }
            Header lastModifiedTime = response.getFirstHeader("Last-Modified");
            if (lastModifiedTime != null) {
                this.mResourceCacheInfo.setLastModified(lastModifiedTime.getValue());
            }
            try {
                InputStream is = response.getEntity().getContent();
                if (requiresXml) {
                    try {
                        return loadXmlDocument(is);
                    } catch (Exception e) {
                        KDKLog.e(TAG, e.getMessage());
                        throw new WebTileException(e);
                    }
                }
                try {
                    byte[] buff = new byte[8000];
                    ByteArrayOutputStream bao = new ByteArrayOutputStream();
                    while (true) {
                        int bytesRead = is.read(buff);
                        if (bytesRead != -1) {
                            bao.write(buff, 0, bytesRead);
                        } else {
                            byte[] data = bao.toByteArray();
                            try {
                                return loadXmlDocument(new ByteArrayInputStream(data));
                            } catch (Exception e2) {
                                KDKLog.i(TAG, e2.getMessage());
                                try {
                                    String jsonString = new String(data, "UTF-8");
                                    try {
                                        JSONObject jSONObject = new JSONObject(jsonString);
                                        this.mURLType = 1;
                                        return jSONObject;
                                    } catch (JSONException e3) {
                                        try {
                                            JSONArray jsonArray = new JSONArray(jsonString);
                                            this.mURLType = 1;
                                            return jsonArray;
                                        } catch (JSONException ex1) {
                                            KDKLog.e(TAG, ex1.getMessage());
                                            throw new WebTileException(String.format("Failed to recognized url %s", uri));
                                        }
                                    }
                                } catch (Exception e4) {
                                    KDKLog.e(TAG, e4.getMessage());
                                    throw new WebTileException(String.format("Failed to recognized url %s", uri));
                                }
                            }
                        }
                    }
                } catch (Exception e5) {
                    throw new WebTileException(String.format("Failed to read bytes from url %s with error %s", uri, e5.getMessage()));
                }
            } catch (Exception e6) {
                throw new WebTileException(String.format("Failed to get input stream from url %s with error = %d", uri, e6.getMessage()));
            }
        }
    }

    private Document loadXmlDocument(InputStream is) throws Exception {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        docFactory.setNamespaceAware(true);
        DocumentBuilder builder = docFactory.newDocumentBuilder();
        Document doc = builder.parse(is);
        is.close();
        this.mURLType = 0;
        return doc;
    }

    public boolean resolveContentMappings(Map<String, String> mappings) throws WebTileException {
        Object data = downloadResource(false);
        if (data == null) {
            return false;
        }
        int type = getURLType();
        if (type == 0) {
            Document xmlDoc = (Document) data;
            Node xmlRootNode = xmlDoc.getDocumentElement();
            resolveContentMappings(mappings, xmlRootNode);
        } else if (1 == type) {
            resolveContentMappings(mappings, data.toString());
        }
        return true;
    }

    public void resolveContentMappings(Map<String, String> mappings, Node node) throws WebTileException {
        try {
            Document xmlDoc = replaceNameSpace(node);
            XPath xpath = XPathFactory.newInstance().newXPath();
            xpath.setNamespaceContext(new WebTileDataNamespaceHelper(xmlDoc));
            for (WebTileContentMapping contentMapping : getContentMapping()) {
                String name = contentMapping.getTemplatePattern();
                if (!mappings.containsKey(name)) {
                    mappings.put(name, evaluateXmlPath(xmlDoc, xpath, contentMapping.getContentPath()));
                }
            }
        } catch (Exception e) {
            throw new WebTileException(e);
        }
    }

    private String evaluateXmlPath(Document xmlDoc, XPath xpath, String expresion) throws XPathExpressionException {
        Node result = (Node) xpath.evaluate(expresion, xmlDoc, XPathConstants.NODE);
        return result == null ? CONTENT_NOT_FOUND : stripHtmlTag(result.getTextContent());
    }

    private String stripHtmlTag(String text) {
        String textWithoutImg = text.replaceAll("(<(/)img>)|(<img.+?>)", "");
        return Html.fromHtml(textWithoutImg).toString().trim();
    }

    private Document replaceNameSpace(Node node) throws WebTileException {
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty("omit-xml-declaration", "yes");
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(node), new StreamResult(writer));
            String output = writer.getBuffer().toString().replaceAll("\n|\r", "");
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            docFactory.setNamespaceAware(true);
            DocumentBuilder builder = docFactory.newDocumentBuilder();
            return builder.parse(new InputSource(new StringReader(output.replaceAll("xmlns=", "xmlns:msbwt="))));
        } catch (Exception e) {
            throw new WebTileException(e);
        }
    }

    public void resolveContentMappings(Map<String, String> mappings, String json) throws WebTileException {
        String jsonValue;
        try {
            Object document = Configuration.defaultConfiguration().jsonProvider().parse(json);
            for (WebTileContentMapping contentMapping : getContentMapping()) {
                String name = contentMapping.getTemplatePattern();
                if (!mappings.containsKey(name)) {
                    try {
                        Object jsonResult = JsonPath.read(document, contentMapping.getContentPath(), new Predicate[0]);
                        if (jsonResult == null) {
                            jsonValue = "";
                        } else {
                            jsonValue = jsonResult.toString();
                        }
                    } catch (PathNotFoundException e) {
                        jsonValue = CONTENT_NOT_FOUND;
                    }
                    mappings.put(name, jsonValue);
                }
            }
        } catch (Exception e2) {
            throw new WebTileException(e2);
        }
    }

    private int getURLType() {
        return this.mURLType;
    }

    private boolean isAtom(Document doc) {
        Element e = doc.getDocumentElement();
        return "feed".equals(e.getLocalName());
    }

    public List<Map<String, String>> resolveFeedContentMappings() throws WebTileException {
        Object data = downloadResource(true);
        return data == null ? new ArrayList() : getFeedContentMappings((Document) data);
    }

    List<Map<String, String>> getFeedContentMappings(Document data) throws WebTileException {
        XPathExpression expr_feed;
        try {
            List<Map<String, String>> mappings = new ArrayList<>();
            Document xmlDoc = replaceNameSpace(data);
            XPathFactory factory = XPathFactory.newInstance();
            XPath xpath = factory.newXPath();
            NamespaceContext namespaceHelper = new WebTileDataNamespaceHelper(xmlDoc);
            xpath.setNamespaceContext(namespaceHelper);
            boolean isRss = false;
            if (!isAtom(xmlDoc)) {
                expr_feed = xpath.compile("/rss/channel/item");
                isRss = true;
            } else {
                String atomPrefix = namespaceHelper.getPrefix(ATOM_NAMESPACE);
                if (!atomPrefix.equals(WebTileDataNamespaceHelper.DEFAULT_NS) && !ATOM_NAMESPACE.equals(namespaceHelper.getNamespaceURI(WebTileDataNamespaceHelper.DEFAULT_NS))) {
                    expr_feed = xpath.compile("/" + atomPrefix + ":feed/" + atomPrefix + ":entry");
                } else {
                    expr_feed = xpath.compile("/feed/entry");
                }
            }
            NodeList result_feed = (NodeList) expr_feed.evaluate(xmlDoc, XPathConstants.NODESET);
            Map<String, String> mappingWithAbsolutePath = new HashMap<>();
            for (WebTileContentMapping contentMapping : getContentMapping()) {
                String name = contentMapping.getTemplatePattern();
                if (!mappingWithAbsolutePath.containsKey(name)) {
                    String path = contentMapping.getContentPath();
                    if (path.startsWith("/") || path.startsWith("//")) {
                        mappingWithAbsolutePath.put(name, evaluateXmlPath(xmlDoc, xpath, path));
                    }
                }
            }
            if (mappingWithAbsolutePath.size() == getContentMapping().size()) {
                mappings.add(mappingWithAbsolutePath);
            } else {
                List<String> currentItemIds = new ArrayList<>();
                for (int i = 0; i < Math.min(8, result_feed.getLength()); i++) {
                    Map<String, String> mapping = new HashMap<>();
                    Node xmlNode = result_feed.item(i);
                    if (xmlNode != null && xmlNode.getNodeType() == 1) {
                        if (mappingWithAbsolutePath.size() > 0) {
                            mapping.putAll(mappingWithAbsolutePath);
                        }
                        String itemId = getUniqueItemId(xpath, isRss, xmlNode);
                        if (itemId != null) {
                            if (this.mResourceCacheInfo.getFeedItemIds().contains(itemId)) {
                                break;
                            }
                            currentItemIds.add(itemId);
                        }
                        for (WebTileContentMapping contentMapping2 : getContentMapping()) {
                            String name2 = contentMapping2.getTemplatePattern();
                            if (!mapping.containsKey(name2)) {
                                XPathExpression expr = xpath.compile(contentMapping2.getContentPath());
                                NodeList item = (NodeList) expr.evaluate(xmlNode, XPathConstants.NODESET);
                                if (item.item(0) != null) {
                                    mapping.put(name2, stripHtmlTag(item.item(0).getTextContent()));
                                } else {
                                    mapping.put(name2, CONTENT_NOT_FOUND);
                                }
                            }
                        }
                        mappings.add(mapping);
                    }
                }
                addItemIdsToCache(currentItemIds);
            }
            return mappings;
        } catch (Exception e) {
            throw new WebTileException(e);
        }
    }

    WebTileResource(Parcel in) {
        this.mStyle = WebTileResourceStyle.SIMPLE;
        this.mURLType = -1;
        this.mAuthenticationHeader = null;
        this.mUrl = (URL) in.readValue(URL.class.getClassLoader());
        this.mStyle = (WebTileResourceStyle) in.readValue(WebTileResourceStyle.class.getClassLoader());
        this.mContentMapping = new ArrayList();
        in.readList(this.mContentMapping, WebTileContentMapping.class.getClassLoader());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.mUrl);
        dest.writeValue(this.mStyle);
        dest.writeList(this.mContentMapping);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setAuthenticationHeader(String username, String password) {
        Validation.validateNullParameter(username, "User name");
        Validation.validateStringEmptyOrWhiteSpace(username, "User name");
        Validation.validateNullParameter(password, "password");
        Validation.validateStringEmptyOrWhiteSpace(password, "password");
        String authString = username + TreeNode.NODES_ID_SEPARATOR + password;
        this.mAuthenticationHeader = Base64.encodeToString(authString.getBytes(), 2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setAuthenticationHeader(String header) {
        Validation.validateNullParameter(header, "Authentication Header");
        this.mAuthenticationHeader = header;
    }

    public String getAuthenticationHeader() {
        return this.mAuthenticationHeader;
    }

    HttpResponse getResourceResponse() throws WebTileException {
        try {
            HttpGet get = new HttpGet();
            get.setURI(new URI(getUrl().toString()));
            if (this.mAuthenticationHeader != null) {
                get.setHeader(Constants.AUTHORIZATION_HEADER_NAME, "Basic " + this.mAuthenticationHeader);
            }
            if (this.mResourceCacheInfo.getETag() != null) {
                get.setHeader("If-None-Match", this.mResourceCacheInfo.getETag());
            }
            if (this.mResourceCacheInfo.getLastModified() != null) {
                get.setHeader("If-Modified-Since", this.mResourceCacheInfo.getLastModified());
            }
            BasicHttpParams basicHttpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(basicHttpParams, 2000);
            HttpConnectionParams.setSoTimeout(basicHttpParams, 60000);
            basicHttpParams.setParameter("http.protocol.handle-authentication", false);
            return new DefaultHttpClient(basicHttpParams).execute(get);
        } catch (Exception e) {
            throw new WebTileException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WebTileResourceCacheInfo getResourceCacheInfo() {
        return this.mResourceCacheInfo;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setResourceCacheInfo(WebTileResourceCacheInfo resourceCacheInfo) {
        this.mResourceCacheInfo = resourceCacheInfo;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean authenticate() throws WebTileException {
        HttpResponse response = getResourceResponse();
        int statusCode = response.getStatusLine().getStatusCode();
        if (200 == statusCode) {
            return true;
        }
        if (401 == statusCode) {
            if ("https".equals(getUrl().getProtocol())) {
                return false;
            }
            throw new WebTileException(String.format("Http is not supported for authentication for webtile. Url [%s] ", getUrl().toString()));
        }
        throw new WebTileException(String.format("Failed to connect url %s with status code = %d", getUrl().toString(), Integer.valueOf(statusCode)));
    }

    String getUniqueItemId(XPath xpath, boolean isRSS, Node xmlNode) throws XPathExpressionException, UnsupportedEncodingException, DOMException {
        String id = null;
        String[] uniqueRSSTags = {"guid", "pubDate", PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_DESCRIPTION};
        String[] uniqueATOMTags = {"id", "updated", "published", "summary"};
        String[] uniqueTagsToCheck = isRSS ? uniqueRSSTags : uniqueATOMTags;
        String[] arr$ = uniqueTagsToCheck;
        for (String tag : arr$) {
            XPathExpression expr = xpath.compile(tag);
            NodeList item = (NodeList) expr.evaluate(xmlNode, XPathConstants.NODESET);
            if (item.item(0) != null) {
                if (PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_DESCRIPTION.equals(tag) || "summary".equals(tag)) {
                    id = new String(StringUtil.toMD5Hash(item.item(0).getTextContent()), "UTF-8");
                } else {
                    id = item.item(0).getTextContent();
                }
            }
            if (id != null) {
                break;
            }
        }
        if (id == null) {
            StringBuilder idString = new StringBuilder();
            NodeList list = xmlNode.getChildNodes();
            for (int i = 0; i < list.getLength(); i++) {
                idString.append(list.item(i).getNodeName()).append(TreeNode.NODES_ID_SEPARATOR);
                idString.append(list.item(i).getTextContent()).append(";");
            }
            String id2 = new String(StringUtil.toMD5Hash(idString.toString()), "UTF-8");
            return id2;
        }
        return id;
    }

    private void addItemIdsToCache(List<String> ids) {
        if (this.mResourceCacheInfo.getFeedItemIds().size() == 0) {
            this.mResourceCacheInfo.setFeedItemIds(ids);
        } else {
            this.mResourceCacheInfo.addFeedItemIds(ids);
        }
        if (this.mResourceCacheInfo.getFeedItemIds().size() > 8) {
            this.mResourceCacheInfo.setFeedItemIds(this.mResourceCacheInfo.getFeedItemIds().subList(0, 8));
        }
    }
}
