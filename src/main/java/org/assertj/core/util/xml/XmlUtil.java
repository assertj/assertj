package org.assertj.core.util.xml;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlUtil {

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

}
