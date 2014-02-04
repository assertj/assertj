package org.assertj.core.api.xml;

import org.w3c.dom.Node;

public class XmlElementAssert extends AbstractXmlNodeAssert {

  public XmlElementAssert(Node actual) {
    super(actual, XmlElementAssert.class);
  }

  @Override
  public void isElement() {

  }

}
