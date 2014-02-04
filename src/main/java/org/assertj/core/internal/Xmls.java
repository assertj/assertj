package org.assertj.core.internal;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.WritableAssertionInfo;
import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.core.error.ShouldBeEmpty;
import org.assertj.core.error.ShouldBeXml;
import org.assertj.core.error.ShouldBeXmlElement;
import org.assertj.core.util.xml.XmlStringPrettyFormatter;
import org.assertj.core.util.xml.XmlUtil;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Xmls {

  private Objects objects = Objects.instance();
  private Failures failures = Failures.instance();

  public NodeList asXml(CharSequence actual) {
    return XmlUtil.nodeList(XmlStringPrettyFormatter.toXmlDocument(actual.toString()));
  }

  public static Xmls instance() {
    return new Xmls();
  }

  public void assertHasSize(AssertionInfo info, NodeList nodeList, int expectedSize) {
    CommonValidations.checkSizes(prettyXml(nodeList), nodeList.getLength(), expectedSize, info);
  }

  public void assertIsEmpty(AssertionInfo info, NodeList nodeList) throws AssertionError {
    if (nodeList.getLength() != 0)
      throw failures.failure(info, ShouldBeEmpty.shouldBeEmpty(prettyXml(nodeList)));
  }

  private String prettyXml(NodeList nodeList) {
    List<String> list = new ArrayList<String>();
    for (int i = 0; i < nodeList.getLength(); i++) {
      list.add(XmlStringPrettyFormatter.prettyFormat(nodeList.item(i)).trim());
    }
    return list.toString();
  }

  public NodeList assertIsXml(AssertionInfo info, CharSequence actual)
      throws AssertionError {
    objects.assertNotNull(info, actual);
    try {
      return asXml(actual);
    } catch (Exception e) {
      throw failures.failure(info, shouldBeXml(actual));
    }
  }
  
  private ErrorMessageFactory shouldBeXml(CharSequence actual) {
    return new ShouldBeXml(actual);
  }
  
  private ErrorMessageFactory shouldBeElementBut(String reason) {
    return new ShouldBeXmlElement(reason);
  }

  public void assertIsElement(AssertionInfo info, NodeList actual) {

    if (actual.getLength() > 1 ){
      throw failures.failure(info, shouldBeElementBut("multiple were found"));
    } else if(actual.getLength() < 1){
      throw failures.failure(info, shouldBeElementBut("no elements found"));
    }

  }

  public void failNotElementBut(AssertionInfo info, String reason) {
    throw failures.failure(info, shouldBeElementBut(reason));
  }


}
