package org.assertj.core.api.xml;


public interface XmlNodeAssert {

  public XmlNodeAssert isElement();
  
  public XmlNodeAssert isAttribute();
  
  public XmlNodeAssert isComment();
  
  public XmlNodeAssert isTextNode();

  public XmlNodeAssert isEqualTo(String expectedXml);

}
