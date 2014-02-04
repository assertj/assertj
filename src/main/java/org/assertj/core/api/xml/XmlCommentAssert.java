package org.assertj.core.api.xml;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Node;

public class XmlCommentAssert extends AbstractXmlNodeAssert {

  private static final Pattern COMMENT_FORMAT = Pattern.compile("^<!--(.*)-->$");
  
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
    
    Matcher matcher = COMMENT_FORMAT.matcher(expectedXml);
    
    if(!matcher.matches()){
      throw new IllegalArgumentException(String.format("Invalid format of expected comment:<\"%s\">. Use: <!-- comment-body --> instead.", expectedXml));
    }
    
    xmls.assertCommentEqual(info, actual, matcher.group(1));
  }
  
}
