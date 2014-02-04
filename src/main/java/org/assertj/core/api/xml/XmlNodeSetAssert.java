package org.assertj.core.api.xml;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.internal.Xmls;
import org.assertj.core.util.xml.XPathExtractor;
import org.w3c.dom.NodeList;

public class XmlNodeSetAssert extends AbstractAssert<XmlNodeSetAssert, NodeList> {

  private Xmls xmls = Xmls.instance();

  public XmlNodeSetAssert(NodeList nodeList) {
    super(nodeList, XmlNodeSetAssert.class);
  }

  public XmlNodeSetAssert extractingXPath(String xpath) {
    return new XmlNodeSetAssert(new XPathExtractor(actual).extract(xpath));
  }

  public void isEmpty() {
    xmls.assertIsEmpty(info, actual);
  }

  public void hasSize(int expectedSize) {
    xmls.assertHasSize(info, actual, expectedSize);
  }

}
