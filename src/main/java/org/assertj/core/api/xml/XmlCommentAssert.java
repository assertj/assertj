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
  public XmlNodeAssert isComment() {

    // success
    return myself;
  }
    
}
