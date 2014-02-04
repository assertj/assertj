package org.assertj.core.api.xml;

public interface XmlNodeAssert {

  public void isElement();
  
  public void isAttribute();
  
  public void isComment();
  
  public void isTextNode();

  public void isEqualTo(String expectedXml);

}
