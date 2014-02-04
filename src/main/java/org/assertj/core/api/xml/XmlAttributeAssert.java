package org.assertj.core.api.xml;

import org.w3c.dom.Node;

public class XmlAttributeAssert extends AbstractXmlNodeAssert {

  public XmlAttributeAssert(Node actual) {
    super(actual, XmlAttributeAssert.class);
  }

  @Override
  protected String type() {
    return "attribute";
  }
  
  @Override
  public void isAttribute() {
    // success
  }

}
