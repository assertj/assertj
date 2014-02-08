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

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.w3c.dom.Node;

/**
 * Tests for <code>{@link XmlUtil#areEqual(Node, Node)}</code>.
 *
 * @author Michał Piotrkowski
 */
public class XmlUtil_areEqual_Test {

  @Test
  public void should_be_true_if_differ_only_on_white_spaces() throws Exception {

    // given:
    Node node1 = XmlUtil.parseNode("<element> <c>abc</c></element>");
    Node node2 = XmlUtil.parseNode(" <element><c>abc</c>  </element>");
    
    // when:
    boolean areEqual = XmlUtil.areEqual(node1, node2);

    // then:
    assertThat(areEqual).isTrue();
  }
  
  @Test
  public void should_be_true_if_differ_only_on_white_spaces_2() throws Exception {
    
    // given:
    Node node1 = XmlUtil.parseNode("<element> <c> <d/></c></element>");
    Node node2 = XmlUtil.parseNode("<element><c><d/></c></element>");
    
    // when:
    boolean areEqual = XmlUtil.areEqual(node1, node2);
    
    // then:
    assertThat(areEqual).isTrue();
  }

  @Test
  public void should_be_false_if_differ_only_on_white_spaces_in_text_nodes() throws Exception {
    
    // given:
    Node node1 = XmlUtil.parseNode("<element><c>abc</c></element>");
    Node node2 = XmlUtil.parseNode("<element><c>abc </c></element>");
    
    // when:
    boolean areEqual = XmlUtil.areEqual(node1, node2);
    
    // then:
    assertThat(areEqual).isFalse();
  }
  
  @Test
  public void should_be_true_if_differ_only_on_quotation_marks() throws Exception {
    
    // given:
    Node node1 = XmlUtil.parseNode("@attribute='value'");
    Node node2 = XmlUtil.parseNode("@attribute=\"value\"");
    
    // when:
    boolean areEqual = XmlUtil.areEqual(node1, node2);
    
    // then:
    assertThat(areEqual).isTrue();
  }
  
  @Test
  public void should_be_true_if_differ_only_on_closing_style() throws Exception {
    
    // given:
    Node node1 = XmlUtil.parseNode("<element></element>");
    Node node2 = XmlUtil.parseNode("<element/>");
    
    // when:
    boolean areEqual = XmlUtil.areEqual(node1, node2);
    
    // then:
    assertThat(areEqual).isTrue();
  }
  
}
