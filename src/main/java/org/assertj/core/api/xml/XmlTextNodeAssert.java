package org.assertj.core.api.xml;

import org.w3c.dom.Node;

public class XmlTextNodeAssert extends AbstractXmlNodeAssert {

  public XmlTextNodeAssert(Node actual) {
    super(actual, XmlTextNodeAssert.class);
  }
  
  @Override
  public XmlNodeAssert isTextNode() {
    
    // success
    return myself;
  }
  
  @Override
  protected String type() {
    return "text node";
  }
  
}
