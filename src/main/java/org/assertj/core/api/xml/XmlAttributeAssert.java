package org.assertj.core.api.xml;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Node;

public class XmlAttributeAssert extends AbstractXmlNodeAssert {

  public XmlAttributeAssert(Node actual) {
    super(actual, XmlAttributeAssert.class);
  }

  @Override
  protected String type() {
    return "attribute";
  }
  
  @Override
  public void isAttribute() {
    // success
  }

  @Override
  public void isEqualTo(String expected) {
    
    Matcher matcher = Pattern.compile("^@([^=]+)='(.*)'$").matcher(expected);

    matcher.matches();
    
    String attributeName = matcher.group(1);
    String attributeValue = matcher.group(2);
    
    xmls.assertAttributeEqual(info, actual, attributeName, attributeValue);
  }
}
