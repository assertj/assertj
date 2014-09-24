/*
 * Created on Feb 04, 2014
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.xml.XmlNodeAssertions.assertFor;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.assertj.core.test.ExpectedException;
import org.junit.Rule;
import org.junit.Test;
import org.w3c.dom.Node;

/**
 * Tests for {@link XmlNodeAssertions#assertFor(org.w3c.dom.Node)}.  
 * 
 * @author Michał Piotrkowski
 */
public class XmlNodeAssertions_assertFor_Test {

  @Rule
  public ExpectedException thrown = ExpectedException.none();
  
  @Test
  public void should_create_assertion_for_element() throws Exception {

    assertThat(assertFor(nodeOfType(Node.ELEMENT_NODE))).isInstanceOf(XmlElementAssert.class);
    
  }

  @Test
  public void should_create_assertion_for_text_node() throws Exception {
    
    assertThat(assertFor(nodeOfType(Node.TEXT_NODE))).isInstanceOf(XmlTextNodeAssert.class);
    
  }
  
  @Test
  public void should_create_assertion_for_attribute() throws Exception {
    
    assertThat(assertFor(nodeOfType(Node.ATTRIBUTE_NODE))).isInstanceOf(XmlAttributeAssert.class);
    
  }
  
  @Test
  public void should_create_assertion_for_comment() throws Exception {
    
    assertThat(assertFor(nodeOfType(Node.COMMENT_NODE))).isInstanceOf(XmlCommentAssert.class);
    
  }

  @Test
  public void should_fail_meaninggully_for_unsupported_node_type() throws Exception {
    
    thrown.expectIllegalArgumentException("This node type <-1> is not supported!");
    
    assertFor(nodeOfType((short)-1));
    
  }
  
  // --
  
  private Node nodeOfType(short type) {
    Node nodeMock = mock(Node.class);
    when(nodeMock.getNodeType()).thenReturn(type);
    return nodeMock;
  }
  
}
