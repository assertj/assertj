package org.assertj.core.internal;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.error.AssertionErrorFactory;
import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.core.error.ShouldBeEmpty;
import org.assertj.core.error.ShouldBeEqual;
import org.assertj.core.error.ShouldBeSingleXmlNode;
import org.assertj.core.error.ShouldBeXml;
import org.assertj.core.error.ShouldBeXmlAttribute;
import org.assertj.core.error.ShouldBeXmlComment;
import org.assertj.core.error.ShouldBeXmlElement;
import org.assertj.core.error.ShouldBeXmlTextNode;
import org.assertj.core.util.Preconditions;
import org.assertj.core.util.xml.XmlStringPrettyFormatter;
import org.assertj.core.util.xml.XmlUtil;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Xmls {

  private Objects objects = Objects.instance();
  private Failures failures = Failures.instance();
  private Iterables iterables = new Iterables(new StandardComparisonStrategy(){
    
    public boolean areEqual(Object actual, Object other) {
      
      if(actual instanceof Node){
        Node left = (Node) actual;
        Node right = (Node) other;
        return XmlUtil.areEqual(left, right);
      }
      return super.areEqual(actual, other);
    };
    
  });

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
      list.add(prettyXml(nodeList.item(i)));
    }
    return list.toString();
  }

  private String prettyXml(Node node) {
    return XmlStringPrettyFormatter.prettyFormat(node).trim();
  }

  private String prettyXmlAttribute(Node node) {
    return String.format("@%s='%s'", node.getNodeName(), node.getNodeValue());
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

  private ErrorMessageFactory shouldBeAttributeBut(String reason) {
    return new ShouldBeXmlAttribute(reason);
  }

  private ErrorMessageFactory shouldBeCommentBut(String reason) {
    return new ShouldBeXmlComment(reason);
  }
  
  private ErrorMessageFactory shouldBeTextNodeBut(String reason) {
    return new ShouldBeXmlTextNode(reason);
  }
  
  private ErrorMessageFactory shouldBeSingleXmlNodeBut(String reason) {
    return new ShouldBeSingleXmlNode(reason);
  }

  private AssertionErrorFactory shouldBeEqual(Node actual, Node expected, AssertionInfo info) {
    return shouldBeEqual(prettyXml(actual), prettyXml(expected), info);
  }

  private AssertionErrorFactory shouldBeEqual(String actual, String expected, AssertionInfo info) {
    return ShouldBeEqual.shouldBeEqual(actual, expected, info.representation());
  }
  
  public void assertIsSingleNode(AssertionInfo info, NodeList actual) {

    if (actual.getLength() > 1 ){
      throw failures.failure(info, shouldBeSingleXmlNodeBut("multiple nodes"));
    } else if(actual.getLength() < 1){
      throw failures.failure(info, shouldBeSingleXmlNodeBut("no nodes"));
    }
  }

  public void failNotElementBut(AssertionInfo info, String reason) {
    throw failures.failure(info, shouldBeElementBut(reason));
  }

  public void failNotAttributeBut(AssertionInfo info, String reason) {
    throw failures.failure(info, shouldBeAttributeBut(reason));
  }

  public void failNotCommentBut(AssertionInfo info, String reason) {
    throw failures.failure(info, shouldBeCommentBut(reason));
  }

  public void failNotTextNodeBut(AssertionInfo info, String reason) {
    throw failures.failure(info, shouldBeTextNodeBut(reason));
  }

  public void assertEqual(AssertionInfo info, Node actual, String expectedXml) {
    Node expected = asXml(expectedXml).item(0).getFirstChild();
    if(!actual.isEqualNode(expected)){
      throw failures.failure(info, shouldBeEqual(actual, expected, info));
    }
  }

  public void assertAttributeEqual(AssertionInfo info, Node actual, String attributeName, String attributeValue) {
    Node expectedAttribute = asXml(String.format("<element %s=\"%s\"/>",attributeName, attributeValue)).item(0).getFirstChild().getAttributes().item(0);
    if(!actual.isEqualNode(expectedAttribute)){
      throw failures.failure(info, shouldBeEqual(prettyXmlAttribute(actual), prettyXmlAttribute(expectedAttribute), info));
    }
  }

  public void assertTextNodeEqual(AssertionInfo info, Node actual, String expectedText) {
    Node expectedTextNode = asXml(String.format("<element>%s</element>", expectedText)).item(0).getFirstChild().getFirstChild();
    if(!actual.isEqualNode(expectedTextNode)){
      throw failures.failure(info, shouldBeEqual(actual.getTextContent(), expectedText, info));
    }
  }

  public void assertCommentEqual(AssertionInfo info, Node actual, String expectedComment) {
    Node expectedCommentNode = asXml(String.format("<element>%s</element>", comment(expectedComment))).item(0).getFirstChild().getFirstChild();
    if(!actual.isEqualNode(expectedCommentNode)){
      throw failures.failure(info, shouldBeEqual(comment(actual.getNodeValue()), comment(expectedComment), info));
    }
  }

  public String comment(String body) {
    return String.format("<!--%s-->", body);
  }

  public void assertContains(AssertionInfo info, NodeList actual, String... nodes) {
    
    Preconditions.checkNotNull(nodes, "Expected node cannot be null!");

    List<Node> actualNodes = actualNodes(actual);
    List<Node> expectedNodes = expectedNodes(nodes);
    
    iterables.assertContains(info, actualNodes, expectedNodes.toArray());
    
  }

  public List<Node> expectedNodes(String... nodes) {
   
    List<Node> expectedNodes = new ArrayList<Node>();
   
    for (String xml : nodes) {
      Preconditions.checkNotNull(xml, "Expected node cannot be null!");
      expectedNodes.add(XmlUtil.parseNode(xml));
    }
    return expectedNodes;
  }

  private List<Node> actualNodes(NodeList nodeList) {

    List<Node> result = new ArrayList<Node>();
    
    for(int i=0; i<nodeList.getLength(); i++){
      result.add(nodeList.item(i));
    }
    return result;
  }

}
