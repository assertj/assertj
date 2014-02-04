package org.assertj.core.api.xml;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.internal.Xmls;
import org.w3c.dom.Node;

public abstract class AbstractXmlNodeAssert extends AbstractAssert<AbstractXmlNodeAssert, Node> implements XmlNodeAssert {

  protected Xmls xmls = Xmls.instance();
  
  protected AbstractXmlNodeAssert(Node actual, Class<?> selfType) {
    super(actual, selfType);
  }

  protected abstract String type();

  @Override
  public void isElement() {
    xmls.failNotElementBut(info, type());
  }

  @Override
  public void isAttribute() {
    xmls.failNotAttributeBut(info, type());
  }
  
  @Override
  public void isComment() {
    xmls.failNotCommentBut(info, type());
  }

  @Override
  public void isTextNode() {
    xmls.failNotTextNodeBut(info, type());
  }
}
