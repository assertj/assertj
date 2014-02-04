package org.assertj.core.api.xml;

import org.w3c.dom.Node;

public class XmlCommentAssert extends AbstractXmlNodeAssert {

  public XmlCommentAssert(Node actual) {
    super(actual, XmlCommentAssert.class);
  }

  @Override
  protected String type() {
    return "comment";
  }
  
  @Override
  public void isComment() {
    // success
  }
  
  @Override
  public void isEqualTo(String expectedXml) {
    xmls.assertCommentEqual(info, actual, expectedXml);
  }
  
}
