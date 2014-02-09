package org.assertj.core.api.xml;

import org.w3c.dom.Node;

public class XmlAttributeAssert extends AbstractXmlNodeAssert {

  public XmlAttributeAssert(Node actual) {
    super(actual, XmlAttributeAssert.class);
  }
  
  @Override
  public XmlNodeAssert isAttribute() {
    
    // success
    return myself;
  }

  @Override
  protected String type() {
    return "attribute";
  }

}
