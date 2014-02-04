package org.assertj.core.util.xml;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class XPathExtractor {

  private Document xml;

  public XPathExtractor(String actual) {
    xml = XmlStringPrettyFormatter.toXmlDocument(actual);
  }

  public NodeList extract(String xpath) {
    
    XPath path = XPathFactory.newInstance().newXPath();
    try {
      return (NodeList) path.compile(xpath).evaluate(xml, XPathConstants.NODESET);
    } catch (XPathExpressionException e) {
      throw new RuntimeException(e);
    }
  }

}
