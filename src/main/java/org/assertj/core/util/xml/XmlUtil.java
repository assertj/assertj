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

import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

/**
 * Helper class with convenient methods for:
 * <ul>
 *  <li>parsing,</li>
 *  <li>printing,</li> 
 *  <li>comparing</li>
 * </ul>
 * xml documents.  
 * 
 * @author Michał Piotrkowski
 */
public class XmlUtil {

  private static final Pattern ATTRIBUTE_FORMAT = Pattern.compile("^@([^=]+)=(?:'|\")(.*)(?:'|\")$");
  
  private XmlUtil() {
    // utility class 
  }
  
  public static NodeList nodeList(final Node node) {
    
    return new NodeList() {
      
      @Override
      public Node item(int index) {
        return node;
      }
      
      @Override
      public int getLength() {
        return 1;
      }
    };
  }

  public static Node parseNode(String xmlString) {
    
    try {

      xmlString = xmlString.trim();

      Matcher matcher = ATTRIBUTE_FORMAT.matcher(xmlString);
      if(matcher.matches()){
        return parseAttribute(matcher);
      }
    
      String wrappedXml = String.format("<wrapper>%s</wrapper>", xmlString);
      return unwrap(wrappedXml).getFirstChild();
      
    } catch (Exception e) {
      throw new IllegalArgumentException(String.format("Invalid format of '%s'!", xmlString), e);
    }
  }

  private static Node parseAttribute(Matcher matcher) throws Exception {
    
    String attributeName = matcher.group(1);  
    String attributeValue = matcher.group(2);  
    String wrappedXml = String.format("<wrapper %s=\"%s\"/>", attributeName, attributeValue);
    return unwrap(wrappedXml).getAttributes().item(0);
  }

  public static Node unwrap(String wrappedXml) throws Exception {
    return doParse(wrappedXml);
  }
  
  public static Node doParse(String xmlString) throws Exception {

    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = dbf.newDocumentBuilder();
    Document document = db.parse(new InputSource(new StringReader(xmlString)));
    
    // normalize xml document by removing text nodes with whitespaces only  
    removeEmptyTextNodes(document.getDocumentElement());
    
    return document.getDocumentElement();
      
  }
  
  public static void removeEmptyTextNodes(Element e) {
    
    NodeList children = e.getChildNodes();

    // counting backwards to not to miss nodes just after removal 
    for (int i = children.getLength() - 1; i >= 0 ; i--) {
      
      Node child = children.item(i);
      
      if (child instanceof Text) {
        Text text = (Text) child;
        if("".equals(text.getData().trim())){
          e.removeChild(text);
          continue;
        }
      } 
      
      if (child instanceof Element) {
        removeEmptyTextNodes((Element) child);
      }
    }
  }

  public static boolean areEqual(Node left, Node right) {
    
    return left.isEqualNode(right);
  }

  public static String toStringOf(Node node) {
    
    if(node.getNodeType() == Node.ATTRIBUTE_NODE){
      return String.format("@%s=\"%s\"", node.getNodeName(), node.getNodeValue());
    }
    
    return "\n" + XmlStringPrettyFormatter.prettyFormat(node);
  }

  public static Node toXml(String xmlString) {
    try {
      return doParse(xmlString);
    } catch (Exception e) {
      throw new RuntimeException(XmlStringPrettyFormatter.PARSE_ERROR, e);
    }
  }

}
