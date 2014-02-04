package org.assertj.core.api.xml;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.internal.Xmls;
import org.w3c.dom.Node;

public class AbstractXmlNodeAssert extends AbstractAssert<AbstractXmlNodeAssert, Node> implements XmlNodeAssert {

  protected Xmls xmls = Xmls.instance();
  
  protected AbstractXmlNodeAssert(Node actual, Class<?> selfType) {
    super(actual, selfType);
  }

  @Override
  public void isElement() {
    
  }

}
