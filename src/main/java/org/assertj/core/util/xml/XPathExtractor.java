/*
 * Created on Feb 08, 2014
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 * Copyright @2010-2014 the original author or authors.
 */
package org.assertj.core.util.xml;

import static java.lang.String.format;
import static org.assertj.core.util.xml.XmlUtil.nodeList;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.assertj.core.util.Preconditions;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Helper class for executing xpath queries.  
 * 
 * @author Michał Piotrkowski
 */
public class XPathExtractor {

  private class AggregateNodeList implements NodeList {

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
        list.append(extractNodeList(xpath, path, x));
      }
      return list;
      
    } catch (XPathExpressionException e) {

      throw new IllegalArgumentException(format("Invalid xpath:<\"%s\">", xpath));
    }
  }

  public NodeList extractNodeList(String xpath, XPath path, Node x) throws XPathExpressionException {

    NodeList result = (NodeList) path.compile(xpath).evaluate(x, XPathConstants.NODESET);
    
    final List<Node> copy = new ArrayList<Node>();
    
    for (int i=0; i<result.getLength(); i++) {
      copy.add(cloneToSeparateDocument(result.item(i)));
    }

    return new NodeList() {
      
      @Override
      public Node item(int index) {
        return copy.get(index);
      }
      
      @Override
      public int getLength() {
        return copy.size();
      }
    };
  }

  private Node cloneToSeparateDocument(Node n) {

    if(n.getNodeType() == Node.DOCUMENT_NODE){
      n = ((Document)n).getDocumentElement();   // document cannot be imported, import document element instead
    }
    
    try {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbf.newDocumentBuilder();
      Document document = db.newDocument();
            
      Node copy = document.importNode(n, true);
      
      if(n.getNodeType() == Node.ELEMENT_NODE){
        document.appendChild(copy);
      }
      
      return copy;
      
    } catch (ParserConfigurationException e) {
      throw new RuntimeException(e);
    }
  }

}
