package org.assertj.core.internal;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.core.error.ShouldBeEmpty;
import org.assertj.core.error.ShouldBeXml;
import org.assertj.core.util.xml.XmlStringPrettyFormatter;
import org.w3c.dom.NodeList;

public class Xmls {

  private Objects objects = Objects.instance();
  private Failures failures = Failures.instance();

  public void asXml(CharSequence actual) {
    XmlStringPrettyFormatter.toXmlDocument(actual.toString());
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

  public void assertIsXml(AssertionInfo info, CharSequence actual)
      throws AssertionError {
    objects.assertNotNull(info, actual);
    try {
      asXml(actual);
    } catch (Exception e) {
      throw failures.failure(info, shouldBeXml(actual));
    }
  }
  
  private ErrorMessageFactory shouldBeXml(CharSequence actual) {
    return new ShouldBeXml(actual);
  }

}
