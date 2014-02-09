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

      if(ATTRIBUTE_FORMAT.matcher(xmlString).matches()){
        return parseAttribute(xmlString);
      }
    
      String wrappedXml = String.format("<wrapper>%s</wrapper>", xmlString);
      return unwrap(wrappedXml).getFirstChild();
      
    } catch (Exception e) {
      throw new IllegalArgumentException(String.format("Invalid format of '%s'!", xmlString), e);
    }
  }

  private static Node parseAttribute(String xmlString) throws Exception {
    
    Matcher matcher = ATTRIBUTE_FORMAT.matcher(xmlString);
    if(matcher.matches()){
      String attributeName = matcher.group(1);  
      String attributeValue = matcher.group(2);  
      String wrappedXml = String.format("<wrapper %s=\"%s\"/>", attributeName, attributeValue);
      return unwrap(wrappedXml).getAttributes().item(0);
    }
      
    return null;
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
