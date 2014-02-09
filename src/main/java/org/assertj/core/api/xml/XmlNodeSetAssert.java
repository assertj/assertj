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
  private Map<Short, Class<? extends XmlNodeAssert>> supportedAssertions = new HashMap<Short, Class<? extends XmlNodeAssert>>(){{
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

  public XmlNodeSetAssert isEmpty() {
    
    xmls.assertIsEmpty(info, actual);
    return myself;
  }

  public XmlNodeSetAssert hasSize(int expectedSize) {
    
    xmls.assertHasSize(info, actual, expectedSize);
    return myself;
  }

  public XmlNodeSetAssert isElement() {
    
    assertFor(singleNode()).isElement();
    return myself;
  }

  public XmlNodeSetAssert isAttribute() {
    
    assertFor(singleNode()).isAttribute();
    return myself;
  }

  public XmlNodeSetAssert isComment() {
    
    assertFor(singleNode()).isComment();
    return myself;
  }
  
  public XmlNodeSetAssert isTextNode() {
    
    assertFor(singleNode()).isTextNode();
    return myself;
  }
  
  public XmlNodeSetAssert isEqualTo(String expectedXml) {
    
    assertFor(singleNode()).isEqualTo(expectedXml);
    return myself;
  }
  
  public Node singleNode() {
    
    xmls.assertIsSingleNode(info, actual);
    return actual.item(0);
  }

  private XmlNodeAssert assertFor(Node item) {
    
    try {
      
      return supportedAssertions.get(item.getNodeType()).getConstructor(Node.class).newInstance(item);
      
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public XmlNodeSetAssert contains(String... expectedNodes) {
    
    xmls.assertContains(info, actual, expectedNodes);
    return myself;
  }

  public XmlNodeSetAssert containsExactly(String... expectedNodes) {
    
    xmls.assertContainsExactly(info, actual, expectedNodes);
    return myself;
  }

  public XmlNodeSetAssert containsOnly(String... expectedNodes) {
    
    xmls.assertContainsOnly(info, actual, expectedNodes);
    return myself;
  }

}
