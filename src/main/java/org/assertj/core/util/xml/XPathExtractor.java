package org.assertj.core.util.xml;

import static java.lang.String.format;
import static org.assertj.core.util.xml.XmlUtil.nodeList;

import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.assertj.core.util.Preconditions;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XPathExtractor {

  public class AggregateNodeList implements NodeList {

    private List<NodeList> delegates = new ArrayList<NodeList>();
    
    public void append(NodeList n){
      delegates.add(n);
    }
    
    @Override
    public Node item(int index) {
      
      int effectiveIndex = index;
      
      for (NodeList n : delegates) {
        if(n.getLength() > effectiveIndex){
          return n.item(effectiveIndex);
        }
        effectiveIndex -= n.getLength();
      }
      
      return null;
    }

    @Override
    public int getLength() {
      int sum = 0;
      for (NodeList n : delegates) {
        sum += n.getLength();
      }
      return sum;
    }
  }

  private NodeList xml;

  public XPathExtractor(String actual) {
    this(nodeList(XmlUtil.toXml(actual)));
  }

  public XPathExtractor(NodeList actual) {
    xml = actual;
  }

  public NodeList extract(String xpath) {

    Preconditions.checkNotNullOrEmpty(xpath, "XPath expression cannot be empty!");
    
    XPath path = XPathFactory.newInstance().newXPath();
    try {
      
      AggregateNodeList list = new AggregateNodeList();
      
      for (int index=0; index<xml.getLength(); index++) {
        Node x = xml.item(index);
        list.append((NodeList) path.compile(xpath).evaluate(x, XPathConstants.NODESET));
      }
      return list;
      
    } catch (XPathExpressionException e) {

      throw new IllegalArgumentException(format("Invalid xpath:<\"%s\">", xpath));
    }
  }

}
