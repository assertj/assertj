/*
 * Created on Feb 8, 2014
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance
 * with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
 * language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2010-2013 the original author or authors.
 */
package org.assertj.core.util.xml;

/**
 * Tests for <code>{@link XmlUtil#parseNode(String)}</code>.
 *
 * @author Michał Piotrkowski
 */
import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.test.ExpectedException;
import org.junit.Rule;
import org.junit.Test;
import org.w3c.dom.Node;

public class XmlUtil_parseNode_Test {

  @Rule
  public ExpectedException thrown = ExpectedException.none();
  
  @Test
  public void should_parse_simple_element() throws Exception {

    // given:
    // when:
    Node node = XmlUtil.parseNode("<element>this is element</element>");
    
    // then:
    assertThat(node.getNodeType()).isEqualTo(Node.ELEMENT_NODE);
  }
  
  @Test
  public void should_parse_nested_element() throws Exception {
    
    // given:
    // when:
    Node node = XmlUtil.parseNode("<complexElement><element>abc</element></complexElement>");
    
    // then:
    assertThat(node.getNodeType()).isEqualTo(Node.ELEMENT_NODE);
  }
  
  @Test
  public void should_parse_attribute() throws Exception {
    
    // given:
    // when:
    Node node = XmlUtil.parseNode("@attribute='value'");
    
    // then:
    assertThat(node.getNodeType()).isEqualTo(Node.ATTRIBUTE_NODE);
  }
  
  @Test
  public void should_parse_attribute_double_quotation() throws Exception {
    
    // given:
    // when:
    Node node = XmlUtil.parseNode("@attribute=\"value\"");
    
    // then:
    assertThat(node.getNodeType()).isEqualTo(Node.ATTRIBUTE_NODE);
  }
  
  @Test
  public void should_parse_comment() throws Exception {
    
    // given:
    // when:
    Node node = XmlUtil.parseNode("<!-- some comment -->");
    
    // then:
    assertThat(node.getNodeType()).isEqualTo(Node.COMMENT_NODE);
  }

  @Test
  public void should_parse_text_node() throws Exception {
    
    // given:
    // when:
    Node node = XmlUtil.parseNode("text");
    
    // then:
    assertThat(node.getNodeType()).isEqualTo(Node.TEXT_NODE);
  }

  @Test
  public void should_fail_meaningfully_on_invalid_format() throws Exception {
    
    // expect:
    thrown.expectIllegalArgumentException("Invalid format of '@text<>'!");
    
    // when:
    XmlUtil.parseNode("@text<>");
  }
}

