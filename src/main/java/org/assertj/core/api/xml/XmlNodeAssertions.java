package org.assertj.core.api.xml;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Node;


class XmlNodeAssertions {

  @SuppressWarnings("serial")
  private static final Map<Short, Class<? extends XmlNodeAssert>> SUPPORTED_ASSERTIONS = new HashMap<Short, Class<? extends XmlNodeAssert>>(){{
    put(Node.ATTRIBUTE_NODE, XmlAttributeAssert.class);
    put(Node.TEXT_NODE, XmlTextNodeAssert.class);
    put(Node.ELEMENT_NODE, XmlElementAssert.class);
    put(Node.COMMENT_NODE, XmlCommentAssert.class);
  }};

  private XmlNodeAssertions() {
    // utility class
  }

  static XmlNodeAssert assertFor(Node item) {
    
    try {
      
      return SUPPORTED_ASSERTIONS.get(item.getNodeType()).getConstructor(Node.class).newInstance(item);
      
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
}
