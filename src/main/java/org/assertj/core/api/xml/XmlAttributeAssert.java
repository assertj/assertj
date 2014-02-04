package org.assertj.core.api.xml;

import org.w3c.dom.Node;

public class XmlAttributeAssert extends AbstractXmlNodeAssert {

  public XmlAttributeAssert(Node actual) {
    super(actual, XmlAttributeAssert.class);
  }

  @Override
  public void isElement() {
    xmls.failNotElementBut(info, "attribute found");
  }

}
