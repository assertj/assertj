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
package org.assertj.core.api.xml;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Node;

/**
 * Helper class with factory method for creating proper instance of {@link XmlNodeAssert}.  
 * 
 * @author Michał Piotrkowski
 */
class XmlNodeAssertions {

  @SuppressWarnings("serial")
  private static final Map<Short, Class<? extends XmlNodeAssert>> SUPPORTED_ASSERTIONS = new HashMap<Short, Class<? extends XmlNodeAssert>>(){{
    put(Node.ATTRIBUTE_NODE, XmlAttributeAssert.class);
    put(Node.TEXT_NODE, XmlTextNodeAssert.class);
    put(Node.ELEMENT_NODE, XmlElementAssert.class);
    put(Node.COMMENT_NODE, XmlCommentAssert.class);
  }};

  private XmlNodeAssertions() {
    // utility class
  }

  /**
   * Creates proper assertion for given node based on node.getNodeType().
   * 
   * @param node for which assertion should be created
   * @return assertion proper to given node type
   */
  static XmlNodeAssert assertFor(Node node) {
    
    try {
      
      return SUPPORTED_ASSERTIONS.get(node.getNodeType()).getConstructor(Node.class).newInstance(node);
      
    } catch (Exception e) {
      throw new IllegalArgumentException(String.format("This node type <%s> is not supported!", node.getNodeType()), e);
    }
  }
  
}
