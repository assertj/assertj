package org.assertj.core.api.xml;

import org.w3c.dom.Node;

public class XmlTextAssert extends AbstractXmlNodeAssert {

  public XmlTextAssert(Node actual) {
    super(actual, XmlTextAssert.class);
  }

  @Override
  public void isElement() {
    xmls.failNotElementBut(info, "text found");
  }

}
