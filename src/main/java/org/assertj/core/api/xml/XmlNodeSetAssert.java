package org.assertj.core.api.xml;

import java.util.HashMap;
import java.util.Map;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.internal.Xmls;
import org.assertj.core.util.xml.XPathExtractor;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlNodeSetAssert extends AbstractAssert<XmlNodeSetAssert, NodeList> {

  private Xmls xmls = Xmls.instance();
  
  @SuppressWarnings("serial")
  private Map<Short, Class<? extends XmlNodeAssert>> mapping = new HashMap<Short, Class<? extends XmlNodeAssert>>(){{
    put(Node.ATTRIBUTE_NODE, XmlAttributeAssert.class);
    put(Node.TEXT_NODE, XmlTextNodeAssert.class);
    put(Node.ELEMENT_NODE, XmlElementAssert.class);
    put(Node.COMMENT_NODE, XmlCommentAssert.class);
  }};

  public XmlNodeSetAssert(NodeList nodeList) {
    super(nodeList, XmlNodeSetAssert.class);
  }

  public XmlNodeSetAssert extractingXPath(String xpath) {
    return new XmlNodeSetAssert(new XPathExtractor(actual).extract(xpath));
  }

  public void isEmpty() {
    xmls.assertIsEmpty(info, actual);
  }

  public void hasSize(int expectedSize) {
    xmls.assertHasSize(info, actual, expectedSize);
  }

  public void isElement() {
    
    assertFor(singleNode()).isElement();
  }

  public void isAttribute() {
    
    assertFor(singleNode()).isAttribute();
  }

  public void isComment() {
    
    assertFor(singleNode()).isComment();
  }
  
  public void isTextNode() {
    
    assertFor(singleNode()).isTextNode();
  }
  
  public Node singleNode() {
    xmls.assertIsSingleNode(info, actual);
    Node item = actual.item(0);
    return item;
  }

  private XmlNodeAssert assertFor(Node item) {
    
    try {
      
      return mapping.get(item.getNodeType()).getConstructor(Node.class).newInstance(item);
      
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }


}
