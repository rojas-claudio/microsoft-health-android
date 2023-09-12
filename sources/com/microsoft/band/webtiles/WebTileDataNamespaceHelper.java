package com.microsoft.band.webtiles;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.namespace.NamespaceContext;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
/* loaded from: classes.dex */
public class WebTileDataNamespaceHelper implements NamespaceContext {
    public static final String DEFAULT_NS = "msbwt";
    private Map<String, String> mPrefix2Uri = new HashMap();
    private Map<String, String> mUri2Prefix = new HashMap();

    public WebTileDataNamespaceHelper(Document document) {
        examineNode(document.getDocumentElement());
    }

    private void examineNode(Node node) {
        NamedNodeMap attributes = node.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node attribute = attributes.item(i);
            storeAttribute((Attr) attribute);
        }
        NodeList children = node.getChildNodes();
        for (int i2 = 0; i2 < children.getLength(); i2++) {
            Node child = children.item(i2);
            if (child.getNodeType() == 1) {
                examineNode(child);
            }
        }
    }

    private void storeAttribute(Attr attribute) {
        if (attribute.getNamespaceURI() != null && attribute.getNamespaceURI().equals("http://www.w3.org/2000/xmlns/")) {
            if (attribute.getNodeName().equals("xmlns")) {
                putInCache(DEFAULT_NS, attribute.getNodeValue());
            } else {
                putInCache(attribute.getLocalName(), attribute.getNodeValue());
            }
        }
    }

    private void putInCache(String prefix, String uri) {
        this.mPrefix2Uri.put(prefix, uri);
        this.mUri2Prefix.put(uri, prefix);
    }

    @Override // javax.xml.namespace.NamespaceContext
    public String getNamespaceURI(String prefix) {
        return (prefix == null || prefix.equals("")) ? this.mPrefix2Uri.get(DEFAULT_NS) : this.mPrefix2Uri.get(prefix);
    }

    @Override // javax.xml.namespace.NamespaceContext
    public String getPrefix(String namespaceURI) {
        return this.mUri2Prefix.get(namespaceURI);
    }

    @Override // javax.xml.namespace.NamespaceContext
    public Iterator getPrefixes(String namespaceURI) {
        return null;
    }
}
