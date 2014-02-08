package org.assertj.core.util.xml;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;

public class PrintableNode implements Node {

  private final Node delegate;

  public PrintableNode(Node delegate) {
    this.delegate = delegate;
  }
  
  @Override
  public String toString() {
    return "\"" + XmlStringPrettyFormatter.prettyFormat(delegate) + "\"";
  }
  
  public String getNodeName() {
    return delegate.getNodeName();
  }

  public String getNodeValue() throws DOMException {
    return delegate.getNodeValue();
  }

  public void setNodeValue(String nodeValue) throws DOMException {
    delegate.setNodeValue(nodeValue);
  }

  public short getNodeType() {
    return delegate.getNodeType();
  }

  public Node getParentNode() {
    return delegate.getParentNode();
  }

  public NodeList getChildNodes() {
    return delegate.getChildNodes();
  }

  public Node getFirstChild() {
    return delegate.getFirstChild();
  }

  public Node getLastChild() {
    return delegate.getLastChild();
  }

  public Node getPreviousSibling() {
    return delegate.getPreviousSibling();
  }

  public Node getNextSibling() {
    return delegate.getNextSibling();
  }

  public NamedNodeMap getAttributes() {
    return delegate.getAttributes();
  }

  public Document getOwnerDocument() {
    return delegate.getOwnerDocument();
  }

  public Node insertBefore(Node newChild, Node refChild) throws DOMException {
    return delegate.insertBefore(newChild, refChild);
  }

  public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
    return delegate.replaceChild(newChild, oldChild);
  }

  public Node removeChild(Node oldChild) throws DOMException {
    return delegate.removeChild(oldChild);
  }

  public Node appendChild(Node newChild) throws DOMException {
    return delegate.appendChild(newChild);
  }

  public boolean hasChildNodes() {
    return delegate.hasChildNodes();
  }

  public Node cloneNode(boolean deep) {
    return delegate.cloneNode(deep);
  }

  public void normalize() {
    delegate.normalize();
  }

  public boolean isSupported(String feature, String version) {
    return delegate.isSupported(feature, version);
  }

  public String getNamespaceURI() {
    return delegate.getNamespaceURI();
  }

  public String getPrefix() {
    return delegate.getPrefix();
  }

  public void setPrefix(String prefix) throws DOMException {
    delegate.setPrefix(prefix);
  }

  public String getLocalName() {
    return delegate.getLocalName();
  }

  public boolean hasAttributes() {
    return delegate.hasAttributes();
  }

  public String getBaseURI() {
    return delegate.getBaseURI();
  }

  public short compareDocumentPosition(Node other) throws DOMException {
    return delegate.compareDocumentPosition(other);
  }

  public String getTextContent() throws DOMException {
    return delegate.getTextContent();
  }

  public void setTextContent(String textContent) throws DOMException {
    delegate.setTextContent(textContent);
  }

  public boolean isSameNode(Node other) {
    return delegate.isSameNode(other);
  }

  public String lookupPrefix(String namespaceURI) {
    return delegate.lookupPrefix(namespaceURI);
  }

  public boolean isDefaultNamespace(String namespaceURI) {
    return delegate.isDefaultNamespace(namespaceURI);
  }

  public String lookupNamespaceURI(String prefix) {
    return delegate.lookupNamespaceURI(prefix);
  }

  public boolean isEqualNode(Node arg) {
    return delegate.isEqualNode(arg);
  }

  public Object getFeature(String feature, String version) {
    return delegate.getFeature(feature, version);
  }

  public Object setUserData(String key, Object data, UserDataHandler handler) {
    return delegate.setUserData(key, data, handler);
  }

  public Object getUserData(String key) {
    return delegate.getUserData(key);
  }

  @Override
  public boolean equals(Object x) {
    
    Node other = (Node) x;
    return XmlUtil.areEqual(this, other);
  }
}