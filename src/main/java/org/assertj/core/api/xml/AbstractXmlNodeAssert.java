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
  public XmlNodeAssert isElement() {
    xmls.failNotElementBut(info, type());
    return null;
  }

  @Override
  public XmlNodeAssert isAttribute() {
    xmls.failNotAttributeBut(info, type());
    return null;
  }
  
  @Override
  public XmlNodeAssert isComment() {
    xmls.failNotCommentBut(info, type());
    return null;
  }

  @Override
  public XmlNodeAssert isTextNode() {
    xmls.failNotTextNodeBut(info, type());
    return null;
  }
  
  @Override
  public XmlNodeAssert isEqualTo(String expectedXml) {
    xmls.assertEqual(info, actual, expectedXml);
    return myself;
  }
}
