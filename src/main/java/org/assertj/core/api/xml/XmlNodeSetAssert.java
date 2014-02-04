package org.assertj.core.api.xml;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.internal.Xmls;
import org.assertj.core.util.xml.XPathExtractor;
import org.w3c.dom.NodeList;

public class XmlNodeSetAssert extends AbstractAssert<XmlNodeSetAssert, CharSequence> {

  private Xmls xmls = Xmls.instance();

  private NodeList nodeList;

  public XmlNodeSetAssert(CharSequence actual) {
    super(actual, XmlNodeSetAssert.class);
    xmls.assertIsXml(info, actual);
  }

  public XmlNodeSetAssert extractingXPath(String xpath) {

    nodeList = new XPathExtractor(actual.toString()).extract(xpath);
    return this;
  }

  public void isEmpty() {
    xmls.assertIsEmpty(info, nodeList);
  }

  public void hasSize(int expectedSize) {
    xmls.assertHasSize(info, nodeList, expectedSize);
  }

}
